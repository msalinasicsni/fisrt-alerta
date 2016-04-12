package ni.gob.minsa.alerta;

import com.google.gson.Gson;
import ni.gob.minsa.alerta.domain.estructura.CalendarioEpi;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudEstudio;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
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
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {
	
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Resource(name = "seguridadService")
    private SeguridadService seguridadService;

    @Resource(name = "homeService")
    private HomeService homeService;

    @Resource(name = "calendarioEpiService")
    private CalendarioEpiService calendarioEpiService;

    @Resource(name = "envioMxService")
    private EnvioMxService envioMxService;

    @Resource(name = "resultadoFinalService")
    private ResultadoFinalService resultadoFinalService;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request, Model model) throws Exception{
        logger.info("Starting project...");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if (urlValidacion.isEmpty()) {
            String semI="1";
            String semF="";
            String anioF="";
            String anioI="";
            //CalendarioEpi calendarioEpi = calendarioEpiService.getCalendarioEpiByFecha("14/12/2014");
            CalendarioEpi calendarioEpi = calendarioEpiService.getCalendarioEpiByFecha(DateUtil.DateToString(new Date(),"dd/MM/yyyy"));
            if (calendarioEpi!=null) {
                semF = String.valueOf(calendarioEpi.getNoSemana());
                anioF = String.valueOf(calendarioEpi.getAnio());
                anioI = String.valueOf(calendarioEpi.getAnio() - 2);
            }
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            String nivelUsuario = seguridadService.getNivelUsuario((int)idUsuario);
            model.addAttribute("semanaI",semI);
            model.addAttribute("semanaF",semF);
            model.addAttribute("anioF",anioF);
            model.addAttribute("anioI",anioI);
            model.addAttribute("nivel",nivelUsuario);
            return "home";
        }else{
            return urlValidacion;
        }

    }
    
    @RequestMapping(value="/403", method = RequestMethod.GET)
	public String noAcceso() {
		return "403"; 
	}
	
	@RequestMapping(value="/404", method = RequestMethod.GET)
	public String noEncontrado() { 
		return "404";
	}

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String salir(HttpServletRequest request) {
        seguridadService.logOut(request.getSession());
        return "redirect:"+seguridadService.obtenerUrlPortal();
    }


    /**
     * Retorna una lista de datos. Acepta una solicitud GET para JSON
     * @return Un arreglo JSON
     * @throws java.text.ParseException
     */
    @RequestMapping(value = "inicio/casostasasdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Object[]> fetchCasosTasasDataJsonInicio(HttpServletRequest request,
                                                 @RequestParam(value = "codPato", required = true) String codPato,
                                                 @RequestParam(value = "semI", required = true) String semI,
                                                 @RequestParam(value = "semF", required = true) String semF,
                                                 @RequestParam(value = "anioI", required = true) String anioI,
                                                 @RequestParam(value = "anioF", required = true) String anioF,
                                                 @RequestParam(value = "nivel", required = true) String nivelUsuario) throws Exception  {
        logger.info("Obteniendo los datos de casos y tasas en JSON");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<Object[]> datos = homeService.getDataCasosTasas(codPato, nivelUsuario, idUsuario, semI, semF, anioI, anioF);
        if (datos == null) {
            logger.debug("Nulo");
        }
       return datos;
    }

    @RequestMapping(value = "inicio/mapasdata", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Object[]> fetchMapasDataJson( HttpServletRequest request,
                                                           @RequestParam(value = "codPato", required = true) String codPato,
                                                           @RequestParam(value = "semI", required = true) String semI,
                                                           @RequestParam(value = "semF", required = true) String semF,
                                                           @RequestParam(value = "anio", required = true) String anio,
                                                           @RequestParam(value = "nivel", required = true) String nivelUsuario,
                                                           @RequestParam(value = "nivelPais", required = false) boolean paisPorSILAIS) throws ParseException {
        logger.info("Obteniendo los datos de mapas en JSON");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<Object[]> datos =  homeService.getDataMapas(codPato, nivelUsuario, (int) idUsuario, semI, semF, anio, paisPorSILAIS);
        if (datos == null) {
            logger.debug("Nulo");
        }
        return datos;
    }

    @RequestMapping(value = "inicio/sinresultado", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String fetchNotiSinResulDataJson( HttpServletRequest request,
                                                            @RequestParam(value = "nivel", required = true) String nivelUsuario,
                                                            @RequestParam(value = "conSubUnidades", required = false) boolean conSubUnidades) throws ParseException {
        logger.info("Obteniendo los datos notificaciones sin resultado en JSON");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<DaNotificacion> datos =  homeService.getDataSinResultado(nivelUsuario, (int) idUsuario, conSubUnidades);
        if (datos == null) {
            logger.debug("Nulo");
        }
        return notificacionesSRToJson(datos);
    }

    @RequestMapping(value = "inicio/embarazadas", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String fetchEmbarazadasDataJson( HttpServletRequest request,
                                                           @RequestParam(value = "nivel", required = true) String nivelUsuario,
                                                           @RequestParam(value = "conSubUnidades", required = false) boolean conSubUnidades) throws ParseException {
        logger.info("Obteniendo los datos de notificaciones para embarazadas");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<DaNotificacion> datos =  homeService.getDataEmbarazadas(nivelUsuario, (int) idUsuario, conSubUnidades);
        if (datos == null) {
            logger.debug("Nulo");
        }
        return notificacionesEmbarazadasToJson(datos);
    }

    @RequestMapping(value = "inicio/hospitalizados", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String fetchHospitalizadosDataJson( HttpServletRequest request,
                                                          @RequestParam(value = "nivel", required = true) String nivelUsuario,
                                                          @RequestParam(value = "conSubUnidades", required = false) boolean conSubUnidades) throws ParseException {
        logger.info("Obteniendo los datos de notificaciones para hospitalizados");
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<DaNotificacion> datos =  homeService.getDataHospitalizados(nivelUsuario, (int) idUsuario, conSubUnidades);
        if (datos == null) {
            logger.debug("Nulo");
        }
        return notificacionesHospToJson(datos);
    }

    /**
     * Convierte a formato JSON una lista de notificaciones sin resultado
     * @param notificacions lista a convertir
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

    /**
     * Convierte a formato JSON una lista de notificaciones de embarazadas
     * @param notificacions lista a convertir
     * @return JSON
     */
    private String notificacionesEmbarazadasToJson(List<DaNotificacion> notificacions){
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
                if (notificacion.getPersona().getMunicipioResidencia()!=null){
                    map.put("municipio",notificacion.getPersona().getMunicipioResidencia().getNombre());
                }else{
                    map.put("municipio","--");
                }
            }else{
                map.put("persona"," ");
                map.put("edad"," ");
                map.put("municipio","");
            }

            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /**
     * Convierte a formato JSON una lista de notificaciones de hospitalizados
     * @param notificacions lista a convertir
     * @return JSON
     */
    private String notificacionesHospToJson(List<DaNotificacion> notificacions){
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
}
