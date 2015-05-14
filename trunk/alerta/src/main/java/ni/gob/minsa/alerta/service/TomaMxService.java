package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.examen.CatalogoExamenes;
import ni.gob.minsa.alerta.domain.muestra.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics on 11-05-14.
 */
@Service("tomaMxService")
@Transactional
public class TomaMxService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public void updateTomaMx(DaTomaMx dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
            else
                throw new Exception("Objeto Orden Examen es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     *
     *
     * @param codMx
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Examen_TipoMxTipoNoti> getExamenes(String codMx, String tipoNoti) throws Exception {
        String query = "select ex from Examen_TipoMxTipoNoti ex where ex.tipoMxTipoNotificacion.tipoMx.idTipoMx = :codMx and ex.tipoMxTipoNotificacion.tipoNotificacion.codigo = :tipoNoti" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
         q.setString("codMx", codMx);
        q.setString("tipoNoti", tipoNoti);
        return q.list();
    }



    /**
     *Retorna una lista de dx segun tipoMx y tipo Notificacion
     * @param codMx tipo de Mx
     * @param tipoNoti tipo Notificacion
     *
     */
    @SuppressWarnings("unchecked")
    public List<Dx_TipoMx_TipoNoti> getDx(String codMx, String tipoNoti) throws Exception {
        String query = "select dx from Dx_TipoMx_TipoNoti dx where dx.tipoMx_tipoNotificacion.tipoMx.idTipoMx = :codMx and dx.tipoMx_tipoNotificacion.tipoNotificacion.codigo = :tipoNoti" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codMx", codMx);
        q.setString("tipoNoti", tipoNoti);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<CatalogoExamenes> getCatalogoExamenes(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM CatalogoExamenes cat where cat.pasivo = false");
        //retrieve all
        return query.list();

    }

    /**
     * Agrega Registro de Toma de Muestra
     */
    public void addTomaMx(DaTomaMx toma) {
        Session session = sessionFactory.getCurrentSession();
        session.save(toma);
    }

    /**
     * Agrega Orden
     */
    public void addSolicitudDx(DaSolicitudDx orden) {
        Session session = sessionFactory.getCurrentSession();
        session.save(orden);
    }


    /**
     * Retorna examen
     * @param id
     */
    public CatalogoExamenes getExamById(String id){
        String query = "from CatalogoExamenes where idExamen = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return (CatalogoExamenes)q.uniqueResult();
    }

    /**
     * Retorna examen
     * @param id
     */
    public Catalogo_Dx getDxById(String id){
        String query = "from Catalogo_Dx where idDiagnostico = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return (Catalogo_Dx)q.uniqueResult();
    }

    /**
     * Retorna toma de muestra
     * @param id
     */
    public DaTomaMx getTomaMxById(String id){
        String query = "from DaTomaMx where idTomaMx = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return (DaTomaMx)q.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<TipoMx_TipoNotificacion> getTipoMxByTipoNoti(String codigo){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoMx_TipoNotificacion tmx where tmx.tipoNotificacion = :codigo and tmx.pasivo= false");
        query.setString("codigo", codigo);
        //retrieve all
        return query.list();

    }

    /**
     * Retorna tipoMx
     * @param id
     */
    public TipoMx getTipoMxById(String id){
        String query = "from TipoMx where idTipoMx = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return (TipoMx)q.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<DaTomaMx> getTomaMxByIdNoti(String idNotificacion){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM DaTomaMx tmx where tmx.idNotificacion = :idNotificacion");
        query.setString("idNotificacion", idNotificacion);
        //retrieve all
        return query.list();

    }

    public DaTomaMx getTomaMxByCodUnicoMx(String codigoUnicoMx){
        String query = "from DaTomaMx as a where codigoUnicoMx= :codigoUnicoMx";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codigoUnicoMx", codigoUnicoMx);
        return  (DaTomaMx)q.uniqueResult();
    }

    /*********************************************************/
    // ESTUDIOS
    /*********************************************************/
    /**
     *Retorna una lista de estudios segun tipoMx y tipo Notificacion
     * @param codTipoMx código del tipo de Mx
     * @param codTipoNoti código del tipo Notificacion
     *
     */
    @SuppressWarnings("unchecked")
    public List<Estudio_TipoMx_TipoNoti> getEstudiosByTipoMxTipoNoti(String codTipoMx, String codTipoNoti) throws Exception {
        String query = "select est from Estudio_TipoMx_TipoNoti est " +
                "where est.tipoMx_tipoNotificacion.tipoMx.idTipoMx = :codTipoMx " +
                "and est.tipoMx_tipoNotificacion.tipoNotificacion.codigo = :codTipoNoti" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codTipoMx", codTipoMx);
        q.setString("codTipoNoti", codTipoNoti);
        return q.list();
    }

    /**
     * Obtiene un estudio
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public Catalogo_Estudio getEstudioById(String id){
        String query = "from Catalogo_Estudio where idEstudio = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return (Catalogo_Estudio)q.uniqueResult();
    }

    /**
     * Agregar solicitud de estudio, para la toma de muestra
     * @param solicitud objeto a guardra
     */
    public void addSolicitudEstudio(DaSolicitudEstudio solicitud) {
        Session session = sessionFactory.getCurrentSession();
        session.save(solicitud);
    }

    /**
     * Obtiene un estudio
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public List<DaSolicitudDx> getRutinasByIdMX(String id){
        String query = "select sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx where sdx.labProcesa is null and mx.idTomaMx  = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return q.list();
    }

    public void updateSolicitudDx(DaSolicitudDx dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
            else
                throw new Exception("Objeto Solicitud Dx es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

}
