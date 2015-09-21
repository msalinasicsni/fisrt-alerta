package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.DaTomaMx;
import ni.gob.minsa.alerta.domain.muestra.FiltroMx;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by souyen-ics on 11-18-14.
 */

@Service("daNotificacionService")
@Transactional
public class DaNotificacionService {

    static final Logger logger = LoggerFactory.getLogger(DaNotificacion.class);

    @Resource(name ="sessionFactory")
    public SessionFactory sessionFactory;


    /**
     * Agrega Notificacion
     */
    public void addNotification(DaNotificacion noti) {
        Session session = sessionFactory.getCurrentSession();
        session.save(noti);
    }

    /**
     * Retorna notificacion
     * @param idNotificacion
     */
    public DaNotificacion getNotifById(String idNotificacion) {

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM DaNotificacion noti where noti.idNotificacion = '" + idNotificacion + "'");
        return (DaNotificacion) query.uniqueResult();

    }

    @SuppressWarnings("unchecked")
    public List<DaNotificacion> getDaNotPersona(long idPerson, String tipoFicha){
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(DaNotificacion.class, "noti")
                .createAlias("noti.persona", "persona")
                .createAlias("noti.codTipoNotificacion", "tipo")
                .add(Restrictions.eq("persona.personaId", idPerson))
                .add(Restrictions.eq("tipo.codigo", tipoFicha))
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<DaNotificacion> getNoticesByPerson(String filtro){
        try {
            filtro = URLDecoder.decode(filtro, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Session session = sessionFactory.getCurrentSession();
        if(filtro.matches("[0-9]*")){
            return session.createCriteria(DaNotificacion.class, "noti")
                    .add(Restrictions.and(Restrictions.eq("noti.pasivo",false)))
                    .createAlias("noti.persona", "persona")
                    .add(Restrictions.or(
                                    Restrictions.eq("persona.telefonoResidencia", filtro),
                                    Restrictions.eq("persona.telefonoMovil", filtro))
                    )
                    .list();
        }else if(filtro.matches("[a-zA-Zí\\s]*")){
            Soundex varSoundex = new Soundex();
            Criteria crit = session.createCriteria(DaNotificacion.class, "noti");
            crit.createAlias("noti.persona", "persona");
            String[] partes = filtro.split(" ");
            String[] partesSnd = filtro.split(" ");
            for(int i=0;i<partes.length;i++){
                try{
                    partesSnd[i] = varSoundex.encode(partes[i]);
                }
                catch (IllegalArgumentException e){
                    partesSnd[i] = "0000";
                    e.printStackTrace();
                }
            }
            for(int i=0;i<partes.length;i++){
                Junction conditionGroup = Restrictions.disjunction();
                conditionGroup.add(Restrictions.ilike("persona.primerNombre" , "%"+partes[i]+"%" ))
                        .add(Restrictions.ilike( "persona.primerApellido" , "%"+partes[i]+"%"  ))
                        .add(Restrictions.ilike( "persona.segundoNombre" , "%"+partes[i]+"%"  ))
                        .add(Restrictions.ilike( "persona.segundoApellido" , "%"+partes[i]+"%"  ))
                        .add(Restrictions.ilike("persona.sndNombre", "%"+partesSnd[i]+"%"));
                crit.add(conditionGroup);
            }
            crit.add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|CAESP")));
            crit.add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|OMX")));
            crit.add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|PCNT")));
            crit.add(Restrictions.and(Restrictions.eq("noti.pasivo",false)));
            return crit.list();
        }
        else{
            return session.createCriteria(DaNotificacion.class, "noti")
                    .add(Restrictions.and(Restrictions.eq("noti.pasivo",false)))
                    .createAlias("noti.persona", "persona")
                    .add( Restrictions.or(
                                    Restrictions.eq("persona.identificacionHse", filtro).ignoreCase(),
                                    Restrictions.eq("persona.identificacion", filtro).ignoreCase())
                    )
                    .add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|CAESP")))
                    .add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|OMX")))
                    .add( Restrictions.and(Restrictions.ne("codTipoNotificacion.codigo", "TPNOTI|PCNT")))
                    .list();
        }
    }


    public void updateNotificacion(DaNotificacion dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
            else
                throw new Exception("Objeto DaNotificacion es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DaNotificacion> getNoticesByPersonAndType(long idPersona, String tipoNotificacion){
        Session session = sessionFactory.getCurrentSession();
        //todas las notificaciones tipo CASO ESPECIAL registradas para la persona seleccionada
        return session.createCriteria(DaNotificacion.class, "noti")
                .createAlias("noti.persona", "persona")
                .add(Restrictions.and(
                                Restrictions.eq("persona.personaId", idPersona),
                                Restrictions.eq("codTipoNotificacion.codigo", tipoNotificacion))
                )
                .list();

    }

    public List<DaNotificacion> getNoticesByFilro(FiltroMx filtro){
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(DaNotificacion.class, "notifi");

        //siempre se tomam las notificaciones activas
        crit.add( Restrictions.and(
                        Restrictions.eq("notifi.pasivo", false))
        );
        //no mostrar las muestras de notificaciones 'OTRAS MUESTRAS' pues son de laboratorio, ni CASOS ESPECIALES
        crit.add( Restrictions.and(
                Restrictions.ne("notifi.codTipoNotificacion.codigo", "TPNOTI|OMX").ignoreCase()));
        crit.add( Restrictions.and(
                Restrictions.ne("notifi.codTipoNotificacion.codigo", "TPNOTI|CAESP").ignoreCase()));
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
        //Se filtra por rango de fecha de registro notificación
        if (filtro.getFechaInicioNotifi()!=null && filtro.getFechaFinNotifi()!=null){
            crit.add( Restrictions.and(
                            Restrictions.between("notifi.fechaRegistro", filtro.getFechaInicioNotifi(),filtro.getFechaFinNotifi()))
            );
        }

        if (filtro.getTipoNotificacion()!=null){
            crit.createAlias("notifi.codTipoNotificacion","tipoNoti");
            crit.add( Restrictions.and(
                            Restrictions.eq("tipoNoti.codigo", filtro.getTipoNotificacion()))
            );
        }
        return crit.list();
    }

}
