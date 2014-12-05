package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaIrag;
import ni.gob.minsa.alerta.domain.muestra.DaEnvioOrden;
import ni.gob.minsa.alerta.domain.muestra.DaOrdenExamen;
import ni.gob.minsa.alerta.domain.muestra.FiltroOrdenExamen;
import ni.gob.minsa.alerta.domain.muestra.Laboratorio;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
@Service("envioOrdenExamenMxService")
@Transactional
public class EnvioOrdenExamenMxService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public String addEnvioOrden(DaEnvioOrden dto) throws Exception {
        String idEnvio;
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                idEnvio = (String)session.save(dto);
            }
            else
                throw new Exception("Objeto Envio Orden es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
        return idEnvio;
    }

    public List<DaOrdenExamen> getOrdenesExamenPendiente(FiltroOrdenExamen filtro){
        Session session = sessionFactory.getCurrentSession();
        Soundex varSoundex = new Soundex();
        Criteria crit = session.createCriteria(DaOrdenExamen.class, "orden");
        crit.createAlias("orden.codEstado","estado");
        crit.createAlias("orden.idTomaMx", "tomaMx");
        crit.createAlias("tomaMx.idNotificacion", "notifi");
        //siempre se tomam las muestras que no estan anuladas
        crit.add( Restrictions.and(
                        Restrictions.eq("tomaMx.anulada", false))
        );//y las ordenes en estado 'PENDIENTE'
        crit.add( Restrictions.and(
                Restrictions.eq("estado.codigo", "ESTORDEN|PEND").ignoreCase()));

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
                            Restrictions.between("tomaMx.fechaRegistro", filtro.getFechaInicioTomaMx(),filtro.getFechaFinTomaMx()))
            );
        }
        // se filtra por tipo de muestra
        if (filtro.getCodTipoMx()!=null){
            crit.add( Restrictions.and(
                            Restrictions.eq("tomaMx.codTipoMx.codigo", filtro.getCodTipoMx()).ignoreCase())
            );
        }

        return crit.list();
    }

    /**
     * Obtiene fecha de Inicio de síntomas, según id de notificación.. Si se agregan nuevas fichas, se debe agregar la consulta a dicha ficha
     * @param strIdNotificacion id de la notificación a filtrar
     * @return Date
     */
    public Date getFechaInicioSintomas(String strIdNotificacion){
        Date fecInicioSintomas = null;
        String query = "from DaIrag where idNotificacion.idNotificacion = :idNotificacion";
        String query2 = "from DaSindFebril where idNotificacion.idNotificacion = :idNotificacion";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("idNotificacion", strIdNotificacion);
        Query q2 = session.createQuery(query2);
        q2.setParameter("idNotificacion", strIdNotificacion);

        DaIrag iragNoti= (DaIrag)q.uniqueResult();
        DaSindFebril sinFebNoti= (DaSindFebril)q2.uniqueResult();
        if(iragNoti!=null)
            fecInicioSintomas = iragNoti.getFechaInicioSintomas();
        else if(sinFebNoti!=null)
            fecInicioSintomas = sinFebNoti.getFechaInicioSintomas();

        return fecInicioSintomas;
    }

    public String estaEmbarazada(String strIdNotificacion){
        String embarazo = "No";
        Session session = sessionFactory.getCurrentSession();
        //IRAG
        /*String query = "select irag from DaIrag as irag, DaCondicionesPreviasIrag cIrag where irag.idNotificacion.idNotificacion = cIrag.idNotificacion.idNotificacion.idNotificacion" +
                " and irag.idNotificacion.idNotificacion = :idNotificacion" +
                " and cIrag.codCondicion.codigo = :codCondicion";*/
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

    public List<Laboratorio> getLaboratorios(){
        String query = "from Laboratorio ORDER BY nombre";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        return q.list();
    }
}
