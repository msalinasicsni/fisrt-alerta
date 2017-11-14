package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.irag.DaIrag;
import ni.gob.minsa.alerta.domain.muestra.*;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
@Service("envioMxService")
@Transactional
public class EnvioMxService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public void addEnvioOrden(DaEnvioMx dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.save(dto);
            }
            else
                throw new Exception("Objeto Envio Orden es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public List<DaTomaMx> getMxPendientes(FiltroMx filtro){
        Session session = sessionFactory.getCurrentSession();
        Soundex varSoundex = new Soundex();
        Criteria crit = session.createCriteria(DaTomaMx.class, "tomaMx");
        crit.createAlias("tomaMx.estadoMx","estado");
        crit.createAlias("tomaMx.idNotificacion", "notifi");

        //siempre se tomam las muestras que no estan anuladas
        crit.add( Restrictions.and(
                        Restrictions.eq("tomaMx.anulada", false))
        );//y las muestras en estado 'PENDIENTE'
        crit.add( Restrictions.and(
                Restrictions.eq("estado.codigo", "ESTDMX|PEND").ignoreCase()));
        //no mostrar las muestras de notificaciones 'CASOS ESPECIALES'
        crit.add( Restrictions.and(
                Restrictions.ne("notifi.codTipoNotificacion.codigo", "TPNOTI|CAESP").ignoreCase()));

        // se filtra por nombre y apellido persona
        if (filtro.getNombreApellido()!=null) {
            crit.createAlias("notifi.persona", "person");
            String[] partes = filtro.getNombreApellido().split(" ");
            String[] partesSnd = filtro.getNombreApellido().split(" ");
            for (int i = 0; i < partes.length; i++) {
                try {
                    partesSnd[i] = varSoundex.encode(partes[i]);
                } catch (IllegalArgumentException e) {
                    partesSnd[i] = "0000";
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < partes.length; i++) {
                Junction conditionGroup = Restrictions.disjunction();
                conditionGroup.add(Restrictions.ilike("person.primerNombre", "%" + partes[i] + "%"))
                        .add(Restrictions.ilike("person.primerApellido", "%" + partes[i] + "%"))
                        .add(Restrictions.ilike("person.segundoNombre", "%" + partes[i] + "%"))
                        .add(Restrictions.ilike("person.segundoApellido", "%" + partes[i] + "%"))
                        .add(Restrictions.ilike("person.sndNombre", "%" + partesSnd[i] + "%"));
                crit.add(conditionGroup);
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
        // se filtra por tipo de muestra
        if (filtro.getCodTipoMx()!=null){
            crit.add( Restrictions.and(
                            Restrictions.eq("tomaMx.codTipoMx.idTipoMx", Integer.valueOf(filtro.getCodTipoMx())))
            );
        }

        //se filtra por tipo de solicitud
       if(filtro.getCodTipoSolicitud()!=null){
            if(filtro.getCodTipoSolicitud().equals("Estudio")){
                crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudEstudio.class)
                        .add(Restrictions.eq("anulado",false))
                        .createAlias("idTomaMx", "idTomaMx")
                        .setProjection(Property.forName("idTomaMx.idTomaMx"))));
            }else{
                crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudDx.class)
                        .add(Restrictions.eq("anulado",false))
                        .createAlias("idTomaMx", "idTomaMx")
                        .setProjection(Property.forName("idTomaMx.idTomaMx"))));
            }

        }

        //nombre solicitud
        if(filtro.getNombreSolicitud()!=null){
            if(filtro.getCodTipoSolicitud()!= null){
                if(filtro.getCodTipoSolicitud().equals("Estudio")){
                    crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudEstudio.class)
                            .add(Restrictions.eq("anulado",false))
                            .createAlias("tipoEstudio", "estudio")
                            .add(Restrictions.ilike("estudio.nombre","%" + filtro.getNombreSolicitud() + "%" ))
                            .setProjection(Property.forName("idTomaMx.idTomaMx"))));
                }else{
                    crit.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudDx.class)
                            .add(Restrictions.eq("anulado",false))
                            .createAlias("codDx", "dx")
                            .add(Restrictions.ilike("dx.nombre","%" + filtro.getNombreSolicitud() + "%" ))
                            .setProjection(Property.forName("idTomaMx.idTomaMx"))));
                }
            }else{

                Junction conditGroup = Restrictions.disjunction();
                conditGroup.add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudEstudio.class)
                        .add(Restrictions.eq("anulado",false))
                        .createAlias("tipoEstudio", "estudio")
                        .add(Restrictions.ilike("estudio.nombre","%" + filtro.getNombreSolicitud() + "%" ))
                        .setProjection(Property.forName("idTomaMx.idTomaMx"))))
                        .add(Subqueries.propertyIn("idTomaMx", DetachedCriteria.forClass(DaSolicitudDx.class)
                                .add(Restrictions.eq("anulado",false))
                        .createAlias("codDx", "dx")
                        .add(Restrictions.ilike("dx.nombre","%" + filtro.getNombreSolicitud() + "%" ))
                        .setProjection(Property.forName("idTomaMx.idTomaMx"))));

                crit.add(conditGroup);

            }

        }

        return crit.list();
    }

    public String estaEmbarazada(String strIdNotificacion){
        String embarazo = "No";
        Session session = sessionFactory.getCurrentSession();
        //IRAG
        String query = "from DaIrag where idNotificacion.idNotificacion = :idNotificacion and condiciones like :codCondicion";
        Query q = session.createQuery(query);
        q.setParameter("idNotificacion", strIdNotificacion);
        q.setParameter("codCondicion","%"+"CONDPRE|EMB"+"%");//código para condición embarazo

        //SINDROMES FEBRILES
        String query2 = "from DaSindFebril where idNotificacion.idNotificacion = :idNotificacion" +
                " and embarazo.codigo = :codigoEmb";
        Query q2 = session.createQuery(query2);
        q2.setParameter("idNotificacion", strIdNotificacion);
        q2.setParameter("codigoEmb","RESP|S"); //respuesta afirmativa

        DaIrag iragNoti= (DaIrag)q.uniqueResult();
        DaSindFebril sinFebNoti= (DaSindFebril)q2.uniqueResult();
        if(iragNoti!=null)
            embarazo="Si";
        else if(sinFebNoti!=null)
            embarazo="Si";

        return embarazo;
    }

    public List<Laboratorio> getLaboratorios(Integer pUsuarioId, String pCodigoSis){
        List<Laboratorio> laboratorioList = new ArrayList<Laboratorio>();

      String query = "select distinct lab from Laboratorio as lab, EntidadAdtvaLaboratorio as el, EntidadesAdtvas as ent, UsuarioEntidad as usuent, Usuarios as usu, Sistema as sis " +
                "where lab.codigo = el.laboratorio.codigo and el.entidadAdtva.codigo = ent.codigo " +
                "and ent.id = usuent.entidadAdtva.entidadAdtvaId and usuent.usuario.usuarioId=usu.usuarioId and usuent.sistema.id = sis.id " +
                "and el.pasivo = false " +
                "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo order by lab.nombre";

        Query qr = sessionFactory.getCurrentSession().createQuery(query);
        qr.setParameter("pUsuarioId", pUsuarioId);
        qr.setParameter("pCodigoSis", pCodigoSis);
        qr.setParameter("pasivo", '0');

        laboratorioList = qr.list();
        if (laboratorioList.size()<=0){
            query = "select distinct lab from Laboratorio as lab, EntidadAdtvaLaboratorio as el, EntidadesAdtvas as ent, UsuarioUnidad as usuni, Usuarios as usu, Sistema as sis " +
                    "where lab.codigo = el.laboratorio.codigo and el.entidadAdtva.codigo = ent.codigo " +
                    "and ent.id = usuni.unidad.entidadAdtva.entidadAdtvaId and usuni.usuario.usuarioId=usu.usuarioId and usuni.sistema.id = sis.id " +
                    "and el.pasivo = false " +
                    "and sis.codigo = :pCodigoSis and usu.usuarioId = :pUsuarioId and ent.pasivo = :pasivo and usuni.unidad.pasivo = :pasivo order by lab.nombre";
            qr = sessionFactory.getCurrentSession().createQuery(query);
            qr.setParameter("pUsuarioId", pUsuarioId);
            qr.setParameter("pCodigoSis", pCodigoSis);
            qr.setParameter("pasivo", '0');

            laboratorioList = qr.list();
        }

        return laboratorioList;
    }

    public List<Laboratorio> getAllLaboratorios(){
        String query = "select lab from Laboratorio lab where pasivo = false " +
                "ORDER BY nombre";

        Query q = sessionFactory.getCurrentSession().createQuery(query);
        return q.list();
    }

    public Laboratorio getLaboratorio(String codigo){
        String query = "from Laboratorio where codigo =:codigo";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("codigo",codigo);
        return (Laboratorio)q.uniqueResult();
    }

    public List<DaSolicitudDx> getSolicitudesDxByIdTomaMx(String idTomaMx){
        String query = "from DaSolicitudDx where anulado = false and idTomaMx.idTomaMx = :idTomaMx ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        return q.list();
    }

    public List<DaSolicitudEstudio> getSolicitudesEstudioByIdTomaMx(String idTomaMx){
        String query = "from DaSolicitudEstudio where anulado = false and idTomaMx.idTomaMx = :idTomaMx ORDER BY fechaHSolicitud";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("idTomaMx",idTomaMx);
        return q.list();
    }
}
