package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.OrdenExamen;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
@Service("ordenExamenMxService")
@Transactional
public class OrdenExamenMxService {

    private Logger logger = LoggerFactory.getLogger(OrdenExamenMxService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public OrdenExamen getOrdenExamenById(String idOrdenExamen){
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("from OrdenExamen where idOrdenExamen =:idOrdenExamen");
        q.setParameter("idOrdenExamen",idOrdenExamen);
        return (OrdenExamen)q.uniqueResult();
    }


      /**
     * Obtiene las ordenes de examen no anuladas para la muestra. Una toma no puede tener de dx y de estudios, es uno u otro.
     * @param idTomaMx id de la toma a consultar
     * @return List<OrdenExamen>
     */
    public List<OrdenExamen> getOrdenesExamenNoAnuladasByIdMx(String idTomaMx){
        Session session = sessionFactory.getCurrentSession();
        List<OrdenExamen> ordenExamenList = new ArrayList<OrdenExamen>();
        //se toman las que son de diagnóstico.
        Query q = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudDx.idTomaMx as mx where mx.idTomaMx =:idTomaMx and oe.anulado = false ");
        q.setParameter("idTomaMx",idTomaMx);
        ordenExamenList = q.list();
        //se toman las que son de estudio
        Query q2 = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudEstudio.idTomaMx as mx where mx.idTomaMx =:idTomaMx and oe.anulado = false ");
        q2.setParameter("idTomaMx",idTomaMx);
        ordenExamenList.addAll(q2.list());
        return ordenExamenList;
    }




    public List<OrdenExamen> getOrdExamenNoAnulByIdMxIdEstIdExamen(String idTomaMx, int idEstudio, int idExamen){
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudEstudio as sdx inner join sdx.idTomaMx as mx where mx.idTomaMx =:idTomaMx " +
                "and sdx.tipoEstudio.idEstudio = :idEstudio and oe.codExamen.idExamen = :idExamen and oe.anulado = false ");
        q.setParameter("idTomaMx",idTomaMx);
        q.setParameter("idEstudio",idEstudio);
        q.setParameter("idExamen",idExamen);
        return q.list();
    }

    public List<OrdenExamen> getOrdenesExamenNoAnuladasByIdSolicitud(String idSolicitud){
        Session session = sessionFactory.getCurrentSession();
        List<OrdenExamen> ordenExamenList = new ArrayList<OrdenExamen>();
        //se toman las que son de diagnóstico.
        Query q = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudDx as sdx where sdx.idSolicitudDx =:idSolicitud and oe.anulado = false ");
        q.setParameter("idSolicitud",idSolicitud);
        ordenExamenList = q.list();
        //se toman las que son de estudio
        Query q2 = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudEstudio as se where se.idSolicitudEstudio =:idSolicitud and oe.anulado = false ");
        q2.setParameter("idSolicitud",idSolicitud);
        ordenExamenList.addAll(q2.list());
        return ordenExamenList;
    }





    public List<OrdenExamen> getOrdenesExamenByIdSolicitud(String idSolicitud){
        Session session = sessionFactory.getCurrentSession();
        List<OrdenExamen> ordenExamenList = new ArrayList<OrdenExamen>();
        //se toman las que son de diagnóstico.
        Query q = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudDx as sdx where sdx.idSolicitudDx =:idSolicitud ");
        q.setParameter("idSolicitud",idSolicitud);
        ordenExamenList = q.list();
        //se toman las que son de estudio
        Query q2 = session.createQuery("select oe from OrdenExamen as oe inner join oe.solicitudEstudio as se where se.idSolicitudEstudio =:idSolicitud ");
        q2.setParameter("idSolicitud",idSolicitud);
        ordenExamenList.addAll(q2.list());
        return ordenExamenList;
    }
}
