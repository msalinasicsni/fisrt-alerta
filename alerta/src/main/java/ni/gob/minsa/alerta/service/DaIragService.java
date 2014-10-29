package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaIrag;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics
 */
@Service("daIragService")
@Transactional
public class DaIragService {

    static final Logger logger = LoggerFactory.getLogger(DaIragService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;


    /**
     * Retorna las fichas activas
     *
     */
    public List<DaIrag> getAllFormActivos() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaIrag vi where vi.anulada = false ");
        return query.list();

    }


    /**
     * Retorna Ficha de Vigilancia Integrada
     * @param id
     */
    public DaIrag getFormById(String id) {

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM DaIrag vi where vi.idIrag = '" + id + "'");
        DaIrag fichaVI = (DaIrag) query.uniqueResult();
        return  fichaVI;

    }


    /**
     * Agrega Ficha Vigilancia Integrada
     *
     */
    public String addIrag(DaIrag dto) throws Exception {
        String idIrag = null;
        if (dto != null){
            Session session = sessionFactory.getCurrentSession();
            idIrag = (String)session.save(dto);

    }else{
            throw new Exception("DaIrag null");
        }
        return idIrag;
    }
    /**
     * Actualiza Ficha Vigilancia Integrada
     *
     */

    public void updateIrag(DaIrag vigIntegrada){
        Session session = sessionFactory.getCurrentSession();
        session.update(vigIntegrada);
    }

    @SuppressWarnings("unchecked")
    public List<DaIrag> getDaIragPersona(long idPerson){
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(DaIrag.class)
                .add(Restrictions.eq("persona.personaId", idPerson))
                .list();
    }




}
