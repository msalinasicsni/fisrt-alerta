package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Unidades;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para el objeto de Unidades - Alerta
 *
 * @author MSalinas
 */
@Service("unidadesService")
@Transactional
public class UnidadesService {

    @Autowired
    @Qualifier(value = "sessionFactory")
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
        List<Unidades> result=null;
        Session session=null;
        try{
            String query = "select a from Unidades as a order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            result = (List<Unidades>)q.list();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return result;
    }

    public List<Unidades> getUnidadesFromEntidades(int idEntidad) throws Exception {
        List<Unidades> result;
        Session session=null;
        try{
            String query = "select a from Unidades as a where entidadAdtva=:idEntidad order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setInteger("idEntidad",idEntidad);
            result = (List<Unidades>)q.list();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return result;
    }

    /**
     * @param codUnidad Id para obtener un objeto en especifico del tipo <code>Unidades</code>
     * @return retorna un objeto filtrado del tipo <code>Unidades</code>
     * @throws Exception
     */
    public Unidades getUnidadByCodigo(Integer codUnidad) throws Exception {
        Unidades result;
        Session session=null;
        try{
            String query = "select a from Unidades as a where codigo=:idUnidad order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setInteger("idUnidad", codUnidad);
            result = (Unidades)q.uniqueResult();
        }
        catch (Exception ex) {
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
}