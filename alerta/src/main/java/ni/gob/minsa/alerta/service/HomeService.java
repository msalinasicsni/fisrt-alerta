package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.sive.SivePatologiasTipo;
import ni.gob.minsa.alerta.utilities.ConstantsSecurity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        for (int i = 0; i<=(Integer.parseInt(semF)-Integer.parseInt(semI)); i++){
            semanas.add(Integer.parseInt(semI)+i);
        }
        for (int i = 0; i<=(Integer.parseInt(anioF)-Integer.parseInt(anioI)); i++){
            anios.add(Integer.parseInt(anioI)+i);
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
        query.setParameter("semF", Integer.parseInt(semF));
        query.setParameter("anioI", Integer.parseInt(anioI));
        query.setParameter("anioF", Integer.parseInt(anioF));
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
                query.setParameter("semF", Integer.parseInt(semF));
                query.setParameter("anio", Integer.parseInt(anio));

                resultado.addAll(query.list());

            }else{ //por municipios
                query = session.createQuery("Select municipio.codigoNacional as munici, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                        "where cast(inf.municipio as long) = municipio.divisionpoliticaId and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio = :anio) " +
                        "group by municipio.codigoNacional order by municipio.codigoNacional");
                query.setParameter("semI", Integer.parseInt(semI));
                query.setParameter("semF", Integer.parseInt(semF));
                query.setParameter("anio", Integer.parseInt(anio));

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
            query.setParameter("semF", Integer.parseInt(semF));
            query.setParameter("anio", Integer.parseInt(anio));

            resultado.addAll(query.list());
        }

        return resultado;
    }

}
