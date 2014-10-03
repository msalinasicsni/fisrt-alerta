package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Servicio para el objeto Division politica
 *
 * @author Miguel Salinas
 */

@Service("divisionPoliticaService")
@Transactional
public class DivisionPoliticaService {

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

    /**
     * @return
     * @throws Exception
     */
    public List<Divisionpolitica> getAllDepartamentos() throws Exception {
        String query = "from Divisionpolitica where dependencia is null order by nombre asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return  q.list();
    }

    public List<Divisionpolitica> getAllMunicipios() throws Exception {
        String query = "from Divisionpolitica where dependencia is not null order by nombre asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        session.close();
        return q.list();
    }

    public List<Divisionpolitica> getMunicipiosFromDepartamento(String idDepartamento) throws Exception {
        String query = "from Divisionpolitica where dependencia is not null and dependencia=:idDepartamento order by nombre asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("idDepartamento", idDepartamento);
        return q.list();
    }

    public Divisionpolitica getDivisionPolitiacaByCodNacional(String codNac){
        String query = "from Divisionpolitica where codigoNacional =:codigoNacional";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codigoNacional", codNac);
        return (Divisionpolitica)q.uniqueResult();
    }
}