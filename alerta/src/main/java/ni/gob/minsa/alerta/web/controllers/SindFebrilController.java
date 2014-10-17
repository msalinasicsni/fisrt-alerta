package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
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
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson) {
    	List<DaSindFebril> results = sindFebrilService.getDaSindFebrilesPersona(idPerson);
        ModelAndView mav = new ModelAndView();
        if (results.size()==0){
        	SisPersona persona = personaService.getPersona(idPerson);
        	if (persona!=null){
	        	DaSindFebril daSindFeb = new DaSindFebril();
	        	daSindFeb.setPersona(persona);
	        	mav.addObject("daSindFeb", daSindFeb);
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
