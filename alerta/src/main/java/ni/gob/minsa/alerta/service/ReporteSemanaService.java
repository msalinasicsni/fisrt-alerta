package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

/**
 * Created by FIRSTICT on 9/29/2015.
 * V1.0
 */
@Service("reporteSemanaService")
@Transactional
public class ReporteSemanaService {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;
    private static final String sqlData = "select cal.noSemana ";
    private static final String sqlWhere = " WHERE cal.anio = :anio " +
            "and cal.noSemana between :semanaI and :semanaF " +
            "order by cal.noSemana";

    public List<Object[]> getDataPorSemana(FiltrosReporte filtro){
        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        Query queryPoblacion = null;
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object[]> resultadoFinal = new ArrayList<Object[]>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlData + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhere);

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependencia is null " +
                    "and pob.grupo =:tipoPob " +
                    "and pob.anio =:anio " +
                    "group by pob.anio order by pob.anio");
        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlData + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codSilaisAtencion.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhere);
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codSilais", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlData + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId =:codDepartamento and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhere);
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codDepartamento", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos = session.createQuery(sqlData + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.municipio.divisionpoliticaId =:codMunicipio and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhere);
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());

            queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codMunicipio", filtro.getCodMunicipio());
        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos = session.createQuery(sqlData + ", (select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.unidadId = :codUnidad and noti.codTipoNotificacion.codigo = :tipoNoti and noti.fechaRegistro between cal.fechaInicial and cal.fechaFinal) " +
                    "From CalendarioEpi cal " + sqlWhere);
            queryCasos.setParameter("codUnidad", filtro.getCodUnidad());

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad " +
                    "and pob.grupo =:tipoPob " +
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
            registroseman[2] = ((Long)semana[1]!=0?((double) Math.round((Integer.valueOf(semana[1].toString()).doubleValue())/poblacion*filtro.getFactor()*100)/100):0);
            resultadoFinal.add(registroseman);
        }
        return resultadoFinal;
    }

}
