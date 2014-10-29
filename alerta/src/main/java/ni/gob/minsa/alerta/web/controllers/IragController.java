package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.*;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Resource(name="personaService")
    private PersonaService personaService;
    @Resource(name="comunidadesService")
    private ComunidadesService comunidadesService;

    List<EntidadesAdtvas> entidades;
    List<Divisionpolitica>departamentos;
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
    Map<String, Object> mapModel;


    DaIrag irag = new DaIrag();
    DaVacunasIrag vacunas = new DaVacunasIrag();
    DaCondicionesPreviasIrag condicion = new DaCondicionesPreviasIrag();
    DaManifestacionesIrag manifestacion = new DaManifestacionesIrag();



    void Initialize() throws Exception {
        try {

            entidades = entidadAdmonService.getAllEntidadesAdtvas();
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

            mapModel = new HashMap<String,Object>();

            mapModel.put("entidades", entidades);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

/*
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String buscarFicha(Model model) throws Exception {
        List<DaIrag> vi = daIragService.getAllFormActivos();
        model.addAttribute("fichas", vi);
        return "search";
    }*/

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model) throws ParseException {
        logger.debug("Crear/Buscar una ficha de IRAG/ETI");
        return "irag/search";
    }

    /**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson) throws Exception {
        List<DaIrag> results =  daIragService.getDaIragPersona(idPerson);
        ModelAndView mav = new ModelAndView();

        if (results.size()==0){
            Initialize();
            SisPersona persona = personaService.getPersona(idPerson);
            if (persona!=null){
                irag.setPersona(persona);

               Divisionpolitica departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(persona.getMunicipioResidencia().getCodigoNacional());

                Divisionpolitica muniResidencia = persona.getMunicipioResidencia();

                mav.addObject("departamentoProce", departamentoProce);
                mav.addObject("muniResidencia", muniResidencia);
                mav.addObject("formVI", irag);
                mav.addObject("fVacuna", new DaVacunasIrag());
                mav.addObject("fCondPre", new DaCondicionesPreviasIrag());
                mav.addObject("fCM", new DaManifestacionesIrag());
                mav.addAllObjects(mapModel);
                mav.setViewName("irag/create");
            }
            else{
                mav.setViewName("404");
            }
        }
        else{
            mav.addObject("records", results);
            mav.setViewName("irag/results");
        }
        return mav;
    }

    /**
     * Handler for edit reports.
     *
     * @param idIrag the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idIrag}")
    public ModelAndView editIrag(@PathVariable("idIrag") String idIrag) throws Exception {
        ModelAndView mav = new ModelAndView();
        irag = daIragService.getFormById(idIrag);
        if (irag !=null){
            Initialize();
            Divisionpolitica municipio = divisionPoliticaService.getMunicipiosByUnidadSalud(irag.getCodUnidadAtencion().getMunicipio());
            List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(irag.getCodSilaisAtencion().getCodigo());
            List<Unidades> uni = unidadesService.getPUnitsHospByMuniAndSilais(municipio.getCodigoNacional(), HealthUnitType.UnidadesPrimHosp.getDiscriminator().split(","), irag.getCodSilaisAtencion().getCodigo());

            mav.addObject("formVI", irag);
            mav.addObject("munic", munic);
            mav.addObject("uni", uni);
            mav.addObject("fVacuna", new DaVacunasIrag());
            mav.addObject("fCondPre", new DaCondicionesPreviasIrag());
            mav.addObject("fCM", new DaManifestacionesIrag());
            mav.addObject("municipio", municipio);
            mav.addAllObjects(mapModel);

            mav.setViewName("irag/create");
        }
        else{
            mav.setViewName("404");
        }
        return mav;
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
            ,@RequestParam(value = "usoAntivirales", required=false) String usoAntivirales
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
            ,@RequestParam(value = "agenteEtiologico",required=false) String agenteEtiologico
            ,@RequestParam(value = "fichaCompleta",required=false) Integer fichaCompleta) throws Exception {

        logger.debug("Registrando nueva ficha de Vigilancia Integrada");

        irag.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
        irag.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
        if (!codClasificacion.isEmpty()){
            irag.setCodClasificacion(catalogoService.getClasificacion(codClasificacion));
        }

        irag.setCodExpediente(codExpediente);
        irag.setUsuario(usuarioService.getUsuarioById(3));
        if(!fechaConsulta.equals("")){
            irag.setFechaConsulta(StringToDate(fechaConsulta));
        }
        if(!fechaPrimeraConsulta.equals("")){
            irag.setFechaPrimeraConsulta(StringToDate(fechaPrimeraConsulta));
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
            irag.setFechaInicioSintomas(StringToDate (fechaInicioSintomas));
        }

        if(!codAntbUlSem.equals("")){
            irag.setCodAntbUlSem(catalogoService.getRespuesta(codAntbUlSem));
        }

        irag.setCantidadAntib(cantidadAntib);
        irag.setNombreAntibiotico(nombreAntibiotico);

        if(!fechaPrimDosisAntib.equals("")){
            irag.setFechaPrimDosisAntib(StringToDate(fechaPrimDosisAntib));
        }

        if(!fechaUltDosisAntib.equals("")){
            irag.setFechaUltDosisAntib(StringToDate(fechaUltDosisAntib));
        }

        irag.setNoDosisAntib(noDosisAntib);

        if (!codViaAntb.equals("")){
            irag.setCodViaAntb(catalogoService.getViaAntibiotico(codViaAntb));
        }

        if(!usoAntivirales.equals("")){
           irag.setUsoAntivirales(catalogoService.getRespuesta(usoAntivirales));
        }

        irag.setNombreAntiviral(nombreAntiviral);

        if(!fechaPrimDosisAntiviral.equals("")){
            irag.setFechaPrimDosisAntiviral(StringToDate (fechaPrimDosisAntiviral));
        }

        if(!fechaUltDosisAntiviral.equals("")){
            irag.setFechaUltDosisAntiviral(StringToDate (fechaUltDosisAntiviral));
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
            irag.setFechaEgreso( StringToDate(fechaEgreso));
        }

        irag.setCodCondEgreso(catalogoService.getCondicionEgreso(codCondEgreso));
        irag.setCodClasFCaso(catalogoService.getClasificacionFinal(codClasFCaso));
        irag.setAgenteBacteriano(agenteBacteriano);
        irag.setSerotipificacion(serotipificacion);
        irag.setAgenteViral(agenteViral);
        irag.setAgenteEtiologico(agenteEtiologico);
        irag.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));

        irag.setFechaRegistro(new Timestamp(new Date().getTime()));
        irag.setUsuario(usuarioService.getUsuarioById(1));


            if(!fichaCompleta.equals("")){
                if(fichaCompleta == 1){
                    irag.setFichaCompleta(true);
                }else{
                    irag.setFichaCompleta(false);
                }
            }


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


    @RequestMapping( value="newVaccine", method= RequestMethod.GET, produces = "application/json" )
    public @ResponseBody List<DaVacunasIrag> processCreationVaccine(@RequestParam(value = "codVacuna", required = true) String codVacuna,
                                         @RequestParam(value = "codTipoVacuna", required = true) String codTipoVacuna,
                                         @RequestParam(value = "dosis", required = true) Integer dosis,
                                         @RequestParam(value = "fechaUltimaDosis", required = true) String fechaUltimaDosis) throws Exception {


        if(irag.getIdIrag() != null){
            //buscar si existe registrada la vacuna y el tipo de vacuna
            DaVacunasIrag vac = daVacunasIragService.searchVaccineRecord(irag.getIdIrag(), codVacuna, codTipoVacuna);

            if(vac == null){
                vacunas.setIdIrag(daIragService.getFormById(irag.getIdIrag()));
                vacunas.setUsuario(usuarioService.getUsuarioById(1));
                vacunas.setFechaRegistro(new Timestamp(new Date().getTime()));
                vacunas.setCodVacuna(catalogoService.getVacuna(codVacuna));
                vacunas.setCodTipoVacuna(catalogoService.getTipoVacuna(codTipoVacuna));
                vacunas.setDosis(dosis);
                if(!fechaUltimaDosis.equals("")){
                    vacunas.setFechaUltimaDosis(StringToDate(fechaUltimaDosis));
                }

                daVacunasIragService.addVaccine(vacunas);


            }else{
            throw new Exception("La vacuna ya existe");
            }
        }else{
            throw new Exception("Debe guardar primeramente el formulario irag antes de agregar una vacuna.");
        }


        return loadVaccines();

    }

    @RequestMapping(value = "vaccines", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DaVacunasIrag> loadVaccines(){
        logger.info("Obteniendo las vacunas agregadas");

        List<DaVacunasIrag> listaVacunas = daVacunasIragService.getAllVaccinesByIdIrag(irag.getIdIrag());

        if (listaVacunas.isEmpty()){
            logger.debug("Null");
        }

        return listaVacunas;
    }



    @RequestMapping( value="newPreCondition", method= RequestMethod.GET)
    public @ResponseBody List<DaCondicionesPreviasIrag> processCreationPreCondition(@RequestParam(value = "codCondicion", required = true) String codCondicion,
                                         @RequestParam(value = "semanasEmbarazo", required = false) Integer semanasEmbarazo,
                                         @RequestParam(value = "otraCondicion", required = false) String otraCondicion) throws Exception {

        if(irag.getIdIrag() != null){

            //buscar si existe registro de condicion
            DaCondicionesPreviasIrag cond = daCondicionesIragService.searchConditionRecord(codCondicion, irag.getIdIrag());


            if(cond == null){
                condicion.setIdIrag(daIragService.getFormById(irag.getIdIrag()));
                condicion.setUsuario(usuarioService.getUsuarioById(1));
                condicion.setFechaRegistro(new Timestamp(new Date().getTime()));
                condicion.setCodCondicion(catalogoService.getCondicionPrevia(codCondicion));
                condicion.setSemanasEmbarazo(semanasEmbarazo);
                condicion.setOtraCondicion(otraCondicion);

                daCondicionesIragService.addCondition(condicion);
            }else{
                throw new Exception("La condicion ya existe");
            }
        }else{
            throw new Exception("Debe guardar primeramente el formulario irag antes de agregar una condicion.");

        }

        return  loadConditions();
    }

    @RequestMapping(value = "conditions", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DaCondicionesPreviasIrag> loadConditions(){
        logger.info("Obteniendo las condiciones agregadas");

        List<DaCondicionesPreviasIrag> listaCondiciones = daCondicionesIragService.getAllConditionsByIdIrag(irag.getIdIrag());

        if (listaCondiciones.isEmpty()){
            logger.debug("Null");
        }

        return listaCondiciones;
    }


    @RequestMapping( value="newCM", method= RequestMethod.GET)

    public @ResponseBody List<DaManifestacionesIrag> processCreationClinicalMan(@RequestParam(value = "codManifestacion", required = true) String codManifestacion,
                                                                                @RequestParam(value = "otraManifestacion", required = false) String otraManifestacion ) throws Exception {

        if(irag.getIdIrag() != null){

            //buscar si existe registro de condicion
            DaManifestacionesIrag manif = daManifestacionesIragService.searchManifestationRecord(codManifestacion, irag.getIdIrag());


            if(manif == null){
                manifestacion.setUsuario(usuarioService.getUsuarioById(1));
                manifestacion.setFechaRegistro(new Timestamp(new Date().getTime()));
                manifestacion.setCodManifestacion(catalogoService.getManifestacionClinica(codManifestacion));
                manifestacion.setIdIrag(daIragService.getFormById(irag.getIdIrag()));
                manifestacion.setOtraManifestacion(otraManifestacion);

                daManifestacionesIragService.addManifestation(manifestacion);
            }else{
                throw new Exception("La manifestacion clinica ya existe");
            }

        }else{
            throw new Exception("Debe guardar primeramente el formulario irag antes de agregar una manifestacion clinica.");

        }
        return  loadManifestations();
    }

    @RequestMapping(value = "manifestations", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DaManifestacionesIrag> loadManifestations(){
        logger.info("Obteniendo las Manifestaciones Clínicas agregadas");

        List<DaManifestacionesIrag> listaMani = daManifestacionesIragService.getAllManifestationsByIdIrag(irag.getIdIrag());

        if (listaMani.isEmpty()){
            logger.debug("Null");
        }

        return listaMani;
    }



  /*  @RequestMapping(value = "cancelVaccine/{idVacuna}" ,method = RequestMethod.GET )
    public String deleteVaccine(@PathVariable("idVacuna") Integer idVacuna) throws Exception {
        DaVacVigilancia  va = daVacVigilanciaService.getVaccineById(idVacuna);
        va.setPasivo(true);
        daVacVigilanciaService.updateVaccine(va);
        String idFicha = ficha.getIdFichaVigilancia();

       return ""
    }*/





    /**
     * Convierte un Date a string con formato dd/MM/yyyy
     * @param dtFecha fecha a convertir
     * @return String
     * @throws java.text.ParseException
     */
    private String DateToString(Date dtFecha) throws ParseException {
        String date;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
         date = DATE_FORMAT.format(dtFecha);
         return date;
    }

    private Date StringToDate(String strFecha) throws ParseException {
        DateFormat formatter;
        Date date;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        date = formatter.parse(strFecha);
        return date;
    }



}
