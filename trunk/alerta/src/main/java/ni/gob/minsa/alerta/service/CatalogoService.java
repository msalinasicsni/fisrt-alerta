package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
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
 * Servicio para el objeto de Catalogos
 *
 * @author Miguel Salinas
 */

@Service("catalogosService")
@Transactional
public class CatalogoService {

    private Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public CatalogoService() {

    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        if(this.sessionFactory == null){
            this.sessionFactory = sessionFactory;
        }
    }

    public List<Catalogo> ElementosCatalogos(String discriminador) throws Exception {
        String query = "from Catalogo where pasivo = false and nodoPadre= :discriminador order by orden";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("discriminador", discriminador);
        return q.list();
    }
    
    public Catalogo getElementoByCodigo(String codigo) throws Exception {
        String query = "from Catalogo as a where pasivo = false and codigo= :codigo order by orden";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codigo", codigo);
        return  (Catalogo)q.uniqueResult();
    }

}