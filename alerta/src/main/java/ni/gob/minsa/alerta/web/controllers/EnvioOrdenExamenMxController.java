package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.ciportal.dto.InfoResultado;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
@Controller
@RequestMapping("envioOrdenMx")
public class EnvioOrdenExamenMxController {
    private static final Logger logger = LoggerFactory.getLogger(TomaMxController.class);

    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;

    @Autowired
    @Resource(name="tomaMxService")
    private TomaMxService tomaMxService;

    @Autowired
    @Resource(name="envioOrdenExamenMxService")
    private EnvioOrdenExamenMxService envioOrdenExamenMxService;

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

            List<EntidadesAdtvas> entidadesAdtvases =  entidadAdmonService.getAllEntidadesAdtvas();
            List<TipoMx> tipoMxList = catalogosService.getTipoMuestra();
            List<Laboratorio> laboratorioList = envioOrdenExamenMxService.getLaboratorios();
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
        logger.info("Obteniendo las ordenes de examen pendienetes según filtros en JSON");
        FiltroOrdenExamen filtroOrdenExamen= jsonToFiltroOrdenExamen(filtro);
        List<DaOrdenExamen> ordenExamenList = envioOrdenExamenMxService.getOrdenesExamenPendiente(filtroOrdenExamen);
        return OrdenesExamenToJson(ordenExamenList);
    }

    @RequestMapping(value = "agregarEnvioOrdenes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void agregarEnvioOrdenes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        String resultado = "";
        String strOrdenes="";
        String idEnvio = "";
        int cantOrdenes=0;
        int cantOrdenesProc = 0;
        InfoResultado infoResultado;
        boolean hacerRollback = false;
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
            EstadoOrdenEx estadoOrdenEx = catalogosService.getEstadoOrdenEx("ESTORDEN|ENV");
            Laboratorio labProcedencia = envioOrdenExamenMxService.getLaboratorio(laboratorioProcedencia);

            DaEnvioOrden envioOrden = new DaEnvioOrden();

            envioOrden.setUsarioRegistro(usuario);
            envioOrden.setFechaHoraEnvio(new Timestamp(new Date().getTime()));
            envioOrden.setNombreTransporta(nombreTransporta);
            envioOrden.setTemperaturaTermo(temperaturaTermo);
            //envioOrden.setTiempoEspera(CalcularDiferenciaHorasFechas());
            envioOrden.setLaboratorioProcedencia(labProcedencia);

            try {
                idEnvio = envioOrdenExamenMxService.addEnvioOrden(envioOrden);
            }catch (Exception ex){
                resultado = messageSource.getMessage("msg.sending.error.add",null,null);
                resultado=resultado+". \n "+ex.getMessage();
                ex.printStackTrace();
            }
            if (!idEnvio.isEmpty()) {
                envioOrden.setIdEnvioOrden(idEnvio);

                JsonObject jObjectOrdenes = new Gson().fromJson(strOrdenes, JsonObject.class);
                for (int i = 0; i < cantOrdenes; i++) {
                    String idOrden = jObjectOrdenes.get(String.valueOf(i)).getAsString();
                    DaOrdenExamen ordenExamen = tomaMxService.getOrdenExamenById(idOrden);
                    ordenExamen.setEnvio(envioOrden);
                    ordenExamen.setCodEstado(estadoOrdenEx);
                    tomaMxService.updateOrdenExamen(ordenExamen);
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

    private String OrdenesExamenToJson(List<DaOrdenExamen> ordenExamenList){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaOrdenExamen orden:ordenExamenList){
            Map<String, String> map = new HashMap<String, String>();
            map.put("idOrdenExamen",orden.getIdOrdenExamen());
            map.put("idTomaMx",orden.getIdTomaMx().getIdTomaMx());
            map.put("fechaHoraOrden",DateToString(orden.getFechaHOrden(),"dd/MM/yyyy hh:mm:ss a"));
            map.put("fechaTomaMx",DateToString(orden.getIdTomaMx().getFechaHTomaMx(),"dd/MM/yyyy hh:mm:ss a"));
            map.put("codSilais",orden.getIdTomaMx().getIdNotificacion().getCodSilaisAtencion().getNombre());
            map.put("codUnidadSalud",orden.getIdTomaMx().getIdNotificacion().getCodUnidadAtencion().getNombre());
            map.put("estadoOrden",orden.getCodEstado().getValor());
            map.put("separadaMx",(orden.getIdTomaMx().getMxSeparada()!=null?(orden.getIdTomaMx().getMxSeparada()?"Si":"No"):""));
            map.put("tipoMuestra",orden.getIdTomaMx().getCodTipoMx().getNombre());
            map.put("tipoExamen",orden.getCodExamen().getNombre());
            //Si hay fecha de inicio de sintomas se muestra
            Date fechaInicioSintomas = envioOrdenExamenMxService.getFechaInicioSintomas(orden.getIdTomaMx().getIdNotificacion().getIdNotificacion());
            if (fechaInicioSintomas!=null)
                map.put("fechaInicioSintomas",DateToString(fechaInicioSintomas,"dd/MM/yyyy"));
            else
                map.put("fechaInicioSintomas"," ");
            //Si hay persona
            if (orden.getIdTomaMx().getIdNotificacion().getPersona()!=null){
                /// se obtiene el nombre de la persona asociada a la ficha
                String nombreCompleto = "";
                nombreCompleto = orden.getIdTomaMx().getIdNotificacion().getPersona().getPrimerNombre();
                if (orden.getIdTomaMx().getIdNotificacion().getPersona().getSegundoNombre()!=null)
                    nombreCompleto = nombreCompleto +" "+ orden.getIdTomaMx().getIdNotificacion().getPersona().getSegundoNombre();
                nombreCompleto = nombreCompleto+" "+orden.getIdTomaMx().getIdNotificacion().getPersona().getPrimerApellido();
                if (orden.getIdTomaMx().getIdNotificacion().getPersona().getSegundoApellido()!=null)
                    nombreCompleto = nombreCompleto +" "+ orden.getIdTomaMx().getIdNotificacion().getPersona().getSegundoApellido();
                map.put("persona",nombreCompleto);
                //Se calcula la edad
                int edad = calcularEdadAnios(orden.getIdTomaMx().getIdNotificacion().getPersona().getFechaNacimiento());
                map.put("edad",String.valueOf(edad));
                //se obtiene el sexo
                map.put("sexo",orden.getIdTomaMx().getIdNotificacion().getPersona().getSexo().getValor());
                if(edad > 12 && orden.getIdTomaMx().getIdNotificacion().getPersona().isSexoFemenino()){
                    map.put("embarazada",envioOrdenExamenMxService.estaEmbarazada(orden.getIdTomaMx().getIdNotificacion().getIdNotificacion()));
                }else
                    map.put("embarazada"," ");
            }else{
                map.put("persona"," ");
                map.put("edad"," ");
                map.put("sexo"," ");
                map.put("embarazada"," ");
            }

            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        return jsonResponse;
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

        //Si está en ese año pero todavía no los ha cumplido
        if( diff_month < 0 || (diff_month==0 && diff_day < 0)){
            diff_year--;
        }
        return diff_year;
    }

    private FiltroOrdenExamen jsonToFiltroOrdenExamen(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltroOrdenExamen filtroOrdenExamen = new FiltroOrdenExamen();
        String nombreApellido = null;
        Date fechaInicioTomaMx = null;
        Date fechaFinTomaMx = null;
        String codSilais = null;
        String codUnidadSalud = null;
        String codTipoMx = null;

        if (jObjectFiltro.get("nombreApellido") != null && !jObjectFiltro.get("nombreApellido").getAsString().isEmpty())
            nombreApellido = jObjectFiltro.get("nombreApellido").getAsString();
        if (jObjectFiltro.get("fechaInicioTomaMx") != null && !jObjectFiltro.get("fechaInicioTomaMx").getAsString().isEmpty())
            fechaInicioTomaMx = StringToDate(jObjectFiltro.get("fechaInicioTomaMx").getAsString()+" 00:00:00");
        if (jObjectFiltro.get("fechaFinTomaMx") != null && !jObjectFiltro.get("fechaFinTomaMx").getAsString().isEmpty())
            fechaFinTomaMx = StringToDate(jObjectFiltro.get("fechaFinTomaMx").getAsString()+" 23:59:59");
        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsString();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsString();
        if (jObjectFiltro.get("codTipoMx") != null && !jObjectFiltro.get("codTipoMx").getAsString().isEmpty())
            codTipoMx = jObjectFiltro.get("codTipoMx").getAsString();

        filtroOrdenExamen.setCodSilais(codSilais);
        filtroOrdenExamen.setCodUnidadSalud(codUnidadSalud);
        filtroOrdenExamen.setFechaInicioTomaMx(fechaInicioTomaMx);
        filtroOrdenExamen.setFechaFinTomaMx(fechaFinTomaMx);
        filtroOrdenExamen.setNombreApellido(nombreApellido);
        filtroOrdenExamen.setCodTipoMx(codTipoMx);

        return filtroOrdenExamen;
    }

    //region ****** UTILITARIOS *******

    /**
     * Convierte un string a Date con formato dd/MM/yyyy
     * @param strFecha cadena a convertir
     * @return Fecha
     * @throws java.text.ParseException
     */
    private Date StringToDate(String strFecha) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.parse(strFecha);
    }

    private String DateToString(Date dtFecha, String format)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if(dtFecha!=null)
            return simpleDateFormat.format(dtFecha);
        else
            return null;
    }
    //endregion
}
