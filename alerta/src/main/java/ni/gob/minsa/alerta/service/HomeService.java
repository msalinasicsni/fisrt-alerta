package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import ni.gob.minsa.alerta.domain.sive.SivePatologiasTipo;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIRSTICT on 3/11/2016.
 * V1.0
 */
@Service("homeService")
@Transactional
public class HomeService {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    private static final String sqlData = "Select inf.anio as anio, inf.semana as semana,  " +
            "sum(inf.totalm) as totalm, sum(inf.totalf) as totalf, sum(inf.totalm+inf.totalf) as total";

    public List<Object[]> getDataCasosTasas(String codPato, String nivelUsuario, Long idUsuario, String semI, String semF, String anioI,String anioF){
       if (nivelUsuario.isEmpty() || semI.isEmpty() || semF.isEmpty() || anioI.isEmpty() || anioF.isEmpty())
           return new ArrayList<Object[]>();

        // Retrieve session from Hibernate
        List<Object[]> resultado = new ArrayList<Object[]>();
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object> itemTransf = new ArrayList<Object>();
        List<Object[]> resultadoF = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query query = null;
        String patoQuery = null;
        String[] patos = codPato.split(",");
        List<Integer> semanas = new ArrayList<Integer>();
        List<Integer> anios = new ArrayList<Integer>();
        for (int i =0; i<patos.length; i++){
            if (patoQuery == null){
                patoQuery = "inf.patologia.codigo = '"+patos[i]+"'";
            }
            else{
                patoQuery = patoQuery + " or inf.patologia.codigo = '"+patos[i]+"'";
            }
        }
        if (!semF.isEmpty()) {
            for (int i = 0; i <= (Integer.parseInt(semF) - Integer.parseInt(semI)); i++) {
                semanas.add(Integer.parseInt(semI) + i);
            }
        }

        if (!anioF.isEmpty() && !anioI.isEmpty()) {
            for (int i = 0; i <= (Integer.parseInt(anioF) - Integer.parseInt(anioI)); i++) {
                anios.add(Integer.parseInt(anioI) + i);
            }
        }
        if (nivelUsuario.equals("PAIS")){
            query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
                    "where ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, inf.semana order by inf.anio, inf.semana");
        }
        else if (nivelUsuario.equals("SILAIS")){
            query = session.createQuery(sqlData + " From SiveInformeDiario inf, UsuarioEntidad ue " +
                    "where ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and inf.silais = cast(ue.entidadAdtva.entidadAdtvaId as string) and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
            query.setParameter("idUsuario", idUsuario.intValue());
            query.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
        }
        else if (nivelUsuario.equals("UNIDAD")){
            query = session.createQuery(sqlData + " From SiveInformeDiario inf, UsuarioUnidad uu " +
                    "where uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and inf.unidad.unidadId = uu.unidad.unidadId and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
            query.setParameter("idUsuario", idUsuario.intValue());
            query.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
        }

        query.setParameter("semI", Integer.parseInt(semI));
        query.setParameter("semF", semF.isEmpty()?0:Integer.parseInt(semF));
        query.setParameter("anioI", anioI.isEmpty()?0:Integer.parseInt(anioI));
        query.setParameter("anioF", anioF.isEmpty()?0: Integer.parseInt(anioF));
        resultadoTemp.addAll(query.list());

        if(!resultadoTemp.isEmpty()){
            String pato =""; String year = "";
            for(Object[] objArray: resultadoTemp){
                if (itemTransf.isEmpty()) {
                    itemTransf.add(objArray[0]);
                    itemTransf.add(objArray[1]);
                    itemTransf.add(objArray[4]);
                    year = objArray[0].toString();
                }else {
                    if (!objArray[0].toString().matches(year)) {
                        resultado.add(itemTransf.toArray());
                        itemTransf.clear();
                        itemTransf.add(objArray[0]);
                        itemTransf.add(objArray[1]);
                        itemTransf.add(objArray[4]);
                    } else {
                            itemTransf.add(objArray[1]);
                            itemTransf.add(objArray[4]);
                    }
                }
                year = objArray[0].toString();
            }
            resultado.add(itemTransf.toArray()); itemTransf.clear();

            //hay mas de una patología, pero solo se tomará la primera para obtener la población pues debería ser la misma para todas
                query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
                query.setParameter("codPato", patos[0]);
                SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
                for(Integer anio: anios){
                    if (nivelUsuario.equals("PAIS")){
                        query = session.createQuery("Select sum(pob.total) as total " +
                                "from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and pob.anio =:anio " +
                                "group by pob.anio order by pob.anio");
                    }
                    else if (nivelUsuario.equals("SILAIS")){
                        query = session.createQuery("Select sum(pob.total) as total " +
                                "from SivePoblacionDivPol pob, UsuarioEntidad ue " +
                                "where ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and pob.divpol.dependenciaSilais.entidadAdtvaId=ue.entidadAdtva.entidadAdtvaId and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                "group by pob.anio order by pob.anio");
                        query.setParameter("idUsuario", idUsuario.intValue());
                        query.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    }
                    else if (nivelUsuario.equals("UNIDAD")){
                        query = session.createQuery("Select sum(pob.total) as total " +
                                "from SivePoblacion pob, UsuarioUnidad uu " +
                                "where uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and " +
                                "pob.comunidad.sector.unidad.unidadId = uu.unidad.unidadId and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                "group by pob.anio order by pob.anio");
                        query.setParameter("idUsuario", idUsuario.intValue());
                        query.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    }
                    query.setParameter("tipoPob", patologia.getTipoPob());
                    query.setParameter("anio", anio);
                    Long poblacion = (Long) query.uniqueResult();

                    for(Object[] obj: resultado){
                        if(obj[0].toString().matches(anio.toString())){
                            itemTransf.add(obj[0]);itemTransf.add(obj[1]);
                            for(Integer sem: semanas){
                                boolean noData = true;
                                for (int i=1; i<obj.length; i+=2){
                                    if(obj[i].toString().matches(sem.toString())){
                                        itemTransf.add(obj[i+1]);
                                        if (poblacion != null) {
                                            itemTransf.add((double) Math.round((Integer.valueOf(obj[i+1].toString()).doubleValue())/poblacion*patologia.getFactor()*100)/100);
                                        }
                                        else{
                                            itemTransf.add(0);
                                        }
                                        noData = false;
                                    }
                                }
                                if(noData) { itemTransf.add(0);itemTransf.add(0.00);}
                            }
                            break;
                        }
                    }
                    if(!itemTransf.isEmpty()) { resultadoF.add(itemTransf.toArray()); itemTransf.clear();}
                }
        }
        resultadoF.add(semanas.toArray());
        return resultadoF;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getDataMapas(String codPato, String nivelUsuario, int idUsuario, String semI, String semF, String anio, boolean paisPorSILAIS){
        if (nivelUsuario.isEmpty() || semI.isEmpty() || semF.isEmpty() || anio.isEmpty())
            return new ArrayList<Object[]>();

        // Retrieve session from Hibernate
        List<Object[]> resultado = new ArrayList<Object[]>();
        List<Object[]> resultadoCasos = new ArrayList<Object[]>();
        List<Object[]> datosPoblacion = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query query = null;
        String patoQuery = null;
        String[] patos = codPato.split(",");
        //query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
        //query.setParameter("codPato", patos[0]);
        //SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
        for (int i =0; i<patos.length; i++){
            if (patoQuery == null){
                patoQuery = "inf.patologia.codigo = '"+patos[i]+"'";
            }
            else{
                patoQuery = patoQuery + " or inf.patologia.codigo = '"+patos[i]+"'";
            }
        }
        if (nivelUsuario.equals("PAIS")) {
            if (paisPorSILAIS) {
                query = session.createQuery("Select inf.silais, sum(inf.totalm + inf.totalf) as total From SiveInformeDiario inf " +
                        "where ("+patoQuery+") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anio) " +
                        "group by inf.silais order by inf.silais");
                query.setParameter("semI", Integer.parseInt(semI));
                query.setParameter("semF", semF.isEmpty()?0:Integer.parseInt(semF));
                query.setParameter("anio", anio.isEmpty()?0:Integer.parseInt(anio));

                resultado.addAll(query.list());

            }else{ //por municipios
                query = session.createQuery("Select municipio.codigoNacional as munici, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                        "where cast(inf.municipio as long) = municipio.divisionpoliticaId and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio = :anio) " +
                        "group by municipio.codigoNacional order by municipio.codigoNacional");
                query.setParameter("semI", Integer.parseInt(semI));
                query.setParameter("semF", semF.isEmpty()?0:Integer.parseInt(semF));
                query.setParameter("anio", anio.isEmpty()?0:Integer.parseInt(anio));

                resultado.addAll(query.list());

            }
        } else if (nivelUsuario.equals("SILAIS")) {
            query = session.createQuery("Select municipio.codigoNacional as munici, sum(inf.totalm+inf.totalf) as total " +
                    "From SiveInformeDiario inf, Divisionpolitica municipio, UsuarioEntidad ue " +
                    "where ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema " +
                    "and cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =ue.entidadAdtva.entidadAdtvaId and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio = :anio) " +
                    "group by municipio.codigoNacional order by municipio.codigoNacional");
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
            query.setParameter("semI", Integer.parseInt(semI));
            query.setParameter("semF", semF.isEmpty()?0:Integer.parseInt(semF));
            query.setParameter("anio", anio.isEmpty()?0:Integer.parseInt(anio));

            resultado.addAll(query.list());
        }

        return resultado;
    }

    private static final String sqlDataSinR = "select distinct noti ";

    /**
     * Método que retornar la información para generar reporte y gráfico de notificaciones sin resultado
     * @param nivelUsuario PAIS,SILAIS o UNIDAD
     * @param idUsuario usuario para validar autoridad
     * @param conSubUnidades si el nivel es UNIDAD, indica si se deben tomar en cuenta las sub unidades de las unidades asociadas al usuario
     * @return Lista de objetos a mostrar
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public List<DaNotificacion> getDataSinResultado(String nivelUsuario, int idUsuario, boolean conSubUnidades) throws ParseException {
        if (nivelUsuario.isEmpty())
            return new ArrayList<DaNotificacion>();

        Session session = sessionFactory.getCurrentSession();
        Query queryCasos = null;
        String sqlCasosEstudios= "";
        boolean filtrarUsuario = false;
        List<DaNotificacion> resultadoTemp = new ArrayList<DaNotificacion>();

        switch (nivelUsuario) {
            case "PAIS":
                queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti " +
                        "where noti.pasivo = false and noti.completa != true and mx.anulada = false and dx.anulado = false and dx.aprobada = false " +
                        " order by noti.fechaRegistro desc");

                sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti " +
                        "where noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false and noti.completa != true  " +
                        " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.idTomaMx.anulada = false " +
                        ") order by noti.fechaRegistro desc";

                break;
            case "SILAIS":
                queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti, UsuarioEntidad ue " +
                        "where noti.codSilaisAtencion.entidadAdtvaId = ue.entidadAdtva.entidadAdtvaId and ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and " +
                        "noti.pasivo = false and noti.completa != true and mx.anulada = false and dx.anulado = false and dx.aprobada = false " +
                        " order by noti.fechaRegistro desc");

                sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti, UsuarioEntidad  ue " +
                        "where noti.codSilaisAtencion.entidadAdtvaId = ue.entidadAdtva.entidadAdtvaId and ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and ue.entidadAdtva.pasivo = '0' and " +
                        "noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false and noti.completa != true " +
                        " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx,UsuarioEntidad  ue2 where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.idTomaMx.anulada = false " +
                        "and dx.idTomaMx.idNotificacion.codSilaisAtencion.entidadAdtvaId = ue2.entidadAdtva.entidadAdtvaId and ue2.usuario.usuarioId = :idUsuario and ue2.sistema.codigo = :sistema and ue2.entidadAdtva.pasivo = '0' "+
                        ") order by noti.fechaRegistro desc";

                queryCasos.setParameter("idUsuario", idUsuario);
                queryCasos.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                filtrarUsuario = true;
                break;
            case "UNIDAD":
                if (conSubUnidades) {
                    queryCasos = session.createQuery(sqlDataSinR + "From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti, UsuarioUnidad uu " +
                            "where (noti.codUnidadAtencion.unidadId = uu.unidad.unidadId or noti.codUnidadAtencion.unidadAdtva = uu.unidad.unidadId) " +
                            "and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' " +
                            "and noti.pasivo = false and dx.anulado = false and dx.aprobada = false and mx.anulada = false and noti.completa != true " +
                            " order by noti.fechaRegistro desc");

                    sqlCasosEstudios = sqlDataSinR + "From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti, UsuarioUnidad uu " +
                            "where (noti.codUnidadAtencion.unidadId = uu.unidad.unidadId or noti.codUnidadAtencion.unidadAdtva = uu.unidad.unidadId) " +
                            "and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' " +
                            "and noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false and noti.completa != true " +
                            " and noti.idNotificacion not in (" +
                            "   select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.idTomaMx.anulada = false " +
                            ") order by noti.fechaRegistro desc";
                } else {
                    queryCasos = session.createQuery(sqlDataSinR + " From DaSolicitudDx dx inner join dx.idTomaMx mx inner join mx.idNotificacion noti, UsuarioUnidad uu " +
                            "where noti.codUnidadAtencion.unidadId = uu.unidad.unidadId and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' and " +
                            " noti.pasivo = false and dx.anulado = false and dx.aprobada = false and mx.anulada = false and noti.completa != true " +
                            " order by noti.fechaRegistro desc");

                    sqlCasosEstudios = sqlDataSinR + " From DaSolicitudEstudio est inner join est.idTomaMx mx inner join mx.idNotificacion noti, UsuarioUnidad uu " +
                            "where noti.codUnidadAtencion.unidadId = uu.unidad.unidadId and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' and " +
                            "noti.pasivo = false and est.anulado = false and est.aprobada = false and mx.anulada = false and noti.completa != true " +
                            " and noti.idNotificacion not in (select dx.idTomaMx.idNotificacion.idNotificacion From DaSolicitudDx dx where dx.idTomaMx.idNotificacion.pasivo = false and dx.anulado = false and dx.aprobada = false and dx.idTomaMx.anulada = false " +
                            ") order by noti.fechaRegistro desc";
                }
                queryCasos.setParameter("idUsuario", idUsuario);
                queryCasos.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                filtrarUsuario = true;
                break;
        }
        queryCasos.setMaxResults(50);
        resultadoTemp = queryCasos.list();

        //estudios
        queryCasos = session.createQuery(sqlCasosEstudios);
        if (filtrarUsuario && !sqlCasosEstudios.isEmpty()){
            queryCasos.setParameter("idUsuario", idUsuario);
            queryCasos.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
        }
        resultadoTemp.addAll(queryCasos.list());

        return resultadoTemp;
    }

    public List<DaNotificacion> getDataEmbarazadas(String nivelUsuario, int idUsuario, boolean conSubUnidades){
        if (nivelUsuario.isEmpty())
            return new ArrayList<DaNotificacion>();

        List<DaNotificacion> resultado = new ArrayList<DaNotificacion>();
        Session session = sessionFactory.getCurrentSession();
        String query = "";
        Query q = null;
        switch (nivelUsuario) {
            case "PAIS":
                query = "select noti from DaNotificacion noti where noti.pasivo = false and noti.completa != true and embarazada.codigo = :codigoEmb" +
                        " order by noti.fechaRegistro desc";
                q = session.createQuery(query);
                q.setParameter("codigoEmb", "RESP|S"); //respuesta afirmativa
                q.setMaxResults(50);
                resultado.addAll(q.list());
                break;
            case "SILAIS":
                query = "select noti from DaNotificacion noti, UsuarioEntidad ue " +
                        "where  noti.codSilaisAtencion.entidadAdtvaId = ue.entidadAdtva.entidadAdtvaId and ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and ue.entidadAdtva.pasivo = '0' and " +
                        "noti.pasivo = false and noti.completa != true and noti.embarazada.codigo = :codigoEmb" +
                        " order by noti.fechaRegistro desc";
                q = session.createQuery(query);
                q.setParameter("codigoEmb", "RESP|S"); //respuesta afirmativa
                q.setParameter("idUsuario", idUsuario);
                q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                q.setMaxResults(50);
                resultado.addAll(q.list());
                break;
            case "UNIDAD":
                if (conSubUnidades) {
                    query = "select noti from DaNotificacion noti, Unidades uni, Usuarios usu, Sistema sis, UsuarioUnidad usuni " +
                            "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id  " +
                            "and sis.codigo = :sistema and usu.usuarioId = :idUsuario and uni.pasivo = '0' " +
                            "and (noti.codUnidadAtencion.unidadId = uni.unidadId or noti.codUnidadAtencion.unidadAdtva = uni.unidadId) " +
                            "and noti.pasivo = false and noti.completa != true and noti.embarazada.codigo = :codigoEmb" +
                            " order by noti.fechaRegistro desc";

                    q = session.createQuery(query);
                    q.setParameter("codigoEmb", "RESP|S"); //respuesta afirmativa
                    q.setParameter("idUsuario", idUsuario);
                    q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    q.setMaxResults(50);
                    resultado.addAll(q.list());
                }else{
                    query = "select noti from DaNotificacion noti, UsuarioUnidad uu " +
                            "where noti.codUnidadAtencion.unidadId = uu.unidad.unidadId and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' and " +
                            "noti.pasivo = false and noti.completa != true and noti.embarazada.codigo = :codigoEmb" +
                            " order by noti.fechaRegistro desc";
                    q = session.createQuery(query);
                    q.setParameter("codigoEmb", "RESP|S"); //respuesta afirmativa
                    q.setParameter("idUsuario", idUsuario);
                    q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    q.setMaxResults(50);
                    resultado.addAll(q.list());
                }
                break;
            default: break;
        }

        return resultado;
    }

    public List<DaNotificacion> getDataHospitalizados(String nivelUsuario, int idUsuario, boolean conSubUnidades){
        if (nivelUsuario.isEmpty())
            return new ArrayList<DaNotificacion>();

        List<DaNotificacion> resultado = new ArrayList<DaNotificacion>();
        Session session = sessionFactory.getCurrentSession();
        String query = "", query2 = "";
        Query q = null, q2 = null;
        switch (nivelUsuario) {
            case "PAIS":
                query = "from DaNotificacion noti where noti.pasivo = false and noti.completa != true and (idNotificacion in (" +
                        " select noti.idNotificacion from DaIrag irag inner join irag.idNotificacion noti where noti.pasivo = false and noti.completa != true and irag.uci = :uci)" +
                        " or idNotificacion in (" +
                        " select noti.idNotificacion from DaSindFebril sf inner join sf.idNotificacion noti where noti.pasivo = false and noti.completa != true and sf.hosp.codigo = :codigoHosp))" +
                        " order by noti.fechaRegistro desc";
                q = session.createQuery(query);
                q.setParameter("uci",1);//si estuvo en UCI
                q.setParameter("codigoHosp", "RESP|S"); //respuesta afirmativa
                q.setMaxResults(50);
                resultado.addAll(q.list());
                break;
            case "SILAIS":

                query = "from DaNotificacion noti where noti.pasivo = false and noti.completa != true and (idNotificacion in (" +
                        "select noti.idNotificacion from DaIrag irag inner join irag.idNotificacion noti, UsuarioEntidad ue " +
                        "where  noti.codSilaisAtencion.entidadAdtvaId = ue.entidadAdtva.entidadAdtvaId and ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and ue.entidadAdtva.pasivo = '0' and " +
                        "noti.pasivo = false and noti.completa != true and irag.uci = :uci )" +
                        " or idNotificacion in (" +
                        "select noti.idNotificacion from DaSindFebril sf inner join sf.idNotificacion noti, UsuarioEntidad ue " +
                        "where  noti.codSilaisAtencion.entidadAdtvaId = ue.entidadAdtva.entidadAdtvaId and ue.usuario.usuarioId = :idUsuario and ue.sistema.codigo = :sistema and ue.entidadAdtva.pasivo = '0' and " +
                        "noti.pasivo = false and noti.completa != true and sf.hosp.codigo = :codigoHosp))" +
                        " order by noti.fechaRegistro desc";
                q = session.createQuery(query);
                q.setParameter("uci",1);//si estuvo en UCI
                q.setParameter("codigoHosp", "RESP|S"); //respuesta afirmativa
                q.setParameter("idUsuario", idUsuario);
                q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                q.setMaxResults(50);
                resultado.addAll(q.list());
                break;
            case "UNIDAD":
                if (conSubUnidades) {
                    query = "from DaNotificacion noti where noti.pasivo = false and noti.completa != true and (idNotificacion in (" +
                            "select noti.idNotificacion from DaIrag irag inner join irag.idNotificacion noti, Unidades uni, Usuarios usu, Sistema sis, UsuarioUnidad usuni " +
                            "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id  " +
                            "and sis.codigo = :sistema and usu.usuarioId = :idUsuario and uni.pasivo = '0' " +
                            "and (noti.codUnidadAtencion.unidadId = uni.unidadId or noti.codUnidadAtencion.unidadAdtva = uni.unidadId) " +
                            "and noti.pasivo = false and noti.completa != true and irag.uci = :uci)" +
                            " or idNotificacion in (" +
                            "select noti.idNotificacion from DaSindFebril sf inner join sf.idNotificacion noti, Unidades uni, Usuarios usu, Sistema sis, UsuarioUnidad usuni " +
                            "where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id  " +
                            "and sis.codigo = :sistema and usu.usuarioId = :idUsuario and uni.pasivo = '0' " +
                            "and (noti.codUnidadAtencion.unidadId = uni.unidadId or noti.codUnidadAtencion.unidadAdtva = uni.unidadId) " +
                            "and noti.pasivo = false and noti.completa != true and sf.hosp.codigo = :codigoHosp "+
                            "))  order by noti.fechaRegistro desc";
                    q = session.createQuery(query);
                    q.setParameter("uci",1);//si estuvo en UCI
                    q.setParameter("codigoHosp", "RESP|S"); //respuesta afirmativa
                    q.setParameter("idUsuario", idUsuario);
                    q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    q.setMaxResults(50);
                    resultado.addAll(q.list());
                }else{
                    query = "from DaNotificacion noti where noti.pasivo = false and noti.completa != true and (idNotificacion in (" +
                            "select noti.idNotificacion from DaIrag irag inner join irag.idNotificacion noti, UsuarioUnidad uu " +
                            "where noti.codUnidadAtencion.unidadId = uu.unidad.unidadId and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' and " +
                            "noti.pasivo = false and noti.completa != true and irag.uci = :uci)" +
                            " or idNotificacion in (" +
                            "select noti.idNotificacion from DaSindFebril sf inner join sf.idNotificacion noti, UsuarioUnidad uu " +
                            "where noti.codUnidadAtencion.unidadId = uu.unidad.unidadId and uu.usuario.usuarioId = :idUsuario and uu.sistema.codigo = :sistema and uu.unidad.pasivo = '0' and " +
                            "noti.pasivo = false and noti.completa != true and sf.hosp.codigo = :codigoHosp" +
                            "))  order by noti.fechaRegistro desc";
                    q = session.createQuery(query);
                    q.setParameter("uci",1);//si estuvo en UCI
                    q.setParameter("codigoHosp", "RESP|S"); //respuesta afirmativa
                    q.setParameter("idUsuario", idUsuario);
                    q.setParameter("sistema", ConstantsSecurity.SYSTEM_CODE);
                    q.setMaxResults(50);
                    resultado.addAll(q.list());
                }
                break;
            default: break;
        }


        return resultado;
    }
}
