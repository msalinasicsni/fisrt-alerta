package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.typeAdapter.StringUtil;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            //si la url esta vacia significa que la validación del login fue exitosa
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
            //si la url esta vacia significa que la validación del login fue exitosa
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
     * Retorna una lista de dx según el tipo de muestra y de notificación
     * @param codMx tipo de muestra
     * @param tipoNoti tipo de notificación
     * @return Un arreglo JSON de dx
     * @throws Exception
     */
    @RequestMapping(value = "dxByMx", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Dx_TipoMx_TipoNoti> getDxBySample(@RequestParam(value = "codMx", required = true) String codMx, @RequestParam(value = "tipoNoti", required = true) String tipoNoti) throws Exception {
        logger.info("Obteniendo los diagnósticos segun muestra y tipo de Notificacion en JSON");
        return tomaMxService.getDx(codMx, tipoNoti);
    }

    /**
     * Obtener tomas de muestra de una notificación
     * @param idNotificacion de la notificación a consultar
     * @return List<DaTomaMx>
     * @throws Exception
     */
    @RequestMapping(value = "tomaMxByIdNoti", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaTomaMx> getTestBySample(@RequestParam(value = "idNotificacion", required = true) String idNotificacion) throws Exception {
        logger.info("Realizando búsqueda de Toma de Mx.");

        return tomaMxService.getTomaMxByIdNoti(idNotificacion);
    }

    /**
     * Guardar toma de muestra de diagnóstico
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

    ) throws Exception {
        logger.debug("Guardando datos de Toma de Muestra");

        DaTomaMx tomaMx = new DaTomaMx();

        DaNotificacion notifi= daNotificacionService.getNotifById(idNotificacion);
        tomaMx.setIdNotificacion(notifi);
        if(fechaHTomaMx != null){
            tomaMx.setFechaHTomaMx(StringToTimestamp(fechaHTomaMx));
        }

        if(horaTomaMx != null){
            tomaMx.setHoraTomaMx(horaTomaMx);
        }

        tomaMx.setCodTipoMx(tomaMxService.getTipoMxById(codTipoMx));
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

        tomaMx.setFechaRegistro(new Timestamp(new Date().getTime()));
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        tomaMx.setUsuario(usuarioService.getUsuarioById((int)idUsuario));
        tomaMx.setEstadoMx(catalogoService.getEstadoMx("ESTDMX|PEND"));

        if(codSilaisAtencion == null && codUnidadAtencion == null){
            tomaMx.setCodSilaisAtencion(notifi.getCodSilaisAtencion());
            tomaMx.setCodUnidadAtencion(notifi.getCodUnidadAtencion());
        }else{
            tomaMx.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
            tomaMx.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
        }

        String codigo = generarCodigoUnicoMx();
        tomaMx.setCodigoUnicoMx(codigo);
        tomaMx.setCodigoLab(null);
        tomaMxService.addTomaMx(tomaMx);
        saveDxRequest(tomaMx.getIdTomaMx(), dx, request);
        return createJsonResponse(tomaMx);
    }

    /**
     * Guardar solicitudes de dx para una muestra
     * @param idTomaMx a la que pertenecen las solicitudes
     * @param dx códigod de los dx solicitados
     * @param request con los datos de autenticación
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

    /**
     * Método para generar un string alfanumérico de 8 caracteres, que se usará como código único de muestra
     * @return String codigoUnicoMx
     */
    private String generarCodigoUnicoMx(){
        DaTomaMx validaC;
        //Se genera el código
        String codigoUnicoMx = StringUtil.getCadenaAlfanumAleatoria(8);
        //Se consulta BD para ver si existe toma de Mx que tenga mismo código
        validaC = tomaMxService.getTomaMxByCodUnicoMx(codigoUnicoMx);
        //si existe, de manera recursiva se solicita un nuevo código
        if (validaC!=null){
            codigoUnicoMx = generarCodigoUnicoMx();
        }
        //si no existe se retorna el último código generado
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
            //si la url esta vacia significa que la validación del login fue exitosa
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
            //si la url esta vacia significa que la validación del login fue exitosa
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
     * Retorna una lista de estudios por tipo de muestra y notificación
     * @return Un arreglo JSON de dx
     */
    @RequestMapping(value = "getStudiesBySampleAndNoti", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Estudio_TipoMx_TipoNoti> getEstudioBySampleAndNoti(@RequestParam(value = "codMx", required = true) String codMx,
                                                            @RequestParam(value = "tipoNoti", required = true) String tipoNoti,
                                                            @RequestParam(value = "idUnidadSalud", required = true) int idUnidadSalud) throws Exception {
        logger.info("Obteniendo los diagnósticos segun muestra y tipo de Notificacion en JSON");
        return tomaMxService.getEstudiosByTipoMxTipoNoti(codMx, tipoNoti,Long.valueOf(idUnidadSalud));
    }

    /**
     * Guardar tomas de muestra de estudio
     * @param request con los datos de autenticación y datos de la muestra a guardar
     * @param response con el resultado de la operación
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
            soli.setTipoEstudio(tomaMxService.getEstudioById(idEstudio));
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
            soli.setTipoEstudio(tomaMxService.getEstudioById(idEstudio.getAsString()));
            soli.setFechaHSolicitud(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            soli.setUsarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            soli.setIdTomaMx(tomaMxService.getTomaMxById(idTomaMx));
            tomaMxService.addSolicitudEstudio(soli);
        }
    }

}
