package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.irag.*;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.service.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
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



    List<EntidadesAdtvas> entidades;
    List<Procedencia> catProcedencia;
    List<Clasificacion> catClasif;
    List<Captacion> catCaptac;
    List<Respuesta> catResp;
    List<ViaAntibiotico> catVia;
    List<ResultadoRadiologia> catResRad;
    List<CondicionEgreso> catConEgreso;
    List<ClasificacionFinal> catClaFinal;
    List<Vacuna> catVacunas;
    List<TipoVacuna> catTipoVacuna;
    List<CondicionPrevia> catCondPre;
    List<ManifestacionClinica> catManCli;
    Map<String, Object> mapModel;

   /* @Autowired
    @Qualifier(value = "sispersonaService")
    private SisPersonasService sisPersonasService;*/

    DaIrag irag = new DaIrag();
    DaVacunasIrag vacunas = new DaVacunasIrag();
    DaCondicionesPreviasIrag condicion = new DaCondicionesPreviasIrag();
    DaManifestacionesIrag manifestacion = new DaManifestacionesIrag();


/*
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String buscarFicha(Model model) throws Exception {
        List<DaIrag> vi = daIragService.getAllFormActivos();
        model.addAttribute("fichas", vi);
        return "search";
    }*/

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initCreationForm(Model model) throws Exception {
        logger.debug("Getting data from IRAG");

            entidades = entidadAdmonService.getAllEntidadesAdtvas();
            catProcedencia = catalogoService.getProcedencia();
            catClasif = catalogoService.getClasificacion();
            catCaptac = catalogoService.getCaptacion();
            catResp = catalogoService.getRespuesta();
            catVia = catalogoService.getViaAntibiotico();
            catResRad = catalogoService.getResultadoRadiologia();
            catConEgreso = catalogoService.getCondicionEgreso();
            catClaFinal = catalogoService.getClasificacionFinal();
            catVacunas = catalogoService.getVacuna();
            catTipoVacuna = catalogoService.getTipoVacuna();
            catCondPre = catalogoService.getCondicionPrevia();
            catManCli = catalogoService.getManifestacionClinica();

            model.addAttribute("entidades", entidades);
            model.addAttribute("catProcedencia", catProcedencia);
            model.addAttribute("catClasif", catClasif);
            model.addAttribute("catCaptac", catCaptac);
            model.addAttribute("catVia", catVia);
            model.addAttribute("catResRad", catResRad);
            model.addAttribute("catConEgreso", catConEgreso);
            model.addAttribute("catClaFinal", catClaFinal);
            model.addAttribute("catVacunas", catVacunas);
            model.addAttribute("catTipoVacuna", catTipoVacuna);
            model.addAttribute("catCondPre", catCondPre);
            model.addAttribute("catManCli", catManCli);
            model.addAttribute("formVI", irag);
            model.addAttribute("fVacuna", new DaVacunasIrag());
            model.addAttribute("fCondPre", new DaCondicionesPreviasIrag());
            model.addAttribute("fCM", new DaManifestacionesIrag());
            model.addAttribute("today", "18/10/2014");
            return "irag/create";

    }



    @RequestMapping(value = "update/{idIrag}", method = RequestMethod.GET)
    public ModelAndView initUpdateFichaForm(@PathVariable("idIrag") String idIrag) throws Exception {
        ModelAndView mav = new ModelAndView("create");
        irag = daIragService.getFormById(idIrag);

        irag.setIdIrag(idIrag);

        mav.addObject("formVI", irag);
        mav.addObject("fVacuna", new DaVacunasIrag());
        mav.addObject("fCondPre", new DaCondicionesPreviasIrag());
        mav.addObject("fCM", new DaManifestacionesIrag());
        mav.addAllObjects(mapModel);
        return
                mav;
    }

    @RequestMapping(value = "saveIrag", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProcessCreationFicha(
            @RequestParam(value="codSilaisAtencion", required=true ) Integer codSilaisAtencion
            ,@RequestParam(value="codUnidadAtencion", required=true ) Integer codUnidadAtencion
            ,@RequestParam(value="fechaConsulta", required=true ) String fechaConsulta
            ,@RequestParam(value="fechaPrimeraConsulta", required=false) String fechaPrimeraConsulta
            ,@RequestParam(value="codExpediente", required=false ) String codExpediente
            ,@RequestParam(value = "codClasificacion", required=false) String codClasificacion
            ,@RequestParam(value = "nombreMadreTutor",required=false) String nombreMadreTutor
            ,@RequestParam(value = "codProcedencia", required=true) String codProcedencia
            ,@RequestParam(value = "codCaptacion", required=false ) String codCaptacion
            ,@RequestParam(value = "diagnostico", required=false) String diagnostico
            ,@RequestParam(value = "tarjetaVacuna", required=false) Integer tarjetaVacuna
            ,@RequestParam(value = "fechaInicioSintomas", required=false) String fechaInicioSintomas
            ,@RequestParam(value = "codAntbUlSem", required=false) String codAntbUlSem
            ,@RequestParam(value = "cantidadAntib", required=false) Integer cantidadAntib
            ,@RequestParam(value = "nombreAntibiotico", required=false) String nombreAntibiotico
            ,@RequestParam(value = "fechaPrimDosisAntib", required=false) String fechaPrimDosisAntib
            ,@RequestParam(value = "fechaUltDosisAntib", required=false) String fechaUltDosisAntib
            ,@RequestParam(value = "codViaAntb", required=false) String codViaAntb
            ,@RequestParam(value = "noDosisAntib", required=false) Integer noDosisAntib
            ,@RequestParam(value = "usoAntivirales", required=false) Integer usoAntivirales
            ,@RequestParam(value = "nombreAntiviral", required=false) String nombreAntiviral
            ,@RequestParam(value = "fechaPrimDosisAntiviral", required=false) String fechaPrimDosisAntiviral
            ,@RequestParam(value = "fechaUltDosisAntiviral", required=false) String fechaUltDosisAntiviral
            ,@RequestParam(value = "noDosisAntiviral", required=false) Integer noDosisAntiviral
            ,@RequestParam(value = "codResRadiologia", required=false) String codResRadiologia
            ,@RequestParam(value = "otroResultadoRadiologia", required=false) String otroResultadoRadiologia
            ,@RequestParam(value = "uci", required=false) Integer uci
            ,@RequestParam(value = "noDiasHospitalizado", required=false) Integer noDiasHospitalizado
            ,@RequestParam(value = "ventilacionAsistida", required=false) Integer ventilacionAsistida
            ,@RequestParam(value = "diagnostico1Egreso", required=false) String diagnostico1Egreso
            ,@RequestParam(value = "diagnostico2Egreso", required=false) String diagnostico2Egreso
            ,@RequestParam(value = "fechaEgreso", required=false) String fechaEgreso
            ,@RequestParam(value = "codCondEgreso", required=false) String codCondEgreso
            ,@RequestParam(value = "codClasFCaso", required=false) String codClasFCaso
            ,@RequestParam(value = "agenteBacteriano", required=false) String agenteBacteriano
            ,@RequestParam(value = "serotipificacion", required=false) String serotipificacion
            ,@RequestParam(value = "agenteViral", required=false) String agenteViral
            ,@RequestParam(value = "codigoConfDescartado", required=false) String codigoConfDescartado
            ,@RequestParam(value = "agenteEtiologico",required=false) String agenteEtiologico) throws Exception {

        logger.debug("Registrando nueva ficha de Vigilancia Integrada");

        irag.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
        irag.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
        if (!codClasificacion.isEmpty()){
            irag.setCodClasificacion(catalogoService.getClasificacion(codClasificacion));
        }

        irag.setCodExpediente(codExpediente);
        irag.setUsuario(usuarioService.getUsuarioById(3));
        if(!fechaConsulta.equals("")){
            irag.setFechaConsulta(new Date(fechaConsulta));
        }
        if(!fechaPrimeraConsulta.equals("")){
            irag.setFechaPrimeraConsulta(new Date(fechaPrimeraConsulta));
        }
        irag.setNombreMadreTutor(nombreMadreTutor);

        if(!codCaptacion.isEmpty()){
            irag.setCodCaptacion(catalogoService.getCaptacion(codCaptacion));
        }

        irag.setDiagnostico(diagnostico);

        if(tarjetaVacuna != null){
            if(tarjetaVacuna.equals(1)){
                irag.setTarjetaVacuna(true);
            }else{
                irag.setTarjetaVacuna(false);
            }
        }

        if(!fechaInicioSintomas.equals("")){
            irag.setFechaInicioSintomas(new Date (fechaInicioSintomas));
        }

        if(!codAntbUlSem.isEmpty()){
            irag.setCodAntbUlSem(catalogoService.getRespuesta(codAntbUlSem));
        }

        irag.setCantidadAntib(cantidadAntib);
        irag.setNombreAntibiotico(nombreAntibiotico);

        if(!fechaPrimDosisAntib.equals("")){
            irag.setFechaPrimDosisAntib(new Date(fechaPrimDosisAntib));
        }

        if(!fechaUltDosisAntib.equals("")){
            irag.setFechaUltDosisAntib(new Date(fechaUltDosisAntib));
        }

        irag.setNoDosisAntib(noDosisAntib);

        if (!codViaAntb.isEmpty()){
            irag.setCodViaAntb(catalogoService.getViaAntibiotico(codViaAntb));
        }

        if(usoAntivirales != null){
            if(usoAntivirales.equals(1)){
                irag.setUsoAntivirales(true);
            }else{
                irag.setUsoAntivirales(false);
            }
        }

        irag.setNombreAntiviral(nombreAntiviral);

        if(!fechaPrimDosisAntiviral.equals("")){
            irag.setFechaPrimDosisAntiviral(new Date (fechaPrimDosisAntiviral));
        }

        if(!fechaUltDosisAntiviral.equals("")){
            irag.setFechaUltDosisAntiviral(new Date (fechaUltDosisAntiviral));
        }

        irag.setNoDosisAntiviral(noDosisAntiviral);
        irag.setCodResRadiologia(catalogoService.getResultadoRadiologia(codResRadiologia));
        irag.setOtroResultadoRadiologia(otroResultadoRadiologia);

        if(uci != null){
            if(uci.equals(1)){
                irag.setUci(true);
            }else{
                irag.setUci(false);
            }
        }

        irag.setNoDiasHospitalizado(noDiasHospitalizado);

        if(ventilacionAsistida != null){
            if(ventilacionAsistida.equals(1)){
                irag.setVentilacionAsistida(true);
            }else{
                irag.setVentilacionAsistida(false);
            }
        }

        irag.setDiagnostico1Egreso(diagnostico1Egreso);
        irag.setDiagnostico2Egreso(diagnostico2Egreso);

        if(!fechaEgreso.equals("")){
            irag.setFechaEgreso( new Date(fechaEgreso));
        }

        irag.setCodCondEgreso(catalogoService.getCondicionEgreso(codCondEgreso));
        irag.setCodClasFCaso(catalogoService.getClasificacionFinal(codClasFCaso));
        irag.setAgenteBacteriano(agenteBacteriano);
        irag.setSerotipificacion(serotipificacion);
        irag.setAgenteViral(agenteViral);
        irag.setAgenteEtiologico(agenteEtiologico);
        irag.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));

        irag.setFechaRegistro(new Timestamp(new Date().getTime()));

        //datos relacionados a persona
      //  irag.setPersona(6958);
/*
        SisPersonas p = sisPersonasService.getSisPersona(Integer.parseInt(idPersona));

        ficha.setPersonaId(Integer.parseInt(idPersona));
        ficha.setCodMunicipioProcedencia(Integer.parseInt(p.getCodigoMunicipioResidencia()));
        Divisionpolitica dP = divisionPoliticaService.getMunicipioByCN(p.getCodigoMunicipioResidencia());
*/

        if(irag.getIdIrag() == null)
        {
            String dto = daIragService.addIrag(irag);
            irag.setIdIrag(dto);
        }
        else
        {
            daIragService.updateIrag(irag);
        }
        return createJsonResponse(irag);
    }

    private ResponseEntity<String> createJsonResponse( Object o )
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson( o );
        return new ResponseEntity<String>( json, headers, HttpStatus.CREATED );
    }

    @RequestMapping(value = "cancel/{idIrag}" ,method = RequestMethod.GET )
    public String anularForm(@PathVariable("idIrag") String idIrag) throws Exception {
        irag = daIragService.getFormById(idIrag);
        irag.setAnulada(true);
        irag.setFechaAnulacion(new Timestamp(new Date().getTime()));
        daIragService.updateIrag(irag);
        return "redirect:/ficha/VigilanciaIntegrada/searchFormVI";
    }
/*
    @RequestMapping( value="newPreCondition", method= RequestMethod.GET)
    @ResponseBody
    public String processCreationPreCondition(@RequestParam(value = "codigoCondicion", required = true) String codigoCondicion,
                                         @RequestParam(value = "codigoRespuesta", required = true) String codigoRespuesta,
                                         @RequestParam(value = "semEmb", required = false) Integer semEmb,
                                         @RequestParam(value = "nombreOtraCondicion", required = false) String nombreOtraCondicion) throws Exception {

        condicion.setIdFichaVigilancia(irag.getIdFichaVigilancia());
        condicion.setUsuario(3);
        condicion.setFechaRegistro(new Timestamp(new Date().getTime()));
        condicion.setCodCondicion(codigoCondicion);
        condicion.setCodRespuesta(codigoRespuesta);
        condicion.setSemanasEmbarazo(semEmb);
        condicion.setOtraCondicion(nombreOtraCondicion);

        daCondPreVigilanciaService.addCondition(condicion);

        return  loadConditions();
    }


    @RequestMapping( value="newVaccine", method= RequestMethod.GET)
    @ResponseBody
    public String processCreationVaccine(@RequestParam(value = "codNombreVacuna", required = true) String codNombreVacuna,
                                         @RequestParam(value = "codAplicada", required = true) String codAplicada,
                                         @RequestParam(value = "codTipoVacuna", required = true) String codTipoVacuna,
                                         @RequestParam(value = "dosis", required = true) Integer dosis,
                                         @RequestParam(value = "fechaUltimaDosis", required = true) String fechaUltimaDosis) throws Exception {

        vacunas.setIdFichaVigilancia(irag.getIdFichaVigilancia());
        vacunas.setUsuario(3);
        vacunas.setFechaRegistro(new Timestamp(new Date().getTime()));
        vacunas.setCodVacuna(codNombreVacuna);
        vacunas.setCodAplicada(codAplicada);
        vacunas.setCodTipoVacuna(codTipoVacuna);
        vacunas.setDosis(dosis);

        if(!fechaUltimaDosis.equals("")){
            vacunas.setFechaUltimaDosis(new Date(fechaUltimaDosis));
        }

        daVacunasIragService.addVaccine(vacunas);

        return  loadVaccines();
    }

    @RequestMapping( value="newCM", method= RequestMethod.GET)
    @ResponseBody
    public String processCreationClinicalMan(@RequestParam(value = "codigoManifestacionClinica", required = true) String codigoManifestacionClinica,
                                              @RequestParam(value = "codigoRespMani", required = true) String codigoRespMani,
                                              @RequestParam(value = "otraManifestacionCli", required = false) String otraManifestacionCli ) throws Exception {


        manifestacion.setUsuarioRegistroId(3);
        manifestacion.setFechaRegistro(new Timestamp(new Date().getTime()));
        manifestacion.setCodigoManifestacionClinica(codigoManifestacionClinica);
        manifestacion.setIdFichaVigilancia(ficha.getIdFichaVigilancia());
        manifestacion.setCodigoRespMani(codigoRespMani);
        manifestacion.setOtraManifestacionCli(otraManifestacionCli);

        daManifestacionesIragService.addManifestation(manifestacion);

        return  loadManifestations();
    }


    public String loadVaccines() throws AlertaException {

        try {
            final GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(DaVacVigilancia.class, new VacTypeAdapter(sessionFactory));
            gson.setPrettyPrinting();

            final Gson gson1 = gson.create();

            List<DaVacVigilancia> response = daVacVigilanciaService.getAllVaccinesByIdFicha(ficha.getIdFichaVigilancia());

            String jsonResponse = gson1. toJson(response, ArrayList.class);
            return  jsonResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public String loadConditions() throws AlertaException {

        try {
            final GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(DaCondPreVigilancia.class, new ConditionTypeAdapter(sessionFactory));
            gson.setPrettyPrinting();

            final Gson gson1 = gson.create();

            List<DaCondPreVigilancia> response = daCondPreVigilanciaService.getAllConditionsByIdFicha(ficha.getIdFichaVigilancia());

            String jsonResponse = gson1. toJson(response, ArrayList.class);
            return  jsonResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public String loadManifestations() throws AlertaException {

        try {
            final GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(DaMClinVigilancia.class, new ManifestationTypeAdapter(sessionFactory));
            gson.setPrettyPrinting();

            final Gson gson1 = gson.create();

            List<DaMClinVigilancia> response = daMClinVigilanciaService.getAllManifestationsByIdFicha(ficha.getIdFichaVigilancia());

            String jsonResponse = gson1. toJson(response, ArrayList.class);
            return  jsonResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }*/

  /*  @RequestMapping(value = "cancelVaccine/{idVacuna}" ,method = RequestMethod.GET )
    public String deleteVaccine(@PathVariable("idVacuna") Integer idVacuna) throws Exception {
        DaVacVigilancia  va = daVacVigilanciaService.getVaccineById(idVacuna);
        va.setPasivo(true);
        daVacVigilanciaService.updateVaccine(va);
        String idFicha = ficha.getIdFichaVigilancia();

       return ""
    }*/




/*
    @RequestMapping(value = "fichaInfl/searchPatient", method = RequestMethod.GET)
    public String getPatient(){
        return
                "/ficha/patients/searchPatientInflz";
    }*/



}
