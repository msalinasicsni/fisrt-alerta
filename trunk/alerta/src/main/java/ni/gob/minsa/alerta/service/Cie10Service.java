package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Cie10;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        Cie10 enfermedad = (Cie10) query.uniqueResult();
        return  enfermedad;

    }

}
