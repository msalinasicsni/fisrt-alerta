package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.AnalisisService;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.SivePatologiasService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador web de peticiones relacionadas a analisis
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/analisis/*")
public class AnalisisController {
	private static final Logger logger = LoggerFactory.getLogger(AnalisisController.class);
	@Resource(name="entidadAdmonService")
	private EntidadAdmonService entidadAdmonService;
	@Resource(name="catalogosService")
	private CatalogoService catalogosService;
	@Resource(name="analisisService")
	private AnalisisService analisisService;
	@Resource(name="sivePatologiasService")
	private SivePatologiasService sivePatologiasService;
	
	@RequestMapping(value = "mapas", method = RequestMethod.GET)
    public String initMapsPage(Model model, HttpServletRequest request) {
		logger.debug("presentar analisis por mapas");
        return "analisis/mapas";
	}
	
	@RequestMapping(value = "agesex", method = RequestMethod.GET)
    public String initAgeSexPage(Model model) throws Exception { 	
		logger.debug("presentar analisis por edad y sexo");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<Anios> anios = catalogosService.getAnios();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("patologias", patologias);
    	return "analisis/edadsexo";
	}
	
	/**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "agesexdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchAgeSexDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semI,
    		@RequestParam(value = "semF", required = true) String semF,
    		@RequestParam(value = "anioI", required = true) String anioI,
    		@RequestParam(value = "anioF", required = true) String anioF,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codMunicipio", required = false, defaultValue = "") String codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos de edad y sexo en JSON");
        List<Object[]> datos = analisisService.getDataEdadSexo(codPato, codArea, codSilais, codMunicipio, codUnidad,semI,semF,anioI,anioF);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }

}
