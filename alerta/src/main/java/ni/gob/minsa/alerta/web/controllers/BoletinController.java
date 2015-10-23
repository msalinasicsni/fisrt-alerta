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

import javax.annotation.Resource;
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

    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String initAgeSexPage(Model model) throws Exception {
        logger.debug("presentar analisis por edad y sexo");
        List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
        List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
        List<AreaRep> areas = catalogosService.getAreaRep();
        List<Anios> anios = catalogosService.getAnios();
        List<Semanas> semanas = catalogosService.getSemanas();
        List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
        model.addAttribute("areas", areas);
        model.addAttribute("semanas", semanas);
        model.addAttribute("anios", anios);
        model.addAttribute("entidades", entidades);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("patologias", patologias);
        return "analisis/boletin";
    }

}
