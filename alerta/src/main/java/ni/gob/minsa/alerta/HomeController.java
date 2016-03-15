package ni.gob.minsa.alerta;

import ni.gob.minsa.alerta.domain.estructura.CalendarioEpi;
import ni.gob.minsa.alerta.service.CalendarioEpiService;
import ni.gob.minsa.alerta.service.HomeService;
import ni.gob.minsa.alerta.service.SeguridadService;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
	
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Qualifier(value = "homeService")
    private HomeService homeService;

    @Autowired
    @Qualifier(value = "calendarioEpiService")
    private CalendarioEpiService calendarioEpiService;

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
            CalendarioEpi calendarioEpi = calendarioEpiService.getCalendarioEpiByFecha("14/12/2014");
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
        List<Object[]> datos =  homeService.getDataMapas(codPato,nivelUsuario, (int)idUsuario ,semI, semF, anio, paisPorSILAIS);
        if (datos == null) {
            logger.debug("Nulo");
        }
        return datos;
    }
}
