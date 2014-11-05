package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.*;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.alerta.utilities.enumeration.surveyModelType;
import ni.gob.minsa.alerta.utilities.typeAdapter.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Maneja los registro de encuesta entomologica por tipo de Encuesta
 *
 * @author Miguel Salinas
 */
@Controller
@RequestMapping("/encuesta")
public class EntomologiaController {

    //region DECLARACIONES
    private static final Logger logger = LoggerFactory.getLogger(EntomologiaController.class);

    private static final String COD_NACIONAL_MUNI_MANAGUA = "5525";

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
    MessageSource messageSource;

    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Qualifier(value = "divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;

    @Autowired
    @Qualifier(value = "entidadAdmonService")
    private EntidadAdmonService silaisServce;

    @Autowired
    @Qualifier(value = "unidadesService")
    private UnidadesService unidadesService;

    @Autowired
    @Qualifier(value = "comunidadesService")
    private ComunidadesService comunidadesService;

    @Autowired
    @Qualifier(value = "catalogosService")
    private CatalogoService catalogosService;

    @Autowired
    @Qualifier(value = "detalleEncuestaAedesService")
    private DaDetalleEncuestaAedesService detalleEncuestaAedesService;

    @Autowired
    @Qualifier(value = "detalleEncuestaLarvariaService")
    private DaDetalleEncuestaLarvariaService detalleEncuestaLarvariaService;

    @Autowired
    @Qualifier(value = "detalleDepositoPreferencialService")
    private DaDetalleDepositoPreferencialService detalleDepositoPreferencialService;

    @Autowired
    @Qualifier(value = "daMaeEncuestaService")
    private DaMaeEncuestaService daMaeEncuestaService;

    @Autowired
    @Qualifier(value = "calendarioEpiService")
    private CalendarioEpiService calendarioEpiService;

    @Autowired
    @Qualifier(value = "usuarioService")
    private UsuarioService usuarioService;

    Map<String, Object> mapModel;
    List<Divisionpolitica> municipios;
    List<EntidadesAdtvas> silais;
    List<Divisionpolitica> departamentos;
    List<Unidades> unidadesSalud;
    List<Comunidades> comunidades;
    List<Procedencia> procedencias;
    List<Ordinal> ordinales;
    List<Distritos> distritosMng;
    List<Areas> areasMng;
    //endregion

    //region REGISTRO AEDES

    /**
     * M Salinas
     * Método que retorna la vista para registrar una encuesta modelo aedes aegypti
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "create/aedes", method = RequestMethod.GET)
    public ModelAndView initCreationForm(HttpServletRequest request) throws Exception {
        logger.debug("Getting data from encuesta Entomologica");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.ValidarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.ValidarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView("");
        if (urlValidacion.isEmpty()) {
            mav.setViewName("encuesta/registrarEncuestaAedes");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
             silais = silaisServce.getAllEntidadesAdtvas();
            }else {
                silais = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            procedencias = catalogosService.getProcedencia();
            ordinales = catalogosService.getOrdinalEncuesta();
            //List<Divisionpolitica> muni = seguridadService.obtenerMunicipiosPorUsuarioEntidad((int)infoSesion.getUsuarioId(),10,UtilitySecurity.SYSTEM_CODE);
            //List<Unidades> prueba = seguridadService.obtenerUnidadesPorUsuario((int)infoSesion.getUsuarioId(), UtilitySecurity.SYSTEM_CODE, HealthUnitType.UnidadesPrimarias.getDiscriminator());
            mav.addObject("entidades", silais);
            mav.addObject("procedencias", procedencias);
            mav.addObject("ordinales", ordinales);
            mav.addObject("fechaHoy", DateToString(new Date()));
        }else
        {
            mav.setViewName(urlValidacion);
        }
        return  mav;
    }

    /**
     * Método utilizado para guardar una encuesta modelo aedes aegypti
     * @param request Contiene los datos del maestro y del detalle a agregar
     * @param response Contiene String en formato json
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "guardarAedes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void guardarAedes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String strMaestro=null;
        String strDetalle=null;
        String idMaestro = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            json = br.readLine();

            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();
            Timestamp fechaRegistro = new Timestamp(new Date().getTime());
            //Obteniendo maestro encuesta
            //List<DaMaeEncuesta> daMaeEncuestaList;
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro, surveyModelType.AedesAegypti.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito() != null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea() != null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            //daMaeEncuestaList = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            //if (daMaeEncuestaList!=null && daMaeEncuestaList.size() > 0){
            //  idMaestro = daMaeEncuestaList.get(0).getEncuestaId();
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, String.valueOf(daMaeEncuesta.getEntidadesAdtva().getCodigo()))
                    && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, String.valueOf(daMaeEncuesta.getUnidadSalud().getCodigo()))) {

                if (daMaeEncuesta.getEncuestaId() != null && !daMaeEncuesta.getEncuestaId().isEmpty()) {
                    idMaestro = daMaeEncuesta.getEncuestaId();
                } else {

                    daMaeEncuesta.setFechaRegistro(fechaRegistro);
                    //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                    idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
                }
                //final Gson gson2 = gsonDetalleEncuestaAedes.create();
                if (idMaestro != null) {
                    try {

                        daMaeEncuesta.setEncuestaId(idMaestro);
                        //json = json.replace("00-00-00",idMaestro);
                        //Obteniedo detalle encuesta
                        DaDetalleEncuestaAedes daDetalleEncuestaAedes = JsonToDetalleEncuestaAedes(strDetalle);

                        //Se guada el detalle y se setea el id del maestro recien guardado
                        daDetalleEncuestaAedes.setMaeEncuesta(daMaeEncuesta);
                        daDetalleEncuestaAedes.setFeRegistro(fechaRegistro);
                        detalleEncuestaAedesService.addDaDetalleEncuestaAedes(daDetalleEncuestaAedes);
                    } catch (Exception ex) {
                        //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                        if (daMaeEncuesta.getEncuestaId() != null && !daMaeEncuesta.getEncuestaId().isEmpty())
                            daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                        resultado = messageSource.getMessage("msg.ento.error.adding.detail", null, null);
                        logger.error(ExceptionUtils.getStackTrace(ex));
                        throw new Exception(resultado);
                    }
                } else {
                    resultado = messageSource.getMessage("msg.ento.error.get.id", null, null);
                    throw new Exception(resultado);
                }
            }else{
                resultado = messageSource.getMessage("msg.not.authorized.save.entity.or.unit", null, null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado = messageSource.getMessage("msg.ento.error.add",null,null);
            resultado=resultado+". \n "+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Método para validar si la localidad seleccionado por el usuario ya fue agregada al maestro de la encuesta informada
     * @param datosEncuesta Contiene el id de la localidad y el id del maestro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "comunidadExisteEncuestaAedes", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaDetalleEncuestaAedes> getComunidadEnEncuestaAedes(@RequestParam(value = "datosEncuesta", required = true) String datosEncuesta) throws Exception {
        logger.info("Obteniendo la comunidad si existe en encuesta maestro");
        List<DaDetalleEncuestaAedes> detalleEncuestaAedeses = new ArrayList<DaDetalleEncuestaAedes>();
        //Recuperando Json enviado desde el cliente
        JsonObject jsonpObject = new Gson().fromJson(datosEncuesta,JsonObject.class);
        String strLocalidad = jsonpObject.get("idLocalidad").getAsString();
        String strIdMaestro = jsonpObject.get("idMaestroEncuesta").getAsString();
        //String strMaestro = jsonpObject.get("maestroEncuesta").toString();
        //Obteniendo maestro encuesta
        //String strIdMaestro = null;
        //Obteniendo maestro encuesta
        /*final Gson gson1 = gsonMaestroEncuesta.create();
        List<DaMaeEncuesta> daMaeEncuestaExiste = new ArrayList<DaMaeEncuesta>();
        DaMaeEncuesta daMaeEncuesta = gson1.fromJson(strMaestro,DaMaeEncuesta.class);
        if (daMaeEncuesta!=null) {
            daMaeEncuesta.setCodModeloEncu("TIPOMODENCU|AEDES");
            daMaeEncuestaExiste = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            if (daMaeEncuestaExiste.size() > 0)
                strIdMaestro=daMaeEncuestaExiste.get(0).getEncuestaId();
        }*/
        if (strIdMaestro!=null && !strIdMaestro.isEmpty()) {
            //El maestro ya existe, se obtiene el id
            detalleEncuestaAedeses = detalleEncuestaAedesService.getDetalleEncuestaByLocalidad(strLocalidad, strIdMaestro);
        }
        return detalleEncuestaAedeses;
    }

    /**
     * Método para determinar si ya existe en BD un maestro tipo Aedes Aegypti con la misma información del nuevo maestro a crear
     * @param datosEncuesta
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "existeMaestroEncuestaAedes", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaMaeEncuesta> getMaestroEncuesta(@RequestParam(value = "maestroEncuesta", required = true) String datosEncuesta) throws Exception {
        return  getMaestroEncuesta(datosEncuesta, surveyModelType.AedesAegypti.getDiscriminator());
    }

    /**
     * Método que retorna la lista de detalles agregados para un maestro determinado
     * @param idMaestroEncuesta id del maestro a buscar detalles
     * @return String con la lista (en formato json) de los detalles encontrados
     * @throws Exception
     */
    @RequestMapping(value = "obtenerEncuestasAedesMae", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleEncuestasMaestro(@RequestParam(value = "idMaestroEncuesta", required = true) String idMaestroEncuesta) throws Exception {
        logger.info("Obteniendo encuestas según filtros informados en JSON");
        String result = "";
        List<DaDetalleEncuestaAedes> detEncuestas = new ArrayList<DaDetalleEncuestaAedes>();
        try {
            if (idMaestroEncuesta!=null) {
                detEncuestas = detalleEncuestaAedesService.getDetalleEncuestaByIdMaestro(idMaestroEncuesta);
                final GsonBuilder gson = new GsonBuilder()
                        .registerTypeAdapter(DaDetalleEncuestaAedes.class, new DetalleEncuestaAedesTypeAdapter())
                        .setPrettyPrinting()
                        .enableComplexMapKeySerialization()
                        .serializeNulls()
                        .setDateFormat(DateFormat.LONG)
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setVersion(1.0);

                final Gson gson1 = gson.create();

                result = gson1.toJson(detEncuestas, ArrayList.class);
            }
        }catch (Exception ex){
            logger.error(ex.getStackTrace().toString());
        }
        return result;
    }
    //endregion

    //region REGISTRO LARVARIA AEDES

    /**
     * M Salinas
     * Método que retorna la vista para registrar una encuesta modelo larvaria aedes aegypti
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "create/larvae", method = RequestMethod.GET)
    public ModelAndView initCreationFormLarvaria(HttpServletRequest request) throws Exception {
        logger.debug("Getting data from encuesta Entomologica");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.ValidarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.ValidarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE);
            seguridadService.obtenerUnidadesPorUsuario(1,"","");
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView("");
        if (urlValidacion.isEmpty()) {
            mav.setViewName("encuesta/registrarEncuestaLarvaria");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todos los SILAIS
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                silais = silaisServce.getAllEntidadesAdtvas();
            }else{//sino sólo se cargan los SILAIS autorizados al usuario
                silais = seguridadService.obtenerEntidadesPorUsuario((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            procedencias = catalogosService.getProcedencia();
            ordinales = catalogosService.getOrdinalEncuesta();
            mav.addObject("entidades", silais);
            //mav.addObject("departamentos", departamentos);
            mav.addObject("procedencias",procedencias);
            mav.addObject("ordinales",ordinales);
            mav.addObject("fechaHoy", DateToString(new Date()));
        }else
        {
            mav.setViewName(urlValidacion);
        }
        return  mav;
    }

    /**
     * Método para determinar si ya existe en BD un maestro tipo Larvaria Aedes Aegypti con la misma información del nuevo maestro a crear
     * @param datosEncuesta
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "existeMaestroEncuestaLarva", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaMaeEncuesta> getMaestroEncuestaLarvaria(@RequestParam(value = "maestroEncuesta", required = true) String datosEncuesta) throws Exception {
        return  getMaestroEncuesta(datosEncuesta, surveyModelType.LarvariaAedes.getDiscriminator());
    }

    /**
     * Método utilizado para guardar una encuesta modelo larvaria aedes aegypti
     * @param request Contiene los datos del maestro y del detalle a agregar
     * @param response Contiene String en formato json
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "guardarLarvariaAedes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void guardarLarvariaAedes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String strMaestro = "";
        String strDetalle = "";
        String idMaestro = null;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                json = br.readLine();
            }
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();
            Timestamp fechaRegistro = new Timestamp(new Date().getTime());
            //Obteniendo maestro encuesta
            //List<DaMaeEncuesta> daMaeEncuestaList;
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro,surveyModelType.LarvariaAedes.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            //daMaeEncuestaList = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            //if (daMaeEncuestaList!=null && daMaeEncuestaList.size() > 0){
            //  idMaestro = daMaeEncuestaList.get(0).getEncuestaId();
            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                idMaestro = daMaeEncuesta.getEncuestaId();
            }else {

                daMaeEncuesta.setFechaRegistro(fechaRegistro);
                //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
            }

            if (idMaestro!= null) {
                try {

                    daMaeEncuesta.setEncuestaId(idMaestro);
                    //json = json.replace("00-00-00",idMaestro);
                    //Obteniedo detalle encuesta
                    DaDetalleEncuestaLarvaria daDetalleEncuestaLarvaria = JsonToDetalleEncuestaLarvaria(strDetalle);

                    //Se guada el detalle y se setea el id del maestro recien guardado
                    daDetalleEncuestaLarvaria.setMaeEncuesta(daMaeEncuesta);
                    daDetalleEncuestaLarvaria.setFeRegistro(fechaRegistro);
                    detalleEncuestaLarvariaService.addDaDetalleEncuestaLarvaria(daDetalleEncuestaLarvaria);
                }catch (Exception ex){
                    //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                    if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty())
                        daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                    resultado=messageSource.getMessage("msg.ento.error.adding.detail",null,null);
                    logger.error(ExceptionUtils.getStackTrace(ex));
                    throw new Exception(resultado);
                }
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado = messageSource.getMessage("msg.ento.error.add",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Método que retorna la lista de detalles agregados para un maestro determinado del tipo larvaria
     * @param idMaestroEncuesta id del maestro a buscar detalles
     * @return String con la lista (en formato json) de los detalles encontrados
     * @throws Exception
     */
    @RequestMapping(value = "obtenerEncuestasLarvariasMae", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleEncuestasLarvariaMaestro(@RequestParam(value = "idMaestroEncuesta", required = true) String idMaestroEncuesta) throws Exception {
        logger.info("Obteniendo encuestas según filtros informados en JSON");
        //List<DaDatosDetEncuestaLarvaria> datosEncuestas = new ArrayList<DaDatosDetEncuestaLarvaria>();
        String result = "";
        List<DaDetalleEncuestaLarvaria> detEncuestas = new ArrayList<DaDetalleEncuestaLarvaria>();
        try {
            if (idMaestroEncuesta!=null) {
                detEncuestas = detalleEncuestaLarvariaService.getDetalleEncuestaByIdMaestro(idMaestroEncuesta);
                int num=1;
                final GsonBuilder gson = new GsonBuilder()
                        .registerTypeAdapter(DaDetalleEncuestaLarvaria.class, new DetalleEncuestaLarvariaTypeAdapter())
                        .setPrettyPrinting()
                        .enableComplexMapKeySerialization()
                        .serializeNulls()
                        .setDateFormat(DateFormat.LONG)
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setVersion(1.0);

                final Gson gson1 = gson.create();

                result = gson1.toJson(detEncuestas, ArrayList.class);
            }
        }catch (Exception ex){
            logger.error(ex.getStackTrace().toString());
        }
        return result;
    }

    /**
     * Método para validar si la localidad seleccionado por el usuario ya fue agregada al maestro de la encuesta informada del tipo larvaria
     * @param datosEncuesta Contiene el id de la localidad y el id del maestro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "comunidadExisteEncuestaLarva", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaDetalleEncuestaLarvaria> getComunidadEnEncuestaLarvaria(@RequestParam(value = "datosEncuesta", required = true) String datosEncuesta) throws Exception {
        logger.info("Obteniendo la comunidad si existe en encuesta maestro");
        List<DaDetalleEncuestaLarvaria> detalleEncuestaAedeses = new ArrayList<DaDetalleEncuestaLarvaria>();
        //Recuperando Json enviado desde el cliente
        JsonObject jsonpObject = new Gson().fromJson(datosEncuesta,JsonObject.class);
        String strLocalidad = jsonpObject.get("idLocalidad").getAsString();
        String strIdMaestro = jsonpObject.get("idMaestroEncuesta").getAsString();
        if (strIdMaestro!=null && !strIdMaestro.isEmpty()) {
            //El maestro ya existe, se obtiene el id
            detalleEncuestaAedeses = detalleEncuestaLarvariaService.getDetalleEncuestaByLocalidad(strLocalidad, strIdMaestro);
        }
        return detalleEncuestaAedeses;
    }
    //endregion

    //region REGISTRO DEPOSITO PREFERENCIAL

    /**
     * M Salinas
     * Método que retorna la vista para registrar una encuesta modelo deposito preferecial
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "create/dep", method = RequestMethod.GET)
    public ModelAndView initCreationFormDepositoPrefe(HttpServletRequest request) throws Exception {
        logger.debug("Getting data from encuesta Entomologica");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.ValidarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.ValidarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE);
            seguridadService.obtenerUnidadesPorUsuario(1,"","");
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView("");
        if (urlValidacion.isEmpty()) {
            mav.setViewName("/encuesta/registrarDepositoPreferencial");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todos los SILAIS
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                silais = silaisServce.getAllEntidadesAdtvas();
            }else{//sino sólo se cargan los SILAIS autorizados al usuario
                silais = seguridadService.obtenerEntidadesPorUsuario((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            procedencias = catalogosService.getProcedencia();
            ordinales = catalogosService.getOrdinalEncuesta();
            mav.addObject("entidades", silais);
            mav.addObject("procedencias",procedencias);
            mav.addObject("ordinales",ordinales);
            mav.addObject("fechaHoy", DateToString(new Date()));
        }else
        {
            mav.setViewName(urlValidacion);
        }
        return  mav;
    }

    /**
     * Método para determinar si ya existe en BD un maestro tipo Deposito Preferencial con la misma información del nuevo maestro a crear
     * @param datosEncuesta
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "existeMaestroDepositoPrefe", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaMaeEncuesta> getMaestroDepositoPreferencial(@RequestParam(value = "maestroEncuesta", required = true) String datosEncuesta) throws Exception {
        return  getMaestroEncuesta(datosEncuesta, surveyModelType.DepositoPreferencial.getDiscriminator());
    }

    /**
     * Método utilizado para guardar una encuesta modelo deposito preferencial
     * @param request Contiene los datos del maestro y del detalle a agregar
     * @param response Contiene String en formato json
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "guardarDepositoPreferencial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void guardarDepositoPreferencial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String idMaestro = null;
        String strMaestro = "";
        String strDetalle = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                json = br.readLine();
            }
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();
            Timestamp fechaRegistro = new Timestamp(new Date().getTime());
            //Obteniendo maestro encuesta
            //List<DaMaeEncuesta> daMaeEncuestaList;
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro,surveyModelType.DepositoPreferencial.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            //daMaeEncuestaList = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            //if (daMaeEncuestaList!=null && daMaeEncuestaList.size() > 0){
            //  idMaestro = daMaeEncuestaList.get(0).getEncuestaId();
            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                idMaestro = daMaeEncuesta.getEncuestaId();
            }else {

                daMaeEncuesta.setFechaRegistro(fechaRegistro);
                //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
            }

            if (idMaestro!= null) {
                try {

                    daMaeEncuesta.setEncuestaId(idMaestro);
                    //json = json.replace("00-00-00",idMaestro);
                    //Obteniedo detalle encuesta
                    DaDetaDepositopreferencial detaDepositopreferencial = JsonToDetalleDepositoPreferencial(strDetalle);

                    //Se guada el detalle y se setea el maestro recien guardado
                    detaDepositopreferencial.setMaeEncuesta(daMaeEncuesta);
                    detaDepositopreferencial.setFeRegistro(fechaRegistro);
                    detalleDepositoPreferencialService.addDaDetaDepositopreferencial(detaDepositopreferencial);
                }catch (Exception ex){
                    //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                    if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty())
                        daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                    resultado=messageSource.getMessage("msg.ento.error.adding.detail",null,null);
                    logger.error(ExceptionUtils.getStackTrace(ex));
                    throw new Exception(resultado);
                }
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado = messageSource.getMessage("msg.ento.error.add",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Método que retorna la lista de detalles agregados para un maestro determinado del tipo depósito preferencial
     * @param idMaestroEncuesta id del maestro a buscar detalles
     * @return String con la lista (en formato json) de los detalles encontrados
     * @throws Exception
     */
    @RequestMapping(value = "obtenerDepositoPrefeMae", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleDepositosPrefeMaestro(@RequestParam(value = "idMaestroEncuesta", required = true) String idMaestroEncuesta, @RequestParam(value = "edicion", required = true) int edicion) throws Exception {
        logger.info("Obteniendo encuestas según filtros informados en JSON");
        //List<DaDatosDetDepositopreferencial> datosEncuestas = new ArrayList<DaDatosDetDepositopreferencial>();
        String result = "";
        List<DaDetaDepositopreferencial> detEncuestas = new ArrayList<DaDetaDepositopreferencial>();
        try {
            if (idMaestroEncuesta!=null) {
                detEncuestas = detalleDepositoPreferencialService.getDetalleEncuestaByIdMaestro(idMaestroEncuesta,edicion);
                int num=1;
                final GsonBuilder gson = new GsonBuilder()
                        .registerTypeAdapter(DaDetaDepositopreferencial.class, new DetalleDepositoPreferencialTypeAdapter())
                        .setPrettyPrinting()
                        .enableComplexMapKeySerialization()
                        .serializeNulls()
                        .setDateFormat(DateFormat.LONG)
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setVersion(1.0);

                final Gson gson1 = gson.create();

                result = gson1.toJson(detEncuestas, ArrayList.class);
            }
        }catch (Exception ex){
            logger.error(ex.getStackTrace().toString());
        }
        return result;
    }

    /**
     * Método para validar si la localidad seleccionado por el usuario ya fue agregada al maestro de la encuesta informada del tipo depósito preferencial
     * @param datosEncuesta Contiene el id de la localidad y el id del maestro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "comunidadExisteDepositoPrefe", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaDetaDepositopreferencial> getComunidadEnDepositoPreferencial(@RequestParam(value = "datosEncuesta", required = true) String datosEncuesta) throws Exception {
        logger.info("Obteniendo la comunidad si existe en encuesta maestro");
        List<DaDetaDepositopreferencial> detaDepositopreferencials = new ArrayList<DaDetaDepositopreferencial>();
        //Recuperando Json enviado desde el cliente
        JsonObject jsonpObject = new Gson().fromJson(datosEncuesta,JsonObject.class);
        String strLocalidad = jsonpObject.get("idLocalidad").getAsString();
        String strIdMaestro = jsonpObject.get("idMaestroEncuesta").getAsString();
        //String strMaestro = jsonpObject.get("maestroEncuesta").toString();
        //Obteniendo maestro encuesta
        //String strIdMaestro = null;
        //Obteniendo maestro encuesta
        /*final Gson gson1 = gsonMaestroEncuesta.create();
        List<DaMaeEncuesta> daMaeEncuestaExiste = new ArrayList<DaMaeEncuesta>();
        DaMaeEncuesta daMaeEncuesta = gson1.fromJson(strMaestro,DaMaeEncuesta.class);
        if (daMaeEncuesta!=null) {
            daMaeEncuesta.setCodModeloEncu("TIPOMODENCU|DEPOS");
            daMaeEncuestaExiste = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            if (daMaeEncuestaExiste.size() > 0)
                strIdMaestro=daMaeEncuestaExiste.get(0).getEncuestaId();
        }*/
        if (strIdMaestro!=null && !strIdMaestro.isEmpty()) {
            //El maestro ya existe, se obtiene el id
            detaDepositopreferencials = detalleDepositoPreferencialService.getDetalleEncuestaByLocalidad(strLocalidad, strIdMaestro);
        }
        return detaDepositopreferencials;
    }
    //endregion

    // region BUSQUEDA

    /**
     * Método que inicaliza la pantalla de búsqueda de encuestas
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView initSearchForm(HttpServletRequest request) throws Exception {
        logger.debug("Getting data to search encuesta Entomologica");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.ValidarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.ValidarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE);
            seguridadService.obtenerUnidadesPorUsuario(1,"","");
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView("");
        if (urlValidacion.isEmpty()) {
            List<ModeloEncuesta> modelosEncuesta = new ArrayList<ModeloEncuesta>();
            mav.setViewName("/encuesta/search");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todos los SILAIS
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                silais = silaisServce.getAllEntidadesAdtvas();
            }else{//sino sólo se cargan los SILAIS autorizados al usuario
                silais = seguridadService.obtenerEntidadesPorUsuario((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            modelosEncuesta = catalogosService.getModeloEncuesta(); //.ElementosCatalogos(typeCatalogs.ModeloEncuesta.getDiscriminator());
            mav.addObject("entidades", silais);
            mav.addObject("modelos", modelosEncuesta);
        }else
        {
            mav.setViewName(urlValidacion);
        }
        return  mav;
    }

    /**
     * Método que se ejecuta para realizar búsqueda de encuestas según los filtros proporcionados
     * @param filtrosEncuesta Contiene SILAIS, unidad de salud, modelo, mes y año
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "busquedaEncuesta", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getEncuestasBusqueda(@RequestParam(value = "filtrosEncuesta", required = true) String filtrosEncuesta) throws Exception {
        logger.info("Obteniendo encuestas según filtros informados en JSON");
        String result = "";
        List<DaMaeEncuesta> encuestas;
        try {
            if (filtrosEncuesta!=null) {
                //Recuperando Json enviado desde el cliente
                JsonObject jsonpObject = new Gson().fromJson(filtrosEncuesta, JsonObject.class);
                String strModeloEncu = jsonpObject.get("codModeloEncu").getAsString();
                Integer codSilais = jsonpObject.get("codSilais").getAsInt();
                Integer codUnidadSalud = jsonpObject.get("codUnidadSalud").getAsInt();
                Integer anioEpi = jsonpObject.get("anioEpi").getAsInt();
                Integer mesEpi = jsonpObject.get("mesEpi").getAsInt();

                encuestas = daMaeEncuestaService.searchMaestroEncuestaByFiltros(codSilais, codUnidadSalud, anioEpi, mesEpi, strModeloEncu);
                Divisionpolitica departamento;
                Distritos distrito;
                Areas area;
                Map<String, Object> data  = new HashMap<String, Object>();

                for(int i=0;i < encuestas.size();i++){
                    Map<String, String> map = new HashMap<String, String>();
                    departamento =divisionPoliticaService.getDepartamentoByMunicipi(encuestas.get(i).getMunicipio().getCodigoNacional());
                    distrito = catalogosService.getDistritos(encuestas.get(i).getCodDistrito());
                    area = catalogosService.getAreas(encuestas.get(i).getCodArea());

                    map.put("encuestaId", encuestas.get(i).getEncuestaId());
                    map.put("silais",encuestas.get(i).getEntidadesAdtva().getNombre());
                    map.put("unidadSalud", encuestas.get(i).getUnidadSalud().getNombre());
                    map.put("mesEpi",String.valueOf(encuestas.get(i).getMesEpi()));
                    map.put("anioEpi", String.valueOf(encuestas.get(i).getAnioEpi()));
                    map.put("departamento",departamento!=null?departamento.getNombre():"");
                    map.put("municipio", encuestas.get(i).getMunicipio().getNombre());
                    map.put("distrito",distrito!=null?distrito.getValor():"");
                    map.put("area", area!=null?area.getValor():"");
                    map.put("ordinalEncu",encuestas.get(i).getOrdinalEncuesta().getValor());
                    map.put("procedencia", encuestas.get(i).getProcedencia().getValor());
                    map.put("feInicioEncuesta",DateToString(encuestas.get(i).getFeInicioEncuesta()));
                    map.put("feFinEncuesta", DateToString(encuestas.get(i).getFeFinEncuesta()));
                    map.put("modeloEncu",encuestas.get(i).getModeloEncuesta().getValor());
                    data.put("encu"+String.valueOf(i),map);
                }
                result = new Gson().toJson(data);
          }
        }catch (Exception ex){
            logger.error(ex.getStackTrace().toString());
        }
        return result;
    }
    //endregion

    //region *****************EDITAR*******************

    /**
     * Método que inicializar la pantalla de edición de encuestas y retornar la vista adecuada según el modelo
     * @param idMaestro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView initEditForm(@RequestParam(value = "idMaestro", required = true) String idMaestro, HttpServletRequest request) throws Exception {
        logger.debug("Inicializando para editar encuesta Entomologica");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.ValidarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            //if (urlValidacion.isEmpty())
                //urlValidacion = seguridadService.ValidarAutorizacionUsuario(request, UtilitySecurity.SYSTEM_CODE);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView("");
        if (urlValidacion.isEmpty()) {
            DaMaeEncuesta maestro = daMaeEncuestaService.getMaestroEncuestaById(idMaestro);
            if (maestro.getModeloEncuesta().getCodigo().equalsIgnoreCase(surveyModelType.AedesAegypti.getDiscriminator()))
                mav.setViewName("encuesta/editarEncuestaAedes");
            else if (maestro.getModeloEncuesta().getCodigo().equalsIgnoreCase(surveyModelType.LarvariaAedes.getDiscriminator()))
                mav.setViewName("encuesta/editarEncuestaLarvaria");
            else
                mav.setViewName("encuesta/editarDepositoPreferencial");
            //se obtiene id del usuario autenticado
            long idUsuario = seguridadService.obtenerIdUsuario(request);

            //Si es usuario a nivel central se cargan todos los municipios asociados al SILAIS
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                municipios = divisionPoliticaService.getMunicipiosBySilais(maestro.getEntidadesAdtva().getEntidadAdtvaId());
            }else{
                municipios = seguridadService.obtenerMunicipiosPorUsuarioEntidad((int)idUsuario,maestro.getEntidadesAdtva().getEntidadAdtvaId(), ConstantsSecurity.SYSTEM_CODE);
            }

            //Si es usuario a nivel central se cargan todos los SILAIS
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                silais = silaisServce.getAllEntidadesAdtvas();
            }else{//sino sólo se cargan los SILAIS autorizados al usuario
                silais = seguridadService.obtenerEntidadesPorUsuario((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            if (procedencias == null)
            procedencias = catalogosService.getProcedencia();

            if (ordinales == null)
            ordinales = catalogosService.getOrdinalEncuesta();
            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                unidadesSalud = unidadesService.getPrimaryUnitsByMunicipio_Silais(maestro.getMunicipio().getCodigoNacional(), maestro.getEntidadesAdtva().getCodigo(), HealthUnitType.UnidadesPrimarias.getDiscriminator().split(","));
            }else{ //sino sólo se cargarn las unidades autorizadas para el usuario según SILAIS y municipio
                unidadesSalud = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int)idUsuario ,maestro.getEntidadesAdtva().getCodigo(),maestro.getMunicipio().getCodigoNacional(), ConstantsSecurity.SYSTEM_CODE,HealthUnitType.UnidadesPrimarias.getDiscriminator());
            }
                comunidades = comunidadesService.getComunidades(maestro.getMunicipio().getCodigoNacional());
            if (maestro.getMunicipio().getCodigoNacional().equalsIgnoreCase(COD_NACIONAL_MUNI_MANAGUA)) {
                distritosMng = catalogosService.getDistritos();
                areasMng = catalogosService.getAreas();
            }else{
                distritosMng = null;
                areasMng = null;
            }
            mav.addObject("maestro",maestro);
            mav.addObject("entidades", silais);
            mav.addObject("unidadesSalud",unidadesSalud);
            //mav.addObject("departamentos",departamentos);
            mav.addObject("municipios",municipios);
            mav.addObject("localidades",comunidades);
            mav.addObject("procedencias",procedencias);
            mav.addObject("ordinales",ordinales);
            mav.addObject("fechaInicioEncuesta", DateToString(maestro.getFeInicioEncuesta()));
            mav.addObject("fechaFinEncuesta", DateToString(maestro.getFeFinEncuesta()));
            mav.addObject("fechaHoy", DateToString(new Date()));
            mav.addObject("distritos",distritosMng);
            mav.addObject("mae",new DaMaeEncuesta());
            mav.addObject("areas",areasMng);
        }else
        {
            mav.setViewName(urlValidacion);
        }
        return  mav;
    }

    /**
     * Método para actualizar el maestro de una encuesta
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "actualizarMaestro", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private void updateMaestroEncu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Se procede a la actualización del maestro");
        String json = "";
        String resultado = "";
        String strMaestro = "";
        try {
            boolean existeMaestro=true;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();

            //Obteniendo maestro encuesta
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro,null);
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                daMaeEncuestaService.updateDaMaeEncuesta(daMaeEncuesta);
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado=messageSource.getMessage("msg.ento.error.update",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }
    //endregion

    //region *****************EDITAR AEDES*******************

    /**
     * Método para recuperar el detalle de un una encuesta modelo aedes aegypti
     * @param idDetalleEncu id del detalle a recuperar
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "recuperarDetalleAedes", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleEncuesta(@RequestParam(value = "idDetalleEncu", required = true) String idDetalleEncu) throws Exception {
        String result;
        DaDetalleEncuestaAedes detalleEncuestaAedes;
        detalleEncuestaAedes = detalleEncuestaAedesService.getDetalleEncuestaByIdDetalle(idDetalleEncu);
        final GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(DaDetalleEncuestaAedes.class, new DetalleEncuestaAedesTypeAdapter(sessionFactory))
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setVersion(1.0);

        final Gson gson1 = gson.create();

        result = gson1.toJson(detalleEncuestaAedes);
        return  result;
    }

    /**
     * Método para actualizar en BD una encuesta modelo aedes aegypti
     * @param request contiene los datos del maestro y el detalle a actualizar
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "actualizarAedes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void actualizarEncuestaAedes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String idMaestro = "";
        String strMaestro = "";
        String strDetalle = "";
        try {
           boolean existeMaestro=true;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                json = br.readLine();
            }
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();

            Timestamp fechaRegistro = new Timestamp(new Date().getTime());

            //Obteniendo maestro encuesta

            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro,surveyModelType.AedesAegypti.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                idMaestro = daMaeEncuesta.getEncuestaId();
                daMaeEncuestaService.updateDaMaeEncuesta(daMaeEncuesta);
            }else { //Al editar no debería entrar aca, pero se conserva la funcionalidad de guardar si no existe
                daMaeEncuesta.setFechaRegistro(fechaRegistro);
                //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
                existeMaestro = false;
            }
            if (idMaestro!= null) {
                try {

                    daMaeEncuesta.setEncuestaId(idMaestro);// se setea por si hay error y será necesario eliminarlo
                    //Obteniedo detalle encuesta
                    DaDetalleEncuestaAedes daDetalleEncuestaAedes = JsonToDetalleEncuestaAedes(strDetalle);

                    //Se actualiza el detalle y se setea el id del maestro
                    daDetalleEncuestaAedes.setMaeEncuesta(daMaeEncuesta);
                    if (daDetalleEncuestaAedes.getDetaEncuestaId()!=null && !daDetalleEncuestaAedes.getDetaEncuestaId().isEmpty()) {
                        detalleEncuestaAedesService.updateDaDetalleEncuestaAedes(daDetalleEncuestaAedes);
                    }else{
                        daDetalleEncuestaAedes.setDetaEncuestaId(null);
                        daDetalleEncuestaAedes.setFeRegistro(fechaRegistro);
                        detalleEncuestaAedesService.addDaDetalleEncuestaAedes(daDetalleEncuestaAedes);
                    }
                }catch (Exception ex){
                    //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                    if (!existeMaestro)
                        daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                    logger.error(ex.getMessage(),ex);
                    resultado=messageSource.getMessage("msg.ento.error.saveorupdate.detail",null,null);
                    throw new Exception(resultado);
                }
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado=messageSource.getMessage("msg.ento.error.update",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }
    //endregion

    //region *****************EDITAR LARVARIA***********************

    /**
     * Método para actualizar en BD una encuesta modelo larvaria aedes
     * @param request contiene los datos del maestro y el detalle a actualizar
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "actualizarLarvaria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void actualizarLarvaria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String idMaestro = "";
        String strMaestro = "";
        String strDetalle = "";
        try {
            boolean existeMaestro = true;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                json = br.readLine();
            }
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();
            Timestamp fechaRegistro = new Timestamp(new Date().getTime());
            //Obteniendo maestro encuesta
            //List<DaMaeEncuesta> daMaeEncuestaList;
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro, surveyModelType.LarvariaAedes.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            //daMaeEncuestaList = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            //if (daMaeEncuestaList!=null && daMaeEncuestaList.size() > 0){
            //  idMaestro = daMaeEncuestaList.get(0).getEncuestaId();
            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                idMaestro = daMaeEncuesta.getEncuestaId();
            }else {

                daMaeEncuesta.setFechaRegistro(fechaRegistro);
                //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
                existeMaestro = false;
            }
            if (idMaestro!= null) {
                try {

                    daMaeEncuesta.setEncuestaId(idMaestro);
                    //Obteniedo detalle encuesta
                    DaDetalleEncuestaLarvaria daDetalleEncuestaLarvaria = JsonToDetalleEncuestaLarvaria(strDetalle);

                    //Se actualiza el detalle y se setea el id del maestro
                    daDetalleEncuestaLarvaria.setMaeEncuesta(daMaeEncuesta);

                    if (daDetalleEncuestaLarvaria.getDetaEncuestaId()!=null && !daDetalleEncuestaLarvaria.getDetaEncuestaId().isEmpty()) {
                        detalleEncuestaLarvariaService.updateDaDetalleEncuestaLarvaria(daDetalleEncuestaLarvaria);
                    }else {
                        daDetalleEncuestaLarvaria.setDetaEncuestaId(null);
                        daDetalleEncuestaLarvaria.setFeRegistro(fechaRegistro);
                        detalleEncuestaLarvariaService.addDaDetalleEncuestaLarvaria(daDetalleEncuestaLarvaria);
                    }
                }catch (Exception ex){
                    //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                    if (!existeMaestro)
                        daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                    logger.error(ex.getMessage(),ex);
                    resultado=messageSource.getMessage("msg.ento.error.saveorupdate.detail",null,null);
                    throw new Exception(resultado);
                }
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado=messageSource.getMessage("msg.ento.error.update",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Método para recuperar el detalle de un una encuesta modelo larvaria aedes
     * @param idDetalleEncu id del detalle a recuperar
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "recuperarDetalleLarvaria", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleEncuestaLarvaria(@RequestParam(value = "idDetalleEncu", required = true) String idDetalleEncu) throws Exception {
        String result;
        DaDetalleEncuestaLarvaria detalleEncuestaAedes;
        detalleEncuestaAedes = detalleEncuestaLarvariaService.getDetalleEncuestaByIdDetalle(idDetalleEncu);
        final GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(DaDetalleEncuestaLarvaria.class, new DetalleEncuestaLarvariaTypeAdapter(sessionFactory))
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setVersion(1.0);

        final Gson gson1 = gson.create();

        result = gson1.toJson(detalleEncuestaAedes);
        return  result;
    }
    //endregion

    //region *******************EDITAR DEPOSITO PREFRENCIAL***********************

    /**
     * Método para actualizar en BD una encuesta modelo deposito preferencial
     * @param request contiene los datos del maestro y el detalle a actualizar
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(value = "actualizarDeposito", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void actualizarDepositoPrefe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String idMaestro = "";
        String strMaestro = "";
        String strDetalle = "";
        try {
            boolean existeMaestro = true;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                json = br.readLine();
            }
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strMaestro = jsonpObject.get("maestro").toString();
            strDetalle = jsonpObject.get("detalle").toString();
            Timestamp fechaRegistro = new Timestamp(new Date().getTime());
            //Obteniendo maestro encuesta
            //List<DaMaeEncuesta> daMaeEncuestaList;
            DaMaeEncuesta daMaeEncuesta = JsonToMaestroEncuesta(strMaestro,surveyModelType.DepositoPreferencial.getDiscriminator());
            if (daMaeEncuesta.getCodDistrito()!=null && daMaeEncuesta.getCodDistrito().isEmpty())
                daMaeEncuesta.setCodDistrito(null);
            if (daMaeEncuesta.getCodArea()!=null && daMaeEncuesta.getCodArea().isEmpty())
                daMaeEncuesta.setCodArea(null);

            //daMaeEncuestaList = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(daMaeEncuesta);
            //if (daMaeEncuestaList!=null && daMaeEncuestaList.size() > 0){
            //  idMaestro = daMaeEncuestaList.get(0).getEncuestaId();
            if (daMaeEncuesta.getEncuestaId()!=null && !daMaeEncuesta.getEncuestaId().isEmpty()){
                idMaestro = daMaeEncuesta.getEncuestaId();
            }else {

                daMaeEncuesta.setFechaRegistro(fechaRegistro);
                //Se guarda el maestro y se recupera id generado para el maestro de encuesta
                idMaestro = daMaeEncuestaService.addDaMaeEncuesta(daMaeEncuesta);
                existeMaestro = false;
            }
            if (idMaestro!= null) {
                try {

                    daMaeEncuesta.setEncuestaId(idMaestro);
                    //Obteniedo detalle encuesta
                    DaDetaDepositopreferencial detaDepositopreferencial = JsonToDetalleDepositoPreferencial(strDetalle);

                    //Se actualiza el detalle y se setea el id del maestro
                    detaDepositopreferencial.setMaeEncuesta(daMaeEncuesta);

                    if (detaDepositopreferencial.getDetaEncuestaId()!=null && !detaDepositopreferencial.getDetaEncuestaId().isEmpty()) {
                        detalleDepositoPreferencialService.updateDaDetaDepositopreferencial(detaDepositopreferencial);
                    }else {
                        detaDepositopreferencial.setDetaEncuestaId(null);
                        detaDepositopreferencial.setFeRegistro(fechaRegistro);
                        detalleDepositoPreferencialService.addDaDetaDepositopreferencial(detaDepositopreferencial);
                    }
                }catch (Exception ex){
                    //Si hay error se elimina el maestro, s�lo si no existe, es decir es un maestro nuevo
                    if (!existeMaestro)
                        daMaeEncuestaService.deleteDaMaeEncuesta(daMaeEncuesta);
                    logger.error(ex.getMessage(),ex);
                    resultado=messageSource.getMessage("msg.ento.error.saveorupdate.detail",null,null);
                    throw new Exception(resultado);
                }
            }else
            {
                resultado=messageSource.getMessage("msg.ento.error.get.id",null,null);
                throw new Exception(resultado);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            resultado=messageSource.getMessage("msg.ento.error.update",null,null);
            resultado=resultado+". \n"+ex.getMessage();
        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idMaestro", idMaestro);
            map.put("mensaje",resultado);
            map.put("maestro", strMaestro);
            map.put("detalle", strDetalle);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Método para recuperar el detalle de un una encuesta modelo deposito preferencial
     * @param idDetalleEncu id del detalle a recuperar
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "recuperarDetalleDeposito", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getDetalleReportePrefe(@RequestParam(value = "idDetalleEncu", required = true) String idDetalleEncu) throws Exception {
        String result;
        DaDetaDepositopreferencial depositopreferencial;
        depositopreferencial = detalleDepositoPreferencialService.getDetalleEncuestaByIdDetalle(idDetalleEncu);
        final GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(DaDetaDepositopreferencial.class, new DetalleDepositoPreferencialTypeAdapter(sessionFactory))
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setVersion(1.0);

        final Gson gson1 = gson.create();

        result = gson1.toJson(depositopreferencial);
        return  result;
    }
    //endregion

    //region ******* GENERALES ******

    /**
     * Método para convertir una cadena JSON a DaMaeEncuesta
     * @param strJsonMaestro String con estructura JSON
     * @param tipoModelo El tipo de modelo al que pertenece el maestro, cuando sólo es edición del maestro se toma el tipo existente en BD
     * @return DaMaeEncuesta
     * @throws Exception
     */
    private DaMaeEncuesta JsonToMaestroEncuesta(String strJsonMaestro, String tipoModelo) throws Exception{
        JsonObject jObjectMae = new Gson().fromJson(strJsonMaestro, JsonObject.class);
        String strEncuestaId = jObjectMae.get("encuestaId").getAsString();
        String strCodSilais = jObjectMae.get("codSilais").getAsString();
        String strCodMunicipio = jObjectMae.get("codMunicipio").getAsString();
        String strCodDistrito = jObjectMae.get("codDistrito").getAsString();
        String strCodArea = jObjectMae.get("codArea").getAsString();
        String strCodUnidadSalud = jObjectMae.get("codUnidadSalud").getAsString();
        String strCodProcedencia = jObjectMae.get("codProcedencia").getAsString();
        String strFeInicioEncuesta = jObjectMae.get("feInicioEncuesta").getAsString();
        String strFeFinEncuesta = jObjectMae.get("feFinEncuesta").getAsString();
        String strCodOrdinalEncu = jObjectMae.get("codOrdinalEncu").getAsString();
        //String strCodModeloEncu = jObjectMae.get("codModeloEncu").getAsString();
        String strSemanaEpi = jObjectMae.get("semanaEpi").getAsString();
        String strMesEpi = jObjectMae.get("mesEpi").getAsString();
        String strAnioEpi = jObjectMae.get("anioEpi").getAsString();
        String strUsuarioRegistroId = jObjectMae.get("usuarioRegistroId").getAsString();
        DaMaeEncuesta maeEncuesta = new DaMaeEncuesta();
        EntidadesAdtvas silais = silaisServce.getSilaisByCodigo(Integer.valueOf(strCodSilais));
        Divisionpolitica divisionpolitica = divisionPoliticaService.getDivisionPolitiacaByCodNacional(strCodMunicipio);
        Unidades unidadSalud = unidadesService.getUnidadByCodigo(Integer.valueOf(strCodUnidadSalud));
        Procedencia procedencia = catalogosService.getProcedencia(strCodProcedencia);
        ModeloEncuesta modeloEncuesta = null;
        if (tipoModelo!=null)
            modeloEncuesta = catalogosService.getModeloEncuesta(tipoModelo);
        else
            modeloEncuesta = daMaeEncuestaService.getModeloEncuByIdMaestro(strEncuestaId);
        Ordinal ordinal = catalogosService.getOrdinalEncuesta(strCodOrdinalEncu);
        Usuarios usuario = usuarioService.getUsuarioById(Integer.valueOf(strUsuarioRegistroId));

        maeEncuesta.setEncuestaId(strEncuestaId);
        maeEncuesta.setEntidadesAdtva(silais);
        maeEncuesta.setUnidadSalud(unidadSalud);
        maeEncuesta.setProcedencia(procedencia);
        maeEncuesta.setOrdinalEncuesta(ordinal);
        maeEncuesta.setModeloEncuesta(modeloEncuesta);
        maeEncuesta.setUsuario(usuario);
        maeEncuesta.setMunicipio(divisionpolitica);
        maeEncuesta.setFeInicioEncuesta(StringToDate(strFeInicioEncuesta));
        maeEncuesta.setFeFinEncuesta(StringToDate(strFeFinEncuesta));
        maeEncuesta.setAnioEpi(Integer.valueOf(strAnioEpi));
        maeEncuesta.setMesEpi(Integer.valueOf(strMesEpi));
        maeEncuesta.setSemanaEpi(Integer.valueOf(strSemanaEpi));
        maeEncuesta.setCodDistrito(strCodDistrito);
        maeEncuesta.setCodArea(strCodArea);
        return maeEncuesta;
    }

    /**
     * Método para convertir cadena json a un objeto DaDetalleEncuestaAedes
     * @param strJsonDetalle
     * @return DaDetalleEncuestaAedes
     * @throws Exception
     */
    private DaDetalleEncuestaAedes JsonToDetalleEncuestaAedes(String strJsonDetalle) throws  Exception{
        DaDetalleEncuestaAedes daDetalleEncuestaAedes = new DaDetalleEncuestaAedes();
        JsonObject jObjectDeta = new Gson().fromJson(strJsonDetalle, JsonObject.class);
        if (jObjectDeta.get("detaEncuestaId") != null) {
            String idDetalle = jObjectDeta.get("detaEncuestaId").getAsString();
            daDetalleEncuestaAedes.setDetaEncuestaId(idDetalle);
        }

        String strCodLocalidad = jObjectDeta.get("codLocalidad").getAsString();
        int nViviendaInspeccionada = jObjectDeta.get("viviendaInspeccionada").getAsInt();
        int nViviendaPositiva = jObjectDeta.get("viviendaPositiva").getAsInt();
        int nManzanaInspeccionada = jObjectDeta.get("manzanaInspeccionada").getAsInt();
        int nManzanaPositiva = jObjectDeta.get("manzanaPositiva").getAsInt();
        int nDepositoInspeccionado = jObjectDeta.get("depositoInspeccionado").getAsInt();
        int nDepositoPositivo = jObjectDeta.get("depositoPositivo").getAsInt();
        int nPupaPositiva = jObjectDeta.get("pupaPositiva").getAsInt();
        String strNoAbatizado = jObjectDeta.get("noAbatizado").getAsString();
        String strNoEliminado = jObjectDeta.get("noEliminado").getAsString();
        String strNoNeutralizado = jObjectDeta.get("noNeutralizado").getAsString();
        String strFeAbatizado = jObjectDeta.get("feAbatizado").getAsString();
        String strFeRepot =  jObjectDeta.get("feRepot").getAsString();
        String strFeVEnt = jObjectDeta.get("feVEnt").getAsString();
        String strUsuarioRegistroId = jObjectDeta.get("usuarioRegistroId").getAsString();

        Comunidades localidad = comunidadesService.getComunidad(strCodLocalidad);
        Usuarios usuario = usuarioService.getUsuarioById(Integer.valueOf(strUsuarioRegistroId));

        daDetalleEncuestaAedes.setLocalidad(localidad);
        daDetalleEncuestaAedes.setUsuarioRegistro(usuario);
        daDetalleEncuestaAedes.setViviendaInspeccionada(nViviendaInspeccionada);
        daDetalleEncuestaAedes.setViviendaPositiva(nViviendaPositiva);
        daDetalleEncuestaAedes.setManzanaInspeccionada(nManzanaInspeccionada);
        daDetalleEncuestaAedes.setManzanaPositiva(nManzanaPositiva);
        daDetalleEncuestaAedes.setDepositoInspeccionado(nDepositoInspeccionado);
        daDetalleEncuestaAedes.setDepositoPositivo(nDepositoPositivo);
        daDetalleEncuestaAedes.setPupaPositiva(nPupaPositiva);
        if (strNoAbatizado!=null && !strNoAbatizado.isEmpty())
            daDetalleEncuestaAedes.setNoAbatizado(Integer.valueOf(strNoAbatizado));
        if (strNoEliminado!=null && !strNoEliminado.isEmpty())
            daDetalleEncuestaAedes.setNoEliminado(Integer.valueOf(strNoEliminado));
        if (strNoNeutralizado!=null && !strNoNeutralizado.isEmpty())
            daDetalleEncuestaAedes.setNoNeutralizado(Integer.valueOf(strNoNeutralizado));
        if (strFeAbatizado!=null && !strFeAbatizado.isEmpty())
            daDetalleEncuestaAedes.setFeAbatizado(StringToDate(strFeAbatizado));
        if (strFeRepot!=null && !strFeRepot.isEmpty())
            daDetalleEncuestaAedes.setFeRepot(StringToDate(strFeRepot));
        if (strFeVEnt!=null && !strFeVEnt.isEmpty())
            daDetalleEncuestaAedes.setFeVEnt(StringToDate(strFeVEnt));

        return  daDetalleEncuestaAedes;
    }

    /**
     * Método para convertir cadena json a un objeto DaDetalleEncuestaLarvaria
     * @param strJsonDetalle
     * @return DaDetalleEncuestaLarvaria
     * @throws Exception
     */
    private DaDetalleEncuestaLarvaria JsonToDetalleEncuestaLarvaria(String strJsonDetalle) throws Exception{
        DaDetalleEncuestaLarvaria detalleEncuestaLarvaria = new DaDetalleEncuestaLarvaria();

        JsonObject jObjectDeta = new Gson().fromJson(strJsonDetalle, JsonObject.class);
        if (jObjectDeta.get("detaEncuestaId") != null) {
            String idDetalle = jObjectDeta.get("detaEncuestaId").getAsString();
            detalleEncuestaLarvaria.setDetaEncuestaId(idDetalle);
        }
        String strCodLocalidad = jObjectDeta.get("codLocalidad").getAsString();
        /*int pilaInfestado = jObjectDeta.get("pilaInfestado").getAsInt();
        int llantaInfestado = jObjectDeta.get("llantaInfestado").getAsInt();
        int barrilInfestado = jObjectDeta.get("barrilInfestado").getAsInt();
        int floreroInfestado = jObjectDeta.get("floreroInfestado").getAsInt();
        int bebederoInfestado = jObjectDeta.get("bebederoInfestado").getAsInt();
        int artEspecialInfes = jObjectDeta.get("artEspecialInfes").getAsInt();
        int otrosDepositosInfes = jObjectDeta.get("otrosDepositosInfes").getAsInt();
        int cisterInfestado = jObjectDeta.get("cisterInfestado").getAsInt();
        int inodoroInfestado = jObjectDeta.get("inodoroInfestado").getAsInt();
        int barroInfestado = jObjectDeta.get("barroInfestado").getAsInt();
        int plantaInfestado = jObjectDeta.get("plantaInfestado").getAsInt();
        int arbolInfestado = jObjectDeta.get("arbolInfestado").getAsInt();
        int pozoInfestado =  jObjectDeta.get("pozoInfestado").getAsInt();*/
        int especieAegypti = jObjectDeta.get("especieAegypti").getAsInt();
        int especieAlbopic = jObjectDeta.get("especieAlbopic").getAsInt();
        int especieCulexQuinque = jObjectDeta.get("especieCulexQuinque").getAsInt();
        int especieCulexNigrip = jObjectDeta.get("especieCulexNigrip").getAsInt();
        int especieCulexCoronat = jObjectDeta.get("especieCulexCoronat").getAsInt();
        int especieCulexErratico = jObjectDeta.get("especieCulexErratico").getAsInt();
        int especieCulexTarsalis = jObjectDeta.get("especieCulexTarsalis").getAsInt();
        int especieCulexFatigans = jObjectDeta.get("especieCulexFatigans").getAsInt();
        int especieCulexAlbim = jObjectDeta.get("especieCulexAlbim").getAsInt();
        String strUsuarioRegistroId = jObjectDeta.get("usuarioRegistroId").getAsString();

        Comunidades localidad = comunidadesService.getComunidad(strCodLocalidad);
        Usuarios usuario = usuarioService.getUsuarioById(Integer.valueOf(strUsuarioRegistroId));
        detalleEncuestaLarvaria.setUsuarioRegistro(usuario);
        detalleEncuestaLarvaria.setLocalidad(localidad);

        /*detalleEncuestaLarvaria.setPilaInfestado(pilaInfestado);
        detalleEncuestaLarvaria.setLlantaInfestado(llantaInfestado);
        detalleEncuestaLarvaria.setBarrilInfestado(barrilInfestado);
        detalleEncuestaLarvaria.setFloreroInfestado(floreroInfestado);
        detalleEncuestaLarvaria.setBebederoInfestado(bebederoInfestado);
        detalleEncuestaLarvaria.setArtEspecialInfes(artEspecialInfes);
        detalleEncuestaLarvaria.setOtrosDepositosInfes(otrosDepositosInfes);
        detalleEncuestaLarvaria.setCisterInfestado(cisterInfestado);
        detalleEncuestaLarvaria.setInodoroInfestado(inodoroInfestado);
        detalleEncuestaLarvaria.setBarroInfestado(barroInfestado);
        detalleEncuestaLarvaria.setPlantaInfestado(plantaInfestado);
        detalleEncuestaLarvaria.setArbolInfestado(arbolInfestado);
        detalleEncuestaLarvaria.setPozoInfestado(pozoInfestado);*/
        detalleEncuestaLarvaria.setEspecieAegypti(especieAegypti);
        detalleEncuestaLarvaria.setEspecieAlbopic(especieAlbopic);
        detalleEncuestaLarvaria.setEspecieCulexQuinque(especieCulexQuinque);
        detalleEncuestaLarvaria.setEspecieCulexNigrip(especieCulexNigrip);
        detalleEncuestaLarvaria.setEspecieCulexCoronat(especieCulexCoronat);
        detalleEncuestaLarvaria.setEspecieCulexErratico(especieCulexErratico);
        detalleEncuestaLarvaria.setEspecieCulexTarsalis(especieCulexTarsalis);
        detalleEncuestaLarvaria.setEspecieCulexFatigans(especieCulexFatigans);
        detalleEncuestaLarvaria.setEspecieCulexAlbim(especieCulexAlbim);

        return detalleEncuestaLarvaria;
    }

    /**
     * Método para convertir cadena json a un objeto DaDetaDepositopreferencial
     * @param strJsonDetalle
     * @return DaDetaDepositopreferencial
     * @throws Exception
     */
    private DaDetaDepositopreferencial JsonToDetalleDepositoPreferencial(String strJsonDetalle) throws Exception{
        DaDetaDepositopreferencial detaDepositopreferencial = new DaDetaDepositopreferencial();

        JsonObject jObjectDeta = new Gson().fromJson(strJsonDetalle, JsonObject.class);
        if (jObjectDeta.get("detaEncuestaId") != null) {
            String idDetalle = jObjectDeta.get("detaEncuestaId").getAsString();
            detaDepositopreferencial.setDetaEncuestaId(idDetalle);
        }
        String strCodLocalidad = jObjectDeta.get("codLocalidad").getAsString();
        int pilaInfestado = jObjectDeta.get("pilaInfestado").getAsInt();
        int llantaInfestado = jObjectDeta.get("llantaInfestado").getAsInt();
        int barrilInfestado = jObjectDeta.get("barrilInfestado").getAsInt();
        int floreroInfestado = jObjectDeta.get("floreroInfestado").getAsInt();
        int bebederoInfestado = jObjectDeta.get("bebederoInfestado").getAsInt();
        int artEspecialInfes = jObjectDeta.get("artEspecialInfes").getAsInt();
        int otrosDepositosInfes = jObjectDeta.get("otrosDepositosInfes").getAsInt();
        int cisterInfestado = jObjectDeta.get("cisterInfestado").getAsInt();
        int inodoroInfestado = jObjectDeta.get("inodoroInfestado").getAsInt();
        int barroInfestado = jObjectDeta.get("barroInfestado").getAsInt();
        int plantaInfestado = jObjectDeta.get("plantaInfestado").getAsInt();
        int arbolInfestado = jObjectDeta.get("arbolInfestado").getAsInt();
        int pozoInfestado =  jObjectDeta.get("pozoInfestado").getAsInt();
        int manzana = jObjectDeta.get("manzana").getAsInt();
        int vivienda = jObjectDeta.get("vivienda").getAsInt();
        String nombre = jObjectDeta.get("nombre").getAsString();
        String decripOtroDeposito = jObjectDeta.get("decripOtroDeposito").getAsString();
        String decripcionCister = jObjectDeta.get("decripcionCister").getAsString();
        String strUsuarioRegistroId = jObjectDeta.get("usuarioRegistroId").getAsString();

        Comunidades localidad = comunidadesService.getComunidad(strCodLocalidad);
        Usuarios usuario = usuarioService.getUsuarioById(Integer.valueOf(strUsuarioRegistroId));
        detaDepositopreferencial.setUsuarioRegistro(usuario);
        detaDepositopreferencial.setLocalidad(localidad);

        detaDepositopreferencial.setPilaInfestado(pilaInfestado);
        detaDepositopreferencial.setLlantaInfestado(llantaInfestado);
        detaDepositopreferencial.setBarrilInfestado(barrilInfestado);
        detaDepositopreferencial.setFloreroInfestado(floreroInfestado);
        detaDepositopreferencial.setBebederoInfestado(bebederoInfestado);
        detaDepositopreferencial.setArtEspecialInfes(artEspecialInfes);
        detaDepositopreferencial.setOtrosDepositosInfes(otrosDepositosInfes);
        detaDepositopreferencial.setCisterInfestado(cisterInfestado);
        detaDepositopreferencial.setInodoroInfestado(inodoroInfestado);
        detaDepositopreferencial.setBarroInfestado(barroInfestado);
        detaDepositopreferencial.setPlantaInfestado(plantaInfestado);
        detaDepositopreferencial.setArbolInfestado(arbolInfestado);
        detaDepositopreferencial.setPozoInfestado(pozoInfestado);
        detaDepositopreferencial.setManzana(manzana);
        detaDepositopreferencial.setVivienda(vivienda);
        detaDepositopreferencial.setNombre(nombre);
        detaDepositopreferencial.setDecripOtroDeposito(decripOtroDeposito);
        detaDepositopreferencial.setDecripcionCister(decripcionCister);

        return detaDepositopreferencial;
    }

    /**
     * Método para obtener el maestro de una encuesta, según el modelo
     * @param datosEncuesta Contiene el silais, unidad de salud, departamento, municipio, fecha inicio, fecha fin, ordinal, procedencia, año epidem
     * @param tipoModelo
     * @return
     * @throws Exception
     */
    private List<DaMaeEncuesta> getMaestroEncuesta(String datosEncuesta, String tipoModelo) throws Exception {
        logger.info("Verificando si existe maestro de encuesta");
        List<DaMaeEncuesta> daMaeEncuestaExiste = new ArrayList<DaMaeEncuesta>();
        //Recuperando Json enviado desde el cliente
        JsonObject jsonpObject = new Gson().fromJson(datosEncuesta, JsonObject.class);
        //Obteniendo maestro encuesta
        String strMaestro = jsonpObject.get("maestroEncuesta").toString();
        DaMaeEncuesta maeEncuesta = JsonToMaestroEncuesta(strMaestro,tipoModelo);

        if (maeEncuesta!=null) {
            daMaeEncuestaExiste = daMaeEncuestaService.searchMaestroEncuestaByDaMaeEncuesta(maeEncuesta);
        }
        return daMaeEncuestaExiste;
    }

    //endregion

    //region ****** UTILITARIOS *******

    /**
     * Convierte un string a Date con formato dd/MM/yyyy
     * @param strFecha cadena a convertir
     * @return Fecha
     * @throws java.text.ParseException
     */
    private Date StringToDate(String strFecha) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(strFecha);
    }

    /**
     * Convierte un Date a string con formato dd/MM/yyyy
     * @param dtFecha fecha a convertir
     * @return String
     * @throws java.text.ParseException
     */
    private String DateToString(Date dtFecha) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(dtFecha);
    }

    //endregion
}