package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.*;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by souyen-ics
 */
@Controller
@RequestMapping("/irag")
public class IragController {

    private static final Logger logger = LoggerFactory.getLogger(IragController.class);
    @Autowired(required = true)
    @Qualifier(value = "sessionFactory")
    public SessionFactory sessionFactory;
    @Autowired(required = true)
    @Qualifier(value = "daIragService")
    public DaIragService daIragService;
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
    @Autowired(required = true)
    @Qualifier(value = "daVacunasIragService")
    public DaVacunasIragService daVacunasIragService;
    @Autowired(required = true)
    @Qualifier(value = "daCondicionesIragService")
    public DaCondicionesIragService daCondicionesIragService;
    @Autowired(required = true)
    @Qualifier(value = "daManifestacionesIragService")
    public DaManifestacionesIragService daManifestacionesIragService;
    @Autowired(required = true)
    @Qualifier(value = "usuarioService")
    public UsuarioService usuarioService;
    @Resource(name = "personaService")
    private PersonaService personaService;
    @Resource(name = "comunidadesService")
    private ComunidadesService comunidadesService;
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Resource(name="daNotificacionService")
    private DaNotificacionService daNotificacionService;

    @Autowired
    MessageSource messageSource;

    List<EntidadesAdtvas> entidades;
    List<Divisionpolitica> departamentos;
    List<Procedencia> catProcedencia;
    List<Clasificacion> catClasif;
    List<Captacion> catCaptac;
    List<Respuesta> catResp;
    List<ViaAntibiotico> catVia;
    List<ResultadoRadiologia> catResRad;
    List<CondicionEgreso> catConEgreso;
    List<ClasificacionFinal> catClaFinal;
    List<Vacuna> catVacunas;
    List<TipoVacuna> catTVacHib;
    List<TipoVacuna> catTVacMenin;
    List<TipoVacuna> catTVacNeumo;
    List<TipoVacuna> catTVacFlu;
    List<CondicionPrevia> catCondPre;
    List<ManifestacionClinica> catManCli;
    List<ClasificacionFinalNB> catClasFNB;
    List<ClasificacionFinalNV> catClasFNV;
    Map<String, Object> mapModel;


    void Initialize() throws Exception {
        try {


            departamentos = divisionPoliticaService.getAllDepartamentos();
            catProcedencia = catalogoService.getProcedencia();
            catClasif = catalogoService.getClasificacion();
            catCaptac = catalogoService.getCaptacion();
            catResp = catalogoService.getRespuesta();
            catVia = catalogoService.getViaAntibiotico();
            catResRad = catalogoService.getResultadoRadiologia();
            catConEgreso = catalogoService.getCondicionEgreso();
            catClaFinal = catalogoService.getClasificacionFinal();
            catVacunas = catalogoService.getVacuna();
            catCondPre = catalogoService.getCondicionPrevia();
            catManCli = catalogoService.getManifestacionClinica();
            catTVacHib = catalogoService.getTipoVacunaHib();
            catTVacMenin = catalogoService.getTipoVacunaMeningococica();
            catTVacNeumo = catalogoService.getTipoVacunaNeumococica();
            catTVacFlu = catalogoService.getTipoVacunaFlu();
            catClasFNB = catalogoService.getClasificacionFinalNB();
            catClasFNV = catalogoService.getClasificacionFinalNV();

            mapModel = new HashMap<String, Object>();

            mapModel.put("catProcedencia", catProcedencia);
            mapModel.put("catClasif", catClasif);
            mapModel.put("catCaptac", catCaptac);
            mapModel.put("catResp", catResp);
            mapModel.put("catVia", catVia);
            mapModel.put("catResRad", catResRad);
            mapModel.put("catConEgreso", catConEgreso);
            mapModel.put("catClaFinal", catClaFinal);
            mapModel.put("catVacunas", catVacunas);
            mapModel.put("catTVacHib", catTVacHib);
            mapModel.put("catTVacMenin", catTVacMenin);
            mapModel.put("catTVacNeumo", catTVacNeumo);
            mapModel.put("catTVacFlu", catTVacFlu);
            mapModel.put("catCondPre", catCondPre);
            mapModel.put("catManCli", catManCli);
            mapModel.put("departamentos", departamentos);
            mapModel.put("catNV", catClasFNV);
            mapModel.put("catNB", catClasFNB);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar una ficha de IRAG/ETI");

        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        if(urlValidacion.isEmpty()){
            return "irag/search";
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
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        List<DaNotificacion> results = daNotificacionService.getDaNotPersona(idPerson, "TPNOTI|IRAG");
        ModelAndView mav = new ModelAndView();

        if(urlValidacion.isEmpty()){
            if (results.size() == 0) {
                boolean autorizado = true;
                DaIrag irag = new DaIrag();
                DaNotificacion noti = new DaNotificacion();
                Initialize();
                SisPersona persona = personaService.getPersona(idPerson);

                long idUsuario = seguridadService.obtenerIdUsuario(request);
                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                }

                if (persona != null) {
                    noti.setPersona(persona);
                    Divisionpolitica departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(noti.getPersona().getMunicipioResidencia().getCodigoNacional());
                    List<Divisionpolitica> municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                    List<Comunidades> comunidades = comunidadesService.getComunidades(noti.getPersona().getMunicipioResidencia().getCodigoNacional());

                    mav.addObject("entidades", entidades);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("departamentoProce", departamentoProce);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("formVI", irag);
                    mav.addObject("noti", noti);
                    mav.addObject("fVacuna", new DaVacunasIrag());
                    mav.addObject("fCondPre", new DaCondicionesPreviasIrag());
                    mav.addObject("fCM", new DaManifestacionesIrag());
                    mav.addAllObjects(mapModel);
                    mav.setViewName("irag/create");
                } else {
                    mav.setViewName("404");
                }
            } else {
                mav.addObject("records", results);
                mav.setViewName("irag/results");
            }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * Handler for edit reports.
     *
     * @param idNotificacion the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idNotificacion}")
    public ModelAndView editIrag(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        boolean autorizado = true;
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {

            if(idNotificacion != null){
                DaIrag irag = daIragService.getFormById(idNotificacion);
                DaNotificacion noti = daNotificacionService.getNotifById(idNotificacion);

                if (irag != null) {
                    Initialize();

                    long idUsuario = seguridadService.obtenerIdUsuario(request);
                    irag.setUsuario(usuarioService.getUsuarioById((int) idUsuario));
                    if (idUsuario != 0) {
                        autorizado = seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, noti.getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, noti.getCodUnidadAtencion().getCodigo());
                    }

                    entidades = entidadAdmonService.getAllEntidadesAdtvas();

                    Divisionpolitica municipio = divisionPoliticaService.getMunicipiosByUnidadSalud(noti.getCodUnidadAtencion().getMunicipio().getCodigoNacional());
                    List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(noti.getCodSilaisAtencion().getCodigo());
                    List<Unidades> uni = unidadesService.getPUnitsHospByMuniAndSilais(municipio.getCodigoNacional(), HealthUnitType.UnidadesPrimHosp.getDiscriminator().split(","), noti.getCodSilaisAtencion().getCodigo());

                    //datos persona
                    Divisionpolitica departamentoProce = null;
                    List<Divisionpolitica> municipiosResi = null;
                    List<Comunidades> comunidades = null;

                    if(noti.getPersona().getMunicipioResidencia() != null){
                        String municipioResidencia = noti.getPersona().getMunicipioResidencia().getCodigoNacional();
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(municipioResidencia);
                        municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                        String comu = noti.getPersona().getMunicipioResidencia().getCodigoNacional();
                        comunidades = comunidadesService.getComunidades(comu);
                    }


                    mav.addObject("formVI", irag);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("entidades", entidades);
                    mav.addObject("munic", munic);
                    mav.addObject("departamentoProce", departamentoProce);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("uni", uni);
                    mav.addObject("irag", irag);
                    mav.addObject("fVacuna", new DaVacunasIrag());
                    mav.addObject("fCondPre", new DaCondicionesPreviasIrag());
                    mav.addObject("fCM", new DaManifestacionesIrag());
                    mav.addObject("municipio", municipio);
                    mav.addObject("noti", noti);
                    mav.addAllObjects(mapModel);

                    mav.setViewName("irag/create");
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

    @RequestMapping(value = "saveIrag", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProcessCreationFicha(
            @RequestParam(value = "codSilaisAtencion", required = true) Integer codSilaisAtencion
            , @RequestParam(value = "codUnidadAtencion", required = true) Integer codUnidadAtencion
            , @RequestParam(value = "fechaConsulta", required = true) String fechaConsulta
            , @RequestParam(value = "fechaPrimeraConsulta", required = false) String fechaPrimeraConsulta
            , @RequestParam(value = "codExpediente", required = false) String codExpediente
            , @RequestParam(value = "codClasificacion", required = false) String codClasificacion
            , @RequestParam(value = "nombreMadreTutor", required = false) String nombreMadreTutor
            , @RequestParam(value = "codProcedencia", required = true) String codProcedencia
            , @RequestParam(value = "codCaptacion", required = false) String codCaptacion
            , @RequestParam(value = "diagnostico", required = false) String diagnostico
            , @RequestParam(value = "tarjetaVacuna", required = false) Integer tarjetaVacuna
            , @RequestParam(value = "fechaInicioSintomas", required = false) String fechaInicioSintomas
            , @RequestParam(value = "codAntbUlSem", required = false) String codAntbUlSem
            , @RequestParam(value = "cantidadAntib", required = false) Integer cantidadAntib
            , @RequestParam(value = "nombreAntibiotico", required = false) String nombreAntibiotico
            , @RequestParam(value = "fechaPrimDosisAntib", required = false) String fechaPrimDosisAntib
            , @RequestParam(value = "fechaUltDosisAntib", required = false) String fechaUltDosisAntib
            , @RequestParam(value = "codViaAntb", required = false) String codViaAntb
            , @RequestParam(value = "noDosisAntib", required = false) Integer noDosisAntib
            , @RequestParam(value = "usoAntivirales", required = false) String usoAntivirales
            , @RequestParam(value = "nombreAntiviral", required = false) String nombreAntiviral
            , @RequestParam(value = "fechaPrimDosisAntiviral", required = false) String fechaPrimDosisAntiviral
            , @RequestParam(value = "fechaUltDosisAntiviral", required = false) String fechaUltDosisAntiviral
            , @RequestParam(value = "noDosisAntiviral", required = false) Integer noDosisAntiviral
            , @RequestParam(value = "codResRadiologia", required = false) String codResRadiologia
            , @RequestParam(value = "otroResultadoRadiologia", required = false) String otroResultadoRadiologia
            , @RequestParam(value = "uci", required = false) Integer uci
            , @RequestParam(value = "noDiasHospitalizado", required = false) Integer noDiasHospitalizado
            , @RequestParam(value = "ventilacionAsistida", required = false) Integer ventilacionAsistida
            , @RequestParam(value = "diagnostico1Egreso", required = false) String diagnostico1Egreso
            , @RequestParam(value = "diagnostico2Egreso", required = false) String diagnostico2Egreso
            , @RequestParam(value = "fechaEgreso", required = false) String fechaEgreso
            , @RequestParam(value = "codCondEgreso", required = false) String codCondEgreso
            , @RequestParam(value = "personaId", required = false) Integer personaId
            , @RequestParam(value = "codClasFDetalleNV", required = false) String codClasFDetalleNV
            , @RequestParam(value = "codClasFDetalleNV", required = false) String codClasFDetalleNB
            , @RequestParam(value = "idNotificacion.idNotificacion", required = false) String idNotificacion, HttpServletRequest request


    ) throws Exception {

        logger.debug("Agregando o actualizando formulario Irag");

        DaIrag irag;
        if (!idNotificacion.equals("")) {
            irag = daIragService.getFormById(idNotificacion);
        } else {
            irag = new DaIrag();
        }

        long idUsuario = seguridadService.obtenerIdUsuario(request);
        irag.setUsuario(usuarioService.getUsuarioById((int)idUsuario));


       // if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, codSilaisAtencion) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, codUnidadAtencion)) {



            if (!codClasificacion.isEmpty()) {
                irag.setCodClasificacion(catalogoService.getClasificacion(codClasificacion));
            }

            irag.setCodExpediente(codExpediente);

            if (!fechaConsulta.equals("")) {
                irag.setFechaConsulta(StringToDate(fechaConsulta));
            }
            if (!fechaPrimeraConsulta.equals("")) {
                irag.setFechaPrimeraConsulta(StringToDate(fechaPrimeraConsulta));
            }

            irag.setNombreMadreTutor(nombreMadreTutor);

            if (!codCaptacion.isEmpty()) {
                irag.setCodCaptacion(catalogoService.getCaptacion(codCaptacion));
            }

            irag.setDiagnostico(diagnostico);

            if (tarjetaVacuna != null) {
                irag.setTarjetaVacuna(tarjetaVacuna);
            }

            if (!fechaInicioSintomas.equals("")) {
                irag.setFechaInicioSintomas(StringToDate(fechaInicioSintomas));
            }

            if (!codAntbUlSem.isEmpty()) {
                irag.setCodAntbUlSem(catalogoService.getRespuesta(codAntbUlSem));
            }

            irag.setCantidadAntib(cantidadAntib);
            irag.setNombreAntibiotico(nombreAntibiotico);

            if (!fechaPrimDosisAntib.equals("")) {
                irag.setFechaPrimDosisAntib(StringToDate(fechaPrimDosisAntib));
            }

            if (!fechaUltDosisAntib.equals("")) {
                irag.setFechaUltDosisAntib(StringToDate(fechaUltDosisAntib));
            }

            irag.setNoDosisAntib(noDosisAntib);

            if (!codViaAntb.equals("")) {
                irag.setCodViaAntb(catalogoService.getViaAntibiotico(codViaAntb));
            }

            if (!usoAntivirales.equals("")) {
                irag.setUsoAntivirales(catalogoService.getRespuesta(usoAntivirales));
            }

            irag.setNombreAntiviral(nombreAntiviral);

            if (!fechaPrimDosisAntiviral.equals("")) {
                irag.setFechaPrimDosisAntiviral(StringToDate(fechaPrimDosisAntiviral));
            }

            if (!fechaUltDosisAntiviral.equals("")) {
                irag.setFechaUltDosisAntiviral(StringToDate(fechaUltDosisAntiviral));
            }

            irag.setNoDosisAntiviral(noDosisAntiviral);
            irag.setCodResRadiologia(codResRadiologia);
            irag.setOtroResultadoRadiologia(otroResultadoRadiologia);

            if (uci != null) {
                irag.setUci(uci);
            }

            irag.setNoDiasHospitalizado(noDiasHospitalizado);

            if (ventilacionAsistida != null) {
                irag.setVentilacionAsistida(ventilacionAsistida);
            }

            irag.setDiagnostico1Egreso(diagnostico1Egreso);
            irag.setDiagnostico2Egreso(diagnostico2Egreso);

            if (!fechaEgreso.equals("")) {
                irag.setFechaEgreso(StringToDate(fechaEgreso));
            }

            irag.setCodCondEgreso(catalogoService.getCondicionEgreso(codCondEgreso));

            irag.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));

            irag.setFechaRegistro(new Timestamp(new Date().getTime()));

            irag.setCodClasFDetalleNB(catalogoService.getClasificacionFinalNB(codClasFDetalleNB));
            irag.setCodClasFDetalleNV(catalogoService.getClasificacionFinalNV(codClasFDetalleNV));

            if (irag.getIdNotificacion() == null) {
              DaNotificacion noti =  guardarNotificacion(personaId, request, codSilaisAtencion, codUnidadAtencion);
                irag.setIdNotificacion(daNotificacionService.getNotifById(noti.getIdNotificacion()));
                 daIragService.addNotificationIrag(irag);
            } else {
                daIragService.updateIrag(irag);
            }
            return createJsonResponse(irag);
      /*  }else{
           throw new Exception();
        }*/

    }


    @RequestMapping(value = "saveNotification", method = RequestMethod.GET)
    public DaNotificacion guardarNotificacion(@PathVariable("personaId") Integer personaId, HttpServletRequest request, Integer silais, Integer unidad) throws Exception {

        logger.debug("Guardando Notificacion");
        DaNotificacion noti = new DaNotificacion();

        if(personaId != 0){
            noti.setPersona(personaService.getPersona(personaId));
            noti.setFechaRegistro(new Timestamp(new Date().getTime()));
            noti.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(silais));
            noti.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(unidad));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //  noti.setUsuarioRegistro(usuarioService.getUsuarioById((int)idUsuario));
            noti.setUsuarioRegistro(usuarioService.getUsuarioById(1));
            noti.setCodTipoNotificacion(catalogoService.getTipoNotificacion("TPNOTI|IRAG"));
            daNotificacionService.addNotification(noti);
            return noti;
        }else{
            throw new Exception();
        }

    }


    @RequestMapping(value = "completeIrag", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<String> getProcessCreationFicha (
              @RequestParam(value = "codClasFCaso", required = false) String codClasFCaso
            , @RequestParam(value = "agenteBacteriano", required = false) String agenteBacteriano
            , @RequestParam(value = "serotipificacion", required = false) String serotipificacion
            , @RequestParam(value = "agenteViral", required = false) String agenteViral
            , @RequestParam(value = "agenteEtiologico", required = false) String agenteEtiologico
            , @RequestParam(value = "idNotificacion.idNotificacion", required = false) String idNotificacion
            , @RequestParam(value = "persona.personaId", required = false) Integer personaId, HttpServletRequest request


    ) throws Exception {

        logger.debug("Completando Formulario Irag");

        DaIrag irag;
        DaNotificacion noti = null;
        if (!idNotificacion.equals("")) {
            irag = daIragService.getFormById(idNotificacion);
            noti = daNotificacionService.getNotifById(idNotificacion);
        } else {
            irag = new DaIrag();
        }

        long idUsuario = seguridadService.obtenerIdUsuario(request);
        irag.setUsuario(usuarioService.getUsuarioById((int)idUsuario));
        //irag.setUsuario(usuarioService.getUsuarioById(1));
        if (noti != null){
            if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, noti.getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, noti.getCodUnidadAtencion().getCodigo())) {
                irag.setCodClasFCaso(codClasFCaso);
                irag.setAgenteBacteriano(agenteBacteriano);
                irag.setSerotipificacion(serotipificacion);
                irag.setAgenteViral(agenteViral);
                irag.setAgenteEtiologico(agenteEtiologico);
                irag.setFichaCompleta(true);
                irag.setFechaRegistro(new Timestamp(new Date().getTime()));
                daIragService.updateIrag(irag);
                return createJsonResponse(irag);

            }else{
                throw new Exception();
            }
        }else{
            throw new Exception();
        }

    }

    @RequestMapping(value = "updatePerson", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePerson(
            @RequestParam(value = "municipioResidencia.codigoNacional", required = false) String municipioResidencia
            , @RequestParam(value = "noti.persona.comunidadResidencia.codigo", required = false) String comunidadResidencia
            , @RequestParam(value = "direccionResidencia", required = false) String direccionResidencia
            , @RequestParam(value = "telefonoResidencia", required = false) String telefonoResidencia
            , @RequestParam(value = "personaId", required = false) Integer personaId

    ) throws Exception {

        logger.debug("Actualizando datos persona");
        SisPersona pers = new SisPersona();

        if (personaId != null) {
            pers = personaService.getPersona(personaId);
            pers.setMunicipioResidencia(divisionPoliticaService.getDivisionPolitiacaByCodNacional(municipioResidencia));
            pers.setComunidadResidencia(comunidadesService.getComunidad(comunidadResidencia));
            pers.setDireccionResidencia(direccionResidencia);
            pers.setTelefonoResidencia(telefonoResidencia);
            personaService.saveOrUpdatePerson(pers);

        }
        return createJsonResponse(pers);
    }


    private ResponseEntity<String> createJsonResponse(Object o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
    }

    /**
     * Override form
     *
     * @param idNotificacion the ID of the form
     *
     */
    @RequestMapping(value = "override/{idNotificacion}")
    public ModelAndView overrideForm(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        DaIrag irag = null;
        Long personaId = null;
        if (idNotificacion != null) {
            irag = daIragService.getFormById(idNotificacion);
            irag.setAnulada(true);
            irag.setFechaAnulacion(new Timestamp(new Date().getTime()));
            daIragService.updateIrag(irag);
            personaId = irag.getIdNotificacion().getPersona().getPersonaId();
        }
        return showPersonReport(personaId, request);
    }

    @RequestMapping(value = "newVaccine", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> processCreationVaccine(@RequestParam(value = "codVacuna", required = true) String codVacuna,
                                                         @RequestParam(value = "codTipoVacuna", required = true) String codTipoVacuna,
                                                         @RequestParam(value = "dosis", required = false) Integer dosis,
                                                         @RequestParam(value = "fechaUltimaDosis", required = false) String fechaUltimaDosis,
                                                         @RequestParam(value = "idNotificacion.idNotificacion", required = false) String idNotificacion,
                                                         ModelMap model, HttpServletRequest request) throws Exception {

        String error = null;
        DaVacunasIrag vacunas = new DaVacunasIrag();

        if (idNotificacion != null) {
            //buscar si existe registrada la vacuna y el tipo de vacuna
            DaVacunasIrag vac = daVacunasIragService.searchVaccineRecord(idNotificacion, codVacuna, codTipoVacuna);

            if (vac == null) {
                vacunas.setIdNotificacion(daIragService.getFormById(idNotificacion));

                long idUsuario = seguridadService.obtenerIdUsuario(request);
                vacunas.setUsuario(usuarioService.getUsuarioById((int)idUsuario));

                vacunas.setFechaRegistro(new Timestamp(new Date().getTime()));
                vacunas.setCodVacuna(catalogoService.getVacuna(codVacuna));
                vacunas.setCodTipoVacuna(catalogoService.getTipoVacuna(codTipoVacuna));
                vacunas.setDosis(dosis);
                if (!fechaUltimaDosis.equals("")) {
                    vacunas.setFechaUltimaDosis(StringToDate(fechaUltimaDosis));
                }

                daVacunasIragService.addVaccine(vacunas);


            } else {
                error = messageSource.getMessage("msg.error.existing.record", null, null);
                model.addAttribute("error", error);
                throw new Exception(error);

            }
        } else {
                error = messageSource.getMessage("msg.error.save.form", null, null);
                model.addAttribute("error", error);
                throw new Exception(error);


        }
        return createJsonResponse(vacunas);

    }

    @RequestMapping(value = "vaccines", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaVacunasIrag> loadVaccines(@RequestParam(value = "idNotificacion", required = false) String idNotificacion) {
        logger.info("Obteniendo las vacunas agregadas");

        List<DaVacunasIrag> listaVacunas = null;

        if (idNotificacion != null) {
            listaVacunas = daVacunasIragService.getAllVaccinesByIdIrag(idNotificacion);

            if (listaVacunas.isEmpty()) {
                logger.debug("Null");
            }
        }
        return listaVacunas;
    }


    @RequestMapping(value = "newPreCondition", method = RequestMethod.GET)
    public ResponseEntity<String> processCreationPreCondition(@RequestParam(value = "codCondicion", required = true) String codCondicion,
                                                              @RequestParam(value = "semanasEmbarazo", required = false) Integer semanasEmbarazo,
                                                              @RequestParam(value = "otraCondicion", required = false) String otraCondicion,
                                                              @RequestParam(value = "idNotificacion.idNotificacion", required = false) String idNotificacion, HttpServletRequest request) throws Exception {

        DaCondicionesPreviasIrag condicion = new DaCondicionesPreviasIrag();

        if (idNotificacion != null) {

            //buscar si existe registro de condicion
            DaCondicionesPreviasIrag cond = daCondicionesIragService.searchConditionRecord(codCondicion, idNotificacion);


            if (cond == null) {
                condicion.setIdNotificacion(daIragService.getFormById(idNotificacion));
                long idUsuario = seguridadService.obtenerIdUsuario(request);
                condicion.setUsuario(usuarioService.getUsuarioById(1));
              //  condicion.setUsuario(usuarioService.getUsuarioById((int)idUsuario));
                condicion.setFechaRegistro(new Timestamp(new Date().getTime()));
                condicion.setCodCondicion(catalogoService.getCondicionPrevia(codCondicion));
                condicion.setSemanasEmbarazo(semanasEmbarazo);
                condicion.setOtraCondicion(otraCondicion);

                daCondicionesIragService.addCondition(condicion);
            } else {
                throw new Exception("La condicion ya existe");
            }
        } else {
            throw new Exception("Debe guardar primeramente el formulario irag antes de agregar una condicion.");
        }

        return createJsonResponse(condicion);
    }

    @RequestMapping(value = "conditions", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaCondicionesPreviasIrag> loadConditions(@RequestParam(value = "idNotificacion", required = false) String idNotificacion) {
        logger.info("Obteniendo las condiciones agregadas");

        List<DaCondicionesPreviasIrag> listaCondiciones = null;

        if (idNotificacion != null) {
            listaCondiciones = daCondicionesIragService.getAllConditionsByIdIrag(idNotificacion);

            if (listaCondiciones.isEmpty()) {
                logger.debug("Null");
            }
        }


        return listaCondiciones;
    }


    @RequestMapping(value = "newCM", method = RequestMethod.GET)

    public ResponseEntity<String> processCreationClinicalMan(@RequestParam(value = "codManifestacion", required = true) String codManifestacion,
                                                             @RequestParam(value = "otraManifestacion", required = false) String otraManifestacion,
                                                             @RequestParam(value = "idNotificacion.idNotificacion", required = false) String idNotificacion, HttpServletRequest request) throws Exception {

        DaManifestacionesIrag manifestacion = new DaManifestacionesIrag();

        if (idNotificacion != null) {
            //buscar si existe registro de condicion
            DaManifestacionesIrag manif = daManifestacionesIragService.searchManifestationRecord(codManifestacion, idNotificacion);


            if (manif == null) {
                long idUsuario = seguridadService.obtenerIdUsuario(request);
                manifestacion.setUsuario(usuarioService.getUsuarioById((int)idUsuario));
                manifestacion.setFechaRegistro(new Timestamp(new Date().getTime()));
                manifestacion.setCodManifestacion(catalogoService.getManifestacionClinica(codManifestacion));
                manifestacion.setIdNotificacion(daIragService.getFormById(idNotificacion));
                manifestacion.setOtraManifestacion(otraManifestacion);

                daManifestacionesIragService.addManifestation(manifestacion);
            } else {
                throw new Exception("La manifestacion clinica ya existe");
            }

        } else {
            throw new Exception("Debe guardar primeramente el formulario irag antes de agregar una manifestacion clinica.");

        }
        return createJsonResponse(manifestacion);
    }

    @RequestMapping(value = "manifestations", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaManifestacionesIrag> loadManifestations(@RequestParam(value = "idNotificacion", required = false) String idNotificacion) {
        logger.info("Obteniendo las Manifestaciones Clínicas agregadas");

        List<DaManifestacionesIrag> listaMani = null;

        if (idNotificacion != null) {
            listaMani = daManifestacionesIragService.getAllManifestationsByIdIrag(idNotificacion);

            if (listaMani.isEmpty()) {
                logger.debug("Null");
            }
        }


        return listaMani;
    }


    /**
     * Override Vaccine
     *
     * @param idVacuna the ID of the form
     *
     */
    @RequestMapping(value = "overrideVaccine/{idVacuna}" ,method = RequestMethod.GET )
    public ModelAndView overrideVaccine(@PathVariable("idVacuna") Integer idVacuna, HttpServletRequest request) throws Exception {
        DaVacunasIrag  va = daVacunasIragService.getVaccineById(idVacuna);
        va.setPasivo(true);
        daVacunasIragService.updateVaccine(va);
        String idNotificacion = va.getIdNotificacion().getIdNotificacion().getIdNotificacion();

       return editIrag(idNotificacion, request);
    }

    /**
     * Override Condition
     *
     * @param idCondicion the ID of the form
     *
     */
    @RequestMapping(value = "overrideCondition/{idCondicion}" ,method = RequestMethod.GET )
    public ModelAndView overrideCondition(@PathVariable("idCondicion") Integer idCondicion, HttpServletRequest request) throws Exception {
        DaCondicionesPreviasIrag  cond = daCondicionesIragService.getConditionById(idCondicion);
        cond.setPasivo(true);
        daCondicionesIragService.updateCondition(cond);
        String idNotificacion = cond.getIdNotificacion().getIdNotificacion().getIdNotificacion();

        return editIrag(idNotificacion, request);
    }

    /**
     * Override Manifestation
     *
     * @param idManifestacion the ID of the form
     *
     */
    @RequestMapping(value = "overrideManifestation/{idManifestacion}" ,method = RequestMethod.GET )
    public ModelAndView overrideManifestation(@PathVariable("idManifestacion") Integer idManifestacion, HttpServletRequest request) throws Exception {
        DaManifestacionesIrag mani = daManifestacionesIragService.getManifestationById(idManifestacion);
        mani.setPasivo(true);
        daManifestacionesIragService.updateManifestation(mani);
        String idNotificacion = mani.getIdNotificacion().getIdNotificacion().getIdNotificacion();

        return editIrag(idNotificacion, request);
    }


    /**
     * Convierte un Date a string con formato dd/MM/yyyy
     *
     * @param dtFecha fecha a convertir
     * @return String
     * @throws java.text.ParseException
     */
    private String DateToString(Date dtFecha) throws ParseException {
        String date;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        date = DATE_FORMAT.format(dtFecha);
        return date;
    }

    private Date StringToDate(String strFecha) throws ParseException {
        DateFormat formatter;
        Date date;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.parse(strFecha);
        return date;
    }


}
