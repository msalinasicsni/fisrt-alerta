package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaCondicionesPreviasIrag;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics
 */
@Service("daCondicionesIragService")
@Transactional
public class DaCondicionesIragService {

    static final Logger logger = LoggerFactory.getLogger(DaCondicionesPreviasIrag.class);

    @Resource(name = "sessionFactory")
    public SessionFactory sessionFactory;

    /*Busca registro de condicion existente
    * @param condicion
    */
    public DaCondicionesPreviasIrag searchConditionRecord(String condicion, String id){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaCondicionesPreviasIrag con where con.codCondicion ='"+condicion+"' and  con.idIrag = '"+id+"' and con.pasivo = false");
        DaCondicionesPreviasIrag cond = (DaCondicionesPreviasIrag) query.uniqueResult();
        return cond;
    }

    /*Retorna las Condiciones preexistentes
     * @param id
     * @param pasivo
     */

    public List<DaCondicionesPreviasIrag> getAllConditionsByIdIrag(String id){

        String query = "select cond FROM DaCondicionesPreviasIrag cond where cond.idIrag = :id and cond.pasivo = :pasivo";
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setParameter("pasivo", false);
        q.setString("id",id);
        return q.list();
    }


    /**Retorna condicion preexistente
     * @param id
     *
     */
    public DaCondicionesPreviasIrag getConditionById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaCondicionesPreviasIrag cond where cond.idCondicion = '" + id + "'");
        DaCondicionesPreviasIrag cond = (DaCondicionesPreviasIrag) query.uniqueResult();
        return cond;
    }


    /**
     * Agrega condicion previa
     */
    public void addCondition(DaCondicionesPreviasIrag cond) {
        Session session = sessionFactory.getCurrentSession();
        session.save(cond);
    }

    /**
     * Actualiza condicion previa
     */
    public void updateCondition(DaCondicionesPreviasIrag cond) {
        Session session = sessionFactory.getCurrentSession();
        session.update(cond);
    }


}