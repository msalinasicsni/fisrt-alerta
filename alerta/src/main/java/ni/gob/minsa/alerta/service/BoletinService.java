package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.agrupaciones.GrupoPatologia;
import ni.gob.minsa.alerta.domain.sive.*;
import ni.gob.minsa.alerta.utilities.boletin.DatosAnio;
import ni.gob.minsa.alerta.utilities.boletin.Detalle;
import ni.gob.minsa.alerta.utilities.boletin.Entidad;
import ni.gob.minsa.alerta.utilities.boletin.Patologia;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souyen-ics.
 */

@Service("boletinService")
@Transactional
public class BoletinService {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name = "sivePatologiasService")
    private SivePatologiasService sivePatologiasService;

    @Resource(name="admonPatoGroupService")
    private AdmonPatoGroupService admonPatoGroupService;

    public List<Detalle> getDataBulletin(String codPato, String codArea, Long codSilais, Long codDepartamento,
                                         String semF, Integer anio) {

        Session session = sessionFactory.getCurrentSession();

        Query queryPoblacion = null;
        Query queryCasos = null;
        Query queryCasosTotal = null;
        Query queryEntidad = null;
        List<Object[]> resultadoTemp = new ArrayList<Object[]>();
        List<Object[]> resultadoTemp1 = new ArrayList<Object[]>();
        List<Object[]> entidad = new ArrayList<Object[]>();
        List<Detalle> resC = new ArrayList<Detalle>();
        List<Detalle> resT = new ArrayList<Detalle>();
        String patoQuery = null;
        String grupoPatoQuery = null;
        String[] patos = codPato.split(",");
        List<Integer> semanas = new ArrayList<Integer>();
        List<Integer> anios = new ArrayList<Integer>();
        List<String> soloPatos=new ArrayList<String>();
        List<String[]> soloGrupos=new ArrayList<String[]>();

        for (String pato : patos) {
            boolean esGrupo = pato.contains("GRP-");
            if (!esGrupo) {
                if (patoQuery == null) {
                    patoQuery = "inf.patologia.codigo = '" + pato + "'";
                } else {
                    patoQuery = patoQuery + " or inf.patologia.codigo = '" + pato + "'";
                }
                soloPatos.add(soloPatos.size(),pato);
            }else{
                String idGrupo = pato.replaceAll("GRP-","");
                String codPatoGrupo = "-";
                String grupo="";
                if (grupoPatoQuery == null) {
                    grupoPatoQuery = "grp.grupo.idGrupo = " + idGrupo + "";
                } else {
                    grupoPatoQuery = grupoPatoQuery + " or grp.grupo.idGrupo = " + idGrupo + "";
                }
                List<GrupoPatologia> grupoPatologia = admonPatoGroupService.getGrupoPatologias(Integer.valueOf(idGrupo));
                if(grupoPatologia.size()>0){
                    //se toma primera pato para sacar población
                    codPatoGrupo = grupoPatologia.get(0).getPatologia().getCodigo();
                    grupo = grupoPatologia.get(0).getGrupo().getNombre();
                }
                String[] datoGrupo = {idGrupo,codPatoGrupo,grupo};
                soloGrupos.add(soloGrupos.size(),datoGrupo);
            }
        }

        if (soloPatos.size()>0) {
            switch (codArea) {
                case "AREAREP|PAIS":

                    queryCasos = session.createQuery(" Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " inf.silais as silais, inf.anio as anio, " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                            " where (" + patoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio, inf.silais " +
                            " order by inf.patologia.codigo, inf.silais, inf.anio");

                    queryCasosTotal = session.createQuery(" Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " inf.silais as silais, inf.anio as anio, " +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si where si.patologia.codigo = inf.patologia.codigo) " +
                            " and sive.patologia.codigo = inf.patologia.codigo" +
                            " group by sive.anio, sive.semana),  " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf " +
                            " where (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio, inf.silais " +
                            " order by inf.patologia.codigo, inf.silais, inf.anio");

                    queryEntidad = session.createQuery("select entidadAdtvaId, nombre from EntidadesAdtvas where pasivo = '0' order by entidadAdtvaId");

                    break;
                case "AREAREP|SILAIS":
                    queryCasos = session.createQuery("Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                            " where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and (" + patoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio, municipio.divisionpoliticaId order by inf.patologia.codigo, municipio.divisionpoliticaId, inf.anio ");
                    queryCasos.setParameter("codSilais", codSilais);

                    queryCasosTotal = session.createQuery("Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio," +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si) " +
                            " and sive.patologia.codigo = inf.patologia.codigo" +
                            " group by sive.anio, sive.semana),  " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                            " where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio, municipio.divisionpoliticaId order by inf.patologia.codigo, municipio.divisionpoliticaId, inf.anio");
                    queryCasosTotal.setParameter("codSilais", codSilais);

                    queryEntidad = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                            " where dp.dependenciaSilais.entidadAdtvaId = :codSilais and dp.pasivo = '0' order by dp.divisionpoliticaId ");
                    queryEntidad.setParameter("codSilais", codSilais);
                    break;
                case "AREAREP|DEPTO":
                    queryCasos = session.createQuery(" Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                            " where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and (" + patoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio,  municipio.divisionpoliticaId order by inf.patologia.codigo,  municipio.divisionpoliticaId, inf.anio");
                    queryCasos.setParameter("codDepartamento", codDepartamento);

                    queryCasosTotal = session.createQuery(" Select inf.patologia.codigo, inf.patologia.nombre, (select tipo.tipoPob from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo),  (select tipo.factor from SivePatologiasTipo tipo where tipo.patologia.codigo = inf.patologia.codigo), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, " +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si) " +
                            " and sive.patologia.codigo = inf.patologia.codigo" +
                            " group by sive.anio, sive.semana),  " +
                            "sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, Divisionpolitica municipio " +
                            " where cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and (" + patoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by inf.patologia.codigo, inf.patologia.nombre, inf.anio, municipio.divisionpoliticaId order by inf.patologia.codigo, municipio.divisionpoliticaId, inf.anio");
                    queryCasosTotal.setParameter("codDepartamento", codDepartamento);

                    queryEntidad = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                            " where dp.dependencia.divisionpoliticaId = :codDepartamento and dp.pasivo = '0' order by dp.divisionpoliticaId");
                    queryEntidad.setParameter("codDepartamento", codDepartamento);
                    break;
            }
        }
        if (soloGrupos.size()>0){
            switch (codArea) {
                case "AREAREP|PAIS":

                    queryCasos = session.createQuery(" Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob)from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " inf.silais as silais, inf.anio as anio, " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp " +
                            " where inf.patologia.id = grp.patologia.id and (" + grupoPatoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio, inf.silais " +
                            " order by grp.grupo.nombre, inf.silais, inf.anio");

                    queryCasosTotal = session.createQuery(" Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob) from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " inf.silais as silais, inf.anio as anio, " +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive, GrupoPatologia grp4 " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si, GrupoPatologia grp3 where si.patologia.id = grp3.patologia.id and grp3.grupo.idGrupo = grp.grupo.idGrupo) " +
                            " and sive.patologia.id = grp4.patologia.id and grp4.grupo.idGrupo = grp.grupo.idGrupo " +
                            " group by sive.anio, sive.semana),  " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp " +
                            " where inf.patologia.id = grp.patologia.id and (" + grupoPatoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio, inf.silais " +
                            " order by grp.grupo.nombre, inf.silais, inf.anio");

                    queryEntidad = session.createQuery("select entidadAdtvaId, nombre from EntidadesAdtvas where pasivo = '0' order by entidadAdtvaId");

                    break;
                case "AREAREP|SILAIS":
                    queryCasos = session.createQuery("Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob)from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp, Divisionpolitica municipio " +
                            " where inf.patologia.id = grp.patologia.id and cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and (" + grupoPatoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio, municipio.divisionpoliticaId order by grp.grupo.nombre, municipio.divisionpoliticaId, inf.anio ");
                    queryCasos.setParameter("codSilais", codSilais);

                    queryCasosTotal = session.createQuery("Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob)from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio," +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive, GrupoPatologia grp4 " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si, GrupoPatologia grp3 where si.patologia.id = grp3.patologia.id and grp3.grupo.idGrupo = grp.grupo.idGrupo) " +
                            " and sive.patologia.id = grp4.patologia.id and grp4.grupo.idGrupo = grp.grupo.idGrupo " +
                            " group by sive.anio, sive.semana),  " +
                            " sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp, Divisionpolitica municipio " +
                            " where inf.patologia.id = grp.patologia.id and cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependenciaSilais.entidadAdtvaId =:codSilais and (" + grupoPatoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio, municipio.divisionpoliticaId order by grp.grupo.nombre, municipio.divisionpoliticaId, inf.anio");
                    queryCasosTotal.setParameter("codSilais", codSilais);

                    queryEntidad = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                            " where dp.dependenciaSilais.entidadAdtvaId = :codSilais and dp.pasivo = '0' order by dp.divisionpoliticaId ");
                    queryEntidad.setParameter("codSilais", codSilais);
                    break;
                case "AREAREP|DEPTO":
                    queryCasos = session.createQuery(" Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob)from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp, Divisionpolitica municipio " +
                            " where inf.patologia.id = grp.patologia.id and cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and (" + grupoPatoQuery + ") and (inf.semana = :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio,  municipio.divisionpoliticaId order by grp.grupo.nombre,  municipio.divisionpoliticaId, inf.anio");
                    queryCasos.setParameter("codDepartamento", codDepartamento);

                    queryCasosTotal = session.createQuery(" Select grp.grupo.idGrupo, grp.grupo.nombre, " +
                            "(select max(tipo.tipoPob)from SivePatologiasTipo tipo, GrupoPatologia grp1 where tipo.patologia.codigo = grp1.patologia.codigo and grp1.grupo.idGrupo = grp.grupo.idGrupo group by tipo.tipoPob), " +
                            "(select max(tipo.factor) from SivePatologiasTipo tipo, GrupoPatologia grp2 where tipo.patologia.codigo = grp2.patologia.codigo and grp2.grupo.idGrupo = grp.grupo.idGrupo group by tipo.factor), " +
                            " municipio.divisionpoliticaId as munici, inf.anio as anio, " +
                            " (select concat(max (sive.semana)  , concat('/', max(sive.anio))) from SiveInformeDiario sive, GrupoPatologia grp4 " +
                            " where sive.fechaNotificacion = (select max(si.fechaNotificacion) as last_record " +
                            " from SiveInformeDiario si, GrupoPatologia grp3 where si.patologia.id = grp3.patologia.id and grp3.grupo.idGrupo = grp.grupo.idGrupo) " +
                            " and sive.patologia.id = grp4.patologia.id and grp4.grupo.idGrupo = grp.grupo.idGrupo " +
                            " group by sive.anio, sive.semana),  " +
                            "sum(inf.totalm+inf.totalf) as total From SiveInformeDiario inf, GrupoPatologia grp, Divisionpolitica municipio " +
                            " where inf.patologia.id = grp.patologia.id and cast(inf.municipio as long) = municipio.divisionpoliticaId and municipio.dependencia.divisionpoliticaId =:codDepartamento and (" + grupoPatoQuery + ") and (inf.semana >= :semI and inf.semana <= :semF) and (inf.anio >= :anio -1 and inf.anio <= :anio) " +
                            " group by grp.grupo.idGrupo, grp.grupo.nombre, inf.anio, municipio.divisionpoliticaId order by grp.grupo.nombre, municipio.divisionpoliticaId, inf.anio");
                    queryCasosTotal.setParameter("codDepartamento", codDepartamento);

                    queryEntidad = session.createQuery("select dp.divisionpoliticaId, dp.nombre from Divisionpolitica dp " +
                            " where dp.dependencia.divisionpoliticaId = :codDepartamento and dp.pasivo = '0' order by dp.divisionpoliticaId");
                    queryEntidad.setParameter("codDepartamento", codDepartamento);
                    break;
            }
        }

        entidad.addAll(queryEntidad.list());
        queryCasosTotal.setParameter("semI", 1);
        queryCasosTotal.setParameter("semF", Integer.parseInt(semF));
        queryCasosTotal.setParameter("anio", anio);
        queryCasos.setParameter("semF", Integer.parseInt(semF));
        queryCasos.setParameter("anio", anio);

        resultadoTemp.addAll(queryCasos.list());
        resultadoTemp1.addAll(queryCasosTotal.list());

        //en caso que se encuentre la misma cantidad de registros
        for (String codPat : patos) {
            boolean esGrupo = codPat.contains("GRP-");
            String nombreGrupo = "";
            if (esGrupo){ //si es grupo, se busca el codigo de la primera patologia del grupo y se quita el prefijo GRP- de patoL para realizar match con los registros obtenidos en las consultas enteriores
                for(String[] datoGrupo : soloGrupos){
                    if (datoGrupo[0].equals(codPat.replaceAll("GRP-",""))){
                        nombreGrupo = datoGrupo[2];
                        break;
                    }
                }
            }
            Patologia patol = new Patologia();
            patol.setIdPatologia(codPat);
            codPat = codPat.replaceAll("GRP-","");
            for (Object[] ent : entidad) {
                boolean contieneEnt = false;
                Entidad enti = new Entidad();
                enti.setIdEntidad(ent[0].toString());
                enti.setNombreEntidad(ent[1].toString());
                enti.setPatologia(patol);

                for (Object[] caso : resultadoTemp1) {
                    //es la misma patologia y la misma entidad
                    if (codPat.matches(caso[0].toString()) && ent[0].toString().matches(caso[4].toString())) {
                        contieneEnt = true;
                        Detalle detalle = new Detalle();
                        DatosAnio dAnio = new DatosAnio();
                        patol.setNombre(caso[1].toString());
                        patol.setTipoPoblacion((String) caso[2]);
                        patol.setFactor((Integer) caso[3]);
                        dAnio.setAnio(Integer.parseInt(caso[5].toString()));
                        dAnio.setEntidad(enti);
                        detalle.setUltimaSemana((caso[6]!=null?caso[6].toString():"-"));
                        detalle.setValorAcum(caso[7].toString());
                        //detalle.setValor(caso[6].toString());
                        detalle.setNombre("Casos");
                        detalle.setAnio(dAnio);
                        resC.add(detalle);
                    }


                }

                //si la lista no contiene datos de una entidad
                //agregar un detalle vacio de la entidad no encontrada

                if (!contieneEnt) {
                    Detalle detalle = new Detalle();
                    DatosAnio dAnio = new DatosAnio();
                    if (!esGrupo) {
                        SivePatologias sive = sivePatologiasService.getSivePatologiaByCod(codPat);
                        patol.setNombre(sive.getNombre());
                    }else{
                       patol.setNombre(nombreGrupo);
                    }
                    dAnio.setAnio(anio);
                    dAnio.setEntidad(enti);
                    detalle.setNombre("Casos");
                    detalle.setAnio(dAnio);
                    resC.add(detalle);

                }

            }

        }

        //recorrer cada registro para calcular la tasa
        for (Detalle de : resC) {

            for (Object[] casoSem : resultadoTemp) {
                if (de.getAnio().getEntidad().getPatologia().getIdPatologia().matches(casoSem[0].toString()) && de.getAnio().getEntidad().getIdEntidad().matches(casoSem[4].toString()) && de.getAnio().getAnio().equals(casoSem[5])) {
                    de.setValor(casoSem[6].toString());
                }
            }

            switch (codArea) {
                case "AREAREP|PAIS":
                    queryPoblacion = session.createQuery("Select sum(pob.total) as total from SivePoblacionDivPol pob " +
                            "where pob.divpol.dependenciaSilais.entidadAdtvaId=:codSilais and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                            "group by pob.anio order by pob.anio");
                    queryPoblacion.setParameter("codSilais", Long.valueOf(de.getAnio().getEntidad().getIdEntidad()));
                    break;
                case "AREAREP|SILAIS":
                    queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                            "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                            "group by pob.anio order by pob.anio");
                    queryPoblacion.setParameter("codMunicipio", Long.valueOf(de.getAnio().getEntidad().getIdEntidad()));
                    break;
                case "AREAREP|DEPTO":
                    queryPoblacion = session.createQuery("Select sum(pob.total) as totales " +
                            "from SivePoblacionDivPol pob where pob.divpol.divisionpoliticaId =:codMunicipio and pob.grupo =:tipoPob and (pob.anio =:anio) " +
                            "group by pob.anio order by pob.anio");
                    queryPoblacion.setParameter("codMunicipio", Long.valueOf(de.getAnio().getEntidad().getIdEntidad()));
                    break;

            }

            queryPoblacion.setParameter("tipoPob", de.getAnio().getEntidad().getPatologia().getTipoPoblacion());
            queryPoblacion.setParameter("anio", de.getAnio().getAnio());


            // detalle.setAnio(de.getAnio());
            // detalle.setNombre("Tasa");

            Long poblacion = (Long) queryPoblacion.uniqueResult();
            if (poblacion != null) {
                if (de.getValorAcum() != null) {
                    //Calcular la tasa en base al acumulado
                    Double tasa = (double) Math.round((Integer.valueOf(de.getValorAcum()).doubleValue()) / poblacion * de.getAnio().getEntidad().getPatologia().getFactor() * 100) / 100;
                    de.setTasa(tasa.toString());
                }

                de.getAnio().getEntidad().setTotalPoblacion(poblacion);
            } else {
                de.setTasa("");
            }

            //resT.add(de);
            // resT.add(detalle);

        }


        return resC;
    }


}
