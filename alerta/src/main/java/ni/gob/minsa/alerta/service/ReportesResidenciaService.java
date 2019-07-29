package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.FiltroMx;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRSTICT on 10/15/2015.
 * V1.0
 */
@Service("reportesResidenciaService")
@Transactional
public class ReportesResidenciaService {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name="resultadoFinalService")
    private ResultadoFinalService resultadoFinalService;

    @Resource(name="respuestasExamenService")
    private RespuestasExamenService respuestasExamenService;

    private static final String sqlTipoNoti = " and noti.codTipoNotificacion.codigo = :tipoNoti ";
    private static  final String sqlFechas =  "and noti.fechaRegistro between :fechaInicio and :fechaFin ";
    private static final String sqlDataSemana = "select cal.noSemana ";
    private static final String sqlWhereSemana = " WHERE cal.anio = :anio and cal.noSemana between :semanaI and :semanaF order by cal.noSemana";
    private static final String sqlDataDia = "select  noti.fechaRegistro, count(noti.idNotificacion) as casos ";
    private static final String sqlDataSinR = "select distinct noti ";
    private static final String sqlRutina = " and dx.codDx.idDiagnostico = :idDx ";
    private static final String sqlFechasProcRut = " and dx.idSolicitudDx in (select r.solicitudDx.idSolicitudDx  from DetalleResultadoFinal r where r.pasivo = false and r.fechahRegistro between :fechaInicio and :fechaFin) "; //" and mx.fechaHTomaMx between :fechaInicio and :fechaFin ";

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones por area (casos y tasas)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango de fechas, factor tasas de población
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataCT(FiltrosReporte filtro){
        // Retrieve session from Hibernate
        List<Object[]> resTemp = new ArrayList<Object[]>();
        List<Object[]> resFinal = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;

        if (filtro.getCodArea().equals("AREAREP|PAIS")){

            if (filtro.isPorSilais()) {
                queryCasos = session.createQuery(" select ent.nombre, " +
                        "(select count(noti.idNotificacion) from DaNotificacion noti " +
                        "where noti.municipioResidencia.dependenciaSilais.codigo =  ent.codigo " +
                        " and noti.municipioResidencia is not null " +
                        " and noti.pasivo = false " +
                        "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                        "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                        "(select sum(pob.total)" +
                        "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId = ent.entidadAdtvaId " +
                        "and pob.grupo =:tipoPob " +
                        "and (pob.anio =:anio)) " +
                        "FROM EntidadesAdtvas ent " +
                        " where ent.pasivo = 0 order by ent.nombre");
            }else{
                //Por Departamento
                queryCasos = session.createQuery(" select divi.nombre, " +
                        "(select count(noti.idNotificacion) from DaNotificacion noti " +
                        "where noti.codTipoNotificacion.codigo = :tipoNoti " +
                        " and noti.municipioResidencia is not null " +
                        " and noti.pasivo = false " +
                        "and noti.municipioResidencia.dependencia.divisionpoliticaId = divi.divisionpoliticaId  " +
                        "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                        "(Select sum(pob.total) as total " +
                        "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =divi.divisionpoliticaId " +
                        "and pob.grupo =:tipoPob " +
                        "and (pob.anio =:anio)) " +
                        "FROM Divisionpolitica divi " +
                        "where divi.dependencia is null and  divi.pasivo = '0' order by divi.nombre");
            }
        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){

            queryCasos = session.createQuery(" select distinct divi.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.municipioResidencia.divisionpoliticaId = divi.divisionpoliticaId  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                    "(select sum(pob.total) " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId = divi.divisionpoliticaId " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio)) " +
                    "FROM Divisionpolitica divi, Unidades as uni " +
                    "where divi.pasivo = '0' and uni.pasivo='0' " +
                    "and uni.municipio.codigoNacional = divi.codigoNacional and uni.entidadAdtva.entidadAdtvaId = :codSilais ");

            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(" select divi.nombre, " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    " and noti.municipioResidencia is not null " +
                    " and noti.municipioResidencia.divisionpoliticaId = divi.divisionpoliticaId  " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                    " (Select sum(pob.total) as total " +
                    " from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =divi.divisionpoliticaId " +
                    " and pob.grupo =:tipoPob " +
                    " and (pob.anio =:anio)) " +
                    " FROM Divisionpolitica divi " +
                    " where divi.dependencia.divisionpoliticaId = :codDepartamento ");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }

        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoPob","Todos");
        queryCasos.setParameter("anio", Integer.valueOf(filtro.getAnioInicial()));


        resTemp.addAll(queryCasos.list());

        Long poblacion;

        for (Object[] reg : resTemp) {
            Object[] reg1 = new Object[3];
            reg1[0] = reg[0];
            reg1[1] = reg[1];
            poblacion = (Long)reg[2];
            if(poblacion != null){
                reg1[2] = ((Long) reg[1] != 0 ? ((double) Math.round((Integer.valueOf(reg[1].toString()).doubleValue()) / poblacion * filtro.getFactor() * 100) / 100) : 0);
            }else{
                reg1[2] = "NP";
            }
            resFinal.add(reg1);
        }

        return resFinal;
    }

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones por sexo (femenino, masculino, indeterminado : casos y tasas)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango de fechas, factor tasas de población
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataSexReport(FiltrosReporte filtro){
        // Retrieve session from Hibernate
        List<Object[]> resTemp = new ArrayList<Object[]>();
        List<Object[]> resFinal = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        Query queryPoblacion = null;


        if (filtro.getCodArea().equals("AREAREP|PAIS")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependencia is null " +
                    "and pob.grupo =:tipoPob " +
                    "and pob.anio =:anio " +
                    "group by pob.anio order by pob.anio");

            queryCasos = session.createQuery(" select sex.valor, coalesce((select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and per.sexo.codigo = sex.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " and noti.municipioResidencia is not null " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codSilais", filtro.getCodSilais());


            queryCasos = session.createQuery(" select sex.valor, coalesce((select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and per.sexo.codigo = sex.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "and noti.municipioResidencia.dependenciaSilais.codigo = :codSilais  " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.municipioResidencia.dependenciaSilais.codigo = :codSilais " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codDepartamento", filtro.getCodDepartamento());

            queryCasos = session.createQuery(" select sex.valor, coalesce((select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and per.sexo.codigo = sex.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.municipioResidencia is not null " +
                    "and noti.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );

            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }

        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryPoblacion.setParameter("tipoPob","Todos");
        queryPoblacion.setParameter("anio", Integer.valueOf(filtro.getAnioInicial()));


        resTemp.addAll(queryCasos.list());

        Long poblacion = (Long) queryPoblacion.uniqueResult();

        for (Object[] reg : resTemp) {
            Object[] reg1 = new Object[4];
            reg1[0] = reg[0];
            reg1[1] = reg[1];
            reg1[2] = ((Long) reg[1] != 0 ? (double) Math.round(Integer.valueOf(reg[1].toString()).doubleValue() / Integer.valueOf(reg[2].toString()).doubleValue() * 100 * 100) / 100 : 0);

            if(poblacion != null){
                reg1[3] = ((Long) reg[1] != 0 ? ((double) Math.round((Integer.valueOf(reg[1].toString()).doubleValue()) / poblacion * filtro.getFactor() * 100) / 100) : 0);

            }else{
                reg1[3] = "NP";
            }
            resFinal.add(reg1);
        }

        return resFinal;
    }

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones por tipo de resultado (positivo, negativo, sin resultado y % positividad)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango de fechas, factor tasas de población
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataResultReport(FiltrosReporte filtro) {
        // Retrieve session from Hibernate
        List<Object[]> resTemp1 = new ArrayList<Object[]>();
        List<Object[]> resTemp2 = new ArrayList<Object[]>();

        List<Object[]> resFinal = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query queryNotiDx = null;
        Query queryIdNoti = null;
        String queryNotiEstudios = "";


        if (filtro.getCodArea().equals("AREAREP|PAIS")) {

            if (filtro.isPorSilais()) {
                queryNotiDx = session.createQuery(" select ent.codigo, ent.nombre, " +
                        " coalesce( " + //TOTAL NOTIFICACIONES
                        " (select count(noti.idNotificacion) from DaNotificacion noti " +
                        " where noti.municipioResidencia is not null and noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo and noti.pasivo = false " +
                        sqlTipoNoti + sqlFechas +
                        " group by noti.municipioResidencia.dependenciaSilais.codigo),0) as noti, " + //TOTAL RUTINAS
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion and noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                        sqlTipoNoti + sqlFechas +
                        " group by noti.municipioResidencia.dependenciaSilais.codigo) as dx, " +
                        " coalesce( " + //TOTAL RUTINAS CON RESULTADO
                        " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        sqlTipoNoti + sqlFechas +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as conresultado, " +
                        " coalesce( " + //TOTAL RUTINAS SIN RESULTADO
                        " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        sqlTipoNoti + sqlFechas +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as sinresultado, " + //TOTAL ESTUDIOS
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion and noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx and noti.pasivo = false and est.anulado = false and mx.anulada = false " +
                        sqlTipoNoti + sqlFechas +
                        " group by noti.municipioResidencia.dependenciaSilais.codigo) as est, " +
                        " coalesce( " + //TOTAL ESTUDIOS CON RESULTADO
                        " (select sum(case est.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        sqlTipoNoti + sqlFechas +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false),0) as est_conresultado, " +
                        " coalesce( " + //TOTAL ESTUDIOS SIN RESULTADO
                        " (select  sum(case est.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        sqlTipoNoti + sqlFechas +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false),0) as est_sinresultado " +
                        " from EntidadesAdtvas ent " +
                        " where ent.pasivo = 0 " +
                        " order by ent.codigo ");

                queryIdNoti = session.createQuery(" select noti.municipioResidencia.dependenciaSilais.codigo, dx.idSolicitudDx " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.idNotificacion = mx.idNotificacion " +
                        sqlTipoNoti +sqlFechas +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false " +
                        " and mx.anulada = false " +
                        " and dx.anulado = false " +
                        " and dx.aprobada = true and dx.controlCalidad = false " +
                        " and noti.municipioResidencia is not null " +
                        " order by noti.municipioResidencia.dependenciaSilais.codigo");

                queryNotiEstudios = " select noti.municipioResidencia.dependenciaSilais.codigo, est.idSolicitudEstudio " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.idNotificacion = mx.idNotificacion " +
                        sqlTipoNoti +sqlFechas +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false " +
                        " and est.aprobada = true " +
                        " and noti.municipioResidencia is not null " +
                        " order by noti.municipioResidencia.dependenciaSilais.codigo ";

            }else{
                queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                        " coalesce( " +
                        " (select count(noti.idNotificacion) from DaNotificacion noti " +
                        " where noti.municipioResidencia is not null and noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId" +
                        " and noti.pasivo = false " +
                        sqlTipoNoti +sqlFechas +
                        " group by noti.municipioResidencia.dependencia.divisionpoliticaId),0) as noti, " +
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                        " and noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                        sqlTipoNoti +sqlFechas +
                        " group by noti.municipioResidencia.dependencia.divisionpoliticaId) as dx, " +
                        " coalesce( " +
                        " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        sqlTipoNoti +sqlFechas +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as conresultado, " +
                        " coalesce( " +
                        " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        sqlTipoNoti +sqlFechas +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as sinresultado, " +
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                        " and noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx and noti.pasivo = false and est.anulado = false and mx.anulada = false " +
                        sqlTipoNoti +sqlFechas +
                        " group by noti.municipioResidencia.dependencia.divisionpoliticaId) as estudios, " +
                        " coalesce( " +
                        " (select sum(case est.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        sqlTipoNoti +sqlFechas +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false),0) as est_conresultado, " +
                        " coalesce( " +
                        " (select  sum(case est.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        sqlTipoNoti +sqlFechas +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false),0) as est_sinresultado " +
                        " from Divisionpolitica div " +
                        "where div.dependencia is null and div.pasivo = '0'" +
                        " order by div.divisionpoliticaId ");

                queryIdNoti = session.createQuery(" select noti.municipioResidencia.dependencia.divisionpoliticaId, dx.idSolicitudDx " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.idNotificacion = mx.idNotificacion " +
                        sqlTipoNoti +sqlFechas +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false " +
                        " and dx.anulado = false " +
                        " and mx.anulada = false " +
                        " and dx.aprobada = true and dx.controlCalidad = false " +
                        " and noti.municipioResidencia is not null " +
                        " order by noti.municipioResidencia.dependencia.divisionpoliticaId");

                queryNotiEstudios = " select noti.municipioResidencia.dependencia.divisionpoliticaId, est.idSolicitudEstudio " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                        " where noti.idNotificacion = mx.idNotificacion " +
                        sqlTipoNoti +sqlFechas +
                        " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false and est.anulado = false " +
                        " and mx.anulada = false " +
                        " and est.aprobada = true " +
                        " and noti.municipioResidencia is not null " +
                        " order by noti.municipioResidencia.dependencia.divisionpoliticaId ";
            }

            //rutinas
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            resTemp2.addAll(queryIdNoti.list());

            //se agregan estudios
            queryIdNoti = session.createQuery(queryNotiEstudios);
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            resTemp2.addAll(queryIdNoti.list());

        } else if (filtro.getCodArea().equals("AREAREP|SILAIS")) {

            queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId" +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId), " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as sinresultado, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx and noti.pasivo = false and est.anulado = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId) as estudios, " +
                    " coalesce( " +
                    " (select sum(case est.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false),0) as est_conresultado, " +
                    " coalesce( " +
                    " (select  sum(case est.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false),0) as est_sinresultado " +
                    " from Divisionpolitica div " +
                    "where div.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.municipioResidencia.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true and dx.anulado = false and dx.controlCalidad = false " +
                    " and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by noti.municipioResidencia.divisionpoliticaId ");

            queryNotiEstudios = "select noti.municipioResidencia.divisionpoliticaId, est.idSolicitudEstudio " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false " +
                    " and est.aprobada = true " +
                    " and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by noti.municipioResidencia.divisionpoliticaId ";

            queryNotiDx.setParameter("codSilais", filtro.getCodSilais());

            //rutinas
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codSilais", filtro.getCodSilais());
            resTemp2.addAll(queryIdNoti.list());

            //se agregan estudios
            queryIdNoti = session.createQuery(queryNotiEstudios);
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codSilais", filtro.getCodSilais());
            resTemp2.addAll(queryIdNoti.list());

        } else if (filtro.getCodArea().equals("AREAREP|DEPTO")) {

            queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId" +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as sinresultado, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx and noti.pasivo = false and est.anulado = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.municipioResidencia.divisionpoliticaId) as estudios, " +
                    " coalesce( " +
                    " (select sum(case est.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false),0) as est_conresultado, " +
                    " coalesce( " +
                    " (select  sum(case est.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false),0) as est_sinresultado " +
                    " from Divisionpolitica div " +
                    "where div.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.municipioResidencia.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false and dx.anulado = false " +
                    " and dx.aprobada = true and dx.controlCalidad = false " +
                    " and noti.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by noti.municipioResidencia.divisionpoliticaId ");

            queryNotiEstudios = " select noti.municipioResidencia.divisionpoliticaId, est.idSolicitudEstudio " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudEstudio est " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = est.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false and est.anulado = false " +
                    " and mx.anulada = false " +
                    " and est.aprobada = true " +
                    " and noti.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by noti.municipioResidencia.divisionpoliticaId ";

            queryNotiDx.setParameter("codDepartamento", filtro.getCodDepartamento());

            //rutinas
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codDepartamento", filtro.getCodDepartamento());
            resTemp2.addAll(queryIdNoti.list());

            //se agregan estudios
            queryIdNoti = session.createQuery(queryNotiEstudios);
            queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codDepartamento", filtro.getCodDepartamento());
            resTemp2.addAll(queryIdNoti.list());

        }

        queryNotiDx.setParameter("tipoNoti", filtro.getTipoNotificacion());
        //queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryNotiDx.setParameter("fechaInicio", filtro.getFechaInicio());
        queryNotiDx.setParameter("fechaFin", filtro.getFechaFin());
        //queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
        //queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());


        resTemp1.addAll(queryNotiDx.list());
        //resTemp2.addAll(queryIdNoti.list());

        for (Object[] reg : resTemp1) {
            Object[] reg1 = new Object[8];
            reg1[0] = reg[1]; //Nombre Silais
            reg1[1] = reg[2]; //Cantidad Notificaciones
            reg1[2] = (Long)reg[3] + (Long)reg[6]; //Cantidad Dx + cantidad estudios

            int pos = 0;
            int neg = 0;
            int inadecuada = 0;
            for (Object[] sol : resTemp2) {
                if (sol[0].equals(reg[0])) {

                    List<DetalleResultadoFinal> finalRes = resultadoFinalService.getDetResActivosBySolicitud(sol[1].toString());
                    for (DetalleResultadoFinal res : finalRes) {
                        if (res.getRespuesta() != null) {
                            if (res.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                Integer idLista = Integer.valueOf(res.getValor());
                                Catalogo_Lista valor = null;
                                try {
                                    valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(idLista);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (valor != null) {
                                    if (valor.getValor().trim().toLowerCase().equals("negativo")
                                            || valor.getValor().trim().toLowerCase().contains("no reactor")
                                            || valor.getValor().trim().toLowerCase().contains("no detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                        neg++;
                                        break;
                                    }else if (valor.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                        inadecuada++;
                                    }else if (valor.getValor().trim().toLowerCase().equals("positivo")
                                            || valor.getValor().trim().toLowerCase().contains("reactor")
                                            || valor.getValor().trim().toLowerCase().contains("detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-DET")
                                            || (!valor.getValor().trim().toLowerCase().contains("negativo") && !valor.getValor().trim().toLowerCase().contains("indetermin"))) {
                                        pos++;
                                        break;
                                    }
                                }


                            } else if (res.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().trim().toLowerCase().equals("negativo")
                                        || res.getValor().trim().toLowerCase().contains("no reactor")
                                        || res.getValor().trim().toLowerCase().contains("no detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                    neg++;
                                    break;
                                }else if (res.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                    inadecuada++;
                                }else if (res.getValor().trim().toLowerCase().equals("positivo")
                                        || res.getValor().trim().toLowerCase().contains("reactor")
                                        || res.getValor().trim().toLowerCase().contains("detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-DET")
                                        || (!res.getValor().trim().toLowerCase().contains("negativo") && !res.getValor().trim().toLowerCase().contains("indetermin"))) {
                                    pos++;
                                    break;
                                }

                            }

                        } else if (res.getRespuestaExamen() != null) {
                            if (res.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                Integer idLista = Integer.valueOf(res.getValor());
                                Catalogo_Lista valor = null;
                                try {
                                    valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(idLista);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (valor != null) {
                                    if (valor.getValor().trim().toLowerCase().equals("negativo")
                                            || valor.getValor().trim().toLowerCase().contains("no reactor")
                                            || valor.getValor().trim().toLowerCase().contains("no detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                        neg++;
                                        break;
                                    }else if (valor.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                        inadecuada++;
                                    }else if (valor.getValor().trim().toLowerCase().equals("positivo")
                                            || valor.getValor().trim().toLowerCase().contains("reactor")
                                            || valor.getValor().trim().toLowerCase().contains("detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-DET")
                                            || (!valor.getValor().trim().toLowerCase().contains("negativo") && !valor.getValor().trim().toLowerCase().contains("indetermin"))) {
                                        pos++;
                                        break;
                                    }
                                }

                            } else if (res.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().trim().toLowerCase().equals("negativo")
                                        || res.getValor().trim().toLowerCase().contains("no reactor")
                                        || res.getValor().trim().toLowerCase().contains("no detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                    neg++;
                                    break;
                                }else if (res.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                    inadecuada++;
                                }else if (res.getValor().trim().toLowerCase().equals("positivo")
                                        || res.getValor().trim().toLowerCase().contains("reactor")
                                        || res.getValor().trim().toLowerCase().contains("detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-DET")
                                        || (!res.getValor().toString().trim().toLowerCase().contains("negativo") && !res.getValor().trim().toLowerCase().contains("indetermin"))) {
                                    pos++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }


            reg1[3] = pos; // Positivo
            reg1[4] = neg; // Negativo
            reg1[5] = (Long)reg[5]+(Long)reg[8]; // Sin Resultado dx + sin resultado est
            Long totalConResultado = (Long) reg[4] +(Long) reg[7];
            reg1[6] = (totalConResultado != 0 ? (double) Math.round(Integer.valueOf(reg1[3].toString()).doubleValue() / Integer.valueOf(totalConResultado.toString()).doubleValue() * 100 * 100) / 100 : 0);
            reg1[7] = inadecuada; //muestras inadecuadas
            resFinal.add(reg1);

        }
        return resFinal;
    }

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones por semanas (casos y tasas)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango semanas, año, factor tasas de población
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataPorSemana(FiltrosReporte filtro){
        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        Query queryPoblacion = null;
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object[]> resultadoFinal = new ArrayList<Object[]>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.municipioResidencia is not null and noti.pasivo = false and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependencia is null " +
                    "and pob.grupo =:tipoPob " +
                    "and pob.anio =:anio " +
                    "group by pob.anio order by pob.anio");
        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.pasivo = '0' " +
                    //"pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais " +
                    "and pob.grupo =:tipoPob " +
                    "and pob.divpol.codigoNacional in (select distinct uni.municipio.codigoNacional from Unidades as uni where uni.pasivo='0' and uni.entidadAdtva.entidadAdtvaId = :codSilais) " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codSilais", filtro.getCodSilais());
        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and noti.municipioResidencia.dependencia.divisionpoliticaId =:codDepartamento and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codDepartamento", filtro.getCodDepartamento());
        }

        queryCasos.setParameter("anio", Integer.valueOf(filtro.getAnioInicial()));
        queryCasos.setParameter("semanaI", Integer.valueOf(filtro.getSemInicial()));
        queryCasos.setParameter("semanaF", Integer.valueOf(filtro.getSemFinal()));
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryPoblacion.setParameter("anio", Integer.valueOf(filtro.getAnioInicial()));
        queryPoblacion.setParameter("tipoPob", filtro.getTipoPoblacion());


        Long poblacion = (Long) queryPoblacion.uniqueResult();

        resultadoTemp.addAll(queryCasos.list());
        for (Object[] semana : resultadoTemp){
            Object[] registroseman = new Object[3];
            registroseman[0] = semana[0];
            registroseman[1] = semana[1];
            if (poblacion !=null) {
                registroseman[2] = ((Long) semana[1] != 0 ? ((double) Math.round((Integer.valueOf(semana[1].toString()).doubleValue()) / poblacion * filtro.getFactor() * 100) / 100) : 0);
            }else{
                registroseman[2] = "NP";
            }
            resultadoFinal.add(registroseman);
        }
        return resultadoFinal;
    }

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones por dia (serie temporal casos)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango de fechas, rango semanas, factor tasas de población
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataPorDia(FiltrosReporte filtro) throws ParseException {

        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object[]> resultadoFinal = new ArrayList<Object[]>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.municipioResidencia is not null and noti.pasivo = false and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codSilais", filtro.getCodSilais());
        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.municipioResidencia.dependencia.divisionpoliticaId =:codDepartamento and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }

        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        resultadoTemp = queryCasos.list();
        Date ultimaFecha = null;
        Long cantidadCasos = 0L;
        for(Object[] registro : resultadoTemp ){
            Object[] registroseman = new Object[2];
            //fecha de registro sin hora
            Date fechaCompara = DateUtil.StringToDate(DateUtil.DateToString((Date) registro[0], "dd/MM/yyyy"), "dd/MM/yyyy");
            if (ultimaFecha!=null) {
                if (ultimaFecha.compareTo(fechaCompara)==0){//fechas son iguales, entonces sumar casos
                    cantidadCasos+= (Long) registro[1];
                    //es el´último registro, se debe agregar
                    if (Arrays.equals(resultadoTemp.get(resultadoTemp.size() - 1), registro)){
                        registroseman[0] = ultimaFecha;
                        registroseman[1] = cantidadCasos;
                        resultadoFinal.add(registroseman);
                    }
                }else{// temporales se actualizan con datos nuevo registro, se indica que es necesario agregar el registro anterior
                    registroseman[0] = ultimaFecha;
                    registroseman[1] = cantidadCasos;
                    ultimaFecha = fechaCompara;
                    cantidadCasos = (Long) registro[1];
                    resultadoFinal.add(registroseman);
                    //no son iguales, pero es el último registro, agregarlo también
                    if (Arrays.equals(resultadoTemp.get(resultadoTemp.size()-1), registro)){
                        registroseman = new Object[2];
                        registroseman[0] = fechaCompara;
                        registroseman[1] = registro[1];
                        resultadoFinal.add(registroseman);
                    }
                }
            }else{//primer registro
                ultimaFecha = fechaCompara;
                cantidadCasos = (Long) registro[1];
                //sólo es un registro
                if (resultadoTemp.size()==1){
                    registroseman[0] = ultimaFecha;
                    registroseman[1] = cantidadCasos;
                    resultadoFinal.add(registroseman);
                }
            }
        }
        return resultadoFinal;
    }

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones sin resultado
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificación, rango de fechas
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<DaNotificacion> getDataSinResultado(FiltrosReporte filtro) throws ParseException {

        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        String sqlCasosEstudios= "";
        List<DaNotificacion> resultadoTemp = new ArrayList<DaNotificacion>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.anulado = false and dx.aprobada = false and mx.anulada = false and dx.controlCalidad = false " +
                    " and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

             sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false  " +
                    " and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.controlCalidad = false and dx.idTomaMx.anulada = false "+
                     " and dx.idTomaMx.idNotificacion.codTipoNotificacion.codigo = :tipoNoti " +
                     " and dx.idTomaMx.idNotificacion.fechaRegistro between :fechaInicio and :fechaFin )" +
                    " order by noti.fechaRegistro asc";

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.anulado = false and dx.aprobada = false and mx.anulada = false and dx.controlCalidad = false " +
                    "and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false  " +
                    "and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.controlCalidad = false and dx.idTomaMx.anulada = false "+
                    " and dx.idTomaMx.idNotificacion.persona.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais and dx.idTomaMx.idNotificacion.codTipoNotificacion.codigo = :tipoNoti " +
                    " and dx.idTomaMx.idNotificacion.fechaRegistro between :fechaInicio and :fechaFin )" +
                    " order by noti.fechaRegistro asc";

            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.anulado = false and dx.aprobada = false and mx.anulada = false and dx.controlCalidad = false " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.municipioResidencia.dependencia.divisionpoliticaId =:codDepartamento" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false  " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.municipioResidencia.dependencia.divisionpoliticaId =:codDepartamento" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.controlCalidad = false and dx.idTomaMx.anulada = false "+
                    " and dx.idTomaMx.idNotificacion.persona.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento and dx.idTomaMx.idNotificacion.codTipoNotificacion.codigo = :tipoNoti " +
                    " and dx.idTomaMx.idNotificacion.fechaRegistro between :fechaInicio and :fechaFin )" +
                    " order by noti.fechaRegistro asc";

            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

        }

        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());

        resultadoTemp = queryCasos.list();

        //estudios
        queryCasos = session.createQuery(sqlCasosEstudios);
        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos.setParameter("codSilais", filtro.getCodSilais());
        }else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());
        }else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());
        }
        resultadoTemp.addAll(queryCasos.list());

        return resultadoTemp;
    }

    @SuppressWarnings("unchecked")
    public List<DaSolicitudDx> getPositiveRoutineRequestByFilter(FiltroMx filtro) throws UnsupportedEncodingException {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(DaSolicitudDx.class, "rutina");
        crit.createAlias("rutina.idTomaMx", "toma");
        crit.createAlias("toma.idNotificacion", "notif");
        crit.createAlias("rutina.codDx", "dx");

        if(filtro.getNombreSolicitud()!= null){
            filtro.setNombreSolicitud(URLDecoder.decode(filtro.getNombreSolicitud(), "utf-8"));
        }

        //se filtra por SILAIS
        if (filtro.getCodSilais()!=null){
            crit.createAlias("notif.municipioResidencia","muni");
            crit.add( Restrictions.and(
                            Restrictions.eq("muni.dependenciaSilais.codigo", Long.valueOf(filtro.getCodSilais())))
            );
        }

        //Se filtra por rango de fecha de toma de muestra
        if (filtro.getFechaInicioTomaMx()!=null && filtro.getFechaFinTomaMx()!=null){
            crit.add( Restrictions.and(
                            Restrictions.between("toma.fechaHTomaMx", filtro.getFechaInicioTomaMx(),filtro.getFechaFinTomaMx()))
            );
        }

        //nombre solicitud
        if (filtro.getNombreSolicitud() != null) {
            crit.add(Restrictions.ilike("dx.nombre", "%" + filtro.getNombreSolicitud() + "%"));
        }

        //filtro de resultados finales aprobados
        crit.add(Restrictions.and(
                        Restrictions.eq("rutina.aprobada", true))
        );

        //omitir control de calidad
        crit.add(Restrictions.and(
                        Restrictions.eq("rutina.controlCalidad", false))
        );

        //filtro de resultado final positivo
        crit.add(Subqueries.propertyIn("rutina.idSolicitudDx", DetachedCriteria.forClass(DetalleResultadoFinal.class)
                .setProjection(Property.forName("solicitudDx.idSolicitudDx"))));

        crit.addOrder(Order.asc("fechaAprobacion"));


        return crit.list();
    }

    /**
     * M?todo que retornar la informaci?n para generar reporte y gr?fico de dx por tipo de resultado (positivo, negativo, sin resultado y % positividad)
     * @param filtro indicando el nivel (pais, silais, departamento, municipio, unidad salud), tipo notificaci?n, rango de fechas, factor tasas de poblaci?n
     * @return Lista de objetos a mostrar
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDataDxResultReport(FiltrosReporte filtro) {
        // Retrieve session from Hibernate
        List<Object[]> resTemp1 = new ArrayList<Object[]>();
        List<Object[]> resTemp2 = new ArrayList<Object[]>();

        List<Object[]> resFinal = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query queryNotiDx = null;
        Query queryIdNoti = null;
        String queryNotiEstudios = "";


        if (filtro.getCodArea().equals("AREAREP|PAIS")) {

            if (filtro.isPorSilais()) {
                queryNotiDx = session.createQuery(" select ent.codigo, ent.nombre, " + //TOTAL RUTINAS
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        "and noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and dx.aprobada = true and mx.anulada = false and dx.controlCalidad = false " +
                        sqlRutina + sqlFechasProcRut +
                        " group by noti.municipioResidencia.dependenciaSilais.codigo) as dx, " +
                        " coalesce( " + //TOTAL RUTINAS CON RESULTADO
                        " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        sqlRutina + sqlFechasProcRut +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and dx.aprobada = true " +
                        " and mx.anulada = false),0) as conresultado, " +
                        " coalesce( " + //TOTAL RUTINAS SIN RESULTADO
                        " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        sqlRutina + sqlFechasProcRut +
                        " and  noti.municipioResidencia.dependenciaSilais.codigo = ent.codigo " +
                        " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                        " and dx.aprobada = true " +
                        " and mx.anulada = false),0) as sinresultado " +
                        " from EntidadesAdtvas ent " +
                        " where ent.pasivo = 0 " +
                        " order by ent.codigo ");

                queryIdNoti = session.createQuery(" select noti.municipioResidencia.dependenciaSilais.codigo, dx.idSolicitudDx " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.idNotificacion = mx.idNotificacion " +
                        sqlRutina +sqlFechasProcRut +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false and dx.anulado = false " +
                        " and mx.anulada = false " +
                        " and dx.aprobada = true and dx.controlCalidad = false " +
                        " and noti.municipioResidencia is not null " +
                        " order by noti.municipioResidencia.dependenciaSilais.codigo");

            }else{
                queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                        " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                        " and noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and dx.aprobada = true and mx.anulada = false and dx.controlCalidad = false " +
                        sqlRutina +sqlFechasProcRut +
                        " group by noti.municipioResidencia.dependencia.divisionpoliticaId) as dx, " +
                        " coalesce( " +
                        " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        sqlRutina +sqlFechasProcRut +
                        " and noti.pasivo = false and dx.anulado = false and dx.aprobada = true and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as conresultado, " +
                        " coalesce( " +
                        " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        sqlRutina +sqlFechasProcRut +
                        " and  noti.municipioResidencia.dependencia.divisionpoliticaId = div.divisionpoliticaId " +
                        " and noti.pasivo = false and dx.anulado = false and dx.aprobada = true and dx.controlCalidad = false " +
                        " and mx.anulada = false),0) as sinresultado " +
                        " from Divisionpolitica div " +
                        "where div.dependencia is null and div.pasivo = '0'" +
                        " order by div.divisionpoliticaId ");

                queryIdNoti = session.createQuery(" select noti.municipioResidencia.dependencia.divisionpoliticaId, dx.idSolicitudDx " +
                        " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                        " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion " +
                        sqlRutina +sqlFechasProcRut +
                        " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                        " and noti.pasivo = false and dx.anulado = false " +
                        " and mx.anulada = false " +
                        " and dx.aprobada = true and dx.controlCalidad = false " +
                        " order by noti.municipioResidencia.dependencia.divisionpoliticaId");

            }

            //rutinas
            queryIdNoti.setParameter("idDx", filtro.getIdDx());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            resTemp2.addAll(queryIdNoti.list());

        } else if (filtro.getCodArea().equals("AREAREP|SILAIS")) {

            queryNotiDx = session.createQuery(" select distinct div.divisionpoliticaId, div.nombre, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                    sqlRutina +sqlFechasProcRut +
                    " group by noti.municipioResidencia.divisionpoliticaId), " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlRutina +sqlFechasProcRut +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlRutina +sqlFechasProcRut +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from Divisionpolitica div, Unidades as uni " +
                    "where div.pasivo = '0' and uni.pasivo='0' " +
                    "and uni.municipio.codigoNacional = div.codigoNacional and uni.entidadAdtva.entidadAdtvaId = :codSilais " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.municipioResidencia.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    " where noti.municipioResidencia is not null and noti.pasivo = false and dx.anulado = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true and dx.controlCalidad = false " +
                    sqlRutina +sqlFechasProcRut +
                    " and noti.municipioResidencia.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by noti.municipioResidencia.divisionpoliticaId ");


            queryNotiDx.setParameter("codSilais", filtro.getCodSilais());

            //rutinas
            queryIdNoti.setParameter("idDx", filtro.getIdDx());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codSilais", filtro.getCodSilais());
            resTemp2.addAll(queryIdNoti.list());

        } else if (filtro.getCodArea().equals("AREAREP|DEPTO")) {

            queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and dx.anulado = false and mx.anulada = false and dx.controlCalidad = false " +
                    sqlRutina +sqlFechasProcRut +
                    " group by noti.municipioResidencia.divisionpoliticaId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlRutina +sqlFechasProcRut +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlRutina +sqlFechasProcRut +
                    " and  noti.municipioResidencia.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false and dx.anulado = false and dx.controlCalidad = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from Divisionpolitica div " +
                    "where div.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.municipioResidencia.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.municipioResidencia is not null and noti.idNotificacion = mx.idNotificacion " +
                    sqlRutina +sqlFechasProcRut +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false and dx.anulado = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true and dx.controlCalidad = false " +
                    " and noti.municipioResidencia.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by noti.municipioResidencia.divisionpoliticaId ");

            queryNotiDx.setParameter("codDepartamento", filtro.getCodDepartamento());

            //rutinas
            queryIdNoti.setParameter("idDx", filtro.getIdDx());
            queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
            queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());
            queryIdNoti.setParameter("codDepartamento", filtro.getCodDepartamento());
            resTemp2.addAll(queryIdNoti.list());

        }

        queryNotiDx.setParameter("idDx", filtro.getIdDx());
        queryNotiDx.setParameter("fechaInicio", filtro.getFechaInicio());
        queryNotiDx.setParameter("fechaFin", filtro.getFechaFin());

        resTemp1.addAll(queryNotiDx.list());

        for (Object[] reg : resTemp1) {
            Object[] reg1 = new Object[8];
            reg1[0] = reg[1]; //Nombre Silais
            reg1[1] = reg[2]; //Cantidad Notificaciones (NO SE USA)
            reg1[2] = (Long)reg[2]; //Cantidad Dx

            int pos = 0;
            int neg = 0;
            int inadecuada = 0;
            for (Object[] sol : resTemp2) {
                if (sol[0].equals(reg[0])) {

                    List<DetalleResultadoFinal> finalRes = resultadoFinalService.getDetResActivosBySolicitud(sol[1].toString());
                    for (DetalleResultadoFinal res : finalRes) {
                        if (res.getRespuesta() != null) {
                            if (res.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                Integer idLista = Integer.valueOf(res.getValor());
                                Catalogo_Lista valor = null;
                                try {
                                    valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(idLista);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (valor != null) {
                                    if (valor.getValor().trim().toLowerCase().contains("negativo")
                                            || valor.getValor().trim().toLowerCase().contains("no reactor")
                                            || valor.getValor().trim().toLowerCase().contains("no detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                        neg++;
                                        break;
                                    }else if (valor.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                        inadecuada++;
                                    }else if (valor.getValor().trim().toLowerCase().contains("positivo")
                                            || valor.getValor().trim().toLowerCase().contains("reactor")
                                            || valor.getValor().trim().toLowerCase().contains("detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-DET")) {
                                        pos++;
                                        break;
                                    }
                                }


                            } else if (res.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().trim().toLowerCase().contains("negativo")
                                        || res.getValor().trim().toLowerCase().contains("no reactor")
                                        || res.getValor().trim().toLowerCase().contains("no detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                    neg++;
                                    break;
                                }else if (res.getValor().trim().toLowerCase().contains("mx inadecuada")){
                                    inadecuada++;
                                }else if (res.getValor().trim().toLowerCase().contains("positivo")
                                        || res.getValor().trim().toLowerCase().contains("reactor")
                                        || res.getValor().trim().toLowerCase().contains("detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-DET")) {
                                    pos++;
                                    break;
                                }

                            }

                        } else if (res.getRespuestaExamen() != null) {
                            if (res.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|LIST")) {
                                Integer idLista = Integer.valueOf(res.getValor());
                                Catalogo_Lista valor = null;
                                try {
                                    valor = respuestasExamenService.getCatalogoListaConceptoByIdLista(idLista);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (valor != null) {
                                    if (valor.getValor().trim().toLowerCase().contains("negativo")
                                            || valor.getValor().trim().toLowerCase().contains("no reactor")
                                            || valor.getValor().trim().toLowerCase().contains("no detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                        neg++;
                                        break;
                                    }else if (valor.getValor().trim().toLowerCase().equals("mx inadecuada")){
                                        inadecuada++;
                                    }else if (valor.getValor().trim().toLowerCase().contains("positivo")
                                            || valor.getValor().trim().toLowerCase().contains("reactor")
                                            || valor.getValor().trim().toLowerCase().contains("detectado")
                                            || valor.getValor().trim().toUpperCase().contains("MTB-DET")) {
                                        pos++;
                                        break;
                                    }
                                }

                            } else if (res.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().trim().toLowerCase().contains("negativo")
                                        || res.getValor().trim().toLowerCase().contains("no reactor")
                                        || res.getValor().trim().toLowerCase().contains("no detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-ND")) {
                                    neg++;
                                    break;
                                }else if (res.getValor().trim().toLowerCase().contains("mx inadecuada")){
                                    inadecuada++;
                                }else if (res.getValor().trim().toLowerCase().contains("positivo")
                                        || res.getValor().trim().toLowerCase().contains("reactor")
                                        || res.getValor().trim().toLowerCase().contains("detectado")
                                        || res.getValor().trim().toUpperCase().contains("MTB-DET")) {
                                    pos++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }


            reg1[3] = pos; // Positivo
            reg1[4] = neg; // Negativo
            reg1[5] = (Long)reg[4]; // Sin Resultado dx
            Long totalConySinResultado = (Long) reg1[2];
            reg1[6] = (totalConySinResultado != 0 ? (double) Math.round(Integer.valueOf(reg1[3].toString()).doubleValue() / totalConySinResultado * 100 * 100) / 100 : 0);
            reg1[7] = inadecuada; //muestras inadecuadas
            resFinal.add(reg1);

        }
        return resFinal;
    }
}
