package ni.gob.minsa.alerta.service;

import java.util.List;

import javax.annotation.Resource;


import ni.gob.minsa.alerta.domain.sive.SivePatologias;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("sivePatologiasService")
@Transactional
public class SivePatologiasService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;


	@SuppressWarnings("unchecked")
	public List<SivePatologias> getSivePatologias() {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM SivePatologias order by nombre");
		// Retrieve all
		return  query.list();
	}
}
