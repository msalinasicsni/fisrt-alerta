package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.CondicionEgreso;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.rotavirus.*;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("rotavirus")
public class RotavirusController {
    private static final Logger logger = LoggerFactory.getLogger(IragController.class);

    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Resource(name="daNotificacionService")
    private DaNotificacionService daNotificacionService;

    @Autowired
    @Qualifier(value = "rotaVirusService")
    private RotaVirusService rotaVirusService;

    @Autowired(required = true)
    @Qualifier(value = "usuarioService")
    public UsuarioService usuarioService;
    @Resource(name = "personaService")
    private PersonaService personaService;
    @Resource(name = "comunidadesService")
    private ComunidadesService comunidadesService;
    @Autowired(required = true)
    @Qualifier(value = "entidadAdmonService")
    public EntidadAdmonService entidadAdmonService;
    @Autowired(required = true)
    @Qualifier(value = "divisionPoliticaService")
    public DivisionPoliticaService divisionPoliticaService;
    @Autowired(required = true)
    @Qualifier(value = "unidadesService")
    public UnidadesService unidadesService;
    @Autowired(required = true)
    @Qualifier(value = "catalogosService")
    public CatalogoService catalogoService;

    @Autowired
    MessageSource messageSource;

    List<EntidadesAdtvas> entidades;
    List<Divisionpolitica> departamentos;
    List<ClasificacionFinalRotavirus> catClasif;
    List<GradoDeshidratacion> gradoDeshidratacions;
    List<Respuesta> catResp;
    List<CaracteristaHeces> caracteristaHeceses;
    List<RegistroVacuna> registroVacunas;
    List<TipoVacunaRotavirus> tipoVacunaRotavirus;
    List<CondicionEgreso> condicionEgresos;
    List<SalaRotaVirus> salaRotaVirusList;
    Map<String, Object> mapModel;


    void Initialize() throws Exception {
        try {


            departamentos = divisionPoliticaService.getAllDepartamentos();
            catClasif = catalogoService.getClasificacionesFinalRotavirus();
            catResp = catalogoService.getRespuesta();
            registroVacunas = catalogoService.getRegistrosVacuna();
            tipoVacunaRotavirus = catalogoService.getTiposVacunasRotavirus();
            caracteristaHeceses = catalogoService.getCaracteristasHeces();
            gradoDeshidratacions = catalogoService.getGradosDeshidratacion();
            condicionEgresos = catalogoService.getCondicionEgreso();
            salaRotaVirusList = catalogoService.getSalasRotaVirus();

            mapModel = new HashMap<>();

            mapModel.put("departamentos", departamentos);
            mapModel.put("catClasif", catClasif);
            mapModel.put("catResp", catResp);
            mapModel.put("registroVacunas", registroVacunas);
            mapModel.put("tipoVacunaRotavirus", tipoVacunaRotavirus);
            mapModel.put("caracteristaHeceses", caracteristaHeceses);
            mapModel.put("gradoDeshidratacions", gradoDeshidratacions);
            mapModel.put("condicionEgresos",condicionEgresos);
            mapModel.put("salas",salaRotaVirusList);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar una ficha de Rotavirus");

        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        if(urlValidacion.isEmpty()){
            return "rotavirus/search";
        }else{
            return urlValidacion;
        }

    }

    /**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();

        if(urlValidacion.isEmpty()){
            List<FichaRotavirus> results = rotaVirusService.getFichaByPersonaId(idPerson);

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            if (results.size() == 0) {
                boolean autorizado = true;
                FichaRotavirus fichaRotavirus = new FichaRotavirus();
                DaNotificacion noti = new DaNotificacion();
                Initialize();
                SisPersona persona = personaService.getPersona(idPerson);

                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                }

                if (persona != null) {
                    noti.setPersona(persona);
                    fichaRotavirus.setDaNotificacion(noti);
                    Divisionpolitica departamentoProce = null;
                    List<Divisionpolitica> municipiosResi = new ArrayList<Divisionpolitica>();
                    List<Comunidades> comunidades = new ArrayList<Comunidades>();
                    if (fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia()!=null) {
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                        municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                        comunidades = comunidadesService.getComunidades(fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                    }

                    mav.addObject("entidades", entidades);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("departamentoProce", departamentoProce);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("fichaRotavirus", fichaRotavirus);
                    mav.addAllObjects(mapModel);
                    mav.setViewName("rotavirus/create");
                } else {
                    mav.setViewName("404");
                }
            } else {
                List<String> fichaRotavirusAutorizadas = new ArrayList<String>();
                for (FichaRotavirus rt : results) {
                    if (idUsuario != 0) {
                        if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE,
                                rt.getDaNotificacion().getCodSilaisAtencion().getCodigo()) &&
                                seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE,
                                        rt.getDaNotificacion().getCodUnidadAtencion().getCodigo())) {
                            fichaRotavirusAutorizadas.add(rt.getDaNotificacion().getIdNotificacion());
                        }
                    }
                }
                mav.addObject("records", results);
                mav.addObject("idPerson", idPerson);
                mav.addObject("fichasAutorizadas", fichaRotavirusAutorizadas);
                mav.setViewName("rotavirus/results");
            }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * Custom handler to create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("new/{idPerson}")
    public ModelAndView newFicha(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            boolean autorizado = true;
            FichaRotavirus fichaRotavirus = new FichaRotavirus();
            DaNotificacion noti = new DaNotificacion();
            Initialize();
            SisPersona persona = personaService.getPersona(idPerson);

            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            } else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            if (persona != null) {
                noti.setPersona(persona);
                fichaRotavirus.setDaNotificacion(noti);
                Divisionpolitica departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                List<Divisionpolitica> municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                List<Comunidades> comunidades = comunidadesService.getComunidades(fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());

                mav.addObject("entidades", entidades);
                mav.addObject("autorizado", autorizado);
                mav.addObject("departamentoProce", departamentoProce);
                mav.addObject("municipiosResi", municipiosResi);
                mav.addObject("comunidades", comunidades);
                mav.addObject("fichaRotavirus", fichaRotavirus);
                mav.addAllObjects(mapModel);
                mav.setViewName("rotavirus/create");
            } else {
                mav.setViewName("404");
            }

        } else {
            mav.setViewName(urlValidacion);
        }

        return mav;
    }

    /***
     * Obetener la información de una notificación y cargarla en pantalla para su edición
     * @param idNotificacion a cargar
     * @param request con datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping("edit/{idNotificacion}")
    public ModelAndView editIrag(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        boolean autorizado = true;
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {

            if(idNotificacion != null){
                FichaRotavirus fichaRotavirus = rotaVirusService.getFichaById(idNotificacion);

                if (fichaRotavirus != null) {
                    Initialize();

                    long idUsuario = seguridadService.obtenerIdUsuario(request);
                    //irag.setUsuario(usuarioService.getUsuarioById((int) idUsuario));
                    if (idUsuario != 0) {
                        autorizado = seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE) ||
                                ((fichaRotavirus.getDaNotificacion().getCodSilaisAtencion()!=null && fichaRotavirus.getDaNotificacion().getCodUnidadAtencion()!=null) &&
                                        seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, fichaRotavirus.getDaNotificacion().getCodSilaisAtencion().getCodigo()) &&
                                        seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, fichaRotavirus.getDaNotificacion().getCodUnidadAtencion().getCodigo()));
                    }

                    entidades = seguridadService.obtenerEntidadesPorUsuario((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);

                    if(entidades.size() <=0){
                        entidades.add(fichaRotavirus.getDaNotificacion().getCodSilaisAtencion());
                    }

                    Divisionpolitica municipio = divisionPoliticaService.getMunicipiosByUnidadSalud(fichaRotavirus.getDaNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional());
                    List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(fichaRotavirus.getDaNotificacion().getCodSilaisAtencion().getCodigo());

                    List<Unidades> uni = null;
                    if (seguridadService.esUsuarioNivelCentral(idUsuario,ConstantsSecurity.SYSTEM_CODE)) {
                        if (fichaRotavirus.getDaNotificacion().getCodSilaisAtencion()!=null && fichaRotavirus.getDaNotificacion().getCodUnidadAtencion()!=null) {
                            uni = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int) idUsuario, fichaRotavirus.getDaNotificacion().getCodSilaisAtencion().getCodigo(),
                                    fichaRotavirus.getDaNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), ConstantsSecurity.SYSTEM_CODE, HealthUnitType.UnidadesPrimHosp.getDiscriminator());
                        }
                    }
                    else{
                        if (fichaRotavirus.getDaNotificacion().getCodSilaisAtencion()!=null && fichaRotavirus.getDaNotificacion().getCodUnidadAtencion()!=null) {
                            uni = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int) idUsuario, fichaRotavirus.getDaNotificacion().getCodSilaisAtencion().getCodigo(), fichaRotavirus.getDaNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), ConstantsSecurity.SYSTEM_CODE, HealthUnitType.UnidadesPrimHosp.getDiscriminator());
                            if (!uni.contains(fichaRotavirus.getDaNotificacion().getCodUnidadAtencion())) {
                                uni.add(fichaRotavirus.getDaNotificacion().getCodUnidadAtencion());
                            }
                        }
                    }

                    //datos persona
                    Divisionpolitica departamentoProce;
                    List<Divisionpolitica> municipiosResi = null;
                    List<Comunidades> comunidades = null;

                    if(fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia() != null){
                        String municipioResidencia = fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional();
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(municipioResidencia);
                        municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                        String comu = fichaRotavirus.getDaNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional();
                        comunidades = comunidadesService.getComunidades(comu);
                    }


                    mav.addObject("fichaRotavirus", fichaRotavirus);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("entidades", entidades);
                    mav.addObject("munic", munic);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("uni", uni);
                    mav.addObject("municipio", municipio);
                    mav.addAllObjects(mapModel);

                    mav.setViewName("rotavirus/create");
                } else {
                    mav.setViewName("404");
                }
            }else{
                mav.setViewName("404");
            }

        }else{
            mav.setViewName(urlValidacion);
        }


        return mav;
    }

    /**
     * Anular notificación
     * @param idNotificacion a anular
     * @param request con datos de autenticación
     * @return String
     * @throws Exception
     */
    @RequestMapping("/override/{idNotificacion}")
    public String overrideNoti(@PathVariable("idNotificacion") String idNotificacion,
                               HttpServletRequest request) throws Exception {
        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "redirect:/404";
        }
        if(urlValidacion.isEmpty()){
            FichaRotavirus fichaRotavirus = rotaVirusService.getFichaById(idNotificacion);
            if (fichaRotavirus!=null){
                DaNotificacion notificacion = fichaRotavirus.getDaNotificacion();
                notificacion.setPasivo(true);
                daNotificacionService.updateNotificacion(notificacion);
                rotaVirusService.saveOrUpdate(fichaRotavirus);
                return "redirect:/rotavirus/search/"+fichaRotavirus.getDaNotificacion().getPersona().getPersonaId();
            }
            else{
                return "redirect:/404";
            }
        }else{
            return "redirect:/"+urlValidacion;
        }
    }

    /**
     * Guardar datos de una notificación rotavirus (agregar o actualizar)
     * @param request con los datos de la notificación
     * @param response con el resultado de la acción
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    protected void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado="";
        String idNotificacion="";
        try {
            logger.debug("Guardando datos de Ficha Rotavirus");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            FichaRotavirus fichaRotavirus = jSonToFichaRotavirus(json);
            if (fichaRotavirus.getDaNotificacion() == null) {
                DaNotificacion noti = guardarNotificacion(json, request);
                fichaRotavirus.setDaNotificacion(noti);
            }else {
                if (fichaRotavirus.getFechaInicioDiarrea()!=null) {
                    fichaRotavirus.getDaNotificacion().setFechaInicioSintomas(fichaRotavirus.getFechaInicioDiarrea());
                    daNotificacionService.updateNotificacion(fichaRotavirus.getDaNotificacion());
                }
            }
            rotaVirusService.saveOrUpdate(fichaRotavirus);
            idNotificacion = fichaRotavirus.getDaNotificacion().getIdNotificacion();
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.error.save.rota",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("mensaje",resultado);
            map.put("idNotificacion",idNotificacion);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Si está habilitada la actualización de la persona mediante el componente de persona del MINSA se actualizan municipio, comunidad y dirección de residencia
     * @param municipioResidencia nuevo municipio
     * @param comunidadResidencia nueva comunidad
     * @param direccionResidencia nueva dirección
     * @param personaId persona a actualizar
     * @param idNotificacion de la persona
     * @param request con datos de autenticación
     * @return resultado de la acción
     * @throws Exception
     */
    @RequestMapping(value = "updatePerson", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePerson(
            @RequestParam(value = "municipioResidencia", required = false) String municipioResidencia
            , @RequestParam(value = "comunidadResidencia", required = false) String comunidadResidencia
            , @RequestParam(value = "direccionResidencia", required = false) String direccionResidencia
            , @RequestParam(value = "personaId", required = false) Integer personaId
            , @RequestParam(value = "idNotificacion", required = false) String idNotificacion, HttpServletRequest request

    ) throws Exception {

        logger.debug("Actualizando datos persona");
        SisPersona pers = new SisPersona();
        InfoResultado infoResultado;
        if (personaId != null) {
            pers = personaService.getPersona(personaId);
            pers.setMunicipioResidencia(divisionPoliticaService.getDivisionPolitiacaByCodNacional(municipioResidencia));
            pers.setComunidadResidencia(comunidadesService.getComunidad(comunidadResidencia));
            pers.setDireccionResidencia(direccionResidencia);
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT) {
                Persona persona = personaService.ensamblarObjetoPersona(pers);
                try {
                    personaService.iniciarTransaccion();

                    infoResultado = personaService.guardarPersona(persona, seguridadService.obtenerNombreUsuario(request));
                    if (infoResultado.isOk() && infoResultado.getObjeto() != null) {
                        updateNotificacion(idNotificacion, pers);
                    } else
                        throw new Exception(infoResultado.getMensaje() + "----" + infoResultado.getMensajeDetalle());
                    personaService.commitTransaccion();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    ex.printStackTrace();
                    try {
                        personaService.rollbackTransaccion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new Exception(ex);
                } finally {
                    try {
                        personaService.remover();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                updateNotificacion(idNotificacion, pers);
            }
        }
        return createJsonResponse(pers);
    }

    /**
     * Obtiene las notificaciones de tipo Rotavirus para la persona seleccionada
     * @param idPerson a consultar
     * @return List<FichaRotavirus>
     * @throws Exception
     */
    @RequestMapping(value = "getResults", method = RequestMethod.GET,  produces = "application/json")
    public @ResponseBody
    List<FichaRotavirus> getResults(@RequestParam(value = "idPerson", required = true) long idPerson) throws Exception {
        logger.info("Obteniendo los resultados de la búsqueda");
        List<FichaRotavirus> results = null;
        results = rotaVirusService.getFichaByPersonaId(idPerson);
        return results;
    }

    /**
     * Agrega una notificación de tipo Rotavirus
     * @param json con los datos de la ficha
     * @param request con datos de autenticación
     * @return DaNotificacion agregada
     * @throws Exception
     */
    public DaNotificacion guardarNotificacion(String json, HttpServletRequest request) throws Exception {

        logger.debug("Guardando Notificacion");
        DaNotificacion noti = new DaNotificacion();
        Integer personaId=null;
        Integer codSilaisAtencion=null;
        Integer codUnidadAtencion=null;
        String urgente = null;
        JsonObject jObjectJson = new Gson().fromJson(json, JsonObject.class);
        if (jObjectJson.get("personaId")!=null && !jObjectJson.get("personaId").getAsString().isEmpty()) {
            personaId = jObjectJson.get("personaId").getAsInt();
        }
        if (jObjectJson.get("codSilaisAtencion")!=null && !jObjectJson.get("codSilaisAtencion").getAsString().isEmpty()) {
            codSilaisAtencion = jObjectJson.get("codSilaisAtencion").getAsInt();
        }
        if (jObjectJson.get("codUnidadAtencion")!=null && !jObjectJson.get("codUnidadAtencion").getAsString().isEmpty()) {
            codUnidadAtencion = jObjectJson.get("codUnidadAtencion").getAsInt();
        }
        if (jObjectJson.get("urgente")!=null && !jObjectJson.get("urgente").getAsString().isEmpty()) {
            urgente = jObjectJson.get("urgente").getAsString();
        }

        if(personaId != null){
            SisPersona persona = personaService.getPersona(personaId);
            noti.setPersona(persona);
            noti.setFechaRegistro(new Timestamp(new Date().getTime()));
            noti.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
            noti.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            noti.setUsuarioRegistro(usuarioService.getUsuarioById((int)idUsuario));
            noti.setCodTipoNotificacion(catalogoService.getTipoNotificacion("TPNOTI|RTV"));
            noti.setMunicipioResidencia(persona.getMunicipioResidencia());
            noti.setDireccionResidencia(persona.getDireccionResidencia());
            noti.setUrgente(catalogoService.getRespuesta(urgente));

            daNotificacionService.addNotification(noti);
            return noti;
        }else{
            throw new Exception();
        }
    }

    /**
     * Actualiza una notificación de tipo Rotavirus
     * @param idNotificacion de la notificación a actualizar
     * @param persona a quien pertenece la notificación
     * @throws Exception
     */
    public void updateNotificacion(String idNotificacion, SisPersona persona ) throws Exception {
        DaNotificacion noti;

        if (idNotificacion != null) {
            //DaNotificacion
            noti = daNotificacionService.getNotifById(idNotificacion);
            noti.setMunicipioResidencia(persona.getMunicipioResidencia());
            noti.setDireccionResidencia(persona.getDireccionResidencia());
            daNotificacionService.updateNotificacion(noti);
        }
    }

    /***
     * Convierte un JSON a objeto FichaRotavirus
     * @param json con los datos de la ficha
     * @return FichaRotavirus
     * @throws ParseException
     */
    private FichaRotavirus jSonToFichaRotavirus(String json) throws ParseException {
        FichaRotavirus fichaRotavirus = new FichaRotavirus();
        JsonObject jObjectJson = new Gson().fromJson(json, JsonObject.class);
        String idNotificacion;
        String numExpediente = null;
        String codigo = null;
        //datos generales
        String nombreTutorAcompana = null;
        Boolean enGuarderia = null;
        String nombreGuarderia = null;
        //datos cl�nicos
        Date fechaInicioDiarrea = null;
        Integer noEvacuaciones24Hrs = null;
        Respuesta fiebre = null;
        Respuesta vomito = null;
        Integer noVomito24Hrs = null;
        Date fechaInicioVomito = null;
        CaracteristaHeces caracteristaHeces = null;
        String otraCaracteristicaHeces= null;
        GradoDeshidratacion gradoDeshidratacion = null;
        Integer diasHospitalizacion = null;
        Date fechaAlta = null;
        //tratamiento
        Respuesta usoAntibioticoPrevio = null;
        String plan = null;
        Boolean planB = null;
        Boolean planC = null;
        Respuesta antibioticoHospital = null;
        String cualAntibiotico= null;
        Boolean UCI = null;
        Integer diasUCI = null;
        Boolean altaUCIDiarrea = null;
        Date fechaTerminoDiarrea = null;
        Boolean ignoradoFechaTD = null;
        //historia vacunacion
        Respuesta vacunado = null;
        RegistroVacuna registroVacuna = null;
        TipoVacunaRotavirus tipoVacunaRotavirus = null;
        Boolean dosi1 = null;
        Date fechaAplicacionDosis1 = null;
        Boolean dosi2 = null;
        Date fechaAplicacionDosis2 = null;
        Boolean dosi3 = null;
        Date fechaAplicacionDosis3 = null;
        //datos laboratorio
        Respuesta tomoMuestraHeces = null;
        //Clasificaci�n final
        ClasificacionFinalRotavirus clasificacionFinal = null;
        CondicionEgreso condicionEgreso = null;
        //Responsable Informaci�n
        String nombreLlenaFicha= null;
        String epidemiologo= null;
        String nombreTomoMx=null;

        SalaRotaVirus sala = null;
        Date fechaIngreso = null;
        String telefonoTutor = null;

        if (jObjectJson.get("idNotificacion")!=null && !jObjectJson.get("idNotificacion").getAsString().isEmpty()) {
            idNotificacion = jObjectJson.get("idNotificacion").getAsString();
            fichaRotavirus = rotaVirusService.getFichaById(idNotificacion);
        }

        if (jObjectJson.get("numExpediente")!=null && !jObjectJson.get("numExpediente").getAsString().isEmpty())
            numExpediente = jObjectJson.get("numExpediente").getAsString();

        if (jObjectJson.get("codigo")!=null && !jObjectJson.get("codigo").getAsString().isEmpty())
            codigo = jObjectJson.get("codigo").getAsString();

        if (jObjectJson.get("nombreTutorAcompana")!=null && !jObjectJson.get("nombreTutorAcompana").getAsString().isEmpty())
            nombreTutorAcompana = jObjectJson.get("nombreTutorAcompana").getAsString();

        if (jObjectJson.get("enGuarderia")!=null)
            enGuarderia = jObjectJson.get("enGuarderia").getAsBoolean();

        if (jObjectJson.get("nombreGuarderia")!=null && !jObjectJson.get("nombreGuarderia").getAsString().isEmpty())
            nombreGuarderia = jObjectJson.get("nombreGuarderia").getAsString();

        if (jObjectJson.get("fechaInicioDiarrea")!=null && !jObjectJson.get("fechaInicioDiarrea").getAsString().isEmpty())
            fechaInicioDiarrea = DateUtil.StringToDate(jObjectJson.get("fechaInicioDiarrea").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("noEvacuaciones24Hrs")!=null && !jObjectJson.get("noEvacuaciones24Hrs").getAsString().isEmpty())
            noEvacuaciones24Hrs = jObjectJson.get("noEvacuaciones24Hrs").getAsInt();

        if (jObjectJson.get("fiebre")!=null && !jObjectJson.get("fiebre").getAsString().isEmpty())
            fiebre = catalogoService.getRespuesta(jObjectJson.get("fiebre").getAsString());

        if (jObjectJson.get("vomito")!=null && !jObjectJson.get("vomito").getAsString().isEmpty())
            vomito = catalogoService.getRespuesta(jObjectJson.get("vomito").getAsString());

        if (jObjectJson.get("noVomito24Hrs")!=null && !jObjectJson.get("noVomito24Hrs").getAsString().isEmpty())
            noVomito24Hrs = jObjectJson.get("noVomito24Hrs").getAsInt();

        if (jObjectJson.get("fechaInicioVomito")!=null && !jObjectJson.get("fechaInicioVomito").getAsString().isEmpty())
            fechaInicioVomito = DateUtil.StringToDate(jObjectJson.get("fechaInicioVomito").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("caracteristaHeces")!=null && !jObjectJson.get("caracteristaHeces").getAsString().isEmpty())
            caracteristaHeces = catalogoService.getCaracteristaHeces(jObjectJson.get("caracteristaHeces").getAsString());

        if (jObjectJson.get("otraCaracteristicaHeces")!=null && !jObjectJson.get("otraCaracteristicaHeces").getAsString().isEmpty())
            otraCaracteristicaHeces = jObjectJson.get("otraCaracteristicaHeces").getAsString();

        if (jObjectJson.get("gradoDeshidratacion")!=null && !jObjectJson.get("gradoDeshidratacion").getAsString().isEmpty())
            gradoDeshidratacion = catalogoService.getGradoDeshidratacion(jObjectJson.get("gradoDeshidratacion").getAsString());

        if (jObjectJson.get("diasHospitalizacion")!=null && !jObjectJson.get("diasHospitalizacion").getAsString().isEmpty())
            diasHospitalizacion = jObjectJson.get("diasHospitalizacion").getAsInt();

        if (jObjectJson.get("fechaAlta")!=null && !jObjectJson.get("fechaAlta").getAsString().isEmpty())
            fechaAlta = DateUtil.StringToDate(jObjectJson.get("fechaAlta").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("usoAntibioticoPrevio")!=null && !jObjectJson.get("usoAntibioticoPrevio").getAsString().isEmpty())
            usoAntibioticoPrevio = catalogoService.getRespuesta(jObjectJson.get("usoAntibioticoPrevio").getAsString());

        if (jObjectJson.get("plan")!=null && !jObjectJson.get("plan").getAsString().isEmpty()) {
            plan = jObjectJson.get("plan").getAsString();
            if (plan.equals("planB"))
                planB = true;
            else
                planC = true;
        }

        if (jObjectJson.get("antibioticoHospital")!=null && !jObjectJson.get("antibioticoHospital").getAsString().isEmpty())
            antibioticoHospital = catalogoService.getRespuesta(jObjectJson.get("antibioticoHospital").getAsString());

        if (jObjectJson.get("cualAntibiotico")!=null && !jObjectJson.get("cualAntibiotico").getAsString().isEmpty())
            cualAntibiotico = jObjectJson.get("cualAntibiotico").getAsString();

        if (jObjectJson.get("UCI")!=null)
            UCI = jObjectJson.get("UCI").getAsBoolean();

        if (jObjectJson.get("diasUCI")!=null && !jObjectJson.get("diasUCI").getAsString().isEmpty())
            diasUCI = jObjectJson.get("diasUCI").getAsInt();

        if (jObjectJson.get("altaUCIDiarrea")!=null)
            altaUCIDiarrea = jObjectJson.get("altaUCIDiarrea").getAsBoolean();

        if (jObjectJson.get("fechaTerminoDiarrea")!=null && !jObjectJson.get("fechaTerminoDiarrea").getAsString().isEmpty())
            fechaTerminoDiarrea = DateUtil.StringToDate(jObjectJson.get("fechaTerminoDiarrea").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("ignoradoFTD")!=null)
            ignoradoFechaTD = jObjectJson.get("ignoradoFTD").getAsBoolean();

        if (jObjectJson.get("vacunado")!=null && !jObjectJson.get("vacunado").getAsString().isEmpty())
            vacunado = catalogoService.getRespuesta(jObjectJson.get("vacunado").getAsString());

        if (jObjectJson.get("registroVacuna")!=null && !jObjectJson.get("registroVacuna").getAsString().isEmpty())
            registroVacuna = catalogoService.getRegistroVacuna(jObjectJson.get("registroVacuna").getAsString());

        if (jObjectJson.get("tipoVacunaRotavirus")!=null && !jObjectJson.get("tipoVacunaRotavirus").getAsString().isEmpty())
            tipoVacunaRotavirus = catalogoService.getTipoVacunaRotavirus(jObjectJson.get("tipoVacunaRotavirus").getAsString());

        if (jObjectJson.get("dosi1")!=null)
            dosi1 = jObjectJson.get("dosi1").getAsBoolean();

        if (jObjectJson.get("fechaAplicacionDosis1")!=null && !jObjectJson.get("fechaAplicacionDosis1").getAsString().isEmpty())
            fechaAplicacionDosis1 = DateUtil.StringToDate(jObjectJson.get("fechaAplicacionDosis1").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("dosi2")!=null)
            dosi2 = jObjectJson.get("dosi2").getAsBoolean();

        if (jObjectJson.get("fechaAplicacionDosis2")!=null && !jObjectJson.get("fechaAplicacionDosis2").getAsString().isEmpty())
            fechaAplicacionDosis2 = DateUtil.StringToDate(jObjectJson.get("fechaAplicacionDosis2").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("dosi3")!=null)
            dosi3 = jObjectJson.get("dosi3").getAsBoolean();

        if (jObjectJson.get("fechaAplicacionDosis3")!=null && !jObjectJson.get("fechaAplicacionDosis3").getAsString().isEmpty())
            fechaAplicacionDosis3 = DateUtil.StringToDate(jObjectJson.get("fechaAplicacionDosis3").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("tomoMuestraHeces")!=null && !jObjectJson.get("tomoMuestraHeces").getAsString().isEmpty())
            tomoMuestraHeces = catalogoService.getRespuesta(jObjectJson.get("tomoMuestraHeces").getAsString());

        if (jObjectJson.get("clasificacionFinal")!=null && !jObjectJson.get("clasificacionFinal").getAsString().isEmpty())
            clasificacionFinal = catalogoService.getClasificacionFinalRotavirus(jObjectJson.get("clasificacionFinal").getAsString());

        if (jObjectJson.get("condicionEgreso")!=null && !jObjectJson.get("condicionEgreso").getAsString().isEmpty())
            condicionEgreso = catalogoService.getCondicionEgreso(jObjectJson.get("condicionEgreso").getAsString());

        if (jObjectJson.get("nombreLlenaFicha")!=null && !jObjectJson.get("nombreLlenaFicha").getAsString().isEmpty())
            nombreLlenaFicha = jObjectJson.get("nombreLlenaFicha").getAsString();

        if (jObjectJson.get("epidemiologo")!=null && !jObjectJson.get("epidemiologo").getAsString().isEmpty())
            epidemiologo = jObjectJson.get("epidemiologo").getAsString();

        if (jObjectJson.get("sala")!=null && !jObjectJson.get("sala").getAsString().isEmpty())
            sala = catalogoService.getSalaRotaVirus(jObjectJson.get("sala").getAsString());

        if (jObjectJson.get("fechaIngreso")!=null && !jObjectJson.get("fechaIngreso").getAsString().isEmpty())
            fechaIngreso = DateUtil.StringToDate(jObjectJson.get("fechaIngreso").getAsString(),"dd/MM/yyyy");

        if (jObjectJson.get("nombreTomoMx")!=null && !jObjectJson.get("nombreTomoMx").getAsString().isEmpty())
            nombreTomoMx = jObjectJson.get("nombreTomoMx").getAsString();

        if (jObjectJson.get("telefonoTutor")!=null && !jObjectJson.get("telefonoTutor").getAsString().isEmpty())
            telefonoTutor = jObjectJson.get("telefonoTutor").getAsString();

        fichaRotavirus.setNombreTutorAcompana(nombreTutorAcompana);
        fichaRotavirus.setEnGuarderia(enGuarderia);
        fichaRotavirus.setNombreGuarderia(nombreGuarderia);
        fichaRotavirus.setFechaInicioDiarrea(fechaInicioDiarrea);
        fichaRotavirus.setNoEvacuaciones24Hrs(noEvacuaciones24Hrs);
        fichaRotavirus.setFiebre(fiebre);
        fichaRotavirus.setVomito(vomito);
        fichaRotavirus.setNoVomito24Hrs(noVomito24Hrs);
        fichaRotavirus.setFechaInicioVomito(fechaInicioVomito);
        fichaRotavirus.setCaracteristaHeces(caracteristaHeces);
        fichaRotavirus.setOtraCaracteristicaHeces(otraCaracteristicaHeces);
        fichaRotavirus.setGradoDeshidratacion(gradoDeshidratacion);
        fichaRotavirus.setDiasHospitalizacion(diasHospitalizacion);
        fichaRotavirus.setFechaAlta(fechaAlta);
        fichaRotavirus.setUsoAntibioticoPrevio(usoAntibioticoPrevio);
        fichaRotavirus.setPlanB(planB);
        fichaRotavirus.setPlanC(planC);
        fichaRotavirus.setAntibioticoHospital(antibioticoHospital);
        fichaRotavirus.setCualAntibiotico(cualAntibiotico);
        fichaRotavirus.setUCI(UCI);
        fichaRotavirus.setDiasUCI(diasUCI);
        fichaRotavirus.setAltaUCIDiarrea(altaUCIDiarrea);
        fichaRotavirus.setFechaTerminoDiarrea(fechaTerminoDiarrea);
        fichaRotavirus.setIgnoradoFechaTD(ignoradoFechaTD);
        fichaRotavirus.setVacunado(vacunado);
        fichaRotavirus.setRegistroVacuna(registroVacuna);
        fichaRotavirus.setTipoVacunaRotavirus(tipoVacunaRotavirus);
        fichaRotavirus.setDosi1(dosi1);
        fichaRotavirus.setDosi2(dosi2);
        fichaRotavirus.setDosi3(dosi3);
        fichaRotavirus.setFechaAplicacionDosis1(fechaAplicacionDosis1);
        fichaRotavirus.setFechaAplicacionDosis2(fechaAplicacionDosis2);
        fichaRotavirus.setFechaAplicacionDosis3(fechaAplicacionDosis3);
        fichaRotavirus.setTomoMuestraHeces(tomoMuestraHeces);
        fichaRotavirus.setClasificacionFinal(clasificacionFinal);
        fichaRotavirus.setCondicionEgreso(condicionEgreso);
        fichaRotavirus.setNombreLlenaFicha(nombreLlenaFicha);
        fichaRotavirus.setEpidemiologo(epidemiologo);
        fichaRotavirus.setNombreTomoMx(nombreTomoMx);
        fichaRotavirus.setSala(sala);
        fichaRotavirus.setFechaIngreso(fechaIngreso);
        fichaRotavirus.setNumExpediente(numExpediente);
        fichaRotavirus.setCodigo(codigo);
        fichaRotavirus.setTelefonoTutor(telefonoTutor);

        return fichaRotavirus;
    }

    private ResponseEntity<String> createJsonResponse(Object o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }
}
