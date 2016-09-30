package ni.gob.minsa.alerta.web.controllers;

import ni.gob.minsa.alerta.domain.agrupaciones.Grupo;
import ni.gob.minsa.alerta.domain.catalogos.Anios;
import ni.gob.minsa.alerta.domain.catalogos.AreaRep;
import ni.gob.minsa.alerta.domain.catalogos.Semanas;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.service.*;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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

    @Resource(name="admonPatoGroupService")
    private AdmonPatoGroupService admonPatoGroupService;

    @Resource(name="boletinService")
    private BoletinService boletinService;

    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String initAgeSexPage(Model model, HttpServletRequest request) throws Exception {
        logger.debug("presentar analisis por edad y sexo");
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
            long idUsuario = seguridadService.obtenerIdUsuario(request);
            List<EntidadesAdtvas> entidades = entidadAdmonService.getAllEntidadesAdtvas();
            List<Divisionpolitica> departamentos = divisionPoliticaService.getAllDepartamentos();
            List<Anios> anios = catalogosService.getAnios();
            List<Semanas> semanas = catalogosService.getSemanas();
            List<SivePatologias> patologias = sivePatologiasService.getSivePatologias();
            List<AreaRep> areas = seguridadService.getAreasUsuario((int)idUsuario,1);
            List<Grupo> grupos = admonPatoGroupService.getGrupos();
            model.addAttribute("areas", areas);
            model.addAttribute("semanas", semanas);
            model.addAttribute("anios", anios);
            model.addAttribute("entidades", entidades);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("patologias", patologias);
            model.addAttribute("grupos",grupos);
        return "analisis/boletin";
    }else{
            return  urlValidacion;
        }
    }

    /**
     *
     * @param codPato patologías a incluir en el boletin
     * @param codArea Pais,  Departamento o SILAIS
     * @param semF hasta que semana tomar en cuenta para consultar registros
     * @param anio anio a tomar en cuenta para consultar registros
     * @param codSilais Si es area SILAIS trae el id del SILAIS seleccionado
     * @param codDepartamento Si es area Departamento trae el id del Departamento seleccionado
     * @param codMunicipio sin uso
     * @param codUnidad sin uso
     * @return List
     * @throws ParseException
     */
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
