package ni.gob.minsa.alerta.service;
import java.util.List;

import javax.annotation.Resource;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personaService")
@Transactional
public class PersonaService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<SisPersona> getPersonas(String filtro) {
		String strQuery = "";
		if(filtro.matches("[0-9]*")){
			strQuery = "FROM SisPersona persona where (persona.telefonoResidencia = :parametro or persona.telefonoMovil = :parametro)";
        }else if(filtro.matches("[a-zA-Z·ÈÌÛ˙—Ò¡…Õ”⁄‹¸\\s]*")){
        	strQuery = "FROM SisPersona persona where (upper(persona.primerNombre) = :parametro or upper(persona.primerApellido) = :parametro" +
        			" or upper(persona.segundoNombre) = :parametro or upper(persona.segundoApellido) = :parametro)";
        }
        else{
        	strQuery = "FROM SisPersona persona where persona.identificacion = :parametro";
        }
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		// Create a Hibernate query (HQL)
		Query query = session.createQuery(strQuery);
		query.setParameter("parametro", filtro.toUpperCase());
		// Retrieve all
		return  query.list();
	}
}