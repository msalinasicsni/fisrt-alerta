package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.DaSolicitudDx;
import ni.gob.minsa.alerta.domain.muestra.DatoSolicitud;
import ni.gob.minsa.alerta.domain.muestra.DatoSolicitudDetalle;
import ni.gob.minsa.alerta.domain.concepto.Catalogo_Lista;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics.
 */
@Service("datosSolicitudService")
@Transactional
public class DatosSolicitudService {

    private Logger logger = LoggerFactory.getLogger(DatosSolicitudService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public DatosSolicitudService() {
    }


    public List<DatoSolicitud> getDatosRecepcionDxByIdSolicitud(Integer idSolicitud){
        String query = "from DatoSolicitud as a where a.diagnostico.idDiagnostico = :idSolicitud order by orden asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("idSolicitud", idSolicitud);
        return q.list();
    }

    public DatoSolicitud getDatoRecepcionSolicitudById(Integer idConceptoSol){
        String query = "from DatoSolicitud as a where idConceptoSol =:idConceptoSol";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("idConceptoSol", idConceptoSol);
        return (DatoSolicitud)q.uniqueResult();
    }

    /**
     * Agrega o Actualiza un Registro de DatoSolicitud
     * @param dto Objeto a actualizar
     * @throws Exception
     */
    public void saveOrUpdateDatoRecepcion(DatoSolicitud dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.saveOrUpdate(dto);
            }
            else
                throw new Exception("Objeto DatoSolicitud es NULL");
        }catch (Exception ex){
            logger.error("Error al actualizar o agregar DatoSolicitud",ex);
            throw ex;
        }
    }

    /**
     * Agrega o Actualiza un Registro de ConceptoSolicitud
     * @param dto Objeto a actualizar
     * @throws Exception
     */
    public void saveOrUpdateDetalleDatoRecepcion(DatoSolicitudDetalle dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.saveOrUpdate(dto);
            }
            else
                throw new Exception("Objeto DatoSolicitudDetalle es NULL");
        }catch (Exception ex){
            logger.error("Error al actualizar o agregar DatoSolicitudDetalle",ex);
            throw ex;
        }
    }

    public List<Catalogo_Lista> getCatalogoListaConceptoByIdDx(Integer idDx) throws Exception {
        String query = "Select a from Catalogo_Lista as a inner join a.idConcepto tdl , DatoSolicitud as r inner join r.concepto tdc " +
                "where a.pasivo = false and tdl.idConcepto = tdc.idConcepto and r.diagnostico.idDiagnostico =:idDx" +
                " order by  a.valor";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("idDx",idDx);
        return q.list();
    }

    public List<DatoSolicitud> getDatosRecepcionActivosDxByIdSolicitud(Integer idSolicitud){
        String query = "from DatoSolicitud as a where a.diagnostico.idDiagnostico = :idSolicitud and pasivo = false order by orden asc";

        Session session = sessionFactory.getCurrentSession();

        Query q = session.createQuery(query);
        q.setParameter("idSolicitud", idSolicitud);
        return q.list();
    }

    public List<DatoSolicitudDetalle> getDatosSolicitudDetalleBySolicitud(String idSolicitud){
        Session session = sessionFactory.getCurrentSession();
        String query = "select a from DatoSolicitudDetalle as a inner join a.solicitudDx as r where r.idSolicitudDx = :idSolicitud ";
        Query q = session.createQuery(query);
        q.setParameter("idSolicitud", idSolicitud);
        return q.list();
    }

    public List<DatoSolicitud> getDatosRecepcionActivosDxByIdSolicitudes(String idSolicitudes){
        String query = "from DatoSolicitud as a where a.diagnostico.idDiagnostico in ("+idSolicitudes+") and pasivo = false order by orden asc";

        Session session = sessionFactory.getCurrentSession();

        Query q = session.createQuery(query);
        return q.list();
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

    public List<DaSolicitudDx> getSolicitudesDxByIdNotificacion(String idNotificacion){
        Session session = sessionFactory.getCurrentSession();
        Soundex varSoundex = new Soundex();
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

        return crit.list();
    }
}
