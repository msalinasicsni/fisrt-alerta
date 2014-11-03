package ni.gob.minsa.alerta.web.controllers;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.ModeloEncuesta;
import ni.gob.minsa.alerta.domain.vih.VihFicha;
import ni.gob.minsa.alerta.service.*;
//import ni.gob.minsa.alerta.utilities.typeAdapter.MaestroEncuestaTypeAdapter;
import ni.gob.minsa.alerta.utilities.typeAdapter.VihFichaTypeAdapter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 13/10/2014.
 */
@Controller
@RequestMapping("/vih")
public class VihFichaController {

    //Declaraciones

    private static final Logger logger = LoggerFactory.getLogger(entomologiaController.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

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
    @Qualifier(value = "vihFichaService")
    private VihFichaService vihFichaService;

    Map<String, Object> mapModel;
    List<Divisionpolitica> municipios;
    List<EntidadesAdtvas> silais;
    List<Divisionpolitica> departamentos;
    List<Unidades> unidadesSalud;

    /**
     * Método que inicaliza la pantalla de búsqueda de fichas vih
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView initSearchForm() throws Exception {
        logger.debug("Getting data to search fichas vih");
        List<VihFicha> fichasVih = new ArrayList<VihFicha>();
        ModelAndView mav = new ModelAndView("/vih/search");
        silais = silaisServce.getAllEntidadesAdtvas();
        fichasVih = vihFichaService.getAllFichasVih();
        mav.addObject("entidades", silais);
        mav.addObject("fichas", fichasVih);
        return  mav;
    }

    @RequestMapping(value = "busquedaFichas", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getFichasBusqueda(@RequestParam(value = "vihFiltros", required = true) String vihFiltros) throws Exception {
        logger.info("Obteniendo encuestas según filtros informados en JSON");
        //List<DaDatosEncuesta> datosEncuestas = new ArrayList<DaDatosEncuesta>();
        String result = "";
        List<VihFicha> fichas = new ArrayList<VihFicha>();
        try {
            if (vihFiltros!=null) {
                //Recuperando Json enviado desde el cliente
                JsonObject jsonpObject = new Gson().fromJson(vihFiltros, JsonObject.class);
                //String strModeloEncu = jsonpObject.get("codModeloEncu").getAsString();
                Integer codSilais = jsonpObject.get("codSilais").getAsInt();
                Integer codUnidadSalud = jsonpObject.get("codUnidadSalud").getAsInt();
                String codUsuario = jsonpObject.get("codUsuario").getAsString();
                //Integer anioEpi = jsonpObject.get("anioEpi").getAsInt();
                //Integer mesEpi = jsonpObject.get("mesEpi").getAsInt();

                fichas = vihFichaService.searchMaestroFichaByFiltros(codSilais, codUnidadSalud,codUsuario);
                final GsonBuilder gson = new GsonBuilder()
                        .registerTypeAdapter(VihFicha.class, new VihFichaTypeAdapter(sessionFactory))
                        .setPrettyPrinting()
                        .enableComplexMapKeySerialization()
                        .serializeNulls()
                        .setDateFormat(DateFormat.LONG)
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setVersion(1.0);

                final Gson gson1 = gson.create();
                result = gson1.toJson(fichas,ArrayList.class);
                //result = gson1.toJson(from(datosEncuestas).orderBy("getMes").orderBy("getAnio").orderBy("getSilais").
                //      orderBy("getUnidadDeSalud").orderBy("getDepartamento").orderBy("getMunicipio").orderBy("getOrdinal").all(), ArrayList.class);
            }
        }catch (Exception ex){
            logger.error(ex.getStackTrace().toString());
        }
        return result;
    }
}
