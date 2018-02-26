package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.alerta.utilities.typeAdapter.StringUtil;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by souyen-ics on 11-05-14.
 */
@Controller
@RequestMapping("tomaMx")
public class TomaMxController {

    private static final Logger logger = LoggerFactory.getLogger(TomaMxController.class);

    @Autowired(required = true)
    @Qualifier(value = "sessionFactory")
    public SessionFactory sessionFactory;
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Resource(name="tomaMxService")
    private TomaMxService tomaMxService;

    @Resource(name="daIragService")
    private DaIragService daIragService;
    @Resource(name="usuarioService")
    public UsuarioService usuarioService;

    @Resource(name = "catalogosService")
    public CatalogoService catalogoService;

    @Resource(name = "daNotificacionService")
    public DaNotificacionService daNotificacionService;

    @Resource(name="entidadAdmonService")
    private EntidadAdmonService entidadAdmonService;

    @Resource(name="divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;

    @Resource(name = "unidadesService")
    public UnidadesService unidadesService;

    @Resource(name = "resultadoFinalService")
    public ResultadoFinalService resultadoFinalService;

    @Autowired
    MessageSource messageSource;

    Map<String, Object> mapModel;
    List<TipoMx_TipoNotificacion> catTipoMx;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar Toma de Mx");

        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        if(urlValidacion.isEmpty()){
            return "tomaMx/search";
        }else{
            return urlValidacion;
        }

    }

    /**
     * Retorna una lista de notificaciones
     * @return Un arreglo JSON de notificaciones
     */
    @RequestMapping(value = "notices", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DaNotificacion> notices(@RequestParam(value = "strFilter", required = true) String filtro) {
        logger.info("Obteniendo las notificaciones en JSON");

        return daNotificacionService.getNoticesByPerson(filtro);
    }

    /**
     * Handler for create tomaMx.
     *
     * @param idNotificacion the ID of the notification
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("create/{idNotificacion}")
    public ModelAndView createTomaMx(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {

            //registros anteriores de toma Mx
            DaTomaMx tomaMx = new DaTomaMx();
            DaNotificacion noti = daNotificacionService.getNotifById(idNotificacion);

            //entidades admon
            List<EntidadesAdtvas> entidades = null;
            long idUsuario = seguridadService.obtenerIdUsuario(request);

            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            }else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            if (noti != null) {
                catTipoMx = tomaMxService.getTipoMxByTipoNoti(noti.getCodTipoNotificacion().getCodigo());

                mav.addObject("noti", noti);
                mav.addObject("tomaMx", tomaMx);
                mav.addObject("catTipoMx", catTipoMx);
                mav.addObject("entidades", entidades);

                mav.addAllObjects(mapModel);
                mav.setViewName("tomaMx/enterForm");
            } else {
                mav.setViewName("404");
            }
        }else{
            mav.setViewName(urlValidacion);
        }


        return mav;
    }

    /**
     * Retorna una lista de dx seg�n el tipo de muestra y de notificaci�n
     * @param codMx tipo de muestra
     * @param tipoNoti tipo de notificaci�n
     * @return Un arreglo JSON de dx
     * @throws Exception
     */
    @RequestMapping(value = "dxByMx", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Dx_TipoMx_TipoNoti> getDxBySample(@RequestParam(value = "codMx", required = true) String codMx, @RequestParam(value = "tipoNoti", required = true) String tipoNoti) throws Exception {
        logger.info("Obteniendo los diagn�sticos segun muestra y tipo de Notificacion en JSON");
        return tomaMxService.getDx(codMx, tipoNoti);
    }

    /**
     * Obtener tomas de muestra de una notificaci�n
     * @param idNotificacion de la notificaci�n a consultar
     * @return List<DaTomaMx>
     * @throws Exception
     */
    @RequestMapping(value = "tomaMxByIdNoti", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaTomaMx> getTestBySample(@RequestParam(value = "idNotificacion", required = true) String idNotificacion) throws Exception {
        logger.info("Realizando b�squeda de Toma de Mx.");

        return tomaMxService.getTomaMxByIdNoti(idNotificacion);
    }

    @RequestMapping(value = "validateTomaMx", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String validateTomaMx(@RequestParam(value = "idNotificacion", required = true) String idNotificacion,
                          @RequestParam(value = "fechaHTomaMx", required = true) String fechaToma,
                          @RequestParam(value = "dxs", required = true) String dxs) throws Exception {
        logger.info("Realizando validacion de Toma de Mx.");
        String respuesta = "OK";
        if (existeTomaMx(idNotificacion, fechaToma, dxs)){
            respuesta = messageSource.getMessage("msg.existe.toma", null, null);
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("respuesta", respuesta);
        String jsonResponse = new Gson().toJson(map);
        return jsonResponse;
    }

    private boolean existeTomaMx(String idNotificacion, String fechaToma, String dxs) throws Exception{
        int totalEncontrados = 0;
        boolean respuesta = false;
        String[] dxArray = dxs.split(",");
        Date fecha1 = DateUtil.StringToDate(fechaToma, "dd/MM/yyyy");
        List<DaTomaMx> muestras = tomaMxService.getTomaMxActivaByIdNoti(idNotificacion);
        for(DaTomaMx muestra : muestras){
            List<DaSolicitudDx> solicitudDxList = tomaMxService.getSoliDxByIdMxFechaToma(muestra.getIdTomaMx(), fecha1);
            for(String dx : dxArray) {
                for (DaSolicitudDx solicitudDx : solicitudDxList) {
                    if (solicitudDx.getCodDx().getIdDiagnostico().equals(Integer.valueOf(dx))) {
                        totalEncontrados++;
                        break;
                    }
                }
            }
            if (totalEncontrados == dxArray.length && totalEncontrados == solicitudDxList.size()) {
                respuesta = true;
                break;
            }
            totalEncontrados = 0;
        }
        return respuesta;
    }

    /**
     * Guardar toma de muestra de diagn�stico
     * @return ResponseEntity<String>
     * @throws Exception
     */
    @RequestMapping(value = "/saveToma", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveTomaMx(HttpServletRequest request,
              @RequestParam(value = "dx", required = false) String dx
            , @RequestParam(value = "fechaHTomaMx", required = false) String fechaHTomaMx
            , @RequestParam(value = "horaTomaMx", required = false) String horaTomaMx
            , @RequestParam(value = "codTipoMx", required = false) String codTipoMx
            , @RequestParam(value = "canTubos", required = false) Integer canTubos
            , @RequestParam(value = "volumen", required = false) String volumen
            , @RequestParam(value = "horaRefrigeracion", required = false) String horaRefrigeracion
            , @RequestParam(value = "mxSeparada", required = false) Integer  mxSeparada
            , @RequestParam(value = "idNotificacion", required = false) String idNotificacion
            , @RequestParam(value = "codUnidadAtencion", required = false) Integer codUnidadAtencion
            , @RequestParam(value = "codSilaisAtencion", required = false) Integer codSilaisAtencion

    )  {
        logger.debug("Guardando datos de Toma de Muestra");
        try {
            DaTomaMx tomaMx = new DaTomaMx();

            DaNotificacion notifi = daNotificacionService.getNotifById(idNotificacion);
            tomaMx.setIdNotificacion(notifi);
            if (fechaHTomaMx != null) {
                tomaMx.setFechaHTomaMx(StringToTimestamp(fechaHTomaMx));
            }

            if (horaTomaMx != null) {
                tomaMx.setHoraTomaMx(horaTomaMx);
            }

            tomaMx.setCodTipoMx(tomaMxService.getTipoMxById(codTipoMx));
            tomaMx.setCanTubos(canTubos);

            if (volumen != null && !volumen.equals("")) {
                tomaMx.setVolumen(Float.valueOf(volumen));
            }

            tomaMx.setHoraRefrigeracion(horaRefrigeracion);


            if (mxSeparada != null) {
                if (mxSeparada == 1) {
                    tomaMx.setMxSeparada(true);
                } else {
                    tomaMx.setMxSeparada(false);
                }
            }

            tomaMx.setFechaRegistro(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            tomaMx.setUsuario(usuarioService.getUsuarioById((int) idUsuario));
            tomaMx.setEstadoMx(catalogoService.getEstadoMx("ESTDMX|PEND"));

            if (codSilaisAtencion == null && codUnidadAtencion == null) {
                tomaMx.setCodSilaisAtencion(notifi.getCodSilaisAtencion());
                tomaMx.setCodUnidadAtencion(notifi.getCodUnidadAtencion());
            } else {
                tomaMx.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
                tomaMx.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
            }

            String codigo = generarCodigoUnicoMx();
            tomaMx.setCodigoUnicoMx(codigo);
            tomaMx.setCodigoLab(null);
            if (existeTomaMx(idNotificacion, fechaHTomaMx, dx)) {
                throw new Exception(messageSource.getMessage("msg.existe.toma", null, null));
            } else {
                tomaMxService.addTomaMx(tomaMx);
                saveDxRequest(tomaMx.getIdTomaMx(), dx, request);
                return createJsonResponse(tomaMx);
            }
        }catch (Exception ex){
            Map<String, String> map = new HashMap<String, String>();
            map.put("error", ex.getMessage());
            return createErrorJsonResponse(map);
        }

    }

    /**
     * Guardar solicitudes de dx para una muestra
     * @param idTomaMx a la que pertenecen las solicitudes
     * @param dx c�digod de los dx solicitados
     * @param request con los datos de autenticaci�n
     * @throws Exception
     */
    private void saveDxRequest(String idTomaMx, String dx, HttpServletRequest request) throws Exception {

        DaSolicitudDx soli = new DaSolicitudDx();
        String[] arrayDx = dx.split(",");

        for (String anArrayDx : arrayDx) {
            soli.setCodDx(tomaMxService.getDxById(anArrayDx));
            soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            soli.setUsarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            soli.setIdTomaMx(tomaMxService.getTomaMxById(idTomaMx));
            soli.setControlCalidad(false);
            soli.setAprobada(false);
            soli.setInicial(true);
            tomaMxService.addSolicitudDx(soli);
        }

    }

    private Timestamp StringToTimestamp(String fechah) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = sdf.parse(fechah);
        return new Timestamp(date.getTime());
    }

    private ResponseEntity<String> createJsonResponse(Object o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    private ResponseEntity<String> createErrorJsonResponse(Object o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<>(json, headers, HttpStatus.CONFLICT);
    }

    /**
     * M�todo para generar un string alfanum�rico de 8 caracteres, que se usar� como c�digo �nico de muestra
     * @return String codigoUnicoMx
     */
    private String generarCodigoUnicoMx(){
        DaTomaMx validaC;
        //Se genera el c�digo
        String codigoUnicoMx = StringUtil.getCadenaAlfanumAleatoria(8);
        //Se consulta BD para ver si existe toma de Mx que tenga mismo c�digo
        validaC = tomaMxService.getTomaMxByCodUnicoMx(codigoUnicoMx);
        //si existe, de manera recursiva se solicita un nuevo c�digo
        if (validaC!=null){
            codigoUnicoMx = generarCodigoUnicoMx();
        }
        //si no existe se retorna el �ltimo c�digo generado
        return codigoUnicoMx;
    }

    /***************************************************************************/
    /******************** TOMA MUESTRAS ESTUDIOS********************************/
    /***************************************************************************/


    @RequestMapping(value = "searchStudy", method = RequestMethod.GET)
    public ModelAndView initSearchFormEstudio(Model model, HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar Toma de Mx");

        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
            if (!seguridadService.esUsuarioAutorizadoTomaMxEstudio((int)seguridadService.obtenerIdUsuario(request), ConstantsSecurity.SYSTEM_CODE)){
                urlValidacion = "403";
            }
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView view = new ModelAndView();
        if(urlValidacion.isEmpty()){
            view.setViewName("tomaMx/search");
            view.addObject("esEstudio",true);
        }else{
            view.setViewName(urlValidacion);
        }
        return view;
    }

    /**
     * Handler for create tomaMx.
     *
     * @param idNotificacion the ID of the notification
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("createStudy/{idNotificacion}")
    public ModelAndView createTomaMxStudy(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
            if (!seguridadService.esUsuarioAutorizadoTomaMxEstudio((int)seguridadService.obtenerIdUsuario(request), ConstantsSecurity.SYSTEM_CODE)){
                urlValidacion = "403";
            }
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }

        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {

            //registros anteriores de toma Mx
            DaTomaMx tomaMx = new DaTomaMx();
            DaNotificacion noti = daNotificacionService.getNotifById(idNotificacion);

            if (noti != null) {
                catTipoMx = tomaMxService.getTipoMxByTipoNoti(noti.getCodTipoNotificacion().getCodigo());
                List<CategoriaMx> categoriasMx = catalogoService.getCategoriasMx();
                mav.addObject("noti", noti);
                mav.addObject("tomaMx", tomaMx);
                mav.addObject("catTipoMx", catTipoMx);
                mav.addObject("categMxList",categoriasMx);
                mav.addAllObjects(mapModel);
                mav.setViewName("tomaMx/enterFormStudy");
            } else {
                mav.setViewName("404");
            }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * Retorna una lista de estudios por tipo de muestra y notificaci�n
     * @return Un arreglo JSON de dx
     */
    @RequestMapping(value = "getStudiesBySampleAndNoti", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Estudio_TipoMx_TipoNoti> getEstudioBySampleAndNoti(@RequestParam(value = "codMx", required = true) String codMx,
                                                            @RequestParam(value = "tipoNoti", required = true) String tipoNoti,
                                                            @RequestParam(value = "idUnidadSalud", required = true) long idUnidadSalud) throws Exception {
        logger.info("Obteniendo los diagn�sticos segun muestra y tipo de Notificacion en JSON");
        return tomaMxService.getEstudiosByTipoMxTipoNoti(codMx, tipoNoti, idUnidadSalud);
    }

    /**
     * Guardar tomas de muestra de estudio
     * @param request con los datos de autenticaci�n y datos de la muestra a guardar
     * @param response con el resultado de la operaci�n
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "saveTomaMxStudy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    protected void saveTomaMxStudy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String fechaHTomaMx="";
        String idNotificacion="";
        String codTipoMx="";
        String codigoUnicoMx="";
        Integer canTubos=null;
        String volumen=null;
        String horaRefrigeracion="";
        Integer mxSeparada=null;
        String estudios="";
        String categoriaMx="";
        String horaTomaMx="";
        try {
            logger.debug("Guardando datos de Toma de Muestra");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            idNotificacion = jsonpObject.get("idNotificacion").getAsString();
            fechaHTomaMx = jsonpObject.get("fechaHTomaMx").getAsString();
            codTipoMx = jsonpObject.get("codTipoMx").getAsString();
            codigoUnicoMx = jsonpObject.get("codigoUnicoMx").getAsString();

            if (jsonpObject.get("canTubos")!=null && !jsonpObject.get("canTubos").getAsString().isEmpty())
                canTubos = jsonpObject.get("canTubos").getAsInt();

            if (jsonpObject.get("volumen")!=null && !jsonpObject.get("volumen").getAsString().isEmpty())
                volumen = jsonpObject.get("volumen").getAsString();
            if (jsonpObject.get("mxSeparada")!=null && !jsonpObject.get("mxSeparada").getAsString().isEmpty())
                mxSeparada = jsonpObject.get("mxSeparada").getAsInt();
            if (jsonpObject.get("categoriaMx")!=null && !jsonpObject.get("categoriaMx").getAsString().isEmpty())
                categoriaMx = jsonpObject.get("categoriaMx").getAsString();
            if (jsonpObject.get("horaTomaMx")!=null && !jsonpObject.get("horaTomaMx").getAsString().isEmpty())
                horaTomaMx = jsonpObject.get("horaTomaMx").getAsString();

            horaRefrigeracion = jsonpObject.get("horaRefrigeracion").getAsString();

            String idEstudio = jsonpObject.get("estudio").getAsString();

            DaTomaMx tomaMx = new DaTomaMx();
            DaNotificacion notificacion = daNotificacionService.getNotifById(idNotificacion);
            tomaMx.setIdNotificacion(notificacion);
            tomaMx.setCodSilaisAtencion(notificacion.getCodSilaisAtencion());
            tomaMx.setCodUnidadAtencion(notificacion.getCodUnidadAtencion());
            if(fechaHTomaMx != null){
                tomaMx.setFechaHTomaMx(StringToTimestamp(fechaHTomaMx));
            }
            if (horaTomaMx != null){
                tomaMx.setHoraTomaMx(horaTomaMx);
            }

            tomaMx.setCodTipoMx(tomaMxService.getTipoMxById(codTipoMx));
            tomaMx.setCanTubos(canTubos);

            if(volumen != null){
                tomaMx.setVolumen(Float.valueOf(volumen));
            }

            tomaMx.setHoraRefrigeracion(horaRefrigeracion);

            if (mxSeparada!=null) {
                if (mxSeparada == 1) {
                    tomaMx.setMxSeparada(true);
                } else {
                    tomaMx.setMxSeparada(false);
                }
            }

            tomaMx.setFechaRegistro(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            Usuarios usuarioRegistro = usuarioService.getUsuarioById((int)idUsuario);
            tomaMx.setUsuario(usuarioRegistro);
            tomaMx.setEstadoMx(catalogoService.getEstadoMx("ESTDMX|PEND"));
            tomaMx.setCodigoUnicoMx(codigoUnicoMx);
            tomaMx.setCodigoLab(null);
            tomaMx.setCategoriaMx(catalogoService.getCategoriaMx(categoriaMx));
            tomaMxService.addTomaMx(tomaMx);
            DaSolicitudEstudio soli = new DaSolicitudEstudio();
            soli.setTipoEstudio(tomaMxService.getEstudioById(Integer.valueOf(idEstudio)));
            soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
            soli.setUsarioRegistro(usuarioRegistro);
            soli.setIdTomaMx(tomaMx);
            soli.setAprobada(false);
            tomaMxService.addSolicitudEstudio(soli);

            //saveSolicitudesEstudio(tomaMx.getIdTomaMx(), jsonArray, request);
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("lbl.messagebox.error.saving",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idNotificacion",idNotificacion);
            map.put("canTubos",String.valueOf(canTubos));
            map.put("volumen",volumen);
            map.put("mensaje",resultado);
            map.put("fechaHTomaMx", fechaHTomaMx);
            map.put("estudios",estudios);
            map.put("mxSeparada",String.valueOf(mxSeparada));
            map.put("codTipoMx",codTipoMx);
            map.put("horaRefrigeracion", horaRefrigeracion);
            map.put("horaTomaMx",horaTomaMx);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    private void saveSolicitudesEstudio(String idTomaMx, JsonArray estudios, HttpServletRequest request) throws Exception {

        DaSolicitudEstudio soli = new DaSolicitudEstudio();
        for (JsonElement idEstudio : estudios) {
            soli.setTipoEstudio(tomaMxService.getEstudioById(idEstudio.getAsInt()));
            soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            soli.setUsarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            soli.setIdTomaMx(tomaMxService.getTomaMxById(idTomaMx));
            tomaMxService.addSolicitudEstudio(soli);
        }
    }

    /**
     * M?todo que se llama al entrar a la opci?n de menu de Reportes "Reporte Resultados Positivos y Negativos".
     *
     * @param request para obtener informaci?n de la petici?n del cliente
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/searchMx/init", method = RequestMethod.GET)
    public ModelAndView initReportForm(HttpServletRequest request) throws Exception {
        logger.debug("Iniciando formulario de b�squeda de muetras");
        String urlValidacion;
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci?n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            List<EntidadesAdtvas> entidadesAdtvases = entidadAdmonService.getAllEntidadesAdtvas();
            mav.addObject("entidades", entidadesAdtvases);
            mav.setViewName("tomaMx/searchMxLab");
        } else
            mav.setViewName(urlValidacion);

        return mav;
    }

    /**
     * M?todo para realizar la b?squeda de Resultados positivos o negativos
     *
     * @param filtro JSon con los datos de los filtros a aplicar en la b?squeda(Rango Fec toma mx, SILAIS, unidad salud, nombre solicitud)
     * @return String con las solicitudes encontradas
     * @throws Exception
     */
    @RequestMapping(value = "getMxs", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String fetchPosNegRequestJson(@RequestParam(value = "strFilter", required = true) String filtro, HttpServletRequest request) throws Exception {
        logger.info("Obteniendo las muestras seg?n filtros en JSON");
        FiltroMx filtroMx = jsonToFiltroMx(filtro);
        List<DaTomaMx> tomaMxList = tomaMxService.getTomaMxByFiltro(filtroMx);
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        boolean nivelCentral = seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE);
        List<EntidadesAdtvas> entidades = new ArrayList<EntidadesAdtvas>();
        if (!nivelCentral)
            entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
        return tomaMxToJson(tomaMxList, entidades, nivelCentral);
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
        Date fechaInicioEnvio = null;
        Date fechaFinEnvio = null;
        Date fechaInicioToma = null;
        Date fechaFinToma = null;
        String codSilais = null;
        String codUnidadSalud = null;
        String tipoNotificacion = null;
        String nombreSolicitud = null;
        String finalRes = null;
        String nombreApellido = null;

        if (jObjectFiltro.get("nombrePersona") != null && !jObjectFiltro.get("nombrePersona").getAsString().isEmpty())
            nombreApellido = jObjectFiltro.get("nombrePersona").getAsString();
        if (jObjectFiltro.get("fechaInicioEnvio") != null && !jObjectFiltro.get("fechaInicioEnvio").getAsString().isEmpty())
            fechaInicioEnvio = DateUtil.StringToDate(jObjectFiltro.get("fechaInicioEnvio").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFinEnvio") != null && !jObjectFiltro.get("fechaFinEnvio").getAsString().isEmpty())
            fechaFinEnvio = DateUtil.StringToDate(jObjectFiltro.get("fechaFinEnvio").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsString();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsString();
        if (jObjectFiltro.get("tipoNotificacion") != null && !jObjectFiltro.get("tipoNotificacion").getAsString().isEmpty())
            tipoNotificacion = jObjectFiltro.get("tipoNotificacion").getAsString();
        if (jObjectFiltro.get("nombreSolicitud") != null && !jObjectFiltro.get("nombreSolicitud").getAsString().isEmpty())
            nombreSolicitud = jObjectFiltro.get("nombreSolicitud").getAsString();
        if (jObjectFiltro.get("finalRes") != null && !jObjectFiltro.get("finalRes").getAsString().isEmpty())
            finalRes = jObjectFiltro.get("finalRes").getAsString();
        if (jObjectFiltro.get("fechaInicioToma") != null && !jObjectFiltro.get("fechaInicioToma").getAsString().isEmpty())
            fechaInicioToma = DateUtil.StringToDate(jObjectFiltro.get("fechaInicioToma").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFinToma") != null && !jObjectFiltro.get("fechaFinToma").getAsString().isEmpty())
            fechaFinToma = DateUtil.StringToDate(jObjectFiltro.get("fechaFinToma").getAsString() + " 23:59:59");

        filtroMx.setNombreApellido(nombreApellido);
        filtroMx.setCodSilais(codSilais);
        filtroMx.setCodUnidadSalud(codUnidadSalud);
        filtroMx.setFechaInicioEnvio(fechaInicioEnvio);
        filtroMx.setFechaFinEnvio(fechaFinEnvio);
        filtroMx.setTipoNotificacion(tipoNotificacion);
        filtroMx.setResultadoFinal(finalRes);
        filtroMx.setNombreSolicitud(nombreSolicitud);
        filtroMx.setFechaInicioTomaMx(fechaInicioToma);
        filtroMx.setFechaFinTomaMx(fechaFinToma);

        return filtroMx;
    }

    /**
     * M�todo que convierte una lista de tomaMx a un string con estructura Json
     *
     * @param tomaMxList lista con las tomaMx a convertir
     * @return String
     */
    private String tomaMxToJson(List<DaTomaMx> tomaMxList, List<EntidadesAdtvas> entidades, boolean nivelCentral) {
        String jsonResponse;
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice = 0;
        boolean esEstudio;
        for (DaTomaMx tomaMx : tomaMxList) {
            //mostrar solo las muestras asociadas a los silais que tiene acceso el usuario
            if ((tomaMx.getCodSilaisAtencion()!=null && ( nivelCentral || entidades.contains(tomaMx.getCodSilaisAtencion())))
                    || (tomaMx.getIdNotificacion().getCodSilaisAtencion()!=null && entidades.contains(tomaMx.getIdNotificacion().getCodSilaisAtencion()))) {
                esEstudio = tomaMxService.getSolicitudesEstudioByIdTomaMx(tomaMx.getIdTomaMx()).size() > 0;
                Map<String, String> map = new HashMap<String, String>();
                map.put("idTomaMx", tomaMx.getIdTomaMx());
                map.put("codigoUnicoMx", esEstudio ? tomaMx.getCodigoUnicoMx() : (tomaMx.getCodigoLab() != null ? tomaMx.getCodigoLab() : (messageSource.getMessage("lbl.not.generated", null, null))));
                map.put("fechaTomaMx", DateUtil.DateToString(tomaMx.getFechaHTomaMx(), "dd/MM/yyyy") +
                        (tomaMx.getHoraTomaMx() != null ? " " + tomaMx.getHoraTomaMx() : ""));

                if (tomaMx.getIdNotificacion().getCodSilaisAtencion() != null) {
                    map.put("codSilais", tomaMx.getIdNotificacion().getCodSilaisAtencion().getNombre());
                } else {
                    map.put("codSilais", "");
                }
                if (tomaMx.getIdNotificacion().getCodUnidadAtencion() != null) {
                    map.put("codUnidadSalud", tomaMx.getIdNotificacion().getCodUnidadAtencion().getNombre());
                } else {
                    map.put("codUnidadSalud", "");
                }
                map.put("tipoNoti", tomaMx.getIdNotificacion().getCodTipoNotificacion().getValor());
                //laboratorio y area
                if (tomaMx.getEnvio() != null) {
                    map.put("laboratorio", tomaMx.getEnvio().getLaboratorioDestino().getNombre());
                } else {
                    map.put("laboratorio", "");
                }
                map.put("estadoMx", tomaMx.getEstadoMx().getValor());

                //Si hay persona
                if (tomaMx.getIdNotificacion().getPersona() != null) {
                    /// se obtiene el nombre de la persona asociada a la ficha
                    String nombreCompleto = "";
                    nombreCompleto = tomaMx.getIdNotificacion().getPersona().getPrimerNombre();
                    if (tomaMx.getIdNotificacion().getPersona().getSegundoNombre() != null)
                        nombreCompleto = nombreCompleto + " " + tomaMx.getIdNotificacion().getPersona().getSegundoNombre();
                    nombreCompleto = nombreCompleto + " " + tomaMx.getIdNotificacion().getPersona().getPrimerApellido();
                    if (tomaMx.getIdNotificacion().getPersona().getSegundoApellido() != null)
                        nombreCompleto = nombreCompleto + " " + tomaMx.getIdNotificacion().getPersona().getSegundoApellido();
                    map.put("persona", nombreCompleto);
                } else if (tomaMx.getIdNotificacion().getSolicitante() != null) {
                    map.put("persona", tomaMx.getIdNotificacion().getSolicitante().getNombre());
                } else {
                    map.put("persona", " ");
                }

                //se arma estructura de diagn?sticos o estudios
                List<DaSolicitudDx> solicitudDxList = tomaMxService.getSolicitudesDxByIdMX(tomaMx.getIdTomaMx());
                List<DaSolicitudEstudio> solicitudE = tomaMxService.getSolicitudesEstudioByIdTomaMx(tomaMx.getIdTomaMx());
                String solicitudes = "";
                if (!solicitudDxList.isEmpty()) {
                    int cont = 0;

                    for (DaSolicitudDx solicitudDx : solicitudDxList) {
                        cont++;
                        if (cont == solicitudDxList.size()) {
                            solicitudes += solicitudDx.getCodDx().getNombre();
                        } else {
                            solicitudes += solicitudDx.getCodDx().getNombre() + "," + " ";
                        }
                    }
                } else {
                    if (!solicitudE.isEmpty()) {
                        int cont = 0;
                        for (DaSolicitudEstudio solicitudDx : solicitudE) {
                            cont++;
                            if (cont == solicitudDxList.size()) {
                                solicitudes += solicitudDx.getTipoEstudio().getNombre();
                            } else {
                                solicitudes += solicitudDx.getTipoEstudio().getNombre() + "," + " ";
                            }
                        }
                    }
                }
                map.put("solicitudes", solicitudes);

                mapResponse.put(indice, map);
                indice++;
            }
        }
        jsonResponse = new Gson().toJson(mapResponse);
        //escapar caracteres especiales, escape de los caracteres con valor num�rico mayor a 127
        UnicodeEscaper escaper = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /**
     * M?todo que se llama al entrar a la opci?n de menu de Reportes "Reporte Resultados Positivos y Negativos".
     *
     * @param request para obtener informaci?n de la petici?n del cliente
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "editMx/{idMx}")
    public ModelAndView editMxForm( @PathVariable("idMx") String idMx, HttpServletRequest request) throws Exception {
        logger.debug("editar muestras");
        String urlValidacion;
        DaTomaMx tomaMx = new DaTomaMx();
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci?n del login fue exitosa
            tomaMx = tomaMxService.getTomaMxById(idMx);
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            List<EntidadesAdtvas> entidadesAdtvases = entidadAdmonService.getAllEntidadesAdtvas();
            List<Divisionpolitica> municipios = null;
            if (tomaMx.getCodSilaisAtencion()!=null){
                municipios = divisionPoliticaService.getMunicipiosBySilais(tomaMx.getCodSilaisAtencion().getCodigo());
            }
            List<Unidades> unidades = null;
            if (tomaMx.getCodUnidadAtencion()!=null && tomaMx.getCodSilaisAtencion()!=null){
                unidades = unidadesService.getPrimaryUnitsByMunicipio_Silais(tomaMx.getCodUnidadAtencion().getMunicipio().getCodigoNacional(),
                        tomaMx.getCodSilaisAtencion().getCodigo(), HealthUnitType.UnidadesPrimarias.getDiscriminator().split(","));
            }
            //se determina si es una muestra para estudio o para vigilancia rutinaria(Dx)
            List<DaSolicitudEstudio> solicitudEstudioList = tomaMxService.getSolicitudesEstudioByIdTomaMx(tomaMx.getIdTomaMx());
            boolean esEstudio = solicitudEstudioList.size()>0;
            mav.addObject("entidades", entidadesAdtvases);
            mav.addObject("municipios",municipios);
            mav.addObject("unidades",unidades);
            mav.addObject("muestra", tomaMx);
            mav.addObject("esEstudio",esEstudio);
            mav.setViewName("tomaMx/editForm");
        } else
            mav.setViewName(urlValidacion);

        return mav;
    }

    @RequestMapping(value = "getSolicitudes", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getSolicitudes(@RequestParam(value = "idTomaMx", required = true) String idTomaMx) throws Exception {
        logger.info("antes getSolicitudes");
        List<DaSolicitudDx> solicitudDxList = tomaMxService.getSolicitudesDxInicialesByIdMX(idTomaMx);
        List<DaSolicitudEstudio> solicitudEstudios = tomaMxService.getSolicitudesEstudioByIdTomaMx(idTomaMx);
        logger.info("despues getSolicitudes");
        return SolicitudesToJson(solicitudDxList, solicitudEstudios);
    }

    /**
     * M�todo para convertir una lista de solicitudes dx o estudios a un string con estructura Json
     * @param dxList lista con las solicitudes de diagn�sticos a convertir
     * @param estudioList lista con las solicitudes de estudio a convertir
     * @return String
     * @throws java.io.UnsupportedEncodingException
     */
    private String SolicitudesToJson(List<DaSolicitudDx> dxList, List<DaSolicitudEstudio> estudioList) throws UnsupportedEncodingException {
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaSolicitudDx dx : dxList){
            Map<String, String> map = new HashMap<String, String>();
            map.put("idTomaMx", dx.getIdTomaMx().getIdTomaMx());
            map.put("idSolicitud", dx.getIdSolicitudDx());
            map.put("nombre", dx.getCodDx().getNombre());
            map.put("fechaSolicitud", DateUtil.DateToString(dx.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
            map.put("tipo", messageSource.getMessage("lbl.routine",null,null));
            if (dx.getAprobada() != null) {
                if (dx.getAprobada().equals(true)) {
                    map.put("estado", (messageSource.getMessage("lbl.approval.result", null, null)));
                } else {
                    map.put("estado", (messageSource.getMessage("lbl.without.result", null, null)));
                }
            } else {
                List<DetalleResultadoFinal> detRes = resultadoFinalService.getDetResActivosBySolicitud(dx.getIdSolicitudDx());
                if (!detRes.isEmpty()) {
                    map.put("estado", (messageSource.getMessage("lbl.result.pending.approval", null, null)));
                } else {
                    map.put("estado", (messageSource.getMessage("lbl.without.result", null, null)));
                }
            }
            mapResponse.put(indice, map);
            indice ++;
        }

        for(DaSolicitudEstudio estudio : estudioList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idTomaMx", estudio.getIdTomaMx().getIdTomaMx());
            map.put("idSolicitud", estudio.getIdSolicitudEstudio() );
            map.put("nombre", estudio.getTipoEstudio().getNombre());
            map.put("fechaSolicitud", DateUtil.DateToString(estudio.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
            map.put("tipo",messageSource.getMessage("lbl.study",null,null));
            if (estudio.getAprobada() != null) {
                if (estudio.getAprobada().equals(true)) {
                    map.put("estado", (messageSource.getMessage("lbl.approval.result", null, null)));
                } else {
                    map.put("estado", (messageSource.getMessage("lbl.without.result", null, null)));
                }
            } else {
                List<DetalleResultadoFinal> detRes = resultadoFinalService.getDetResActivosBySolicitud(estudio.getIdSolicitudEstudio());
                if (!detRes.isEmpty()) {
                    map.put("estado", (messageSource.getMessage("lbl.result.pending.approval", null, null)));
                } else {
                    map.put("estado", (messageSource.getMessage("lbl.without.result", null, null)));
                }
            }
            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        //escapar caracteres especiales, escape de los caracteres con valor num�rico mayor a 127
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /**
     * M�todo para anular una orden de examen
     * @param request para obtener informaci�n de la petici�n del cliente. Contiene en un par�metro la estructura json del registro a anular
     * @param response para notificar al cliente del resultado de la operaci�n
     * @throws Exception
     */
    @RequestMapping(value = "anularSolicitud", method = RequestMethod.POST)
    protected void anularSolicitud(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("anular solicitud dx o estudio");
        String urlValidacion;
        String idSolicitud = "";
        String causaAnulacion = "";
        String json="";
        String resultado = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            idSolicitud = jsonpObject.get("idSolicitud").getAsString();
            causaAnulacion = jsonpObject.get("causaAnulacion").getAsString();
            DaSolicitudDx solicitudDx = tomaMxService.getSolicitudDxByIdSolicitud(idSolicitud);
            if(solicitudDx!=null){
                try{
                    tomaMxService.bajaSolicitudDx(seguridadService.obtenerNombreUsuario(request),idSolicitud, causaAnulacion);
                }catch (Exception ex){
                    logger.error("Error al anular solicitud dx",ex);
                    resultado = messageSource.getMessage("msg.request.cancel.error2", null, null);
                    resultado = resultado + ". \n " + ex.getMessage();
                }
            }else{
                DaSolicitudEstudio solicitudEst = tomaMxService.getSolicitudEstByIdSolicitud(idSolicitud);
                if(solicitudEst!=null){
                    try{
                        tomaMxService.bajaSolicitudEstudio(seguridadService.obtenerNombreUsuario(request),idSolicitud, causaAnulacion);
                    }catch (Exception ex){
                        logger.error("Error al anular solicitud de estudio",ex);
                        resultado = messageSource.getMessage("msg.request.cancel.error2", null, null);
                        resultado = resultado + ". \n " + ex.getMessage();
                    }
                }else{
                    throw new Exception(messageSource.getMessage("msg.request.notfound", null, null));
                }
            }

        }catch (Exception ex){
            logger.error("Sucedio un error al anular solicitud",ex);
            resultado = messageSource.getMessage("msg.request.cancel.error1", null, null);
            resultado = resultado + ". \n " + ex.getMessage();
        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idSolicitud", idSolicitud);
            map.put("causaAnulacion",causaAnulacion);
            map.put("mensaje",resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * M�todo para agregar una solicitud de dx o estudio para una mx
     * @param request para obtener informaci�n de la petici�n del cliente. Contiene en un par�metro la estructura json del registro a agregar
     * @param response para notificar al cliente del resultado de la operaci�n
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "agregarSolicitud", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void agregarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json;
        String resultado = "";
        boolean esEstudio=false;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            esEstudio = jsonpObject.get("esEstudio").getAsBoolean();
            if(esEstudio)
                resultado = agregarSolicitudEstudio(jsonpObject, request);
            else
                resultado = agregarSolicitudDx(jsonpObject, request);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.request.error",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idTomaMx","tmp");
            map.put("idDiagnostico", "tmp");
            map.put("idEstudio", "tmp");
            map.put("esEstudio",String.valueOf(esEstudio));
            map.put("mensaje",resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    private String agregarSolicitudDx(JsonObject jsonpObject, HttpServletRequest request) throws Exception {
        String resultado = "";
        String idTomaMx = "";
        int idDiagnostico = 0;
        idTomaMx = jsonpObject.get("idTomaMx").getAsString();
        idDiagnostico = jsonpObject.get("idDiagnostico").getAsInt();
        DaTomaMx tomaMx = tomaMxService.getTomaMxById(idTomaMx);
        //se valida si existe una orden activa para la muestra, el diagn�stico y el examen
        DaSolicitudDx solicitudDx = tomaMxService.getSolicitudesDxByMxDx(idTomaMx, idDiagnostico);
        if (solicitudDx!=null){
            if (tomaMx.getEstadoMx().getCodigo().equalsIgnoreCase("ESTDMX|PEND")
                    || (solicitudDx.getLabProcesa() !=null && solicitudDx.getLabProcesa().getCodigo().equalsIgnoreCase(solicitudDx.getIdTomaMx().getEnvio().getLaboratorioDestino().getCodigo()))){
                Catalogo_Dx dx = tomaMxService.getDxById(String.valueOf(idDiagnostico));
                resultado = messageSource.getMessage("msg.add.request.error2", null, null);
                resultado = resultado.replace("{0}", dx.getNombre());
            }

        }else{
            try {

                DaSolicitudDx soli = new DaSolicitudDx();
                soli.setCodDx(tomaMxService.getDxById(String.valueOf(idDiagnostico)));
                soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
                    soli.setUsarioRegistro(usuarioService.getUsuarioById((int)seguridadService.obtenerIdUsuario(request)));
                soli.setIdTomaMx(tomaMx);
                soli.setAprobada(false);
                if (tomaMx.getEnvio()!=null) {
                    soli.setLabProcesa(tomaMx.getEnvio().getLaboratorioDestino());
                }
                soli.setControlCalidad(false);
                soli.setInicial(true);
                tomaMxService.addSolicitudDx(soli);

            } catch (Exception ex) {
                resultado = messageSource.getMessage("msg.add.request.error", null, null);
                resultado = resultado + ". \n " + ex.getMessage();
                ex.printStackTrace();
            }

        }

        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(resultado);
    }

    private String agregarSolicitudEstudio(JsonObject jsonpObject, HttpServletRequest request) throws Exception {
        String resultado = "";
        String idTomaMx = "";
        int idEstudio = 0;
        idTomaMx = jsonpObject.get("idTomaMx").getAsString();
        idEstudio = jsonpObject.get("idEstudio").getAsInt();
        //se valida si existe una orden activa para la muestra, el diagn�stico y el examen
        DaSolicitudEstudio solicitudEstudio = tomaMxService.getSolicitudesEstudioByMxEst(idTomaMx, idEstudio);
        if (solicitudEstudio!=null){
            Catalogo_Estudio estudio = tomaMxService.getEstudioById(idEstudio);
            resultado = messageSource.getMessage("msg.add.request.error2", null, null);
            resultado = resultado.replace("{0}", estudio.getNombre());
        }else{
            try {
                DaSolicitudEstudio soli = new DaSolicitudEstudio();
                soli.setTipoEstudio(tomaMxService.getEstudioById(idEstudio));
                soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
                soli.setUsarioRegistro(usuarioService.getUsuarioById((int)seguridadService.obtenerIdUsuario(request)));
                soli.setIdTomaMx(tomaMxService.getTomaMxById(idTomaMx));
                soli.setAprobada(false);
                tomaMxService.addSolicitudEstudio(soli);

            } catch (Exception ex) {
                resultado = messageSource.getMessage("msg.add.request.error", null, null);
                resultado = resultado + ". \n " + ex.getMessage();
                ex.printStackTrace();
            }

        }

        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(resultado);
    }

    /**
     * Guardar toma de muestra de diagn�stico
     * @return ResponseEntity<String>
     * @throws Exception
     */
    @RequestMapping(value = "/editToma", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editTomaMx(HttpServletRequest request
            , @RequestParam(value = "fechaHTomaMx", required = false) String fechaHTomaMx
            , @RequestParam(value = "horaTomaMx", required = false) String horaTomaMx
            , @RequestParam(value = "canTubos", required = false) Integer canTubos
            , @RequestParam(value = "volumen", required = false) String volumen
            , @RequestParam(value = "horaRefrigeracion", required = false) String horaRefrigeracion
            , @RequestParam(value = "mxSeparada", required = false) Integer  mxSeparada
            , @RequestParam(value = "idTomaMx", required = true) String idTomaMx
            , @RequestParam(value = "codUnidadAtencion", required = false) Integer codUnidadAtencion
            , @RequestParam(value = "codSilaisAtencion", required = false) Integer codSilaisAtencion

    ) throws Exception {
        logger.debug("Guardando datos de Toma de Muestra");

        DaTomaMx tomaMx = tomaMxService.getTomaMxById(idTomaMx);
        if(fechaHTomaMx != null){
            tomaMx.setFechaHTomaMx(StringToTimestamp(fechaHTomaMx));
        }

        if(horaTomaMx != null){
            tomaMx.setHoraTomaMx(horaTomaMx);
        }

        tomaMx.setCanTubos(canTubos);

        if(volumen != null && !volumen.equals("")){
            tomaMx.setVolumen(Float.valueOf(volumen));
        }

        tomaMx.setHoraRefrigeracion(horaRefrigeracion);


        if(mxSeparada != null){
            if(mxSeparada == 1){
                tomaMx.setMxSeparada(true);
            }else {
                tomaMx.setMxSeparada(false);
            }
        }

        if(codSilaisAtencion == null && codUnidadAtencion == null){
            tomaMx.setCodSilaisAtencion(tomaMx.getIdNotificacion().getCodSilaisAtencion());
            tomaMx.setCodUnidadAtencion(tomaMx.getIdNotificacion().getCodUnidadAtencion());
        }else{
            tomaMx.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
            tomaMx.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
        }

        tomaMxService.addTomaMx(tomaMx);
        return createJsonResponse(tomaMx);
    }

    @RequestMapping(value = "override", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void anularMuestra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json;
        String resultado = "";
        String idMx=null;
        String causaAnulacion = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            idMx = jsonpObject.get("idMx").getAsString();
            causaAnulacion = jsonpObject.get("causaAnulacion").getAsString();
            DaTomaMx tomaMx = tomaMxService.getTomaMxById(idMx);
            if (tomaMx!=null) {
                if (tomaMx.getEstadoMx().getCodigo().equalsIgnoreCase("ESTDMX|PEND")) {
                    tomaMx.setAnulada(true);
                    tomaMx.setFechaAnulacion(new Timestamp(new Date().getTime()));
                    tomaMxService.updateTomaMx(tomaMx);
                    List<DaSolicitudDx> solicitudDxList = tomaMxService.getSolicitudesDxByIdMX(tomaMx.getIdTomaMx());
                    for (DaSolicitudDx solicitudDx : solicitudDxList) {
                        tomaMxService.bajaSolicitudDx(seguridadService.obtenerNombreUsuario(request), solicitudDx.getIdSolicitudDx(), causaAnulacion);
                    }
                }else
                {
                    throw new Exception(messageSource.getMessage("msg.sample.already.sent.lab",null,null));
                }

            }else{
                throw new Exception(messageSource.getMessage("msg.tomamx.notfound",null,null));
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.override.tomamx.error",null,null);
            resultado=resultado+". \n "+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMx",String.valueOf(idMx));
            map.put("mensaje",resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }
}
