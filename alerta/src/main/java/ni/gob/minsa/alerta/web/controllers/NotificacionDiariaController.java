package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SiveInformeDiario;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.domain.sive.UnidadesVwEntity;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by souyen-ics.
 */
@Controller
@RequestMapping("notificacionDiaria")
public class NotificacionDiariaController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionDiariaController.class);

    @Resource(name="seguridadService")
    private SeguridadService seguridadService;

    @Resource(name="entidadAdmonService")
    private EntidadAdmonService entidadAdmonService;

    @Resource(name="sivePatologiasService")
    private SivePatologiasService sivePatologiasService;

    @Resource(name = "notificacionDiariaService")
    private NotificacionDiariaService notifacionDiariaService;

    @Resource(name = "divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;

    @Resource(name = "unidadesService")
    private UnidadesService unidadesService;

    @Resource(name = "usuarioService")
    private UsuarioService usuarioService;

    @Autowired
    MessageSource messageSource;
    /**
     * Carga la pantalla inicial de Notificación diaria
     * @param request Con los datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public ModelAndView initForm(HttpServletRequest request) throws Exception {
        logger.debug("Cargando pagina de inicio de Notificación diaria");
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
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            //entidades admon
            List<EntidadesAdtvas> entidades = null;
            List<SivePatologias> patologias;

            patologias = sivePatologiasService.getSivePatologias();

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            }else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }


            mav.addObject("entidades", entidades);
            mav.addObject("patologias", patologias);
            mav.setViewName("notificacionDiaria/enterForm");

        }else

            mav.setViewName(urlValidacion);

        return mav;
    }

    /**
     * Carga la pantalla inicial de Notificación diaria
     * @param request Con los datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "initSearch", method = RequestMethod.GET)
    public ModelAndView initSearchForm(HttpServletRequest request) throws Exception {
        logger.debug("Cargando pagina de búsqueda de notificaciones diarias");
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
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            //entidades admon
            List<EntidadesAdtvas> entidades = null;

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            }else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            mav.addObject("entidades", entidades);
            mav.setViewName("notificacionDiaria/searchForm");

        }else

            mav.setViewName(urlValidacion);

        return mav;
    }

    /**
     * Guardar datos de una notificación diaria (agregar o actualizar)
     * @param request con los datos de la notificación diaria
     * @param response con el resultado de la acción
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    protected void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado="";
        String silais = "";
        String municipio = "";
        long unidad = 0;
        String fecha = "";


        try {
            logger.debug("Guardando datos de Informe Diario");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            SiveInformeDiario notiD = jSonToInformeD(json, request);

            SiveInformeDiario record = notifacionDiariaService.getNotiD(notiD.getSilais(), notiD.getMunicipio(), notiD.getUnidad().getCodigo(), notiD.getFechaNotificacion(), notiD.getPatologia().getCodigo());

            if(record != null){
            notifacionDiariaService.update(notiD);
            }else{
                notifacionDiariaService.save(notiD);
            }


            silais = notiD.getSilais();
            municipio = notiD.getMunicipio();
            unidad = notiD.getUnidad().getCodigo();

            if (notiD.getFechaNotificacion() != null){
                fecha = DateUtil.DateToString(notiD.getFechaNotificacion(), "dd/MM/yyyy");
            }

            } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.error.save.notiD",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("mensaje",resultado);
            map.put("silais",silais);
            map.put("municipio",municipio);
            map.put("unidad", String.valueOf(unidad));
            map.put("fecha", fecha);

            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /***
     * Convierte un JSON a objeto SiveInformeDiario
     * @param json con los datos del informe diario
     * @return NotiD
     * @throws ParseException
     */
    private SiveInformeDiario jSonToInformeD(String json, HttpServletRequest request) throws Exception {
        SiveInformeDiario notiD = new SiveInformeDiario();
        JsonObject jObjectJson = new Gson().fromJson(json, JsonObject.class);

        EntidadesAdtvas silais = null;
        Divisionpolitica municipio = null;
        UnidadesVwEntity unidadSalud = null;
        Date fecha = null;
        Integer semanaEpi = null;
        SivePatologias patologia = null;
        Integer grupo1M = 0;
        Integer grupo1F = 0;
        Integer grupo2M = 0;
        Integer grupo2F = 0;
        Integer grupo3M = 0;
        Integer grupo3F = 0;
        Integer grupo4M = 0;
        Integer grupo4F = 0;
        Integer grupo5M = 0;
        Integer grupo5F = 0;
        Integer grupo6M = 0;
        Integer grupo6F = 0;
        Integer grupo7M = 0;
        Integer grupo7F = 0;
        Integer grupo8M = 0;
        Integer grupo8F = 0;
        Integer grupo9M = 0;
        Integer grupo9F = 0;
        Integer grupo10M = 0;
        Integer grupo10F = 0;
        Integer grupo11M = 0;
        Integer grupo11F = 0;
        Integer grupo12M = 0;
        Integer grupo12F = 0;
        Integer grupo13M = 0;
        Integer grupo13F = 0;
        Integer descM = 0;
        Integer descF = 0;
        Integer totalM = null;
        Integer totalF = null;

        if (jObjectJson.get("codSilaisAtencion")!=null && !jObjectJson.get("codSilaisAtencion").getAsString().isEmpty())
            silais =  entidadAdmonService.getSilaisByCodigo(jObjectJson.get("codSilaisAtencion").getAsInt());

        if (jObjectJson.get("codMunicipio")!=null && !jObjectJson.get("codMunicipio").getAsString().isEmpty())
           municipio =   divisionPoliticaService.getDivisionPolitiacaById(jObjectJson.get("codMunicipio").getAsLong());

        if (jObjectJson.get("codUnidadAtencion")!=null && !jObjectJson.get("codUnidadAtencion").getAsString().isEmpty())
            unidadSalud =   notifacionDiariaService.getUnidadSive(jObjectJson.get("codUnidadAtencion").getAsInt());

        if (jObjectJson.get("notidate")!=null && !jObjectJson.get("notidate").getAsString().isEmpty())
            fecha = DateUtil.StringToDate(jObjectJson.get("notidate").getAsString(), "dd/MM/yyyy");

        if (jObjectJson.get("semanaEpi")!=null && !jObjectJson.get("semanaEpi").getAsString().isEmpty())
            semanaEpi = jObjectJson.get("semanaEpi").getAsInt();

        if (jObjectJson.get("codPato")!=null && !jObjectJson.get("codPato").getAsString().isEmpty())
            patologia =  sivePatologiasService.getSivePatologiaByCod(jObjectJson.get("codPato").getAsString());

        if (jObjectJson.get("masc0")!=null && !jObjectJson.get("masc0").getAsString().isEmpty())
            grupo1M = jObjectJson.get("masc0").getAsInt();

        if (jObjectJson.get("fem0")!=null && !jObjectJson.get("fem0").getAsString().isEmpty())
            grupo1F = jObjectJson.get("fem0").getAsInt();

        if (jObjectJson.get("masc1")!=null && !jObjectJson.get("masc1").getAsString().isEmpty())
            grupo2M = jObjectJson.get("masc1").getAsInt();

        if (jObjectJson.get("fem1")!=null && !jObjectJson.get("fem1").getAsString().isEmpty())
            grupo2F = jObjectJson.get("fem1").getAsInt();

        if (jObjectJson.get("masc2")!=null && !jObjectJson.get("masc2").getAsString().isEmpty())
            grupo3M = jObjectJson.get("masc2").getAsInt();

        if (jObjectJson.get("fem2")!=null && !jObjectJson.get("fem2").getAsString().isEmpty())
            grupo3F = jObjectJson.get("fem2").getAsInt();

        if (jObjectJson.get("masc3")!=null && !jObjectJson.get("masc3").getAsString().isEmpty())
            grupo4M = jObjectJson.get("masc3").getAsInt();

        if (jObjectJson.get("fem3")!=null && !jObjectJson.get("fem3").getAsString().isEmpty())
            grupo4F = jObjectJson.get("fem3").getAsInt();

        if (jObjectJson.get("masc4")!=null && !jObjectJson.get("masc4").getAsString().isEmpty())
            grupo5M = jObjectJson.get("masc4").getAsInt();

        if (jObjectJson.get("fem4")!=null && !jObjectJson.get("fem4").getAsString().isEmpty())
            grupo5F = jObjectJson.get("fem4").getAsInt();

        if (jObjectJson.get("masc5")!=null && !jObjectJson.get("masc5").getAsString().isEmpty())
            grupo6M = jObjectJson.get("masc5").getAsInt();

        if (jObjectJson.get("fem5")!=null && !jObjectJson.get("fem5").getAsString().isEmpty())
            grupo6F = jObjectJson.get("fem5").getAsInt();

        if (jObjectJson.get("masc6")!=null && !jObjectJson.get("masc6").getAsString().isEmpty())
            grupo7M = jObjectJson.get("masc6").getAsInt();

        if (jObjectJson.get("fem6")!=null && !jObjectJson.get("fem6").getAsString().isEmpty())
            grupo7F = jObjectJson.get("fem6").getAsInt();

        if (jObjectJson.get("masc7")!=null && !jObjectJson.get("masc7").getAsString().isEmpty())
            grupo8M = jObjectJson.get("masc7").getAsInt();

        if (jObjectJson.get("fem7")!=null && !jObjectJson.get("fem7").getAsString().isEmpty())
            grupo8F = jObjectJson.get("fem7").getAsInt();

        if (jObjectJson.get("masc8")!=null && !jObjectJson.get("masc8").getAsString().isEmpty())
            grupo9M = jObjectJson.get("masc8").getAsInt();

        if (jObjectJson.get("fem8")!=null && !jObjectJson.get("fem8").getAsString().isEmpty())
            grupo9F = jObjectJson.get("fem8").getAsInt();

        if (jObjectJson.get("masc9")!=null && !jObjectJson.get("masc9").getAsString().isEmpty())
            grupo10M = jObjectJson.get("masc9").getAsInt();

        if (jObjectJson.get("fem9")!=null && !jObjectJson.get("fem9").getAsString().isEmpty())
            grupo10F = jObjectJson.get("fem9").getAsInt();

        if (jObjectJson.get("masc10")!=null && !jObjectJson.get("masc10").getAsString().isEmpty())
            grupo11M = jObjectJson.get("masc10").getAsInt();

        if (jObjectJson.get("fem10")!=null && !jObjectJson.get("fem10").getAsString().isEmpty())
            grupo11F = jObjectJson.get("fem10").getAsInt();

        if (jObjectJson.get("masc11")!=null && !jObjectJson.get("masc11").getAsString().isEmpty())
            grupo12M = jObjectJson.get("masc11").getAsInt();

        if (jObjectJson.get("fem11")!=null && !jObjectJson.get("fem11").getAsString().isEmpty())
            grupo12F = jObjectJson.get("fem11").getAsInt();

        if (jObjectJson.get("masc12")!=null && !jObjectJson.get("masc12").getAsString().isEmpty())
            grupo13M = jObjectJson.get("masc12").getAsInt();

        if (jObjectJson.get("fem12")!=null && !jObjectJson.get("fem12").getAsString().isEmpty())
            grupo13F = jObjectJson.get("fem12").getAsInt();

        if (jObjectJson.get("masc13")!=null && !jObjectJson.get("masc13").getAsString().isEmpty())
            descM = jObjectJson.get("masc13").getAsInt();

        if (jObjectJson.get("fem13")!=null && !jObjectJson.get("fem13").getAsString().isEmpty())
            descF = jObjectJson.get("fem13").getAsInt();

        if (jObjectJson.get("totalMasc")!=null && !jObjectJson.get("totalMasc").getAsString().isEmpty())
            totalM = jObjectJson.get("totalMasc").getAsInt();

        if (jObjectJson.get("totalFem")!=null && !jObjectJson.get("totalFem").getAsString().isEmpty())
            totalF = jObjectJson.get("totalFem").getAsInt();

        notiD.setSilais(String.valueOf(silais.getCodigo()));
        notiD.setMunicipio(String.valueOf(municipio.getDivisionpoliticaId()));
        notiD.setUnidad(unidadSalud);
        notiD.setFechaNotificacion(fecha);
        notiD.setSemana(semanaEpi);
        notiD.setPatologia(patologia);
        notiD.setG01m(grupo1M);
        notiD.setG01f(grupo1F);
        notiD.setG02m(grupo2M);
        notiD.setG02f(grupo2F);
        notiD.setG03m(grupo3M);
        notiD.setG03f(grupo3F);
        notiD.setG04m(grupo4M);
        notiD.setG04f(grupo4F);
        notiD.setG05m(grupo5M);
        notiD.setG05f(grupo5F);
        notiD.setG06m(grupo6M);
        notiD.setG06f(grupo6F);
        notiD.setG07m(grupo7M);
        notiD.setG07f(grupo7F);
        notiD.setG08m(grupo8M);
        notiD.setG08f(grupo8F);
        notiD.setG09m(grupo9M);
        notiD.setG09f(grupo9F);
        notiD.setG10m(grupo10M);
        notiD.setG10f(grupo10F);
        notiD.setG11m(grupo11M);
        notiD.setG11f(grupo11F);
        notiD.setG12m(grupo12M);
        notiD.setG12f(grupo12F);
        notiD.setG13m(grupo13M);
        notiD.setG13f(grupo13F);
        notiD.setDescm(descM);
        notiD.setDescf(descF);
        notiD.setTotalm(totalM);
        notiD.setTotalf(totalF);
        notiD.setBloqueado(0);//cambiar
        notiD.setFecharegistro(new Timestamp(new Date().getTime()));
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        notiD.setUsuarioregistro((usuarioService.getUsuarioById((int)idUsuario).getUsername()));

        int anio = 0;
        if(fecha != null){
            Calendar fe = Calendar.getInstance();
            fe.setTime(fecha);

            anio = fe.get(Calendar.YEAR);
        }
        notiD.setAnio(anio);
        return notiD;
    }

    /**
     * Retorna una lista de eventos de notificaciones diarias segun parametros
     * @return Un arreglo JSON de dx
     */
    @RequestMapping(value = "getEventsByParams", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<SiveInformeDiario> getEventsByParams(@RequestParam(value = "silais", required = true) String silais,
                                                            @RequestParam(value = "municipio", required = true) String municipio,
                                                            @RequestParam(value = "unidad", required = true) long unidad,
                                                            @RequestParam(value = "fecha", required = true) String fecha
                                                            ) throws Exception {
        logger.info("Obteniendo los eventos de notificaciones diarias segun parámetros en JSON");
        Date fecha1 = DateUtil.StringToDate(fecha, "dd/MM/yyyy");
        return notifacionDiariaService.getEventsByParams(silais, municipio,unidad, fecha1);
    }

    /**
     * Retorna una lista de eventos de notificaciones diarias segun parametros
     * @return Un arreglo JSON de dx
     */
    @RequestMapping(value = "getEventsByParams1", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<SiveInformeDiario> getEventsByParams1(@RequestParam(value = "silais", required = true) String silais,
                                              @RequestParam(value = "municipio", required = true) String municipio,
                                              @RequestParam(value = "unidad", required = true) long unidad,
                                              @RequestParam(value = "fecha", required = true) String fecha,
                                              @RequestParam(value = "pato", required = false) String pato) throws Exception {
        logger.info("Obteniendo los eventos de notificaciones diarias segun parámetros en JSON");
        Date fecha1 = DateUtil.StringToDate(fecha, "dd/MM/yyyy");
        return notifacionDiariaService.getEventsByParams1(silais, municipio,unidad, fecha1, pato);
    }


    @RequestMapping(value = "getNotiD", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    SiveInformeDiario getNotiD(@RequestParam(value = "silais", required = true) String silais,
                                               @RequestParam(value = "municipio", required = true) String municipio,
                                               @RequestParam(value = "unidad", required = true) long unidad,
                                               @RequestParam(value = "fecha", required = true) String fecha,
                                               @RequestParam(value = "pato", required = false) String pato) throws Exception {
        logger.info("Obteniendo Notificacion Diaria");
        Date fecha1 = DateUtil.StringToDate(fecha, "yyyy-MM-dd");
        return notifacionDiariaService.getNotiD(silais, municipio,unidad, fecha1, pato);
    }

    @RequestMapping(value = "getPato", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    SivePatologias getPato(@RequestParam(value = "pato", required = true) String pato) throws Exception {
        logger.info("Obteniendo Datos Patología");
        return sivePatologiasService.getSivePatologiaByCod(pato);
    }


    /**
     * Retorna una lista de eventos de notificaciones diarias segun parametros
     * @return Un arreglo JSON de dx
     */
    @RequestMapping(value = "getEventNotiD", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Object> getEventNotiD(@RequestParam(value = "silais", required = true) String silais,
                                              @RequestParam(value = "municipio", required = true) String municipio,
                                              @RequestParam(value = "unidad", required = true) long unidad,
                                              @RequestParam(value = "fecha", required = true) String fecha
    ) throws Exception {
        logger.info("Obteniendo los eventos de notificaciones diarias segun parámetros en JSON");
        Date fecha1 = DateUtil.StringToDate(fecha, "dd/MM/yyyy");
        return notifacionDiariaService.getEventNotiD(silais, municipio,unidad, fecha1);
    }


    @RequestMapping(value = "loadEventsNoti/{parameters}", method = RequestMethod.GET)
    public ModelAndView loadEventsNoti(HttpServletRequest request, @PathVariable("parameters") String parameters) throws Exception {
        logger.debug("Cargando las notificaciones diarias segun parametros");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            //entidades admon
            List<EntidadesAdtvas> entidades = null;
            List<Divisionpolitica> munic = null;
            List<UnidadesVwEntity> unidades = null;
            List<SivePatologias> patologias;
            String silais;
            String municipio;
            String unidad;
            String fecha;
            String fechaFinal = null;


            String[] array = parameters.split(",");
            silais = array[0];
            municipio = array[1];
            unidad = array[2];
            fecha =  array[3];

            patologias = sivePatologiasService.getSivePatologias();

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            }else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            if(silais!= null){
                munic = divisionPoliticaService.getMunicipiosBySilais(Long.parseLong(silais));

                if(munic != null){
                    unidades = notifacionDiariaService.getPUnitsHospByMuniAndSilais(Long.valueOf(municipio), HealthUnitType.UnidadesSIVE.getDiscriminator().split(","), Long.parseLong(silais));

                }
            }

            if(fecha != null){
                String[] fe = fecha.split("-");
                    String dia = fe[2];
                    String mes = fe[1];
                    String anio = fe[0];
                    fechaFinal = dia + "/" + mes + "/" + anio;
            }

            mav.addObject("entidades", entidades);
            mav.addObject("patologias", patologias);
            mav.addObject("silais", silais);
            mav.addObject("munici", municipio);
            mav.addObject("unidad", unidad);
            mav.addObject("fecha", fechaFinal);
            mav.addObject("munic", munic);
            mav.addObject("unidades", unidades);
            mav.setViewName("notificacionDiaria/enterForm");

        }else

            mav.setViewName(urlValidacion);

        return mav;
    }


    /**
     * Custom handler for override Daily Notification.     *
     *
     * @return a String
     */

    @RequestMapping(value = "overrideNotiD", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    protected void overrideNotiD(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {

        String json = "";
        String resultado="";
        String silais = "";
        String municipio = "";
        long unidad = 0;
        String fecha = "";
        String pato ="";


        String usuario = seguridadService.obtenerNombreUsuario(request);

        try {
            logger.debug("Eliminando evento de Informe Diario");

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            SiveInformeDiario notiD = jSonToInformeD(json, request);

            SiveInformeDiario record = notifacionDiariaService.getNotiD(notiD.getSilais(), notiD.getMunicipio(), notiD.getUnidad().getCodigo(), notiD.getFechaNotificacion(), notiD.getPatologia().getCodigo());

            silais = notiD.getSilais();
            municipio = notiD.getMunicipio();
            unidad = notiD.getUnidad().getCodigo();

            if (notiD.getFechaNotificacion() != null){
                fecha = DateUtil.DateToString(notiD.getFechaNotificacion(), "dd/MM/yyyy");
            }

            if(record != null){
                record.setActor(usuario);
                notifacionDiariaService.delete(record);
            }


        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.error.override.notiD",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("mensaje",resultado);
            map.put("silais",silais);
            map.put("municipio",municipio);
            map.put("unidad", String.valueOf(unidad));
            map.put("fecha", fecha);


            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }


    @RequestMapping(value = "unidadesPorSilaisyMuni2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    List<UnidadesVwEntity> getUnitsByMuniAndSilais2(@RequestParam(value = "municipio", required = true) Long municipio,@RequestParam(value = "silais", required = true) long silais, HttpServletRequest request) throws Exception {
        logger.info("Obteniendo las unidades primarias y Hospitales por municipio y Silais en JSON");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
        if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
            return  notifacionDiariaService.getPUnitsHospByMuniAndSilais(municipio, HealthUnitType.UnidadesSIVE.getDiscriminator().split(","), silais);
        }else{ //sino sólo se cargarn las unidades autorizadas para el usuario según SILAIS y municipio
            return notifacionDiariaService.obtenerUnidadesPorUsuarioEntidadMunicipio((int) idUsuario, silais, municipio, ConstantsSecurity.SYSTEM_CODE, HealthUnitType.UnidadesSIVE.getDiscriminator());
        }
    }



}
