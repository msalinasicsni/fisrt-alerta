package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.DaSolicitudEstudio;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import ni.gob.minsa.alerta.domain.resultados.DetalleResultadoFinal;
import ni.gob.minsa.alerta.utilities.reportes.ResultadoSolicitud;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIRSTICT on 3/20/2015.
 * V1.0
 */
@Service("resultadoFinalService")
@Transactional
public class ResultadoFinalService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<DaSolicitudDx> getSolicitudesDxByIdNotificacion(String idNotificacion){
        Session session = sessionFactory.getCurrentSession();
       Criteria crit = session.createCriteria(DaSolicitudDx.class, "diagnostico");
        crit.createAlias("diagnostico.idTomaMx","tomaMx");
        //crit.createAlias("tomaMx.estadoMx","estado");
        crit.createAlias("tomaMx.idNotificacion", "noti");
        //siempre se tomam las muestras que no estan anuladas
        crit.add( Restrictions.and(
                        Restrictions.eq("tomaMx.anulada", false))
        );
        //siempre se tomam las rutinas que no son control de calidad
        crit.add( Restrictions.and(
                        Restrictions.eq("diagnostico.controlCalidad", false))
        );
        crit.add( Restrictions.and(
                        Restrictions.eq("noti.idNotificacion", idNotificacion))
        );
        crit.add(Restrictions.and(Restrictions.eq("diagnostico.aprobada", true)));
        crit.add(Restrictions.eq("diagnostico.anulado",false));
        return crit.list();
    }

    public List<DaSolicitudEstudio> getSolicitudesEstByIdNotificacion(String idNotificacion){
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(DaSolicitudEstudio.class, "estudio");
        crit.createAlias("estudio.idTomaMx","tomaMx");
        //crit.createAlias("tomaMx.estadoMx","estado");
        crit.createAlias("tomaMx.idNotificacion", "noti");
        //siempre se tomam las muestras que no estan anuladas
        crit.add( Restrictions.and(
                        Restrictions.eq("tomaMx.anulada", false))
        );
        crit.add( Restrictions.and(
                        Restrictions.eq("noti.idNotificacion", idNotificacion))
        );
        crit.add(Restrictions.and(Restrictions.eq("estudio.aprobada", true)));
        crit.add(Restrictions.and(Restrictions.eq("estudio.anulado", false)));

        return crit.list();
    }

    /**
     *
     * @param idSolicitud
     * @return
     */
    public List<DetalleResultadoFinal> getDetResActivosBySolicitud(String idSolicitud){
        List<DetalleResultadoFinal> resultadoFinals = new ArrayList<DetalleResultadoFinal>();
        Session session = sessionFactory.getCurrentSession();
        String query = "select a from DetalleResultadoFinal as a inner join a.solicitudDx as r where a.pasivo = false and r.idSolicitudDx = :idSolicitud ";
        Query q = session.createQuery(query);
        q.setParameter("idSolicitud", idSolicitud);
        resultadoFinals = q.list();
        String query2 = "select a from DetalleResultadoFinal as a inner join a.solicitudEstudio as r where a.pasivo = false and r.idSolicitudEstudio = :idSolicitud ";
        Query q2 = session.createQuery(query2);
        q2.setParameter("idSolicitud", idSolicitud);
        resultadoFinals.addAll(q2.list());
        return  resultadoFinals;
    }

    public List<ResultadoSolicitud> getDetResActivosBySolicitudV2(String idSolicitud){
        List<ResultadoSolicitud> resultadoFinals = new ArrayList<ResultadoSolicitud>();
        Session session = sessionFactory.getCurrentSession();
        String query = "select a.idDetalle as idDetalle, coalesce((select rs.nombre from RespuestaSolicitud rs where rs.idRespuesta = a.respuesta.idRespuesta), null) as respuesta, coalesce((select rs.codigo from Catalogo rs where rs.codigo = a.respuesta.concepto.tipo.codigo), null) as tipo, a.valor as valor," +
                " coalesce((select rs.nombre from RespuestaExamen rs where rs.idRespuesta = a.respuestaExamen.idRespuesta), null) as respuestaExamen, coalesce((select rs.codigo from Catalogo rs where rs.codigo = a.respuestaExamen.concepto.tipo.codigo), null) as tipoExamen " +
                "from DetalleResultadoFinal as a inner join a.solicitudDx as r where a.pasivo = false and r.idSolicitudDx = :idSolicitud ";

        Query q = session.createQuery(query);
        q.setParameter("idSolicitud", idSolicitud);
        q.setResultTransformer(Transformers.aliasToBean(ResultadoSolicitud.class));
        resultadoFinals = q.list();
        if (resultadoFinals.size()<=0) {
            String query2 = "select a.idDetalle as idDetalle, coalesce((select rs.nombre from RespuestaSolicitud rs where rs.idRespuesta = a.respuesta.idRespuesta), null) as respuesta, coalesce((select rs.codigo from Catalogo rs where rs.codigo = a.respuesta.concepto.tipo.codigo), null) as tipo, a.valor as valor," +
                    " coalesce((select rs.nombre from RespuestaExamen rs where rs.idRespuesta = a.respuestaExamen.idRespuesta), null) as respuestaExamen, coalesce((select rs.codigo from Catalogo rs where rs.codigo = a.respuestaExamen.concepto.tipo.codigo), null) as tipoExamen " +
                    "from DetalleResultadoFinal as a inner join a.solicitudEstudio as r where a.pasivo = false and r.idSolicitudEstudio = :idSolicitud ";
            Query q2 = session.createQuery(query2);
            q2.setParameter("idSolicitud", idSolicitud);
            q2.setResultTransformer(Transformers.aliasToBean(ResultadoSolicitud.class));
            resultadoFinals.addAll(q2.list());
        }
        return  resultadoFinals;
    }

    /**
     * Obtiene un catalogo lista según el id indicado
     * @param id del Catalogo Lista a obtener
     * @return Catalogo_lista
     */
    public Catalogo_Lista getCatalogoLista(String id){
        String query = "from Catalogo_Lista as c where c.idCatalogoLista= :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return  (Catalogo_Lista)q.uniqueResult();
    }


}
