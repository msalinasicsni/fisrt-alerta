package ni.gob.minsa.alerta.service;
import java.util.List;

import javax.annotation.Resource;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sindFebrilService")
@Transactional
public class SindFebrilService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<DaSindFebril> getDaSindFebrilesPersona(long idPerson){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("From DaSindFebril sf where sf.idNotificacion.persona.personaId =:idPerson");
		query.setParameter("idPerson", idPerson);
		return query.list();
	}
	
	public DaSindFebril getDaSindFebril(String idNotificacion){
		Session session = sessionFactory.getCurrentSession();
		return (DaSindFebril) session.createCriteria(DaSindFebril.class)
					.add(Restrictions.eq("idNotificacion.idNotificacion", idNotificacion)).uniqueResult();
				   
	}
}