package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.rotavirus.FichaRotavirus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("rotaVirusService")
@Transactional
public class RotaVirusService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public void saveOrUpdate(FichaRotavirus fichaRotavirus){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(fichaRotavirus);
    }

    public FichaRotavirus getFichaById(String id){
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM FichaRotavirus vi where vi.daNotificacion.idNotificacion = :id");
        query.setParameter("id",id);
        return (FichaRotavirus)query.uniqueResult();
    }

    public List<FichaRotavirus> getFichaByPersonaId(long id){
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM FichaRotavirus rt where rt.daNotificacion.persona.personaId = :id");
        query.setParameter("id",id);
        return query.list();
    }
}
