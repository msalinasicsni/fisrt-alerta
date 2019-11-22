package ni.gob.minsa.alerta.web.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.Cie10;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.*;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.OrdenExamen;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultado;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.alerta.utilities.pdfUtils.BaseTable;
import ni.gob.minsa.alerta.utilities.pdfUtils.Cell;
import ni.gob.minsa.alerta.utilities.pdfUtils.GeneralUtils;
import ni.gob.minsa.alerta.utilities.pdfUtils.Row;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by souyen-ics
 */
@Controller
@RequestMapping("/irag")
public class IragController {

    private static final Logger logger = LoggerFactory.getLogger(IragController.class);
    @Autowired(required = true)
    @Qualifier(value = "sessionFactory")
    public SessionFactory sessionFactory;
    @Autowired(required = true)
    @Qualifier(value = "daIragService")
    public DaIragService daIragService;
    @Autowired(required = true)
    @Qualifier(value = "entidadAdmonService")
    public EntidadAdmonService entidadAdmonService;
    @Autowired(required = true)
    @Qualifier(value = "divisionPoliticaService")
    public DivisionPoliticaService divisionPoliticaService;
    @Autowired(required = true)
    @Qualifier(value = "unidadesService")
    public UnidadesService unidadesService;
    @Autowired(required = true)
    @Qualifier(value = "catalogosService")
    public CatalogoService catalogoService;
    @Autowired(required = true)
    @Qualifier(value = "daVacunasIragService")
    public DaVacunasIragService daVacunasIragService;
    @Autowired(required = true)
    @Qualifier(value = "usuarioService")
    public UsuarioService usuarioService;
    @Resource(name = "personaService")
    private PersonaService personaService;
    @Resource(name = "comunidadesService")
    private ComunidadesService comunidadesService;
    @Autowired
    @Qualifier(value = "seguridadService")
    private SeguridadService seguridadService;
    @Resource(name = "daNotificacionService")
    private DaNotificacionService daNotificacionService;
    @Resource(name = "cie10Service")
    private Cie10Service cie10Service;
    @Resource(name = "resultadosService")
    private ResultadosService resultadosService;
    @Resource(name = "respuestasExamenService")
    private RespuestasExamenService respuestasExamenService;
    @Resource(name = "ordenExamenMxService")
    private OrdenExamenMxService ordenExamenMxService;
    @Resource(name = "resultadoFinalService")
    public ResultadoFinalService resultadoFinalService;

    @Autowired
    MessageSource messageSource;

    List<EntidadesAdtvas> entidades;
    List<Divisionpolitica> departamentos;
    List<Procedencia> catProcedencia;
    List<Clasificacion> catClasif;
    List<Captacion> catCaptac;
    List<Respuesta> catResp;
    List<ViaAntibiotico> catVia;
    List<ResultadoRadiologia> catResRad;
    List<CondicionEgreso> catConEgreso;
    List<ClasificacionFinal> catClaFinal;
    List<Vacuna> catVacunas;
    List<TipoVacuna> catTVacHib;
    List<TipoVacuna> catTVacMenin;
    List<TipoVacuna> catTVacNeumo;
    List<TipoVacuna> catTVacFlu;
    List<CondicionPrevia> catCondPre;
    List<ManifestacionClinica> catManCli;
    List<ClasificacionFinalNB> catClasFNB;
    List<ClasificacionFinalNV> catClasFNV;
    List<Cie10> catCie10Irag;
    Map<String, Object> mapModel;


    void Initialize() throws Exception {
        try {
            departamentos = divisionPoliticaService.getAllDepartamentos();
            catProcedencia = catalogoService.getProcedencia();
            catClasif = catalogoService.getClasificacion();
            catCaptac = catalogoService.getCaptacion();
            catResp = catalogoService.getRespuesta();
            catVia = catalogoService.getViaAntibiotico();
            catResRad = catalogoService.getResultadoRadiologia();
            catConEgreso = catalogoService.getCondicionEgreso();
            catClaFinal = catalogoService.getClasificacionFinal();
            catVacunas = catalogoService.getVacuna();
            catCondPre = catalogoService.getCondicionPrevia();
            catManCli = catalogoService.getManifestacionClinica();
            catTVacHib = catalogoService.getTipoVacunaHib();
            catTVacMenin = catalogoService.getTipoVacunaMeningococica();
            catTVacNeumo = catalogoService.getTipoVacunaNeumococica();
            catTVacFlu = catalogoService.getTipoVacunaFlu();
            catClasFNB = catalogoService.getClasificacionFinalNB();
            catClasFNV = catalogoService.getClasificacionFinalNV();
            catCie10Irag = cie10Service.getCie10Irag("J", true);


            mapModel = new HashMap<>();

            mapModel.put("catProcedencia", catProcedencia);
            mapModel.put("catClasif", catClasif);
            mapModel.put("catCaptac", catCaptac);
            mapModel.put("catResp", catResp);
            mapModel.put("catVia", catVia);
            mapModel.put("catResRad", catResRad);
            mapModel.put("catConEgreso", catConEgreso);
            mapModel.put("catClaFinal", catClaFinal);
            mapModel.put("catVacunas", catVacunas);
            mapModel.put("catTVacHib", catTVacHib);
            mapModel.put("catTVacMenin", catTVacMenin);
            mapModel.put("catTVacNeumo", catTVacNeumo);
            mapModel.put("catTVacFlu", catTVacFlu);
            mapModel.put("catCondPre", catCondPre);
            mapModel.put("catManCli", catManCli);
            mapModel.put("departamentos", departamentos);
            mapModel.put("catNV", catClasFNV);
            mapModel.put("catNB", catClasFNB);
            mapModel.put("catCie10Irag", catCie10Irag);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
        logger.debug("Crear/Buscar una ficha de IRAG/ETI");

        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }

        if (urlValidacion.isEmpty()) {
            return "irag/search";
        } else {
            return urlValidacion;
        }

    }

    /**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        List<DaIrag> results = getResults(idPerson);
        ModelAndView mav = new ModelAndView();

        if (urlValidacion.isEmpty()) {
            long idUsuario = seguridadService.obtenerIdUsuario(request);

            if (results.size() == 0) {
                boolean autorizado = true;
                DaIrag irag = new DaIrag();
                DaNotificacion noti = new DaNotificacion();
                Initialize();
                SisPersona persona = personaService.getPersona(idPerson);

                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                } else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                }

                if (persona != null) {
                    noti.setPersona(persona);
                    irag.setIdNotificacion(noti);
                    Divisionpolitica departamentoProce = null;
                    List<Divisionpolitica> municipiosResi = null;
                    List<Comunidades> comunidades = null;
                    if (irag.getIdNotificacion().getPersona().getMunicipioResidencia()!=null) {
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                        municipiosResi = departamentoProce != null ? divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional()) : null;
                        comunidades = comunidadesService.getComunidades(irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                    }
                    mav.addObject("entidades", entidades);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("departamentoProce", departamentoProce);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("irag", irag);
                    mav.addObject("fVacuna", new DaVacunasIrag());
                    mav.addAllObjects(mapModel);
                    mav.setViewName("irag/create");
                } else {
                    mav.setViewName("404");
                }
            } else {
                List<String> iragAutorizados = new ArrayList<String>();
                boolean fichaincompleta=false;
                for (DaIrag ira : results) {
                    if (idUsuario != 0) {
                        if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)){
                            iragAutorizados.add(ira.getIdNotificacion().getIdNotificacion());
                        }else {
                            if (ira.getIdNotificacion().getCodSilaisAtencion() == null && ira.getIdNotificacion().getCodUnidadAtencion() == null) {
                                iragAutorizados.add(ira.getIdNotificacion().getIdNotificacion());
                            } else if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, ira.getIdNotificacion().getCodSilaisAtencion().getCodigo()) || seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, ira.getIdNotificacion().getCodUnidadAtencion().getCodigo())) {
                                iragAutorizados.add(ira.getIdNotificacion().getIdNotificacion());
                            }
                        }
                        if (!ira.getIdNotificacion().isPasivo() && !fichaincompleta) {
                            fichaincompleta = !ira.getIdNotificacion().isCompleta();
                        }
                    }
                }
                mav.addObject("records", results);
                mav.addObject("idPerson", idPerson);
                mav.addObject("incompleta", fichaincompleta);
                mav.addObject("iragAutorizadas", iragAutorizados);
                mav.setViewName("irag/results");
            }
        } else {
            mav.setViewName(urlValidacion);
        }
        return mav;
    }

    //Load results list
    @RequestMapping(value = "getResults", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DaIrag> getResults(@RequestParam(value = "idPerson", required = true) long idPerson) throws Exception {
        logger.info("Obteniendo los resultados de la b�squeda");
        List<DaIrag> results = null;
        results = daIragService.getDaIragPersona(idPerson);
        return results;
    }


    /**
     * Custom handler to create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("new/{idPerson}")
    public ModelAndView newIrag(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {
            boolean autorizado = true;
            DaIrag irag = new DaIrag();
            DaNotificacion noti = new DaNotificacion();
            Initialize();
            SisPersona persona = personaService.getPersona(idPerson);

            long idUsuario = seguridadService.obtenerIdUsuario(request);
            //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
            if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                entidades = entidadAdmonService.getAllEntidadesAdtvas();
            } else {
                entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
            }

            if (persona != null) {
                noti.setPersona(persona);
                irag.setIdNotificacion(noti);
                Divisionpolitica departamentoProce = null;
                List<Divisionpolitica> municipiosResi = null;
                List<Comunidades> comunidades = null;
                if (irag.getIdNotificacion().getPersona().getMunicipioResidencia()!=null) {
                    departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                    municipiosResi = departamentoProce != null ? divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional()) : null;
                    comunidades = comunidadesService.getComunidades(irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
                }

                mav.addObject("entidades", entidades);
                mav.addObject("autorizado", autorizado);
                mav.addObject("departamentoProce", departamentoProce);
                mav.addObject("municipiosResi", municipiosResi);
                mav.addObject("comunidades", comunidades);
                mav.addObject("irag", irag);
                mav.addObject("fVacuna", new DaVacunasIrag());
                mav.addAllObjects(mapModel);
                mav.setViewName("irag/create");
            } else {
                mav.setViewName("404");
            }

        } else {
            mav.setViewName(urlValidacion);
        }

        return mav;
    }

    /**
     * Handler for edit reports.
     *
     * @param idNotificacion the ID of the report
     * @return a ModelMap with the model attributes for the respective view
     */
    @RequestMapping("edit/{idNotificacion}")
    public ModelAndView editIrag(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
        String urlValidacion = "";
        boolean autorizado = true;
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "404";
        }

        ModelAndView mav = new ModelAndView();
        if (urlValidacion.isEmpty()) {

            if (idNotificacion != null) {
                DaIrag irag = daIragService.getFormById(idNotificacion);

                if (irag != null) {
                    Initialize();

                    long idUsuario = seguridadService.obtenerIdUsuario(request);
                    irag.setUsuario(usuarioService.getUsuarioById((int) idUsuario));
                    if (idUsuario != 0) {
                        autorizado = seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE) ||
                                ((irag.getIdNotificacion().getCodSilaisAtencion()!=null && irag.getIdNotificacion().getCodUnidadAtencion()!=null) &&
                                        seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, irag.getIdNotificacion().getCodSilaisAtencion().getCodigo()) &&
                                        seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, irag.getIdNotificacion().getCodUnidadAtencion().getCodigo()));

                    }

                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);

                    if (!entidades.contains(irag.getIdNotificacion().getCodSilaisAtencion())){
                        entidades.add(irag.getIdNotificacion().getCodSilaisAtencion());
                    }

                    Divisionpolitica municipio = null;
                    if (irag.getIdNotificacion().getCodUnidadAtencion()!=null)
                        municipio = divisionPoliticaService.getMunicipiosByUnidadSalud(irag.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional());

                    List<Divisionpolitica> munic = null;
                    if (irag.getIdNotificacion().getCodSilaisAtencion()!=null)
                        munic = divisionPoliticaService.getMunicipiosBySilais(irag.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                    List<Unidades> uni = null;
                    if (irag.getIdNotificacion().getCodSilaisAtencion()!=null && irag.getIdNotificacion().getCodUnidadAtencion()!=null) {
                        uni = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int) idUsuario, irag.getIdNotificacion().getCodSilaisAtencion().getCodigo(), irag.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), ConstantsSecurity.SYSTEM_CODE, HealthUnitType.UnidadesPrimHosp.getDiscriminator());
                        if (!uni.contains(irag.getIdNotificacion().getCodUnidadAtencion())) {
                            uni.add(irag.getIdNotificacion().getCodUnidadAtencion());
                        }
                    }

                    //datos persona
                    Divisionpolitica departamentoProce = null;
                    List<Divisionpolitica> municipiosResi = null;
                    List<Comunidades> comunidades = null;

                    if (irag.getIdNotificacion().getPersona().getMunicipioResidencia() != null) {
                        String municipioResidencia = irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional();
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(municipioResidencia);
                        municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                        String comu = irag.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional();
                        comunidades = comunidadesService.getComunidades(comu);
                    }else if (irag.getIdNotificacion().getMunicipioResidencia() != null) {
                        String municipioResidencia = irag.getIdNotificacion().getMunicipioResidencia().getCodigoNacional();
                        departamentoProce = divisionPoliticaService.getDepartamentoByMunicipi(municipioResidencia);
                        municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(departamentoProce.getCodigoNacional());
                        String comu = irag.getIdNotificacion().getMunicipioResidencia().getCodigoNacional();
                        comunidades = comunidadesService.getComunidades(comu);
                    }


                    mav.addObject("irag", irag);
                    mav.addObject("autorizado", autorizado);
                    mav.addObject("entidades", entidades);
                    mav.addObject("munic", munic);
                    mav.addObject("departamentoProce", departamentoProce);
                    mav.addObject("municipiosResi", municipiosResi);
                    mav.addObject("comunidades", comunidades);
                    mav.addObject("uni", uni);
                    mav.addObject("fVacuna", new DaVacunasIrag());
                    mav.addObject("municipio", municipio);
                    mav.addAllObjects(mapModel);

                    mav.setViewName("irag/create");
                } else {
                    mav.setViewName("404");
                }
            } else {
                mav.setViewName("404");
            }

        } else {
            mav.setViewName(urlValidacion);
        }


        return mav;
    }

    @RequestMapping(value = "saveIrag", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProcessCreationFicha(
            @RequestParam(value = "codSilaisAtencion", required = true) Integer codSilaisAtencion
            , @RequestParam(value = "codUnidadAtencion", required = true) Integer codUnidadAtencion
            , @RequestParam(value = "fechaConsulta", required = true) String fechaConsulta
            , @RequestParam(value = "fechaPrimeraConsulta", required = false) String fechaPrimeraConsulta
            , @RequestParam(value = "codExpediente", required = false) String codExpediente
            , @RequestParam(value = "codClasificacion", required = false) String codClasificacion
            , @RequestParam(value = "nombreMadreTutor", required = false) String nombreMadreTutor
            , @RequestParam(value = "codProcedencia", required = true) String codProcedencia
            , @RequestParam(value = "codCaptacion", required = false) String codCaptacion
            , @RequestParam(value = "diagnostico", required = false) String diagnostico
            , @RequestParam(value = "tarjetaVacuna", required = false) Integer tarjetaVacuna
            , @RequestParam(value = "idNotificacion.fechaInicioSintomas", required = false) String fechaInicioSintomas
            , @RequestParam(value = "codAntbUlSem", required = false) String codAntbUlSem
            , @RequestParam(value = "cantidadAntib", required = false) Integer cantidadAntib
            , @RequestParam(value = "nombreAntibiotico", required = false) String nombreAntibiotico
            , @RequestParam(value = "fechaPrimDosisAntib", required = false) String fechaPrimDosisAntib
            , @RequestParam(value = "fechaUltDosisAntib", required = false) String fechaUltDosisAntib
            , @RequestParam(value = "codViaAntb", required = false) String codViaAntb
            , @RequestParam(value = "noDosisAntib", required = false) Integer noDosisAntib
            , @RequestParam(value = "usoAntivirales", required = false) String usoAntivirales
            , @RequestParam(value = "nombreAntiviral", required = false) String nombreAntiviral
            , @RequestParam(value = "fechaPrimDosisAntiviral", required = false) String fechaPrimDosisAntiviral
            , @RequestParam(value = "fechaUltDosisAntiviral", required = false) String fechaUltDosisAntiviral
            , @RequestParam(value = "noDosisAntiviral", required = false) Integer noDosisAntiviral
            , @RequestParam(value = "codResRadiologia", required = false) String codResRadiologia
            , @RequestParam(value = "otroResultadoRadiologia", required = false) String otroResultadoRadiologia
            , @RequestParam(value = "uci", required = false) Integer uci
            , @RequestParam(value = "noDiasHospitalizado", required = false) Integer noDiasHospitalizado
            , @RequestParam(value = "ventilacionAsistida", required = false) Integer ventilacionAsistida
            , @RequestParam(value = "diagnostico1Egreso", required = false) String diagnostico1Egreso
            , @RequestParam(value = "diagnostico2Egreso", required = false) String diagnostico2Egreso
            , @RequestParam(value = "fechaEgreso", required = false) String fechaEgreso
            , @RequestParam(value = "codCondEgreso", required = false) String codCondEgreso
            , @RequestParam(value = "personaId", required = false) Integer personaId
            , @RequestParam(value = "idNotificacion", required = false) String idNotificacion
            , @RequestParam(value = "manifestaciones", required = false) String manifestaciones
            , @RequestParam(value = "otraManifestacion", required = false) String otraManifestacion
            , @RequestParam(value = "condiciones", required = false) String condiciones
            , @RequestParam(value = "otraCondicion", required = false) String otraCondicion
            , @RequestParam(value = "semanasEmbarazo", required = false) Integer semanasEmbarazo
            , @RequestParam(value = "urgente", required = false) String urgente
            , @RequestParam(value = "codClasFCaso", required = false) String codClasFCaso
            , @RequestParam(value = "agenteBacteriano", required = false) String agenteBacteriano
            , @RequestParam(value = "serotipificacion", required = false) String serotipificacion
            , @RequestParam(value = "agenteViral", required = false) String agenteViral
            , @RequestParam(value = "agenteEtiologico", required = false) String agenteEtiologico
            , @RequestParam(value = "codClasFDetalleNV", required = false) String codClasFDetalleNV
            , @RequestParam(value = "codClasFDetalleNB", required = false) String codClasFDetalleNB
            , @RequestParam(value = "completa", required = false) String completa
            , HttpServletRequest request


    ) throws Exception {

        logger.debug("Agregando o actualizando formulario Irag");

        DaIrag irag;
        if (!idNotificacion.equals("")) {
            irag = daIragService.getFormById(idNotificacion);
        } else {
            irag = new DaIrag();
            irag.setFechaRegistro(new Timestamp(new Date().getTime()));
        }

        long idUsuario = seguridadService.obtenerIdUsuario(request);
        irag.setUsuario(usuarioService.getUsuarioById((int) idUsuario));


        if (seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE) ||
                (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, codSilaisAtencion)
                        && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, codUnidadAtencion))) {


            if (!codClasificacion.isEmpty()) {
                irag.setCodClasificacion(catalogoService.getClasificacion(codClasificacion));
            }

            irag.setCodExpediente(codExpediente);

            if (!fechaConsulta.equals("")) {
                irag.setFechaConsulta(StringToDate(fechaConsulta));
            }
            if (!fechaPrimeraConsulta.equals("")) {
                irag.setFechaPrimeraConsulta(StringToDate(fechaPrimeraConsulta));
            }

            irag.setNombreMadreTutor(nombreMadreTutor);

            if (!codCaptacion.isEmpty()) {
                irag.setCodCaptacion(catalogoService.getCaptacion(codCaptacion));
            }

            irag.setDiagnostico(cie10Service.getCie10ByCodigo(diagnostico));

            if (tarjetaVacuna != null) {
                irag.setTarjetaVacuna(tarjetaVacuna);
            }

            /*if (!fechaInicioSintomas.equals("")) {
                irag.setFechaInicioSintomas(StringToDate(fechaInicioSintomas));
            }*/

            if (!codAntbUlSem.isEmpty()) {
                irag.setCodAntbUlSem(catalogoService.getRespuesta(codAntbUlSem));
            }

            irag.setCantidadAntib(cantidadAntib);
            irag.setNombreAntibiotico(nombreAntibiotico);

            if (!fechaPrimDosisAntib.equals("")) {
                irag.setFechaPrimDosisAntib(StringToDate(fechaPrimDosisAntib));
            }

            if (!fechaUltDosisAntib.equals("")) {
                irag.setFechaUltDosisAntib(StringToDate(fechaUltDosisAntib));
            }

            irag.setNoDosisAntib(noDosisAntib);

            if (!codViaAntb.equals("")) {
                irag.setCodViaAntb(catalogoService.getViaAntibiotico(codViaAntb));
            }

            if (!usoAntivirales.equals("")) {
                irag.setUsoAntivirales(catalogoService.getRespuesta(usoAntivirales));
            }

            irag.setNombreAntiviral(nombreAntiviral);

            if (!fechaPrimDosisAntiviral.equals("")) {
                irag.setFechaPrimDosisAntiviral(StringToDate(fechaPrimDosisAntiviral));
            }

            if (!fechaUltDosisAntiviral.equals("")) {
                irag.setFechaUltDosisAntiviral(StringToDate(fechaUltDosisAntiviral));
            }

            irag.setNoDosisAntiviral(noDosisAntiviral);
            irag.setCodResRadiologia(codResRadiologia);
            irag.setOtroResultadoRadiologia(otroResultadoRadiologia);

            if (uci != null) {
                irag.setUci(uci);
            }

            irag.setNoDiasHospitalizado(noDiasHospitalizado);

            if (ventilacionAsistida != null) {
                irag.setVentilacionAsistida(ventilacionAsistida);
            }

            //irag.setDiagnostico1Egreso(cie10Service.getCie10ByCodigo(diagnostico1Egreso));
            //irag.setDiagnostico2Egreso(cie10Service.getCie10ByCodigo(diagnostico2Egreso));
            irag.setDiagnostico1Egreso(diagnostico1Egreso);
            irag.setDiagnostico2Egreso(diagnostico2Egreso);

            if (!fechaEgreso.equals("")) {
                irag.setFechaEgreso(StringToDate(fechaEgreso));
            }

            irag.setCodCondEgreso(catalogoService.getCondicionEgreso(codCondEgreso));

            irag.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));
            irag.setManifestaciones(manifestaciones);
            irag.setCondiciones(condiciones);
            irag.setOtraCondicion(otraCondicion);
            irag.setOtraManifestacion(otraManifestacion);
            irag.setSemanasEmbarazo(semanasEmbarazo);

            if (irag.getIdNotificacion() == null) {
                //crear nueva notificacion
                DaNotificacion noti = guardarNotificacion(personaId, request, codSilaisAtencion, codUnidadAtencion, urgente,completa, semanasEmbarazo, condiciones);
                irag.setIdNotificacion(daNotificacionService.getNotifById(noti.getIdNotificacion()));
            } else {
                irag.getIdNotificacion().setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
                irag.getIdNotificacion().setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
                irag.getIdNotificacion().setUrgente(catalogoService.getRespuesta(urgente));
                if (fechaInicioSintomas != null && !fechaInicioSintomas.equals("")) {
                    irag.getIdNotificacion().setFechaInicioSintomas(StringToDate(fechaInicioSintomas));
                }
                //actualizar notificacion
                irag.getIdNotificacion().setCompleta(Boolean.parseBoolean(completa));
                irag.getIdNotificacion().setActor(seguridadService.obtenerNombreUsuario(request));
                if (irag.getIdNotificacion().getPersona().getSexo().getCodigo().equalsIgnoreCase("SEXO|F") && condiciones!=null) {
                    irag.getIdNotificacion().setSemanasEmbarazo(semanasEmbarazo);
                    if (condiciones.contains("CONDPRE|EMB")) {
                        irag.getIdNotificacion().setEmbarazada(catalogoService.getRespuesta("RESP|S"));
                    }else {
                        irag.getIdNotificacion().setEmbarazada(catalogoService.getRespuesta("RESP|N"));
                    }
                }
            }
            irag.getIdNotificacion().setCodExpediente(irag.getCodExpediente());

            irag.setCodClasFDetalleNB(catalogoService.getClasificacionFinalNB(codClasFDetalleNB));
            irag.setCodClasFDetalleNV(catalogoService.getClasificacionFinalNV(codClasFDetalleNV));
            irag.setCodClasFCaso(codClasFCaso);
            irag.setAgenteBacteriano(agenteBacteriano);
            irag.setSerotipificacion(serotipificacion);
            irag.setAgenteViral(agenteViral);
            irag.setAgenteEtiologico(agenteEtiologico);
            irag.setActor(seguridadService.obtenerNombreUsuario(request));

            daIragService.saveOrUpdateIrag(irag);


            return createJsonResponse(irag);
        } else {
            throw new Exception("No tiene autorizaci�n para guardar notificaci�n en esta unidad de salud");
        }

    }


    @RequestMapping(value = "saveNotification", method = RequestMethod.GET)
    public DaNotificacion guardarNotificacion(@PathVariable("personaId") Integer personaId, HttpServletRequest request, Integer silais, Integer unidad, String urgente, String completa, Integer semanasEmbarazo, String condiciones) throws Exception {

        logger.debug("Guardando Notificacion");
        DaNotificacion noti = new DaNotificacion();

        if (personaId != 0) {
            SisPersona persona = personaService.getPersona(personaId);
            noti.setPersona(persona);
            noti.setFechaRegistro(new Timestamp(new Date().getTime()));
            noti.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(silais));
            noti.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(unidad));
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            noti.setUsuarioRegistro(usuarioService.getUsuarioById((int) idUsuario));
            // noti.setUsuarioRegistro(usuarioService.getUsuarioById(1));
            noti.setCodTipoNotificacion(catalogoService.getTipoNotificacion("TPNOTI|IRAG"));
            noti.setMunicipioResidencia(persona.getMunicipioResidencia());
            noti.setComunidadResidencia(persona.getComunidadResidencia());
            noti.setDireccionResidencia(persona.getDireccionResidencia());
            noti.setUrgente(catalogoService.getRespuesta(urgente));
            noti.setCompleta(Boolean.parseBoolean(completa));
            noti.setActor(seguridadService.obtenerNombreUsuario(request));
            if (persona.getSexo().getCodigo().equalsIgnoreCase("SEXO|F") && condiciones!=null) {
                noti.setSemanasEmbarazo(semanasEmbarazo);
                if (condiciones.contains("CONDPRE|EMB")) {
                    noti.setEmbarazada(catalogoService.getRespuesta("RESP|S"));
                }else {
                    noti.setEmbarazada(catalogoService.getRespuesta("RESP|N"));
                }
            }
            daNotificacionService.addNotification(noti);
            return noti;
        } else {
            throw new Exception();
        }

    }

    @RequestMapping(value = "updatePerson", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePerson(
            @RequestParam(value = "municipioResidencia", required = false) String municipioResidencia
            , @RequestParam(value = "comunidadResidencia", required = false) String comunidadResidencia
            , @RequestParam(value = "direccionResidencia", required = false) String direccionResidencia
            , @RequestParam(value = "telefonoResidencia", required = false) String telefonoResidencia
            , @RequestParam(value = "personaId", required = false) Integer personaId
            , @RequestParam(value = "idNotificacion", required = false) String idNotificacion
            , @RequestParam(value = "completa", required = false) String completa
            , HttpServletRequest request

    ) throws Exception {

        logger.debug("Actualizando datos persona");
        SisPersona pers = new SisPersona();
        InfoResultado infoResultado;
        if (personaId != null) {
            pers = personaService.getPersona(personaId);
            pers.setMunicipioResidencia(divisionPoliticaService.getDivisionPolitiacaByCodNacional(municipioResidencia));
            pers.setComunidadResidencia(comunidadesService.getComunidad(comunidadResidencia));
            pers.setDireccionResidencia(direccionResidencia);
            pers.setTelefonoResidencia(telefonoResidencia);
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT) {
                Persona persona = personaService.ensamblarObjetoPersona(pers);
                try {
                    personaService.iniciarTransaccion();

                    infoResultado = personaService.guardarPersona(persona, seguridadService.obtenerNombreUsuario(request));
                    if (infoResultado.isOk() && infoResultado.getObjeto() != null) {
                        updateNotificacion(request,idNotificacion, pers, completa);
                    } else
                        throw new Exception(infoResultado.getMensaje() + "----" + infoResultado.getMensajeDetalle());
                    personaService.commitTransaccion();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    ex.printStackTrace();
                    try {
                        personaService.rollbackTransaccion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new Exception(ex);
                } finally {
                    try {
                        personaService.remover();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                updateNotificacion(request,idNotificacion, pers, completa);
            }
        }
        return createJsonResponse(pers);
    }


    private ResponseEntity<String> createJsonResponse(Object o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    /**
     * Override form
     *
     * @param idNotificacion the ID of the form
     */
    @RequestMapping(value = "update/{idNotificacion}")
    public void updateNotificacion(HttpServletRequest request, String idNotificacion, SisPersona persona, String completa) throws Exception {
        DaIrag irag = null;
        DaNotificacion noti = null;

        if (idNotificacion != null) {
            irag = daIragService.getFormById(idNotificacion);

            //DaNotificacion
            noti = daNotificacionService.getNotifById(idNotificacion);

            if (noti !=null && !noti.isCompleta()){
                noti.setMunicipioResidencia(persona.getMunicipioResidencia());
                noti.setComunidadResidencia(persona.getComunidadResidencia());
                noti.setDireccionResidencia(persona.getDireccionResidencia());
                noti.setCompleta(Boolean.parseBoolean(completa));
                noti.setActor(seguridadService.obtenerNombreUsuario(request));
                daNotificacionService.updateNotificacion(noti);
            }

        }

    }


    /**
     * Custom handler for voiding a notificacion.
     *
     * @param idNotificacion the ID of the chs to avoid
     * @return a String
     */

    @RequestMapping("/override/{idNotificacion}")
    public String overrideNoti(@PathVariable("idNotificacion") String idNotificacion,
                               RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String urlValidacion = "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaci�n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        } catch (Exception e) {
            e.printStackTrace();
            urlValidacion = "redirect:/404";
        }
        if (urlValidacion.isEmpty()) {
            DaIrag irag = daIragService.getFormById(idNotificacion);
            if (irag != null) {
                irag.getIdNotificacion().setPasivo(true);
                irag.getIdNotificacion().setActor(seguridadService.obtenerNombreUsuario(request));
                irag.setActor(irag.getIdNotificacion().getActor());
                daIragService.saveOrUpdateIrag(irag);
                return "redirect:/irag/search/" + irag.getIdNotificacion().getPersona().getPersonaId();
            } else {
                return "redirect:/404";
            }
        } else {
            return "redirect:/" + urlValidacion;
        }
    }

    /**
     * Override Vaccine
     *
     * @param idVacuna the ID of the form
     */
    @RequestMapping(value = "overrideVaccine/{idVacuna}", method = RequestMethod.GET)
    public ModelAndView overrideVaccine(@PathVariable("idVacuna") Integer idVacuna, HttpServletRequest request) throws Exception {
        DaVacunasIrag va = daVacunasIragService.getVaccineById(idVacuna);
        va.setPasivo(true);
        va.setActor(seguridadService.obtenerNombreUsuario(request));
        daVacunasIragService.updateVaccine(va);
        String idNotificacion = va.getIdNotificacion().getIdNotificacion().getIdNotificacion();

        return editIrag(idNotificacion, request);
    }


    private Date StringToDate(String strFecha) throws ParseException {
        DateFormat formatter;
        Date date;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.parse(strFecha);
        return date;
    }

    /**
     * Retorna una lista de enfermedades. Acepta una solicitud GET para JSON
     *
     * @return Un arreglo JSON de Cie10
     */
    @RequestMapping(value = "enfermedades", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Cie10> fetchEnfermedadesJson(@RequestParam(value = "filtro", required = true) String filtro) {
        logger.info("Obteniendo las enfermedades en JSON");
        List<Cie10> enfermedades = cie10Service.getCie10Filtered(filtro);
        if (enfermedades == null) {
            logger.debug("Nulo");
        }
        return enfermedades;
    }


    @RequestMapping(value = "getPDF", method = RequestMethod.GET)
    public
    @ResponseBody
    String getPDF(@RequestParam(value = "idNotificacion", required = true) String idNotificacion, HttpServletRequest request) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDDocument doc = new PDDocument();
        DaNotificacion not = daNotificacionService.getNotifById(idNotificacion);
        String res = null;
        if (not != null) {
            if (not.getCodTipoNotificacion().getCodigo().equals("TPNOTI|IRAG")) {
                DaIrag irag = daIragService.getFormById(not.getIdNotificacion());
                // String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

                if (irag != null) {
                    PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                    doc.addPage(page);
                    PDPageContentStream stream = new PDPageContentStream(doc, page);

                    String urlServer = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
                    URL url = new URL(urlServer+"/resources/img/fichas/fichaIrag1.png");
                    BufferedImage image = ImageIO.read(url);

                    GeneralUtils.drawObject(stream, doc, image, 10, 50, 580, 780);

                    String clasificacion = irag.getCodClasificacion()!= null? irag.getCodClasificacion().getCodigo():null;

                    String us = irag.getIdNotificacion().getCodUnidadAtencion() != null ? irag.getIdNotificacion().getCodUnidadAtencion().getNombre() : "----";

                    // String silais = irag.getIdNotificacion().getCodSilaisAtencion().getNombre();

                    // String nombreS = silais != null ? silais.replace("SILAIS", "") : "----";
                    // String municipio = irag.getIdNotificacion().getPersona().getMunicipioResidencia() != null ? irag.getIdNotificacion().getPersona().getMunicipioResidencia().getNombre() : "----";
                    String nExp = irag.getCodExpediente() != null ? irag.getCodExpediente() : "----------";

                    String fecha = irag.getFechaConsulta() != null ? DateUtil.DateToString(irag.getFechaConsulta(), "yyyy/MM/dd") : null;
                    String[] array = fecha != null ? fecha.split("/") : null;

                    String dia = array != null ? array[2] : "--";
                    String mes = array != null ? array[1] : "--";
                    String anio = array != null ? array[0] : "--";


                    String fechaPrimera = irag.getFechaPrimeraConsulta() != null ? DateUtil.DateToString(irag.getFechaPrimeraConsulta(), "yyyy/MM/dd") : null;
                    String[] array1 = fechaPrimera != null ? fechaPrimera.split("/") : null;

                    String diaFP = array1 != null ? array1[2] : "--";
                    String mesFP = array1 != null ? array1[1] : "--";
                    String anioFP = array1 != null ? array1[0] : "--";

                    String nombrePersona = null;

                    nombrePersona = irag.getIdNotificacion().getPersona().getPrimerNombre();
                    if (irag.getIdNotificacion().getPersona().getSegundoNombre() != null)
                        nombrePersona = nombrePersona + " " + irag.getIdNotificacion().getPersona().getSegundoNombre();
                    nombrePersona = nombrePersona + " " + irag.getIdNotificacion().getPersona().getPrimerApellido();
                    if (irag.getIdNotificacion().getPersona().getSegundoApellido() != null)
                        nombrePersona = nombrePersona + " " + irag.getIdNotificacion().getPersona().getSegundoApellido();

                    String edad = null;
                    if (irag.getIdNotificacion().getPersona().getFechaNacimiento() != null && irag.getFechaConsulta() != null) {
                        edad = DateUtil.calcularEdad(irag.getIdNotificacion().getPersona().getFechaNacimiento(), irag.getFechaConsulta());
                    }

                    String[] edad1 = edad != null ? edad.split("/") : null;
                    String anios = edad1 != null ? edad1[0] : null;
                    String meses = edad1 != null ? edad1[1] : "--";
                    String dias = edad1 != null ? edad1[2] : "--";

                    String fNac = irag.getIdNotificacion().getPersona().getFechaNacimiento() != null ? DateUtil.DateToString(irag.getIdNotificacion().getPersona().getFechaNacimiento(), "yyyy/MM/dd") : null;
                    String[] fechaNac = fNac != null ? fNac.split("/") : null;
                    String anioNac = fechaNac != null ? fechaNac[0] : "--";
                    String mesNac = fechaNac != null ? fechaNac[1] : "--";
                    String diaNac = fechaNac != null ? fechaNac[2] : "--";

                    String sexo = irag.getIdNotificacion().getPersona().getSexo() != null ? irag.getIdNotificacion().getPersona().getSexo().getValor() : null;

                    String tutor = irag.getNombreMadreTutor() != null ? irag.getNombreMadreTutor() : "----------";

                    String procedencia = irag.getCodProcedencia() != null ? irag.getCodProcedencia().getValor() : null;


                    String fis = irag.getIdNotificacion().getFechaInicioSintomas() != null ? DateUtil.DateToString(irag.getIdNotificacion().getFechaInicioSintomas(), "dd/MM/yyyy") : null;
                    String[] fechaFis = fis != null ? fis.split("/") : null;
                    String diaFis = fechaFis != null ? fechaFis[0] : "--";
                    String mesFis = fechaFis != null ? fechaFis[1] : "--";
                    String anioFis = fechaFis != null ? fechaFis[2] : "--";


                    String depProce = irag.getIdNotificacion().getPersona().getMunicipioResidencia() != null ? irag.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getNombre() : "----------";
                    String municProce = irag.getIdNotificacion().getPersona().getMunicipioResidencia() != null ? irag.getIdNotificacion().getPersona().getMunicipioResidencia().getNombre() : "----------";
                    String comunidadResidencia = irag.getIdNotificacion().getMunicipioResidencia() != null ? irag.getIdNotificacion().getMunicipioResidencia().getNombre() : "----------";
                    String direccionResidencia = irag.getIdNotificacion().getDireccionResidencia() != null ? irag.getIdNotificacion().getDireccionResidencia() : "----------";
                    String telefono = irag.getIdNotificacion().getPersona().getTelefonoResidencia() != null ? irag.getIdNotificacion().getPersona().getTelefonoResidencia() : "----------";
                    String captacion = irag.getCodCaptacion() != null ? irag.getCodCaptacion().getCodigo() : null;
                    String dxIngreso = irag.getDiagnostico() != null ? irag.getDiagnostico().getNombreCie10() : "----------";
                    Integer tarjetaVac = irag.getTarjetaVacuna() != null ? irag.getTarjetaVacuna() : null;

                    List<DaVacunasIrag> vacunas = daVacunasIragService.getAllVaccinesByIdIrag(irag.getIdNotificacion().getIdNotificacion());
                    String hib = null;
                    String influenza = null;
                    String menin = null;
                    String neumo = null;

                    for (DaVacunasIrag vac : vacunas) {

                        if (vac.getCodVacuna().getCodigo().equals("VAC|HIB")) {

                            hib = vac.getCodTipoVacuna() + "-" + vac.getDosis() + "-" + DateUtil.DateToString(vac.getFechaUltimaDosis(), "dd/MM/yyyy");

                        } else if (vac.getCodVacuna().getCodigo().equals("VAC|INFLU")) {

                            influenza = vac.getCodTipoVacuna() + "-" + vac.getDosis() + "-" + DateUtil.DateToString(vac.getFechaUltimaDosis(), "dd/MM/yyyy");

                        } else if (vac.getCodVacuna().getCodigo().equals("VAC|MEN")) {

                            menin = vac.getCodTipoVacuna() + "-" + vac.getDosis() + "-" + DateUtil.DateToString(vac.getFechaUltimaDosis(), "dd/MM/yyyy");

                        } else if (vac.getCodVacuna().getCodigo().equals("VAC|NEUM")) {

                            neumo = vac.getCodTipoVacuna() + "-" + vac.getDosis() + "-" + DateUtil.DateToString(vac.getFechaUltimaDosis(), "dd/MM/yyyy");

                        }

                    }

                    String condicPre = irag.getCondiciones() != null ? irag.getCondiciones() : null;

                    String mesesEmb = irag.getSemanasEmbarazo() != null ? irag.getSemanasEmbarazo().toString() : "--";

                    String otraCondicion = irag.getOtraCondicion() != null ? irag.getOtraCondicion() : "----------";

                    String manifestaciones = irag.getManifestaciones() != null ? irag.getManifestaciones() : null;

                    String otraManif = irag.getOtraManifestacion() != null ? irag.getOtraManifestacion() : "----------";

                    String usoAntib = irag.getCodAntbUlSem() != null ? irag.getCodAntbUlSem().getValor() : null;

                    String cantAntib = irag.getCantidadAntib() != null ? irag.getCantidadAntib().toString() : "--";

                    String nombreAntib = irag.getNombreAntibiotico() != null ? irag.getNombreAntibiotico() : "-----------";

                    Integer difDiasAntib = irag.getFechaPrimDosisAntib() != null && irag.getFechaUltDosisAntib() != null ? DateUtil.CalcularDiferenciaDiasFechas(irag.getFechaPrimDosisAntib(), irag.getFechaUltDosisAntib()) : 0;

                    String viaAntib = irag.getCodViaAntb() != null ? irag.getCodViaAntb().getValor() : null;

                    String fechaUltDosis = irag.getFechaUltDosisAntib() != null ? DateUtil.DateToString(irag.getFechaUltDosisAntib(), "dd/MM/yyyy") : "-----------";

                    String usoAntiv = irag.getUsoAntivirales() != null ? irag.getUsoAntivirales().getValor() : null;

                    String nombreAntiv = irag.getNombreAntiviral() != null ? irag.getNombreAntiviral() : "----------";

                    String fechaPDAntiv = irag.getFechaPrimDosisAntiviral() != null ? DateUtil.DateToString(irag.getFechaPrimDosisAntiviral(), "dd/MM/yyyy") : "----------";

                    String fechaUDAntiv = irag.getFechaUltDosisAntiviral() != null ? DateUtil.DateToString(irag.getFechaUltDosisAntiviral(), "dd/MM/yyyy") : "----------";

                    Integer dosisAntiv = irag.getNoDosisAntiviral() != null ? irag.getNoDosisAntiviral() : 0;

                    String radiologia = irag.getCodResRadiologia() != null ? irag.getCodResRadiologia() : null;

                    String otroResRad = irag.getOtroResultadoRadiologia() != null ? irag.getOtroResultadoRadiologia() : null;

                    String uci = irag.getUci() != null ? irag.getUci().toString() : null;

                    Integer diasUci = irag.getNoDiasHospitalizado() != null ? irag.getNoDiasHospitalizado() : 0;

                    String ventilacion = irag.getVentilacionAsistida() != null ? irag.getVentilacionAsistida().toString() : null;

                    String dxEgreso1 = irag.getDiagnostico1Egreso() != null ? irag.getDiagnostico1Egreso() : "----------";

                    String dxEgreso2 = irag.getDiagnostico2Egreso() != null ? irag.getDiagnostico2Egreso() : "----------";

                    String fechaEgreso = irag.getFechaEgreso() != null ? DateUtil.DateToString(irag.getFechaEgreso(), "dd/MM/yyyy HH:mm:ss") : "----------";

                    String condicionEgreso = irag.getCodCondEgreso() != null ? irag.getCodCondEgreso().getCodigo() : null;

                    String codEgreso1 = irag.getDiagnostico1Egreso() != null ? irag.getDiagnostico1Egreso() : null;

                    String codEgreso2 = irag.getDiagnostico2Egreso() != null ? irag.getDiagnostico2Egreso() : null;

                    String clasFinal = irag.getCodClasFCaso() != null ? irag.getCodClasFCaso() : null;

                    String nb = irag.getCodClasFDetalleNB() != null ? irag.getCodClasFDetalleNB().getCodigo() : null;

                    String nv = irag.getCodClasFDetalleNV() != null ? irag.getCodClasFDetalleNV().getCodigo() : null;

                    String etiologicoBacteriano = irag.getAgenteBacteriano() != null ? irag.getAgenteBacteriano() : null;

                    String etiologicoViral = irag.getAgenteViral() != null ? irag.getAgenteViral() : null;

                    // String agentesEt = irag.getAgenteEtiologico() != null ? irag.getAgenteEtiologico() : null;

                    String seroti = irag.getSerotipificacion() != null ? irag.getSerotipificacion() : null;

                    String fechaRegistro = irag.getFechaRegistro() != null ? DateUtil.DateToString(irag.getFechaRegistro(), "dd/MM/yyyy hh:mm:ss a") : "------";

                    String nombreUsuario = irag.getUsuario() != null ? irag.getUsuario().getNombre() : null;

                    float y = 737;
                    float x = 86;
                    float x1 = 86;

                    if(clasificacion!= null){
                        switch (clasificacion) {
                            case "CLASIFVI|ETI":
                                x1 += 377;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y + 25, x1, stream, 8, PDType1Font.TIMES_BOLD);
                                break;
                            case "CLASIFVI|IRAG":
                                x1 += 336;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y + 25, x1, stream, 8, PDType1Font.TIMES_BOLD);

                                break;
                            case "CLASIFVI|INUS":
                                x1 += 463;
                                GeneralUtils.drawTEXT("(" + messageSource.getMessage("lbl.x", null, null) +")", y + 25, x1, stream, 8, PDType1Font.TIMES_BOLD);

                                break;
                        }
                    }

                    x1= x +55;
                    GeneralUtils.drawTEXT(us, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 42;
                    x1 = x + 55;
                    GeneralUtils.drawTEXT(dia, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 20;
                    GeneralUtils.drawTEXT(mes, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 20;
                    GeneralUtils.drawTEXT(anio, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    x1 = x + 188;
                    GeneralUtils.drawTEXT(diaFP, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 20;
                    GeneralUtils.drawTEXT(mesFP, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 23;
                    GeneralUtils.drawTEXT(anioFP, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    x1 = x1 + 110;
                    GeneralUtils.drawTEXT(nExp, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 20;
                    //menor o mayor de 5 a�os
                    if (anios != null) {
                        int edadAnios = Integer.parseInt(anios);
                        if (edadAnios > 5) {
                            x1 = x + 303;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 8, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 163;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 8, PDType1Font.TIMES_BOLD);
                        }
                    }

                    y -= 23;
                    x1 = x + 23;
                    GeneralUtils.drawTEXT(nombrePersona, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);


                    x1 = x1 + 265;
                    GeneralUtils.drawTEXT(tutor, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);


                    y -= 12;
                    if (sexo != null) {
                        if (sexo.equals("Hombre")) {
                            x1 = x + 24;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        } else if (sexo.equals("Mujer")) {
                            x1 = x + 67;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }
                    }

                    x1 = x + 180;
                    GeneralUtils.drawTEXT(diaNac, y, x1, stream, 6, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 16;
                    GeneralUtils.drawTEXT(mesNac, y, x1, stream, 6, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 15;
                    GeneralUtils.drawTEXT(anioNac, y, x1, stream, 6, PDType1Font.TIMES_ROMAN);


                    if (anios != null && meses != null) {
                        int edadAnios = Integer.parseInt(anios);
                        int mesesEdad = Integer.parseInt(meses);

                        if (edadAnios < 1) {
                            x1 = x + 415;
                            GeneralUtils.drawTEXT(meses, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                        } else if (mesesEdad < 1) {
                            y -= 10;
                            x1 = x + 25;
                            GeneralUtils.drawTEXT(dias, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                        } else if (edadAnios > 1) {
                            x1 = x + 330;
                            GeneralUtils.drawTEXT(anios, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                        }

                    }

                    y -= 28;
                    x1 = x + 82;
                    GeneralUtils.drawTEXT(depProce, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 180;
                    GeneralUtils.drawTEXT(municProce, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 9;
                    x1 = x + 38;
                    GeneralUtils.drawTEXT(direccionResidencia, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 8;
                    x1 = x1 + 28;
                    GeneralUtils.drawTEXT(comunidadResidencia + " " + telefono, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);


                    if (procedencia != null) {
                        if (procedencia.equals("Urbano")) {
                            x1 = x + 334;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        } else if (procedencia.equals("Rural")) {
                            x1 = x + 368;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                    }


                    y -= 9;
                    if (captacion != null) {
                        switch (captacion) {
                            case "CAPTAC|EMER":
                                x1 = x + 60;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                            case "CAPTAC|SALA":
                                x1 = x + 98;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                            case "CAPTAC|UCI":
                                x1 = x + 132;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                            case "CAPTAC|AMB":
                                x1 = x + 65;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 8, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                        }

                    }

                    y -= 8;
                    x1 = x + 230;
                    GeneralUtils.drawTEXT(dxIngreso, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 16;
                    x1 = x + 110;
                    GeneralUtils.drawTEXT(diaFis, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 20;
                    GeneralUtils.drawTEXT(mesFis, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    x1 = x1 + 16;
                    GeneralUtils.drawTEXT(anioFis, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    y -= 18;
                    if (tarjetaVac != null) {
                        if (tarjetaVac == 0) {
                            x1 = x + 248;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 287;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }
                    }


                    y -= 19;
                    if (hib != null) {
                        x1 = x + 60;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        String[] hib1 = hib.split("-");
                        x1 = x + 320;
                        GeneralUtils.drawTEXT(hib1[1], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        x1 = x + 368;
                        GeneralUtils.drawTEXT(hib1[2], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        if (hib.contains("TVAC|HIB1")) {
                            x1 = x + 153;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                    } else {
                        x1 = x + 85;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                    }

                    y -= 12;
                    if (menin != null) {
                        x1 = x + 60;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        String[] menin1 = menin.split("-");
                        x1 = x + 320;
                        GeneralUtils.drawTEXT(menin1[1], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        x1 = x + 368;
                        GeneralUtils.drawTEXT(menin1[2], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        if (menin.contains("TVAC|MENING1")) {
                            x1 = x + 153;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                        if (menin.contains("TVAC|MENING2")) {
                            x1 = x + 199;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                    } else {
                        x1 = x + 85;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                    }

                    y -= 14;
                    if (neumo != null) {
                        x1 = x + 60;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        String[] neumo1 = neumo.split("-");
                        x1 = x + 320;
                        GeneralUtils.drawTEXT(neumo1[1], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        x1 = x + 368;
                        GeneralUtils.drawTEXT(neumo1[2], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        if (neumo.contains("TVAC|NEUMO1")) {
                            x1 = x + 153;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y + 4, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                        if (neumo.contains("TVAC|NEUMO2")) {
                            x1 = x + 205;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y + 4, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (neumo.contains("TVAC|NEUMO3")) {
                            x1 = x + 155;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 4, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                    } else {
                        x1 = x + 85;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                    }

                    y -= 14;
                    if (influenza != null) {
                        x1 = x + 60;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        String[] flu1 = influenza.split("-");
                        x1 = x + 320;
                        GeneralUtils.drawTEXT(flu1[1], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        x1 = x + 368;
                        GeneralUtils.drawTEXT(flu1[2], y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        if (influenza.contains("TVAC|FLU1")) {
                            x1 = x + 153;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                        if (influenza.contains("TVAC|FLU2")) {
                            x1 = x + 199;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (influenza.contains("TVAC|FLU3")) {
                            x1 = x + 235;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                    } else {
                        x1 = x + 85;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                    }


                    y -= 40;
                    if (condicPre != null) {
                        if (condicPre.contains("CONDPRE|CANC")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|ENFCARD")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|DESN")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 15;

                        if (condicPre.contains("CONDPRE|DIAB")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|ASMA")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|OBES")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 15;

                        if (condicPre.contains("CONDPRE|VIH")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|EPOC")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|EMB")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                            x1 = x + 350;
                            GeneralUtils.drawTEXT(mesesEmb, y + 4, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 13;

                        if (condicPre.contains("CONDPRE|OTINM")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|OTENFPUL")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|CORTIC")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 13;

                        if (condicPre.contains("CONDPRE|ENFNEU")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|INSRENAL")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (condicPre.contains("CONDPRE|OTRA")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                            x1 = x + 300;
                            GeneralUtils.drawTEXT(otraCondicion, y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }
                    }else{
                        y-= 56; // saltar toda la tabla de condiciones, aunque no tenga valoresw
                    }

                    y -= 40;
                    if (manifestaciones != null) {
                        if (manifestaciones.contains("MANCLIN|FIEB")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT("*****", y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT("****", y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|TIRAJS")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|DLMUSC")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 13;

                        if (manifestaciones.contains("MANCLIN|DLGAR")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|ESTRID")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|EXANT")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 11;

                        if (manifestaciones.contains("MANCLIN|TOS")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|VOMTS")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|MALGRAL")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 11;

                        if (manifestaciones.contains("MANCLIN|ESTOR")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|INTVO")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|LETAR")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 9;

                        if (manifestaciones.contains("MANCLIN|SIBIL")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|DIARREA")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|CONV")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);


                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }


                        y -= 11;

                        if (manifestaciones.contains("MANCLIN|SECNAS")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|DLCBZA")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|OTRA")) {
                            x1 = x + 388;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                            x1 = x + 335;
                            GeneralUtils.drawTEXT(otraManif, y, x1, stream, 7, PDType1Font.TIMES_BOLD);


                        } else {
                            x1 = x + 408;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        y -= 9;

                        if (manifestaciones.contains("MANCLIN|DIFRESP")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|CONJUN")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        y -= 9;

                        if (manifestaciones.contains("MANCLIN|TAQPN")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 99;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (manifestaciones.contains("MANCLIN|DLABDM")) {
                            x1 = x + 225;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        } else {
                            x1 = x + 245;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                    }else{
                        y -= 73; // desplazar toda la tabla de manifestaciones aunque no tenga datos
                    }

                    y -= 18;
                    if (usoAntib != null) {
                        switch (usoAntib) {
                            case "Si":
                                x1 = x + 192;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                x1 = x + 108;
                                GeneralUtils.drawTEXT(cantAntib, y - 13, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                x1 = x + 210;
                                GeneralUtils.drawTEXT(nombreAntib, y - 13, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                if (difDiasAntib != 0) {
                                    x1 = x + 17;
                                    GeneralUtils.drawTEXT(difDiasAntib.toString(), y - 26, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                                }

                                if (viaAntib != null) {
                                    switch (viaAntib) {
                                        case "Oral":
                                            x1 = x + 63;
                                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 26, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                            break;
                                        case "Parenteral":
                                            x1 = x + 117;
                                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 26, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                            break;
                                        case "Ambas":
                                            x1 = x + 156;
                                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 26, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                            break;
                                    }
                                }

                                x1 = x + 250;
                                GeneralUtils.drawTEXT(fechaUltDosis, y - 26, x1, stream, 7, PDType1Font.TIMES_ROMAN);


                                break;
                            case "No":
                                x1 = x + 230;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                            case "No Sabe":
                                x1 = x + 295;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                        }
                    }

                    y -= 38;
                    if (usoAntiv != null) {
                        switch (usoAntiv) {
                            case "Si":
                                x1 = x + 98;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                x1 = x + 230;
                                GeneralUtils.drawTEXT(nombreAntiv, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                x1 = x + 47;
                                GeneralUtils.drawTEXT(fechaPDAntiv, y - 12, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                x1 = x + 151;
                                GeneralUtils.drawTEXT(fechaUDAntiv, y - 12, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                if (dosisAntiv != 0) {
                                    x1 = x + 270;
                                    GeneralUtils.drawTEXT(dosisAntiv.toString(), y - 12, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                                }


                                break;
                            case "No":
                                x1 = x + 126;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;

                        }
                    }

                    y -= 47;
                    if (radiologia != null) {
                        if (radiologia.contains("RESRAD|CONS")) {
                            x1 = x + 79;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (radiologia.contains("RESRAD|DERR")) {
                            x1 = x + 173;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (radiologia.contains("RESRAD|PAMIX")) {
                            x1 = x + 285;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (radiologia.contains("RESRAD|PAINT")) {
                            x1 = x + 355;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (radiologia.contains("RESRAD|AIRE")) {
                            x1 = x + 430;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }

                        if (otroResRad != null) {
                            x1 = x + 30;
                            GeneralUtils.drawTEXT(otroResRad, y - 10, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                        }


                    }

                    stream.close();
                    page = new PDPage(PDPage.PAGE_SIZE_A4);
                    doc.addPage(page);
                    stream = new PDPageContentStream(doc, page);

                    y = 770;
                    x1 = 65;
                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.irag.lab.data", null, null), y, x1, stream, 9, PDType1Font.TIMES_BOLD);
                    y -= 10;

                    //load all the request by notification

                    List<DaSolicitudDx> diagnosticosList = resultadoFinalService.getSolicitudesDxByIdNotificacion(irag.getIdNotificacion().getIdNotificacion());

                    if(diagnosticosList.isEmpty()){
                        x1 = 75;
                        GeneralUtils.drawTEXT(messageSource.getMessage("lbl.nothing.to.show", null, null), y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    }


                    float y1 = 0;
                    url = new URL(urlServer+"/resources/img/fichas/fichaIrag2.png");
                    BufferedImage image2 = ImageIO.read(url);

                    if (!diagnosticosList.isEmpty()) {
                        int con = 0;
                        for (DaSolicitudDx soli : diagnosticosList) {
                            List<String[]> reqList = new ArrayList<String[]>();
                            List<String[]> dxList = new ArrayList<String[]>();
                            con++;
                            if (con >= 2) {
                                y = y1;
                            }
                            String[] content = new String[5];
                            List<OrdenExamen> ordenes = ordenExamenMxService.getOrdenesExamenNoAnuladasByIdSolicitud(soli.getIdSolicitudDx());
                            List<DetalleResultadoFinal> resul = resultadoFinalService.getDetResActivosBySolicitud(soli.getIdSolicitudDx());

                            content[0] = soli.getCodDx().getNombre() != null ? soli.getCodDx().getNombre() : "";
                            content[1] = soli.getFechaHSolicitud() != null ? DateUtil.DateToString(soli.getFechaHSolicitud(), "dd/MM/yyyy hh:mm:ss a") : "";
                            content[2] = soli.getIdTomaMx().getFechaHTomaMx() != null ? DateUtil.DateToString(soli.getIdTomaMx().getFechaHTomaMx(), "dd/MM/yyyy hh:mm:ss a") : "";
                            content[3] = soli.getIdTomaMx().getCodTipoMx() != null ? soli.getIdTomaMx().getCodTipoMx().getNombre() : "";

                            int cont = 0;
                            String rFinal = null;
                            for (DetalleResultadoFinal det : resul) {
                                cont++;
                                //first record
                                if (cont == 1) {
                                    //single record result
                                    if (det.getRespuesta() != null) {
                                        if (det.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                            Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(det.getValor()));
                                            rFinal = det.getRespuesta().getNombre() + ":" + " " + valor.getValor();

                                        } else {
                                            rFinal = det.getRespuesta().getNombre() + ":" + " " + det.getValor();
                                        }
                                    } else {
                                        if (det.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                            Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(det.getValor()));
                                            rFinal = det.getRespuestaExamen().getNombre() + ":" + " " + valor.getValor();

                                        } else {
                                            rFinal = det.getRespuestaExamen().getNombre() + ":" + " " + det.getValor();
                                        }
                                    }

                                    //no first record
                                } else {
                                    if (det.getRespuesta() != null) {
                                        if (det.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                            Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(det.getValor()));
                                            rFinal += ","+ " " +det.getRespuesta().getNombre() + ":" + " " + valor.getValor();

                                        } else {
                                            rFinal += ","+ " " + det.getRespuesta().getNombre() + ":" + " " + det.getValor();
                                        }
                                    } else {
                                        if (det.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                            Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(det.getValor()));
                                            rFinal += ","+ " " + det.getRespuestaExamen().getNombre() + ":" + " " + valor.getValor();

                                        } else {
                                            rFinal += ","+ " " + det.getRespuestaExamen().getNombre() + ":" + " " + det.getValor();
                                        }
                                    }
                                }

                            }

                            content[4] = rFinal;
                            reqList.add(content);


                            if (!ordenes.isEmpty()) {

                                String rExamen = null;
                                String fechaProcesamiento = "";
                                for (OrdenExamen ex : ordenes) {
                                    String[] examen = new String[3];
                                    List<DetalleResultado> results = resultadosService.getDetallesResultadoActivosByExamen(ex.getIdOrdenExamen());

                                    examen[0] = ex.getCodExamen() != null ? ex.getCodExamen().getNombre() : "";


                                    int cont1 = 0;
                                    //records tests results
                                    for (DetalleResultado resExamen : results) {
                                        cont1++;
                                        //first record
                                        if (cont1 == 1) {
                                            fechaProcesamiento = DateUtil.DateToString(resExamen.getFechahProcesa(), "dd/MM/yyyy hh:mm:ss a");
                                            if (resExamen.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                                Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(resExamen.getValor()));
                                                rExamen = resExamen.getRespuesta().getNombre() + ":" + " " + valor.getValor();

                                            } else {
                                                rExamen = resExamen.getRespuesta().getNombre() + ":" + " " + resExamen.getValor();
                                            }

                                        } else {
                                            if (resExamen.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                                Catalogo_Lista valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(Integer.valueOf(resExamen.getValor()));
                                                rExamen += "," + " " + resExamen.getRespuesta().getNombre() + ":" + " " + valor.getValor();

                                            } else {
                                                rExamen += "," + " " + resExamen.getRespuesta().getNombre() + ":" + " " + resExamen.getValor();
                                            }
                                        }

                                    }
                                    examen[1] = fechaProcesamiento;
                                    examen[2] = rExamen != null ? rExamen : "";
                                    dxList.add(examen);


                                }


                            }
                            float height1 = drawRequestTable(reqList, doc, page, y);
                            y -= height1;
                            float height2 = drawTestTable(dxList, doc, page, y);
                            y1 = y - height2;

                        }
                    }



                    if(y1==0){
                        y1 = 610;
                        GeneralUtils.drawObject(stream, doc, image2, 10, y1, 580, 140);
                        y1 += 150;

                    }else{
                        GeneralUtils.drawObject(stream, doc, image2, 10, y1 - 150, 580, 140);
                    }

                    y = y1 - 32;
                    if (uci != null) {
                        if (uci.equals("1")) {
                            x1 = x + 181;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                            if (diasUci != 0) {
                                x1 = x + 232;
                                GeneralUtils.drawTEXT(diasUci.toString(), y + 2, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                            }

                            if (ventilacion != null) {
                                if (ventilacion.equals("1")) {
                                    x1 = x + 338;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                } else {
                                    x1 = x + 362;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                }
                            }

                        } else {
                            x1 = x + 256;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                        }
                    }
                    if (codEgreso1 != null) {
                        x1 = x + 87;
                        //GeneralUtils.drawTEXT(codEgreso1, y - 7, x1, stream, 5, PDType1Font.TIMES_ROMAN);
                        x1 = x + 109;
                        GeneralUtils.drawTEXT(dxEgreso1, y - 7, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    }

                    if (codEgreso2 != null) {
                        x1 = x + 240;
                        //GeneralUtils.drawTEXT(codEgreso2, y - 7, x1, stream, 5, PDType1Font.TIMES_ROMAN);
                        x1 = x + 264;
                        GeneralUtils.drawTEXT(dxEgreso2, y - 7, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    }

                    x1 = x + 35;
                    GeneralUtils.drawTEXT(fechaEgreso, y - 17, x1, stream, 7, PDType1Font.TIMES_ROMAN);

                    if (condicionEgreso != null) {
                        switch (condicionEgreso) {
                            case "CONEGRE|ALTA":
                                x1 = x + 129;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 30, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                break;
                            case "CONEGRE|FUGA":
                                x1 = x + 206;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 30, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                break;
                            case "CONEGRE|REF":
                                x1 = x + 302;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 30, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                break;
                            case "CONEGRE|FALL":
                                x1 = x + 348;
                                GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 30, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                break;
                        }

                    }

                    y -= 57;
                    if (clasFinal != null) {
                        if (clasFinal.contains("CLASFI|INAD")) {
                            x1 = x + 76;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);
                        }

                        if (clasFinal.contains("CLASFI|NV")) {
                            x1 = x + 192;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                            if (nv != null) {
                                if (nv.equals("CLASFNV|CONF")) {
                                    x1 = x + 30;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 20, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                    if (etiologicoViral != null) {
                                        x1 = x + 112;
                                        GeneralUtils.drawTEXT(etiologicoViral, y - 18, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                                    }
                                }

                                if (nv.equals("CLASFNV|DESC")) {
                                    x1 = x + 28;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 26, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                }
                            }
                        }

                        if (clasFinal.contains("CLASFI|NB")) {
                            x1 = x + 133;
                            GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y, x1, stream, 7, PDType1Font.TIMES_BOLD);

                            if (nb != null) {
                                if (nb.equals("CLASFNB|CONF")) {
                                    x1 = x + 30;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 7, x1, stream, 7, PDType1Font.TIMES_BOLD);

                                    if (etiologicoViral != null) {
                                        x1 = x + 129;
                                        GeneralUtils.drawTEXT(etiologicoBacteriano, y - 5, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                                    }

                                    if (seroti != null) {
                                        x1 = x + 300;
                                        GeneralUtils.drawTEXT(seroti, y - 5, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                                    }
                                }

                                if (nb.equals("CLASFNB|DESC")) {
                                    x1 = x + 30;
                                    GeneralUtils.drawTEXT(messageSource.getMessage("lbl.x", null, null), y - 14, x1, stream, 7, PDType1Font.TIMES_BOLD);
                                }
                            }

                        }
                    }

                    y -= 54;

                    if (nombreUsuario != null) {
                        x1 = x + 75;
                        GeneralUtils.drawTEXT(nombreUsuario, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);
                    }
                    x1 = x + 282;
                    GeneralUtils.drawTEXT(fechaRegistro, y, x1, stream, 7, PDType1Font.TIMES_ROMAN);


                    //fecha impresi�n
                   /* GeneralUtils.drawTEXT(messageSource.getMessage("lbl.print.datetime", null, null), 100, 605, stream, 10, PDType1Font.HELVETICA_BOLD);
                    GeneralUtils.drawTEXT(fechaImpresion, 100, 900, stream, 10, PDType1Font.HELVETICA);*/

                    stream.close();

                    doc.save(output);
                    doc.close();
                    // generate the file
                    res = Base64.encodeBase64String(output.toByteArray());
                }


            }
        }

        return res;
    }

    private float drawRequestTable(List<String[]> reqList, PDDocument doc, PDPage page, float y) throws IOException {

        //drawRequestTable

        //Initialize table
        float height;
        float margin = 60;
        float tableWidth = 470;
        float bottomMargin = 45;
        BaseTable table = new BaseTable(y, y, bottomMargin, tableWidth, margin, doc, page, true, true);

        //Create Header row
        Row headerRow = table.createRow(10f);
        table.setHeader(headerRow);

        //Create 2 column row

        Cell cell;
        Row row;


        //Create Fact header row
        Row factHeaderrow = table.createRow(10f);
        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.request", null, null));
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);
        cell.setFillColor(Color.LIGHT_GRAY);

        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.send.request.date", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.sampling.datetime", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.sample.type", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.final.result", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        height = factHeaderrow.getHeight();

        //Add multiple rows with random facts about Belgium
        for (String[] fact : reqList) {

            row = table.createRow(10);


            for (String aFact : fact) {
                cell = row.createCell(20, aFact);
                cell.setFont(PDType1Font.TIMES_ROMAN);
                cell.setFontSize(7);

            }
            height += row.getHeight();
        }
        table.draw();
        return height;
    }


    private float drawTestTable(List<String[]> reqList, PDDocument doc, PDPage page, float y) throws IOException {

        //drawRequestTable

        //Initialize table
        float height = 0;
        float margin = 60;
        float tableWidth = 470;
        float bottomMargin = 45;
        BaseTable table = new BaseTable(y, y, bottomMargin, tableWidth, margin, doc, page, true, true);

        //Create Header row
        Row headerRow = table.createRow(10f);
        table.setHeader(headerRow);

        //Create 2 column row

        Cell cell;
        Row row;


        //Create Fact header row
        Row factHeaderrow = table.createRow(10f);
        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.test", null, null));
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);
        cell.setFillColor(Color.LIGHT_GRAY);

        cell = factHeaderrow.createCell(20, messageSource.getMessage("lbl.processing.datetime", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        cell = factHeaderrow.createCell(60, messageSource.getMessage("lbl.result", null, null));
        cell.setFillColor(Color.lightGray);
        cell.setFont(PDType1Font.TIMES_BOLD);
        cell.setFontSize(7);

        height = factHeaderrow.getHeight();


        //Add multiple rows with random facts about Belgium
        for (String[] fact : reqList) {
            row = table.createRow(10);


            for (int i = 0; i < fact.length; i++) {

                switch (i) {
                    case 2: {
                        cell = row.createCell(60, fact[i]);
                        cell.setFont(PDType1Font.TIMES_ROMAN);
                        cell.setFontSize(7);
                        break;
                    }
                    default: {
                        cell = row.createCell(20, fact[i]);
                        cell.setFont(PDType1Font.TIMES_ROMAN);
                        cell.setFontSize(7);
                    }

                }


            }
            height += row.getHeight();
        }
        table.draw();
        return height;
    }

    //Cargar lista de Vacunas
    @RequestMapping(value = "getVaccines", method = RequestMethod.GET)
    public @ResponseBody String getDepartmentAssociated(@RequestParam(value = "idNotificacion", required = true) String idNotificacion) {
        logger.info("Obteniendo las vacunas de una notificacion");
        List<DaVacunasIrag> vaccinesList = null;
        vaccinesList = daVacunasIragService.getAllVaccinesByIdIrag(idNotificacion);
        return vaccinesToJson(vaccinesList);
    }

    private String vaccinesToJson(List<DaVacunasIrag> vaccinesList){
        String jsonResponse="";
        Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
        Integer indice=0;
        for(DaVacunasIrag vac : vaccinesList){

            //tipos de vacunas
            String tipos = vac.getCodTipoVacuna();
            String[] tiposV = tipos.split(",");
            String nombre = null;

            int cont = 0;
            for (String nombreTipo : tiposV) {
                cont++;
                TipoVacuna tip = catalogoService.getTipoVacuna(nombreTipo);

                if(cont == 1){
                    nombre = tip.getValor();
                }else{
                    nombre += "," + " " + tip.getValor();
                }


            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("idVacuna", vac.getIdVacuna().toString());
            map.put("nombreVacuna", vac.getCodVacuna().getValor());
            map.put("tipoVacuna",nombre );
            map.put("dosis", vac.getDosis().toString());
            map.put("fechaUltDosis",DateUtil.DateToString(vac.getFechaUltimaDosis(),"dd/MM/yyyy"));
            mapResponse.put(indice, map);
            indice ++;
        }
        jsonResponse = new Gson().toJson(mapResponse);
        //escapar caracteres especiales, escape de los caracteres con valor num�rico mayor a 127
        UnicodeEscaper escaper     = UnicodeEscaper.above(127);
        return escaper.translate(jsonResponse);
    }


    @RequestMapping(value = "addVaccine", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected void addVaccine(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = "";
        String resultado = "";
        Integer dosis = 0;
        String fechaUltimaDosis = "";
        String codVacuna = null;
        JsonArray tVacHib = null;
        JsonArray tVacMenin = null;
        JsonArray tVacNeumo = null;
        JsonArray tVacFlu = null;
        String idNotificacion = null;
        String pasivo = null;
        Integer idVacuna = 0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
            json = br.readLine();
            //Recuperando Json enviado desde el cliente
            JsonObject jsonpObject = new Gson().fromJson(json, JsonObject.class);

            if(jsonpObject.get("dosis") != null && !jsonpObject.get("dosis").getAsString().isEmpty() ) {
                dosis = jsonpObject.get("dosis").getAsInt();
            }

            if(jsonpObject.get("idVacuna") != null && !jsonpObject.get("idVacuna").getAsString().isEmpty() ) {
                idVacuna = jsonpObject.get("idVacuna").getAsInt();
            }

            if (jsonpObject.get("fechaUltimaDosis") != null && !jsonpObject.get("fechaUltimaDosis").getAsString().isEmpty()) {
                fechaUltimaDosis = jsonpObject.get("fechaUltimaDosis").getAsString();
            }

            if (jsonpObject.get("codVacuna") != null && !jsonpObject.get("codVacuna").getAsString().isEmpty()) {
                codVacuna = jsonpObject.get("codVacuna").getAsString();
            }

            if (jsonpObject.get("pasivo") != null && !jsonpObject.get("pasivo").getAsString().isEmpty()) {
                pasivo = jsonpObject.get("pasivo").getAsString();
            }

            if (!jsonpObject.get("tVacHib").isJsonNull()) {
                tVacHib = jsonpObject.get("tVacHib").getAsJsonArray();
            }

            if (!jsonpObject.get("tVacMenin").isJsonNull()) {
                tVacMenin = jsonpObject.get("tVacMenin").getAsJsonArray();
            }

            if (!jsonpObject.get("tVacNeumo").isJsonNull()) {
                tVacNeumo = jsonpObject.get("tVacNeumo").getAsJsonArray();
            }

            if (!jsonpObject.get("tVacFlu").isJsonNull()) {
                tVacFlu = jsonpObject.get("tVacFlu").getAsJsonArray();
            }


            if (jsonpObject.get("idNotificacion") != null && !jsonpObject.get("idNotificacion").getAsString().isEmpty()) {
                idNotificacion = jsonpObject.get("idNotificacion").getAsString();
            }

            DaVacunasIrag vacunas = new DaVacunasIrag();

            if (idNotificacion != null) {
                //buscar si existe registrada la vacuna y el tipo de vacuna
                DaVacunasIrag vac = daVacunasIragService.searchVaccineRecord(idNotificacion, codVacuna);

                if (vac == null) {
                    vacunas.setIdNotificacion(daIragService.getFormById(idNotificacion));

                    long idUsuario = seguridadService.obtenerIdUsuario(request);
                    vacunas.setUsuario(usuarioService.getUsuarioById((int) idUsuario));

                    vacunas.setFechaRegistro(new Timestamp(new Date().getTime()));
                    vacunas.setCodVacuna(catalogoService.getVacuna(codVacuna));

                    if(tVacHib != null){
                        vacunas.setCodTipoVacuna(jsonArrayToString(tVacHib));
                    }

                    if(tVacMenin!= null){
                        vacunas.setCodTipoVacuna(jsonArrayToString(tVacMenin));
                    }

                    if(tVacNeumo != null){
                        vacunas.setCodTipoVacuna(jsonArrayToString(tVacNeumo));
                    }

                    if(tVacFlu!= null){
                        vacunas.setCodTipoVacuna(jsonArrayToString(tVacFlu));
                    }

                    vacunas.setDosis(dosis);
                    if (!fechaUltimaDosis.isEmpty()) {
                        vacunas.setFechaUltimaDosis(StringToDate(fechaUltimaDosis));
                    }

                    daVacunasIragService.addVaccine(vacunas);


                } else {
                    resultado = messageSource.getMessage("msg.existing.record.vaccine.error", null, null);
                    throw new Exception(resultado);

                }
            }else{
                DaVacunasIrag va = daVacunasIragService.getVaccineById(idVacuna);
                va.setPasivo(true);
                daVacunasIragService.updateVaccine(va);

            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            resultado = messageSource.getMessage("msg.add.vaccine.error", null, null);
            resultado = resultado + ". \n " + ex.getMessage();

        } finally {
            Map<String, String> map = new HashMap<String, String>();
            map.put("idNotificacion", idNotificacion);
            map.put("mensaje", resultado);
            map.put("dosis", dosis.toString());
            map.put("fechaUltimaDosis", fechaUltimaDosis);
            map.put("codVacuna", codVacuna);
            map.put("tVacHib", "");
            map.put("tVacMenin", "");
            map.put("tVacNeumo", "");
            map.put("tVacFlu", "");

            String jsonResponse = new Gson().toJson(map);
            response.getOutputStream().write(jsonResponse.getBytes());
            response.getOutputStream().close();
        }
    }

    public String jsonArrayToString(JsonArray jsonA){
        String text = null;
        for(int i = 0; i < jsonA.size(); i++){
            if(i == 0){
                text = String.valueOf(jsonA.get(i));

            }else{
                text += "," + String.valueOf(jsonA.get(i));
            }
        }

        text= text != null ? text.replaceAll("\"", "") : null;
        return  text;
    }

}
