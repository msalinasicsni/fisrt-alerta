package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.muestra.Estudio_UnidadSalud;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.UtilityProperties;
import ni.gob.minsa.ciportal.dto.*;
import ni.gob.minsa.ciportal.servicios.PortalService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel Salinas on 10/28/2014.
 * v 1.0
 */
@Service("seguridadService")
@Transactional
public class SeguridadService {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    UtilityProperties utilityProperties = new UtilityProperties();

    /**
     * Retorna valor de constante que indica que se habilita o no la seguridad en el sistema
     * @return True: seguridad habilitada, False: Seguridad deshabilitada
     */
    public boolean seguridadHabilitada(){
        return ConstantsSecurity.ENABLE_SECURITY;
    }

    /**
     * M�todo que obtiene la informaci�n del login activo
     * @param pBdSessionId nombre del cockie establecido por el sistema de seguridad del MINSA
     * @return InfoSesion de la sesi�n actual
     */
    private InfoSesion obtenerInfoSesion(String pBdSessionId) {
        InfoSesion infoSesion = null;

        try{

            InitialContext ctx = new InitialContext();

            PortalService portalService = (PortalService)ctx.lookup(ConstantsSecurity.EJB_BIN);
            InfoResultado infoResultado = portalService.obtenerInfoSesion(pBdSessionId);

            if(infoResultado!=null){
                if(infoResultado.isOk()){
                    infoSesion = (InfoSesion) infoResultado.getObjeto();
                }
            }
            //Desde aca
            //infoSesion = new InfoSesion();
            //usuario produccion
            /*infoSesion.setUsuarioId(5859);
            infoSesion.setNombre("ALERTA MANAGUA SILAIS");
            infoSesion.setUsername("alerta-managua");
            infoSesion.setSistemaSesion("ALERTA");*/
            //usuario de pruebas
            /*infoSesion.setUsuarioId(4791);
            infoSesion.setNombre("Juan Marcio Palacios");
            infoSesion.setUsername("jpalacios");
            infoSesion.setSistemaSesion("ALERTA");
            */
            /*infoSesion.setUsuarioId(4772);
            infoSesion.setNombre("Admin Alerta");
            infoSesion.setUsername("admalerta");
            infoSesion.setSistemaSesion("ALERTA");*/
            //usuario de desarrollo
            /*infoSesion.setUsuarioId(170);
            infoSesion.setNombre("Adm Alerta");
            infoSesion.setUsername("alerta");
            infoSesion.setSistemaSesion("ALERTA");*/
            //hasta aca
            ctx.close();
        }catch(Exception e){
            System.out.println("---- EXCEPTION");
            System.out.println("Error no controlado: " + e.toString());
        }

        return infoSesion;
    }

    /**
     * M�todo que ejecuta el servicio del portal para obtener la url de inicio del portal del MINSA
     * @return String con url de incio del MINSA
     */
    public String obtenerUrlPortal() {
        String urlPortal;

        try{
            InitialContext ctx = new InitialContext();

            PortalService portalService = (PortalService)ctx.lookup(ConstantsSecurity.EJB_BIN);
            urlPortal = portalService.obtenerUrlLogin();
            ctx.close();
        }catch(NamingException e){
            urlPortal = "404";
        }

        return urlPortal;
    }

    /**
     * M�todo que valida si es correcto el login en el sistema
     * @param request petici�n actual
     * @return String vacio "" si login es correcto, en caso contrario url de login del portal del minsa
     */
    public String validarLogin(HttpServletRequest request){
        String urlRetorno="";
        if (seguridadHabilitada()) { //Si es false no se realiza ninguna validaci�n
            if (!esUsuarioAutenticado(request.getSession())) {
                String bdSessionId = "";  // esta variable dejarla en blanco par activar la seguridad
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        if (cookies[i].getName().equalsIgnoreCase(ConstantsSecurity.COOKIE_NAME)) {
                            bdSessionId = cookies[i].getValue();
                        }
                    }
                }
                if (!bdSessionId.equals("")) {
                    InfoSesion infoSesion;
                    if (request.getSession().getAttribute("infoSesionActual") == null) {
                        infoSesion = obtenerInfoSesion(bdSessionId);

                    } else {
                        infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");
                    }
                    if (infoSesion != null) {
                        request.getSession().setAttribute("infoSesionActual", infoSesion);
                    }else {
                        urlRetorno = "redirect:" + obtenerUrlPortal();
                    }
                } else {
                    urlRetorno = "redirect:" + obtenerUrlPortal();
                }
            }
        }
        return urlRetorno;
    }

    /**
     * M�todo que valida si el usuario logueado tiene acceso a la vista solicitada
     * @param request reques actual
     * @param codSistema c�digo de sistema actual
     * @param hayParametro TRUE indica que en el contextPath el �ltimo elemento es un p�rametro de spring, FALSE no hay par�metro
     * @return String vacio "" si tiene autorizaci�n, si no tiene retorna url de acceso denegado
     */
    public String validarAutorizacionUsuario(HttpServletRequest request, String codSistema, boolean hayParametro){
        String urlRetorno="";
        if (seguridadHabilitada()) { //Si es false no se realiza ninguna validaci�n
            boolean autorizado;
            InfoSesion infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");

            if (infoSesion != null) {
                String pViewId = request.getServletPath();
                if (hayParametro) // indica que el �ltimo componente de la url es un par�metro de spring, por lo tanto no se debe tomar en cuenta al validar autorizaci�n
                    pViewId = pViewId.substring(0, pViewId.lastIndexOf("/"));
                autorizado = esUsuarioAutorizado(infoSesion.getUsuarioId(), codSistema, pViewId);
                if (!autorizado) {
                    urlRetorno = "403";
                }
            } else {
                urlRetorno = "redirect:" + obtenerUrlPortal();
            }
        }
        return urlRetorno;
    }

    /**
     * M�todo que determina si la sesi�n que contiene la informaci�n del usuario aunticada existe, es decir hay usuario autenticado
     * @param session sesi�n actual
     * @return TRUE si existe sessi�n, False en caso contrario
     */
    private boolean esUsuarioAutenticado(HttpSession session) {
        return session.getAttribute("infoSesionActual")!=null;
    }

    /**
     * M�todo que consume el portal de seguridad para determinar si un usario determinado tiene autorizaci�n se ingresar a una vista determinada, en el sistema actual
     * @param pUsuarioId id del usuario autenticado
     * @param pSistema c�digo del sistema actual, ALERTA
     * @param pViewId url de la vista solicitada
     * @return True si el usuario tiene permiso, False en caso contrario
     */
    private boolean esUsuarioAutorizado(long pUsuarioId, String pSistema, String pViewId) {
        boolean autorizado = false;
        try {
            InitialContext ctx = new InitialContext();
            PortalService portalService = (PortalService) ctx.lookup(ConstantsSecurity.EJB_BIN);
            if (portalService != null) {
                autorizado = portalService.esUsuarioAutorizado(pUsuarioId, pViewId, pSistema);
            }
            ctx.close();
        } catch (Exception e) {
            autorizado = false;
        }
        return autorizado;
    }

    /**
     * M�todo que determina si un usuario determinado esta configurado como usario de nivel central en el sistema
     * @param pUsuarioId id del usuario autenticado
     * @param pSistema c�digo del sistema actual, ALERTA
     * @return TRUE: si es de nivel central  o la seguridad esta deshabilitada, FALSE: no es nivel central o sucedi� un error
     */
    public boolean esUsuarioNivelCentral(long pUsuarioId, String pSistema) {
        boolean nivelCentral=false;
        if (seguridadHabilitada()) {
            try {
                InitialContext ctx = new InitialContext();
                PortalService portalService = (PortalService) ctx.lookup(ConstantsSecurity.EJB_BIN);

                if (portalService != null) {
                    nivelCentral = portalService.esUsuarioNivelCentral(pUsuarioId, pSistema);
                }
                ctx.close();

            } catch (Exception e) {
                nivelCentral = false;
            }
        }
        return nivelCentral;
    }

    /**
     * M�todo que consulta la sessi�n con informaci�n del usuario y obtiene el id el usuario auntenticado
     * @param request petici�n actual
     * @return long con Id del usuario almacenado en sesi�n o O si no se encontr�
     */
    public long obtenerIdUsuario(HttpServletRequest request){
        long idUsuario =0 ;

        if(ConstantsSecurity.ENABLE_SECURITY){
            InfoSesion infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");

            if (infoSesion != null) {
                idUsuario = infoSesion.getUsuarioId();
            }
        }/*else{
            idUsuario= 4791L; //4772L;///usuario alerta en pruebas
        }*/

        return idUsuario;
    }

    /**
     *  M�todo que consulta la sessi�n con informaci�n del usuario y obtiene el nombre el usuario auntenticado
     * @param request petici�n actual
     * @return String con el nombre del usuario auntenticado, "" si no se encontr�, por defecto alerta
     */
    public String obtenerNombreUsuario(HttpServletRequest request){
        String nombreUsuario="";
        InfoSesion infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");

        if (infoSesion != null) {
            nombreUsuario = infoSesion.getNombre();
        }else {
            if (!seguridadHabilitada())
                nombreUsuario = "alerta";
        }

        return nombreUsuario;
    }

    /**
     * M�todo que obtiene el �rbol del menu del sistema seg�n la configuraci�n en la seguridad, luego se arma el men� en un string
     * @param request petici�n actual
     * @return String que contiene el html de todas las opciones de menu
     */
    public String obtenerMenu(HttpServletRequest request){
        String menuSistema = "";
        try{
            String urlValidacion = validarLogin(request);
            if (urlValidacion.isEmpty()){
                //if (request.getSession().getAttribute("menuSistema")==null) {
                    InitialContext ctx = new InitialContext();
                    PortalService portalService = (PortalService) ctx.lookup(ConstantsSecurity.EJB_BIN);
                    long idUsuario=obtenerIdUsuario(request);
                    NodoArbol arbolMenuSistema = portalService.obtenerArbolMenu(idUsuario,ConstantsSecurity.SYSTEM_CODE);
                    String contextPath = request.getContextPath();

                    menuSistema = armarOpcionesMenu(arbolMenuSistema, contextPath);
                    //request.getSession().setAttribute("menuSistema", menuSistema);
                    ctx.close();
                //}else {
                    //menuSistema = request.getSession().getAttribute("menuSistema").toString();
                //}
            }else{
                menuSistema = "";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return menuSistema;
    }

    /**
     * M�todo que apartir de un nodoArbol que contiene el men� de la seguridad arma un string con estructura html de las opciones del menu para ser presentadas en las vistas
     * �ste m�todo es recursivo, pues se necesita acceder hasta el nivel mas bajo de la estructura (hijos)
     * @param nodoArbol Estructura de men� seg�n la seguridad
     * @param contextPath del sistema
     * @return String que contiene el html de todas las opciones de menu
     */
    public String armarOpcionesMenu(NodoArbol nodoArbol, String contextPath){
        String menu="";
        for(NodoArbol hijo: nodoArbol.hijos()){
            String nombreOpcionMenu;
            String urlOpcionMenu;
            boolean esItem = hijo.tieneHijos();
            boolean padreSinHijo = false;
            if (hijo.getDatoNodo() instanceof NodoSubmenu){
                NodoSubmenu data = (NodoSubmenu)hijo.getDatoNodo();
                nombreOpcionMenu = data.getNombre();
                data.getEstilo();
                urlOpcionMenu = null;
            }
            else{
                NodoItem data = (NodoItem)hijo.getDatoNodo();
                if (data==null){
                    padreSinHijo = true;
                    urlOpcionMenu = null;
                    nombreOpcionMenu = "";
                }else {
                    nombreOpcionMenu = data.getNombre();
                    urlOpcionMenu = data.getUrl();
                }
            }
            if (!padreSinHijo) {
                String[] dataOpcionMenu = nombreOpcionMenu.split(",");

                String desCodeMessage = "";
                if (dataOpcionMenu.length>1){
                     desCodeMessage = utilityProperties.getPropertie(dataOpcionMenu[1]);
                }
                    if (urlOpcionMenu != null || esItem) {
                        menu = menu + "<li class=\"" + dataOpcionMenu[0] + "\">\n";
                        menu = menu + " <a href=\"" + (urlOpcionMenu!=null?contextPath + urlOpcionMenu:"#") + "\" title=\"" + desCodeMessage + "\"><i class=\"fa fa-lg fa-fw " + (dataOpcionMenu.length > 2 ? dataOpcionMenu[2] : "") + "\"></i>" + (!esItem ? "" : "<span class=\"menu-item-parent\">") + desCodeMessage + (!esItem ? "" : "</span>") + "</a>\n";
                    }

            }
            if (hijo.tieneHijos()){
                menu = menu + "<ul>\n";
                menu = menu + armarOpcionesMenu(hijo, contextPath);
                menu = menu + "</ul>\n";
            }
            menu = menu + "</li>\n";
        }
        return  menu;
    }

    /**
     * M�todo que se ejecuta cuando se selecciona la opci�n "Salir" del sistema
     * @param request sesi�n actual para limpiarla
     */
    public void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.setAttribute("infoSesionActual", null);
        session.setAttribute("menuSistema", null);
        session.removeAttribute("infoSesionActual");
        session.removeAttribute("menuSistema");
        session.invalidate();
    }

    /**
     * M�todo que obtiene las entidades administrativas (SILAIS) a las que tiene autorizaci�n el usuario en el sistema
     * @param pUsuarioId id del usuario autenticado
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @return List<EntidadesAdtvas>
     */
    public List<EntidadesAdtvas> obtenerEntidadesPorUsuario(Integer pUsuarioId, String pCodigoSis){
        List<EntidadesAdtvas> entidadesAdtvasList = new ArrayList<EntidadesAdtvas>();
        try {
            String query = "select distinct ent from EntidadesAdtvas ent, UsuarioEntidad usuent, Usuarios usu, Sistema sis " +
                    "where ent.id = usuent.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuent.usuario.usuarioId and usuent.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo order by ent.nombre";
            Query qrUsuarioEntidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioEntidad.setParameter("pUsuarioId", pUsuarioId);
            qrUsuarioEntidad.setParameter("pCodigoSis", pCodigoSis);
            qrUsuarioEntidad.setParameter("pasivo", '0');
            entidadesAdtvasList = qrUsuarioEntidad.list();

            //si no tiene entidades asignadas directamente, se obtienen las entidades asociadas a las unidades de salud asignadas directamente
            if (entidadesAdtvasList.size()<=0){
                query = "select distinct ent from EntidadesAdtvas ent, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                        "where ent.id = usuni.unidad.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo and usuni.unidad.pasivo = :pasivo order by ent.nombre";
                qrUsuarioEntidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioEntidad.setParameter("pUsuarioId", pUsuarioId);
                qrUsuarioEntidad.setParameter("pCodigoSis", pCodigoSis);
                qrUsuarioEntidad.setParameter("pasivo", '0');
                entidadesAdtvasList = qrUsuarioEntidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return entidadesAdtvasList;
    }

    /**
     * M�todo que valida si el usuario logueado tiene autorizaci�n sobre una entidad administrativa determinada
     * @param pUsuarioId id del usuario autenticado
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param pCodEntidad c�digo de la entidad a validar
     * @return TRUE: si tiena autorizaci�n o la seguridad esta deshabilitada, FALSE: no tiene autorizaci�n
     */
    public boolean esUsuarioAutorizadoEntidad(Integer pUsuarioId, String pCodigoSis, long pCodEntidad){
        //if (seguridadHabilitada()) {
        List<EntidadesAdtvas> entidadesAdtvasList = new ArrayList<EntidadesAdtvas>();
        try {
            String query = "select distinct ent from EntidadesAdtvas ent, UsuarioEntidad usuent, Usuarios usu, Sistema sis " +
                    "where ent.id = usuent.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuent.usuario.usuarioId and usuent.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.codigo = :pCodEntidad and ent.pasivo = :pasivo order by ent.nombre";
            Query qrUsuarioEntidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioEntidad.setParameter("pUsuarioId", pUsuarioId);
            qrUsuarioEntidad.setParameter("pCodigoSis", pCodigoSis);
            qrUsuarioEntidad.setParameter("pCodEntidad", pCodEntidad);
            qrUsuarioEntidad.setParameter("pasivo", '0');
            entidadesAdtvasList = qrUsuarioEntidad.list();

            //si no tiene entidades asignadas directamente, se obtienen las entidades asociadas a las unidades de salud asignadas directamente, y que pertenenzcan a la entidad que se valida
            if (entidadesAdtvasList.size()<=0){
                query = "select distinct ent from EntidadesAdtvas ent, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                        "where ent.id = usuni.unidad.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo and usuni.unidad.pasivo = :pasivo " +
                        "and ent.id = :pCodEntidad order by ent.nombre";
                qrUsuarioEntidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioEntidad.setParameter("pUsuarioId", pUsuarioId);
                qrUsuarioEntidad.setParameter("pCodigoSis", pCodigoSis);
                qrUsuarioEntidad.setParameter("pCodEntidad", pCodEntidad);
                qrUsuarioEntidad.setParameter("pasivo", '0');
                entidadesAdtvasList = qrUsuarioEntidad.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entidadesAdtvasList.size() > 0;
        //}else return true;
    }

    /**
     * M�todo que obtiene todas las unidades de salud a las que tiene autorizaci�n el usuario en el sistema
     * @param pUsuarioId id del usuario autenticado
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param tipoUnidades tipos de unidades a carga. Eje: Primarias , Primarias+Hospitales
     * @return List<Unidades>
     */
    public List<Unidades> obtenerUnidadesPorUsuario(Integer pUsuarioId, String pCodigoSis, String tipoUnidades){
        List<Unidades> unidadesList = new ArrayList<Unidades>();
        try {
            String query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+") " +
                    "order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas directamente al usuario, se obtienen todas las unidades del los silais asociados directamente
            if (unidadesList.size()<=0){
                query = "select uni from Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                        "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                        "order by uni.nombre";

                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId", pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis", pCodigoSis);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                unidadesList = qrUsuarioUnidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
    }

    /**
     * M�todo que valida si el usuario logueado tiene autorizaci�n sobre una unidad de salud determinada
     * @param pUsuarioId id del usuario autenticado
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param pCodUnidad c�digo de la unidad a validar
     * @return TRUE: si tiena autorizaci�n o la seguridad esta deshabilitada, FALSE: no tiene autorizaci�n
     */
    public boolean esUsuarioAutorizadoUnidad(Integer pUsuarioId, String pCodigoSis, long pCodUnidad){
        //if (seguridadHabilitada()) {
        List<Unidades> unidadesList = new ArrayList<Unidades>();
        try {
            String query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.codigo = :pCodUnidad and uni.pasivo = :pasivo " +
                    "order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId", pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis", pCodigoSis);
            qrUsuarioUnidad.setParameter("pCodUnidad", pCodUnidad);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas directamente al usuario, se obtienen todas las unidades del los silais asociados directamente
            if (unidadesList.size()<=0){
                query = "select uni from Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                        "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.codigo = :pCodUnidad and uni.pasivo = :pasivo " +
                        "order by uni.nombre";

                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId", pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis", pCodigoSis);
                qrUsuarioUnidad.setParameter("pCodUnidad", pCodUnidad);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                unidadesList = qrUsuarioUnidad.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unidadesList.size() > 0;
        //}else return true;
    }

    /**
     * M�todo que obtiene todas las unidades de salud a las que tiene autorizaci�n el usuario en el sistema seg�n el SILAIS y el tipo de Unidad
     * @param pUsuarioId id del usuario autenticado
     * @param pCodSilais C�digo del silais a filtrar
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param tipoUnidades tipos de unidades a carga. Eje: Primarias , Primarias+Hospitales
     * @return List<Unidades>
     */
    public List<Unidades> obtenerUnidadesPorUsuarioEntidad(Integer pUsuarioId, long pCodSilais, String pCodigoSis, String tipoUnidades){
        List<Unidades> unidadesList = new ArrayList<Unidades>();
        try {
            String query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                    "and uni.entidadAdtva.codigo = :pCodSilais order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas directamente al usuario, se obtienen todas las unidades del los silais asociados directamente
            if (unidadesList.size()<=0){
                query = "select uni from Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                        "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                        "and uni.entidadAdtva.codigo = :pCodSilais order by uni.nombre";
                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
                unidadesList = qrUsuarioUnidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
    }

    /**
     * M�todo que obtiene todas las unidades de salud a las que tiene autorizaci�n el usuario en el sistema seg�n el SILAIS
     * @param pUsuarioId id del usuario autenticado
     * @param pCodSilais C�digo del silais a filtrar
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @return List<Unidades>
     */
    public List<Unidades> obtenerUnidadesPorUsuarioEntidad(Integer pUsuarioId, long pCodSilais, String pCodigoSis){
        List<Unidades> unidadesList = new ArrayList<Unidades>();
        try {
            String query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                    "and uni.entidadAdtva.codigo = :pCodSilais order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas directamente al usuario, se obtienen todas las unidades del los silais asociados directamente
            if (unidadesList.size()<=0){
                query = "select uni from Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                        "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                        "and uni.entidadAdtva.codigo = :pCodSilais order by uni.nombre";
                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
                unidadesList = qrUsuarioUnidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
    }

    /**
     * M�todo que obtiene todas las unidades de salud a las que tiene autorizaci�n el usuario en el sistema seg�n el SILAIS y municipio
     * @param pUsuarioId id del usuario autenticado
     * @param pCodSilais C�digo del silais a filtrar
     * @param pCodMunicipio C�digo del municio a filtrar
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param tipoUnidades tipos de unidades a carga. Eje: Primarias , Primarias+Hospitales
     * @return List<Unidades>
     */
    public List<Unidades> obtenerUnidadesPorUsuarioEntidadMunicipio(Integer pUsuarioId, long pCodSilais, String pCodMunicipio, String pCodigoSis, String tipoUnidades){
        List<Unidades> unidadesList = new ArrayList<Unidades>();
        try {
            //se obtienen todas las unidades del los silais asociados directamente
            String query = "select uni from Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                    "and uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.codigoNacional = :pCodMunicipio order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            qrUsuarioUnidad.setParameter("pCodMunicipio",pCodMunicipio);
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas indirectamente al usuario, se obtienen las unidades asociadas directamente
            if (unidadesList.size()<=0){
                query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                        "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                        "and uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.codigoNacional = :pCodMunicipio order by uni.nombre";
                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
                qrUsuarioUnidad.setParameter("pCodMunicipio",pCodMunicipio);
                unidadesList = qrUsuarioUnidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
    }

    /**
     * M�todo que obtiene los municipios autorizados en el sistema para el usuario seg�n el SILAIS, las unidades autorizadas y el tipo de Unidad
     * @param pUsuarioId id del usuario autenticado
     * @param pCodSilais C�digo del silais a filtrar
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @return List<Divisionpolitica>
     */
    public List<Divisionpolitica> obtenerMunicipiosPorUsuarioEntidad(Integer pUsuarioId, long pCodSilais, String pCodigoSis){
        /*String query = "select distinct muni from Divisionpolitica as muni, Unidades as uni " +
                "where muni.pasivo = :pasivo and  uni.entidadAdtva = :pCodSilais and uni.municipio = muni.codigoNacional order by muni.nombre"; // muni.dependenciaSilais =:idSilas";
        */
        List<Divisionpolitica> resultado = new ArrayList<Divisionpolitica>();
        String query = "select distinct muni from Divisionpolitica as muni, Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                "and muni.pasivo = :pasivo and  uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.codigoNacional = muni.codigoNacional " +
                "order by muni.nombre";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("pCodSilais", pCodSilais);
        q.setParameter("pCodigoSis",pCodigoSis);
        q.setParameter("pUsuarioId",pUsuarioId);
        q.setParameter("pasivo",'0');
        resultado = q.list();
        //no hay unidades asociadas directamente al usuario, se obtienen todos los municipios asociados a todas las unidades del los silais asociados directamente
        if (resultado.size()<=0){
            query = "select distinct muni from Divisionpolitica as muni, Unidades uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                    "and muni.pasivo = :pasivo and  uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.codigoNacional = muni.codigoNacional " +
                    "order by muni.nombre";

            q = session.createQuery(query);
            q.setParameter("pCodSilais", pCodSilais);
            q.setParameter("pCodigoSis",pCodigoSis);
            q.setParameter("pUsuarioId",pUsuarioId);
            q.setParameter("pasivo",'0');
            resultado = q.list();
        }
        return resultado;
    }

    /**
     * M�todo que valida si el usuario tiene asignada una unidad de salud, que tiene asignado alg�n estudio para permitir toma de muestra de estudio
     * @param pUsuarioId id del usuario autenticado
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @return boolean
     */
    public boolean esUsuarioAutorizadoTomaMxEstudio(Integer pUsuarioId, String pCodigoSis){
        List<Estudio_UnidadSalud> estudioUnidadSaludList = new ArrayList<Estudio_UnidadSalud>();
        String query = "from Estudio_UnidadSalud eu, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                "where eu.unidad.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and eu.pasivo = false ";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("pUsuarioId",pUsuarioId);
        q.setParameter("pCodigoSis",pCodigoSis);
        estudioUnidadSaludList = q.list();
        return estudioUnidadSaludList.size()>0;
    }

    public String getNivelUsuario(Integer idUsuario){

        if (esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE))
            return "PAIS";
        else{
            Session session = sessionFactory.getCurrentSession();
            String query = "select distinct ent from EntidadesAdtvas ent, UsuarioEntidad usuent, Usuarios usu, Sistema sis " +
                    "where ent.id = usuent.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuent.usuario.usuarioId and usuent.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo order by ent.nombre";

            Query q = session.createQuery(query);
            q.setParameter("pUsuarioId", idUsuario);
            q.setParameter("pCodigoSis",ConstantsSecurity.SYSTEM_CODE);
            q.setParameter("pasivo",'0');
            if (q.list().size()>0){
                return "SILAIS";
            }

            query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo order by uni.nombre";

            q = session.createQuery(query);
            q.setParameter("pUsuarioId", idUsuario);
            q.setParameter("pCodigoSis",ConstantsSecurity.SYSTEM_CODE);
            q.setParameter("pasivo",'0');

            if (q.list().size()>0){
                return "UNIDAD";
            }
            return null;
        }
    }

    /**
     * M�todo que obtiene la lista de areas por las que puede generar reportes un usuario
     * @param idUsuario id del usuario a validar
     * @param menorNivelPermitido nivel mas bajo permitido a obtener areas: 1=PAIS, 2=SILAIS, 3=UNIDAD
     * @return
     */
    public List<AreaRep> getAreasUsuario(Integer idUsuario, int menorNivelPermitido){
        List<AreaRep> areaRepList = new ArrayList<AreaRep>();
        Session session = sessionFactory.getCurrentSession();
        String query = "from AreaRep as a ";


        String nivelUsuario = getNivelUsuario(idUsuario);
        if (nivelUsuario!=null) {
            switch (nivelUsuario){
                case "PAIS" :{
                    switch (menorNivelPermitido){
                        case 1 :{
                            query += " where a.codigo in ('AREAREP|PAIS','AREAREP|DEPTO','AREAREP|SILAIS') "; // no incluir municipios, ni unidad, ni zona
                            break;
                        }
                        case 2: {
                            query += " where a.codigo not in ('AREAREP|UNI') "; // s�lo no incluir unidad
                            break;
                        }
                        case 3 : {
                            query += " where 1 = 1";  // todos
                            break;
                        }
                        case 4 :{
                            query += " where a.codigo in ('AREAREP|PAIS','AREAREP|SILAIS') "; // no incluir municipios, ni unidad, ni zona, ni departamento
                            break;
                        }
                        default: break;
                    }

                    break;
                }
                case "SILAIS" : {
                    /*if (menorNivelPermitido >= 2) {
                        query += " where a.codigo not in ('AREAREP|PAIS','AREAREP|DEPTO') "; // no incluir pais y departamento
                    }else{
                        query += " where 1 = 0";  // ninguno
                    }*/
                    switch (menorNivelPermitido){
                        case 1 :{
                            query += " where 1 = 0";  // ninguno
                            break;
                        }
                        case 2: {
                            query += " where a.codigo in ('AREAREP|SILAIS') "; // solo silais//query += " where a.codigo not in ('AREAREP|PAIS','AREAREP|DEPTO','AREAREP|UNI') "; // no incluir pais, departamento y unidades
                            break;
                        }
                        case 3 : {
                            query += " where a.codigo not in ('AREAREP|PAIS','AREAREP|DEPTO') "; // no incluir pais y departamento
                            break;
                        }
                        case 4 :{
                            query += " where a.codigo in ('AREAREP|SILAIS') "; // solo silais
                            break;
                        }
                        default: break;
                    }
                    break;
                }
                case "UNIDAD" :{
                    switch (menorNivelPermitido){
                        case 1 :{
                            query += " where 1 = 0";  // ninguno
                            break;
                        }
                        case 2: {
                            query += " where 1 = 0";  // ninguno
                            break;
                        }
                        case 3 : {
                            query += " where a.codigo in ('AREAREP|UNI') "; // s�lo unidad
                            break;
                        }
                        case 4: {
                            query += " where 1 = 0";  // ninguno
                            break;
                        }
                        default: break;
                    }
                    /*if (menorNivelPermitido==3) {
                        query += " where a.codigo in ('AREAREP|UNI') "; // s�lo unidad
                    }else {
                        query += " where 1 = 0";  // ninguno
                    }*/
                    break;
                }
                default: break;
            }
            query += "order by a.orden asc";
            Query q = session.createQuery(query);
            areaRepList = q.list();
        }
        return  areaRepList;
    }
}
