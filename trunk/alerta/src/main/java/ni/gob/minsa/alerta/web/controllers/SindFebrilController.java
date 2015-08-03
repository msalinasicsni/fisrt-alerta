package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.Ocupacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.*;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.alerta.utilities.pdfUtils.GeneralUtils;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador web de peticiones relacionadas a DaSindFebril
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/febriles/")
public class SindFebrilController {
	private static final Logger logger = LoggerFactory.getLogger(SindFebrilController.class);
	@Resource(name="sindFebrilService")
	private SindFebrilService sindFebrilService;
	@Resource(name="personaService")
	private PersonaService personaService;
	@Resource(name="divisionPoliticaService")
	private DivisionPoliticaService divisionPoliticaService;
	@Resource(name="entidadAdmonService")
	private EntidadAdmonService entidadAdmonService;
	@Resource(name="catalogosService")
	public CatalogoService catalogoService;
	@Resource(name="ocupacionService")
	public OcupacionService ocupacionService;
	@Resource(name="seguridadService")
	public SeguridadService seguridadService;
	@Resource(name = "unidadesService")
    public UnidadesService unidadesService;
	@Resource(name = "comunidadesService")
    public ComunidadesService comunidadesService;
	@Resource(name = "usuarioService")
	public UsuarioService usuarioService;
    @Resource(name = "daNotificacionService")
    public DaNotificacionService daNotificacionService;
    @Autowired
    MessageSource messageSource;


	@RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
		logger.debug("Crear/Buscar una ficha de sindromes febriles");
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
        	return "sindfeb/search";
        }else{
            return urlValidacion;
        }
	}
	
	/**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
	 * @throws Exception 
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
    	String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaciÃ³n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
        	List<DaSindFebril> results = getResults(idPerson);
            long idUsuario = seguridadService.obtenerIdUsuario(request);
	        if (results.size()==0){
	        	SisPersona persona = personaService.getPersona(idPerson);
	        	if (persona!=null){
		        	DaSindFebril daSindFeb = new DaSindFebril();
		        	daSindFeb.setIdNotificacion(new DaNotificacion());
		        	daSindFeb.getIdNotificacion().setPersona(persona);
		        	List<EntidadesAdtvas> entidades = null;

	                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
	                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
	                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
	                }else {
	                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
	                }
		        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
		        	List<Divisionpolitica> municipiosResi = null;
	        		List<Comunidades> comunidades = null;
		        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
		        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
		        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
		        	}
		        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
		        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
		        	List<Respuesta> catResp =catalogoService.getRespuesta();
		        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
		        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
		        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
		        	List<Animales> animales =catalogoService.getAnimales();
		        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
		        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
		        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
		        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
		        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
		        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
		        	mav.addObject("daSindFeb", daSindFeb);
		        	mav.addObject("entidades", entidades);
		        	mav.addObject("departamentos", departamentos);
		        	mav.addObject("municipiosResi", municipiosResi);
		        	mav.addObject("comunidades", comunidades);
		        	mav.addObject("catProcedencia", catProcedencia);
		        	mav.addObject("ocupaciones", ocupaciones);
		        	mav.addObject("catResp", catResp);
		        	mav.addObject("enfCronicas", enfCronicas);
		        	mav.addObject("enfAgudas", enfAgudas);
		        	mav.addObject("fuentesAgua", fuentesAgua);
		        	mav.addObject("animales", animales);
		        	mav.addObject("sintChik", sintChik);
		        	mav.addObject("sintDcsa", sintDcsa);
		        	mav.addObject("sintDgra", sintDgra);
		        	mav.addObject("sintDssa", sintDssa);
		        	mav.addObject("sintHant", sintHant);
		        	mav.addObject("sintLept", sintLept);
                    mav.addObject("autorizado",true);
		        	mav.setViewName("sindfeb/enterForm");
	        	}
	        	else{
	        		mav.setViewName("404");
	        	}
	        }
	        else{
                List<DaSindFebril> febrilAutorizados = new ArrayList<DaSindFebril>();
                for(DaSindFebril febril: results){
                    if (idUsuario != 0) {
                        if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, febril.getIdNotificacion().getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, febril.getIdNotificacion().getCodUnidadAtencion().getCodigo())) {
                            febrilAutorizados.add(febril);
                        }
                    }
                }
	        	mav.addObject("fichas", results);
                mav.addObject("idPerson", idPerson);
                mav.addObject("fichasAutorizadas",febrilAutorizados);
	        	mav.setViewName("sindfeb/results");
	        }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    //Load results list
    @RequestMapping(value = "getResults", method = RequestMethod.GET,  produces = "application/json")
    public @ResponseBody
    List<DaSindFebril> getResults(@RequestParam(value = "idPerson", required = true) long idPerson) throws Exception {
        logger.info("Obteniendo los resultados de la búsqueda");
        List<DaSindFebril> results = null;
        results = sindFebrilService.getDaSindFebrilesPersona(idPerson);
        return results;
    }
    
    /**
     * Handler for edit reports.
     *
     * @param idNotificacion the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idNotificacion}")
    public ModelAndView editReport(@PathVariable(value = "idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
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
        boolean autorizado = false;
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
	        DaSindFebril daSindFeb = sindFebrilService.getDaSindFebril(idNotificacion);
	        if (daSindFeb!=null){
	        	List<EntidadesAdtvas> entidades = null;
	        	long idUsuario = seguridadService.obtenerIdUsuario(request);
                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                    if (entidades.size()<=0){
                        entidades.add(daSindFeb.getIdNotificacion().getCodSilaisAtencion());
                    }
                }
                if (idUsuario != 0) {
                      autorizado = seguridadService.esUsuarioNivelCentral(idUsuario,ConstantsSecurity.SYSTEM_CODE) ||
                        seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, daSindFeb.getIdNotificacion().getCodUnidadAtencion().getCodigo());

                }
                List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                List<Unidades> uni = null;
                if (seguridadService.esUsuarioNivelCentral(idUsuario,ConstantsSecurity.SYSTEM_CODE)) {
                     uni = unidadesService.getPUnitsHospByMuniAndSilais(daSindFeb.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), HealthUnitType.UnidadesPrimHosp.getDiscriminator().split(","), daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                }else{
                    uni = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int)idUsuario,daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo(),daSindFeb.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(),ConstantsSecurity.SYSTEM_CODE,HealthUnitType.UnidadesPrimHosp.getDiscriminator());
                    if (uni.size()<=0){
                        uni.add(daSindFeb.getIdNotificacion().getCodUnidadAtencion());
                    }
                }
	        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
	        	List<Divisionpolitica> municipiosResi = null;
        		List<Comunidades> comunidades = null;
	        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
	        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
	        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
	        	}
	        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
	        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
	        	List<Respuesta> catResp =catalogoService.getRespuesta();
	        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
	        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
	        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
	        	List<Animales> animales =catalogoService.getAnimales();
	        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
	        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
	        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
	        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
	        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
	        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
	        	mav.addObject("entidades", entidades);
	        	mav.addObject("munic", munic);
	        	mav.addObject("uni", uni);
	        	mav.addObject("departamentos", departamentos);
	        	mav.addObject("municipiosResi", municipiosResi);
	        	mav.addObject("comunidades", comunidades);
	        	mav.addObject("catProcedencia", catProcedencia);
	        	mav.addObject("ocupaciones", ocupaciones);
	        	mav.addObject("catResp", catResp);
	        	mav.addObject("enfCronicas", enfCronicas);
	        	mav.addObject("enfAgudas", enfAgudas);
	        	mav.addObject("fuentesAgua", fuentesAgua);
	        	mav.addObject("animales", animales);
	        	mav.addObject("sintChik", sintChik);
	        	mav.addObject("sintDcsa", sintDcsa);
	        	mav.addObject("sintDgra", sintDgra);
	        	mav.addObject("sintDssa", sintDssa);
	        	mav.addObject("sintHant", sintHant);
	        	mav.addObject("sintLept", sintLept);
	        	mav.addObject("daSindFeb", daSindFeb);
                mav.addObject("autorizado",autorizado);
                mav.setViewName("sindfeb/enterForm");
	        }
	        else{
	        	mav.setViewName("404");
	        }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }
    
    @RequestMapping( value="save", method=RequestMethod.POST)
	public ResponseEntity<String> processCreationSindFebrilForm( @RequestParam(value="codSilaisAtencion", required=true ) Integer codSilaisAtencion
			, @RequestParam( value="codUnidadAtencion", required=true ) Integer codUnidadAtencion
			, @RequestParam( value="personaId", required=true ) long personaId
			, @RequestParam( value="idNotificacion", required=false, defaultValue="" ) String idNotificacion
			, @RequestParam( value="codExpediente", required=false) String codExpediente
			, @RequestParam( value="numFicha", required=false) String numFicha
			, @RequestParam( value="nombPadre", required=false) String nombPadre
			, @RequestParam( value="codProcedencia", required=false) String codProcedencia
			, @RequestParam( value="viaje", required=false) String viaje
			, @RequestParam( value="dondeViaje", required=false) String dondeViaje
			, @RequestParam( value="embarazo", required=false) String embarazo
			, @RequestParam( value="mesesEmbarazo", required=false) int mesesEmbarazo
			, @RequestParam( value="enfCronica", required=false) String enfCronica
			, @RequestParam( value="otraCronica", required=false) String otraCronica
			, @RequestParam( value="enfAgudaAdicional", required=false) String enfAgudaAdicional
			, @RequestParam( value="otraAgudaAdicional", required=false) String otraAgudaAdicional
			, @RequestParam( value="fuenteAgua", required=false) String fuenteAgua
			, @RequestParam( value="otraFuenteAgua", required=false) String otraFuenteAgua
			, @RequestParam( value="animales", required=false) String animales
			, @RequestParam( value="otrosAnimales", required=false) String otrosAnimales
			, @RequestParam( value="fechaTomaMuestra", required=false, defaultValue="") String fechaTomaMuestra
			, @RequestParam( value="temperatura", required=false) Float temperatura
			, @RequestParam( value="pas", required=false) Integer pas
			, @RequestParam( value="pad", required=false) Integer pad
			, @RequestParam( value="ssDSA", required=true) String ssDSA
			, @RequestParam( value="ssDCA", required=true) String ssDCA
			, @RequestParam( value="ssDS", required=true) String ssDS
			, @RequestParam( value="ssLepto", required=true) String ssLepto
			, @RequestParam( value="ssHV", required=true) String ssHV
			, @RequestParam( value="ssCK", required=true) String ssCK
			, @RequestParam( value="hosp", required=false) String hosp
			, @RequestParam( value="fechaIngreso", required=false, defaultValue="") String fechaIngreso
			, @RequestParam( value="fallecido", required=false) String fallecido
			, @RequestParam( value="fechaFallecido", required=false, defaultValue="") String fechaFallecido
			, @RequestParam( value="dxPresuntivo", required=true) String dxPresuntivo
			, @RequestParam( value="dxFinal", required=true) String dxFinal
			, @RequestParam( value="nombreLlenoFicha", required=true) String nombreLlenoFicha
			, @RequestParam( value="fechaFicha", required=true) String fechaFicha
			, @RequestParam( value="fechaInicioSintomas", required=true) String fechaInicioSintomas
            , @RequestParam(value = "municipioResidencia", required = false) String municipioResidencia
            , @RequestParam(value = "comunidadResidencia", required = false) String comunidadResidencia
            , @RequestParam(value = "direccionResidencia", required = false) String direccionResidencia
            , @RequestParam(value = "ocupacion", required = false) String ocupacion
            , @RequestParam(value = "urgente", required = false) String urgente
                                                                 ,HttpServletRequest request) throws Exception
	{
    	DaSindFebril daSindFeb = new DaSindFebril();
    	DaNotificacion daNotificacion = new DaNotificacion();
    	daNotificacion.setPersona(personaService.getPersona(personaId));
        //antes actualizar a la persona
        InfoResultado infoResultado;
        try{
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT) {
                SisPersona pers = daNotificacion.getPersona();
                pers.setMunicipioResidencia(divisionPoliticaService.getDivisionPolitiacaByCodNacional(municipioResidencia));
                pers.setComunidadResidencia(comunidadesService.getComunidad(comunidadResidencia));
                pers.setDireccionResidencia(direccionResidencia);
                pers.setOcupacion(catalogoService.getOcupacion(ocupacion));
                Persona persona = personaService.ensamblarObjetoPersona(pers);

                personaService.iniciarTransaccion();
                logger.info("NombrePersona:"+persona.getPrimerNombre());
                logger.info("IdPersona:"+persona.getPersonaId());
                logger.info("ApellidoPersona:"+persona.getPrimerApellido());
                logger.info("FechaNacPersona:"+persona.getFechaNacimiento());
                logger.info("SexoPersona:"+persona.getSexoCodigo());
                logger.info("GUARDAR PERSONA");
                infoResultado = personaService.guardarPersona(persona, seguridadService.obtenerNombreUsuario(request));
                logger.info("FIN GUARDAR PERSONA");
            }else{
                infoResultado = new InfoResultado();
                infoResultado.setOk(true);
                infoResultado.setObjeto(daNotificacion);
            }
            //si se actualizó la persona se registra la notificación
            if (infoResultado.isOk() && infoResultado.getObjeto() != null ){
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                daNotificacion.setFechaRegistro(new Timestamp(new Date().getTime()));
                daNotificacion.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
                daNotificacion.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
                long idUsuario = seguridadService.obtenerIdUsuario(request);
                daNotificacion.setUrgente(catalogoService.getRespuesta(urgente));
                daNotificacion.setUsuarioRegistro(usuarioService.getUsuarioById((int)idUsuario));
                //	daNotificacion.setUsuarioRegistro(usuarioService.getUsuarioById(1));
                daNotificacion.setCodTipoNotificacion(catalogoService.getTipoNotificacion("TPNOTI|SINFEB"));
                Date dateFIS = formatter.parse(fechaInicioSintomas);
                daNotificacion.setFechaInicioSintomas(dateFIS);
                if (!idNotificacion.equals("")){
                    daNotificacion.setIdNotificacion(idNotificacion);
                }
                daSindFeb.setIdNotificacion(daNotificacion);
                Date dateFicha = formatter.parse(fechaFicha);
                daSindFeb.setFechaFicha(dateFicha);
                daSindFeb.setCodExpediente(codExpediente);
                daSindFeb.setNumFicha(numFicha);
                daSindFeb.setNombPadre(nombPadre);
                daSindFeb.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));
                daSindFeb.setViaje(catalogoService.getRespuesta(viaje));
                daSindFeb.setDondeViaje(dondeViaje);
                daSindFeb.setEmbarazo(catalogoService.getRespuesta(embarazo));
                daSindFeb.setMesesEmbarazo(mesesEmbarazo);
                daSindFeb.setEnfCronica(enfCronica);
                daSindFeb.setOtraCronica(otraCronica);
                daSindFeb.setEnfAgudaAdicional(enfAgudaAdicional);
                daSindFeb.setOtraAgudaAdicional(otraAgudaAdicional);
                daSindFeb.setFuenteAgua(fuenteAgua);
                daSindFeb.setOtraFuenteAgua(otraFuenteAgua);
                daSindFeb.setAnimales(animales);
                daSindFeb.setOtrosAnimales(otrosAnimales);


                if (!fechaTomaMuestra.equals("")){
                    Date dateFTM = formatter.parse(fechaTomaMuestra);
                    daSindFeb.setFechaTomaMuestra(dateFTM);
                }
                daSindFeb.setTemperatura(temperatura);
                daSindFeb.setPas(pas);
                daSindFeb.setPad(pad);
                daSindFeb.setSsCK(ssCK);
                daSindFeb.setSsDCA(ssDCA);
                daSindFeb.setSsDS(ssDS);
                daSindFeb.setSsDSA(ssDSA);
                daSindFeb.setSsHV(ssHV);
                daSindFeb.setSsLepto(ssLepto);
                daSindFeb.setHosp(catalogoService.getRespuesta(hosp));
                if (!fechaIngreso.equals("")){
                    Date dateIngreso = formatter.parse(fechaIngreso);
                    daSindFeb.setFechaIngreso(dateIngreso);
                }
                daSindFeb.setFallecido(catalogoService.getRespuesta(fallecido));
                if (!fechaFallecido.equals("")){
                    Date dateFallecido = formatter.parse(fechaFallecido);
                    daSindFeb.setFechaFallecido(dateFallecido);
                }
                daSindFeb.setDxPresuntivo(dxPresuntivo);
                daSindFeb.setDxFinal(dxFinal);
                daSindFeb.setNombreLlenoFicha(nombreLlenoFicha);
                sindFebrilService.saveSindFebril(daSindFeb);
            }else
                throw new Exception(infoResultado.getMensaje()+"----"+infoResultado.getMensajeDetalle());
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                personaService.commitTransaccion();
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            try {
                if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                    personaService.rollbackTransaccion();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new Exception(ex);
        }finally {
            try {
                if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                    personaService.remover();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    	return createJsonResponse(daSindFeb);
	}
    
    /**
     * Custom handler for voiding a notificacion.
     *
     * @param idNotificacion the ID of the chs to avoid
     * @return a String
     */
    @RequestMapping("/delete/{idNotificacion}")
    public String voidNoti(@PathVariable("idNotificacion") String idNotificacion, 
    		RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "redirect:/404";
        }
        if(urlValidacion.isEmpty()){
	        DaSindFebril daSindFeb = sindFebrilService.getDaSindFebril(idNotificacion);
	        if (daSindFeb!=null){
	        	daSindFeb.getIdNotificacion().setPasivo(true);
	        	sindFebrilService.saveSindFebril(daSindFeb);
	        	return "redirect:/febriles/search/"+daSindFeb.getIdNotificacion().getPersona().getPersonaId();
	        }
	        else{
	        	return "redirect:/404";
	        }
        }else{
        	return "redirect:/"+urlValidacion;
        }
    }
    
    /**
     * Custom handler for create a new report.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
	 * @throws Exception 
     */
    @RequestMapping("new/{idPerson}")
    public ModelAndView newPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
    	String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaciÃ³n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
        	SisPersona persona = personaService.getPersona(idPerson);
        	if (persona!=null){
	        	DaSindFebril daSindFeb = new DaSindFebril();
	        	daSindFeb.setIdNotificacion(new DaNotificacion());
	        	daSindFeb.getIdNotificacion().setPersona(persona);
	        	List<EntidadesAdtvas> entidades = null;
	        	long idUsuario = seguridadService.obtenerIdUsuario(request);
                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                }
	        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
	        	List<Divisionpolitica> municipiosResi = null;
        		List<Comunidades> comunidades = null;
	        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
	        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
	        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
	        	}
	        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
	        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
	        	List<Respuesta> catResp =catalogoService.getRespuesta();
	        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
	        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
	        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
	        	List<Animales> animales =catalogoService.getAnimales();
	        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
	        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
	        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
	        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
	        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
	        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
	        	mav.addObject("daSindFeb", daSindFeb);
	        	mav.addObject("entidades", entidades);
	        	mav.addObject("departamentos", departamentos);
	        	mav.addObject("municipiosResi", municipiosResi);
	        	mav.addObject("comunidades", comunidades);
	        	mav.addObject("catProcedencia", catProcedencia);
	        	mav.addObject("ocupaciones", ocupaciones);
	        	mav.addObject("catResp", catResp);
	        	mav.addObject("enfCronicas", enfCronicas);
	        	mav.addObject("enfAgudas", enfAgudas);
	        	mav.addObject("fuentesAgua", fuentesAgua);
	        	mav.addObject("animales", animales);
	        	mav.addObject("sintChik", sintChik);
	        	mav.addObject("sintDcsa", sintDcsa);
	        	mav.addObject("sintDgra", sintDgra);
	        	mav.addObject("sintDssa", sintDssa);
	        	mav.addObject("sintHant", sintHant);
	        	mav.addObject("sintLept", sintLept);
                mav.addObject("autorizado",true);
	        	mav.setViewName("sindfeb/enterForm");
        	}
        	else{
        		mav.setViewName("404");
        	}
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }
    
    private ResponseEntity<String> createJsonResponse( Object o )
	{
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    Gson gson = new Gson();
	    String json = gson.toJson( o );
	    return new ResponseEntity<String>( json, headers, HttpStatus.CREATED );
	}


    @RequestMapping(value = "getPDF", method = RequestMethod.GET)
    public
    @ResponseBody
    String getPDF(@RequestParam(value = "idNotificacion", required = true) String idNotificacion,HttpServletRequest request) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDDocument doc = new PDDocument();
        DaNotificacion not = daNotificacionService.getNotifById(idNotificacion);
        String res = null;
        if(not != null){
            if(not.getCodTipoNotificacion().getCodigo().equals("TPNOTI|SINFEB")){
                DaSindFebril febril = sindFebrilService.getDaSindFebril(idNotificacion);


                String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());


                if (febril != null) {
                    PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                    doc.addPage(page);
                    PDPageContentStream stream = new PDPageContentStream(doc, page);
                    float xCenter;

                    String workingDir = System.getProperty("user.dir");

                    BufferedImage image = ImageIO.read(new File(workingDir + "/fichaFebril.png"));

                    GeneralUtils.drawObject(stream,doc,image,20,50,545,750);
                    String silais = febril.getIdNotificacion().getCodSilaisAtencion().getNombre();

                    String nombreS = silais != null ? silais.replace("SILAIS", ""): "----";
                    String municipio = febril.getIdNotificacion().getPersona().getMunicipioResidencia() != null ? febril.getIdNotificacion().getPersona().getMunicipioResidencia().getNombre(): "----";
                    String us = febril.getIdNotificacion().getCodUnidadAtencion() != null ? febril.getIdNotificacion().getCodUnidadAtencion().getNombre(): "----";
                    String nExp = febril.getCodExpediente() != null ? febril.getCodExpediente(): "----------";
                    //laboratorio pendiente
                    String fecha = febril.getFechaFicha() != null ? DateUtil.DateToString(febril.getFechaFicha(),"yyyy/MM/dd"): null;
                    String[] array = fecha != null ?  fecha.split("/"): null;

                    String dia = array != null ? array[2]: "--";
                    String mes = array != null ? array[1]: "--";
                    String anio = array != null ? array[0]: "--";
                    String nombrePersona = null;

                    nombrePersona = febril.getIdNotificacion().getPersona().getPrimerNombre();
                    if (febril.getIdNotificacion().getPersona().getSegundoNombre() != null)
                        nombrePersona = nombrePersona + " " + febril.getIdNotificacion().getPersona().getSegundoNombre();
                    nombrePersona = nombrePersona + " " + febril.getIdNotificacion().getPersona().getPrimerApellido();
                    if (febril.getIdNotificacion().getPersona().getSegundoApellido() != null)
                        nombrePersona = nombrePersona + " " + febril.getIdNotificacion().getPersona().getSegundoApellido();

                    String edad= null;
                    if(febril.getIdNotificacion().getPersona().getFechaNacimiento() != null && febril.getFechaFicha() != null){
                        edad= DateUtil.calcularEdad(febril.getIdNotificacion().getPersona().getFechaNacimiento(), febril.getFechaFicha());
                    }

                    String[] edadDias = edad != null ? edad.split("/"): null;
                    String anios = edadDias != null ? edadDias[0]:"--";
                    String meses = edadDias != null ? edadDias[1]:"--";

                    String fNac = febril.getIdNotificacion().getPersona().getFechaNacimiento() != null ? DateUtil.DateToString(febril.getIdNotificacion().getPersona().getFechaNacimiento(), "yyyy/MM/dd"): null;
                    String[] fechaNac = fNac != null ? fNac.split("/"): null;
                    String anioNac = fechaNac != null ? fechaNac[0]: "--";
                    String mesNac = fechaNac != null ? fechaNac[1]: "--";
                    String diaNac = fechaNac != null ? fechaNac[2]:"--";

                    String sexo = febril.getIdNotificacion().getPersona().getSexo() != null ? febril.getIdNotificacion().getPersona().getSexo().getValor(): null;

                    String ocupacion = febril.getIdNotificacion().getPersona().getOcupacion() != null? febril.getIdNotificacion().getPersona().getOcupacion().getNombre(): "----------";

                    String tutor = febril.getNombPadre() != null? febril.getNombPadre(): "----------";

                    String direccion = febril.getIdNotificacion().getDireccionResidencia() != null? febril.getIdNotificacion().getDireccionResidencia(): "----------";

                    String procedencia = febril.getCodProcedencia() != null? febril.getCodProcedencia().getValor(): null;

                    String viaje = febril.getViaje() != null? febril.getViaje().getValor() : "----";

                    String donde = febril.getDondeViaje() != null? febril.getDondeViaje(): "----------";

                    String emb = febril.getEmbarazo() != null? febril.getEmbarazo().getValor(): "----";

                    String mesesEmb = febril.getMesesEmbarazo() != 0 ? String.valueOf(febril.getMesesEmbarazo()): "--";

                    String enfCronica = febril.getEnfCronica() != null ? febril.getEnfCronica():null;



                    boolean asma = false;
                    boolean alergiaR = false;
                    boolean alergiaD = false;
                    boolean diab = false;
                    boolean otra = false;
                    boolean ninguna = false;
                    if (enfCronica != null) {
                        if (enfCronica.contains("CRONICAS|ASMA")) {
                            asma = true;
                        }
                        if (enfCronica.contains("CRONICAS|ALERRESP")) {
                            alergiaR = true;
                        }
                        if (enfCronica.contains("CRONICAS|ALERDER")) {
                            alergiaD = true;
                        }
                        if (enfCronica.contains("CRONICAS|DIAB")) {
                            diab = true;
                        }
                        if (enfCronica.contains("CRONICAS|OTRA")) {
                           otra = true;
                        }

                        if (enfCronica.contains("CRONICAS|NING")) {
                            ninguna = true;
                        }

                    }

                    String eAguda = febril.getEnfAgudaAdicional() != null? febril.getEnfAgudaAdicional(): null;

                    boolean neumonia = false;
                    boolean malaria = false;
                    boolean infeccionV = false;
                    boolean otraAguda = false;

                    if(eAguda != null){
                        if (eAguda.contains("AGUDAS|NEU")) {
                            neumonia = true;
                        }
                        if (eAguda.contains("AGUDAS|MAL")) {
                            malaria = true;
                        }
                        if (eAguda.contains("AGUDAS|IVU")) {
                            infeccionV = true;
                        }
                        if (eAguda.contains("AGUDAS|OTRA")) {
                            otraAguda = true;
                        }
                    }

                    String fAgua = febril.getFuenteAgua() != null ? febril.getFuenteAgua(): null;

                    boolean aguaP = false;
                    boolean puestoP = false;
                    boolean pozo = false;
                    boolean rio = false;

                    if(fAgua != null){
                        if(fAgua.contains("AGUA|APP")){
                            aguaP = true;
                        }

                        if(fAgua.contains("AGUA|PP")){
                            puestoP = true;
                        }

                        if(fAgua.contains("AGUA|POZO")){
                            pozo = true;
                        }

                        if(fAgua.contains("AGUA|RIO")){
                            rio = true;
                        }
                    }

                    String animales = febril.getAnimales() != null ? febril.getAnimales(): null;
                    boolean perros = false;
                    boolean gatos = false;
                    boolean cerdos = false;
                    boolean ganado = false;
                    boolean ratones = false;
                    boolean ratas = false;
                    boolean otrosAnim = false;

                    if(animales != null){
                        if(animales.contains("ANIM|PERRO")){
                            perros = true;
                        }
                        if(animales.contains("ANIM|GATO")){
                            gatos = true;
                        }
                        if(animales.contains("ANIM|CERDO")){
                            cerdos = true;
                        }
                        if(animales.contains("ANIM|GANADO")){
                            ganado = true;
                        }
                        if(animales.contains("ANIM|RATON")){
                           ratones = true;
                        }
                        if(animales.contains("ANIM|RATA")){
                            ratas = true;
                        }
                        if(animales.contains("ANIM|OTRA")){
                            otrosAnim = true;
                        }

                    }

                    String fis = febril.getIdNotificacion().getFechaInicioSintomas() != null? DateUtil.DateToString(febril.getIdNotificacion().getFechaInicioSintomas(), "yyyy/MM/dd"): null;
                    String[] fechaFis = fis != null ? fis.split("/"): null;
                    String anioFis = fechaFis != null ? fechaFis[0]: "--";
                    String mesFis = fechaFis != null ? fechaFis[1]: "--";
                    String diaFis = fechaFis != null ? fechaFis[2]:"--";

                    String dsa = febril.getSsDSA() != null? febril.getSsDSA(): null;

                    boolean fiebre = false;
                    boolean cefalea = false;
                    boolean mialgias = false;
                    boolean artralgias = false;
                    boolean dolorRetro = false;
                    boolean nauseas = false;
                    boolean rash = false;
                    boolean pruebaTorn = false;

                    if(dsa != null){
                       if(dsa.contains("DSSA|FIE")){
                        fiebre = true;
                       }
                        if(dsa.contains("DSSA|CEF")){
                        cefalea = true;
                        }
                        if(dsa.contains("DSSA|MIA")){
                        mialgias = true;
                        }

                        if(dsa.contains("DSSA|DRO")){
                        dolorRetro = true;
                        }
                        if(dsa.contains("DSSA|NAU")){
                        nauseas = true;
                        }
                        if(dsa.contains("DSSA|RAS")){
                        rash = true;
                        }
                        if(dsa.contains("DSSA|PTO")){
                        pruebaTorn = true;
                        }

                        if(dsa.contains("DSSA|ART")){
                        artralgias = true;
                        }
                    }

                    String dcsa = febril.getSsDCA() != null? febril.getSsDCA(): null;
                    boolean dolorAbd = false;
                    boolean vomitos = false;
                    boolean hemorragias = false;
                    boolean letargia = false;
                    boolean hepatomegalia = false;
                    boolean acumulacion = false;
                    if(dcsa != null){
                        if(dcsa.contains("DCSA|ABD")){
                        dolorAbd = true;
                        }

                        if(dcsa.contains("DCSA|VOM")){
                        vomitos = true;
                        }

                        if(dcsa.contains("DCSA|HEM")){
                        hemorragias = true;
                        }

                        if(dcsa.contains("DCSA|LET")){
                        letargia = true;
                        }

                        if(dcsa.contains("DCSA|HEP")){
                        hepatomegalia = true;
                        }

                        if(dcsa.contains("DCSA|ACU")){
                        acumulacion = true;
                        }

                    }

                    String dengueGrave = febril.getSsDS() != null ? febril.getSsDS():null;
                    boolean pinzamiento = false;
                    boolean hipotension = false;
                    boolean shock = false;
                    boolean distres = false;
                    boolean fallaOrg = false;
                    if(dengueGrave != null){
                        if (dengueGrave.contains("DGRA|PIN")) {
                        pinzamiento = true;
                        }

                        if (dengueGrave.contains("DGRA|HIP")) {
                        hipotension = true;
                        }

                        if (dengueGrave.contains("DGRA|SHO")) {
                        shock = true;
                        }

                        if (dengueGrave.contains("DGRA|DIS")) {
                        distres = true;
                        }

                        if (dengueGrave.contains("DGRA|ORG")) {
                        fallaOrg = true;
                        }
                    }

                    String leptospirosis = febril.getSsLepto() != null ? febril.getSsLepto() : null;
                    boolean cefaleaIn = false;
                    boolean tos = false;
                    boolean respiratorio = false;
                    boolean ictericia = false;
                    boolean oliguria = false;
                    boolean escalofrio = false;
                    boolean dolorPant = false;
                    boolean hematuria = false;
                    boolean congestion = false;
                    if (leptospirosis != null) {

                        if (leptospirosis.contains("LEPT|CEF")) {
                            cefaleaIn = true;
                        }

                        if (leptospirosis.contains("LEPT|TOS")) {
                            tos = true;
                        }

                        if (leptospirosis.contains("LEPT|ICT")) {
                            ictericia = true;
                        }

                        if (leptospirosis.contains("LEPT|OLI")) {
                            oliguria = true;
                        }

                        if (leptospirosis.contains("LEPT|ESC")) {
                            escalofrio = true;
                        }

                        if (leptospirosis.contains("LEPT|DOL")) {
                            dolorPant = true;
                        }

                        if (leptospirosis.contains("LEPT|HEM")) {
                            hematuria = true;
                        }

                        if (leptospirosis.contains("LEPT|CON")) {
                            congestion = true;
                        }

                    }

                    String hantavirus = febril.getSsHV() != null ? febril.getSsHV() : null;
                    boolean difResp = false;
                    boolean hip2 = false;
                    boolean dAbdIn = false;
                    boolean dLumbar = false;
                    boolean oliguria2 = false;
                    if (hantavirus != null) {
                        if (hantavirus.contains("HANT|DIF")) {
                            difResp = false;
                        }
                        if (hantavirus.contains("HANT|HIP")) {
                            hip2 = true;
                        }
                        if (hantavirus.contains("HANT|ABD")) {
                            dAbdIn = true;
                        }
                        if (hantavirus.contains("HANT|LUM")) {
                            dLumbar = true;
                        }
                        if (hantavirus.contains("HANT|OLI")) {
                            oliguria2 = true;
                        }
                    }

                    String chik = febril.getSsCK() != null ? febril.getSsCK() : null;
                    boolean cefaleaChik = false;
                    boolean fiebreChik = false;
                    boolean artritisChik = false;
                    boolean artralgiasChik = false;
                    boolean edemaChik = false;
                    boolean maniChik = false;
                    boolean mialgiaCHik = false;
                    boolean dEspChik = false;
                    boolean meninChik = false;

                    if (chik != null) {
                        if (chik.contains("CHIK|CEF")) {
                            cefaleaChik = true;
                        }

                        if (chik.contains("CHIK|FIE")) {
                            fiebreChik = true;
                        }

                        if (chik.contains("CHIK|ART")) {
                            artritisChik = true;
                        }

                        if (chik.contains("CHIK|ARL")) {
                            artralgiasChik = true;
                        }

                        if (chik.contains("CHIK|EDE")) {
                            edemaChik = true;
                        }

                        if (chik.contains("CHIK|MAN")) {
                            maniChik = true;
                        }

                        if (chik.contains("CHIK|MIA")) {
                            mialgiaCHik = true;
                        }

                        if (chik.contains("CHIK|ESP")) {
                            dEspChik = true;
                        }

                        if (chik.contains("CHIK|MEN")) {
                            meninChik = true;
                        }
                    }

                    String hospitalizado = febril.getHosp() != null ? febril.getHosp().getValor(): "----";

                    String fechaIngreso = febril.getFechaIngreso() != null? DateUtil.DateToString(febril.getFechaIngreso(), "yyyy/MM/dd"): null;


                    String[] fechaIn = fechaIngreso != null ? fechaIngreso.split("/"): null;
                    String anioIn = fechaIn != null ? fechaIn[0]: "--";
                    String mesIn = fechaIn != null ? fechaIn[1]: "--";
                    String diaIn = fechaIn != null ? fechaIn[2]:"--";

                    String fallecido = febril.getFallecido() != null ? febril.getFallecido().getValor(): "--";

                    String fechaFallecido = febril.getFechaFallecido() != null? DateUtil.DateToString(febril.getFechaFallecido(), "yyyy/MM/dd"): null;

                    String[] fechaFa = fechaFallecido != null ? fechaFallecido.split("/"): null;
                    String anioFa = fechaFa != null ? fechaFa[0]: "--";
                    String mesFa = fechaFa != null ? fechaFa[1]: "--";
                    String diaFa = fechaFa != null ? fechaFa[2]:"--";

                    String dxPresuntivo = febril.getDxPresuntivo()!= null ? febril.getDxPresuntivo(): "----------";
                    String temp = febril.getTemperatura()!= null? febril.getTemperatura().toString(): "--";
                    String pad = febril.getPad()!= null? febril.getPad().toString():"--";
                    String pas = febril.getPas()!= null? febril.getPas().toString():"--";

                    String dxFinal = febril.getDxFinal() != null ? febril.getDxFinal() :"----------";




                    float y = 693;
                    float m = 11;
                    float m1 = 29;
                    float x = 86;
                    float x1 =86 ;
                    GeneralUtils.drawTEXT(nombreS, y, x, stream,7, PDType1Font.COURIER);
                    x1+= 122;
                    GeneralUtils.drawTEXT(municipio,y,x1,stream, 7, PDType1Font.COURIER);
                    x1+= 160;
                    GeneralUtils.drawTEXT(us,y,x1,stream, 7, PDType1Font.COURIER);
                    y-= m;
                    x1 = x + 45;
                    GeneralUtils.drawTEXT(nExp,y, x1, stream, 7, PDType1Font.COURIER);

                    x1 += 199;
                    GeneralUtils.drawTEXT(dia,y, x1, stream, 7, PDType1Font.COURIER);
                    x1+= 23;
                    GeneralUtils.drawTEXT(mes,y, x1, stream, 7, PDType1Font.COURIER);
                    x1+= 25;
                    GeneralUtils.drawTEXT(anio,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= m1;
                    x1 = x + 55;
                    GeneralUtils.drawTEXT(nombrePersona,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= 9;
                    x1 = x - 3;
                    GeneralUtils.drawTEXT(anios,y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 16;
                    GeneralUtils.drawTEXT(meses,y, x1, stream, 7, PDType1Font.COURIER);

                    x1 += 102;
                    GeneralUtils.drawTEXT(diaNac,y, x1, stream, 6, PDType1Font.COURIER);
                    x1 += 15;
                    GeneralUtils.drawTEXT(mesNac,y, x1, stream, 6, PDType1Font.COURIER);
                    x1 += 13;
                    GeneralUtils.drawTEXT(anioNac,y, x1, stream, 6, PDType1Font.COURIER);

                    if(sexo != null){
                        if(sexo.equals("Hombre")){
                            x1 += 78;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null),y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                        }else if(sexo.equals("Mujer")){
                            x1 += 58;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null),y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                        }
                    }

                    x1 = x +290;
                    GeneralUtils.drawTEXT(ocupacion,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= m;
                    x1 = x + 75;
                    GeneralUtils.drawTEXT(tutor,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= 9;
                    x1 = x+15;
                    GeneralUtils.drawTEXT(direccion,y, x1, stream, 7, PDType1Font.COURIER);

                    if (procedencia != null) {
                        y -= 9;
                        if (procedencia.equals("Urbano")) {

                            x1 = x+55;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                        } else if (procedencia.equals("Rural")) {
                            x1 = x+98;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                        }

                    }

                    x1 = x +210;
                    GeneralUtils.drawTEXT(viaje,y, x1, stream, 7, PDType1Font.COURIER);

                    x1 += 58;
                    GeneralUtils.drawTEXT(donde,y, x1, stream, 7, PDType1Font.COURIER);


                    y-= 9;
                    x1 = x+25;
                    GeneralUtils.drawTEXT(emb,y, x1, stream, 7, PDType1Font.COURIER);

                    x1 += 100;
                    GeneralUtils.drawTEXT(mesesEmb,y, x1, stream, 7, PDType1Font.COURIER);

                    if (ninguna) {
                        x1 +=  150;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.none", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(asma){
                        x1 = x+350;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    y-= 9;
                    if(alergiaR){
                        x1 = x+15;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(alergiaD){
                        x1 = x+117;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(diab){
                        x1 = x+175;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(otra){
                        x1+= 110;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(neumonia){
                        x1 = x+420;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    y-= 9;
                    if(malaria){
                        x1 = x+8;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(infeccionV){
                        x1 = x+115;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(otraAguda){
                        x1 = x+175;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    y-= 28;

                    if(aguaP){
                        x1 = x+143;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                    }else{
                        x1 = x+171;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(puestoP){
                        x1 = x+260;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(pozo){
                        x1 = x+318;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(rio){
                        x1 = x+370;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }


                    y-= 9;

                    if(perros){
                        x1 = x+130;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                    }

                    if(gatos){
                        x1 = x+172;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                    }

                    if(cerdos){
                        x1 = x+220;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                    }

                    if(ganado){
                        x1 = x+272;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(ratones){
                        x1 = x+323;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);

                    }

                    if(ratas){
                        x1 = x+365;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    if(otrosAnim){
                        x1 = x+405;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.COURIER_BOLD);
                    }

                    y-= 37;
                    x1 = x + 88;
                    GeneralUtils.drawTEXT(diaFis,y, x1, stream, 7, PDType1Font.COURIER);
                    x1 = x + 115;
                    GeneralUtils.drawTEXT(mesFis,y, x1, stream, 7, PDType1Font.COURIER);
                    x1 = x + 150;
                    GeneralUtils.drawTEXT(anioFis,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= 22;
                    x1 = x + 25;
                    GeneralUtils.drawTEXT(temp,y, x1, stream, 7, PDType1Font.COURIER);
                    x1 = x + 140;
                    GeneralUtils.drawTEXT(pas,y, x1, stream, 7, PDType1Font.COURIER);
                    x1 = x + 170;
                    GeneralUtils.drawTEXT(pad,y, x1, stream, 7, PDType1Font.COURIER);

                    y-= 48;
                    if(fiebre){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(dolorAbd){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(pinzamiento){
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    y-= 9;
                    if(cefalea){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(vomitos){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(hipotension){
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    y-= 10;
                    if(mialgias){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(hemorragias){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(shock){
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-= 10;
                    if(artralgias){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(letargia){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(distres){
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    y-= 9;
                    if(dolorRetro){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(hepatomegalia){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(fallaOrg){
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+388;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-= 9;
                    if(nauseas){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(acumulacion){
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+275;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-= 10;
                    if(rash){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    y-= 9;
                    if(pruebaTorn){
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+90;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    y-=39;

                    if(cefaleaIn){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(difResp){
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(fiebreChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    y-=10;

                    if(tos){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(hip2){
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(artritisChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-=9;

                    if(ictericia){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(dAbdIn){
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(artralgiasChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-=9;

                    if(oliguria){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(dLumbar){
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(edemaChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-=9;

                    if(escalofrio){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    if(oliguria2){
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+264;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(maniChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-=10;

                    if(dolorPant){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(mialgiaCHik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    y-=9;

                    if(hematuria){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(dEspChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }


                    y-=10;

                    if(congestion){
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+127;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    if(cefaleaChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }

                    y-=10;

                    if(meninChik){
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.yes", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }else{
                        x1 = x+410;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.abbreviation.no", null, null), y, x1, stream, 8, PDType1Font.COURIER_BOLD);
                    }



                    y-=27;
                    x1 = x+ 35;
                    GeneralUtils.drawTEXT(hospitalizado, y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 83;
                    GeneralUtils.drawTEXT(diaIn, y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 35;
                    GeneralUtils.drawTEXT(mesIn, y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 30;
                    GeneralUtils.drawTEXT(anioIn, y, x1, stream, 7, PDType1Font.COURIER);

                    x1 = x+ 240;
                    GeneralUtils.drawTEXT(fallecido, y, x1, stream, 7, PDType1Font.COURIER);

                    x1 += 88;
                    GeneralUtils.drawTEXT(diaFa, y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 21;
                    GeneralUtils.drawTEXT(mesFa, y, x1, stream, 7, PDType1Font.COURIER);
                    x1 += 20;
                    GeneralUtils.drawTEXT(anioFa, y, x1, stream, 7, PDType1Font.COURIER);

                    y-=20;
                    x1 = x+ 70;
                    GeneralUtils.drawTEXT(dxPresuntivo, y, x1, stream, 7, PDType1Font.COURIER);




                    y-=102;
                    x1 = x+ 50;
                    GeneralUtils.drawTEXT(dxFinal, y, x1, stream, 7, PDType1Font.COURIER);































                    //fecha impresión
                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.print.datetime", null, null), 100, 605, stream, 10, PDType1Font.HELVETICA_BOLD);
                    GeneralUtils.drawTEXT(fechaImpresion, 100, 900, stream, 10, PDType1Font.HELVETICA);

                    stream.close();

                    doc.save(output);
                    doc.close();
                    // generate the file
                    res = Base64.encodeBase64String(output.toByteArray());
            }
        }
        }

        return res;
    }



}
