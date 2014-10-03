package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Unidades;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Servicio para el objeto de Unidades
 *
 * @author Miguel Salinas
 */
@Service("unidadesService")
@Transactional
public class UnidadesService {

    @Resource(name="sessionFactory")
    public SessionFactory sessionFactory;

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        if(this.sessionFactory == null){
            this.sessionFactory = sessionFactory;
        }
    }

    /**
     * Retorna todos los datos del Objeto <code> Unidades </code>
     *
     * @return una lista de  Unidades
     * @throws Exception
     */
    public List<Unidades> getAllUnidades() throws Exception {
        String query = "from Unidades order by nombre asc";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return q.list();
    }

    public List<Unidades> getUnidadesFromEntidades(int idEntidad) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from Unidades where entidadAdtva=:idEntidad order by nombre asc";
        Query q = session.createQuery(query);
        q.setInteger("idEntidad",idEntidad);
        return q.list();
    }

    /**
     * @param codUnidad Id para obtener un objeto en especifico del tipo <code>Unidades</code>
     * @return retorna un objeto filtrado del tipo <code>Unidades</code>
     * @throws Exception
     */
    public Unidades getUnidadByCodigo(Integer codUnidad) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from Unidades as a where codigo=:idUnidad order by nombre asc";
        Query q = session.createQuery(query);
        q.setInteger("idUnidad", codUnidad);
        return  (Unidades)q.uniqueResult();
    }
}