package ni.gob.minsa.alerta.web.controllers;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import ni.gob.minsa.alerta.domain.estructura.CalendarioEpi;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.parametros.Parametro;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultado;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import ni.gob.minsa.alerta.utilities.reportes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Miguel Salinas on 8/30/2017.
 * V1.0
 */
@Controller
@RequestMapping("reports")
public class ReportesExcelController {

    private static final Logger logger = LoggerFactory.getLogger(ReportesExcelController.class);
    @Resource(name = "seguridadService")
    private SeguridadService seguridadService;

    @Resource(name = "catalogosService")
    private CatalogoService catalogosService;

    @Resource(name = "entidadAdmonService")
    private EntidadAdmonService entidadAdmonService;

    @Resource(name = "tomaMxService")
    private TomaMxService tomaMxService;

    @Resource(name = "recepcionMxService")
    private RecepcionMxService recepcionMxService;

    @Resource(name = "sindFebrilService")
    private SindFebrilService sindFebrilService;

    @Resource(name = "calendarioEpiService")
    private CalendarioEpiService calendarioEpiService;

    @Resource(name = "organizationChartService")
    private OrganizationChartService organizationChartService;

    @Resource(name = "reportesService")
    private ReportesService reportesService;

    @Resource(name = "resultadoFinalService")
    private ResultadoFinalService resultadoFinalService;

    @Resource(name = "ordenExamenMxService")
    private OrdenExamenMxService ordenExamenMxService;

    @Resource(name = "resultadosService")
    private ResultadosService resultadosService;

    @Resource(name = "laboratoriosService")
    private LaboratoriosService laboratoriosService;

    @Resource(name = "envioMxService")
    private EnvioMxService envioMxService;

    @Resource(name = "daIragService")
    private DaIragService daIragService;

    @Resource(name = "parametrosService")
    private ParametrosService parametrosService;

    @Autowired
    MessageSource messageSource;

    /*******************************************************************/
    /************************ REPORTE POR RESULTADO DX PARA VIGILANCIA ***********************/
    /*******************************************************************/

    @RequestMapping(value = "reportResultDxVig/init", method = RequestMethod.GET)
    public String initReportResultDxVig(Model model,HttpServletRequest request) throws Exception {
        logger.debug("Reporte por Resultado dx enviado a vigilancia");
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
        if (urlValidacion.isEmpty()) {
            List<Laboratorio> laboratorios = null;
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = new ArrayList<EntidadesAdtvas>();
            if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)){
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
                laboratorios = laboratoriosService.getLaboratoriosRegionales();
            }else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                laboratorios = envioMxService.getLaboratorios((int)idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
            List<AreaRep> areas = new ArrayList<AreaRep>();
            areas.add(catalogosService.getAreaRep("AREAREP|PAIS"));
            areas.add(catalogosService.getAreaRep("AREAREP|SILAIS"));
            areas.add(catalogosService.getAreaRep("AREAREP|UNI"));
            List<Catalogo_Dx> catDx = tomaMxService.getCatalogosDx();
            catDx.add(new Catalogo_Dx(0,"VIRUS RESPIRATORIOS COMPLETO"));
            model.addAttribute("laboratorios", laboratorios);
            model.addAttribute("areas", areas);
            model.addAttribute("entidades", entidades);
            model.addAttribute("dxs", catDx);
            return "reportes/resultadoDxVig";
        }else{
            return  urlValidacion;
        }
    }

    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    public ModelAndView downloadExcel(@RequestParam(value = "filtro", required = true) String filtro, HttpServletRequest request) throws Exception{
        // create some sample data
        logger.info("Obteniendo los datos para Reporte por Resultado dx vigilancia ");
        FiltrosReporte filtroRep = jsonToFiltroReportes(filtro);
        String[] idDxsVirusResp = null;
        List<Object[]> registrosPos = new ArrayList<Object[]>();
        List<Object[]> registrosNeg = new ArrayList<Object[]>();
        List<Object[]> registrosMxInadec = new ArrayList<Object[]>();
        List<String> columnas = new ArrayList<String>();
        String nombreDx = "";
        Catalogo_Dx dx = tomaMxService.getDxById(filtroRep.getIdDx().toString());
        if (filtroRep.getIdDx()!=null) {
            if (filtroRep.getIdDx()==0){
                idDxsVirusResp = new String[2];
                Parametro parametro = parametrosService.getParametroByName("ID_DXS_VIRUS_RESPIRATORIOS");
                if (parametro!=null && parametro.getValor()!=null)
                    idDxsVirusResp = parametro.getValor().split(",");
                filtroRep.setIdDx(Integer.valueOf(idDxsVirusResp[0]));
                dx = tomaMxService.getDxById(filtroRep.getIdDx().toString());
                if (dx!=null) nombreDx = dx.getNombre().toUpperCase();
                dx = tomaMxService.getDxById(idDxsVirusResp[1]);
                if (dx!=null) nombreDx = nombreDx + " - "+ dx.getNombre().toUpperCase();
            }
        }
        long idUsuario = seguridadService.obtenerIdUsuario(request);
        List<Laboratorio> laboratorios = null;
        String textoFiltro = "", textoFiltroInd = "", fecha1 = "", fecha2 = "";
        if (filtroRep.getCodLaboratio().equalsIgnoreCase("ALL")) {
            if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                laboratorios = laboratoriosService.getLaboratoriosRegionales();
            } else {
                laboratorios = envioMxService.getLaboratorios((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }
        }else {
            laboratorios = new ArrayList<Laboratorio>();
            laboratorios.add(laboratoriosService.getLaboratorioByCodigo(filtroRep.getCodLaboratio()));
        }
        ModelAndView excelView = new ModelAndView("excelView");
        boolean mostrarTabla1 = true, mostrarTabla2 = true;
        String tipoReporte = "";
        if (dx.getNombre().toLowerCase().contains("dengue")) {
            tipoReporte = "DENGUE";
            setNombreColumnasDengue(columnas);
        } else if (dx.getNombre().toLowerCase().contains("chikun")) {
            tipoReporte = "CHIK";
            setNombreColumnasChik(columnas);
        } else if (dx.getNombre().toLowerCase().contains("zika")) {
            tipoReporte = "ZIKA";
            setNombreColumnasZika(columnas);
        } else if (dx.getNombre().toLowerCase().contains("leptospi")) {
            tipoReporte = "LEPTO";
            setNombreColumnasLepto(columnas);
        }//Mycobacterium Tuberculosis
        else if (dx.getNombre().toLowerCase().contains("mycobacterium") && (dx.getNombre().toLowerCase().contains("tuberculosis") || dx.getNombre().toLowerCase().contains("tb"))) {
            tipoReporte = "XPERT_TB";
            mostrarTabla2 = false;
            setNombreColumnasMycobacTB(columnas);
        }//Cultivo TB
        else if (dx.getNombre().toLowerCase().contains("cultivo") && (dx.getNombre().toLowerCase().contains("tuberculosis") || dx.getNombre().toLowerCase().contains("tb"))) {
            tipoReporte = "CULTIVO_TB";
            mostrarTabla2 = false;
            filtroRep.setIncluirMxInadecuadas(false);
            setNombreColumnasCultivoTB(columnas);
        } else if (dx!=null && dx.getNombre().toLowerCase().contains("ifi virus respiratorio") && idDxsVirusResp == null) {
            tipoReporte = "IFI_VIRUS_RESP";
            setNombreColumnasIFIVR(columnas);
        }else if (dx.getNombre().toLowerCase().contains("molecular virus respiratorio") && idDxsVirusResp == null) {
            tipoReporte = "BIO_MOL_VIRUS_RESP";
            setNombreColumnasBioMolVR(columnas);
        }else if (idDxsVirusResp != null) {
            tipoReporte = "VIRUS_RESPIRATORIOS";
            setNombreColumnasVirusResp(columnas);
        }else if (dx!=null){
            tipoReporte = dx.getNombre().replace(" ", "_");
            setNombreColumnasDefecto(columnas);
        }
        for (Laboratorio lab : laboratorios) {
            filtroRep.setCodLaboratio(lab.getCodigo());
            filtroRep.setIdTomaMx(null);
            List<ResultadoVigilancia> dxList = reportesService.getDiagnosticosAprobadosByFiltroV2(filtroRep);
            if (dx.getNombre().toLowerCase().contains("dengue")) {
                setDatosDengue(dxList, registrosPos, registrosNeg, lab.getCodigo(), filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx.getNombre().toLowerCase().contains("chikun")) {
                setDatosChikungunya(dxList, registrosPos, registrosNeg, lab.getCodigo(), filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx.getNombre().toLowerCase().contains("zika")) {
                setDatosZika(dxList, registrosPos, registrosNeg, lab.getCodigo(), filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx.getNombre().toLowerCase().contains("leptospi")) {
                setDatosLepto(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx.getNombre().toLowerCase().contains("mycobacterium") && (dx.getNombre().toLowerCase().contains("tuberculosis") || dx.getNombre().toLowerCase().contains("tb"))) {
                setDatosXpertTB(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx.getNombre().toLowerCase().contains("cultivo") && (dx.getNombre().toLowerCase().contains("tuberculosis") || dx.getNombre().toLowerCase().contains("tb"))) {
                setDatosCultivoTB(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (dx!=null && dx.getNombre().toLowerCase().contains("ifi virus respiratorio") && idDxsVirusResp == null) {
                setDatosIFIVR(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            }else if (dx.getNombre().toLowerCase().contains("molecular virus respiratorio") && idDxsVirusResp == null) {
                setDatosBioMolVR(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size());
            } else if (idDxsVirusResp != null) {
                filtroRep.setIdDx(Integer.valueOf(idDxsVirusResp[1]));//PONER DX BIOMOL LAB ACTUAL
                List<ResultadoVigilancia> dxListBio = reportesService.getDiagnosticosAprobadosByFiltroV2(filtroRep);
                setDatosVirusResp(dxList, dxListBio, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec, columnas.size(), filtroRep);
                filtroRep.setIdDx(Integer.valueOf(idDxsVirusResp[0]));//VOLVER A PONER DX DE IFI PARA EL PROX LAB
            }else if (dx!=null){
                tipoReporte = dx.getNombre().replace(" ", "_");
                setDatosDefecto(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec);
            }else {
                setDatosDefecto(dxList, registrosPos, registrosNeg, filtroRep.isIncluirMxInadecuadas(), registrosMxInadec);
            }
        }
        excelView.addObject("titulo", messageSource.getMessage("lbl.minsa", null, null));
        excelView.addObject("subtitulo", idDxsVirusResp==null?dx.getNombre().toUpperCase():nombreDx);

        if (filtroRep.getFechaInicio()!=null && filtroRep.getFechaFin()!=null){
            textoFiltro = messageSource.getMessage("lbl.excel.filter", null, null);
            textoFiltroInd = messageSource.getMessage("lbl.excel.filter.mx.inadec", null, null);
            fecha1 = DateUtil.DateToString(filtroRep.getFechaInicio(), "dd/MM/yyyy");
            fecha2 = DateUtil.DateToString(filtroRep.getFechaFin(), "dd/MM/yyyy");
        }else{
            textoFiltro = messageSource.getMessage("lbl.excel.filter.fis", null, null);
            textoFiltroInd = messageSource.getMessage("lbl.excel.filter.mx.inadec.fis", null, null);
            fecha1 = DateUtil.DateToString(filtroRep.getFisInicial(), "dd/MM/yyyy");
            fecha2 = DateUtil.DateToString(filtroRep.getFisFinal(), "dd/MM/yyyy");
        }

        excelView.addObject("tablaPos", String.format(textoFiltro, messageSource.getMessage((tipoReporte.equalsIgnoreCase("LEPTO")?"lbl.reactor":"lbl.positives"), null, null), fecha1, fecha2));

        excelView.addObject("tablaNeg", String.format(textoFiltro, messageSource.getMessage((tipoReporte.equalsIgnoreCase("LEPTO")?"lbl.no.reactor":"lbl.negatives"), null, null), fecha1, fecha2));

        excelView.addObject("tablaMxInadec", String.format(textoFiltroInd, fecha1, fecha2));

        excelView.addObject("columnas", columnas);
        excelView.addObject("tipoReporte", tipoReporte);

        excelView.addObject("listaDxPos", registrosPos);
        excelView.addObject("listaDxNeg", registrosNeg);
        excelView.addObject("listaDxInadec", registrosMxInadec);
        excelView.addObject("incluirMxInadecuadas", filtroRep.isIncluirMxInadecuadas());
        excelView.addObject("mostrarTabla1", mostrarTabla1);
        excelView.addObject("mostrarTabla2", mostrarTabla2);
        excelView.addObject("sinDatos", messageSource.getMessage("lbl.nothing.to.show",null,null));
        return excelView;
    }

    private void setNombreColumnasDengue(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("lbl.parents.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.reception.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.dengue.igm.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.result.pcr", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.serotype", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.week", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm.dengue", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.absorbance", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.mun.res", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("lbl.fill.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.fecnac", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.provenance", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pregnant", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.time.pregnancy", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.hosp", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.admission", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.deceased", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.deceased", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.clinical.dx", null, null));
    }

    private void setNombreColumnasChik(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.address", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.reception.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.result.pcr", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date.long", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.week", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.SILAIS.res", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("person.mun.res", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("lbl.clinical.dx", null, null));
    }

    private void setNombreColumnasZika(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":", ""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("lbl.address", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.reception.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.result.pcr", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pregnant", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.SILAIS.res", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("lbl.ctzica", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.week", null, null).toUpperCase());
    }

    private void setNombreColumnasLepto(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm.lepto", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lepto.igm.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":", ""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.SILAIS.res", null, null).toUpperCase().replace(" ", "_"));
        columnas.add(messageSource.getMessage("lbl.week", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.hosp", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.admission", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.deceased", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.deceased", null, null).toUpperCase());
    }

    private void setNombreColumnasVirusRespiratorios(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.week", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.result.pcr", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
    }

    private void setNombreColumnasMycobacTB(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.receipt.person.name", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":", ""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.population.risk", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.category.patient", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.comorbidities", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.location.infection", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.sample.type1", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.bacilloscopy", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.xpert.tb", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fr.expert.tb", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.observations", null, null).toUpperCase());
    }

    private void setNombreColumnasCultivoTB(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.receipt.person.name", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":", ""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.population.risk", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.category.patient", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.comorbidities", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.location.infection", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.sample.type1", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.bacilloscopy", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.xpert.tb", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fr.expert.tb", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.planting.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.num.tubes", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.num.tubes.con", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.planting", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.res.planting", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lote.lj", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.observations", null, null).toUpperCase());
    }

    private void setNombreColumnasDefecto(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.result.pcr", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.igm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
    }

    private void setNombreColumnasIFIVR(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.a", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.b", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.rsv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.adv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv1", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv2", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv3", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.mpv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.proc", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date.long", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
    }

    private void setNombreColumnasBioMolVR(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.direccion", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a.sub", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b.linaje", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date.long", null, null).toUpperCase());
    }

    private void setNombreColumnasVirusResp(List<String> columnas){
        columnas.add(messageSource.getMessage("lbl.num", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.procesa", null, null));
        columnas.add(messageSource.getMessage("lbl.lab.code.mx", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.names", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.lastnames", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.fecnac", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.age", null, null).toUpperCase().replace(":",""));
        columnas.add(messageSource.getMessage("lbl.age.um", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.sexo", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.address", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.silais", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.muni", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.health.unit.excel", null, null));

        columnas.add(messageSource.getMessage("lbl.capture", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.file.number.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.classification", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.urgent", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.consultation.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.first.consultation.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("person.com.res", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.provenance", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.admission.diagnosis", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.mother.father", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.fis.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ftm", null, null).toUpperCase());
        //ifi
        columnas.add(messageSource.getMessage("lbl.ifi.flu.a", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.b", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.rsv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.adv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv1", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv2", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.piv3", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.ifi.flu.mpv", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.date.proc", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date.long", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        //pcr
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a.sub", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.a.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b.linaje", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.pcr.flu.b.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final.date.long", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.res.final", null, null).toUpperCase());
        //RES_LAB_FINAL	FECHA_EGRESO	ESTUVO UCI	DIAS_UCI	VENTILACION_ASISTIDA	DIAG EGRESO 1	DIAG EGRESO 2	CONDICION_EGRESO	CLASIFICACION_FINAL_CASO
        columnas.add(messageSource.getMessage("lbl.res.final.lab", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.egress.date", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.uci.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.numbers.days.uci", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.assisted.ventilation", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.egress.diagnosis1.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.egress.diagnosis2.short", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.egress.condition", null, null).toUpperCase());
        columnas.add(messageSource.getMessage("lbl.final.case.classification.short", null, null).toUpperCase());
    }

    public Integer getSemanaEpi(Date fechaSemana) throws Exception{
        CalendarioEpi calendario = null;
        if (fechaSemana != null)
            calendario = calendarioEpiService.getCalendarioEpiByFecha(DateUtil.DateToString(fechaSemana, "dd/MM/yyyy"));
        if (calendario != null) {
            return calendario.getNoSemana();
        } else return null;
    }

    private void setDatosDengue(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, String codigoLab, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            DatosDaSindFebril sindFebril = sindFebrilService.getDaSindFebrilV2(solicitudDx.getIdNotificacion());
            Object[] registro = new Object[numColumnas];
            //registro[0]= rowCount;
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            registro[7] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti()://silais en la notif
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[8] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma
            registro[9] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():""));//unidad en la toma
            if (edad!=null && edad<18)
                registro[10] = (sindFebril!=null?sindFebril.getNombPadre():"");
            else
                registro[10] = "";
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[11] = direccion;
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[13] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            DatosRecepcionMx recepcionMx = recepcionMxService.getRecepcionMxByCodUnicoMxV2(solicitudDx.getCodUnicoMx(), codigoLab);
            if (recepcionMx!=null){
                registro[14] = DateUtil.DateToString(recepcionMx.getFechaRecibido()!=null?recepcionMx.getFechaRecibido():recepcionMx.getFechaHoraRecepcion(),"dd/MM/yyyy");
            }

            validarPCRIgMDengue(registro, solicitudDx.getIdSolicitud());
            registro[19] = getSemanaEpi(solicitudDx.getFechaInicioSintomas());

            registro[21] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            registro[23] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[24] = (solicitudDx.getCodigoMuniResid()!=null?solicitudDx.getNombreMuniResid():"");
            registro[25] = (sindFebril!=null?DateUtil.DateToString(sindFebril.getFechaFicha(),"dd/MM/yyyy"):"");
            registro[26] = DateUtil.DateToString(solicitudDx.getFechaNacimiento(),"dd/MM/yyyy");
            String sexo = solicitudDx.getSexo();
            registro[27] = sexo.substring(sexo.length()-1, sexo.length());
            registro[28] = (sindFebril!=null && sindFebril.getCodProcedencia()!=null?sindFebril.getCodProcedencia():"");
            registro[29] = (solicitudDx.getEmbarazada()!=null? solicitudDx.getEmbarazada():"");
            registro[30] = solicitudDx.getSemanasEmbarazo();
            registro[31] = (sindFebril!=null && sindFebril.getHosp()!=null?sindFebril.getHosp():"");
            registro[32] = (sindFebril!=null?DateUtil.DateToString(sindFebril.getFechaIngreso(),"dd/MM/yyyy"):"");
            registro[33] = (sindFebril!=null && sindFebril.getFallecido()!=null?sindFebril.getFallecido():"");
            registro[34] = (sindFebril!=null?DateUtil.DateToString(sindFebril.getFechaFallecido(),"dd/MM/yyyy"):"");
            if (sindFebril!=null && sindFebril.getDxPresuntivo()!=null && !sindFebril.getDxPresuntivo().isEmpty()) {
                registro[35] = sindFebril.getDxPresuntivo();
            } else {
                registro[35] = parseDxs(solicitudDx.getIdTomaMx(), codigoLab);
            }
            if (registro[21].toString().toLowerCase().contains("positivo")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            }else if (registro[21].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            }else if (incluirMxInadecuadas && registro[21].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }
        }
    }

    private void setDatosChikungunya(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, String codigoLab, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";
            DatosDaSindFebril sindFebril = sindFebrilService.getDaSindFebrilV2(solicitudDx.getIdNotificacion());
            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[7] = direccion;
            registro[8] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //SILAIS  en la notifi
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[9] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la notifi
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[10] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
            String sexo = solicitudDx.getSexo();
            registro[11] = sexo.substring(sexo.length()-1, sexo.length());
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[13] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            DatosRecepcionMx recepcionMx = recepcionMxService.getRecepcionMxByCodUnicoMxV2(solicitudDx.getCodUnicoMx(), codigoLab);
            if (recepcionMx!=null){
                registro[14] = DateUtil.DateToString(recepcionMx.getFechaRecibido()!=null?recepcionMx.getFechaRecibido():recepcionMx.getFechaHoraRecepcion(),"dd/MM/yyyy");
            }
            validarPCRIgMChikunZika(registro, solicitudDx.getIdSolicitud(), 15, 16, 17, 18);
            registro[19] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (registro[19].toString().toLowerCase().contains("positivo")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            } else if (registro[19].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            } else if (incluirMxInadecuadas && registro[19].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }
            registro[20] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[21] = getSemanaEpi(solicitudDx.getFechaInicioSintomas());
            registro[22] = (solicitudDx.getCodigoSilaisResid()!=null?solicitudDx.getNombreSilaisResid():"");
            registro[23] = (solicitudDx.getCodigoMuniResid()!=null?solicitudDx.getNombreMuniResid():"");
            if (sindFebril!=null && sindFebril.getDxPresuntivo()!=null && !sindFebril.getDxPresuntivo().isEmpty()) {
                registro[24] = sindFebril.getDxPresuntivo();
            } else {
                registro[24] = parseDxs(solicitudDx.getIdTomaMx(), codigoLab);
            }
        }
    }

    private void setDatosZika(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, String codigoLab, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            registro[7] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[8] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti()://unidad en la notificacion
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[9] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la notificacion
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():""));//unidad en la toma mx
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[10] = direccion;
            registro[11] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            DatosRecepcionMx recepcionMx = recepcionMxService.getRecepcionMxByCodUnicoMxV2(solicitudDx.getCodUnicoMx(), codigoLab);
            if (recepcionMx!=null){
                registro[13] = DateUtil.DateToString(recepcionMx.getFechaRecibido()!=null?recepcionMx.getFechaRecibido():recepcionMx.getFechaHoraRecepcion(),"dd/MM/yyyy");
            }
            validarPCRIgMChikunZika(registro, solicitudDx.getIdSolicitud(), 14, 15, 16, 17);
            registro[18] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            registro[19] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[20] = (solicitudDx.getEmbarazada()!=null? solicitudDx.getEmbarazada():"");
            registro[21] = (solicitudDx.getCodigoSilaisResid()!=null?solicitudDx.getNombreSilaisResid():"");
            registro[22] = "";
            String sexo = solicitudDx.getSexo();
            registro[23] = sexo.substring(sexo.length() - 1, sexo.length());
            registro[24] = getSemanaEpi(solicitudDx.getFechaInicioSintomas());
            //la posición que contiene el resultado final
            if (registro[18].toString().toLowerCase().contains("positivo")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            } else if (registro[18].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            } else if (incluirMxInadecuadas && registro[18].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }
        }
    }

    private void setDatosLepto(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            DatosDaSindFebril sindFebril = sindFebrilService.getDaSindFebrilV2(solicitudDx.getIdNotificacion());
            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();
            registro[3] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            registro[4] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[5] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[6] = apellidos;
            registro[7] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[8] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la notif
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[9] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la notif
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():""));//unidad en la toma mx
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[10] = direccion;
            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[11] = edad;
            registro[12] = medidaEdad;
            String sexo = solicitudDx.getSexo();
            registro[13] = sexo.substring(sexo.length() - 1, sexo.length());
            registro[14] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[15] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            registro[16] = (solicitudDx.getCodigoSilaisResid()!=null?solicitudDx.getNombreSilaisResid():"");
            CalendarioEpi calendario = null;
            if (solicitudDx.getFechaInicioSintomas()!=null)
                calendario = calendarioEpiService.getCalendarioEpiByFecha(DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy"));
            if (calendario!=null) {
                registro[17] = calendario.getNoSemana();
            }
            registro[18] = (sindFebril!=null && sindFebril.getHosp()!=null?sindFebril.getHosp():"");
            registro[19] = (sindFebril!=null?DateUtil.DateToString(sindFebril.getFechaIngreso(),"dd/MM/yyyy"):"");
            registro[20] = (sindFebril!=null && sindFebril.getFallecido()!=null?sindFebril.getFallecido():"");
            registro[21] = (sindFebril!=null?DateUtil.DateToString(sindFebril.getFechaFallecido(),"dd/MM/yyyy"):"");
            //la posición que contiene el resultado final
            if (registro[3].toString().toLowerCase().contains("no reactor") || registro[3].toString().toLowerCase().contains("positivo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            }else if (registro[3].toString().toLowerCase().contains("reactor") || registro[3].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            } else if (incluirMxInadecuadas && registro[3].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }
        }
    }

    private void setDatosXpertTB(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();
            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[3] = nombres + " " + apellidos;
            String sexo = solicitudDx.getSexo();
            registro[4] = sexo.substring(sexo.length() - 1, sexo.length());
            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            registro[7] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[8] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la notif
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():""));//unidad en la toma mx
            registro[13] = solicitudDx.getNombreTipoMx();

            validarPCRTB(registro, solicitudDx.getIdSolicitud(), 16, 15);
            String resSol = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (incluirMxInadecuadas && (resSol.toLowerCase().contains("inadecuada") || (registro[16] != null && registro[16].toString().toLowerCase().contains("inadecuada")))) {
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }else {
                //la posición que contiene el resultado final
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            }
        }
    }

    private void setDatosCultivoTB(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();
            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[3] = nombres + " " + apellidos;
            String sexo = solicitudDx.getSexo();
            registro[4] = sexo.substring(sexo.length() - 1, sexo.length());
            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            registro[7] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //silais en la toma mx
            registro[8] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti()://unidad en la notif
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():""));//unidad en la toma mx
            registro[13] = solicitudDx.getNombreTipoMx();

            validarCultivoTB(registro, solicitudDx.getIdSolicitud());
            String resSol = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (incluirMxInadecuadas && (resSol.toLowerCase().contains("inadecuada") || (registro[20] != null && registro[20].toString().toLowerCase().contains("inadecuada")))) {
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }else {
                //la posición que contiene el resultado final
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            }

            List<DaSolicitudDx> buscarCultivoTb = tomaMxService.getSoliDxAprobByIdToma(solicitudDx.getIdTomaMx());
            for(DaSolicitudDx cultivoDx : buscarCultivoTb){
                if (cultivoDx.getCodDx().getNombre().toLowerCase().contains("mycobacterium") && (cultivoDx.getCodDx().getNombre().toLowerCase().contains("tuberculosis") || cultivoDx.getCodDx().getNombre().toLowerCase().contains("tb"))){
                    validarPCRTB(registro, cultivoDx.getIdSolicitudDx(), 16, 15);
                }
            }
        }
    }

    private void setDatosDefecto(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[17];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[7] = direccion;
            registro[8] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //solais en la toma mx
            registro[9] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[10] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti(): //unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
            String sexo = solicitudDx.getSexo();
            registro[11] = sexo.substring(sexo.length()-1, sexo.length());
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[13] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            validarPCRIgMDefecto(registro, solicitudDx.getIdSolicitud());
            registro[16] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (registro[16].toString().toLowerCase().contains("positivo")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            } else if (registro[16].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            } else if (incluirMxInadecuadas && registro[16].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }
        }
    }

    private void setDatosIFIVR(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[7] = direccion;
            registro[8] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //solais en la toma mx
            registro[9] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[10] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti(): //unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
            String sexo = solicitudDx.getSexo();
            registro[11] = sexo.substring(sexo.length()-1, sexo.length());
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[13] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            validarTipoIFI(registro, solicitudDx.getIdSolicitud(), 14);
            registro[23] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[24] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (registro[24].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            } else if (incluirMxInadecuadas && registro[24].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }else if (!registro[24].toString().toLowerCase().contains("indetermin")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            }
        }
    }

    private void setDatosVirusResp(List<ResultadoVigilancia> dxListIfi, List<ResultadoVigilancia> dxListBio, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas, FiltrosReporte filtroRep) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxListIfi) {
            String nombres = "";
            String apellidos = "";
            final String codigoMx = solicitudDx.getCodigoMx();
            final String codigoUnicoMx = solicitudDx.getCodUnicoMx();
            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = DateUtil.DateToString(solicitudDx.getFechaNacimiento(), "dd/MM/yyyy");
            registro[6] = edad;
            registro[7] = medidaEdad;
            String sexo = solicitudDx.getSexo();
            registro[8] = sexo.substring(sexo.length()-1, sexo.length());

            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[9] = direccion;
            registro[10] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //solais en la toma mx
            registro[11] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[12] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti(): //unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
            DatosDaIrag irag = daIragService.getFormByIdV2(solicitudDx.getIdNotificacion());
            registro[13] = (irag.getCodCaptacion()!=null?irag.getCodCaptacion():"");
            registro[14] = irag.getCodExpediente();
            registro[15] = (irag.getCodClasificacion()!=null?irag.getCodClasificacion():"");
            registro[16] = (solicitudDx.getUrgente()!=null?solicitudDx.getUrgente():"");
            registro[17] = (irag.getFechaConsulta()!=null?DateUtil.DateToString(irag.getFechaConsulta(), "dd/MM/yyyy"):"");
            registro[18] = (irag.getFechaPrimeraConsulta()!=null?DateUtil.DateToString(irag.getFechaPrimeraConsulta(), "dd/MM/yyyy"):"");
            registro[19] = (solicitudDx.getComunidadResidencia()!=null?solicitudDx.getComunidadResidencia():"");
            registro[20] = (irag.getCodProcedencia()!=null?irag.getCodProcedencia():"");
            registro[21] = (irag.getDiagnostico()!=null?irag.getDiagnostico():"");
            registro[22] = irag.getNombreMadreTutor();

            registro[23] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[24] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");

            validarTipoIFI(registro, solicitudDx.getIdSolicitud(), 25);
            registro[34] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[35] = parseFinalResultDetails(solicitudDx.getIdSolicitud());

            //para dx ifi, filtrar si tiene biologia molecular esa muestra
            Predicate<ResultadoVigilancia> byIdOrdenExamen = new Predicate<ResultadoVigilancia>() {
                @Override
                public boolean apply(ResultadoVigilancia bioMolVirusResp) {
                    return (bioMolVirusResp.getCodigoMx()!=null && bioMolVirusResp.getCodigoMx().equalsIgnoreCase(codigoMx)) || (bioMolVirusResp.getCodUnicoMx()!=null && bioMolVirusResp.getCodUnicoMx().equalsIgnoreCase(codigoUnicoMx));
                }
            };
            //si se encuentra la muestra poner agregar datos de bio molecular a la fila
            boolean tieneBioMol = false;
            //filtroRep.setIdTomaMx(solicitudDx.getIdTomaMx());
            //List<ResultadoVigilancia> dxListBio = reportesService.getDiagnosticosAprobadosByFiltroV2(filtroRep);
            Collection<ResultadoVigilancia> resExamen = FilterLists.filter(dxListBio, byIdOrdenExamen);
            if (resExamen.size()>0) {
                tieneBioMol = true;
                for(ResultadoVigilancia dxBm : resExamen){
                    validarPCRVirusResp(registro, dxBm.getIdSolicitud(), 36);
                    registro[42] = DateUtil.DateToString(dxBm.getFechaAprobacion(),"dd/MM/yyyy");
                    registro[43] = parseFinalResultDetails(dxBm.getIdSolicitud());
                }
            }else{
                registro[42] = "";
                registro[43] = "";
            }
            if (dxListBio.size()>0 && resExamen.size()>0)
                dxListBio.removeAll(resExamen);
            registro[45] = (irag.getFechaEgreso()!=null? DateUtil.DateToString(irag.getFechaEgreso(),"dd/MM/yyyy"):"");
            registro[46] = (irag.getUci()!=null?(irag.getUci()==1?messageSource.getMessage("lbl.yes", null, null):messageSource.getMessage("lbl.no", null, null)): "");
            registro[47] = irag.getNoDiasHospitalizado();
            registro[48] = (irag.getVentilacionAsistida()!=null?(irag.getVentilacionAsistida()==1?messageSource.getMessage("lbl.yes", null, null):messageSource.getMessage("lbl.no", null, null)): "");
            registro[49] = irag.getDiagnostico1Egreso();
            registro[50] = irag.getDiagnostico2Egreso();
            registro[51] = irag.getCodCondEgreso();
            registro[52] = irag.getCodClasFCaso();

            if (registro[35].toString().toLowerCase().contains("negativo") && (registro[43].toString().equalsIgnoreCase("") || registro[43].toString().toLowerCase().contains("negativo"))) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
                registro[44] = "Negativo";
            } else if (incluirMxInadecuadas && (registro[35].toString().toLowerCase().contains("inadecuada") || registro[43].toString().toLowerCase().contains("inadecuada"))){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
                registro[44] = "Mx Inadecuada";
            }else if (!registro[35].toString().toLowerCase().contains("indetermin") || !registro[43].toString().toLowerCase().contains("indetermin")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
                if (tieneBioMol)
                    registro[44] = registro[43].toString();
                else {
                    if(!registro[25].toString().equalsIgnoreCase("Positivo"))
                        registro[44] = registro[35].toString();
                    else
                        registro[44] = registro[35].toString() + ", " + messageSource.getMessage("lbl.pcr.flu.a.NS.2", null, null);
                }
            }
        }
        //los que solo tienen PCR
        if (dxListBio.size()>0){
            for (ResultadoVigilancia solicitudDx : dxListBio) {
                String nombres = "";
                String apellidos = "";
                Object[] registro = new Object[numColumnas];
                registro[1] = solicitudDx.getNombreLabProcesa();
                registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

                nombres = solicitudDx.getPrimerNombre();
                if (solicitudDx.getSegundoNombre()!=null)
                    nombres += " "+solicitudDx.getSegundoNombre();
                registro[3] = nombres;

                apellidos = solicitudDx.getPrimerApellido();
                if (solicitudDx.getSegundoApellido()!=null)
                    apellidos += " "+solicitudDx.getSegundoApellido();
                registro[4] = apellidos;

                Integer edad = null;
                String medidaEdad = "";
                String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
                if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                    edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
                }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                    edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
                }else if (arrEdad[2] != null) {
                    edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
                }
                registro[5] = DateUtil.DateToString(solicitudDx.getFechaNacimiento(), "dd/MM/yyyy");
                registro[6] = edad;
                registro[7] = medidaEdad;
                String sexo = solicitudDx.getSexo();
                registro[8] = sexo.substring(sexo.length()-1, sexo.length());

                String direccion = solicitudDx.getDireccionResidencia();
                if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                    direccion += ". TEL. ";
                    direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                    direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
                }
                registro[9] = direccion;
                registro[10] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                        (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //solais en la toma mx
                registro[11] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                        (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
                registro[12] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti(): //unidad en la noti
                        (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
                DatosDaIrag irag = daIragService.getFormByIdV2(solicitudDx.getIdNotificacion());
                registro[13] = (irag.getCodCaptacion()!=null?irag.getCodCaptacion():"");
                registro[14] = irag.getCodExpediente();
                registro[15] = (irag.getCodClasificacion()!=null?irag.getCodClasificacion():"");
                registro[16] = (solicitudDx.getUrgente()!=null?solicitudDx.getUrgente():"");
                registro[17] = (irag.getFechaConsulta()!=null?DateUtil.DateToString(irag.getFechaConsulta(), "dd/MM/yyyy"):"");
                registro[18] = (irag.getFechaPrimeraConsulta()!=null?DateUtil.DateToString(irag.getFechaPrimeraConsulta(), "dd/MM/yyyy"):"");
                registro[19] = (solicitudDx.getComunidadResidencia()!=null?solicitudDx.getComunidadResidencia():"");
                registro[20] = (irag.getCodProcedencia()!=null?irag.getCodProcedencia():"");
                registro[21] = (irag.getDiagnostico()!=null?irag.getDiagnostico():"");
                registro[22] = irag.getNombreMadreTutor();

                registro[23] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
                registro[24] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");

                //validarTipoIFI(registro, solicitudDx.getIdSolicitud(), 25);
                registro[34] = ""; //DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
                registro[35] = ""; //parseFinalResultDetails(solicitudDx.getIdSolicitud());

                validarPCRVirusResp(registro, solicitudDx.getIdSolicitud(), 36);
                registro[42] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
                registro[43] = parseFinalResultDetails(solicitudDx.getIdSolicitud());

                registro[45] = (irag.getFechaEgreso()!=null? DateUtil.DateToString(irag.getFechaEgreso(),"dd/MM/yyyy"):"");
                registro[46] = (irag.getUci()!=null?(irag.getUci()==1?messageSource.getMessage("lbl.yes", null, null):messageSource.getMessage("lbl.no", null, null)): "");
                registro[47] = irag.getNoDiasHospitalizado();
                registro[48] = (irag.getVentilacionAsistida()!=null?(irag.getVentilacionAsistida()==1?messageSource.getMessage("lbl.yes", null, null):messageSource.getMessage("lbl.no", null, null)): "");
                registro[49] = irag.getDiagnostico1Egreso();
                registro[50] = irag.getDiagnostico2Egreso();
                registro[51] = irag.getCodCondEgreso();
                registro[52] = irag.getCodClasFCaso();

                if (registro[43].toString().equalsIgnoreCase("") || registro[43].toString().toLowerCase().contains("negativo")) {
                    registro[0]= rowCountNeg++;
                    registrosNeg.add(registro);
                    registro[44] = "Negativo";
                } else if (incluirMxInadecuadas && (registro[43].toString().toLowerCase().contains("inadecuada"))){
                    registro[0]= rowCountInadec++;
                    registrosMxInadec.add(registro);
                    registro[44] = "Mx Inadecuada";
                }else if (!registro[43].toString().toLowerCase().contains("indetermin")) {
                    registro[0]= rowCountPos++;
                    registrosPos.add(registro);
                    registro[44] = registro[43].toString();
                }
            }
        }
    }

    private void setDatosBioMolVR(List<ResultadoVigilancia> dxList, List<Object[]> registrosPos, List<Object[]> registrosNeg, boolean incluirMxInadecuadas, List<Object[]> registrosMxInadec, int numColumnas) throws Exception{
// create data rows
        int rowCountPos = registrosPos.size()+1;
        int rowCountNeg = registrosNeg.size()+1;
        int rowCountInadec = registrosMxInadec.size()+1;
        for (ResultadoVigilancia solicitudDx : dxList) {
            String nombres = "";
            String apellidos = "";

            Object[] registro = new Object[numColumnas];
            registro[1] = solicitudDx.getNombreLabProcesa();
            registro[2] = solicitudDx.getCodigoMx()!=null?solicitudDx.getCodigoMx():solicitudDx.getCodUnicoMx();

            nombres = solicitudDx.getPrimerNombre();
            if (solicitudDx.getSegundoNombre()!=null)
                nombres += " "+solicitudDx.getSegundoNombre();
            registro[3] = nombres;

            apellidos = solicitudDx.getPrimerApellido();
            if (solicitudDx.getSegundoApellido()!=null)
                apellidos += " "+solicitudDx.getSegundoApellido();
            registro[4] = apellidos;

            Integer edad = null;
            String medidaEdad = "";
            String[] arrEdad = DateUtil.calcularEdad(solicitudDx.getFechaNacimiento(), new Date()).split("/");
            if (arrEdad[0] != null && !arrEdad[0].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[0]); medidaEdad = "A";
            }else if (arrEdad[1] != null && !arrEdad[1].equalsIgnoreCase("0")) {
                edad = Integer.valueOf(arrEdad[1]); medidaEdad = "M";
            }else if (arrEdad[2] != null) {
                edad = Integer.valueOf(arrEdad[2]); medidaEdad = "D";
            }
            registro[5] = edad;
            registro[6] = medidaEdad;
            String direccion = solicitudDx.getDireccionResidencia();
            if (solicitudDx.getTelefonoResidencia()!=null || solicitudDx.getTelefonoMovil()!=null ){
                direccion += ". TEL. ";
                direccion+= (solicitudDx.getTelefonoResidencia()!=null?solicitudDx.getTelefonoResidencia()+",":"");
                direccion+= (solicitudDx.getTelefonoMovil()!=null?solicitudDx.getTelefonoMovil():"");
            }
            registro[7] = direccion;
            registro[8] = (solicitudDx.getCodigoSilaisNoti()!=null?solicitudDx.getNombreSilaisNoti(): //silais en la notificacion
                    (solicitudDx.getCodigoSilaisMx()!=null?solicitudDx.getNombreSilaisMx():"")); //solais en la toma mx
            registro[9] = (solicitudDx.getCodigoMuniNoti()!=null?solicitudDx.getNombreMuniNoti(): //unidad en la noti
                    (solicitudDx.getCodigoMuniMx()!=null?solicitudDx.getNombreMuniMx():"")); //unidad en la toma mx
            registro[10] = (solicitudDx.getCodigoUnidadNoti()!=null?solicitudDx.getNombreUnidadNoti(): //unidad en la noti
                    (solicitudDx.getCodigoUnidadMx()!=null?solicitudDx.getNombreUnidadMx():"")); //unidad en la toma mx
            String sexo = solicitudDx.getSexo();
            registro[11] = sexo.substring(sexo.length()-1, sexo.length());
            registro[12] = DateUtil.DateToString(solicitudDx.getFechaInicioSintomas(),"dd/MM/yyyy");
            registro[13] = DateUtil.DateToString(solicitudDx.getFechaTomaMx(),"dd/MM/yyyy");
            validarPCRVirusResp(registro, solicitudDx.getIdSolicitud(), 14);
            registro[21] = DateUtil.DateToString(solicitudDx.getFechaAprobacion(),"dd/MM/yyyy");
            registro[20] = parseFinalResultDetails(solicitudDx.getIdSolicitud());
            if (registro[20].toString().toLowerCase().contains("negativo")) {
                registro[0]= rowCountNeg++;
                registrosNeg.add(registro);
            } else if (incluirMxInadecuadas && registro[20].toString().toLowerCase().contains("inadecuada")){
                registro[0]= rowCountInadec++;
                registrosMxInadec.add(registro);
            }else if (!registro[20].toString().toLowerCase().contains("indetermin")) {
                registro[0]= rowCountPos++;
                registrosPos.add(registro);
            }
        }
    }

    private void validarTipoIFI(Object[] dato, String idSolicitudDx, int iniciarEn){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        Date fechaProcesamiento = null;
        for (DatosOrdenExamen examen : examenes) {
            List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

            String detalleResultado = "";
            for (ResultadoExamen resultado : resultados) {
                if (resultado.getTipo().equals("TPDATO|LIST")) {
                    Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                    detalleResultado = cat_lista.getEtiqueta();
                } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                    detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                }
                fechaProcesamiento = resultado.getFechahProcesa();
            }
            if (resultados.size() > 0) {
                String nombreEx = examen.getExamen().toUpperCase();
                if (nombreEx.contains("INFLUENZA A") || nombreEx.contains("FLUA")){
                    dato[iniciarEn] = detalleResultado;
                }else if (nombreEx.contains("INFLUENZA B") || nombreEx.contains("FLUB")){
                    dato[iniciarEn+1] = detalleResultado;
                }else if (nombreEx.contains("VIRUS SINCITIAL RESPIRATORIO") || nombreEx.contains("RSV")){
                    dato[iniciarEn+2] = detalleResultado;
                }else if (nombreEx.contains("ADENOVIRUS") || nombreEx.contains("ADV")){
                    dato[iniciarEn+3] = detalleResultado;
                }else if (nombreEx.contains("PIV1")){
                    dato[iniciarEn+4] = detalleResultado;
                }else if (nombreEx.contains("PIV2")){
                    dato[iniciarEn+5] = detalleResultado;
                }else if (nombreEx.contains("PIV3")){
                    dato[iniciarEn+6] = detalleResultado;
                }else if (nombreEx.contains("METAPNEUMOVIRUS") || nombreEx.contains("MPV")){
                    dato[iniciarEn+7] = detalleResultado;
                }
            }
        }
        dato[iniciarEn+8] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
    }

    private void validarPCRVirusResp(Object[] dato, String idSolicitudDx, int iniciarEn){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toUpperCase().contains("FLU A")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());
                Date fechaProcesamiento = null;
                String detalleResultado = "";
                String subtipo = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getRespuesta().toLowerCase().contains("subtipo")){
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        subtipo = cat_lista.getEtiqueta();
                    }else{
                        if (resultado.getTipo().equals("TPDATO|LIST")) {
                            Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                            detalleResultado = cat_lista.getEtiqueta();
                        } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                            detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        }
                    }
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[iniciarEn] = detalleResultado;
                    dato[iniciarEn+1] = subtipo;
                    dato[iniciarEn+2] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }else if (examen.getExamen().toUpperCase().contains("FLU B")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());
                Date fechaProcesamiento = null;
                String detalleResultado = "";
                String linaje = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getRespuesta().toLowerCase().contains("linaje")){
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        linaje = cat_lista.getEtiqueta();
                    }else{
                        if (resultado.getTipo().equals("TPDATO|LIST")) {
                            Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                            detalleResultado = cat_lista.getEtiqueta();
                        } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                            detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        }
                    }
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[iniciarEn+3] = detalleResultado;
                    dato[iniciarEn+4] = linaje;
                    dato[iniciarEn+5] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }
        }
    }

    private void validarPCRTB(Object[] dato, String idSolicitudDx, int indiceRes, int indiceFR){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toUpperCase().contains("XPERT")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                Date fechaProcesamiento = null;
                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {

                    if (resultado.getTipo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        detalleResultado = (detalleResultado.isEmpty()?cat_lista.getEtiqueta():detalleResultado+"/"+cat_lista.getEtiqueta());
                    } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                        String valorSN = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        detalleResultado = (detalleResultado.isEmpty()?valorSN:detalleResultado+"/"+valorSN);
                    }/* else {
                            detalleResultado = resultado.getValor();
                        }*/
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[indiceRes] = detalleResultado;
                    dato[indiceFR] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }
        }
    }

    private void validarCultivoTB(Object[] dato, String idSolicitudDx) throws Exception{

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toLowerCase().contains("cultivo")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                Date fechaProcesamiento = null;
                String fechaSiembra = null;
                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getRespuesta().toLowerCase().contains("fecha") && resultado.getRespuesta().toLowerCase().contains("siembra"))
                    {
                        fechaSiembra = resultado.getValor();
                    }else {

                        if (resultado.getTipo().equals("TPDATO|LIST")) {
                            Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                            detalleResultado = cat_lista.getEtiqueta();
                        } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                            detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        }/* else {
                            detalleResultado = resultado.getValor();
                        }*/
                    }
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[17] = fechaSiembra;
                    dato[20] = detalleResultado;
                    dato[21] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }
        }
    }

    private void validarPCRIgMDengue(Object[] dato, String idSolicitudDx){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toUpperCase().contains("PCR")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                Date fechaProcesamiento = null;
                String detalleResultado = "";
                String serotipo = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getRespuesta().toLowerCase().contains("serotipo")){
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        serotipo = cat_lista.getEtiqueta();
                    }else{
                        if (resultado.getTipo().equals("TPDATO|LIST")) {
                            Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                            detalleResultado = cat_lista.getEtiqueta();
                        } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                            detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        }/* else {
                            detalleResultado = resultado.getValor();
                        }*/
                    }
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[16] = detalleResultado;
                    dato[17] = serotipo;
                    dato[18] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }else if (examen.getExamen().toUpperCase().contains("IGM")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                Date fechaProcesamiento = null;
                String detalleResultado = "";
                String densidad = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getRespuesta().toLowerCase().contains("densidad optica")){
                        densidad = resultado.getValor();
                    }else {
                        if (resultado.getTipo().equals("TPDATO|LIST")) {
                            Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                            detalleResultado = cat_lista.getEtiqueta();
                        } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                            detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                        }/*else {
                        detalleResultado = resultado.getValor();
                        }*/
                    }
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[20] = detalleResultado;
                    dato[15] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                    dato[22] = densidad;
                }
            }
        }
    }

    private void validarPCRIgMChikunZika(Object[] dato, String idSolicitudDx, int indicePCR, int indiceFPCR, int indiceIgm, int indiceFIgm){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toUpperCase().contains("PCR")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                Date fechaProcesamiento = null;
                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {

                    if (resultado.getTipo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        detalleResultado = cat_lista.getEtiqueta();
                    } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                        detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                    }/* else {
                            detalleResultado = resultado.getValor();
                        }*/
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[indicePCR] = detalleResultado;
                    dato[indiceFPCR] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }else if (examen.getExamen().toUpperCase().contains("IGM")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());
                Date fechaProcesamiento = null;
                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getTipo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        detalleResultado = cat_lista.getEtiqueta();
                    } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                        detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                    }/* else {
                        detalleResultado = resultado.getValor();
                    }*/
                    fechaProcesamiento = resultado.getFechahProcesa();
                }
                if (resultados.size() > 0) {
                    dato[indiceIgm] = detalleResultado;
                    dato[indiceFIgm] = DateUtil.DateToString(fechaProcesamiento,"dd/MM/yyyy");
                }
            }
        }
    }

    private void validarPCRIgMDefecto(Object[] dato, String idSolicitudDx){

        List<DatosOrdenExamen> examenes = ordenExamenMxService.getOrdenesExamenByIdSolicitudV2(idSolicitudDx);
        for (DatosOrdenExamen examen : examenes) {
            if (examen.getExamen().toUpperCase().contains("PCR")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {

                    if (resultado.getTipo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        detalleResultado = cat_lista.getEtiqueta();
                    } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                        detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                    }/*else {
                        detalleResultado = resultado.getValor();
                    }*/
                }
                if (resultados.size() > 0) {
                    dato[14] = detalleResultado;
                }
            }else if (examen.getExamen().toUpperCase().contains("IGM")){
                List<ResultadoExamen> resultados = resultadosService.getDetallesResultadoActivosByExamenV2(examen.getIdOrdenExamen());

                String detalleResultado = "";
                for (ResultadoExamen resultado : resultados) {
                    if (resultado.getTipo().equals("TPDATO|LIST")) {
                        Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(resultado.getValor());
                        detalleResultado = cat_lista.getEtiqueta();
                    } else if (resultado.getTipo().equals("TPDATO|LOG")) {
                        detalleResultado = (Boolean.valueOf(resultado.getValor()) ? "lbl.yes" : "lbl.no");
                    } /*else {
                        detalleResultado = resultado.getValor();
                    }*/
                }
                if (resultados.size() > 0) {
                    dato[15] = detalleResultado;
                }
            }
        }
    }

    private String parseDxs(String idTomaMx, String codigoLab){
        List<Solicitud> solicitudDxList = tomaMxService.getSolicitudesDxByIdTomaV2(idTomaMx, codigoLab);
        String dxs = "";
        if (!solicitudDxList.isEmpty()) {
            int cont = 0;
            for (Solicitud solicitudDx : solicitudDxList) {
                cont++;
                if (cont == solicitudDxList.size()) {
                    dxs += solicitudDx.getNombre();
                } else {
                    dxs += solicitudDx.getNombre() + ", ";
                }
            }
        }
        return dxs;
    }

    private String parseFinalResultDetails(String idSolicitud){
        List<ResultadoSolicitud> resFinalList = resultadoFinalService.getDetResActivosBySolicitudV2(idSolicitud);
        String resultados="";
        for(ResultadoSolicitud res: resFinalList){
            if (res.getRespuesta()!=null) {
                //resultados+=(resultados.isEmpty()?res.getRespuesta().getNombre():", "+res.getRespuesta().getNombre());
                if (res.getTipo().equals("TPDATO|LIST")) {
                    Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(res.getValor());
                    resultados+=cat_lista.getEtiqueta();
                }else if (res.getTipo().equals("TPDATO|LOG")) {
                    String valorBoleano = (Boolean.valueOf(res.getValor())?"lbl.yes":"lbl.no");
                    resultados+=valorBoleano;
                } else if (res.getValor().toLowerCase().contains("inadecuada")) {
                    resultados+=res.getValor();
                }
            }else if (res.getRespuestaExamen()!=null){
                //resultados+=(resultados.isEmpty()?res.getRespuestaExamen().getNombre():", "+res.getRespuestaExamen().getNombre());
                if (res.getTipoExamen().equals("TPDATO|LIST")) {
                    Catalogo_Lista cat_lista = resultadoFinalService.getCatalogoLista(res.getValor());
                    resultados+=cat_lista.getEtiqueta();
                } else if (res.getTipoExamen().equals("TPDATO|LOG")) {
                    String valorBoleano = (Boolean.valueOf(res.getValor())?"lbl.yes":"lbl.no");
                    resultados+=valorBoleano;
                }/*else { // no tomar en cuenta respuestas auxiliares
                    resultados+=res.getValor();
                }*/
            }
        }
        return resultados;
    }

    /**
     * Convierte un JSON con los filtros de búsqueda a objeto FiltrosReporte
     * @param strJson filtros
     * @return FiltrosReporte
     * @throws Exception
     */
    private FiltrosReporte jsonToFiltroReportes(String strJson) throws Exception {
        JsonObject jObjectFiltro = new Gson().fromJson(strJson, JsonObject.class);
        FiltrosReporte filtroRep = new FiltrosReporte();
        Date fechaInicio = null;
        Date fechaFin = null;
        Long codSilais = null;
        Long codUnidadSalud = null;
        String tipoNotificacion = null;
        Integer factor= 0;
        Long codDepartamento = null;
        Long codMunicipio = null;
        String codArea = null;
        boolean subunidad = false;
        boolean porSilais = true;//por defecto true
        String codZona = null;
        Integer idDx = null;
        boolean mxInadecuadas = true;
        String codLabo = null;
        Date fisInicial = null;
        Date fisFinal = null;
        String consolidarPor = null;

        if (jObjectFiltro.get("codSilais") != null && !jObjectFiltro.get("codSilais").getAsString().isEmpty())
            codSilais = jObjectFiltro.get("codSilais").getAsLong();
        if (jObjectFiltro.get("codUnidadSalud") != null && !jObjectFiltro.get("codUnidadSalud").getAsString().isEmpty())
            codUnidadSalud = jObjectFiltro.get("codUnidadSalud").getAsLong();
        if (jObjectFiltro.get("tipoNotificacion") != null && !jObjectFiltro.get("tipoNotificacion").getAsString().isEmpty())
            tipoNotificacion = jObjectFiltro.get("tipoNotificacion").getAsString();
        if (jObjectFiltro.get("codFactor") != null && !jObjectFiltro.get("codFactor").getAsString().isEmpty())
            factor = jObjectFiltro.get("codFactor").getAsInt();
        if (jObjectFiltro.get("fechaInicio") != null && !jObjectFiltro.get("fechaInicio").getAsString().isEmpty())
            fechaInicio = DateUtil.StringToDate(jObjectFiltro.get("fechaInicio").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fechaFin") != null && !jObjectFiltro.get("fechaFin").getAsString().isEmpty())
            fechaFin = DateUtil.StringToDate(jObjectFiltro.get("fechaFin").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("codDepartamento") != null && !jObjectFiltro.get("codDepartamento").getAsString().isEmpty())
            codDepartamento = jObjectFiltro.get("codDepartamento").getAsLong();
        if (jObjectFiltro.get("codMunicipio") != null && !jObjectFiltro.get("codMunicipio").getAsString().isEmpty())
            codMunicipio = jObjectFiltro.get("codMunicipio").getAsLong();
        if (jObjectFiltro.get("codArea") != null && !jObjectFiltro.get("codArea").getAsString().isEmpty())
            codArea = jObjectFiltro.get("codArea").getAsString();
        if (jObjectFiltro.get("subunidades") != null && !jObjectFiltro.get("subunidades").getAsString().isEmpty())
            subunidad = jObjectFiltro.get("subunidades").getAsBoolean();
        if (jObjectFiltro.get("porSilais") != null && !jObjectFiltro.get("porSilais").getAsString().isEmpty())
            porSilais = jObjectFiltro.get("porSilais").getAsBoolean();
        if (jObjectFiltro.get("codZona") != null && !jObjectFiltro.get("codZona").getAsString().isEmpty())
            codZona = jObjectFiltro.get("codZona").getAsString();
        if (jObjectFiltro.get("idDx") != null && !jObjectFiltro.get("idDx").getAsString().isEmpty())
            idDx = jObjectFiltro.get("idDx").getAsInt();
        if (jObjectFiltro.get("incluirMxInadecuadas") != null && !jObjectFiltro.get("incluirMxInadecuadas").getAsString().isEmpty())
            mxInadecuadas = jObjectFiltro.get("incluirMxInadecuadas").getAsBoolean();
        if (jObjectFiltro.get("codLabo") != null && !jObjectFiltro.get("codLabo").getAsString().isEmpty())
            codLabo = jObjectFiltro.get("codLabo").getAsString();
        if (jObjectFiltro.get("fisInicial") != null && !jObjectFiltro.get("fisInicial").getAsString().isEmpty())
            fisInicial = DateUtil.StringToDate(jObjectFiltro.get("fisInicial").getAsString() + " 00:00:00");
        if (jObjectFiltro.get("fisFinal") != null && !jObjectFiltro.get("fisFinal").getAsString().isEmpty())
            fisFinal = DateUtil.StringToDate(jObjectFiltro.get("fisFinal").getAsString() + " 23:59:59");
        if (jObjectFiltro.get("consolidarPor") != null && !jObjectFiltro.get("consolidarPor").getAsString().isEmpty())
            consolidarPor = jObjectFiltro.get("consolidarPor").getAsString();

        filtroRep.setSubunidades(subunidad);
        filtroRep.setCodSilais(codSilais);
        filtroRep.setCodUnidad(codUnidadSalud);
        filtroRep.setFechaInicio(fechaInicio);
        filtroRep.setFechaFin(fechaFin);
        filtroRep.setTipoNotificacion(tipoNotificacion);
        filtroRep.setFactor(factor);
        filtroRep.setCodDepartamento(codDepartamento);
        filtroRep.setCodMunicipio(codMunicipio);
        filtroRep.setCodArea(codArea);
        filtroRep.setAnioInicial(DateUtil.DateToString(fechaInicio, "yyyy"));
        filtroRep.setPorSilais(porSilais);
        filtroRep.setCodZona(codZona);
        filtroRep.setIdDx(idDx);
        filtroRep.setIncluirMxInadecuadas(mxInadecuadas);
        filtroRep.setCodLaboratio(codLabo);
        filtroRep.setFisInicial(fisInicial);
        filtroRep.setFisFinal(fisFinal);
        filtroRep.setConsolidarPor(consolidarPor);
        return filtroRep;
    }
}
