package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.examen.CatalogoExamenes;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    List<DaNotificacion> personasJson(@RequestParam(value = "strFilter", required = true) String filtro) {
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

            if (noti != null) {
                catTipoMx = tomaMxService.getTipoMxByTipoNoti(noti.getCodTipoNotificacion().getCodigo());

                mav.addObject("noti", noti);
                mav.addObject("tomaMx", tomaMx);
                mav.addObject("catTipoMx", catTipoMx);
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
     * Retorna una lista de examenes
     * @return Un arreglo JSON de examenes
     */
    @RequestMapping(value = "examenesByMuestra", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Examen_TipoMxTipoNoti> getTestBySample(@RequestParam(value = "codMx", required = true) String codMx, @RequestParam(value = "tipoNoti", required = true) String tipoNoti) throws Exception {
        logger.info("Obteniendo los examenes segun muestra en JSON");

        return tomaMxService.getExamenes(codMx, tipoNoti);

    }


    @RequestMapping(value = "tomaMxByIdNoti", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaTomaMx> getTestBySample(@RequestParam(value = "idNotificacion", required = true) String idNotificacion) throws Exception {
        logger.info("Realizando b�squeda de Toma de Mx.");

        return tomaMxService.getTomaMxByIdNoti(idNotificacion);

    }

    @RequestMapping(value = "/saveToma", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveTomaMx(HttpServletRequest request,
              @RequestParam(value = "examenes", required = false) String examenes
            , @RequestParam(value = "fechaHTomaMx", required = false) String fechaHTomaMx
            , @RequestParam(value = "codTipoMx", required = false) String codTipoMx
            , @RequestParam(value = "canTubos", required = false) Integer canTubos
            , @RequestParam(value = "volumen", required = false) String volumen
            , @RequestParam(value = "horaRefrigeracion", required = false) String horaRefrigeracion
            , @RequestParam(value = "mxSeparada", required = false) Integer  mxSeparada
            , @RequestParam(value = "idNotificacion", required = false) String idNotificacion

    ) throws Exception {
        logger.debug("Guardando datos de Toma de Muestra");

        DaTomaMx tomaMx = new DaTomaMx();

        tomaMx.setIdNotificacion(daNotificacionService.getNotifById(idNotificacion));
        tomaMx.setExamenes(examenes);
        if(fechaHTomaMx != null){
            tomaMx.setFechaHTomaMx(StringToTimestamp(fechaHTomaMx));
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
        tomaMxService.addTomaMx(tomaMx);
        saveOrder(tomaMx.getIdTomaMx(), tomaMx.getExamenes(), request);
        return createJsonResponse(tomaMx);
    }

    private void saveOrder(String idTomaMx, String examenes, HttpServletRequest request) throws Exception {

        DaOrdenExamen orden = new DaOrdenExamen();
        String[] arrayExa = examenes.split(",");

        for (String anArrayExa : arrayExa) {
            orden.setCodEstado(catalogoService.getEstadoOrdenEx("ESTORDEN|PEND"));
            orden.setCodExamen(tomaMxService.getExamById(anArrayExa));
            orden.setFechaHOrden(new Timestamp(new Date().getTime()));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            orden.setUsarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            orden.setIdTomaMx(tomaMxService.getTomaMxById(idTomaMx));

            tomaMxService.addOrdenExamen(orden);
        }

    }



    private Timestamp StringToTimestamp(String fechah) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
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



}
