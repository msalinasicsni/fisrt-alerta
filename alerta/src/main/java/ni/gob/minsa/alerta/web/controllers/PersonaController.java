package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.service.PersonaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador web de peticiones relacionadas a SisPersona
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/personas/*")
public class PersonaController {
	private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);
	@Resource(name="personaService")
	private PersonaService personaService;
	
	@RequestMapping(value = "search", method = RequestMethod.GET)
    public String initSearchForm(Model model) throws ParseException { 	
    	logger.debug("Buscar una Persona");
    	return "personas/search";
	}
	
	/**
     * Retorna una lista de personas. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON de personas 
     */
    @RequestMapping(value = "persons", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<SisPersona> fetchPersonasJson(@RequestParam(value = "strFilter", required = true) String filtro) {
        logger.info("Obteniendo las personas en JSON");
        List<SisPersona> personas = personaService.getPersonas(filtro);
        if (personas == null){
        	logger.debug("Nulo");
        }
        else{
        	for (SisPersona persona : personas) {
        		if(persona.getOcupacion()!=null) persona.getOcupacion().setRelacionGrupoOcupacion(null);
        	}
        }
        return personas;	
    }

}
