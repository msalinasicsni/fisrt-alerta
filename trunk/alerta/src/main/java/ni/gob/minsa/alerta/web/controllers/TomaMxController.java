package ni.gob.minsa.alerta.web.controllers;

import ni.gob.minsa.alerta.domain.irag.DaIrag;
import ni.gob.minsa.alerta.domain.muestra.DaTomaMx;
import ni.gob.minsa.alerta.domain.muestra.TipoMx;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by souyen-ics on 11-05-14.
 */
@Controller
@RequestMapping("/tomaMx")
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
    List<TipoMx> catTipoMx;


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
     * Retorna una lista de irag.
     * @return Un arreglo JSON de irag
     */
    @RequestMapping(value = "notices", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DaNotificacion> personasJson(@RequestParam(value = "strFilter", required = true) String filtro) {
        logger.info("Obteniendo las notificaciones en JSON");
        List<DaNotificacion> notices = daNotificacionService.getNoticesByPerson(filtro);

        return notices;
    }

    /**
     * Handler for create tomaMx.
     *
     * @param idNotificacion the ID of the notification
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("create/{idNotificacion}")
    public ModelAndView createTomaMxWithIrag(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        boolean autorizado = false;
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
            DaTomaMx tomaMx = new DaTomaMx();
            DaNotificacion noti = daNotificacionService.getNotifById(idNotificacion);
            if (noti != null) {

                catTipoMx = catalogoService.getTipoMuestra();

                mav.addObject("noti", noti);
                mav.addObject("tomaMx", tomaMx);
                mav.addObject("autorizado", autorizado);
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

}
