package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Cie10;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souyen-ics on 05-09-14.
 */
@Service("cie10Service")
@Transactional
public class Cie10Service {

    static final Logger logger = LoggerFactory.getLogger(Cie10Service.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
      public List<Cie10> getCie10Filtered(String filtro) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM Cie10 where ((activo = :activo) and (lower(codigoCie10) like :codigo or lower(nombreCie10) like :nombre)) order by codigoCie10");
        query.setParameter("activo", true);
        query.setParameter("codigo", "%" +filtro.toLowerCase() + "%");
        query.setParameter("nombre", "%" +filtro.toLowerCase() + "%");
        // Retrieve all
        return  query.list();
    }


    /**
     * @param codigo
     */
    public Cie10 getCie10ByCodigo(String codigo) {

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM Cie10 ci where ci.codigoCie10 = '" + codigo + "'");
        return (Cie10) query.uniqueResult();

    }

    /**
     *
     * @param codigo si son códigos específicos enviarlos separados por coma, Ejemplo: J458,J459. Sino enviar la inicial del del Capítulo, ejemplo: J
     * @param todos true, si es capítulo completo, false, si son códigos específicos
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Cie10> getCie10Irag(String codigo, boolean todos) {
        List<Cie10> res = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Cie10.class);

            String[] cod = codigo.split(",");
            if (!todos) {
                criteria.add(Restrictions.in("codigoCie10", cod));
            }else{
                criteria.add(Restrictions.ilike("codigoCie10", codigo + "%"));
            }
            criteria.add(Restrictions.eq("activo", true));
            criteria.addOrder(Order.asc("codigoCie10"));

            res = criteria.list();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;

    }

    @SuppressWarnings("unchecked")
    public List<Cie10> getCie10(){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM Cie10 ci where ci.activo = true  order by ci.nombreCie10");
        //retrieve all
        return query.list();
    }

}
