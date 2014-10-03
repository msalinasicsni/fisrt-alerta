package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Servicio para el obtjeto Comunidades
 * @author MSalinas
 */
@Service("comunidadesService")
@Transactional
public class ComunidadesService {

    @Autowired(required = true)
    @Qualifier(value = "sessionFactory")
    public SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     *
     *
     * @param idMunicipio
     * @return
     * @throws Exception
     */
    public List<Comunidades> getComunidades(String idMunicipio) throws Exception {
        List<Comunidades> result = null;
        Session session=null;
        try {
            String query = "select a from Comunidades as a, Sectores as s " +
                    "where a.sector = s.codigo and s.municipio = :municipio";
            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("municipio", idMunicipio);
            result = q.list();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }

        return  result;
    }

    public Comunidades getComunidad(String codigo) throws Exception {

        Comunidades aux = new Comunidades();
        Session session = null;
        try {
            String query = "select a from Comunidades as a, Sectores as s " +
                    "where a.sector = s.codigo and s.municipio = :municipio";
            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("municipio", codigo);
            aux = (Comunidades) q.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return  aux;
    }
}