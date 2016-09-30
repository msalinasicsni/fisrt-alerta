package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.agrupaciones.Grupo;
import ni.gob.minsa.alerta.domain.agrupaciones.GrupoPatologia;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.muestra.Catalogo_Estudio;
import ni.gob.minsa.alerta.domain.muestra.Estudio_UnidadSalud;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.AdmonPatoGroupService;
import ni.gob.minsa.alerta.service.SeguridadService;
import ni.gob.minsa.alerta.service.UsuarioService;
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
 * Created by FIRSTICT on 6/15/2016.
 * V1.0
 */
@Controller
@RequestMapping("administracion/patogroup")
public class AdmonPatoGroupController {
    private static final Logger logger = LoggerFactory.getLogger(AdmonPatoGroupController.class);
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired(required = true)
    @Qualifier(value = "usuarioService")
    public UsuarioService usuarioService;

    @Autowired(required = true)
    @Qualifier(value = "admonPatoGroupService")
    public AdmonPatoGroupService admonPatoGroupService;

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
        logger.debug("Cargando lista de patologias agrupadas");
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
            mav.setViewName("administracion/patoGroup");
        }else
            mav.setViewName(urlValidacion);

        return mav;
    }

    @RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getGroups() throws Exception {
        logger.info("Obteniendo el catálogo de grupos");
        List<Grupo> grupos = admonPatoGroupService.getGrupos();
        return groupsToJson(grupos);
    }

    private String groupsToJson(List<Grupo> grupos) {
        String jsonResponse = "";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice = 0;

        if (grupos != null) {
            List<GrupoPatologia> grupoPatologias;
            String patologias = "";
            for (Grupo grupo : grupos) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("idGrupo", grupo.getIdGrupo().toString());
                map.put("nombre", grupo.getNombre());
                grupoPatologias = admonPatoGroupService.getGrupoPatologias(grupo.getIdGrupo());
                for(GrupoPatologia grupoP : grupoPatologias){
                    patologias += grupoP.getPatologia().getCodigo() + " - " + grupoP.getPatologia().getNombre() + ", ";
                }
                map.put("patologias",patologias);
                mapResponse.put(indice, map);
                patologias = "";
                indice++;
            }
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    @RequestMapping(value = "getPathoGroup", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<GrupoPatologia> getPathoGroup(
            @RequestParam(value = "idGroup", required = true) Integer idGrupo    ) throws Exception {
        logger.info("Obteniendo patologias del grupo");
        List<GrupoPatologia> grupoPatologias = admonPatoGroupService.getGrupoPatologias(idGrupo);
        return grupoPatologias;
    }

    @RequestMapping(value = "getPathoAvailableGroup", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<SivePatologias> getPathoAvailableGroup(
            @RequestParam(value = "idGroup", required = true) Integer idGrupo    ) throws Exception {
        logger.info("Obteniendo patologias disponibles para el grupo");
        List<SivePatologias> patologias = admonPatoGroupService.getPalogiasDisponibles(idGrupo);
        return patologias;
    }

    @RequestMapping(value = "addOrUpdateGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void addOrUpdateGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        String nombre = "";
        Integer idGrupo = 0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("idGrupo") != null && !jsonpObject.get("idGrupo").getAsString().isEmpty() ) {
                idGrupo = jsonpObject.get("idGrupo").getAsInt();
            }

            if (jsonpObject.get("nombre") != null && !jsonpObject.get("nombre").getAsString().isEmpty()) {
                nombre = jsonpObject.get("nombre").getAsString();
            }

            long idUsuario = seguridadService.obtenerIdUsuario(request);

            Grupo grupo;
            if (idGrupo!=0){
                grupo = admonPatoGroupService.getGrupoById(idGrupo);
            }else{
                grupo = new Grupo();
                grupo.setFechaRegistro(new Date());
                grupo.setUsuarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            }
            grupo.setNombre(nombre);
            admonPatoGroupService.addOrUpdateGroup(grupo);
            idGrupo = grupo.getIdGrupo();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.add.pathoGroup.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();

        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idGrupo", String.valueOf(idGrupo));
            map.put("nombre", nombre);
            map.put("mensaje", resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    @RequestMapping(value = "addPatoGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void addPatoGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        Integer idPatologia = 0;
        Integer idGrupo = 0;
        String nombreGrupo = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("idGrupo") != null && !jsonpObject.get("idGrupo").getAsString().isEmpty() ) {
                idGrupo = jsonpObject.get("idGrupo").getAsInt();
            }

            if (jsonpObject.get("idPatologia") != null && !jsonpObject.get("idPatologia").getAsString().isEmpty()) {
                idPatologia = jsonpObject.get("idPatologia").getAsInt();
            }

            if (jsonpObject.get("nombreGrupo") != null && !jsonpObject.get("nombreGrupo").getAsString().isEmpty()) {
                nombreGrupo = jsonpObject.get("nombreGrupo").getAsString();
            }


            long idUsuario = seguridadService.obtenerIdUsuario(request);
            Grupo grupo;
            if (idGrupo!=0){
                grupo = admonPatoGroupService.getGrupoById(idGrupo);
            }else{ // aún no se ha guardado el grupo.. por tanto se agrega primero el grupo y luego se asocia la patología
                grupo = new Grupo();
                grupo.setNombre(nombreGrupo);
                grupo.setFechaRegistro(new Date());
                grupo.setUsuarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
                admonPatoGroupService.addOrUpdateGroup(grupo);
                idGrupo = grupo.getIdGrupo();
            }

            GrupoPatologia grupoPatologia = new GrupoPatologia();
            grupoPatologia.setFechaRegistro(new Date());
            grupoPatologia.setUsuarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            grupoPatologia.setGrupo(grupo);
            grupoPatologia.setPatologia(admonPatoGroupService.getPatologiaById(idPatologia));
            admonPatoGroupService.addPatoGroup(grupoPatologia);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.add.pathoGroup.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();
        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idGrupo", String.valueOf(idGrupo));
            map.put("idPatologia", String.valueOf(idPatologia));
            map.put("nombreGrupo", nombreGrupo);
            map.put("mensaje", resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    @RequestMapping(value = "deletePatoGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void deletePatoGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        Integer idPatoGrupo = 0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("idPatoGrupo") != null && !jsonpObject.get("idPatoGrupo").getAsString().isEmpty() ) {
                idPatoGrupo = jsonpObject.get("idPatoGrupo").getAsInt();
            }
            GrupoPatologia grupoPatologia;

            if (idPatoGrupo!=0){
                grupoPatologia = admonPatoGroupService.getGrupoPatologiaById(idPatoGrupo);
                admonPatoGroupService.deletePatoGroup(grupoPatologia);
            }else{
                resultado = messageSource.getMessage("msg.patho.group.not.found", null, null);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.delete.pathoGroup.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();

        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idPatoGrupo", String.valueOf(idPatoGrupo));
            map.put("mensaje", resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    @RequestMapping(value = "deleteGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void deleteGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        Integer idGrupo = 0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("idGrupo") != null && !jsonpObject.get("idGrupo").getAsString().isEmpty() ) {
                idGrupo = jsonpObject.get("idGrupo").getAsInt();
            }
            Grupo grupo;
            List<GrupoPatologia> grupoPatologias;

            if (idGrupo!=0){
                String actor = seguridadService.obtenerNombreUsuario(request);
                grupo = admonPatoGroupService.getGrupoById(idGrupo);
                grupoPatologias = admonPatoGroupService.getGrupoPatologias(idGrupo);
                for(GrupoPatologia grupoPatologia : grupoPatologias) {
                    grupoPatologia.setActor(actor);
                    admonPatoGroupService.deletePatoGroup(grupoPatologia);
                }
                grupo.setActor(actor);
                admonPatoGroupService.deleteGroup(grupo);
            }else{
                resultado = messageSource.getMessage("msg.group.not.found", null, null);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.delete.group.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();

        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idGrupo", String.valueOf(idGrupo));
            map.put("mensaje", resultado);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }
}
