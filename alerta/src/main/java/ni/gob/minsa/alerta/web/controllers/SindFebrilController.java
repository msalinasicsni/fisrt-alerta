package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.Ocupacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.Animales;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.EnfAgudas;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.EnfCronicas;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.FuenteAgua;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasCHIK;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDCSA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDGRA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDSSA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasHANT;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasLEPT;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.ComunidadesService;
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.OcupacionService;
import ni.gob.minsa.alerta.service.PersonaService;
import ni.gob.minsa.alerta.service.SeguridadService;
import ni.gob.minsa.alerta.service.SindFebrilService;
import ni.gob.minsa.alerta.service.UnidadesService;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlador web de peticiones relacionadas a DaSindFebril
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/febriles/*")
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
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
        	List<DaSindFebril> results = sindFebrilService.getDaSindFebrilesPersona(idPerson);
	        if (results.size()==0){
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
		        	mav.setViewName("sindfeb/enterForm");
	        	}
	        	else{
	        		mav.setViewName("404");
	        	}
	        }
	        else{
	        	mav.addObject("fichas", results);
	        	mav.setViewName("sindfeb/results");
	        }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }
    
    /**
     * Handler for edit reports.
     *
     * @param idFicha the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idNotificacion}")
    public ModelAndView editReport(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
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
                }
                List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                List<Unidades> uni = unidadesService.getPUnitsHospByMuniAndSilais(daSindFeb.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), HealthUnitType.UnidadesPrimHosp.getDiscriminator().split(","), daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
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
	public ResponseEntity<String> processCreationSindFebrilForm( @RequestParam(value="comunidad", required=true ) String comunidad
			, @RequestParam( value="numVivienda", required=true ) Integer numVivienda
			, @RequestParam( value="numFamilia", required=true ) Integer numFamilia
			, @RequestParam( value="direccion", required=true ) String direccion
			, @RequestParam( value="idFamilia", required=false, defaultValue="" ) String idFamilia
			, @RequestParam( value="idVisita", required=false, defaultValue="" ) String idVisita
			, @RequestParam( value="numFicha", required=true ) Integer numFicha
			, @RequestParam( value="personaVisita", required=true) String personaVisita
			, @RequestParam( value="personaVisitaProfesion", required=true) String personaVisitaProfesion
			, @RequestParam( value="fechaVisita", required=true) String fechaVisita) throws ParseException
	{
		
    	
    		return null;
    	
	}

}
