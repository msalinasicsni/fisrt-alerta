package ni.gob.minsa.alerta.service;
import java.util.List;

import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("analisisService")
@Transactional
public class AnalisisService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataEdadSexo(String codPato, String codArea, Long codSilais, String codMunicipio, Long codUnidad,
			String semI, String semF, String anioI,String anioF){
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select inf.anio as anio, " +
					"sum(inf.g01m) as g01m,sum(inf.g01f) as g01f,sum(inf.g02m) as g02m,sum(inf.g02f) as g02f,sum(inf.g03m) as g03m,sum(inf.g03f) as g03f," +
					"sum(inf.g04m) as g04m,sum(inf.g04f) as g04f,sum(inf.g05m) as g05m,sum(inf.g05f) as g05f,sum(inf.g06m) as g06m,sum(inf.g06f) as g06f," +
					"sum(inf.g07m) as g07m,sum(inf.g07f) as g07f,sum(inf.g08m) as g08m,sum(inf.g08f) as g08f,sum(inf.g09m) as g09m,sum(inf.g09f) as g09f, " +
					"sum(inf.g10m) as g10m,sum(inf.g10f) as g10f,sum(inf.g11m) as g11m,sum(inf.g11f) as g11f,sum(inf.g12m) as g12m,sum(inf.g12f) as g12f," +
					"sum(inf.g13m) as g13m,sum(inf.g13f) as g13f,sum(inf.descm) as descm,sum(inf.descf) as descf, " +
					"sum(inf.totalm) as totalm,sum(inf.totalf) as totalf " +
					"From SiveInformeDiario inf where inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		else{
			query = session.createQuery("Select inf.anio as anio, " +
					"sum(inf.g01m) as g01m,sum(inf.g01f) as g01f,sum(inf.g02m) as g02m,sum(inf.g02f) as g02f,sum(inf.g03m) as g03m,sum(inf.g03f) as g03f," +
					"sum(inf.g04m) as g04m,sum(inf.g04f) as g04f,sum(inf.g05m) as g05m,sum(inf.g05f) as g05f,sum(inf.g06m) as g06m,sum(inf.g06f) as g06f," +
					"sum(inf.g07m) as g07m,sum(inf.g07f) as g07f,sum(inf.g08m) as g08m,sum(inf.g08f) as g08f,sum(inf.g09m) as g09m,sum(inf.g09f) as g09f, " +
					"sum(inf.g10m) as g10m,sum(inf.g10f) as g10f,sum(inf.g11m) as g11m,sum(inf.g11f) as g11f,sum(inf.g12m) as g12m,sum(inf.g12f) as g12f," +
					"sum(inf.g13m) as g13m,sum(inf.g13f) as g13f,sum(inf.descm) as descm,sum(inf.descf) as descf, " +
					"sum(inf.totalm) as totalm,sum(inf.totalf) as totalf " +
					"From SiveInformeDiario inf where inf.patologia.codigo = '4620' group by inf.anio order by inf.anio");
		}
		return query.list();
	}
}