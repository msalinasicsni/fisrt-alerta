package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.AnalisisService;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
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
	@Resource(name="divisionPoliticaService")
	private DivisionPoliticaService divisionPoliticaService;
	
	@RequestMapping(value = "series", method = RequestMethod.GET)
    public String initSeriesPage(Model model) throws Exception { 	
		logger.debug("presentar series temporales");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/series";
	}
	
	/**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "seriesdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchSeriesDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos de series temporales en JSON");
        List<Object[]> datos = analisisService.getDataSeries(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }   
    
    @RequestMapping(value = "mapas", method = RequestMethod.GET)
    public String initMapasPage(Model model) throws Exception { 	
		logger.debug("presentar mapas");
		List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = new ArrayList<AreaRep>();
        areas.add(catalogosService.getAreaRep("AREAREP|PAIS"));
        areas.add(catalogosService.getAreaRep("AREAREP|SILAIS"));
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<Anios> anios = catalogosService.getAnios();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/mapas";
	}
    
    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "mapasdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchMapasDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semI,
    		@RequestParam(value = "semF", required = true) String semF,
    		@RequestParam(value = "anioI", required = true) String anioI,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "tipoIndicador", required = false) String tipoIndicador) throws ParseException {
        logger.info("Obteniendo los datos de mapas en JSON");
        List<Object[]> datos = analisisService.getDataMapas(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad,semI,semF,anioI, tipoIndicador);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "piramides", method = RequestMethod.GET)
    public String initPiramidesPage(Model model) throws Exception { 	
		logger.debug("presentar piramides");
		List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Anios> anios = catalogosService.getAnios();
    	model.addAttribute("areas", areas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	return "analisis/piramides";
	}
    
    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "piramidesdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchPiramidesDataJson(@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "anioI", required = true) String anioI,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos de piramides en JSON");
        List<Object[]> datos = analisisService.getDataPiramides(codArea, codSilais, codDepartamento, codMunicipio, codUnidad,anioI);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }

}
