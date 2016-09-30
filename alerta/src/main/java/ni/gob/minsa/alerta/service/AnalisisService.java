package ni.gob.minsa.alerta.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ni.gob.minsa.alerta.domain.agrupaciones.GrupoPatologia;
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
    @Resource(name = "admonPatoGroupService")
    private AdmonPatoGroupService admonPatoGroupService;
	//private static final String varOrden = "concat(inf.anio, '-', substring(concat('00', inf.semana),length(concat('00', inf.semana))-1,length(concat('00', inf.semana))))";
	//private static final String sqlData = "Select " + varOrden + " as periodo, sum(inf.totalm+inf.totalf) as total";
	
	//private static final String varOrden = "concat(inf.anio, '-', substring(concat('00', inf.semana),length(concat('00', inf.semana))-1,length(concat('00', inf.semana))))";
	//private static final String sqlData = "Select " + varOrden + " as periodo, sum(inf.totalf) as totalf";
	private static final String sqlData = "Select inf.silais, sum(inf.totalm + inf.totalf) as total";
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataSeries(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad, String codZona,  boolean subunidades){
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
        String patoQuery = null;
        //si es grupo se obtienen todas las patologias asociadas para anidar los códigos mediante OR en el where
        boolean esGrupo = codPato.contains("GRP-");
        if (esGrupo) {
            String idGrupo = codPato.replaceAll("GRP-","");
            List<GrupoPatologia> grupoPatologia = admonPatoGroupService.getGrupoPatologias(Integer.valueOf(idGrupo));
            for(GrupoPatologia grupoP : grupoPatologia){
                //se toma primera pato para sacar población
                codPato = grupoP.getPatologia().getCodigo();
                if (patoQuery == null){
                    patoQuery = "inf.patologia.codigo = '"+grupoP.getPatologia().getCodigo()+"'";
                }
                else{
                    patoQuery = patoQuery + " or inf.patologia.codigo = '"+grupoP.getPatologia().getCodigo()+"'";
                }
            }
        }else{
            patoQuery = "inf.patologia.codigo = '"+codPato+"'";
        }
		if (codArea.equals("AREAREP|PAIS")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where ("+patoQuery +")"+
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			//query.setParameter("codPato", codPato);
		}
		else if (codArea.equals("AREAREP|SILAIS")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where inf.silais =:codSilais and ("+patoQuery +")"+
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			//query.setParameter("codPato", codPato);
			query.setParameter("codSilais", codSilais.toString());
		}
		else if (codArea.equals("AREAREP|DEPTO")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and ("+patoQuery +")"+
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			//query.setParameter("codPato", codPato);
			query.setParameter("codDepartamento", codDepartamento);
		}
		else if (codArea.equals("AREAREP|MUNI")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
					"where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.divisionpoliticaId =:codMunicipio and ("+patoQuery +")"+
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			//query.setParameter("codPato", codPato);
			query.setParameter("codMunicipio", codMunicipio);
		}
		else if (codArea.equals("AREAREP|UNI")){
			query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
					"where (inf.unidad.unidadId =:codUnidad " + (subunidades?"or inf.unidad.unidadAdtva in (select uni.codigo from Unidades uni where uni.unidadId = :codUnidad )":"") +
                    ")and ("+patoQuery +")"+
					"group by inf.fechaNotificacion order by inf.fechaNotificacion");
			//query.setParameter("codPato", codPato);
			query.setParameter("codUnidad", codUnidad);
		}
        else if (codArea.equals("AREAREP|ZE")){
            query = session.createQuery("Select inf.fechaNotificacion as fecha, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                    "where inf.unidad.zona =:codZona and ("+patoQuery +")"+
                    "group by inf.fechaNotificacion order by inf.fechaNotificacion");
            //query.setParameter("codPato", codPato);
            query.setParameter("codZona", codZona);
        }
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataMapas(String codPato, String codArea, Long codSilais, Long codDepartamento, Long codMunicipio, Long codUnidad,
			String semI, String semF, String anioI, String tipoIndicador, boolean paisPorSILAIS){
		// Retrieve session from Hibernate
		List<Object[]> resultado = new ArrayList<Object[]>();
        List<Object[]> resultadoCasos = new ArrayList<Object[]>();
        List<Object[]> datosPoblacion = new ArrayList<Object[]>();
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
        String patoQuery = null;
        //si es grupo se obtienen todas las patologias asociadas para anidar los códigos mediante OR en el where
        boolean esGrupo = codPato.contains("GRP-");
        if (esGrupo) {
            String idGrupo = codPato.replaceAll("GRP-","");
            List<GrupoPatologia> grupoPatologia = admonPatoGroupService.getGrupoPatologias(Integer.valueOf(idGrupo));
            for(GrupoPatologia grupoP : grupoPatologia){
                //se toma primera pato para sacar población
                codPato = grupoP.getPatologia().getCodigo();
                if (patoQuery == null){
                    patoQuery = "inf.patologia.codigo = '"+grupoP.getPatologia().getCodigo()+"'";
                }
                else{
                    patoQuery = patoQuery + " or inf.patologia.codigo = '"+grupoP.getPatologia().getCodigo()+"'";
                }
            }
        }else{
            patoQuery = "inf.patologia.codigo = '"+codPato+"'";
        }

        query =  session.createQuery("From SivePatologiasTipo patologia where patologia.patologia.codigo =:codPato");
		query.setParameter("codPato", codPato);
		SivePatologiasTipo patologia = (SivePatologiasTipo) query.uniqueResult();
            if (codArea.equals("AREAREP|PAIS")) {
                if (paisPorSILAIS) {
                    query = session.createQuery(sqlData + " From SiveInformeDiario inf " +
                            "where ("+patoQuery+") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio=:anioI) " +
                            "group by inf.silais order by inf.silais");
                    //query.setParameter("codPato", codPato);
                    query.setParameter("semI", Integer.parseInt(semI));
                    query.setParameter("semF", Integer.parseInt(semF));
                    query.setParameter("anioI", Integer.parseInt(anioI));

                    if (tipoIndicador.equals("CASOS")) {
                        resultado.addAll(query.list());
                    } else {
                        resultadoCasos.addAll(query.list());
                        query = session.createQuery("Select 'Pob' as poblacion, pob.divpol.dependenciaSilais.codigo as silais, sum(pob.total) as totales " +
                                "from SivePoblacionDivPol pob where pob.grupo =:tipoPob and pob.anio =:anio " +
                                "group by pob.divpol.dependenciaSilais.codigo order by pob.divpol.dependenciaSilais.codigo");
                        query.setParameter("tipoPob", patologia.getTipoPob());
                        query.setParameter("anio", Integer.parseInt(anioI));

                        datosPoblacion.addAll(query.list());
                        double tasa = 0d;
                        for (Object[] casos : resultadoCasos) {
                            for (Object[] poblacion : datosPoblacion) {
                                if (casos[0].toString().equals(poblacion[1].toString())) {
                                    tasa = (double) Math.round((Integer.valueOf(casos[1].toString()).doubleValue()) / Long.valueOf(poblacion[2].toString()) * patologia.getFactor() * 100) / 100;
                                    break;
                                }
                            }
                            Object[] tasas = new Object[2];
                            tasas[0] = casos[0]; //codigo silais
                            tasas[1] = tasa; // tasa calculada
                            resultado.add(tasas);
                        }
                    }
                }else{ //por municipios
                    query = session.createQuery("Select municipio.codigoNacional as munici, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                            "where cast(inf.municipio as long) = municipio.divisionpoliticaId and ("+patoQuery+") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio = :anioI) " +
                            "group by municipio.codigoNacional order by municipio.codigoNacional");
                    //query.setParameter("codPato", codPato);
                    query.setParameter("semI", Integer.parseInt(semI));
                    query.setParameter("semF", Integer.parseInt(semF));
                    query.setParameter("anioI", Integer.parseInt(anioI));

                    if (tipoIndicador.equals("CASOS")) {
                        resultado.addAll(query.list());
                    }else{
                        resultadoCasos.addAll(query.list());

                        query = session.createQuery("Select 'Pob' as poblacion, pob.divpol.codigoNacional as muni, sum(pob.total) as totales " +
                                "from SivePoblacionDivPol pob where pob.grupo =:tipoPob and pob.anio =:anio " +
                                "group by pob.divpol.codigoNacional order by pob.divpol.codigoNacional");
                        query.setParameter("tipoPob", patologia.getTipoPob());
                        query.setParameter("anio", Integer.parseInt(anioI));

                        datosPoblacion.addAll(query.list());
                        double tasa = 0d;
                        for(Object[] casos : resultadoCasos){
                            for(Object[] poblacion : datosPoblacion){
                                if (casos[0].toString().equals(poblacion[1].toString())){
                                    tasa = (double) Math.round((Integer.valueOf(casos[1].toString()).doubleValue())/Long.valueOf(poblacion[2].toString())*patologia.getFactor()*100)/100;
                                    break;
                                }
                            }
                            Object[] tasas = new Object[2];
                            tasas[0] = casos[0]; //codigo municipio
                            tasas[1] = tasa; // tasa calculada
                            resultado.add(tasas);
                        }
                    }
                }
            } else if (codArea.equals("AREAREP|SILAIS")) {
                query = session.createQuery("Select municipio.codigoNacional as munici, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                        "where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and ("+patoQuery+") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio = :anioI) " +
                        "group by municipio.codigoNacional order by municipio.codigoNacional");
                query.setParameter("codSilais", codSilais);
                //query.setParameter("codPato", codPato);
                query.setParameter("semI", Integer.parseInt(semI));
                query.setParameter("semF", Integer.parseInt(semF));
                query.setParameter("anioI", Integer.parseInt(anioI));

                if (tipoIndicador.equals("CASOS")) {
                    resultado.addAll(query.list());
                }else{
                    resultadoCasos.addAll(query.list());

                    query = session.createQuery("Select 'Pob' as poblacion, pob.divpol.codigoNacional as muni, sum(pob.total) as totales " +
                            "from SivePoblacionDivPol pob where pob.grupo =:tipoPob and pob.anio =:anio and pob.divpol.dependenciaSilais.codigo =:codSilais " +
                            "group by pob.divpol.codigoNacional order by pob.divpol.codigoNacional");
                    query.setParameter("tipoPob", patologia.getTipoPob());
                    query.setParameter("anio", Integer.parseInt(anioI));
                    query.setParameter("codSilais", codSilais);

                    datosPoblacion.addAll(query.list());
                    double tasa = 0d;
                    for(Object[] casos : resultadoCasos){
                        for(Object[] poblacion : datosPoblacion){
                            if (casos[0].toString().equals(poblacion[1].toString())){
                                tasa = (double) Math.round((Integer.valueOf(casos[1].toString()).doubleValue())/Long.valueOf(poblacion[2].toString())*patologia.getFactor()*100)/100;
                                break;
                            }
                        }
                        Object[] tasas = new Object[2];
                        tasas[0] = casos[0]; //codigo municipio
                        tasas[1] = tasa; // tasa calculada
                        resultado.add(tasas);
                    }
                }
            }

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