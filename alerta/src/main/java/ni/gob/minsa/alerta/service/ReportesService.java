package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.resultados.Catalogo_Lista;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRSTICT on 10/15/2015.
 * V1.0
 */
@Service("reportesService")
@Transactional
public class ReportesService {
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

            queryCasos = session.createQuery(" select ent.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codSilaisAtencion.codigo =  ent.codigo " +
                    " and noti.pasivo = false " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                            "(select sum(pob.total)" +
                            "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId = ent.entidadAdtvaId " +
                            "and pob.grupo =:tipoPob " +
                            "and (pob.anio =:anio)) " +
                    "FROM EntidadesAdtvas ent "+
                    " where ent.pasivo = 0 " );
        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){

            queryCasos = session.createQuery(" select divi.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId = divi.divisionpoliticaId  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                    "(select sum(pob.total) " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId = divi.divisionpoliticaId " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio)) " +
                    "FROM Divisionpolitica divi " +
                    "where divi.dependenciaSilais.entidadAdtvaId = :codSilais ");

            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(" select divi.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId = divi.divisionpoliticaId  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                    "(Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =divi.divisionpoliticaId " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio)) " +
                    "FROM Divisionpolitica divi " +
                    "where divi.dependencia.divisionpoliticaId = :codDepartamento ");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){

            queryCasos = session.createQuery("select uni.nombre, " +
                    " ( select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin), " +
                    "(select sum(pob.total) as total " +
                    "from SivePoblacion pob where (pob.comunidad.sector.unidad.unidadId =uni.unidadId " +
                    " or  pob.comunidad.sector.unidad.unidadAdtva in (select uni2.codigo from Unidades uni2 where uni2.unidadId = uni.unidadId ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    " ) and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio)) " +
                    "FROM Unidades uni " +
                    "where uni.municipio.divisionpoliticaId = :codMunicipio" +
                    " and uni.tipoUnidad in ("+ HealthUnitType.UnidadesPrimHosp.getDiscriminator()+") ");
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());
        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){

            queryCasos = session.createQuery("select uni.nombre, " +
                    " coalesce (( select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin),0), " +
                    "coalesce ((Select sum(pob.total) as total " +
                    "from SivePoblacion pob where (pob.comunidad.sector.unidad.unidadId =:codUnidad " +
                    " or  pob.comunidad.sector.unidad.unidadAdtva in (select uni2.codigo from Unidades uni2 where uni2.unidadId = uni.unidadId ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    " ) and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio)),0) " +
                    "FROM Unidades uni " +
                    "where uni.unidadId = :codUnidad " +
                    "or uni.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad ) ");
            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());

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
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
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
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "and noti.codSilaisAtencion.codigo = :codSilais  " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.codSilaisAtencion.codigo = :codSilais " +
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
                    "and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId = :codDepartamento  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId = :codDepartamento  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );

            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codMunicipio", filtro.getCodMunicipio());

            queryCasos = session.createQuery(" select sex.valor, coalesce((select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and per.sexo.codigo = sex.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId = :codMunicipio  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId = :codMunicipio  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );

            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());
        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacion pob where (pob.comunidad.sector.unidad.unidadId =:codUnidad " +
                    " or  pob.comunidad.sector.unidad.unidadAdtva in (select uni.codigo from Unidades uni where uni.unidadId = :codUnidad ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    " ) and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codUnidad", filtro.getCodUnidad());

            queryCasos = session.createQuery(" select sex.valor, coalesce((select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    " and per.sexo.codigo = sex.codigo " +
                    " and noti.codUnidadAtencion.unidadId = :codUnidad " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by per.sexo.valor),0),  " +
                    " coalesce( (select count(noti.idNotificacion) " +
                    "from DaNotificacion noti, SisPersona per " +
                    "where noti.persona.id = per.personaId " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.codUnidadAtencion.unidadId = :codUnidad " +
                    "or noti.codUnidadAtencion.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad ) " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "),0) " +
                    " from Sexo sex where sex.pasivo = false" );

            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());

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


        if (filtro.getCodArea().equals("AREAREP|PAIS")) {

            queryNotiDx = session.createQuery(" select ent.codigo, ent.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codSilaisAtencion.codigo = ent.codigo and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codSilaisAtencion.codigo),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion and noti.codSilaisAtencion.codigo = ent.codigo " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codSilaisAtencion.codigo) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codSilaisAtencion.codigo = ent.codigo " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codSilaisAtencion.codigo = ent.codigo " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from EntidadesAdtvas ent " +
                    " where ent.pasivo = 0 " +
                    " order by ent.codigo ");



            queryIdNoti = session.createQuery(" select noti.codSilaisAtencion.codigo, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codSilaisAtencion is not null " +
                    " order by noti.codSilaisAtencion.codigo ");


        } else if (filtro.getCodArea().equals("AREAREP|SILAIS")) {

          /*  queryNotiDx = session.createQuery(" select ent.codigo, ent.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codSilaisAtencion.codigo = ent.codigo and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codSilaisAtencion.codigo),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion and noti.codSilaisAtencion.codigo = ent.codigo " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codSilaisAtencion.codigo) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codSilaisAtencion.codigo = ent.codigo " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codSilaisAtencion.codigo = ent.codigo " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from EntidadesAdtvas ent " +
                    " where ent.codigo = :codSilais " +
                    " order by ent.codigo ");



            queryIdNoti = session.createQuery(" select noti.codSilaisAtencion.codigo, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codSilaisAtencion.codigo = :codSilais " +
                    " order by noti.codSilaisAtencion.codigo ");*/

            queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId" +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.municipio.divisionpoliticaId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.municipio.divisionpoliticaId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from Divisionpolitica div " +
                    "where div.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.codUnidadAtencion.municipio.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codUnidadAtencion.municipio.dependenciaSilais.entidadAdtvaId = :codSilais " +
                    " order by noti.codUnidadAtencion.municipio.divisionpoliticaId ");

            queryNotiDx.setParameter("codSilais", filtro.getCodSilais());
            queryIdNoti.setParameter("codSilais", filtro.getCodSilais());

        } else if (filtro.getCodArea().equals("AREAREP|DEPTO")) {

            queryNotiDx = session.createQuery(" select div.divisionpoliticaId, div.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId" +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.municipio.divisionpoliticaId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.municipio.divisionpoliticaId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codUnidadAtencion.municipio.divisionpoliticaId = div.divisionpoliticaId " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    " from Divisionpolitica div " +
                    "where div.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by div.divisionpoliticaId ");

            queryIdNoti = session.createQuery(" select noti.codUnidadAtencion.municipio.divisionpoliticaId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId = :codDepartamento " +
                    " order by noti.codUnidadAtencion.municipio.divisionpoliticaId ");

            queryNotiDx.setParameter("codDepartamento", filtro.getCodDepartamento());
            queryIdNoti.setParameter("codDepartamento", filtro.getCodDepartamento());

        } else if (filtro.getCodArea().equals("AREAREP|MUNI")) {


            queryNotiDx = session.createQuery(" select uni.unidadId, uni.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.unidadId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by  noti.codUnidadAtencion.unidadId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codUnidadAtencion.codigo =  uni.codigo " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    "FROM Unidades uni " +
                    "where uni.municipio.divisionpoliticaId = :codMunicipio" +
                    " and uni.tipoUnidad in ("+ HealthUnitType.UnidadesPrimHosp.getDiscriminator()+") " +
                    " order by uni.unidadId ");



            queryIdNoti = session.createQuery(" select noti.codUnidadAtencion.unidadId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codUnidadAtencion.municipio.divisionpoliticaId = :codMunicipio " +
                    " order by noti.codUnidadAtencion.unidadId ");


            queryNotiDx.setParameter("codMunicipio", filtro.getCodMunicipio());
            queryIdNoti.setParameter("codMunicipio", filtro.getCodMunicipio());
        } else if (filtro.getCodArea().equals("AREAREP|UNI")) {


            queryNotiDx = session.createQuery(" select uni.unidadId, uni.nombre, " +
                    " coalesce( " +
                    " (select count(noti.idNotificacion) from DaNotificacion noti " +
                    " where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and noti.pasivo = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by noti.codUnidadAtencion.unidadId),0) as noti, " +
                    " (select coalesce(sum(count(noti.idNotificacion)),0) from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion" +
                    " and noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx and noti.pasivo = false and mx.anulada = false " +
                    sqlTipoNoti +sqlFechas +
                    " group by  noti.codUnidadAtencion.unidadId) as dx, " +
                    " coalesce( " +
                    " (select sum(case dx.aprobada when true then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and  noti.codUnidadAtencion.codigo =  uni.codigo " +
                    sqlTipoNoti +sqlFechas +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as conresultado, " +
                    " coalesce( " +
                    " (select  sum(case dx.aprobada when false then 1 else 0 end) " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion.idNotificacion " +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    sqlTipoNoti +sqlFechas +
                    " and  noti.codUnidadAtencion.codigo =  uni.codigo " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false),0) as sinresultado " +
                    "FROM Unidades uni " +
                    "where uni.unidadId = :codUnidad" +
                    " or uni.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad ) " +
                    " and uni.tipoUnidad in ("+ HealthUnitType.UnidadesPrimHosp.getDiscriminator()+") " +
                    " order by uni.unidadId ");



            queryIdNoti = session.createQuery(" select noti.codUnidadAtencion.unidadId, dx.idSolicitudDx " +
                    " from DaNotificacion noti, DaTomaMx mx, DaSolicitudDx dx " +
                    " where noti.idNotificacion = mx.idNotificacion " +
                    sqlTipoNoti +sqlFechas +
                    " and mx.idTomaMx = dx.idTomaMx.idTomaMx " +
                    " and noti.pasivo = false " +
                    " and mx.anulada = false " +
                    " and dx.aprobada = true " +
                    " and noti.codUnidadAtencion.unidadId = :codUnidad " +
                    " or noti.codUnidadAtencion.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad ) " +
                    " order by noti.codUnidadAtencion.unidadId ");


            queryNotiDx.setParameter("codUnidad", filtro.getCodUnidad());
            queryIdNoti.setParameter("codUnidad", filtro.getCodUnidad());

        }

        queryNotiDx.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryIdNoti.setParameter("tipoNoti", filtro.getTipoNotificacion());
        queryNotiDx.setParameter("fechaInicio", filtro.getFechaInicio());
        queryNotiDx.setParameter("fechaFin", filtro.getFechaFin());
        queryIdNoti.setParameter("fechaInicio", filtro.getFechaInicio());
        queryIdNoti.setParameter("fechaFin", filtro.getFechaFin());


        resTemp1.addAll(queryNotiDx.list());
        resTemp2.addAll(queryIdNoti.list());

        for (Object[] reg : resTemp1) {
            Object[] reg1 = new Object[7];
            reg1[0] = reg[1]; //Nombre Silais
            reg1[1] = reg[2]; //Cantidad Notificaciones
            reg1[2] = reg[3]; //Cantidad Dx

            int pos = 0;
            int neg = 0;
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
                                    if (valor.getValor().toLowerCase().equals("positivo")) {
                                        pos++;
                                        break;
                                    } else if (valor.getValor().toLowerCase().equals("negativo")) {
                                        neg++;
                                        break;
                                    }
                                }


                            } else if (res.getRespuesta().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().toLowerCase().equals("positivo")) {
                                    pos++;
                                    break;
                                } else if (res.getValor().toLowerCase().equals("negativo")) {
                                    neg++;
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
                                    if (valor.getValor().toLowerCase().equals("positivo")) {
                                        pos++;
                                        break;
                                    } else if (valor.getValor().toLowerCase().equals("negativo")) {
                                        neg++;
                                        break;
                                    }
                                }

                            } else if (res.getRespuestaExamen().getConcepto().getTipo().getCodigo().equals("TPDATO|TXT")) {
                                if (res.getValor().toLowerCase().equals("positivo")) {
                                    pos++;
                                    break;
                                } else if (res.getValor().toLowerCase().equals("negativo")) {
                                    neg++;
                                    break;
                                }

                            }
                        }


                    }
                }
            }


            reg1[3] = pos; // Positivo
            reg1[4] = neg; // Negativo
            reg1[5] = reg[5]; // Sin Resultado
            reg1[6] = ((Long) reg[4] != 0 ? (double) Math.round(Integer.valueOf(reg1[3].toString()).doubleValue() / Integer.valueOf(reg[4].toString()).doubleValue() * 100 * 100) / 100 : 0);

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
                    "where noti.pasivo = false and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependencia is null " +
                    "and pob.grupo =:tipoPob " +
                    "and pob.anio =:anio " +
                    "group by pob.anio order by pob.anio");
        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and noti.codSilaisAtencion.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codSilais", filtro.getCodSilais());
        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId =:codDepartamento and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codDepartamento", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and noti.codUnidadAtencion.municipio.divisionpoliticaId =:codMunicipio and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());

            queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codMunicipio", filtro.getCodMunicipio());
        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos = session.createQuery(sqlDataSemana + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.pasivo = false and (noti.codUnidadAtencion.unidadId = :codUnidad " +
                    " or noti.codUnidadAtencion.unidadAdtva in (select uni.codigo from Unidades uni where uni.unidadId = :codUnidad ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    ") " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhereSemana);
            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacion pob where (pob.comunidad.sector.unidad.unidadId =:codUnidad " +
                    " or  pob.comunidad.sector.unidad.unidadAdtva in (select uni.codigo from Unidades uni where uni.unidadId = :codUnidad ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    " ) and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codUnidad", filtro.getCodUnidad());
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
                    "where noti.pasivo = false and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.codSilaisAtencion.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId =:codDepartamento and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.codUnidadAtencion.municipio.divisionpoliticaId =:codMunicipio and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());

        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos = session.createQuery(sqlDataDia + " From DaNotificacion  noti " +
                    "where noti.pasivo = false and noti.codUnidadAtencion.unidadId = :codUnidad and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    "group by noti.fechaRegistro order by noti.fechaRegistro asc");
            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());

        }

        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());
        resultadoTemp = queryCasos.list();
        Date ultimaFecha = null;
        Long cantidadCasos = 0L;
        boolean agregar;
        for(Object[] jaja : resultadoTemp ){
            agregar = false;
            Object[] registroseman = new Object[2];
            //fecha de registro sin hora
            Date fechaCompara = DateUtil.StringToDate(DateUtil.DateToString((Date) jaja[0], "dd/MM/yyyy"), "dd/MM/yyyy");
            if (ultimaFecha!=null) {
                if (ultimaFecha.compareTo(fechaCompara)==0){//fechas son iguales, entonces sumar casos
                    cantidadCasos+= (Long)jaja[1];
                    //es el´último registro, se debe agregar
                    if (Arrays.equals(resultadoTemp.get(resultadoTemp.size() - 1), jaja)){
                        registroseman[0] = ultimaFecha;
                        registroseman[1] = cantidadCasos;
                        //agregar = true;
                        resultadoFinal.add(registroseman);
                    }
                }else{// temporales se actualizan con datos nuevo registro, se indica que es necesario agregar el registro anterior
                    registroseman[0] = ultimaFecha;
                    registroseman[1] = cantidadCasos;
                    ultimaFecha = fechaCompara;
                    cantidadCasos = (Long)jaja[1];
                    //agregar =true;
                    resultadoFinal.add(registroseman);
                    //no son iguales, pero es el último registro, agregarlo también
                    if (Arrays.equals(resultadoTemp.get(resultadoTemp.size()-1),jaja)){
                        registroseman = new Object[2];
                        registroseman[0] = fechaCompara;
                        registroseman[1] = jaja[1];
                        resultadoFinal.add(registroseman);
                    }
                }
            }else{//primer registro
                ultimaFecha = fechaCompara;
                cantidadCasos = (Long)jaja[1];
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
        List<DaNotificacion> resultadoTemp = new ArrayList<DaNotificacion>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.aprobada = false and mx.anulada = false  " +
                    " and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.aprobada = false and mx.anulada = false  " +
                    "and noti.codSilaisAtencion.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.aprobada = false and mx.anulada = false  " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId =:codDepartamento" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.aprobada = false and mx.anulada = false  " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId =:codMunicipio" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());

        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                    "where noti.pasivo = false and dx.aprobada = false and mx.anulada = false  " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and (noti.codUnidadAtencion.unidadId = :codUnidad " +
                    " or noti.codUnidadAtencion.unidadAdtva in (select uni.codigo from Unidades uni where uni.unidadId = :codUnidad ) " + //se toman en cuenta sus unidades dependientes( si las tiene)
                    " ) and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());
        }

        queryCasos.setParameter("fechaInicio", filtro.getFechaInicio());
        queryCasos.setParameter("fechaFin", filtro.getFechaFin());
        queryCasos.setParameter("tipoNoti", filtro.getTipoNotificacion());

        resultadoTemp = queryCasos.list();
        return resultadoTemp;
    }

}
