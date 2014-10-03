package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para el objeto Entidades Administrativas
 *
 * @author Miguel Salinas
 */
@Service("entidadAdmonService")
@Transactional
public class EntidadAdmonService {

    @Autowired()
    @Qualifier("sessionFactory")
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        if(this.sessionFactory == null){
            this.sessionFactory = sessionFactory;
        }
    }

    public List<EntidadesAdtvas> getAllEntidadesAdtvas() throws Exception {
        List<EntidadesAdtvas> result = null;
        Session session=null;
        try{
            String query = "select a from EntidadesAdtvas as a order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            result = (List<EntidadesAdtvas>)q.list();
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
     * @param idMunicipio Id del Municipio por el que queres obtener las Entidades Administrativas
     * @return Retorna una lista de Entidades Administrativas obtenidas a partir del parametro ID Municipio
     * @throws Exception
     */
    public EntidadesAdtvas getSilaisFromMunicipio(String idMunicipio) throws Exception {
        EntidadesAdtvas aux;
        Session session=null;
        try{
            String query = "select a from EntidadesAdtvas as a where municipio=:idMunicipio order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("idMunicipio",idMunicipio);
            aux = (EntidadesAdtvas)q.uniqueResult();
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
        return aux;
    }

    /**
     * @param codigo Id para obtener un objeto en especifico del tipo <code>EntidadesAdtvas</code>
     * @return retorna un objeto filtrado del tipo <code>EntidadesAdtvas</code>
     * @throws Exception
     */
    public EntidadesAdtvas getSilaisById(Integer codigo) throws Exception {
        EntidadesAdtvas result;
        Session session=null;
        try{
            String query = "select a from EntidadesAdtvas as a where codigo= :codigo order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setInteger("codigo",codigo);
            result = (EntidadesAdtvas)q.uniqueResult();
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
}