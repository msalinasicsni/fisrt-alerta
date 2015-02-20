package ni.gob.minsa.alerta.web.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.persona.Ocupacion;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.Procedencia;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.Animales;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.EnfAgudas;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.EnfCronicas;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.FuenteAgua;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasCHIK;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDCSA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDGRA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasDSSA;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasHANT;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.SintomasLEPT;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.ComunidadesService;
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.OcupacionService;
import ni.gob.minsa.alerta.service.PersonaService;
import ni.gob.minsa.alerta.service.SeguridadService;
import ni.gob.minsa.alerta.service.SindFebrilService;
import ni.gob.minsa.alerta.service.UnidadesService;
import ni.gob.minsa.alerta.service.UsuarioService;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

/**
 * Controlador web de peticiones relacionadas a DaSindFebril
 * 
 * @author William Aviles
 */
@Controller
@RequestMapping("/febriles/*")
public class SindFebrilController {
	private static final Logger logger = LoggerFactory.getLogger(SindFebrilController.class);
	@Resource(name="sindFebrilService")
	private SindFebrilService sindFebrilService;
	@Resource(name="personaService")
	private PersonaService personaService;
	@Resource(name="divisionPoliticaService")
	private DivisionPoliticaService divisionPoliticaService;
	@Resource(name="entidadAdmonService")
	private EntidadAdmonService entidadAdmonService;
	@Resource(name="catalogosService")
	public CatalogoService catalogoService;
	@Resource(name="ocupacionService")
	public OcupacionService ocupacionService;
	@Resource(name="seguridadService")
	public SeguridadService seguridadService;
	@Resource(name = "unidadesService")
    public UnidadesService unidadesService;
	@Resource(name = "comunidadesService")
    public ComunidadesService comunidadesService;
	@Resource(name = "usuarioService")
	public UsuarioService usuarioService;
          
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
    public String initSearchForm(Model model, HttpServletRequest request) throws ParseException {
		logger.debug("Crear/Buscar una ficha de sindromes febriles");
		String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, false);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        if(urlValidacion.isEmpty()){
        	return "sindfeb/search";
        }else{
            return urlValidacion;
        }
	}
	
	/**
     * Custom handler for displaying persons reports or create a new one.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
	 * @throws Exception 
     */
    @RequestMapping("search/{idPerson}")
    public ModelAndView showPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
    	String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaciÃ³n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
        	List<DaSindFebril> results = sindFebrilService.getDaSindFebrilesPersona(idPerson);
            long idUsuario = seguridadService.obtenerIdUsuario(request);
	        if (results.size()==0){
	        	SisPersona persona = personaService.getPersona(idPerson);
	        	if (persona!=null){
		        	DaSindFebril daSindFeb = new DaSindFebril();
		        	daSindFeb.setIdNotificacion(new DaNotificacion());
		        	daSindFeb.getIdNotificacion().setPersona(persona);
		        	List<EntidadesAdtvas> entidades = null;

	                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
	                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
	                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
	                }else {
	                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
	                }
		        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
		        	List<Divisionpolitica> municipiosResi = null;
	        		List<Comunidades> comunidades = null;
		        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
		        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
		        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
		        	}
		        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
		        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
		        	List<Respuesta> catResp =catalogoService.getRespuesta();
		        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
		        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
		        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
		        	List<Animales> animales =catalogoService.getAnimales();
		        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
		        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
		        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
		        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
		        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
		        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
		        	mav.addObject("daSindFeb", daSindFeb);
		        	mav.addObject("entidades", entidades);
		        	mav.addObject("departamentos", departamentos);
		        	mav.addObject("municipiosResi", municipiosResi);
		        	mav.addObject("comunidades", comunidades);
		        	mav.addObject("catProcedencia", catProcedencia);
		        	mav.addObject("ocupaciones", ocupaciones);
		        	mav.addObject("catResp", catResp);
		        	mav.addObject("enfCronicas", enfCronicas);
		        	mav.addObject("enfAgudas", enfAgudas);
		        	mav.addObject("fuentesAgua", fuentesAgua);
		        	mav.addObject("animales", animales);
		        	mav.addObject("sintChik", sintChik);
		        	mav.addObject("sintDcsa", sintDcsa);
		        	mav.addObject("sintDgra", sintDgra);
		        	mav.addObject("sintDssa", sintDssa);
		        	mav.addObject("sintHant", sintHant);
		        	mav.addObject("sintLept", sintLept);
                    mav.addObject("autorizado",true);
		        	mav.setViewName("sindfeb/enterForm");
	        	}
	        	else{
	        		mav.setViewName("404");
	        	}
	        }
	        else{
                List<DaSindFebril> febrilAutorizados = new ArrayList<DaSindFebril>();
                for(DaSindFebril febril: results){
                    if (idUsuario != 0) {
                        if (seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, febril.getIdNotificacion().getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, febril.getIdNotificacion().getCodUnidadAtencion().getCodigo())) {
                            febrilAutorizados.add(febril);
                        }
                    }
                }
	        	mav.addObject("fichas", results);
                mav.addObject("fichasAutorizadas",febrilAutorizados);
	        	mav.setViewName("sindfeb/results");
	        }
        }else{
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
    public ModelAndView editReport(@PathVariable("idNotificacion") String idNotificacion, HttpServletRequest request) throws Exception {
    	String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        boolean autorizado = false;
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
	        DaSindFebril daSindFeb = sindFebrilService.getDaSindFebril(idNotificacion);
	        if (daSindFeb!=null){
	        	List<EntidadesAdtvas> entidades = null;
	        	long idUsuario = seguridadService.obtenerIdUsuario(request);
                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                    if (entidades.size()<=0){
                        entidades.add(daSindFeb.getIdNotificacion().getCodSilaisAtencion());
                    }
                }
                if (idUsuario != 0) {
                      autorizado = seguridadService.esUsuarioNivelCentral(idUsuario,ConstantsSecurity.SYSTEM_CODE) ||
                        seguridadService.esUsuarioAutorizadoEntidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo()) && seguridadService.esUsuarioAutorizadoUnidad((int) idUsuario, ConstantsSecurity.SYSTEM_CODE, daSindFeb.getIdNotificacion().getCodUnidadAtencion().getCodigo());

                }
                List<Divisionpolitica> munic = divisionPoliticaService.getMunicipiosBySilais(daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                List<Unidades> uni = null;
                if (seguridadService.esUsuarioNivelCentral(idUsuario,ConstantsSecurity.SYSTEM_CODE)) {
                     uni = unidadesService.getPUnitsHospByMuniAndSilais(daSindFeb.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(), HealthUnitType.UnidadesPrimHosp.getDiscriminator().split(","), daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo());
                }else{
                    uni = seguridadService.obtenerUnidadesPorUsuarioEntidadMunicipio((int)idUsuario,daSindFeb.getIdNotificacion().getCodSilaisAtencion().getCodigo(),daSindFeb.getIdNotificacion().getCodUnidadAtencion().getMunicipio().getCodigoNacional(),ConstantsSecurity.SYSTEM_CODE,HealthUnitType.UnidadesPrimHosp.getDiscriminator());
                    if (uni.size()<=0){
                        uni.add(daSindFeb.getIdNotificacion().getCodUnidadAtencion());
                    }
                }
	        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
	        	List<Divisionpolitica> municipiosResi = null;
        		List<Comunidades> comunidades = null;
	        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
	        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
	        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
	        	}
	        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
	        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
	        	List<Respuesta> catResp =catalogoService.getRespuesta();
	        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
	        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
	        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
	        	List<Animales> animales =catalogoService.getAnimales();
	        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
	        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
	        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
	        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
	        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
	        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
	        	mav.addObject("entidades", entidades);
	        	mav.addObject("munic", munic);
	        	mav.addObject("uni", uni);
	        	mav.addObject("departamentos", departamentos);
	        	mav.addObject("municipiosResi", municipiosResi);
	        	mav.addObject("comunidades", comunidades);
	        	mav.addObject("catProcedencia", catProcedencia);
	        	mav.addObject("ocupaciones", ocupaciones);
	        	mav.addObject("catResp", catResp);
	        	mav.addObject("enfCronicas", enfCronicas);
	        	mav.addObject("enfAgudas", enfAgudas);
	        	mav.addObject("fuentesAgua", fuentesAgua);
	        	mav.addObject("animales", animales);
	        	mav.addObject("sintChik", sintChik);
	        	mav.addObject("sintDcsa", sintDcsa);
	        	mav.addObject("sintDgra", sintDgra);
	        	mav.addObject("sintDssa", sintDssa);
	        	mav.addObject("sintHant", sintHant);
	        	mav.addObject("sintLept", sintLept);
	        	mav.addObject("daSindFeb", daSindFeb);
                mav.addObject("autorizado",autorizado);
                mav.setViewName("sindfeb/enterForm");
	        }
	        else{
	        	mav.setViewName("404");
	        }
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }
    
    @RequestMapping( value="save", method=RequestMethod.POST)
	public ResponseEntity<String> processCreationSindFebrilForm( @RequestParam(value="codSilaisAtencion", required=true ) Integer codSilaisAtencion
			, @RequestParam( value="codUnidadAtencion", required=true ) Integer codUnidadAtencion
			, @RequestParam( value="personaId", required=true ) long personaId
			, @RequestParam( value="idNotificacion", required=false, defaultValue="" ) String idNotificacion
			, @RequestParam( value="codExpediente", required=false) String codExpediente
			, @RequestParam( value="numFicha", required=false) String numFicha
			, @RequestParam( value="nombPadre", required=false) String nombPadre
			, @RequestParam( value="codProcedencia", required=false) String codProcedencia
			, @RequestParam( value="viaje", required=false) String viaje
			, @RequestParam( value="dondeViaje", required=false) String dondeViaje
			, @RequestParam( value="embarazo", required=false) String embarazo
			, @RequestParam( value="mesesEmbarazo", required=false) int mesesEmbarazo
			, @RequestParam( value="enfCronica", required=false) String enfCronica
			, @RequestParam( value="otraCronica", required=false) String otraCronica
			, @RequestParam( value="enfAgudaAdicional", required=false) String enfAgudaAdicional
			, @RequestParam( value="otraAgudaAdicional", required=false) String otraAgudaAdicional
			, @RequestParam( value="fuenteAgua", required=false) String fuenteAgua
			, @RequestParam( value="otraFuenteAgua", required=false) String otraFuenteAgua
			, @RequestParam( value="animales", required=false) String animales
			, @RequestParam( value="otrosAnimales", required=false) String otrosAnimales
			, @RequestParam( value="fechaTomaMuestra", required=false, defaultValue="") String fechaTomaMuestra
			, @RequestParam( value="temperatura", required=false) Float temperatura
			, @RequestParam( value="pas", required=false) Integer pas
			, @RequestParam( value="pad", required=false) Integer pad
			, @RequestParam( value="ssDSA", required=true) String ssDSA
			, @RequestParam( value="ssDCA", required=true) String ssDCA
			, @RequestParam( value="ssDS", required=true) String ssDS
			, @RequestParam( value="ssLepto", required=true) String ssLepto
			, @RequestParam( value="ssHV", required=true) String ssHV
			, @RequestParam( value="ssCK", required=true) String ssCK
			, @RequestParam( value="hosp", required=false) String hosp
			, @RequestParam( value="fechaIngreso", required=false, defaultValue="") String fechaIngreso
			, @RequestParam( value="fallecido", required=false) String fallecido
			, @RequestParam( value="fechaFallecido", required=false, defaultValue="") String fechaFallecido
			, @RequestParam( value="dxPresuntivo", required=true) String dxPresuntivo
			, @RequestParam( value="dxFinal", required=true) String dxFinal
			, @RequestParam( value="nombreLlenoFicha", required=true) String nombreLlenoFicha
			, @RequestParam( value="fechaFicha", required=true) String fechaFicha
			, @RequestParam( value="fechaInicioSintomas", required=true) String fechaInicioSintomas
            , @RequestParam(value = "municipioResidencia", required = false) String municipioResidencia
            , @RequestParam(value = "comunidadResidencia", required = false) String comunidadResidencia
            , @RequestParam(value = "direccionResidencia", required = false) String direccionResidencia
            , @RequestParam(value = "ocupacion", required = false) String ocupacion
                                                                 ,HttpServletRequest request) throws Exception
	{
    	DaSindFebril daSindFeb = new DaSindFebril();
    	DaNotificacion daNotificacion = new DaNotificacion();
    	daNotificacion.setPersona(personaService.getPersona(personaId));
        //antes actualizar a la persona
        InfoResultado infoResultado;
        try{
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT) {
                SisPersona pers = daNotificacion.getPersona();
                pers.setMunicipioResidencia(divisionPoliticaService.getDivisionPolitiacaByCodNacional(municipioResidencia));
                pers.setComunidadResidencia(comunidadesService.getComunidad(comunidadResidencia));
                pers.setDireccionResidencia(direccionResidencia);
                pers.setOcupacion(catalogoService.getOcupacion(ocupacion));
                Persona persona = personaService.ensamblarObjetoPersona(pers);

                personaService.iniciarTransaccion();

                infoResultado = personaService.guardarPersona(persona, seguridadService.obtenerNombreUsuario(request));
            }else{
                infoResultado = new InfoResultado();
                infoResultado.setOk(true);
                infoResultado.setObjeto(daNotificacion);
            }
            //si se actualizó la persona se registra la notificación
            if (infoResultado.isOk() && infoResultado.getObjeto() != null ){
                daNotificacion.setFechaRegistro(new Timestamp(new Date().getTime()));
                daNotificacion.setCodSilaisAtencion(entidadAdmonService.getSilaisByCodigo(codSilaisAtencion));
                daNotificacion.setCodUnidadAtencion(unidadesService.getUnidadByCodigo(codUnidadAtencion));
                long idUsuario = seguridadService.obtenerIdUsuario(request);
                daNotificacion.setUsuarioRegistro(usuarioService.getUsuarioById((int)idUsuario));
                //	daNotificacion.setUsuarioRegistro(usuarioService.getUsuarioById(1));
                daNotificacion.setCodTipoNotificacion(catalogoService.getTipoNotificacion("TPNOTI|SINFEB"));
                if (!idNotificacion.equals("")){
                    daNotificacion.setIdNotificacion(idNotificacion);
                }
                daSindFeb.setIdNotificacion(daNotificacion);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date dateFicha = formatter.parse(fechaFicha);
                daSindFeb.setFechaFicha(dateFicha);
                daSindFeb.setCodExpediente(codExpediente);
                daSindFeb.setNumFicha(numFicha);
                daSindFeb.setNombPadre(nombPadre);
                daSindFeb.setCodProcedencia(catalogoService.getProcedencia(codProcedencia));
                daSindFeb.setViaje(catalogoService.getRespuesta(viaje));
                daSindFeb.setDondeViaje(dondeViaje);
                daSindFeb.setEmbarazo(catalogoService.getRespuesta(embarazo));
                daSindFeb.setMesesEmbarazo(mesesEmbarazo);
                daSindFeb.setEnfCronica(enfCronica);
                daSindFeb.setOtraCronica(otraCronica);
                daSindFeb.setEnfAgudaAdicional(enfAgudaAdicional);
                daSindFeb.setOtraAgudaAdicional(otraAgudaAdicional);
                daSindFeb.setFuenteAgua(fuenteAgua);
                daSindFeb.setOtraFuenteAgua(otraFuenteAgua);
                daSindFeb.setAnimales(animales);
                daSindFeb.setOtrosAnimales(otrosAnimales);
                Date dateFIS = formatter.parse(fechaInicioSintomas);
                daSindFeb.setFechaInicioSintomas(dateFIS);
                if (!fechaTomaMuestra.equals("")){
                    Date dateFTM = formatter.parse(fechaTomaMuestra);
                    daSindFeb.setFechaTomaMuestra(dateFTM);
                }
                daSindFeb.setTemperatura(temperatura);
                daSindFeb.setPas(pas);
                daSindFeb.setPad(pad);
                daSindFeb.setSsCK(ssCK);
                daSindFeb.setSsDCA(ssDCA);
                daSindFeb.setSsDS(ssDS);
                daSindFeb.setSsDSA(ssDSA);
                daSindFeb.setSsHV(ssHV);
                daSindFeb.setSsLepto(ssLepto);
                daSindFeb.setHosp(catalogoService.getRespuesta(hosp));
                if (!fechaIngreso.equals("")){
                    Date dateIngreso = formatter.parse(fechaIngreso);
                    daSindFeb.setFechaIngreso(dateIngreso);
                }
                daSindFeb.setFallecido(catalogoService.getRespuesta(fallecido));
                if (!fechaFallecido.equals("")){
                    Date dateFallecido = formatter.parse(fechaFallecido);
                    daSindFeb.setFechaFallecido(dateFallecido);
                }
                daSindFeb.setDxPresuntivo(dxPresuntivo);
                daSindFeb.setDxFinal(dxFinal);
                daSindFeb.setNombreLlenoFicha(nombreLlenoFicha);
                sindFebrilService.saveSindFebril(daSindFeb);
            }else
                throw new Exception(infoResultado.getMensaje()+"----"+infoResultado.getMensajeDetalle());
            if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                personaService.commitTransaccion();
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
            try {
                if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                    personaService.rollbackTransaccion();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new Exception(ex);
        }finally {
            try {
                if (ConstantsSecurity.ENABLE_PERSON_COMPONENT)
                    personaService.remover();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    	return createJsonResponse(daSindFeb);
	}
    
    /**
     * Custom handler for voiding a notificacion.
     *
     * @param idNotificacion the ID of the chs to avoid
     * @return a String
     */
    @RequestMapping("/delete/{idNotificacion}")
    public String voidNoti(@PathVariable("idNotificacion") String idNotificacion, 
    		RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	String urlValidacion= "";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validación del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "redirect:/404";
        }
        if(urlValidacion.isEmpty()){
	        DaSindFebril daSindFeb = sindFebrilService.getDaSindFebril(idNotificacion);
	        if (daSindFeb!=null){
	        	daSindFeb.getIdNotificacion().setPasivo(true);
	        	sindFebrilService.saveSindFebril(daSindFeb);
	        	return "redirect:/febriles/search/"+daSindFeb.getIdNotificacion().getPersona().getPersonaId();
	        }
	        else{
	        	return "redirect:/404";
	        }
        }else{
        	return "redirect:/"+urlValidacion;
        }
    }
    
    /**
     * Custom handler for create a new report.
     *
     * @param idPerson the ID of the person
     * @return a ModelMap with the model attributes for the respective view
	 * @throws Exception 
     */
    @RequestMapping("new/{idPerson}")
    public ModelAndView newPersonReport(@PathVariable("idPerson") long idPerson, HttpServletRequest request) throws Exception {
    	String urlValidacion="";
        try {
            urlValidacion = seguridadService.validarLogin(request);
            //si la url esta vacia significa que la validaciÃ³n del login fue exitosa
            if (urlValidacion.isEmpty())
                urlValidacion = seguridadService.validarAutorizacionUsuario(request, ConstantsSecurity.SYSTEM_CODE, true);
        }catch (Exception e){
            e.printStackTrace();
            urlValidacion = "404";
        }
        ModelAndView mav = new ModelAndView();
        if(urlValidacion.isEmpty()){
        	SisPersona persona = personaService.getPersona(idPerson);
        	if (persona!=null){
	        	DaSindFebril daSindFeb = new DaSindFebril();
	        	daSindFeb.setIdNotificacion(new DaNotificacion());
	        	daSindFeb.getIdNotificacion().setPersona(persona);
	        	List<EntidadesAdtvas> entidades = null;
	        	long idUsuario = seguridadService.obtenerIdUsuario(request);
                //Si es usuario a nivel central se cargan todas las unidades asociados al SILAIS y municipio
                if(seguridadService.esUsuarioNivelCentral(idUsuario, ConstantsSecurity.SYSTEM_CODE)) {
                    entidades = entidadAdmonService.getAllEntidadesAdtvas();
                }else {
                    entidades = seguridadService.obtenerEntidadesPorUsuario((int) idUsuario, ConstantsSecurity.SYSTEM_CODE);
                }
	        	List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
	        	List<Divisionpolitica> municipiosResi = null;
        		List<Comunidades> comunidades = null;
	        	if(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia()!=null){
	        		municipiosResi = divisionPoliticaService.getMunicipiosFromDepartamento(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getDependencia().getCodigoNacional());
	        		comunidades = comunidadesService.getComunidades(daSindFeb.getIdNotificacion().getPersona().getMunicipioResidencia().getCodigoNacional());
	        	}
	        	List<Procedencia> catProcedencia = catalogoService.getProcedencia();
	        	List<Ocupacion> ocupaciones = ocupacionService.getAllOcupaciones();
	        	List<Respuesta> catResp =catalogoService.getRespuesta();
	        	List<EnfCronicas> enfCronicas =catalogoService.getEnfCronicas();
	        	List<EnfAgudas> enfAgudas =catalogoService.getEnfAgudas();
	        	List<FuenteAgua> fuentesAgua =catalogoService.getFuenteAgua();
	        	List<Animales> animales =catalogoService.getAnimales();
	        	List<SintomasCHIK> sintChik =catalogoService.getSintomasCHIK();
	        	List<SintomasDCSA> sintDcsa =catalogoService.getSintomasDCSA();
	        	List<SintomasDGRA> sintDgra =catalogoService.getSintomasDGRA();
	        	List<SintomasDSSA> sintDssa =catalogoService.getSintomasDSSA();
	        	List<SintomasHANT> sintHant =catalogoService.getSintomasHANT();
	        	List<SintomasLEPT> sintLept =catalogoService.getSintomasLEPT();
	        	mav.addObject("daSindFeb", daSindFeb);
	        	mav.addObject("entidades", entidades);
	        	mav.addObject("departamentos", departamentos);
	        	mav.addObject("municipiosResi", municipiosResi);
	        	mav.addObject("comunidades", comunidades);
	        	mav.addObject("catProcedencia", catProcedencia);
	        	mav.addObject("ocupaciones", ocupaciones);
	        	mav.addObject("catResp", catResp);
	        	mav.addObject("enfCronicas", enfCronicas);
	        	mav.addObject("enfAgudas", enfAgudas);
	        	mav.addObject("fuentesAgua", fuentesAgua);
	        	mav.addObject("animales", animales);
	        	mav.addObject("sintChik", sintChik);
	        	mav.addObject("sintDcsa", sintDcsa);
	        	mav.addObject("sintDgra", sintDgra);
	        	mav.addObject("sintDssa", sintDssa);
	        	mav.addObject("sintHant", sintHant);
	        	mav.addObject("sintLept", sintLept);
                mav.addObject("autorizado",true);
	        	mav.setViewName("sindfeb/enterForm");
        	}
        	else{
        		mav.setViewName("404");
        	}
        }else{
            mav.setViewName(urlValidacion);
        }
        return mav;
    }
    
    private ResponseEntity<String> createJsonResponse( Object o )
	{
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    Gson gson = new Gson();
	    String json = gson.toJson( o );
	    return new ResponseEntity<String>( json, headers, HttpStatus.CREATED );
	}

}
