package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.persona.Ocupacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
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
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.OcupacionService;
import ni.gob.minsa.alerta.service.PersonaService;
import ni.gob.minsa.alerta.service.SindFebrilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
          
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model) throws ParseException { 	
    	logger.debug("Crear/Buscar una ficha de sindromes febriles");
    	return "sindfeb/search";
	}
	
	/**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
	 * @throws Exception 
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson) throws Exception {
    	List<DaSindFebril> results = sindFebrilService.getDaSindFebrilesPersona(idPerson);
        ModelAndView mav = new ModelAndView();
        if (results.size()==0){
        	SisPersona persona = personaService.getPersona(idPerson);
        	if (persona!=null){
	        	DaSindFebril daSindFeb = new DaSindFebril();
	        	Divisionpolitica depaResidencia = divisionPoliticaService.getDepartamentoByMunicipi(persona.getMunicipioResidencia().getCodigoNacional());
	        	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
	        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
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
	        	daSindFeb.setPersona(persona);
	        	mav.addObject("daSindFeb", daSindFeb);
	        	mav.addObject("depaResidencia", depaResidencia);
	        	mav.addObject("entidades", entidades);
	        	mav.addObject("departamentos", departamentos);
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
        return mav;
    }
    
    /**
     * Handler for edit reports.
     *
     * @param idFicha the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idFicha}")
    public ModelAndView editReport(@PathVariable("idFicha") String idFicha) {
        ModelAndView mav = new ModelAndView();
        DaSindFebril daSindFeb = sindFebrilService.getDaSindFebril(idFicha);
        if (daSindFeb!=null){
        	mav.addObject("daSindFeb", daSindFeb);
        	mav.setViewName("sindfeb/enterForm");
        }
        else{
        	mav.setViewName("404");
        }
        return mav;
    }

}
