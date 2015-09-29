package ni.gob.minsa.alerta.web.controllers;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.AnalisisObsEsperadosService;
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
public class AnalisisObsEsperadosController {
	private static final Logger logger = LoggerFactory.getLogger(AnalisisObsEsperadosController.class);
	@Resource(name="entidadAdmonService")
	private EntidadAdmonService entidadAdmonService;
	@Resource(name="catalogosService")
	private CatalogoService catalogosService;
	@Resource(name="analisisObsEsperadosService")
	private AnalisisObsEsperadosService analisisObsEsperadosService;
	@Resource(name="sivePatologiasService")
	private SivePatologiasService sivePatologiasService;
	@Resource(name="divisionPoliticaService")
	private DivisionPoliticaService divisionPoliticaService;

	@RequestMapping(value = "casostasas", method = RequestMethod.GET)
    public String initCasosTasasPage(Model model) throws Exception { 	
		logger.debug("presentar analisis por casos y tasas");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<Anios> anios = catalogosService.getAnios();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/casostasas";
	}
	
	/**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "casostasasdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchCasosTasasDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semI,
    		@RequestParam(value = "semF", required = true) String semF,
    		@RequestParam(value = "anioI", required = true) String anioI,
    		@RequestParam(value = "anioF", required = true) String anioF,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos de casos y tasas en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataCasosTasas(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad,semI,semF,anioI,anioF);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "razones", method = RequestMethod.GET)
    public String initRazonesIndicesPage(Model model) throws Exception { 	
		logger.debug("presentar analisis de razones e indices");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<Anios> anios = catalogosService.getAnios();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/razones";
	}
    
    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "razonesdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchRazonesIndicesDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semana,
    		@RequestParam(value = "anioI", required = true) String anio,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos de razones e indices en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataRazonesIndices(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad,semana,anio);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "corredores", method = RequestMethod.GET)
    public String initCorredoresPage(Model model) throws Exception { 	
		logger.debug("presentar analisis de corredores endemicos");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Anios> anios = catalogosService.getAnios();
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/corredores";
	}
    
    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "corredoresdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchCorredoresDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semana,
    		@RequestParam(value = "anioI", required = true) String anio,
    		@RequestParam(value = "cantAnio", required = true) int cantAnio,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos corredores endemicos en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataCorredores(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad, semana, anio, cantAnio);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "indice", method = RequestMethod.GET)
    public String initIndicePage(Model model) throws Exception { 	
		logger.debug("presentar analisis de indice endemicos");
    	List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
    	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
    	List<AreaRep> areas = catalogosService.getAreaRep();
    	List<Anios> anios = catalogosService.getAnios();
    	List<Semanas> semanas = catalogosService.getSemanas();
    	List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
    	model.addAttribute("areas", areas);
    	model.addAttribute("anios", anios);
    	model.addAttribute("semanas", semanas);
    	model.addAttribute("entidades", entidades);
    	model.addAttribute("departamentos", departamentos);
    	model.addAttribute("patologias", patologias);
    	return "analisis/indice";
	}
    
    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
	 * @throws ParseException 
     */
    @RequestMapping(value = "indicedata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchIndiceDataJson(@RequestParam(value = "codPato", required = true) String codPato,
    		@RequestParam(value = "codArea", required = true) String codArea,
    		@RequestParam(value = "semI", required = true) String semana,
    		@RequestParam(value = "anioI", required = true) String anio,
    		@RequestParam(value = "cantAnio", required = true) int cantAnio,
    		@RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
    		@RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
    		@RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException {
        logger.info("Obteniendo los datos indice endemico en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataIndice(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad, semana, anio, cantAnio);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
}