package ni.gob.minsa.alerta.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import ni.gob.minsa.alerta.domain.sive.SivePatologiasTipo;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("analisisDescService")
@Transactional
public class AnalisisDescService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	private static final String sqlData = "Select inf.anio as anio, " +
			"sum(inf.g01m) as g01m,sum(inf.g01f) as g01f,sum(inf.g02m) as g02m,sum(inf.g02f) as g02f,sum(inf.g03m) as g03m,sum(inf.g03f) as g03f," +
			"sum(inf.g04m) as g04m,sum(inf.g04f) as g04f,sum(inf.g05m) as g05m,sum(inf.g05f) as g05f,sum(inf.g06m) as g06m,sum(inf.g06f) as g06f," +
			"sum(inf.g07m) as g07m,sum(inf.g07f) as g07f,sum(inf.g08m) as g08m,sum(inf.g08f) as g08f,sum(inf.g09m) as g09m,sum(inf.g09f) as g09f, " +
			"sum(inf.g10m) as g10m,sum(inf.g10f) as g10f,sum(inf.g11m) as g11m,sum(inf.g11f) as g11f,sum(inf.g12m) as g12m,sum(inf.g12f) as g12f," +
			"sum(inf.g13m) as g13m,sum(inf.g13f) as g13f,sum(inf.descm) as descm,sum(inf.descf) as descf, " +
			"sum(inf.totalm) as totalm,sum(inf.totalf) as totalf";
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataEdadSexo(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String semF, String anioI,String anioF){
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) " +
					"and (inf.anio=:anioI or inf.anio=:anioF) group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codSilais", codSilais.toString());
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica  municipio " +
					"where cast(inf.municipio as long ) = municipio.divisionpoliticaId and  municipio.dependencia.divisionpoliticaId =:codDepartamento and inf.patologia.codigo =:codPato " +
					"and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codDepartamento", codDepartamento);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica  municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and inf.patologia.codigo =:codPato " +
					"and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codMunicipio", codMunicipio);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) " +
					"and (inf.anio=:anioI or inf.anio=:anioF) group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codUnidad", codUnidad);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
		}
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataAnaSexo(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
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
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query = session.createQuery("Select 'Pop' as label, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and pob.anio =:anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anio", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
					"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", codPato);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codSilais", codSilais.toString());
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query = session.createQuery("Select 'Pop' as label, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and pob.anio =:anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("codSilais", codSilais);
			query.setParameter("anio", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
					"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", codPato);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica  municipio " +
					"where cast(inf.municipio as long)= municipio.divisionpoliticaId and  municipio.dependencia.divisionpoliticaId =:codDepartamento and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codDepartamento", codDepartamento);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query = session.createQuery("Select 'Pop' as label, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento and pob.grupo =:tipoPob and pob.anio =:anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("codDepartamento", codDepartamento);
			query.setParameter("anio", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
					"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", codPato);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica  municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codMunicipio", codMunicipio);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query = session.createQuery("Select 'Pop' as label, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and pob.anio =:anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("codMunicipio", codMunicipio);
			query.setParameter("anio", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
					"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", codPato);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and inf.patologia.codigo =:codPato and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
					"group by inf.anio order by inf.anio");
			query.setParameter("codPato", codPato);
			query.setParameter("codUnidad", codUnidad);
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query = session.createQuery("Select 'Pop' as label, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and pob.anio =:anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("codUnidad", codUnidad);
			query.setParameter("anio", Integer.parseInt(anioI));
			resultado.addAll(query.list());
			query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
					"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", codPato);
			resultado.addAll(query.list());
		}
		return resultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataAnaPato(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String semF, String anioI,String anioF){
		// Retrieve session from Hibernate
		List<Object[]> resultado = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		String patoQuery = null;
		String[] patos = codPato.split(",");
		boolean noData1 = true;
		boolean noData2 = true;
		query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
		query.setParameter("codPato", patos[0]);
		SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
		for (int i =0; i<patos.length; i++){
			if (patoQuery == null){
				patoQuery = "inf.patologia.codigo = '"+patos[i]+"'";
			}
			else{
				patoQuery = patoQuery + " or inf.patologia.codigo = '"+patos[i]+"'";
			}
		}
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + ", inf.patologia.codigo, inf.patologia.nombre as patologia  From SiveInformeDiario inf " +
					"where ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + ", inf.patologia.codigo, inf.patologia.nombre as patologia  From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codSilais", codSilais.toString());
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + ", inf.patologia.codigo, inf.patologia.nombre as patologia  From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codDepartamento", codDepartamento);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + ", inf.patologia.codigo, inf.patologia.nombre as patologia  From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codMunicipio", codMunicipio);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + ", inf.patologia.codigo, inf.patologia.nombre as patologia  From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI or inf.anio=:anioF) " +
					"group by inf.anio, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("semI", Integer.parseInt(semI));
			query.setParameter("semF", Integer.parseInt(semF));
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codUnidad", codUnidad);
			resultado.addAll(query.list());
		}
		for(String pato: patos){
			noData1=true;
			noData2=true;
			for(Object[] obj: resultado){
				if(obj[31].toString().matches(pato) && obj[0].toString().matches(anioI)) noData1 = false;
				if(obj[31].toString().matches(pato) && obj[0].toString().matches(anioF)) noData2 = false;
			}
			if (noData1 || noData2){
				query =  session.createQuery("From SivePatologias patologia where patologia.codigo =:codPato");
				query.setParameter("codPato", pato);
				SivePatologias patologiaNoData = (SivePatologias) query.uniqueResult();
				if(noData1){
					Object[] nulo = {anioI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,pato,patologiaNoData.getNombre()};
					resultado.add(nulo);
				}
				if(noData2){
					Object[] nulo = {anioF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,pato,patologiaNoData.getNombre()};
					resultado.add(nulo);
				}
			}
		}
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select 'Pop' as label, pob.anio as anio, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and (pob.anio =:anioI or pob.anio=:anioF) " +
					"group by pob.anio order by pob.anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select 'Pop' as label, pob.anio as anio, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anioI or pob.anio=:anioF) " +
					"group by pob.anio order by pob.anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codSilais", codSilais);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select 'Pop' as label, pob.anio as anio, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento and pob.grupo =:tipoPob and (pob.anio =:anioI or pob.anio=:anioF) " +
					"group by pob.anio order by pob.anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codDepartamento", codDepartamento);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select 'Pop' as label, pob.anio as anio, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anioI or pob.anio=:anioF) " +
					"group by pob.anio order by pob.anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codMunicipio", codMunicipio);
			resultado.addAll(query.list());
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery("Select 'Pop' as label, pob.anio as anio, sum(pob.masculino) as hombres, sum(pob.femenino) as mujeres, sum(pob.total) as totales " +
					"from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anioI or pob.anio=:anioF) " +
					"group by pob.anio order by pob.anio");
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anioI", Integer.parseInt(anioI));
			query.setParameter("anioF", Integer.parseInt(anioF));
			query.setParameter("codUnidad", codUnidad);
			resultado.addAll(query.list());
		}
		query =  session.createQuery("Select 'Pato' as label, patologia.factor as factor, patologia.tipoPob as tipo " +
				"From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
		query.setParameter("codPato", patos[0]);
		resultado.addAll(query.list());
		return resultado;
	}
}