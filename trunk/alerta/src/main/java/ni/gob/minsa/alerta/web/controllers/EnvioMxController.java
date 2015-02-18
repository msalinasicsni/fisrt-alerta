package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.typeAdapter.DateUtil;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.text.ParseException;
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
    public ModelAndView initCreateFormTmp(Model model, HttpServletRequest request) throws Exception, ParseException {
        logger.debug("Crear un envio de ordenes de examen");
        String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            mav.setViewName("tomaMx/sendOrders");

            List<EntidadesAdtvas> entidadesAdtvases =  entidadAdmonService.getAllEntidadesAdtvas();
            List<TipoMx> tipoMxList = catalogosService.getTipoMuestra();
            List<Laboratorio> laboratorioList = envioMxService.getLaboratorios();
            mav.addObject("entidades",entidadesAdtvases);
            mav.addObject("tipoMuestra", tipoMxList);
            mav.addObject("laboratorios",laboratorioList);
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    @RequestMapping(value = "orders", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String fetchOrdersJson(@RequestParam(value = "strFilter", required = true) String filtro) throws Exception{
        logger.info("Obteniendo las ordenes de examen pendienetes seg�n filtros en JSON");
        FiltroMx filtroMx = jsonToFiltroSolicitudDx(filtro);
        List<DaTomaMx> tomaMxList = envioMxService.getMxPendientes(filtroMx);
        return tomaMxToJson(tomaMxList);
    }

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
            Laboratorio labProcedencia = envioMxService.getLaboratorio(laboratorioProcedencia);

            DaEnvioMx envioOrden = new DaEnvioMx();

            envioOrden.setUsarioRegistro(usuario);
            envioOrden.setFechaHoraEnvio(new Timestamp(new Date().getTime()));
            envioOrden.setNombreTransporta(nombreTransporta);
            envioOrden.setTemperaturaTermo(temperaturaTermo);
            //envioOrden.setTiempoEspera(CalcularDiferenciaHorasFechas());
            envioOrden.setLaboratorioProcedencia(labProcedencia);

            EstadoMx estadoMx = catalogosService.getEstadoMx("ESTDMX|ENV");

            try {
                idEnvio = envioMxService.addEnvioOrden(envioOrden);
            }catch (Exception ex){
                resultado = messageSource.getMessage("msg.sending.error.add",null,null);
                resultado=resultado+". \n "+ex.getMessage();
                ex.printStackTrace();
            }
            if (!idEnvio.isEmpty()) {
                envioOrden.setIdEnvio(idEnvio);

                JsonObject jObjectOrdenes = new Gson().fromJson(strOrdenes, JsonObject.class);
                for (int i = 0; i < cantOrdenes; i++) {
                    String idSoli = jObjectOrdenes.get(String.valueOf(i)).getAsString();
                    DaTomaMx tomaMxUpd = tomaMxService.getSolicitudDxById(idSoli);
                    tomaMxUpd.setEnvio(envioOrden);
                    tomaMxUpd.setEstadoMx(estadoMx);
                    tomaMxService.updateTomaMx(tomaMxUpd);
                    cantOrdenesProc++;
                }
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

    private String tomaMxToJson(List<DaTomaMx> tomaMxList){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaTomaMx tomaMx:tomaMxList){
            Map<String, String> map = new HashMap<String, String>();
            map.put("idTomaMx",tomaMx.getIdTomaMx());
            //map.put("fechaHoraOrden",DateToString(orden.getFechaHSolicitud(),"dd/MM/yyyy hh:mm:ss a"));
            map.put("fechaTomaMx",DateUtil.DateToString(tomaMx.getFechaHTomaMx(), "dd/MM/yyyy hh:mm:ss a"));
            map.put("estadoMx",tomaMx.getEstadoMx().getValor());
            map.put("codSilais",tomaMx.getIdNotificacion().getCodSilaisAtencion().getNombre());
            map.put("codUnidadSalud",tomaMx.getIdNotificacion().getCodUnidadAtencion().getNombre());
            map.put("separadaMx",(tomaMx.getMxSeparada()!=null?(tomaMx.getMxSeparada()?"Si":"No"):""));
            map.put("tipoMuestra",tomaMx.getCodTipoMx().getNombre());
            //map.put("tipoExamen",orden.getCodDx().getNombre());
            //Si hay fecha de inicio de sintomas se muestra
            Date fechaInicioSintomas = envioMxService.getFechaInicioSintomas(tomaMx.getIdNotificacion().getIdNotificacion());
            if (fechaInicioSintomas!=null)
                map.put("fechaInicioSintomas",DateUtil.DateToString(fechaInicioSintomas, "dd/MM/yyyy"));
            else
                map.put("fechaInicioSintomas"," ");
            //Si hay persona
            if (tomaMx.getIdNotificacion().getPersona()!=null){
                /// se obtiene el nombre de la persona asociada a la ficha
                String nombreCompleto = "";
                nombreCompleto = tomaMx.getIdNotificacion().getPersona().getPrimerNombre();
                if (tomaMx.getIdNotificacion().getPersona().getSegundoNombre()!=null)
                    nombreCompleto = nombreCompleto +" "+ tomaMx.getIdNotificacion().getPersona().getSegundoNombre();
                nombreCompleto = nombreCompleto+" "+tomaMx.getIdNotificacion().getPersona().getPrimerApellido();
                if (tomaMx.getIdNotificacion().getPersona().getSegundoApellido()!=null)
                    nombreCompleto = nombreCompleto +" "+ tomaMx.getIdNotificacion().getPersona().getSegundoApellido();
                map.put("persona",nombreCompleto);
                //Se calcula la edad
                int edad = calcularEdadAnios(tomaMx.getIdNotificacion().getPersona().getFechaNacimiento());
                map.put("edad",String.valueOf(edad));
                //se obtiene el sexo
                map.put("sexo",tomaMx.getIdNotificacion().getPersona().getSexo().getValor());
                if(edad > 12 && tomaMx.getIdNotificacion().getPersona().isSexoFemenino()){
                    map.put("embarazada", envioMxService.estaEmbarazada(tomaMx.getIdNotificacion().getIdNotificacion()));
                }else
                    map.put("embarazada"," ");
            }else{
                map.put("persona"," ");
                map.put("edad"," ");
                map.put("sexo"," ");
                map.put("embarazada"," ");
            }
            //se arma estructura de diagn�sticos
            List<DaSolicitudDx> solicitudDxList = envioMxService.getSolicitudesDxByIdTomaMx(tomaMx.getIdTomaMx());
            Map<Integer, Object> mapDxList = new HashMap<Integer, Object>();
            Map<String, String> mapDx = new HashMap<String, String>();
            int subIndice=0;
            for(DaSolicitudDx solicitudDx: solicitudDxList){
                mapDx.put("nombre",solicitudDx.getCodDx().getNombre());
                mapDx.put("fechaSolicitud", DateUtil.DateToString(solicitudDx.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a"));
                subIndice++;
            }
            mapDxList.put(subIndice,mapDx);
            map.put("diagnosticos", new Gson().toJson(mapDxList));
            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }


    /*private long CalcularDiferenciaHorasFechas(Date fecha1, Date fecha2){
        // Crear 2 instancias de Calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;
        // calcular la diferencia en horas
        long diffHours = diff / (60 * 60 * 1000);
        return diffHours;
    }*/

    public int calcularEdadAnios(Date dFechaNac){
        Calendar today = Calendar.getInstance();
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(dFechaNac);
        int diff_year = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diff_month = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diff_day = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        //Si est� en ese a�o pero todav�a no los ha cumplido
        if( diff_month < 0 || (diff_month==0 && diff_day < 0)){
            diff_year--;
        }
        return diff_year;
    }

    private FiltroMx jsonToFiltroSolicitudDx(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltroMx filtroMx = new FiltroMx();
        String nombreApellido = null;
        Date fechaInicioTomaMx = null;
        Date fechaFinTomaMx = null;
        String codSilais = null;
        String codUnidadSalud = null;
        String codTipoMx = null;

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

        filtroMx.setCodSilais(codSilais);
        filtroMx.setCodUnidadSalud(codUnidadSalud);
        filtroMx.setFechaInicioTomaMx(fechaInicioTomaMx);
        filtroMx.setFechaFinTomaMx(fechaFinTomaMx);
        filtroMx.setNombreApellido(nombreApellido);
        filtroMx.setCodTipoMx(codTipoMx);

        return filtroMx;
    }

}
