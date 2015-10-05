package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.utilities.FiltrosReporte;
import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souyen-ics.
 */
@Service("areaReportService")
@Transactional
public class AreaReportService {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<Object[]> getDataCT(FiltrosReporte filtro){
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

            queryCasos = session.createQuery(" select ent.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codSilaisAtencion.codigo =  ent.codigo " +
                    " and noti.pasivo = false " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin) " +
                    "FROM EntidadesAdtvas ent ");



        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codSilais", filtro.getCodSilais());

            queryCasos = session.createQuery("select ent.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codSilaisAtencion.codigo =  ent.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti" +
                    " and noti.pasivo = false " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin) " +
                    "FROM EntidadesAdtvas ent " +
                    "where ent.codigo = :codSilais ");
            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codDepartamento", filtro.getCodDepartamento());

            queryCasos = session.createQuery(" select divi.nombre, " +
                    "(select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId = divi.divisionpoliticaId  " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin) " +
                    "FROM Divisionpolitica divi " +
                    "where divi.dependencia.divisionpoliticaId = :codDepartamento ");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());
        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){

            queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codMunicipio", filtro.getCodMunicipio());

            queryCasos = session.createQuery("select uni.nombre, " +
                    " ( select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin) " +
                    "FROM Unidades uni " +
                    "where uni.municipio.divisionpoliticaId = :codMunicipio" +
                    " and uni.tipoUnidad in ("+ HealthUnitType.UnidadesPrimHosp.getDiscriminator()+") ");
            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());
        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryPoblacion = session.createQuery("Select sum(pob.total) as total " +
                    "from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad " +
                    "and pob.grupo =:tipoPob " +
                    "and (pob.anio =:anio) " +
                    "group by pob.anio order by pob.anio");
            queryPoblacion.setParameter("codUnidad", filtro.getCodUnidad());

            queryCasos = session.createQuery("select uni.nombre, " +
                    " ( select count(noti.idNotificacion) from DaNotificacion noti " +
                    "where noti.codUnidadAtencion.codigo =  uni.codigo " +
                    "and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.pasivo = false " +
                    "and noti.fechaRegistro between :fechaInicio and :fechaFin) " +
                    "FROM Unidades uni " +
                    "where uni.codigo = :codUnidad " +
                    "or uni.unidadAdtva = :codUnidad");
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
            Object[] reg1 = new Object[3];
            reg1[0] = reg[0];
            reg1[1] = reg[1];
            reg1[2] = ((Long) reg[1] != 0 ? ((double) Math.round((Integer.valueOf(reg[1].toString()).doubleValue()) / poblacion * filtro.getFactor() * 100) / 100) : 0);
            resFinal.add(reg1);
        }

            return resFinal;
    }



}
