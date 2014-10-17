package ni.gob.minsa.alerta.service;
import java.util.List;

import javax.annotation.Resource;
import ni.gob.minsa.alerta.domain.vigilanciaSindFebril.DaSindFebril;

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
		return session.createCriteria(DaSindFebril.class)
					.add(Restrictions.eq("persona.personaId", idPerson))
				    .list();
	}
	
	public DaSindFebril getDaSindFebril(String idFicha){
		Session session = sessionFactory.getCurrentSession();
		return (DaSindFebril) session.createCriteria(DaSindFebril.class)
					.add(Restrictions.eq("idFichaEpidem", idFicha)).uniqueResult();
				   
	}
}