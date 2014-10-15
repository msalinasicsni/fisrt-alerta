package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.ModeloEncuesta;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servicio para el manejo de Objeto de dominio "Maestro de Encuesta de Encuesta Entomologica"
 *
 * @author Miguel Salinas
 * @version v1.0
 */

@Service("daMaeEncuestaService")
@Transactional
public class DaMaeEncuestaService {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     * Agrega una Registro de encuesta al maestro y al detalle
     *
     * @param dto
     * @throws Exception
     */
    public String addDaMaeEncuesta(DaMaeEncuesta dto) throws Exception {
        String idMaestro;
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                idMaestro = (String)session.save(dto);
            }
            else
                throw new Exception("Objeto Maestro Encuesta es NULL");
        }catch (Exception ex){
            throw ex;
        }
        return idMaestro;
    }

    /**
     * Actualiza un registro ya sea en el Maestro Encuesta
     *
     * @param dto
     * @throws Exception
     */
    public void updateDaMaeEncuesta(DaMaeEncuesta dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }else
                throw new Exception("");
        }catch (Exception ex){
        throw ex;
        }
    }

    /**
     * Elimina un registro de Maestro Encuesta
     *
     * @param dto
     * @throws Exception
     */
    public void deleteDaMaeEncuesta(DaMaeEncuesta dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.delete(dto);
            }else
                throw new Exception("");
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<DaMaeEncuesta> searchMaestroEncuestaByDaMaeEncuesta(DaMaeEncuesta maeEncuesta) {
        List<DaMaeEncuesta> result = new ArrayList<DaMaeEncuesta>();
        boolean exist = false;
        try {
            //result = controllersBase.loadAll(DaMaeEncuesta.class);
            //silais, departamento, municipio, unidad salud, año, ordinal, procedencia, fecha inicio, fecha fin, modelo
            Timestamp tsFechaIni = new Timestamp(maeEncuesta.getFeInicioEncuesta().getTime());
            Timestamp tsFechaFin = new Timestamp(maeEncuesta.getFeFinEncuesta().getTime());
            String query = "select ma from DaMaeEncuesta as ma inner join ma.entidadesAdtva as en " +
                    "inner join ma.unidadSalud as un " +
                    "inner join ma.departamento as de " +
                    "inner join ma.municipio as mu " +
                    "inner join ma.ordinalEncuesta as ordi " +
                    "inner join ma.procedencia as pr " +
                    "inner join ma.modeloEncuesta as mo " +
                    "where en.codigo =:codSilais and de.codigoNacional=:departamento and mu.codigoNacional=:municipio " +
                    "and un.codigo=:unidadSalud and ma.anioEpi=:anioEpi and ordi.codigo=:ordinalEncu and pr.codigo=:procedencia " +
                    "and mo.codigo=:modelo and ma.feInicioEncuesta = :fecInicio and ma.feFinEncuesta =:fecFin";

            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setLong("codSilais", maeEncuesta.getEntidadesAdtva().getCodigo());
            q.setString("departamento", maeEncuesta.getDepartamento().getCodigoNacional());
            q.setString("municipio", maeEncuesta.getMunicipio().getCodigoNacional());
            q.setLong("unidadSalud",maeEncuesta.getUnidadSalud().getCodigo());
            q.setInteger("anioEpi",maeEncuesta.getAnioEpi());
            q.setString("ordinalEncu", maeEncuesta.getOrdinalEncuesta().getCodigo());
            q.setString("procedencia", maeEncuesta.getProcedencia().getCodigo());
            q.setString("modelo", maeEncuesta.getModeloEncuesta().getCodigo());
            q.setTimestamp("fecInicio", tsFechaIni);
            q.setTimestamp("fecFin", tsFechaFin);
            result = q.list();

        } catch (Exception ex) {
            try {
                throw new Exception(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //if (!exist)
          //  result = new ArrayList<DaMaeEncuesta>();
        return result;
    }

    public List<DaMaeEncuesta> searchMaestroEncuestaByFiltros(Integer codSilais, Integer codUnidadSalud, Integer anioEpi, Integer mesEpi, String codModeloEncu) {
        List<DaMaeEncuesta> aux = null;
        try {
            //silais, unidad salud, año, mes y modelo
            String query = "from DaMaeEncuesta as a join a.entidadesAdtva as b " +
                    "join a.unidadSalud as c " +
                    "join a.modeloEncuesta as d " +
                    "where b.codigo =:codSilais and c.codigo =:unidadSalud and a.anioEpi=:anioEpi and a.mesEpi=:mesEpi and d.codigo =:modelo";//" and feFinEncuesta =: fecInicio and feFinEncuesta =: fecFin";
            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setInteger("codSilais", codSilais);
            q.setInteger("unidadSalud",codUnidadSalud);
            q.setInteger("anioEpi", anioEpi);
            q.setInteger("mesEpi", mesEpi);
            q.setString("modelo", codModeloEncu);
            aux = q.list();

            } catch (Exception ex) {
            try {
                throw new Exception(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public DaMaeEncuesta getMaestroEncuestaById(String idMaeEncuesta) {
        DaMaeEncuesta aux = null;
        try {
            String query = "from DaMaeEncuesta where encuestaId=:idMaestro";//" and feFinEncuesta =: fecInicio and feFinEncuesta =: fecFin";
            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setString("idMaestro", idMaeEncuesta);
            aux = (DaMaeEncuesta)q.uniqueResult();
        } catch (Exception ex) {
            try {
                throw new Exception(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public ModeloEncuesta getModeloEncuByIdMaestro(String idMaeEncuesta) {
        ModeloEncuesta aux = null;
        try {
            String query = "select mae.modeloEncuesta as mode from DaMaeEncuesta as mae where mae.encuestaId=:idMaestro";//" and feFinEncuesta =: fecInicio and feFinEncuesta =: fecFin";
            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setString("idMaestro", idMaeEncuesta);
            aux = (ModeloEncuesta)q.uniqueResult();
        } catch (Exception ex) {
            try {
                throw new Exception(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    private Date StringToDate(String strFecha) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(strFecha);
    }

    private String DatetToString(Date dtFecha) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(dtFecha);
    }
}