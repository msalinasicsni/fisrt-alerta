package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.muestra.Catalogo_Estudio;
import ni.gob.minsa.alerta.domain.muestra.Estudio_UnidadSalud;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by souyen-ics.
 */
@Controller
@RequestMapping("administracion/studiesUS")
public class AdmonStudiesUSController {

    private static final Logger logger = LoggerFactory.getLogger(AdmonStudiesUSController.class);
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Qualifier(value = "studiesUsService")
    private AdmonStudiesUsService studiesUsService;

    @Autowired(required = true)
    @Qualifier(value = "entidadAdmonService")
    public EntidadAdmonService entidadAdmonService;

    @Autowired(required = true)
    @Qualifier(value = "usuarioService")
    public UsuarioService usuarioService;

    @Autowired(required = true)
    @Qualifier(value = "tomaMxService")
    public TomaMxService tomaMxService;

    @Autowired(required = true)
    @Qualifier(value = "unidadesService")
    public UnidadesService unidadesService;

    @Autowired
    MessageSource messageSource;


    /**
     * Carga la pantalla inicial para la administración de estudios por unidades de salud
      * @param request Con los datos de autenticación
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public ModelAndView initForm(HttpServletRequest request) throws Exception {
        logger.debug("Cargando lista de estudios");
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
          List<EntidadesAdtvas> entidades;

            entidades = entidadAdmonService.getAllEntidadesAdtvas();

            mav.setViewName("administracion/studiesUS");
            mav.addObject("entidades", entidades);
        }else
            mav.setViewName(urlValidacion);

        return mav;
    }


    /**
     * Carga el catálogo de estudios registrados. Acepta una solicitud GET para JSON
     * @return JSON
     * @throws Exception
     */
    @RequestMapping(value = "getStudies", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getStudies() throws Exception {
        logger.info("Obteniendo el catálogo de estudios");

        List<Catalogo_Estudio> estudioList = studiesUsService.getStudies();

        return listToJson(estudioList);
    }

    /**
     * Convierte una lista de catálogos de estudios en formato JSON
     * @param estudioList Catálogo de estudios
     * @return JSON
     */
    private String listToJson(List<Catalogo_Estudio> estudioList) {
        String jsonResponse = "";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice = 0;


        if (estudioList != null) {
            for (Catalogo_Estudio estudio : estudioList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("nombre", estudio.getNombre());
                map.put("idEstudio", estudio.getIdEstudio().toString());
                map.put("area", estudio.getArea().getNombre());
                mapResponse.put(indice, map);
                indice++;
            }
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /**
     * Carga el catálogo de unidades de salud asociadas a un estudio. Acepta una solicitud GET para JSON
     * @param idEstudio del Estudio a obtener unidades de salud asociadas
     * @return JSON
     * @throws Exception
     */
    @RequestMapping(value = "getAssociatedUS", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String fetchUSJson(@RequestParam(value = "idEstudio", required = true) Integer idEstudio) throws Exception{
        logger.info("Obteniendo las Unidades de Salud asociadas al estudio");

        List<Estudio_UnidadSalud> usList = null;

        usList = studiesUsService.getUsByIdEstudio(idEstudio);

        return usToJson(usList);
    }

    /**
     * Convierte una lista unidades de salud asociadas al estudio en formato JSON
     * @param usList Unidades de salud asociadas al estudio
     * @return JSON
     */
    private String usToJson(List<Estudio_UnidadSalud> usList) {
        String jsonResponse = "";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice = 0;

        if (usList != null) {
            for (Estudio_UnidadSalud us : usList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("idEstudioUnidad", us.getIdEstudioUnidad().toString());
                map.put("silais", us.getUnidad().getEntidadAdtva().getNombre());
                map.put("municipio", us.getUnidad().getMunicipio().getNombre());
                map.put("nombreUS", us.getUnidad().getNombre());
                mapResponse.put(indice, map);
                indice++;
            }
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /***
     * Agrega o actualiza un registro de unidad de salud asociada a un estudio. Si se envia idRecord se actualiza, en caso contrario se agrega
     * . Acepta una solicitud POST para JSON
     * @param request datos de autenticación y valores a guardar
     * @param response resultado de la operación
     * @throws Exception
     */
    @RequestMapping(value = "addUpdateUs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void addUpdateTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        Integer idRecord = 0;
        Integer us = 0;
        Integer idEstudio = 0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("idEstudio") != null && !jsonpObject.get("idEstudio").getAsString().isEmpty() ) {
                idEstudio = jsonpObject.get("idEstudio").getAsInt();
            }

            if (jsonpObject.get("us") != null && !jsonpObject.get("us").getAsString().isEmpty()) {
                us = jsonpObject.get("us").getAsInt();
            }

            if (jsonpObject.get("idRecord") != null && !jsonpObject.get("idRecord").getAsString().isEmpty()) {
                idRecord = jsonpObject.get("idRecord").getAsInt();
            }

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            if (idRecord == 0) {
                if (idEstudio != 0 && us != 0) {
                    //search log
                    Estudio_UnidadSalud record = studiesUsService.getStudyUsByIdEstUs(idEstudio, us);

                    if (record == null) {
                        Estudio_UnidadSalud estUs = new Estudio_UnidadSalud();
                        estUs.setFechaRegistro(new Date());
                        estUs.setUsuarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
                        estUs.setEstudio(tomaMxService.getEstudioById(idEstudio));
                        Unidades unidad = unidadesService.getUnidadByCodigo(us);
                        estUs.setUnidad(unidad);
                        estUs.setPasivo(false);
                        studiesUsService.addOrUpdateStudyUs(estUs);
                    } else {
                        resultado = messageSource.getMessage("msg.existing.record.error", null, null);
                        throw new Exception(resultado);
                    }
                }

            } else {
                Estudio_UnidadSalud rec = studiesUsService.getStudyUsByIdRecord(idRecord);
                if (rec != null) {
                    rec.setActor(seguridadService.obtenerNombreUsuario(request));
                    rec.setPasivo(true);
                    studiesUsService.addOrUpdateStudyUs(rec);
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.add.estUs.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();

        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idEstudio", String.valueOf(idEstudio));
            map.put("mensaje", resultado);
            map.put("idRecord", "");
            map.put("us", "");
            map.put("pasivo", "");
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

}
