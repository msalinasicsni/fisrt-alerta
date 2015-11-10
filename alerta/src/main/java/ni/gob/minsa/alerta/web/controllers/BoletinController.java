package ni.gob.minsa.alerta.web.controllers;

import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souyen-ics.
 */
@Controller
@RequestMapping("boletin")
public class BoletinController {
    private static final Logger logger = LoggerFactory.getLogger(BoletinController.class);

    @Resource(name="entidadAdmonService")
    private EntidadAdmonService entidadAdmonService;

    @Resource(name="seguridadService")
    private SeguridadService seguridadService;

    @Resource(name="divisionPoliticaService")
    private DivisionPoliticaService divisionPoliticaService;

    @Resource(name="catalogosService")
    private CatalogoService catalogosService;

    @Resource(name="sivePatologiasService")
    private SivePatologiasService sivePatologiasService;


    @Resource(name="boletinService")
    private BoletinService boletinService;

    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String initAgeSexPage(Model model) throws Exception {
        logger.debug("presentar analisis por edad y sexo");
        List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
        List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
        List<Anios> anios = catalogosService.getAnios();
        List<Semanas> semanas = catalogosService.getSemanas();
        List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
        List<AreaRep> areas = new ArrayList<AreaRep>();
        AreaRep pais = catalogosService.getAreaRep("AREAREP|PAIS");
        AreaRep silais = catalogosService.getAreaRep("AREAREP|SILAIS");
        AreaRep dep = catalogosService.getAreaRep("AREAREP|DEPTO");
        areas.add(pais);
        areas.add(silais);
        areas.add(dep);
        model.addAttribute("areas", areas);
        model.addAttribute("semanas", semanas);
        model.addAttribute("anios", anios);
        model.addAttribute("entidades", entidades);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("patologias", patologias);
        return "analisis/boletin";
    }


    @RequestMapping(value = "dataBulletin", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List fetchCasosTasasAreaDataJson(@RequestParam(value = "codPato", required = true) String codPato,
                                            @RequestParam(value = "codArea", required = true) String codArea,
                                            @RequestParam(value = "semF", required = true) String semF,
                                            @RequestParam(value = "anio", required = true) Integer anio,
                                            @RequestParam(value = "codSilaisAtencion", required = false) Long codSilais,
                                            @RequestParam(value = "codDepartamento", required = false) Long codDepartamento,
                                            @RequestParam(value = "codMunicipio", required = false) Long codMunicipio,
                                            @RequestParam(value = "codUnidadAtencion", required = false) Long codUnidad) throws ParseException
    {
        logger.info("Obteniendo los datos de boletin en JSON");
        List datos = boletinService.getDataBulletin(codPato, codArea, codSilais, codDepartamento, semF, anio);
        if(datos == null)
            logger.debug("Nulo");
        return datos;
    }


}
