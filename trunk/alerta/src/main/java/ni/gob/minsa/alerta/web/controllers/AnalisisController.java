package ni.gob.minsa.alerta.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controlador web de peticiones relacionadas a analisis
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/analisis/*")
public class AnalisisController {
	private static final Logger logger = LoggerFactory.getLogger(AnalisisController.class);
	
	@RequestMapping(value = "mapas", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) {
		logger.debug("presentar analisis por mapas");
            return "analisis/mapas";
	}

}
