package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.audit.AuditTrail;
import ni.gob.minsa.alerta.domain.examen.CatalogoExamenes;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.solicitante.Solicitante;
import ni.gob.minsa.alerta.utilities.reportes.Solicitud;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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
        session.saveOrUpdate(toma);
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
        Query query = session.createQuery("FROM TipoMx_TipoNotificacion tmx where tmx.tipoNotificacion = :codigo and tmx.pasivo= false order by  tmx.tipoMx.nombre");
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

    @SuppressWarnings("unchecked")
    public List<DaTomaMx> getTomaMxActivaByIdNoti(String idNotificacion){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM DaTomaMx tmx where tmx.idNotificacion = :idNotificacion and tmx.anulada = false order by fechaHTomaMx asc");
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

    public Integer updateLabProcesaDxByMx(String labo, String idTomaMx) throws Exception{
        // Retrieve session from Hibernate
        Session s = null;
        int updateEntities = 0;
        try {
            s = sessionFactory.openSession();
            Transaction tx = s.beginTransaction();

            String hqlDx = "update DaSolicitudDx set labProcesa.codigo=:labo where idTomaMx.idTomaMx = :idTomaMx ";
            updateEntities = s.createQuery(hqlDx)
                    .setString("idTomaMx", idTomaMx)
                    .setString("labo", labo)
                    .executeUpdate();
            tx.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }finally {
            if (s!=null) s.close();
        }

        return updateEntities;
    }

    public Integer updateEnvioEnTomaMx(String estado, String idEnvio, DaTomaMx tomaMx, String username){
        // Retrieve session from Hibernate
        Session s = null;
        int updateEntities = 0;
        try {
            s = sessionFactory.openSession();
            Transaction tx = s.beginTransaction();//envio.idEnvio = :idEnvio,

            String hqlDx = "update da_tomamx set ID_ENVIO = :idEnvio, COD_ESTADOMX = :estado where ID_TOMAMX = :idTomaMx ";
            updateEntities = s.createSQLQuery(hqlDx)
                    .setString("idEnvio", idEnvio)
                    .setString("estado", estado)
                    .setString("idTomaMx", tomaMx.getIdTomaMx())
                    .executeUpdate();


            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setEntityId(tomaMx.getIdTomaMx());
            auditTrail.setEntityName(tomaMx.getClass().getName());
            auditTrail.setEntityProperty("estadoMx");
            auditTrail.setEntityPropertyNewValue(estado);
            auditTrail.setEntityPropertyOldValue(tomaMx.getEstadoMx().getCodigo());
            auditTrail.setOperationType("UPDATE");
            auditTrail.setOperationDate(new Date());
            auditTrail.setUsername(username);
            s.save(auditTrail);

            auditTrail = new AuditTrail();
            auditTrail.setEntityId(tomaMx.getIdTomaMx());
            auditTrail.setEntityName(tomaMx.getClass().getName());
            auditTrail.setEntityProperty("envio");
            auditTrail.setEntityPropertyNewValue(idEnvio);
            auditTrail.setEntityPropertyOldValue((tomaMx.getEnvio() != null ? tomaMx.getEnvio().getIdEnvio() : null));
            auditTrail.setOperationType("UPDATE");
            auditTrail.setOperationDate(new Date());
            auditTrail.setUsername(username);
            s.save(auditTrail);

            tx.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }finally {
            if (s!=null) s.close();
        }

        return updateEntities;
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
    public List<Estudio_TipoMx_TipoNoti> getEstudiosByTipoMxTipoNoti(String codTipoMx, String codTipoNoti, Long idUnidadSalud) throws Exception {
        String query = "select est from Estudio_TipoMx_TipoNoti est, Estudio_UnidadSalud eu " +
                "where est.tipoMx_tipoNotificacion.tipoMx.idTipoMx = :codTipoMx " +
                "and est.tipoMx_tipoNotificacion.tipoNotificacion.codigo = :codTipoNoti " +
                "and est.estudio.idEstudio = eu.estudio.idEstudio "+
                "and eu.unidad.unidadId = :idUnidadSalud" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codTipoMx", codTipoMx);
        q.setString("codTipoNoti", codTipoNoti);
        q.setParameter("idUnidadSalud",idUnidadSalud);
        return q.list();
    }

    /**
     * Obtiene un estudio
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public Catalogo_Estudio getEstudioById(Integer id){
        String query = "from Catalogo_Estudio where idEstudio = :id";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("id", id);
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
        String query = "select sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx where sdx.anulado = false and sdx.labProcesa is null and mx.idTomaMx  = :id";
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

    @SuppressWarnings("unchecked")
    public List<DaTomaMx> getTomaMxByFiltro(FiltroMx filtro){
        Session session = sessionFactory.getCurrentSession();
        Soundex varSoundex = new Soundex();
        Criteria crit = session.createCriteria(DaTomaMx.class, "tomaMx");
        crit.createAlias("tomaMx.estadoMx","estado");
        crit.createAlias("tomaMx.idNotificacion", "notifi");
        //siempre se tomam las muestras que no estan anuladas
        crit.add( Restrictions.and(
                        Restrictions.eq("tomaMx.anulada", false))
        );
        // se filtra por nombre y apellido persona
        if (filtro.getNombreApellido()!=null) {
            //crit.createAlias("notifi.persona", "person");
            String nombreApellido = "";
            try {
                nombreApellido = URLDecoder.decode(filtro.getNombreApellido(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String[] partes = nombreApellido.split(" ");
            String[] partesSnd = nombreApellido.split(" ");
            for (int i = 0; i < partes.length; i++) {
                try {
                    partesSnd[i] = varSoundex.encode(partes[i]);
                } catch (IllegalArgumentException e) {
                    partesSnd[i] = "0000";
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < partes.length; i++) {
                Junction conditGroup = Restrictions.disjunction();
                conditGroup.add(Subqueries.propertyIn("notifi.persona.personaId", DetachedCriteria.forClass(SisPersona.class, "person")
                        .add(Restrictions.or(Restrictions.ilike("person.primerNombre", "%" + partes[i] + "%"))
                                .add(Restrictions.or(Restrictions.ilike("person.primerApellido", "%" + partes[i] + "%"))
                                        .add(Restrictions.or(Restrictions.ilike("person.segundoNombre", "%" + partes[i] + "%"))
                                                .add(Restrictions.or(Restrictions.ilike("person.segundoApellido", "%" + partes[i] + "%"))
                                                        .add(Restrictions.or(Restrictions.ilike("person.sndNombre", "%" + partesSnd[i] + "%")))))))
                        .setProjection(Property.forName("personaId"))))
                        .add(Subqueries.propertyIn("notifi.solicitante.idSolicitante", DetachedCriteria.forClass(Solicitante.class,"solicitante")
                                .add(Restrictions.ilike("solicitante.nombre", "%" + partes[i] + "%"))
                                .setProjection(Property.forName("idSolicitante"))));
                crit.add(conditGroup);
            }
        }
        //se filtra por SILAIS
        if (filtro.getCodSilais()!=null){
            crit.createAlias("notifi.codSilaisAtencion","silais");
            crit.add( Restrictions.and(
                            Restrictions.eq("silais.codigo", Long.valueOf(filtro.getCodSilais())))
            );
        }
        //se filtra por unidad de salud
        if (filtro.getCodUnidadSalud()!=null){
            crit.createAlias("notifi.codUnidadAtencion","unidadS");
            crit.add( Restrictions.and(
                            Restrictions.eq("unidadS.codigo", Long.valueOf(filtro.getCodUnidadSalud())))
            );
        }
        //Se filtra por rango de fecha de toma de muestra
        if (filtro.getFechaInicioTomaMx()!=null && filtro.getFechaFinTomaMx()!=null){
            crit.add( Restrictions.and(
                            Restrictions.between("tomaMx.fechaHTomaMx", filtro.getFechaInicioTomaMx(),filtro.getFechaFinTomaMx()))
            );
        }
        //Se filtra por rango de fecha de envio de muestra
        if (filtro.getFechaInicioEnvio()!=null && filtro.getFechaFinEnvio()!=null){
            crit.createAlias("tomaMx.envio","envio");
            crit.add(Subqueries.propertyIn("envio.idEnvio", DetachedCriteria.forClass(DaEnvioMx.class)
                    .add(Restrictions.between("fechaHoraEnvio", filtro.getFechaInicioEnvio(),filtro.getFechaFinEnvio()))
                    .setProjection(Property.forName("idEnvio"))));
        }
        //nombre solicitud
        if (filtro.getNombreSolicitud() != null) {
            if (filtro.getCodTipoSolicitud() != null) {
                if (filtro.getCodTipoSolicitud().equals("Estudio")) {
                    crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudEstudio.class)
                            .add(Restrictions.eq("anulado",false))
                            .createAlias("tipoEstudio", "estudio")
                            .add(Restrictions.ilike("estudio.nombre", "%" + filtro.getNombreSolicitud() + "%"))
                            .setProjection(Property.forName("idTomaMx.idTomaMx"))));
                } else {
                    crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudDx.class)
                            .add(Restrictions.eq("anulado",false))
                            .createAlias("codDx", "dx")
                            .add(Restrictions.ilike("dx.nombre", "%" + filtro.getNombreSolicitud() + "%"))
                            .setProjection(Property.forName("idTomaMx.idTomaMx"))));
                }
            } else {

                Junction conditGroup = Restrictions.disjunction();
                conditGroup.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudEstudio.class)
                        .add(Restrictions.eq("anulado",false))
                        .createAlias("tipoEstudio", "estudio")
                        .add(Restrictions.ilike("estudio.nombre", "%" + filtro.getNombreSolicitud() + "%"))
                        .setProjection(Property.forName("idTomaMx.idTomaMx"))))
                        .add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudDx.class)
                                .add(Restrictions.eq("anulado",false))
                                .createAlias("codDx", "dx")
                                .add(Restrictions.ilike("dx.nombre", "%" + filtro.getNombreSolicitud() + "%"))
                                .setProjection(Property.forName("idTomaMx.idTomaMx"))));

                crit.add(conditGroup);
            }
        }
        return crit.list();
    }

    public List<DaSolicitudEstudio> getSolicitudesEstudioByIdTomaMx(String idTomaMx){
        String query = "from DaSolicitudEstudio where anulado = false and idTomaMx.idTomaMx = :idTomaMx ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        return q.list();
    }

    /**
     * Obtiene un estudio
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public List<DaSolicitudDx> getSolicitudesDxByIdMX(String id){
        String query = "select sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx where sdx.anulado = false and mx.idTomaMx  = :id ORDER BY sdx.fechaHSolicitud";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return q.list();
    }

    /**
     * Obtiene los dx iniciales solicitados en la toma de mx
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public List<DaSolicitudDx> getSolicitudesDxInicialesByIdMX(String id){
        String query = "select sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx where sdx.anulado = false and sdx.inicial = true and mx.idTomaMx  = :id ORDER BY sdx.fechaHSolicitud";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        return q.list();
    }
    public List<Catalogo_Dx> getCatalogosDx(){
        String query = "from Catalogo_Dx where pasivo = false order by nombre asc";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return q.list();
    }

    public DaSolicitudDx getSolicitudDxByIdSolicitud(String idSolicitud){
        String query = "from DaSolicitudDx where idSolicitudDx = :idSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idSolicitud",idSolicitud);
        return (DaSolicitudDx)q.uniqueResult();
    }

    public DaSolicitudEstudio getSolicitudEstByIdSolicitud(String idSolicitud){
        String query = "from DaSolicitudEstudio where idSolicitudEstudio = :idSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idSolicitud",idSolicitud);
        return (DaSolicitudEstudio)q.uniqueResult();
    }

    public Integer bajaSolicitudDx(String userName, String idSolicitud, String causa) {
        // Retrieve session from Hibernate
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        int updateEntities = 0,updateEntities2=0,updateEntities3=0,updateEntities4=0;
        try {

            String hqlBajaResExam = "update DetalleResultado o set pasivo=true, fechahAnulacion = current_date, razonAnulacion = :causa " +
                    "where examen.idOrdenExamen in (select idOrdenExamen from OrdenExamen where solicitudDx.idSolicitudDx = :idSolicitud)";
            updateEntities3 = s.createQuery(hqlBajaResExam)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud).executeUpdate();

            String hqlBajaExam = "update OrdenExamen ex set anulado=true, fechaAnulacion = current_date, causaAnulacion = :causa where solicitudDx.idSolicitudDx = :idSolicitud and anulado = false ";
            updateEntities = s.createQuery(hqlBajaExam)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            String hqlBajaResDx = "update DetalleResultadoFinal ex set pasivo=true, fechahAnulacion = current_date, razonAnulacion = :causa where solicitudDx.idSolicitudDx = :idSolicitud and pasivo = false ";
            updateEntities4 = s.createQuery(hqlBajaResDx)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            String hqlBajaDx = "update DaSolicitudDx ex set anulado=true, fechaAnulacion = current_date, aprobada = false, usuarioAprobacion = null, fechaAprobacion = null, causaAnulacion = :causa where idSolicitudDx = :idSolicitud";
            updateEntities2 = s.createQuery(hqlBajaDx)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            tx.commit();
        }catch (Exception ex){
            tx.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            s.close();
        }
        return updateEntities+updateEntities2+updateEntities3+updateEntities4;
    }

    public Integer bajaSolicitudEstudio(String userName, String idSolicitud, String causa) {
        // Retrieve session from Hibernate
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        int updateEntities = 0,updateEntities2=0,updateEntities3=0,updateEntities4=0;
        try {

            String hqlBajaResExam = "update DetalleResultado o set pasivo=true, fechahAnulacion = current_date, razonAnulacion = :causa " +
                    "where examen.idOrdenExamen in (select idOrdenExamen from OrdenExamen where solicitudEstudio.idSolicitudEstudio = :idSolicitud)";
            updateEntities3 = s.createQuery(hqlBajaResExam)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            String hqlBajaExam = "update OrdenExamen ex set anulado=true, fechaAnulacion = current_date, causaAnulacion = :causa where solicitudEstudio.idSolicitudEstudio = :idSolicitud";
            updateEntities2 = s.createQuery(hqlBajaExam)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            String hqlBajaResEst = "update DetalleResultadoFinal ex set pasivo=true, fechahAnulacion = current_date, razonAnulacion = :causa where solicitudEstudio.idSolicitudEstudio = :idSolicitud";
            updateEntities4 = s.createQuery(hqlBajaResEst)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            String hqlBajaEst = "update DaSolicitudEstudio ex set anulado=true, fechaAnulacion = current_date, aprobada = false, usuarioAprobacion = null, fechaAprobacion = null, causaAnulacion = :causa where idSolicitudEstudio = :idSolicitud";
            updateEntities2 = s.createQuery(hqlBajaEst)
                    .setString("causa",causa+". Anulado por: "+userName)
                    .setString("idSolicitud", idSolicitud)
                    .executeUpdate();

            tx.commit();
        }catch (Exception ex){
            tx.rollback();
            throw ex;
        }finally {
            s.close();
        }
        return updateEntities+updateEntities2+updateEntities3+updateEntities4;
    }

    public DaSolicitudDx getSolicitudesDxByMxDx(String idTomaMx,  Integer idDiagnostico){
        String query = "from DaSolicitudDx where anulado = false and idTomaMx.idTomaMx = :idTomaMx " +
                "and codDx.idDiagnostico = :idDiagnostico ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        q.setParameter("idDiagnostico",idDiagnostico);
        return (DaSolicitudDx)q.uniqueResult();
    }

    public DaSolicitudEstudio getSolicitudesEstudioByMxEst(String idTomaMx, Integer idEstudio){
        String query = "from DaSolicitudEstudio where anulado = false and idTomaMx.idTomaMx = :idTomaMx " +
                "and tipoEstudio.idEstudio= :idEstudio ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        q.setParameter("idEstudio",idEstudio);
        return (DaSolicitudEstudio)q.uniqueResult();
    }

    /**
     * Obtiene los dx iniciales solicitados en la toma de mx en una fecha de toma especifica
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public List<DaSolicitudDx> getSoliDxByIdMxFechaToma(String id, Date fechaToma){
        String query = "select sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx " +
                "where sdx.anulado = false and sdx.inicial = true and mx.idTomaMx  = :id and mx.fechaHTomaMx = :fechaToma ORDER BY sdx.idTomaMx.idTomaMx";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        q.setParameter("fechaToma", fechaToma);
        return q.list();
    }

    /**REPORTE DE RESULTADOS DX PARA VIGILANCIA*/
    public List<DaSolicitudDx> getSolicitudesDxByIdToma(String idTomaMx, String codigoLab){
        String query = "select distinct sdx from DaSolicitudDx sdx inner join sdx.idTomaMx mx " +
                "where sdx.anulado = false and mx.idTomaMx = :idTomaMx " +
                "and (sdx.labProcesa.codigo = :codigoLab" +
                " or sdx.idSolicitudDx in (select oe.solicitudDx.idSolicitudDx " +
                "                   from OrdenExamen oe where oe.solicitudDx.idSolicitudDx = sdx.idSolicitudDx and oe.labProcesa.codigo = :codigoLab )) " +
                "ORDER BY sdx.fechaHSolicitud ";

        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        q.setParameter("codigoLab",codigoLab);
        return q.list();
    }

    public List<Solicitud> getSolicitudesDxByIdTomaV2(String idTomaMx, String codigoLab){
        String query = "select distinct sdx.codDx.idDiagnostico as idSolicitud, sdx.codDx.nombre as nombre from DaSolicitudDx sdx inner join sdx.idTomaMx mx " +
                "where sdx.anulado = false and mx.idTomaMx = :idTomaMx " +
                "and (sdx.labProcesa.codigo = :codigoLab" +
                " or sdx.idSolicitudDx in (select oe.solicitudDx.idSolicitudDx " +
                "                   from OrdenExamen oe where oe.solicitudDx.idSolicitudDx = sdx.idSolicitudDx and oe.labProcesa.codigo = :codigoLab )) ";

        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        q.setParameter("codigoLab",codigoLab);
        q.setResultTransformer(Transformers.aliasToBean(Solicitud.class));
        return q.list();
    }

    /**
     * Obtiene las mx tomadas para una notificación en fecha de toma especifica
     * @param id del estudio a buscar
     * @return Catalogo_Estudio
     */
    public List<DaTomaMx> getTomaMxByIdNotiAndFechaToma(String id, Date fechaToma){
        String query = "select mx from DaTomaMx mx " +
                "where mx.idNotificacion.idNotificacion  = :id and mx.fechaHTomaMx = :fechaToma";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("id", id);
        q.setParameter("fechaToma", fechaToma);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<DaSolicitudDx> getSoliDxAprobByIdToma(String idTomaMx){
        String query = "from DaSolicitudDx where idTomaMx.idTomaMx = :idTomaMx and anulado = false and  aprobada = true ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        return q.list();
    }

}
