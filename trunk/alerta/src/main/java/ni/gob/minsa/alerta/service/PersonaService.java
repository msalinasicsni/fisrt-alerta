package ni.gob.minsa.alerta.service;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import ni.gob.minsa.alerta.domain.persona.SisPersona;

import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personaService")
@Transactional
public class PersonaService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<SisPersona> getPersonas(String filtro){
		try {
			filtro = URLDecoder.decode(filtro, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Session session = sessionFactory.getCurrentSession();
		if(filtro.matches("[0-9]*")){
			return session.createCriteria(SisPersona.class)
					.add( Restrictions.or(
					        Restrictions.eq("telefonoResidencia", filtro),
					        Restrictions.eq("telefonoMovil", filtro))
					 )
				    .list();
        }else if(filtro.matches("[a-zA-Zí\\s]*")){
        	Soundex varSoundex = new Soundex();
        	Criteria crit = session.createCriteria(SisPersona.class);
            String[] partes = filtro.split(" ");
            String[] partesSnd = filtro.split(" ");
            for(int i=0;i<partes.length;i++){
            	try{
            		partesSnd[i] = varSoundex.encode(partes[i]);
            	}
            	catch (IllegalArgumentException e){
            		partesSnd[i] = "0000";
            		e.printStackTrace();
            	}
            }
            for(int i=0;i<partes.length;i++){
            	Junction conditionGroup = Restrictions.disjunction();
            	conditionGroup.add(Restrictions.ilike("primerNombre" , "%"+partes[i]+"%" ))
            					.add(Restrictions.ilike( "primerApellido" , "%"+partes[i]+"%"  ))
            					.add(Restrictions.ilike( "segundoNombre" , "%"+partes[i]+"%"  ))
            					.add(Restrictions.ilike( "segundoApellido" , "%"+partes[i]+"%"  ))
            					.add(Restrictions.ilike("sndNombre", "%"+partesSnd[i]+"%"));
            	crit.add(conditionGroup);
            }
            
        	return crit.list();
        }
        else{
        	return session.createCriteria(SisPersona.class)
				    .add( Restrictions.or(
					        Restrictions.eq("identificacionHse", filtro).ignoreCase(),
					        Restrictions.eq("identificacion", filtro).ignoreCase())
					 )
				    .list();
        }
	}
	
	public SisPersona getPersona(long idPerson){
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM SisPersona p where p.personaId = :idPerson");
		query.setParameter("idPerson", idPerson);
		SisPersona persona = (SisPersona) query.uniqueResult();
		return persona;
		
	}
}