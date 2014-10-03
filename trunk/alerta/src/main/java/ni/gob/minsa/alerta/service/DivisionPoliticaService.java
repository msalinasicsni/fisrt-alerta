package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para el objeto Division politica
 *
 * @author Miguel Salinas
 */

@Service("divisionPoliticaService")
@Transactional
public class DivisionPoliticaService {

    @Autowired(required = true)
    @Qualifier(value = "sessionFactory")
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

    /**
     * @return
     * @throws Exception
     */
    public List<Divisionpolitica> getAllDepartamentos() throws Exception {
        List<Divisionpolitica> aux;
        Session session=null;
        try{
            String query = "select a from Divisionpolitica as a where dependencia is null order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            aux = q.list();
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
        return
                aux;
    }

    public List<Divisionpolitica> getAllMunicipios() throws Exception {
        List<Divisionpolitica> aux;
        Session session=null;
        try{
            String query = "select a from Divisionpolitica as a where dependencia is not null order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            session.close();
            aux = q.list();
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
        return
                aux;
    }

    public List<Divisionpolitica> getMunicipiosFromDepartamento(String idDepartamento) throws Exception {
        List<Divisionpolitica> aux;
        Session session=null;
        try{
            String query = "select a from Divisionpolitica as a where dependencia is not null and dependencia=:idDepartamento order by nombre asc";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("idDepartamento", idDepartamento);
            aux = q.list();
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
        return aux;
    }

    public Divisionpolitica getDivisionPolitiacaByCodNacional(String codNac){
        Divisionpolitica dp = null;
        Session session=null;
        try{
            String query = "select a from Divisionpolitica as a where codigoNacional =:codigoNacional";

            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            q.setString("codigoNacional", codNac);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            if(session !=null && session.isOpen())
            {
                session.close();
                session=null;
            }
        }
        return   dp;
    }
}