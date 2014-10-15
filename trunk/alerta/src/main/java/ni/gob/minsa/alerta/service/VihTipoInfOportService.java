package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.vih.VihTipoInfOport;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by USER on 13/10/2014.
 */
@Service("vihTipoInfOportService")
@Transactional
public class VihTipoInfOportService {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory(){
        return
                sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        if (this.sessionFactory == null) {
            this.sessionFactory = sessionFactory;
        }
    }

    public List<VihTipoInfOport> getAllInfeccionesOport() throws Exception {
        String query = "from VihTipoInfOport where fechaBaja is null order by nombre asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return  q.list();
    }
}
