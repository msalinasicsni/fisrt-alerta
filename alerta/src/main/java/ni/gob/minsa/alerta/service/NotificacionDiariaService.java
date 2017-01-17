package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.sive.SiveInformeDiario;
import ni.gob.minsa.alerta.domain.sive.UnidadesVwEntity;
import ni.gob.minsa.alerta.utilities.DateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by souyen-ics.
 */
@Service("notificacionDiariaService")
@Transactional
public class NotificacionDiariaService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public void save(SiveInformeDiario notiD){
        Session session = sessionFactory.getCurrentSession();
        session.save(notiD);
    }

    public void update(SiveInformeDiario notiD){
        Session session = sessionFactory.getCurrentSession();
        session.update(notiD);
    }

    public void delete(SiveInformeDiario notiD){
        Session session = sessionFactory.getCurrentSession();
        session.delete(notiD);
    }

    /**
     *Retorna una lista de eventos segun silais, municipio, unidad y fecha
     * @param silais
     * @param municipio
     * @param unidad
     * @param fecha
     *
     */
    @SuppressWarnings("unchecked")
    public List<SiveInformeDiario> getEventsByParams(String silais, String municipio, long unidad, Date fecha) throws Exception {
        String query = "select ev from SiveInformeDiario ev " +
                " where ev.silais = :silais " +
                " and ev.municipio = :municipio " +
                " and ev.unidad.codigo = :unidad "+
                " and ev.fechaNotificacion = :fecha "+
                "order by ev.fecharegistro" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("silais", silais);
        q.setString("municipio", municipio);
        q.setParameter("unidad",unidad);
        q.setParameter("fecha", fecha);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<SiveInformeDiario> getEventsByParams1(String silais, String municipio, long unidad, Date fecha, String pato) throws Exception {
        String query = "select ev from SiveInformeDiario ev " +
                "where ev.silais = :silais " +
                "and ev.municipio = :municipio " +
                "and ev.unidad.codigo = :unidad "+
                "and ev.fechaNotificacion = :fecha "+
                "and ev.patologia.codigo = :pato ";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("silais", silais);
        q.setString("municipio", municipio);
        q.setParameter("unidad",unidad);
        q.setParameter("fecha", fecha);
        q.setString("pato", pato);
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public SiveInformeDiario getNotiD(String silais, String municipio, long unidad, Date fecha, String pato) throws Exception {
        String query = "select ev from SiveInformeDiario ev " +
                "where ev.silais = :silais " +
                "and ev.municipio = :municipio " +
                "and ev.unidad.codigo = :unidad "+
                "and ev.fechaNotificacion = :fecha "+
                "and ev.patologia.codigo = :pato ";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("silais", silais);
        q.setString("municipio", municipio);
        q.setParameter("unidad",unidad);
        q.setDate("fecha", fecha);
        q.setString("pato", pato);
        return (SiveInformeDiario) q.uniqueResult();
    }

/*    public List<SiveInformeDiario> getEventsByParams(String silais, String municipio, long unidad, Date fecha, String pato) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(SiveInformeDiario.class, "noti");

        //silais

            crit.add( Restrictions.and(
                            Restrictions.eq("silais", silais)));

        //municipio

            crit.add( Restrictions.and(
                            Restrictions.eq("municipio", municipio))
            );

        //unidad de salud

         //   crit.createAlias("unidad","uni");
      *//*      crit.add( Restrictions.and(
                            Restrictions.eq("unidad", unidad))
            );*//*

        //fecha de notificacion

        crit.add( Restrictions.and(
                        Restrictions.like("fechaNotificacion", fecha))
        );

        // pato

        if (pato != null){
          //  crit.createAlias("notiD.patologia.codigo","pato");
            crit.add( Restrictions.and(
                            Restrictions.eq("patologia.codigo", pato))
            );
        }

        //ordenar por fecha de registro
       crit.addOrder(Order.asc("fecharegistro"));

        return crit.list();
    }*/


    /**
     *Retorna una lista de eventos segun silais, municipio, unidad y fecha
     * @param silais
     * @param municipio
     * @param unidad
     * @param fecha
     *
     */
    @SuppressWarnings("unchecked")
    public List<Object> getEventNotiD(String silais, String municipio, long unidad, Date fecha) throws Exception {
        String query = "select ev.fechaNotificacion, ent.nombre, div.nombre, ev.unidad.nombre, ev.bloqueado, ev.silais, ev.municipio, ev.unidad.codigo  " +
                "from SiveInformeDiario ev, EntidadesAdtvas ent, Divisionpolitica div " +
                "where ev.silais = :silais " +
                "and ent.codigo = cast(ev.silais as long)  " +
                "and div.divisionpoliticaId = cast(ev.municipio as long)  " +
                "and ev.municipio = :municipio " +
                "and ev.unidad.codigo = :unidad "+
                "and ev.fechaNotificacion = :fecha "+
                "group by ev.fechaNotificacion, ent.nombre, div.nombre, ev.unidad.nombre, ev.bloqueado, ev.silais, ev.municipio, ev.unidad.codigo" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("silais", silais);
        q.setString("municipio", municipio);
        q.setParameter("unidad",unidad);
        q.setParameter("fecha", fecha);
        return q.list();
    }

    /**
     * @param codUnidad Id para obtener un objeto en especifico del tipo <code>Unidades</code>
     * @return retorna un objeto filtrado del tipo UnidadesVwEntity
     * @throws Exception
     */
    public UnidadesVwEntity getUnidadSive(Integer codUnidad) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from UnidadesVwEntity as a where codigo=:idUnidad order by nombre asc";
        Query q = session.createQuery(query);
        q.setInteger("idUnidad", codUnidad);
        return  (UnidadesVwEntity)q.uniqueResult();
    }

    public List<UnidadesVwEntity> getPUnitsHospByMuniAndSilais(long codMunicipio, String[] codTiposUnidades, long codSilais) throws Exception {
        List<UnidadesVwEntity> res = new ArrayList<UnidadesVwEntity>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(UnidadesVwEntity.class);
            criteria.createAlias("municipio", "muni");
            criteria.add(Restrictions.eq("muni.divisionpoliticaId", codMunicipio));
            criteria.add(Restrictions.eq("entidadAdtva.codigo",codSilais));
            criteria.add(Restrictions.eq("pasivo",'0'));
            Long[] dataTipUnidades = new Long[codTiposUnidades.length];
            for(int i=0; i < codTiposUnidades.length; i++){
                dataTipUnidades[i] = Long.parseLong(codTiposUnidades[i]);
            }
            criteria.add(Restrictions.in("tipoUnidad", dataTipUnidades));
            criteria.addOrder(Order.asc("nombre"));
            res = criteria.list();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return res;
    }

    /**
     * M�todo que obtiene todas las unidades de salud a las que tiene autorizaci�n el usuario en el sistema seg�n el SILAIS y municipio
     * @param pUsuarioId id del usuario autenticado
     * @param pCodSilais C�digo del silais a filtrar
     * @param pCodMunicipio C�digo del municio a filtrar
     * @param pCodigoSis c�digo del sistema, ALERTA
     * @param tipoUnidades tipos de unidades a carga. Eje: Primarias , Primarias+Hospitales
     * @return List<Unidades>
     */
    public List<UnidadesVwEntity> obtenerUnidadesPorUsuarioEntidadMunicipio(Integer pUsuarioId, long pCodSilais, Long pCodMunicipio, String pCodigoSis, String tipoUnidades){
        List<UnidadesVwEntity> unidadesList = new ArrayList<UnidadesVwEntity>();
        try {
            //se obtienen todas las unidades del los silais asociados directamente
            String query = "select uni from UnidadesVwEntity uni, UsuarioEntidad usuni, Usuarios usu, Sistema sis " +
                    " where uni.entidadAdtva.entidadAdtvaId = usuni.entidadAdtva.entidadAdtvaId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                    " and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                    " and uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.divisionpoliticaId = :pCodMunicipio order by uni.nombre";
            Query qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
            qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
            qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
            qrUsuarioUnidad.setParameter("pasivo", '0');
            qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
            qrUsuarioUnidad.setParameter("pCodMunicipio",pCodMunicipio);
            unidadesList = qrUsuarioUnidad.list();
            //no hay unidades asociadas indirectamente al usuario, se obtienen las unidades asociadas directamente
            if (unidadesList.size()<=0){
                query = " select uni from UnidadesVwEntity uni, UsuarioUnidad usuni, Usuarios usu, Sistema sis " +
                        " where uni.unidadId = usuni.unidad.unidadId and usu.usuarioId = usuni.usuario.usuarioId and usuni.sistema.id = sis.id " +
                        " and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and uni.pasivo = :pasivo and uni.tipoUnidad in ("+tipoUnidades+")" +
                        " and uni.entidadAdtva.codigo = :pCodSilais and uni.municipio.divisionpoliticaId = :pCodMunicipio order by uni.nombre";
                qrUsuarioUnidad = sessionFactory.getCurrentSession().createQuery(query);
                qrUsuarioUnidad.setParameter("pUsuarioId",pUsuarioId);
                qrUsuarioUnidad.setParameter("pCodigoSis",pCodigoSis);
                qrUsuarioUnidad.setParameter("pasivo", '0');
                qrUsuarioUnidad.setParameter("pCodSilais", pCodSilais);
                qrUsuarioUnidad.setParameter("pCodMunicipio",pCodMunicipio);
                unidadesList = qrUsuarioUnidad.list();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return unidadesList;
    }
}
