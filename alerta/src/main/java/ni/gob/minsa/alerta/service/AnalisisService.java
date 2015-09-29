package ni.gob.minsa.alerta.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.sive.SivePatologiasTipo;

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
	//private static final String varOrden = "concat(inf.anio, '-', substring(concat('00', inf.semana),length(concat('00', inf.semana))-1,length(concat('00', inf.semana))))";
	//private static final String sqlData = "Select " + varOrden + " as periodo, sum(inf.totalm+inf.totalf) as total";
	
	//private static final String varOrden = "concat(inf.anio, '-', substring(concat('00', inf.semana),length(concat('00', inf.semana))-1,length(concat('00', inf.semana))))";
	//private static final String sqlData = "Select " + varOrden + " as periodo, sum(inf.totalf) as totalf";
	private static final String sqlData = "Select inf.silais, sum(inf.totalm + inf.totalf) as total";
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataSeries(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad){
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.patologia.codigo =:codPato " +
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			query.setParameter("codPato", codPato);
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and inf.patologia.codigo =:codPato " +
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			query.setParameter("codPato", codPato);
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.municipio.dependencia.divisionpoliticaId =:codDepartamento and inf.patologia.codigo =:codPato " +
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			query.setParameter("codPato", codPato);
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.municipio.divisionpoliticaId =:codMunicipio and inf.patologia.codigo =:codPato " +
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			query.setParameter("codPato", codPato);
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and inf.patologia.codigo =:codPato " +
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			query.setParameter("codPato", codPato);
			query.setParameter("codUnidad", codUnidad);
		}
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataMapas(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String semF, String anioI){
		// Retrieve session from Hibernate
		List<Object[]> resultado = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
		query.setParameter("codPato", codPato);
		SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
					"group by inf.silais order by inf.silais");
			query.setParameter("codPato", codPato);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
		}
		resultado.addAll(query.list());
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select 'Pob' as poblacion, pob.divpol.dependenciaSilais.codigo as silais, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.grupo =:tipoPob and pob.anio =:anio " +
					"group by pob.divpol.dependenciaSilais.codigo order by pob.divpol.dependenciaSilais.codigo");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anio", Integer.parseInt(anioI));
		}
		resultado.addAll(query.list());
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataPiramides(String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,String anioI){
		// Retrieve session from Hibernate
		List<Object[]> resultado = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select pob.grupo, sum(pob.masculino) as masculino, sum(pob.femenino) as femenino, sum(pob.total) as total From SivePoblacionDivPol pob " +
					"where pob.divpol.dependencia is null and pob.anio=:anioI " +
					"group by pob.grupo order by pob.grupo");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select pob.grupo, sum(pob.masculino) as masculino, sum(pob.femenino) as femenino, sum(pob.total) as total From SivePoblacionDivPol pob " +
					"where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.anio=:anioI " +
					"group by pob.grupo order by pob.grupo");
			query.setParameter("codSilais", codSilais);
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select pob.grupo, sum(pob.masculino) as masculino, sum(pob.femenino) as femenino, sum(pob.total) as total From SivePoblacionDivPol pob " +
					"where pob.divpol.divisionpoliticaId =:codDepartamento and pob.anio=:anioI " +
					"group by pob.grupo order by pob.grupo");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select pob.grupo, sum(pob.masculino) as masculino, sum(pob.femenino) as femenino, sum(pob.total) as total From SivePoblacionDivPol pob " +
					"where pob.divpol.divisionpoliticaId =:codMunicipio and pob.anio=:anioI " +
					"group by pob.grupo order by pob.grupo");
			query.setParameter("codMunicipio", codMunicipio);
		}
		query.setParameter("anioI", Integer.parseInt(anioI));
		resultado.addAll(query.list());
		return resultado;
	}
}