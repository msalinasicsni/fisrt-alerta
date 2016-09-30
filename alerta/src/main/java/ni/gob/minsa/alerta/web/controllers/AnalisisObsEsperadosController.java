package ni.gob.minsa.alerta.web.controllers;

import ni.gob.minsa.alerta.domain.agrupaciones.Grupo;
import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.ZonaEspecial;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

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
    @Resource(name="seguridadService")
    private SeguridadService seguridadService;
    @Resource(name="admonPatoGroupService")
    private AdmonPatoGroupService admonPatoGroupService;

	@RequestMapping(value = "casostasas", method = RequestMethod.GET)
    public String initCasosTasasPage(Model model, HttpServletRequest request) throws Exception {
        logger.debug("presentar analisis por casos y tasas");
        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            //List<AreaRep> areas = catalogosService.getAreaRep();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int) idUsuario, 3);
            List<Semanas> semanas = catalogosService.getSemanas();
            List<Anios> anios = catalogosService.getAnios();
            List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
            List<ZonaEspecial> zonas = catalogosService.getZonasEspeciales();
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("semanas", semanas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("zonas",zonas);
            model.addAttribute("grupos",grupos);
            return "analisis/casostasas";
        } else {
            return urlValidacion;
        }
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
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "ckUS", required = false) boolean subunidades,
            @RequestParam(value = "codZona", required = false) String codZona) throws ParseException {
        logger.info("Obteniendo los datos de casos y tasas en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataCasosTasas(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad,semI,semF,anioI,anioF,codZona,subunidades);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "razones", method = RequestMethod.GET)
    public String initRazonesIndicesPage(Model model, HttpServletRequest request) throws Exception {
		logger.debug("presentar analisis de razones e indices");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            //List<AreaRep> areas = catalogosService.getAreaRep();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            List<Semanas> semanas = catalogosService.getSemanas();
            List<Anios> anios = catalogosService.getAnios();
            List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
            List<ZonaEspecial> zonas = catalogosService.getZonasEspeciales();
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("semanas", semanas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("zonas",zonas);
            model.addAttribute("grupos",grupos);
            return "analisis/razones";
        }else{
            return  urlValidacion;
        }
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
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "ckUS", required = false) boolean subunidades,
            @RequestParam(value = "codZona", required = false) String codZona) throws ParseException {
        logger.info("Obteniendo los datos de razones e indices en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataRazonesIndices(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad,semana,anio,codZona,subunidades);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "corredores", method = RequestMethod.GET)
    public String initCorredoresPage(Model model, HttpServletRequest request) throws Exception {
		logger.debug("presentar analisis de corredores endemicos");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            //List<AreaRep> areas = catalogosService.getAreaRep();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            List<Anios> anios = catalogosService.getAnios();
            List<Semanas> semanas = catalogosService.getSemanas();
            List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
            List<ZonaEspecial> zonas = catalogosService.getZonasEspeciales();
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("anios", anios);
            model.addAttribute("semanas", semanas);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("zonas",zonas);
            model.addAttribute("grupos",grupos);
    	return "analisis/corredores";
	}else{
            return  urlValidacion;
        }
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
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "ckUS", required = false) boolean subunidades,
            @RequestParam(value = "codZona", required = false) String codZona) throws ParseException {
        logger.info("Obteniendo los datos corredores endemicos en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataCorredores(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad, semana, anio, cantAnio, codZona, subunidades);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }
    
    @RequestMapping(value = "indice", method = RequestMethod.GET)
    public String initIndicePage(Model model, HttpServletRequest request) throws Exception {
		logger.debug("presentar analisis de indice endemicos");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            //List<AreaRep> areas = catalogosService.getAreaRep();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            List<Anios> anios = catalogosService.getAnios();
            List<Semanas> semanas = catalogosService.getSemanas();
            List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
            List<ZonaEspecial> zonas = catalogosService.getZonasEspeciales();
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("anios", anios);
            model.addAttribute("semanas", semanas);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("zonas",zonas);
            model.addAttribute("grupos",grupos);
            return "analisis/indice";
        }else{
            return  urlValidacion;
        }
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
    		@RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "ckUS", required = false) boolean subunidades,
            @RequestParam(value = "codZona", required = false) String codZona) throws ParseException {
        logger.info("Obteniendo los datos indice endemico en JSON");
        List<Object[]> datos = analisisObsEsperadosService.getDataIndice(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad, semana, anio, cantAnio, codZona, subunidades);
        if (datos == null){
        	logger.debug("Nulo");
        }
        return datos;
    }

    @RequestMapping(value = "casostasasarea", method = RequestMethod.GET)
    public String initCasosTasasAreaPage(Model model, HttpServletRequest request) throws Exception {
        logger.debug("presentar analisis por casos y tasas");
        logger.debug("Reporte por Area");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List departamentos = divisionPoliticaService.getAllDepartamentos();
           // List areas = catalogosService.getAreaRep();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            List semanas = catalogosService.getSemanas();
            List anios = catalogosService.getAnios();
            List patologias = sivePatologiasService.getSivePatologias();
            List<ZonaEspecial> zonas = catalogosService.getZonasEspeciales();
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("semanas", semanas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("zonas",zonas);
            model.addAttribute("grupos",grupos);
            return "analisis/casostasasarea";
        }else{
            return  urlValidacion;
        }
    }

    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
     * @throws ParseException
     */
    @RequestMapping(value = "casostasasareadata", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List fetchCasosTasasAreaDataJson(@RequestParam(value = "codPato", required = true) String codPato,
                                            @RequestParam(value = "codArea", required = true) String codArea,
                                            @RequestParam(value = "semI", required = true) String semI,
                                            @RequestParam(value = "semF", required = true) String semF,
                                            @RequestParam(value = "anioI", required = true) String anioI,
                                            @RequestParam(value = "anioF", required = true) String anioF,
                                            @RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
                                            @RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
                                            @RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
                                            @RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
                                            @RequestParam(value = "ckUS", required = false) boolean conSubUnidades,
                                            @RequestParam(value = "rbNivelPais", required = false) boolean porSILAIS,
                                            @RequestParam(value = "codZona", required = false) String codZona) throws ParseException
    {
        logger.info("Obteniendo los datos de casos y tasas en JSON");
        List datos = analisisObsEsperadosService.getDataCasosTasasArea(codPato, codArea, codSilais, codDepartamento, codMunicipio, codUnidad, semI, semF, anioI, anioF, porSILAIS, conSubUnidades, codZona);
        if(datos == null)
            logger.debug("Nulo");
        return datos;
    }


}
