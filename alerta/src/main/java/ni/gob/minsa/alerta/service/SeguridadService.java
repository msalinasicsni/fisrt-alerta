package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
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
 * Created by FIRSTICT on 10/28/2014.
 */
@Service("seguridadService")
@Transactional
public class SeguridadService {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public InfoSesion obtenerInfoSesion(String pBdSessionId) {
        InfoSesion infoSesion = null;

        try{

            InitialContext ctx = new InitialContext();

            PortalService portalService = (PortalService)ctx.lookup(ConstantsSecurity.EJB_BIN);
            InfoResultado infoResultado = portalService.obtenerInfoSesion(pBdSessionId);

            if(infoResultado!=null){
                if(infoResultado.isOk()){
                    if(infoResultado!=null) infoSesion = (InfoSesion) infoResultado.getObjeto();
                }
            }
            infoSesion = new InfoSesion();
            infoSesion.setUsuarioId(25);
            infoSesion.setNombre("usuariosis1");
            infoSesion.setUsername("usuariosis1");
            infoSesion.setSistemaSesion("ALERTA");
            ctx.close();
        }catch(Exception e){
            System.out.println("---- EXCEPTION");
            System.out.println("Error no controlado: " + e.toString());
        }

        return infoSesion;
    }

    public String obtenerUrlPortal() {
        String urlPortal;

        try{
            InitialContext ctx = new InitialContext();

            PortalService portalService = (PortalService)ctx.lookup(ConstantsSecurity.EJB_BIN);
            urlPortal = portalService.obtenerUrlLogin();

            ctx.close();
        }catch(NamingException e){
            //urlPortal = "/errorPage.xhtml?faces-redirect=true";
            urlPortal = "redirect:/403";
        }

        return urlPortal;
    }

    public String ValidarLogin(HttpServletRequest request){
        String urlRetorno="";
        if (!esUsuarioAutenticado(request.getSession())){
            String bdSessionId = "a";  // esta variable dejarla en blanco par activar la seguridad
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for(int i=0; i<cookies.length; i++){
                    if (cookies[i].getName().equalsIgnoreCase(ConstantsSecurity.COOKIE_NAME)){
                        bdSessionId = cookies[i].getValue();
                    }
                }
            }
            if(!bdSessionId.equals("")) {
                InfoSesion infoSesion;
                if (request.getSession().getAttribute("infoSesionActual") == null) {
                    infoSesion = obtenerInfoSesion(bdSessionId);

                } else {
                    infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");
                }
                if (infoSesion != null) {
                    request.getSession().setAttribute("infoSesionActual", infoSesion);
                }
            }else{
                urlRetorno = "redirect:"+obtenerUrlPortal();
            }
        }
        return urlRetorno;
    }

    public String ValidarAutorizacionUsuario(HttpServletRequest request, String codSistema){
        String urlRetorno="";
        boolean autorizado;
        InfoSesion infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");

        if (infoSesion != null) {
            String pViewId = request.getServletPath();
            autorizado = esUsuarioAutorizado(infoSesion.getUsuarioId(), codSistema, pViewId);
            if (!autorizado){
                urlRetorno = "403";
            }
        }else{
            urlRetorno = "redirect:"+obtenerUrlPortal();
        }
        return urlRetorno;
    }

    public boolean esUsuarioAutenticado(HttpSession session) {
        return session.getAttribute("infoSesionActual")!=null;
    }

    public boolean esUsuarioAutorizado(long pUsuarioId, String pSistema, String pViewId) {
        boolean autorizado = false;

        try{
            InitialContext ctx = new InitialContext();
            PortalService portalService = (PortalService)ctx.lookup(ConstantsSecurity.EJB_BIN);
             if(portalService!=null){
                autorizado = portalService.esUsuarioAutorizado(pUsuarioId, pViewId, pSistema);
            }
            ctx.close();
        }catch(Exception e){
            autorizado = false;
        }
        return autorizado;
    }

    public long obtenerIdUsuario(HttpServletRequest request){
        long idUsuario=0L;
        InfoSesion infoSesion = (InfoSesion) request.getSession().getAttribute("infoSesionActual");

        if (infoSesion != null) {
            idUsuario = infoSesion.getUsuarioId();
        }
        return idUsuario;
    }

    public boolean esUsuarioNivelCentral(long pUsuarioId, String pSistema) {
        boolean nivelCentral = false;

        try{
            InitialContext ctx = new InitialContext();
            PortalService portalService = (PortalService) ctx.lookup(ConstantsSecurity.EJB_BIN);

            if (portalService != null) {
                nivelCentral = portalService.esUsuarioNivelCentral(pUsuarioId, pSistema);
            }
            ctx.close();

        }catch(Exception e){
            nivelCentral = false;
        }

        return nivelCentral;
    }

    public void logOut(HttpSession session) {
        session.removeAttribute("infoSesionActual");
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
            String query = "select ent from EntidadesAdtvas ent, UsuarioEntidad usuent, Usuarios usu, Sistema sis " +
                    "where ent.id = usuent.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuent.usuario.usuarioId and usuent.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo order by ent.nombre";
            Query qrUsuarioEntidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioEntidad.setParameter("pUsuarioId", pUsuarioId);
            qrUsuarioEntidad.setParameter("pCodigoSis", pCodigoSis);
            qrUsuarioEntidad.setParameter("pasivo", '0');
            entidadesAdtvasList = qrUsuarioEntidad.list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return entidadesAdtvasList;
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
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
                    "and uni.entidadAdtva = :pCodSilais order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            unidadesList = qrUsuarioUnidad.list();
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
                    "and uni.entidadAdtva = :pCodSilais order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            unidadesList = qrUsuarioUnidad.list();
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
            String query = "select uni from Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                    "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                    "and uni.entidadAdtva = :pCodSilais and uni.municipio = :pCodMunicipio order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            qrUsuarioUnidad.setParameter("pCodMunicipio",pCodMunicipio);
            unidadesList = qrUsuarioUnidad.list();
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
        String query = "select distinct muni from Divisionpolitica as muni, Unidades uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo " +
                "and muni.pasivo = :pasivo and  uni.entidadAdtva = :pCodSilais and uni.municipio = muni.codigoNacional " +
                "order by muni.nombre";
         Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("pCodSilais", pCodSilais);
        q.setParameter("pCodigoSis",pCodigoSis);
        q.setParameter("pUsuarioId",pUsuarioId);
        q.setParameter("pasivo",'0');
        return q.list();
    }
}
