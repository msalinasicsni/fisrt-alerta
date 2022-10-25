package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.UtilityProperties;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
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

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
@Controller
@RequestMapping("envioMx")
public class EnvioMxController {
    private static final Logger logger = LoggerFactory.getLogger(TomaMxController.class);

    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Resource(name="tomaMxService")
    private TomaMxService tomaMxService;

    @Autowired
    @Resource(name="envioMxService")
    private EnvioMxService envioMxService;

    @Autowired
    @Qualifier(value = "entidadAdmonService")
    private EntidadAdmonService entidadAdmonService;

    @Autowired
    @Qualifier(value = "usuarioService")
    private UsuarioService usuarioService;

    @Autowired
    @Qualifier(value = "catalogosService")
    private CatalogoService catalogosService;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView initCreateFormTmp(HttpServletRequest request) throws Exception {
        logger.debug("Crear un envio de ordenes de examen");
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
            mav.setViewName("tomaMx/sendOrders");
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidadesAdtvases =  entidadAdmonService.getAllEntidadesAdtvas();
            List<TipoMx> tipoMxList = catalogosService.getTipoMuestra();
            List<Laboratorio> laboratorioList;
            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                laboratorioList = envioMxService.getAllLaboratorios();
            }else {
                 laboratorioList = envioMxService.getLaboratorios((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tipoMuestra", tipoMxList);
            mav.addObject("laboratorios",laboratorioList);
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    /**
     *  Obtiene las tomas de muestra según los filtros de búsqueda especificados
     * @param filtro de búsqueda
     * @return String JSON
     * @throws Exception
     */
    @RequestMapping(value = "orders", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String fetchOrdersJson(@RequestParam(value = "strFilter", required = true) String filtro, HttpServletRequest request) throws Exception{
        logger.info("Obteniendo las ordenes de examen pendientes según filtros en JSON");
        FiltroMx filtroMx = jsonToFiltroMx(filtro);
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<DaTomaMx> tomaMxList = envioMxService.getMxPendientes(filtroMx);
        boolean nivelCentral = seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE);
        List<Unidades> unidadesPermitidas = new ArrayList<Unidades>();
        if (!nivelCentral)
            unidadesPermitidas = seguridadService.obtenerUnidadesPorUsuario((int)idUsuario,ConstantsSecurity.SYSTEM_CODE, HealthUnitType.UnidadesPrimHosp.getDiscriminator());
        return tomaMxToJson(tomaMxList, unidadesPermitidas, nivelCentral);
    }

    /**
     * registra el envio de las muestras.
     * @param request con valores a registrar
     * @param response con el resultado de la acción.
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "agregarEnvioOrdenes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void agregarEnvioOrdenes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String strOrdenes="";
        String idEnvio = "";
        int cantOrdenes=0;
        int cantOrdenesProc = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);
            strOrdenes = jsonpObject.get("ordenes").toString();
            cantOrdenes = jsonpObject.get("cantOrdenes").getAsInt();
            String nombreTransporta = jsonpObject.get("nombreTransporta").getAsString();
            Float temperaturaTermo = jsonpObject.get("temperaturaTermo").getAsFloat();
            String laboratorioProcedencia = jsonpObject.get("laboratorioProcedencia").getAsString();

            long idUsuario = seguridadService.obtenerIdUsuario(request);
            Usuarios usuario = usuarioService.getUsuarioById((int)idUsuario);
            //Se obtiene estado enviado a laboratorio
            Laboratorio labDestino = envioMxService.getLaboratorio(laboratorioProcedencia);

            DaEnvioMx envioOrden = new DaEnvioMx();

            envioOrden.setUsarioRegistro(usuario);
            envioOrden.setFechaHoraEnvio(new Timestamp(new Date().getTime()));
            envioOrden.setNombreTransporta(nombreTransporta);
            envioOrden.setTemperaturaTermo(temperaturaTermo);
            //envioOrden.setTiempoEspera(CalcularDiferenciaHorasFechas());
            envioOrden.setLaboratorioDestino(labDestino);

            EstadoMx estadoMx = catalogosService.getEstadoMx("ESTDMX|ENV");

            try {
                envioMxService.addEnvioOrden(envioOrden);
            }catch (Exception ex){
                resultado = messageSource.getMessage("msg.sending.error.add",null,null);
                resultado=resultado+". \n "+ex.getMessage();
                ex.printStackTrace();
            }
            if (!envioOrden.getIdEnvio().isEmpty()) {

                JsonObject jObjectOrdenes = new Gson().fromJson(strOrdenes, JsonObject.class);
                for (int i = 0; i < cantOrdenes; i++) {
                    String idSoli = jObjectOrdenes.get(String.valueOf(i)).getAsString();
                    DaTomaMx tomaMxUpd = tomaMxService.getTomaMxById(idSoli);
                    //tiene que estar en estado pendiente para ser procesada. 05/08/2022
                    if (tomaMxUpd.getEstadoMx().getCodigo().equalsIgnoreCase("ESTDMX|PEND")) {
                        try {
                            int proce = tomaMxService.updateLabProcesaDxByMx(envioOrden.getLaboratorioDestino().getCodigo(), tomaMxUpd.getIdTomaMx());
                            int tieneEstudio = 0;
                            if (proce == 0) tieneEstudio = envioMxService.tieneEstudios(tomaMxUpd.getIdTomaMx());
                            if (proce > 0 || tieneEstudio > 0)
                                tomaMxService.updateEnvioEnTomaMx(estadoMx.getCodigo(), envioOrden.getIdEnvio(), tomaMxUpd, usuario.getUsername());
                        } catch (Exception ex) {
                            resultado = resultado + ". \n " + ex.getMessage();
                            ex.printStackTrace();
                        }
                        cantOrdenesProc++;
                    }
                }
                idEnvio = envioOrden.getIdEnvio();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            resultado =  messageSource.getMessage("msg.sending.error",null,null);
            resultado=resultado+". \n "+ex.getMessage();

        }finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idEnvio",idEnvio);
            map.put("cantOrdenes",String.valueOf(cantOrdenes));
            map.put("cantOrdenesProc",String.valueOf(cantOrdenesProc));
            map.put("mensaje",resultado);
            map.put("ordenes", strOrdenes);
            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    /**
     * Convierte una lista de tomas de muestra en formato JSON
     * @param tomaMxList lista de muestras
     * @return JSON
     */
    private String tomaMxToJson(List<DaTomaMx> tomaMxList, List<Unidades> unidadesPermitidas, boolean nivelCentral){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaTomaMx tomaMx:tomaMxList){
            if (nivelCentral || unidadesPermitidas.contains(tomaMx.getCodUnidadAtencion())) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("idTomaMx", tomaMx.getIdTomaMx());
                //map.put("fechaHoraOrden",DateToString(orden.getFechaHSolicitud(),"dd/MM/yyyy hh:mm:ss a"));
                map.put("fechaTomaMx", DateUtil.DateToString(tomaMx.getFechaHTomaMx(), "dd/MM/yyyy") +
                        (tomaMx.getHoraTomaMx() != null ? " " + tomaMx.getHoraTomaMx() : ""));
                map.put("estadoMx", tomaMx.getEstadoMx().getValor());
                map.put("codSilais", (tomaMx.getIdNotificacion().getCodSilaisAtencion() != null ? tomaMx.getIdNotificacion().getCodSilaisAtencion().getNombre() : ""));
                map.put("codUnidadSalud", (tomaMx.getIdNotificacion().getCodUnidadAtencion() != null ? tomaMx.getIdNotificacion().getCodUnidadAtencion().getNombre() : ""));
                map.put("separadaMx", (tomaMx.getMxSeparada() != null ? (tomaMx.getMxSeparada() ? "Si" : "No") : ""));
                map.put("tipoMuestra", tomaMx.getCodTipoMx().getNombre());

                if (tomaMx.getIdNotificacion().getUrgente() != null) {
                    map.put("urgente", tomaMx.getIdNotificacion().getUrgente().getValor());
                } else {
                    map.put("urgente", "--");
                }

                //Si hay fecha de inicio de sintomas se muestra
                Date fechaInicioSintomas = tomaMx.getIdNotificacion().getFechaInicioSintomas();//envioMxService.getFechaInicioSintomas(tomaMx.getIdNotificacion().getIdNotificacion());
                if (fechaInicioSintomas != null)
                    map.put("fechaInicioSintomas", DateUtil.DateToString(fechaInicioSintomas, "dd/MM/yyyy"));
                else
                    map.put("fechaInicioSintomas", " ");

                //hospitalizado
                String[] arrayHosp = {"13", "17", "11", "16", "10", "12"};
                boolean hosp = false;

                if (tomaMx.getCodUnidadAtencion() != null) {
                    int h = Arrays.binarySearch(arrayHosp, String.valueOf(tomaMx.getCodUnidadAtencion().getTipoUnidad()));
                    hosp = h > 0;

                }

                if (hosp) {
                    map.put("hospitalizado", messageSource.getMessage("lbl.yes", null, null));
                } else {
                    map.put("hospitalizado", messageSource.getMessage("lbl.no", null, null));
                }


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
                    //Se calcula la edad
                    int edad = DateUtil.calcularEdadAnios(tomaMx.getIdNotificacion().getPersona().getFechaNacimiento());
                    map.put("edad", String.valueOf(edad));
                    //se obtiene el sexo
                    map.put("sexo", tomaMx.getIdNotificacion().getPersona().getSexo().getValor());
                    if (edad > 12 && tomaMx.getIdNotificacion().getPersona().isSexoFemenino()) {
                        map.put("embarazada", envioMxService.estaEmbarazada(tomaMx.getIdNotificacion().getIdNotificacion()));
                    } else
                        map.put("embarazada", "--");
                } else {
                    map.put("persona", " ");
                    map.put("edad", " ");
                    map.put("sexo", " ");
                    map.put("embarazada", "--");
                }
                //se arma estructura de diagnósticos o estudios
                List<DaSolicitudDx> solicitudDxList = envioMxService.getSolicitudesDxByIdTomaMx(tomaMx.getIdTomaMx());
                Map<Integer, Object> mapSolicitudesList = new HashMap<Integer, Object>();
                Map<String, String> mapSolicitud = new HashMap<String, String>();
                if (solicitudDxList.size() > 0) {
                    int subIndice = 0;
                    for (DaSolicitudDx solicitudDx : solicitudDxList) {
                        mapSolicitud.put("nombre", solicitudDx.getCodDx().getNombre());
                        mapSolicitud.put("tipo", "Rutina");
                        mapSolicitud.put("fechaSolicitud", DateUtil.DateToString(solicitudDx.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
                        subIndice++;
                        mapSolicitudesList.put(subIndice, mapSolicitud);
                        mapSolicitud = new HashMap<String, String>();
                    }
                    map.put("solicitudes", new Gson().toJson(mapSolicitudesList));
                } else {
                    List<DaSolicitudEstudio> solicitudEstudios = envioMxService.getSolicitudesEstudioByIdTomaMx(tomaMx.getIdTomaMx());
                    int subIndice = 0;
                    for (DaSolicitudEstudio solicitudEstudio : solicitudEstudios) {
                        mapSolicitud.put("nombre", solicitudEstudio.getTipoEstudio().getNombre());
                        mapSolicitud.put("tipo", "Estudio");
                        mapSolicitud.put("fechaSolicitud", DateUtil.DateToString(solicitudEstudio.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
                        subIndice++;
                        mapSolicitudesList.put(subIndice, mapSolicitud);
                        mapSolicitud = new HashMap<String, String>();
                    }
                    map.put("solicitudes", new Gson().toJson(mapSolicitudesList));
                }
                mapResponse.put(indice, map);
                indice++;
            }
        }

        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }

    /**
     * Convierte String en formato JSON a objeto FiltroMx para realizar búsqueda de tomas de muestra
     * @param strJson JSON
     * @return FiltroMx
     * @throws Exception
     */
    private FiltroMx jsonToFiltroMx(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltroMx filtroMx = new FiltroMx();
        String nombreApellido = null;
        Date fechaInicioTomaMx = null;
        Date fechaFinTomaMx = null;
        String codSilais = null;
        String codUnidadSalud = null;
        String codTipoMx = null;
        String codTipoSolicitud = null;
        String nombreSolicitud = null;

        if (jObjectFiltro.get("nombreApellido") != null && !jObjectFiltro.get("nombreApellido").getAsString().isEmpty())
            nombreApellido = jObjectFiltro.get("nombreApellido").getAsString();
        if (jObjectFiltro.get("fechaInicioTomaMx") != null && !jObjectFiltro.get("fechaInicioTomaMx").getAsString().isEmpty())
            fechaInicioTomaMx = DateUtil.StringToDate(jObjectFiltro.get("fechaInicioTomaMx").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFinTomaMx") != null && !jObjectFiltro.get("fechaFinTomaMx").getAsString().isEmpty())
            fechaFinTomaMx = DateUtil.StringToDate(jObjectFiltro.get("fechaFinTomaMx").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsString();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsString();
        if (jObjectFiltro.get("codTipoMx") != null && !jObjectFiltro.get("codTipoMx").getAsString().isEmpty())
            codTipoMx = jObjectFiltro.get("codTipoMx").getAsString();
        if (jObjectFiltro.get("codTipoSolicitud") != null && !jObjectFiltro.get("codTipoSolicitud").getAsString().isEmpty())
            codTipoSolicitud = jObjectFiltro.get("codTipoSolicitud").getAsString();
        if (jObjectFiltro.get("nombreSolicitud") != null && !jObjectFiltro.get("nombreSolicitud").getAsString().isEmpty())
            nombreSolicitud = jObjectFiltro.get("nombreSolicitud").getAsString();

        filtroMx.setCodSilais(codSilais);
        filtroMx.setCodUnidadSalud(codUnidadSalud);
        filtroMx.setFechaInicioTomaMx(fechaInicioTomaMx);
        filtroMx.setFechaFinTomaMx(fechaFinTomaMx);
        filtroMx.setNombreApellido(nombreApellido);
        filtroMx.setCodTipoMx(codTipoMx);
        filtroMx.setCodTipoSolicitud(codTipoSolicitud);
        filtroMx.setNombreSolicitud(nombreSolicitud);

        return filtroMx;
    }

}
