package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.DatoSolicitudDetalle;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.Ocupacion;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FIRSTICT on 8/24/2015.
 * V1.0
 */
@Controller
@RequestMapping("paciente")
public class NotiPacienteController {
    private static final Logger logger = LoggerFactory.getLogger(SindFebrilController.class);
    @Resource(name="seguridadService")
    public SeguridadService seguridadService;
    @Resource(name="daNotificacionService")
    public DaNotificacionService daNotificacionService;
    @Resource(name="ocupacionService")
    public OcupacionService ocupacionService;
    @Resource(name="divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;
    @Resource(name = "comunidadesService")
    public ComunidadesService comunidadesService;
    @Resource(name = "datosSolicitudService")
    public DatosSolicitudService datosSolicitudService;

    @Autowired
    MessageSource messageSource;


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar una ficha de sindromes febriles");
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
            return "paciente/search";
        }else{
            return urlValidacion;
        }
    }

    /**
     * Si usuario  está autorizado muestra las notificaciones previas de la persona
     * @param idPerson a consultar
     * @param request datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaciÃ³n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
            //List<DaNotificacion> results = getResults(idPerson);
                //mav.addObject("fichas", results);
                mav.addObject("idPerson", idPerson);
                mav.setViewName("paciente/results");

        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * Consultar las notificaciones previas tipo PACIENTE de la persona
     * @param idPerson de la persona a consultar
     * @return List<DaNotificacion>
     * @throws Exception
     */
    @RequestMapping(value = "getResults", method = RequestMethod.GET,  produces = "application/json")
    public @ResponseBody
    List<DaNotificacion> getResults(@RequestParam(value = "idPerson", required = true) long idPerson) throws Exception {
        logger.info("Obteniendo los resultados de la búsqueda");
        List<DaNotificacion> results = null;
        results = daNotificacionService.getNoticesByPersonAndType(idPerson,"TPNOTI|PCNT");
        return results;
    }

    /**
     * Mostrar en pantalla el detalle de la notificación seleccioonada
     * @param idNotificacion de la notificación a mostrar
     * @param request datos datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping("detail/{idNotificacion}")
    public ModelAndView editReport(@PathVariable(value = "idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        boolean autorizado = false;
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
            DaNotificacion notificacion = daNotificacionService.getNotifById(idNotificacion);
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            List<Divisionpolitica> municipiosResi = null;
            List<Comunidades> comunidades = null;
            if(notificacion.getPersona().getMunicipioResidencia()!=null){
                municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(notificacion.getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
                comunidades = comunidadesService.getComunidades(notificacion.getPersona().getMunicipioResidencia().getCodigoNacional());
            }
            List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
            mav.addObject("notificacion",notificacion);
            mav.addObject("ocupaciones", ocupaciones);
            mav.addObject("departamentos", departamentos);
            mav.addObject("municipiosResi", municipiosResi);
            mav.addObject("comunidades", comunidades);
                mav.setViewName("paciente/detail");

        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     * Obtiene el detalle de la socilicitudes de dx asociados a la notificación
     * @param strIdNotificacion notificación a consultar
     * @return JSON
     * @throws Exception
     */
    @RequestMapping(value = "getDatosSolicitudDetalleByNotifi", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getDatosSolicitudDetalleBySolicitud(@RequestParam(value = "strIdNotificacion", required = true) String strIdNotificacion) throws Exception {
        logger.info("Se obtienen los detalles de resultados activos para la solicitud");
        List<DaSolicitudDx> diagnosticosList = datosSolicitudService.getSolicitudesDxByIdNotificacion(strIdNotificacion);
        return detalleDatoSolicitudToJson(diagnosticosList);
    }

    /**
     *  COnvierte una lista de solicitudes dx en formato JSON
     * @param solicitudDxList lista de solicitud de diagnósticos
     * @return JSON
     */
    public String detalleDatoSolicitudToJson(List<DaSolicitudDx> solicitudDxList) {
        String jsonResponse = "";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice = 0;
        String idSolicitud = "";
        for (DaSolicitudDx diagnostico : solicitudDxList) {
            Map<String, String> map = new HashMap<String, String>();
            idSolicitud = diagnostico.getIdSolicitudDx();
            map.put("tipoSolicitud", messageSource.getMessage("lbl.routine", null, null));
            map.put("nombreSolicitud", diagnostico.getCodDx().getNombre());
            map.put("fechaSolicitud", DateUtil.DateToString(diagnostico.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
            if (diagnostico.getFechaAprobacion()!=null) {
                map.put("fechaAprobacion", DateUtil.DateToString(diagnostico.getFechaAprobacion(), "dd/MM/yyyy hh:mm:ss a"));
            }else{
                map.put("fechaAprobacion"," ");
            }
            map.put("codigoUnicoMx", diagnostico.getIdTomaMx().getCodigoLab()!=null?diagnostico.getIdTomaMx().getCodigoLab():diagnostico.getIdTomaMx().getCodigoUnicoMx());
            map.put("tipoMx", diagnostico.getIdTomaMx().getCodTipoMx().getNombre());
            //detalle resultado solicitud
            List<DatoSolicitudDetalle> resultList = datosSolicitudService.getDatosSolicitudDetalleBySolicitud(idSolicitud);
            Map<Integer, Object> mapResList = new HashMap<Integer, Object>();
            int subIndice = 0;
            Map<String, String> mapRes = new HashMap<String, String>();
            for (DatoSolicitudDetalle res : resultList) {
                if (res.getDatoSolicitud() != null) {
                    if (res.getDatoSolicitud().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = datosSolicitudService.getCatalogoLista(res.getValor());
                        mapRes.put("valor", cat_lista.getValor());
                    } else if (res.getDatoSolicitud().getConcepto().getTipo().getCodigo().equals("TPDATO|LOG")) {
                        String valorBoleano = (Boolean.valueOf(res.getValor()) ? "lbl.yes" : "lbl.no");
                        mapRes.put("valor", messageSource.getMessage(valorBoleano, null, null));
                    } else {
                        mapRes.put("valor", res.getValor());
                    }
                    mapRes.put("respuesta", res.getDatoSolicitud().getNombre());

                }
                mapRes.put("fechaResultado", DateUtil.DateToString(res.getFechahRegistro(), "dd/MM/yyyy hh:mm:ss a"));
                mapResList.put(subIndice, mapRes);
                subIndice++;
                mapRes = new HashMap<String, String>();
            }
            map.put("resultado", new Gson().toJson(mapResList));

            mapResponse.put(indice, map);
            indice++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }
}
