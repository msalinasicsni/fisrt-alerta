package ni.gob.minsa.alerta.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.sive.SivePatologiasTipo;

import ni.gob.minsa.alerta.utilities.enumeration.HealthUnitType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("analisisObsEsperadosService")
@Transactional
public class AnalisisObsEsperadosService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	private static final String sqlData = "Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, inf.semana as semana,  " +
			"sum(inf.totalm) as totalm, sum(inf.totalf) as totalf, sum(inf.totalm+inf.totalf) as total";

	@SuppressWarnings("unchecked")
	public List<Object[]> getDataCasosTasas(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String semF, String anioI,String anioF){
		// Retrieve session from Hibernate
		List<Object[]> resultado = new ArrayList<Object[]>();
		List<Object[]> resultadoTemp = new ArrayList<Object[]>();
		List<Object> itemTransf = new ArrayList<Object>();
		List<Object[]> resultadoF = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		String patoQuery = null;
		String[] patos = codPato.split(",");
		List<Integer> semanas = new ArrayList<Integer>();
		List<Integer> anios = new ArrayList<Integer>();
		for (int i =0; i<patos.length; i++){
			if (patoQuery == null){
				patoQuery = "inf.patologia.codigo = '"+patos[i]+"'";
			}
			else{
				patoQuery = patoQuery + " or inf.patologia.codigo = '"+patos[i]+"'";
			}
		}
		for (int i = 0; i<=(Integer.parseInt(semF)-Integer.parseInt(semI)); i++){
			semanas.add(Integer.parseInt(semI)+i);
		}
		for (int i = 0; i<=(Integer.parseInt(anioF)-Integer.parseInt(anioI)); i++){
			anios.add(Integer.parseInt(anioI)+i);
		}
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codUnidad", codUnidad);
		}
		
		query.setParameter("semI", Integer.parseInt(semI));
		query.setParameter("semF", Integer.parseInt(semF));
		query.setParameter("anioI", Integer.parseInt(anioI));
		query.setParameter("anioF", Integer.parseInt(anioF));
		resultadoTemp.addAll(query.list());
		
		if(!query.list().isEmpty()){
			String pato =""; String year = "";
			for(Object[] objArray: resultadoTemp){
				if (itemTransf.isEmpty()) {
					itemTransf.add(objArray[0]);itemTransf.add(objArray[1]);itemTransf.add(objArray[2]);
					pato = objArray[0].toString(); year = objArray[2].toString();
				}
				if(!(objArray[0].toString().matches(pato) && objArray[2].toString().matches(year))){
					resultado.add(itemTransf.toArray()); itemTransf.clear();itemTransf.add(objArray[0]);itemTransf.add(objArray[1]);itemTransf.add(objArray[2]);itemTransf.add(objArray[3]);itemTransf.add(objArray[6]);
				}
				else{
					itemTransf.add(objArray[3]);
					itemTransf.add(objArray[6]);
				}
				pato = objArray[0].toString(); year = objArray[2].toString();
			}
			resultado.add(itemTransf.toArray()); itemTransf.clear();
			
			for(String patoL: patos){
				query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
				query.setParameter("codPato", patos[0]);
				SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
				for(Integer anio: anios){
					if (codArea.equals("AREAREP|PAIS")){
						query = session.createQuery("Select sum(pob.total) as total " +
							"from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and pob.anio =:anio " +
							"group by pob.anio order by pob.anio");
					}
					else if (codArea.equals("AREAREP|SILAIS")){
						query = session.createQuery("Select sum(pob.total) as total " +
								"from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anio) " +
								"group by pob.anio order by pob.anio");
						query.setParameter("codSilais", codSilais);
					}
					else if (codArea.equals("AREAREP|DEPTO")){
						query = session.createQuery("Select sum(pob.total) as total " +
								"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento and pob.grupo =:tipoPob and (pob.anio =:anio) " +
								"group by pob.anio order by pob.anio");
						query.setParameter("codDepartamento", codDepartamento);
					}
					else if (codArea.equals("AREAREP|MUNI")){
						query = session.createQuery("Select sum(pob.total) as totales " +
								"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
								"group by pob.anio order by pob.anio");
						query.setParameter("codMunicipio", codMunicipio);
					}
					else if (codArea.equals("AREAREP|UNI")){
						query = session.createQuery("Select sum(pob.total) as total " +
								"from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anio) " +
								"group by pob.anio order by pob.anio");
						query.setParameter("codUnidad", codUnidad);
					}
					query.setParameter("tipoPob", patologia.getTipoPob());
					query.setParameter("anio", anio);
					Long poblacion = (Long) query.uniqueResult();
					
					for(Object[] obj: resultado){
						if((obj[0].toString().matches(patoL) && obj[2].toString().matches(anio.toString()))){
							itemTransf.add(obj[0]);itemTransf.add(obj[1]);itemTransf.add(obj[2]);
							for(Integer sem: semanas){
								boolean noData = true;
								for (int i=3; i<obj.length; i+=2){
									if(obj[i].toString().matches(sem.toString())){
										itemTransf.add(obj[i+1]);
										if (poblacion != null) {
											itemTransf.add((double) Math.round((Integer.valueOf(obj[i+1].toString()).doubleValue())/poblacion*patologia.getFactor()*100)/100);
										}
										else{
											itemTransf.add(0);
										}
										noData = false;
									}
								}
								if(noData) { itemTransf.add(0);itemTransf.add(0.00);}
							}
							break;
						}
					}
					if(!itemTransf.isEmpty()) { resultadoF.add(itemTransf.toArray()); itemTransf.clear();}
				}
			}
		}
		resultadoF.add(semanas.toArray());
		return resultadoF;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataRazonesIndices(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semana, String anio){
		DescriptiveStatistics stats = new DescriptiveStatistics();
		List<Object> itemTransf = new ArrayList<Object>();
		List<Object[]> resultadoF = new ArrayList<Object[]>();
		List<Object[]> semanaActualAnt = new ArrayList<Object[]>();
		List<Object[]> semanaActualAct = new ArrayList<Object[]>();
		List<Object[]> semanasAnterioresAnt = new ArrayList<Object[]>();
		List<Object[]> semanasAnterioresAct = new ArrayList<Object[]>();
		List<Object[]> acumuladosAnt = new ArrayList<Object[]>();
		List<Object[]> acumuladosAct = new ArrayList<Object[]>();
		List<Object[]> medianaSemana = new ArrayList<Object[]>();
		List<Object[]> medianaSemanasAnteriores = new ArrayList<Object[]>();
		List<Object[]> medianaAcumulados = new ArrayList<Object[]>();
		boolean noData = true;
		Long poblacion1 = null;
		Long poblacion2 = null;
		
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		String patoQuery = null;
		String[] patos = codPato.split(",");
		
		for (int i =0; i<patos.length; i++){
			if (patoQuery == null){
				patoQuery = "inf.patologia.codigo = '"+patos[i]+"'";
			}
			else{
				patoQuery = patoQuery + " or inf.patologia.codigo = '"+patos[i]+"'";
			}
		}
		
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre order by inf.patologia.codigo, inf.patologia.nombre");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre order by inf.patologia.codigo, inf.patologia.nombre");
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre order by inf.patologia.codigo, inf.patologia.nombre");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre order by inf.patologia.codigo, inf.patologia.nombre");
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre order by inf.patologia.codigo, inf.patologia.nombre");
			query.setParameter("codUnidad", codUnidad);
		}
		
		//Semana actual año anterior
		query.setParameter("semanaI", Integer.parseInt(semana));
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-1);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		semanaActualAnt.addAll(query.list());
		
		//Semana actual año actual
		query.setParameter("semanaI", Integer.parseInt(semana));
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio));
		query.setParameter("anioF", Integer.parseInt(anio));
		semanaActualAct.addAll(query.list());		
		
		//ultimas cuatro semanas año anterior
		query.setParameter("semanaI", Integer.parseInt(semana)-3);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-1);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		semanasAnterioresAnt.addAll(query.list());
		
		//ultimas cuatro semanas año actual
		query.setParameter("semanaI", Integer.parseInt(semana)-3);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio));
		query.setParameter("anioF", Integer.parseInt(anio));
		semanasAnterioresAct.addAll(query.list());
		
		//acumulados año anterior
		query.setParameter("semanaI", 1);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-1);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		acumuladosAnt.addAll(query.list());
		
		//acumulados año actual
		query.setParameter("semanaI", 1);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio));
		query.setParameter("anioF", Integer.parseInt(anio));
		acumuladosAct.addAll(query.list());		
		
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, " +
				"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
				"where ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
				"group by inf.patologia.codigo, inf.patologia.nombre, inf.anio order by inf.patologia.codigo, inf.patologia.nombre, inf.anio");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, " +
				"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
				"where inf.silais =:codSilais and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
				"group by inf.patologia.codigo, inf.patologia.nombre, inf.anio order by inf.patologia.codigo, inf.patologia.nombre, inf.anio");
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, " +
				"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
				"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
				"group by inf.patologia.codigo, inf.patologia.nombre, inf.anio order by inf.patologia.codigo, inf.patologia.nombre, inf.anio");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, " +
				"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
				"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
				"group by inf.patologia.codigo, inf.patologia.nombre, inf.anio order by inf.patologia.codigo, inf.patologia.nombre, inf.anio");
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery("Select inf.patologia.codigo as codigo, inf.patologia.nombre as patologia, inf.anio as anio, " +
					"sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and ("+ patoQuery +") and (inf.semana >= :semanaI and inf.semana <= :semanaF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.patologia.codigo, inf.patologia.nombre, inf.anio order by inf.patologia.codigo, inf.patologia.nombre, inf.anio");
			query.setParameter("codUnidad", codUnidad);
		}

		//Mediana semana actual
		query.setParameter("semanaI", Integer.parseInt(semana));
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-5);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		medianaSemana.addAll(query.list());		

		//Mediana ultimas cuatro semanas
		query.setParameter("semanaI", Integer.parseInt(semana)-3);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-5);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		medianaSemanasAnteriores.addAll(query.list());
		
		//Mediana acumulados
		query.setParameter("semanaI", 1);
		query.setParameter("semanaF", Integer.parseInt(semana));
		query.setParameter("anioI", Integer.parseInt(anio)-5);
		query.setParameter("anioF", Integer.parseInt(anio)-1);
		medianaAcumulados.addAll(query.list());		
		
		
		for(String patoL: patos){
			query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
			query.setParameter("codPato", patoL);
			SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
			if (codArea.equals("AREAREP|PAIS")){
				query = session.createQuery("Select sum(pob.total) as total " +
					"from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and pob.anio =:anio " +
					"group by pob.anio order by pob.anio");
			}
			else if (codArea.equals("AREAREP|SILAIS")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codSilais", codSilais);
			}
			else if (codArea.equals("AREAREP|DEPTO")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codDepartamento", codDepartamento);
			}
			else if (codArea.equals("AREAREP|MUNI")){
				query = session.createQuery("Select sum(pob.total) as totales " +
						"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codMunicipio", codMunicipio);
			}
			else if (codArea.equals("AREAREP|UNI")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codUnidad", codUnidad);
			}
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anio", Integer.valueOf(anio)-1);
			poblacion1 = (Long) query.uniqueResult();
			query.setParameter("anio", Integer.valueOf(anio));
			poblacion2 = (Long) query.uniqueResult();
			
			itemTransf.add(patologia.getPatologia().getNombre());
			//semana actual año anterior
			noData = true;
			for(Object[] obj: semanaActualAnt){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);
			//semana actual año actual
			noData = true;
			for(Object[] obj: semanaActualAct){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);
			//ultimas cuatro semanas año anterior
			noData = true;
			for(Object[] obj: semanasAnterioresAnt){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);
			//ultimas cuatro semanas año actual
			noData = true;
			for(Object[] obj: semanasAnterioresAct){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);
			//acumulados año anterior
			noData = true;
			for(Object[] obj: acumuladosAnt){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);				
			//acumulados año actual
			noData = true;
			for(Object[] obj: acumuladosAct){
				if(obj[0].toString().matches(patoL)){
					itemTransf.add(obj[2]);
					noData = false;
					break;
				}
			}
			if(noData) itemTransf.add(0);	
			
			// Mediana semana
			for(Object[] obj: medianaSemana){
				if(obj[0].toString().matches(patoL)){
					stats.addValue((long) obj[3]);
				}
			}
			itemTransf.add(stats.getPercentile(50));
			stats.clear();
			
			// Mediana ultimas cuatro semanas
			for(Object[] obj: medianaSemanasAnteriores){
				if(obj[0].toString().matches(patoL)){
					stats.addValue((long) obj[3]);
				}
			}
			itemTransf.add(stats.getPercentile(50));
			stats.clear();	
			
			// Mediana acumulados
			for(Object[] obj: medianaAcumulados){
				if(obj[0].toString().matches(patoL)){
					stats.addValue((long) obj[3]);
				}
			}
			itemTransf.add(stats.getPercentile(50));
			stats.clear();	
			
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(2).toString())/Double.valueOf(itemTransf.get(7).toString()))*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(4).toString())/Double.valueOf(itemTransf.get(8).toString()))*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(6).toString())/Double.valueOf(itemTransf.get(9).toString()))*100)/100);
			
			if(poblacion1!=null){
				itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(5).toString())/poblacion1*patologia.getFactor())*100)/100);
			}
			else{
				itemTransf.add(0);
			}
			if(poblacion2!=null){
				itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(6).toString())/poblacion2*patologia.getFactor())*100)/100);
			}
			else{
				itemTransf.add(0);
			}
			
			
			itemTransf.add(Math.rint(100*(Double.valueOf(itemTransf.get(14).toString())-Double.valueOf(itemTransf.get(13).toString()))/Double.valueOf(itemTransf.get(13).toString())*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(14).toString())-Double.valueOf(itemTransf.get(13).toString()))*100)/100);
			
			itemTransf.add(Math.rint(100*(Double.valueOf(itemTransf.get(6).toString())/Double.valueOf(itemTransf.get(9).toString()))*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(17).toString())/Math.sqrt(Double.valueOf(itemTransf.get(6).toString())))*100)/100);
			itemTransf.add(Math.rint(((Double.valueOf(itemTransf.get(17).toString())/Math.sqrt(Double.valueOf(itemTransf.get(6).toString()))))*1.96*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(17).toString())-Double.valueOf(itemTransf.get(19).toString()))*100)/100);
			itemTransf.add(Math.rint((Double.valueOf(itemTransf.get(17).toString())+Double.valueOf(itemTransf.get(19).toString()))*100)/100);
			
			
			if(!itemTransf.isEmpty()) { resultadoF.add(itemTransf.toArray()); itemTransf.clear();}
		}
		
		//all results
		
		return resultadoF;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataCorredores(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String anioI, int cantAnio){
		// Retrieve session from Hibernate
		List<Object[]> resultadoTemp = new ArrayList<Object[]>();
		Object [][] datos = new Object [52][cantAnio*2+13];
		List<Object[]> resultadoF = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		int semana = Integer.valueOf(semI);
		Double casos,tasa,t = 0D,lnMedia,lnDe,intervalo,lnICinf,lnICsup,iCinfTasa,mediaTasa,iCsupTasa,iCinfCaso,mediaCaso,iCsupCaso;
		Long poblacion = null;
		List<Long> poblaciones = new ArrayList<Long>();
		DescriptiveStatistics stats = new DescriptiveStatistics();
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("codUnidad", codUnidad);
		}
		
		query.setParameter("codPato", codPato);
		query.setParameter("anioI", Integer.parseInt(anioI)-cantAnio);
		query.setParameter("anioF", Integer.parseInt(anioI));
		resultadoTemp.addAll(query.list());
		
		for (int i = 0 ; i < 52 ; i++){
			datos[i][0] = i+1;
		}		
		for(Object[] objeto: resultadoTemp){
			if (Integer.parseInt(objeto[3].toString())<53) datos[Integer.parseInt(objeto[3].toString())-1][Integer.parseInt(objeto[2].toString())-Integer.parseInt(anioI)+cantAnio+1] = objeto[6];
		}
		for (int i = 0; i < datos.length; i++){
			for (int j = 0; j < datos[i].length; j++){
				if (datos[i][j] == null) {
					datos[i][j] = 0;
				}
			}
		}
		query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
		query.setParameter("codPato", codPato);
		SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
		for (int i = (Integer.parseInt(anioI)-cantAnio); i<=Integer.parseInt(anioI); i++){
			if (codArea.equals("AREAREP|PAIS")){
				query = session.createQuery("Select sum(pob.total) as total " +
					"from SivePoblacionDivPol pob where pob.divpol.dependencia is null and pob.grupo =:tipoPob and pob.anio =:anio " +
					"group by pob.anio order by pob.anio");
			}
			else if (codArea.equals("AREAREP|SILAIS")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacionDivPol pob where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codSilais", codSilais);
			}
			else if (codArea.equals("AREAREP|DEPTO")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codDepartamento and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codDepartamento", codDepartamento);
			}
			else if (codArea.equals("AREAREP|MUNI")){
				query = session.createQuery("Select sum(pob.total) as totales " +
						"from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codMunicipio", codMunicipio);
			}
			else if (codArea.equals("AREAREP|UNI")){
				query = session.createQuery("Select sum(pob.total) as total " +
						"from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anio) " +
						"group by pob.anio order by pob.anio");
				query.setParameter("codUnidad", codUnidad);
			}
			query.setParameter("tipoPob", patologia.getTipoPob());
			query.setParameter("anio", i);
			poblacion = (Long) query.uniqueResult();
			if(poblacion == null) poblacion = 5142098L;
			//poblacion = 5142098L;
			poblaciones.add(poblacion);
			for (int j = 0 ; j < 52 ; j++){
				casos = Double.valueOf(datos[j][i-Integer.parseInt(anioI)+cantAnio+1].toString());
				tasa = casos/Double.valueOf(poblacion.toString())*patologia.getFactor()+1; 
				if (i == Integer.parseInt(anioI) && j >= semana){
					datos[j][(i-Integer.parseInt(anioI)+cantAnio+1)+cantAnio+1] = null;
					datos[j][i-Integer.parseInt(anioI)+cantAnio+1] = null;
				}
				else {
					datos[j][(i-Integer.parseInt(anioI)+cantAnio+1)+cantAnio+1] = tasa;
				}
			}
		}
		
		switch (cantAnio){
		case 3:
			t = 4.30;
			break;
		case 4:
			t = 3.18;
			break;
		case 5:
			t = 2.78;
			break;
		case 6:
			t = 2.57;
			break;
		case 7:
			t = 2.45;
			break;
		case 8:
			t = 2.36;
			break;
		case 9:
			t = 2.31;
			break;
		case 10:
			t = 2.26;
			break;
		case 11:
			t = 2.23;
			break;
		case 12:
			t = 2.20;
			break;
		}
		
		for (int i = 0; i < datos.length; i++){
			for (int j = cantAnio+2; j < cantAnio*2+2; j++){
				stats.addValue(Math.log(Double.valueOf(datos[i][j].toString())));
				datos[i][j] = Math.rint(Double.valueOf(datos[i][j].toString())*10000)/10000;
			}
			lnMedia = stats.getMean();
			lnDe= stats.getStandardDeviation();
			intervalo = t*lnDe/Math.sqrt(cantAnio);
			lnICinf = lnMedia - intervalo;
			lnICsup = lnMedia + intervalo;
			iCinfTasa = Math.exp(lnICinf)-1;
			mediaTasa = Math.exp(lnMedia)-1;
			iCsupTasa = Math.exp(lnICsup)-1;
			poblacion = poblaciones.get(poblaciones.size()-1);
			iCinfCaso = iCinfTasa * poblacion / patologia.getFactor();
			mediaCaso = mediaTasa * poblacion / patologia.getFactor();
			iCsupCaso = iCsupTasa * poblacion / patologia.getFactor();
			
			if(datos[i][cantAnio*2+2]!=null) datos[i][cantAnio*2+2] = Math.rint(Double.valueOf(datos[i][cantAnio*2+2].toString())*10000)/10000;
			datos[i][cantAnio*2+3]=Math.rint(lnMedia*10000)/10000;
			datos[i][cantAnio*2+4]=Math.rint(lnDe*10000)/10000;
			datos[i][cantAnio*2+5]=Math.rint(lnICinf*10000)/10000;
			datos[i][cantAnio*2+6]=Math.rint(lnICsup*10000)/10000;
			datos[i][cantAnio*2+7]=Math.rint(iCinfTasa*10000)/10000;
			datos[i][cantAnio*2+8]=Math.rint(mediaTasa*10000)/10000;
			datos[i][cantAnio*2+9]=Math.rint(iCsupTasa*10000)/10000;
			datos[i][cantAnio*2+10]=Math.rint(iCinfCaso*10000)/10000;
			datos[i][cantAnio*2+11]=Math.rint(mediaCaso*10000)/10000;
			datos[i][cantAnio*2+12]=Math.rint(iCsupCaso*10000)/10000;
			stats.clear();
		}
		
		resultadoF.add(datos);
		
		return resultadoF;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataIndice(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String anioI, int cantAnio){
		// Retrieve session from Hibernate
		List<Object[]> resultadoTemp = new ArrayList<Object[]>();
		Object [][] datos = new Object [52][cantAnio+4];
		List<Object[]> resultadoF = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		int semana = Integer.valueOf(semI);
		Double indice,casos,medianaCasos;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.patologia.nombre, inf.patologia.codigo, inf.semana");
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
					"where inf.unidad.unidadId =:codUnidad and inf.patologia.codigo =:codPato and (inf.anio >= :anioI and inf.anio <= :anioF) " +
					"group by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo order by inf.anio, inf.semana, inf.patologia.nombre, inf.patologia.codigo");
			query.setParameter("codUnidad", codUnidad);
		}
		
		query.setParameter("codPato", codPato);
		query.setParameter("anioI", Integer.parseInt(anioI)-cantAnio);
		query.setParameter("anioF", Integer.parseInt(anioI));
		resultadoTemp.addAll(query.list());
		
		for (int i = 0 ; i < 52 ; i++){
			datos[i][0] = i+1;
		}		
		for(Object[] objeto: resultadoTemp){
			if (Integer.parseInt(objeto[3].toString())<53) datos[Integer.parseInt(objeto[3].toString())-1][Integer.parseInt(objeto[2].toString())-Integer.parseInt(anioI)+cantAnio+1] = objeto[6];
		}
		for (int i = 0; i < datos.length; i++){
			for (int j = 0; j < datos[i].length; j++){
				if (datos[i][j] == null) {
					datos[i][j] = 0;
				}
			}
		}
		
		for (int i = 0; i < datos.length; i++){
			for (int j = 1; j < cantAnio+1; j++){
				stats.addValue(Double.valueOf(datos[i][j].toString()));
			}
			medianaCasos = stats.getPercentile(50);			
			datos[i][cantAnio+2]=medianaCasos;
			if(i<semana){
				casos = Double.valueOf(datos[i][cantAnio+1].toString());
				indice = casos / medianaCasos;
				datos[i][cantAnio+3]=indice;
			}
			else{
				datos[i][cantAnio+1]=null;
				datos[i][cantAnio+3]=null;
			}
			stats.clear();
		}
		
		resultadoF.add(datos);
		
		return resultadoF;
	}

    public List<Object[]> getDataCasosTasasArea(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad, String semI, String semF, String anioI, String anioF, boolean porSILAIS, boolean conSubUnidades)
    {
        List<Object[]> resultadoFinal = new ArrayList<Object[]>();
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object[]> instancias = new ArrayList<Object[]>();
        Session session = sessionFactory.getCurrentSession();
        Query queryInstancias = null; //para recuperar la lista de instancias (SILAIS, municipio, o unidad de salud) que se van a mostrar en el reporte
        Query query = null;
        String patoQuery = null;
        List<Integer> anios = new ArrayList<Integer>();
        patoQuery = "inf.patologia.codigo = '"+codPato+"'";

        for (int i = 0; i<=(Integer.parseInt(anioF)-Integer.parseInt(anioI)); i++){
            anios.add(Integer.parseInt(anioI)+i);
        }

        if(codArea.equals("AREAREP|PAIS"))
        {
            if (porSILAIS) {
                query = session.createQuery("Select inf.silais as silais, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                        "where (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                        "group by inf.anio, inf.silais order by inf.silais, inf.anio");

                queryInstancias = session.createQuery("select entidadAdtvaId, nombre from EntidadesAdtvas where pasivo = '0' order by entidadAdtvaId");
            }else{
                query = session.createQuery("Select municipio.dependencia.divisionpoliticaId as departamento, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                        "where cast(inf.municipio as long) = municipio.divisionpoliticaId and (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                        "group by inf.anio, municipio.dependencia.divisionpoliticaId order by municipio.dependencia.divisionpoliticaId, inf.anio");

                queryInstancias = session.createQuery("select divi.divisionpoliticaId, divi.nombre from Divisionpolitica divi where divi.dependencia is null and  divi.pasivo = '0' order by divi.nombre");
            }

        }else if (codArea.equals("AREAREP|SILAIS")){
            query = session.createQuery("Select municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                    "where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, municipio.divisionpoliticaId order by municipio.divisionpoliticaId, inf.anio");
            query.setParameter("codSilais", codSilais);

            queryInstancias = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                    "where dp.dependenciaSilais.entidadAdtvaId = :codSilais and dp.pasivo = '0' order by dp.divisionpoliticaId");
            queryInstancias.setParameter("codSilais", codSilais);
        }
        else if (codArea.equals("AREAREP|DEPTO")){
            query = session.createQuery("Select municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                    "where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, municipio.divisionpoliticaId order by municipio.divisionpoliticaId, inf.anio");
            query.setParameter("codDepartamento", codDepartamento);

            queryInstancias = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                    "where dp.dependencia.divisionpoliticaId = :codDepartamento and dp.pasivo = '0' order by dp.divisionpoliticaId");
            queryInstancias.setParameter("codDepartamento", codDepartamento);
        }
        else if (codArea.equals("AREAREP|MUNI")){
            query = session.createQuery("Select inf.unidad.unidadId as unidadid, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                    "where inf.unidad.municipio.divisionpoliticaId =:codMunicipio and ("+ patoQuery +") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                    "group by inf.anio, inf.unidad.unidadId order by inf.unidad.unidadId, inf.anio");
            query.setParameter("codMunicipio", codMunicipio);

            queryInstancias = session.createQuery("select uni.unidadId, uni.nombre from Unidades uni " +
                    "where uni.municipio.divisionpoliticaId = :codMunicipio " +
                    " and uni.tipoUnidad in ("+ HealthUnitType.UnidadesSIVE.getDiscriminator()+") " +
                    "and uni.pasivo = '0' order by uni.unidadId");
            queryInstancias.setParameter("codMunicipio", codMunicipio);
        }
        else if (codArea.equals("AREAREP|UNI")){
            if (conSubUnidades){
                query = session.createQuery("Select inf.unidad.unidadId as unidadid, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                        "where (inf.unidad.unidadId =:codUnidad or inf.unidad.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad )) and (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                        "group by inf.anio, inf.unidad.unidadId order by inf.unidad.unidadId, inf.anio");

                queryInstancias = session.createQuery("select uni.unidadId, uni.nombre from Unidades uni " +
                        "where (uni.unidadId = :codUnidad or uni.unidadAdtva in (select u.codigo from Unidades u where u.unidadId = :codUnidad )) " +
                        "and uni.pasivo = '0' order by uni.unidadId");

            }else {
                query = session.createQuery("Select inf.unidad.unidadId as unidadid, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                        "where inf.unidad.unidadId =:codUnidad and (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anioI and inf.anio <= :anioF) " +
                        "group by inf.anio, inf.unidad.unidadId order by inf.unidad.unidadId, inf.anio");

                queryInstancias = session.createQuery("select uni.unidadId, uni.nombre from Unidades uni " +
                        "where uni.unidadId = :codUnidad " +
                        "and uni.pasivo = '0' order by uni.unidadId");

            }
            query.setParameter("codUnidad", codUnidad);
            queryInstancias.setParameter("codUnidad", codUnidad);
        }
        instancias.addAll(queryInstancias.list());
        query.setParameter("semI", Integer.parseInt(semI));
        query.setParameter("semF", Integer.parseInt(semF));
        query.setParameter("anioI", Integer.parseInt(anioI));
        query.setParameter("anioF", Integer.parseInt(anioF));
        resultadoTemp.addAll(query.list());
        query = session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
        query.setParameter("codPato", codPato);
        SivePatologiasTipo patologia = (SivePatologiasTipo)query.uniqueResult();
        if(instancias.size() > 0)
        {
            //por cada instancia que se va a mostrar en el reporte, se obtiene los casos agrupados por año en el informe diario
            for (Object[] objArray :instancias )
            {
                Object[] dataInstancia; //objeto que tomara todos los datos de la instancia que se esta consultando ID,NOMBRE,[[año1,[CASOS,TASAS]],[año2,[CASOS,TASAS]],...,[añoN,[CASOS,TASAS]]]
                dataInstancia = new Object[anios.size() + 2];//tamaño es todos los años mas id y nombre de la instancia que se esta filtrando(SILAIS, Departamento, municipio, unidad salud)
                dataInstancia[0] = objArray[0]; //id
                dataInstancia[1] = objArray[1]; //nombre
                int indice = 1;
                int indiceAnio = 0;
                boolean noEncontrado=true;
                for(Object[] objArrayDatos : resultadoTemp){
                    Integer anioEvaluar = anios.get(indiceAnio);
                    List<Object> dataAnios = new ArrayList<Object>();
                    noEncontrado = true;
                    //id de la instancia a mostrar coincide con el id de la instancia en el informe diario
                    if(objArray[0].toString().matches(objArrayDatos[0].toString()))
                    {
                        //mientras no se encuentre registro para el año en evaluación, se compara el año de la instancia en el informe diario contra cada anio indicado por el usuario
                        while (noEncontrado) {
                            //si año que se esta evaluando es menor al año que se obtiene de la entidad actual, significa que para ese año no se encontraron registros y se agrega con caso y tasa null
                            if (anioEvaluar < (int) objArrayDatos[1]) {
                                dataAnios.add(anioEvaluar); //año (ejemplo 2010,2011,2012, etc.)
                                Object casoTasa[] = new Object[2];
                                casoTasa[0] = null; //se recuperan los casos
                                casoTasa[1] = null; //la tasa es null, se seteara posteriormente
                                dataAnios.add(casoTasa); //[caso,tasa]
                                indiceAnio++;
                                indice++;
                                dataInstancia[indice] = dataAnios; //se agrega data(año, [casos,tasas]) del año consultado
                                dataAnios = new ArrayList<Object>();
                                anioEvaluar = anios.get(indiceAnio);//pasamos al siguiente año indicado por el usuario
                            }else{ // coinciden los años, entonces se recuperan casos de la instancia en ese año
                                Object casoTasa[] = new Object[2];
                                casoTasa[0] = objArrayDatos[2]; //se recuperan los casos
                                casoTasa[1] = null; //la tasa es null, se seteara posteriormente
                                dataAnios.add(objArrayDatos[1]); //año (ejemplo 2010,2011,2012, etc.)
                                dataAnios.add(casoTasa); //[caso,tasa]
                                indice++;
                                dataInstancia[indice] = dataAnios; //se agrega data(año, [casos,tasas]) del año consultado
                                noEncontrado = false;//salir del ciclo
                                indiceAnio++;//pasar al siguiente año
                            }
                        }
                    }
                    //si es el último año indicado por el usuario, entonces se reinicia indice de años para que el siguiente registro[instancia,año,casos], se evalue contra todos los años
                    if (indiceAnio==anios.size())
                        indiceAnio=0;
                }
                resultadoFinal.add(dataInstancia);
            }
            //se recorre cada registro para calcular la tasa
            for(Object[] dataInstancia : resultadoFinal)
            {
                int indice = 2; //indice es 2, porque en la posición 0 y 1 estan el id y nombre de la instancia, respectivamente
                List<Object> dataAnios = new ArrayList<Object>();
                //por cada año que se busca se obtiene la población según la instancia, el tipo de población y el año
                for(Integer anio : anios){
                    switch (codArea){
                        case "AREAREP|PAIS" :
                            query = session.createQuery("Select sum(pob.total) as total from SivePoblacionDivPol pob " +
                                    "where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                    "group by pob.anio order by pob.anio");
                            query.setParameter("codSilais", dataInstancia[0]);
                            break;
                        case "AREAREP|SILAIS" :
                            query = session.createQuery("Select sum(pob.total) as totales " +
                                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                    "group by pob.anio order by pob.anio");
                            query.setParameter("codMunicipio", dataInstancia[0]);
                            break;
                        case "AREAREP|DEPTO" :
                            query = session.createQuery("Select sum(pob.total) as totales " +
                                    "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                    "group by pob.anio order by pob.anio");
                            query.setParameter("codMunicipio", dataInstancia[0]);
                            break;
                        case "AREAREP|MUNI" :
                            query = session.createQuery("Select sum(pob.total) as total " +
                                    "from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                    "group by pob.anio order by pob.anio");
                            query.setParameter("codUnidad", codUnidad);
                            break;
                        case "AREAREP|UNI" :
                            query = session.createQuery("Select sum(pob.total) as total " +
                                    "from SivePoblacion pob where pob.comunidad.sector.unidad.unidadId =:codUnidad and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                                    "group by pob.anio order by pob.anio");
                            query.setParameter("codUnidad", codUnidad);
                            break;
                    }

                    query.setParameter("tipoPob", patologia.getTipoPob());
                    query.setParameter("anio", anio);

                    //se recupera la población
                    Long poblacion = (Long)query.uniqueResult();

                    dataAnios = (List<Object>)dataInstancia[indice];// se recupera la info del año que se esta recorriendo
                    //si hay datos sobre el año, y es el año que se esta recorriendo
                    if(dataAnios != null && dataAnios.size() > 0 && dataAnios.get(0).toString().matches(anio.toString()))
                    {
                        Object[] casoTasa = (Object[])dataAnios.get(1);//em la posición 1 esta el dato de casos
                        if(poblacion != null && casoTasa.length > 0 && casoTasa[0] != null) {
                            casoTasa[1] = (double) Math.round((Integer.valueOf(casoTasa[0].toString()).doubleValue())/poblacion*patologia.getFactor()*100)/100;
                        }
                        //se actualiza datos, ya con la tasa calculada
                        dataAnios.remove(1);
                        dataAnios.add(1, casoTasa);
                    }else{//se rellenan con caso y tasa null, los años que no hay registros en la base de datos para esa instancia
                        if (dataAnios == null){
                            dataAnios = new ArrayList<Object>();
                            dataAnios.add(anio);
                            Object casoTasa[] = new Object[2];
                            casoTasa[0] = null; //se recuperan los casos
                            casoTasa[1] = null; //la tasa es null, se seteara posteriormente
                            dataAnios.add(casoTasa); //[caso,tasa]
                            dataInstancia[indice] = dataAnios;
                        }
                    }
                    indice++;
                }
            }
        }
        resultadoFinal.add(anios.toArray());
        return resultadoFinal;
    }


}