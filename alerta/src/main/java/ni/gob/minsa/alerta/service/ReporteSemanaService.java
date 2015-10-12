package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.utilities.DateUtil;
import ni.gob.minsa.alerta.utilities.FiltrosReporte;
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
    
    private static final String sqlDataSemana = "select cal.noSemana ";
    private static final String sqlWhereSemana = " WHERE cal.anio = :anio and cal.noSemana between :semanaI and :semanaF order by cal.noSemana";
    private static final String sqlDataDia = "select  noti.fechaRegistro, count(noti.idNotificacion) as casos ";
    private static final String sqlDataSinR = "select distinct noti ";

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
                    if (Arrays.equals(resultadoTemp.get(resultadoTemp.size()-1),jaja)){
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

    public List<DaNotificacion> getDataSinResultado(FiltrosReporte filtro) throws ParseException {

        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        List<DaNotificacion> resultadoTemp = new ArrayList<DaNotificacion>();

        if (filtro.getCodArea().equals("AREAREP|PAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaNotificacion noti " +
                            "where noti.pasivo = false  and noti.codTipoNotificacion.codigo = :tipoNoti " +
            " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

        }
        else if (filtro.getCodArea().equals("AREAREP|SILAIS")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaNotificacion noti " +
                    "where noti.pasivo = false  and noti.codSilaisAtencion.entidadAdtvaId = :codSilais and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            queryCasos.setParameter("codSilais", filtro.getCodSilais());

        }
        else if (filtro.getCodArea().equals("AREAREP|DEPTO")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaNotificacion noti " +
                    "where noti.pasivo = false  and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.dependencia.divisionpoliticaId =:codDepartamento" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");
            queryCasos.setParameter("codDepartamento", filtro.getCodDepartamento());

        }
        else if (filtro.getCodArea().equals("AREAREP|MUNI")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaNotificacion noti " +
                    "where noti.pasivo = false  and noti.codTipoNotificacion.codigo = :tipoNoti " +
                    "and noti.codUnidadAtencion.municipio.divisionpoliticaId =:codMunicipio" +
                    " and noti.fechaRegistro between :fechaInicio and :fechaFin " +
                    " order by noti.fechaRegistro asc");

            queryCasos.setParameter("codMunicipio", filtro.getCodMunicipio());

        }
        else if (filtro.getCodArea().equals("AREAREP|UNI")){
            queryCasos = session.createQuery(sqlDataSinR + "From DaNotificacion noti " +
                    "where noti.pasivo = false  and noti.codTipoNotificacion.codigo = :tipoNoti " +
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
