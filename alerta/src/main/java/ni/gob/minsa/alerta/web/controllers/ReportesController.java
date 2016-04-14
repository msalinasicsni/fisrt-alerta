package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.FactorPoblacion;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudEstudio;
import ni.gob.minsa.alerta.domain.muestra.FiltroMx;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.notificacion.TipoNotificacion;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by FIRSTICT on 9/21/2015.
 * V1.0
 */
@Controller
@RequestMapping("reportes")
public class ReportesController {
    private static final Logger logger = LoggerFactory.getLogger(TomaMxController.class);

    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Qualifier(value = "daNotificacionService")
    private DaNotificacionService daNotificacionService;

    @Autowired
    @Qualifier(value = "catalogosService")
    private CatalogoService catalogosService;

    @Autowired
    @Resource(name="envioMxService")
    private EnvioMxService envioMxService;

    @Autowired
    @Resource(name="resultadoFinalService")
    private ResultadoFinalService resultadoFinalService;

    @Resource(name="divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;

    @Resource(name="reportesService")
    private ReportesService reportesService;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "generalnoti", method = RequestMethod.GET)
    public ModelAndView initCreateFormTmp(HttpServletRequest request) throws Exception {
        logger.debug("Crear reporte general de notificaciones");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            mav.setViewName("reportes/generalNotificaciones");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidadesAdtvases =  seguridadService.obtenerEntidadesPorUsuario((int)idUsuario,ConstantsSecurity.SYSTEM_CODE);
            List<TipoNotificacion> tiposNotificacion = new ArrayList<TipoNotificacion>();// = catalogosService.getTipoNotificacion();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            TipoNotificacion tipoNotificacionPCT = catalogosService.getTipoNotificacion("TPNOTI|PCNT");
            tiposNotificacion.add(tipoNotificacionSF);
            tiposNotificacion.add(tipoNotificacionIRA);
            tiposNotificacion.add(tipoNotificacionPCT);
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tiposNotificacion", tiposNotificacion);
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    @RequestMapping(value = "getNotifications", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getNotifications(@RequestParam(value = "strFilter", required = true) String filtro) throws Exception{
        logger.info("Obteniendo las ordenes de examen pendienetes seg�n filtros en JSON");
        FiltroMx filtroMx = jsonToFiltroMx(filtro);
        List<DaNotificacion> notificacionList = daNotificacionService.getNoticesByFilro(filtroMx);
        return notificacionesToJson(notificacionList);
    }

    /**
     * Convierte un JSON con los filtros de b�squeda a objeto FiltroMx
     * @param strJson JSON
     * @return FiltroMx
     * @throws Exception
     */
    private FiltroMx jsonToFiltroMx(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltroMx filtroMx = new FiltroMx();
        Date fechaInicio = null;
        Date fechaFin = null;
        String codSilais = null;
        String codUnidadSalud = null;
        String tipoNotificacion = null;

        if (jObjectFiltro.get("fechaInicio") != null && !jObjectFiltro.get("fechaInicio").getAsString().isEmpty())
            fechaInicio = DateUtil.StringToDate(jObjectFiltro.get("fechaInicio").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFin") != null && !jObjectFiltro.get("fechaFin").getAsString().isEmpty())
            fechaFin = DateUtil.StringToDate(jObjectFiltro.get("fechaFin").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsString();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsString();
        if (jObjectFiltro.get("tipoNotificacion") != null && !jObjectFiltro.get("tipoNotificacion").getAsString().isEmpty())
            tipoNotificacion = jObjectFiltro.get("tipoNotificacion").getAsString();

        filtroMx.setCodSilais(codSilais);
        filtroMx.setCodUnidadSalud(codUnidadSalud);
        filtroMx.setFechaInicioNotifi(fechaInicio);
        filtroMx.setFechaFinNotifi(fechaFin);
        filtroMx.setTipoNotificacion(tipoNotificacion);

        return filtroMx;
    }

    /**
     * Convierte una lista de notificaciones a formato JSON
     * @param notificacions lista de nofiticaciones
     * @return JSON
     */
    private String notificacionesToJson(List<DaNotificacion> notificacions){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaNotificacion notificacion : notificacions){
            Map<String, String> map = new HashMap<String, String>();
            map.put("idNotificacion",notificacion.getIdNotificacion());
            if (notificacion.getFechaInicioSintomas()!=null)
                map.put("fechaInicioSintomas",DateUtil.DateToString(notificacion.getFechaInicioSintomas(), "dd/MM/yyyy"));
            else
                map.put("fechaInicioSintomas"," ");
            map.put("codtipoNoti",notificacion.getCodTipoNotificacion().getCodigo());
            map.put("tipoNoti",notificacion.getCodTipoNotificacion().getValor());
            map.put("fechaRegistro",DateUtil.DateToString(notificacion.getFechaRegistro(), "dd/MM/yyyy"));
            map.put("SILAIS",(notificacion.getCodSilaisAtencion()!=null?notificacion.getCodSilaisAtencion().getNombre():""));
            map.put("unidad",(notificacion.getCodUnidadAtencion()!=null?notificacion.getCodUnidadAtencion().getNombre():""));
            //Si hay persona
            if (notificacion.getPersona()!=null){
                /// se obtiene el nombre de la persona asociada a la ficha
                String nombreCompleto = "";
                nombreCompleto = notificacion.getPersona().getPrimerNombre();
                if (notificacion.getPersona().getSegundoNombre()!=null)
                    nombreCompleto = nombreCompleto +" "+ notificacion.getPersona().getSegundoNombre();
                nombreCompleto = nombreCompleto+" "+notificacion.getPersona().getPrimerApellido();
                if (notificacion.getPersona().getSegundoApellido()!=null)
                    nombreCompleto = nombreCompleto +" "+ notificacion.getPersona().getSegundoApellido();
                map.put("persona",nombreCompleto);
                //Se calcula la edad
                int edad = DateUtil.calcularEdadAnios(notificacion.getPersona().getFechaNacimiento());
                map.put("edad",String.valueOf(edad));
                //se obtiene el sexo
                map.put("sexo",notificacion.getPersona().getSexo().getValor());
                if(edad > 12 && notificacion.getPersona().isSexoFemenino()){
                    map.put("embarazada", envioMxService.estaEmbarazada(notificacion.getIdNotificacion()));
                }else
                    map.put("embarazada","--");
                if (notificacion.getPersona().getMunicipioResidencia()!=null){
                    map.put("municipio",notificacion.getPersona().getMunicipioResidencia().getNombre());
                }else{
                    map.put("municipio","--");
                }
            }else{
                map.put("persona"," ");
                map.put("edad"," ");
                map.put("sexo"," ");
                map.put("embarazada","--");
                map.put("municipio","");
            }

            //se valida si tiene resultado final aprobado
            boolean conResultado = false;
            List<DaSolicitudDx> solicitudDxList = resultadoFinalService.getSolicitudesDxByIdNotificacion(notificacion.getIdNotificacion());
            for (DaSolicitudDx solicitudDx : solicitudDxList) {
                conResultado = resultadoFinalService.getDetResActivosBySolicitud(solicitudDx.getIdSolicitudDx()).size() > 0;
                //cuando se encuentre el primer diagn�stico con resultado, salimos
                if (conResultado)
                    break;
            }
            //si no hay resultado para diagn�stico, validar estudios
            if (!conResultado) {
                List<DaSolicitudEstudio> solicitudEstudioList = resultadoFinalService.getSolicitudesEstByIdNotificacion(notificacion.getIdNotificacion());
                for (DaSolicitudEstudio solicitudEstudio : solicitudEstudioList) {
                    conResultado = resultadoFinalService.getDetResActivosBySolicitud(solicitudEstudio.getIdSolicitudEstudio()).size() > 0;
                    //cuando se encuentre el primer estudio con resultado, salimos
                    if (conResultado)
                        break;
                }
            }
            map.put("conResultado", (conResultado ? messageSource.getMessage("lbl.yes", null, null) : messageSource.getMessage("lbl.no", null, null)));
            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /*******************************************************************/
    /************************ REPORTE POR SEMANA ***********************/
    /*******************************************************************/
    @RequestMapping(value = "porSemana", method = RequestMethod.GET)
    public ModelAndView initCreateFormSemana(HttpServletRequest request) throws Exception {
        logger.debug("Crear reporte general de notificaciones");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            mav.setViewName("reportes/porSemana");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidadesAdtvases =  seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<TipoNotificacion> tiposNotificacion = new ArrayList<TipoNotificacion>();// = catalogosService.getTipoNotificacion();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tiposNotificacion.add(tipoNotificacionSF);
            tiposNotificacion.add(tipoNotificacionIRA);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            List<Semanas> semanas = catalogosService.getSemanas();
            List<Anios> anios = catalogosService.getAnios();
            List<FactorPoblacion> factores = catalogosService.getFactoresPoblacion();
            mav.addObject("areas", areas);
            mav.addObject("semanas", semanas);
            mav.addObject("anios", anios);
            mav.addObject("departamentos", departamentos);
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tiposNotificacion", tiposNotificacion);
            mav.addObject("factores",factores);

        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * M�todo para obtener data para Reporte de semana
     * @param factor factor poblacional
     * @param codArea PAIS, Departamente, SILAIS, Municip�o o Unidad de Salud
     * @param semI desde que semana consultar
     * @param semF hasta que semana consultar
     * @param anioI que anio consultar
     * @param codSilais que silais consultar
     * @param codDepartamento que departamento consultar
     * @param codMunicipio que municipio consultar
     * @param codUnidad que unidad de salud consultar
     * @param tipoNotificacion que tipo notificaci�n consultar
     * @param ckUS incluir sub unidades (areas de salud)
     * @return List<Object>
     * @throws ParseException
     */
    @RequestMapping(value = "getDataPorSemana", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> getDataPorSemana(
            @RequestParam(value = "factor", required = false) Integer factor,
            @RequestParam(value = "codArea", required = true) String codArea,
            @RequestParam(value = "semI", required = true) String semI,
            @RequestParam(value = "semF", required = true) String semF,
            @RequestParam(value = "anioI", required = true) String anioI,
            @RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
            @RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
            @RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
            @RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "tipoNotificacion", required = true) String tipoNotificacion,
            @RequestParam(value= "ckUS", required = false) boolean ckUS) throws ParseException {

        logger.info("Obteniendo los datos de casos de notificaciones por semana");
        FiltrosReporte filtrosReporte = new FiltrosReporte();
        filtrosReporte.setCodArea(codArea);
        filtrosReporte.setCodSilais(codSilais);
        filtrosReporte.setCodDepartamento(codDepartamento);
        filtrosReporte.setCodMunicipio(codMunicipio);
        filtrosReporte.setCodUnidad(codUnidad);
        filtrosReporte.setAnioInicial(anioI);
        filtrosReporte.setSemInicial(semI);
        filtrosReporte.setSemFinal(semF);
        filtrosReporte.setTipoNotificacion(tipoNotificacion);
        filtrosReporte.setFactor(factor);
        filtrosReporte.setTipoPoblacion("Todos");//por defecto se toma toda la poblaci�n
        filtrosReporte.setSubunidades((ckUS));//Incluir subunidades
        List<Object[]> datos = reportesService.getDataPorSemana(filtrosReporte);
        if (datos == null){
            logger.debug("Nulo");
        }
        return datos;
    }

    /*******************************************************************/
    /************************* REPORTE POR DIA *************************/
    /*******************************************************************/
    @RequestMapping(value = "porDia", method = RequestMethod.GET)
    public ModelAndView initCreateFormDia(HttpServletRequest request) throws Exception {
        logger.debug("Crear reporte notificaciones por dia");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            mav.setViewName("reportes/porDia");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidadesAdtvases =  seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<TipoNotificacion> tiposNotificacion = new ArrayList<TipoNotificacion>();// = catalogosService.getTipoNotificacion();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tiposNotificacion.add(tipoNotificacionSF);
            tiposNotificacion.add(tipoNotificacionIRA);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            mav.addObject("areas", areas);
            mav.addObject("departamentos", departamentos);
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tiposNotificacion", tiposNotificacion);

        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * M�todo para obtener data para Reporte de dia
     * @param factor factor poblacional
     * @param codArea PAIS, Departamente, SILAIS, Municip�o o Unidad de Salud
     * @param fechaInicial desde que fecha de notificaci�n consultar
     * @param fechaFinal hasta que fecha de notificaci�n consultar
     * @param codSilais que silais consultar
     * @param codDepartamento que departamento consultar
     * @param codMunicipio que municipio consultar
     * @param codUnidad que unidad de salud consultar
     * @param tipoNotificacion que tipo notificaci�n consultar
     * @param ckUS incluir sub unidades (areas de salud)
     * @return List<Object>
     * @throws ParseException
     */
    @RequestMapping(value = "getDataPorDia", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> getDataPorDia(
            @RequestParam(value = "factor", required = false) Integer factor,
            @RequestParam(value = "codArea", required = true) String codArea,
            @RequestParam(value = "fechaInicial", required = true) String fechaInicial,
            @RequestParam(value = "fechaFinal", required = true) String fechaFinal,
            @RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
            @RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
            @RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
            @RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "tipoNotificacion", required = true) String tipoNotificacion,
            @RequestParam(value= "ckUS", required = false) boolean ckUS) throws ParseException {

        logger.info("Obteniendo los datos de casos de notificaciones por semana");
        FiltrosReporte filtrosReporte = new FiltrosReporte();
        filtrosReporte.setCodArea(codArea);
        filtrosReporte.setCodSilais(codSilais);
        filtrosReporte.setCodDepartamento(codDepartamento);
        filtrosReporte.setCodMunicipio(codMunicipio);
        filtrosReporte.setCodUnidad(codUnidad);
        filtrosReporte.setTipoNotificacion(tipoNotificacion);
        filtrosReporte.setFactor(factor);
        filtrosReporte.setFechaInicio(DateUtil.StringToDate(fechaInicial+" 00:00:00","dd/MM/yyyy HH:mm:ss"));
        filtrosReporte.setFechaFin(DateUtil.StringToDate(fechaFinal+" 23:59:59","dd/MM/yyyy HH:mm:ss"));
        //filtrosReporte.setTipoPoblacion("Todos");//por defecto se toma toda la poblaci�n
        filtrosReporte.setSubunidades((ckUS));//Incluir subunidades
        List<Object[]> datos = reportesService.getDataPorDia(filtrosReporte);
        if (datos == null){
            logger.debug("Nulo");
        }
        return datos;
    }

/*******************************************************************/
    /************************ REPORTE POR AREA ***********************/
    /*******************************************************************/

    @RequestMapping(value = "area", method = RequestMethod.GET)
    public String initArea(Model model,HttpServletRequest request) throws Exception {
        logger.debug("Reporte por Area");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
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
            List<AreaRep> areas = seguridadService.getAreasUsuario((int) idUsuario, 3);
            List<Anios> anios = catalogosService.getAnios();
            List<TipoNotificacion> tipoNoti = new ArrayList<TipoNotificacion>();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tipoNoti.add(tipoNotificacionSF);
            tipoNoti.add(tipoNotificacionIRA);
            List<FactorPoblacion> factor = catalogosService.getFactoresPoblacion();
            model.addAttribute("areas", areas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("tipoNoti", tipoNoti);
            model.addAttribute("factor", factor);
            return "reportes/area";
        }else{
            return  urlValidacion;
        }
    }

    /**
     * Convierte un JSON con los filtros de b�squeda a objeto FiltrosReporte
     * @param strJson filtros
     * @return FiltrosReporte
     * @throws Exception
     */
    private FiltrosReporte jsonToFiltroReportes(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltrosReporte filtroRep = new FiltrosReporte();
        Date fechaInicio = null;
        Date fechaFin = null;
        Long codSilais = null;
        Long codUnidadSalud = null;
        String tipoNotificacion = null;
        Integer factor= 0;
        Long codDepartamento = null;
        Long codMunicipio = null;
        String codArea = null;
        boolean subunidad = false;
        boolean porSilais = true;//por defecto true

        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsLong();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsLong();
        if (jObjectFiltro.get("tipoNotificacion") != null && !jObjectFiltro.get("tipoNotificacion").getAsString().isEmpty())
            tipoNotificacion = jObjectFiltro.get("tipoNotificacion").getAsString();
        if (jObjectFiltro.get("codFactor") != null && !jObjectFiltro.get("codFactor").getAsString().isEmpty())
            factor = jObjectFiltro.get("codFactor").getAsInt();
        if (jObjectFiltro.get("fechaInicio") != null && !jObjectFiltro.get("fechaInicio").getAsString().isEmpty())
            fechaInicio = DateUtil.StringToDate(jObjectFiltro.get("fechaInicio").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFin") != null && !jObjectFiltro.get("fechaFin").getAsString().isEmpty())
            fechaFin = DateUtil.StringToDate(jObjectFiltro.get("fechaFin").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("codDepartamento") != null && !jObjectFiltro.get("codDepartamento").getAsString().isEmpty())
            codDepartamento = jObjectFiltro.get("codDepartamento").getAsLong();
        if (jObjectFiltro.get("codMunicipio") != null && !jObjectFiltro.get("codMunicipio").getAsString().isEmpty())
            codMunicipio = jObjectFiltro.get("codMunicipio").getAsLong();
        if (jObjectFiltro.get("codArea") != null && !jObjectFiltro.get("codArea").getAsString().isEmpty())
            codArea = jObjectFiltro.get("codArea").getAsString();
        if (jObjectFiltro.get("subunidades") != null && !jObjectFiltro.get("subunidades").getAsString().isEmpty())
            subunidad = jObjectFiltro.get("subunidades").getAsBoolean();
        if (jObjectFiltro.get("porSilais") != null && !jObjectFiltro.get("porSilais").getAsString().isEmpty())
            porSilais = jObjectFiltro.get("porSilais").getAsBoolean();

        filtroRep.setSubunidades(subunidad);
        filtroRep.setCodSilais(codSilais);
        filtroRep.setCodUnidad(codUnidadSalud);
        filtroRep.setFechaInicio(fechaInicio);
        filtroRep.setFechaFin(fechaFin);
        filtroRep.setTipoNotificacion(tipoNotificacion);
        filtroRep.setFactor(factor);
        filtroRep.setCodDepartamento(codDepartamento);
        filtroRep.setCodMunicipio(codMunicipio);
        filtroRep.setCodArea(codArea);
        filtroRep.setAnioInicial(DateUtil.DateToString(fechaInicio, "yyyy"));
        filtroRep.setPorSilais(porSilais);

        return filtroRep;
    }

    /**
     * M�todo para obtener data para Reporte por Area
     * @param filtro JSon con los datos de los filtros a aplicar en la b�squeda
     * @return Object
     * @throws Exception
     */
    @RequestMapping(value = "dataArea", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Object[]> fetchDataAreaJson(@RequestParam(value = "filtro", required = true) String filtro) throws Exception{
        logger.info("Obteniendo los datos para reporte por Area ");
        FiltrosReporte filtroRep = jsonToFiltroReportes(filtro);
        return reportesService.getDataCT(filtroRep);
    }

    /*******************************************************************/
    /************************* REPORTE NOTIFICACIONES SIN RESULTADO *************************/
    /*******************************************************************/
    @RequestMapping(value = "sinResultado", method = RequestMethod.GET)
    public ModelAndView initCreateFormSR(HttpServletRequest request) throws Exception {
        logger.debug("Crear reporte general de notificaciones");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            mav.setViewName("reportes/sinResultado");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidadesAdtvases =  seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            List<TipoNotificacion> tiposNotificacion = new ArrayList<TipoNotificacion>();// = catalogosService.getTipoNotificacion();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tiposNotificacion.add(tipoNotificacionSF);
            tiposNotificacion.add(tipoNotificacionIRA);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,3);
            mav.addObject("areas", areas);
            mav.addObject("departamentos", departamentos);
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tiposNotificacion", tiposNotificacion);

        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * M�todo para obtener data para reporte de notificaciones sin resultados
     * @param codArea PAIS, Departamente o SILAIS
     * @param fechaInicial a filtrar registros
     * @param fechaFinal a filtrar registros
     * @param codSilais a consultar
     * @param codDepartamento a consultar
     * @param codMunicipio a consultar
     * @param codUnidad a cosnultar
     * @param tipoNotificacion a filtrar
     * @param subunidades si deben incluirse las subunidades (area de salud)
     * @return JSON
     * @throws ParseException
     */
    @RequestMapping(value = "getDataSinResultado", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getDataSinResultado(
            @RequestParam(value = "codArea", required = true) String codArea,
            @RequestParam(value = "fechaInicial", required = true) String fechaInicial,
            @RequestParam(value = "fechaFinal", required = true) String fechaFinal,
            @RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
            @RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
            @RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
            @RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad,
            @RequestParam(value = "tipoNotificacion", required = true) String tipoNotificacion,
            @RequestParam(value = "ckUS", required = false) boolean subunidades) throws ParseException {

        logger.info("Obteniendo los datos de casos de notificaciones por semana");
        FiltrosReporte filtrosReporte = new FiltrosReporte();
        filtrosReporte.setCodArea(codArea);
        filtrosReporte.setCodSilais(codSilais);
        filtrosReporte.setCodDepartamento(codDepartamento);
        filtrosReporte.setCodMunicipio(codMunicipio);
        filtrosReporte.setCodUnidad(codUnidad);
        filtrosReporte.setTipoNotificacion(tipoNotificacion);
        filtrosReporte.setFechaInicio(DateUtil.StringToDate(fechaInicial+" 00:00:00","dd/MM/yyyy HH:mm:ss"));
        filtrosReporte.setFechaFin(DateUtil.StringToDate(fechaFinal+" 23:59:59","dd/MM/yyyy HH:mm:ss"));
        filtrosReporte.setSubunidades(subunidades);
        //filtrosReporte.setTipoPoblacion("Todos");//por defecto se toma toda la poblaci�n
        List<DaNotificacion> datos = reportesService.getDataSinResultado(filtrosReporte);
        if (datos == null){
            logger.debug("Nulo");
        }
        return notificacionesSRToJson(datos);
    }

    /**
     * Convierte una lista de nofiticaciones sin resultado en formato JSON
     * @param notificacions lista de nofiticaciones
     * @return JSON
     */
    private String notificacionesSRToJson(List<DaNotificacion> notificacions){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaNotificacion notificacion : notificacions){
            Map<String, String> map = new HashMap<String, String>();
            map.put("idNotificacion",notificacion.getIdNotificacion());
            if (notificacion.getFechaInicioSintomas()!=null)
                map.put("fechaInicioSintomas",DateUtil.DateToString(notificacion.getFechaInicioSintomas(), "dd/MM/yyyy"));
            else
                map.put("fechaInicioSintomas"," ");
            map.put("codtipoNoti",notificacion.getCodTipoNotificacion().getCodigo());
            map.put("tipoNoti",notificacion.getCodTipoNotificacion().getValor());
            map.put("fechaRegistro",DateUtil.DateToString(notificacion.getFechaRegistro(), "dd/MM/yyyy"));
            map.put("SILAIS",(notificacion.getCodSilaisAtencion()!=null?notificacion.getCodSilaisAtencion().getNombre():""));
            map.put("unidad",(notificacion.getCodUnidadAtencion()!=null?notificacion.getCodUnidadAtencion().getNombre():""));
            //Si hay persona
            if (notificacion.getPersona()!=null){
                /// se obtiene el nombre de la persona asociada a la ficha
                String nombreCompleto = "";
                nombreCompleto = notificacion.getPersona().getPrimerNombre();
                if (notificacion.getPersona().getSegundoNombre()!=null)
                    nombreCompleto = nombreCompleto +" "+ notificacion.getPersona().getSegundoNombre();
                nombreCompleto = nombreCompleto+" "+notificacion.getPersona().getPrimerApellido();
                if (notificacion.getPersona().getSegundoApellido()!=null)
                    nombreCompleto = nombreCompleto +" "+ notificacion.getPersona().getSegundoApellido();
                map.put("persona",nombreCompleto);
                //Se calcula la edad
                int edad = DateUtil.calcularEdadAnios(notificacion.getPersona().getFechaNacimiento());
                map.put("edad",String.valueOf(edad));
                //se obtiene el sexo
                map.put("sexo",notificacion.getPersona().getSexo().getValor());
                if(edad > 12 && notificacion.getPersona().isSexoFemenino()){
                    map.put("embarazada", envioMxService.estaEmbarazada(notificacion.getIdNotificacion()));
                }else
                    map.put("embarazada","--");
                if (notificacion.getPersona().getMunicipioResidencia()!=null){
                    map.put("municipio",notificacion.getPersona().getMunicipioResidencia().getNombre());
                }else{
                    map.put("municipio","--");
                }
            }else{
                map.put("persona"," ");
                map.put("edad"," ");
                map.put("sexo"," ");
                map.put("embarazada","--");
                map.put("municipio","");
            }

            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /*******************************************************************/
    /************************ REPORTE POR SEXO ***********************/
    /*******************************************************************/

    @RequestMapping(value = "sexReport", method = RequestMethod.GET)
    public String initSexReport(Model model,HttpServletRequest request) throws Exception {
        logger.debug("Reporte por Sexo");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
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
            List<AreaRep> areas = seguridadService.getAreasUsuario((int) idUsuario, 3);
            List<Anios> anios = catalogosService.getAnios();
            List<TipoNotificacion> tipoNoti = new ArrayList<TipoNotificacion>();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tipoNoti.add(tipoNotificacionSF);
            tipoNoti.add(tipoNotificacionIRA);
            List<FactorPoblacion> factor = catalogosService.getFactoresPoblacion();
            model.addAttribute("areas", areas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("tipoNoti", tipoNoti);
            model.addAttribute("factor", factor);
            return "reportes/porSexo";
        }else{
            return  urlValidacion;
        }
    }

    /**
     * M�todo para obtener data para Reporte por Sexo
     * @param filtro JSon con los datos de los filtros a aplicar en la b�squeda
     * @return Object
     * @throws Exception
     */
    @RequestMapping(value = "datasexReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Object[]> fetchDataSexJson(@RequestParam(value = "filtro", required = true) String filtro) throws Exception{
        logger.info("Obteniendo los datos para reporte por Sexo ");
        FiltrosReporte filtroRep = jsonToFiltroReportes(filtro);
        return reportesService.getDataSexReport(filtroRep);
    }

    /*******************************************************************/
    /************************ REPORTE POR RESULTADO ***********************/
    /*******************************************************************/

    @RequestMapping(value = "reportResult", method = RequestMethod.GET)
    public String initReportResult(Model model,HttpServletRequest request) throws Exception {
        logger.debug("Reporte por Resultado");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
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
            List<AreaRep> areas = seguridadService.getAreasUsuario((int) idUsuario, 3);
            List<Anios> anios = catalogosService.getAnios();
            List<TipoNotificacion> tipoNoti = new ArrayList<TipoNotificacion>();// = catalogosService.getTipoNotificacion();
            TipoNotificacion tipoNotificacionSF = catalogosService.getTipoNotificacion("TPNOTI|SINFEB");
            TipoNotificacion tipoNotificacionIRA = catalogosService.getTipoNotificacion("TPNOTI|IRAG");
            tipoNoti.add(tipoNotificacionSF);
            tipoNoti.add(tipoNotificacionIRA);
            List<FactorPoblacion> factor = catalogosService.getFactoresPoblacion();
            model.addAttribute("areas", areas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("tipoNoti", tipoNoti);
            model.addAttribute("factor", factor);
            return "reportes/porResultado";
        }else{
            return  urlValidacion;
        }
    }

    /**
     * M�todo para obtener data para Reporte por Resultado
     * @param filtro JSon con los datos de los filtros a aplicar en la b�squeda
     * @return Object
     * @throws Exception
     */
    @RequestMapping(value = "dataReportResult", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Object[]> fetchReportResultJson(@RequestParam(value = "filtro", required = true) String filtro) throws Exception{
        logger.info("Obteniendo los datos para Reporte por Resultado ");
        FiltrosReporte filtroRep = jsonToFiltroReportes(filtro);
        return reportesService.getDataResultReport(filtroRep);
    }
}
