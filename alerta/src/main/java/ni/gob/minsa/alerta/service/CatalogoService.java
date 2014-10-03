package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by MSalinas
 */

@Service("catalogosService")
@Transactional
public class CatalogoService {

    private Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    @Autowired(required = true)
    @Qualifier("sessionFactory")
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
        Session session=null;
        List<Catalogo> result;
        try{
            String query = "select a from Catalogo as a where pasivo = false and nodoPadre = :discriminador order by orden";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("discriminador", discriminador);
            result = q.list();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return result;

    }
    
    public Catalogo getElementoByCodigo(String codigo) throws Exception {
        Catalogo res;
        Session session=null;
        try{
            String query = "select a from Catalogo as a where pasivo = false and codigo= :codigo order by orden";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("codigo", codigo);
            res = (Catalogo)q.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return res;
    }

}