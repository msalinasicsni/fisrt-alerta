package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaDetalleEncuestaLarvaria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para el manejo de Objeto de dominio "Maestro de Encuesta de Encuesta Entomologica"
 *
 * @author Miguel Salinas
 * @version v1.0
 */
@Service("detalleEncuestaLarvariaService")
@Transactional
public class DaDetalleEncuestaLarvariaService {


    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     * Agrega una Registro de encuesta al maestro y al detalle larvaria
     *
     * @param dto
     * @throws Exception
     */
    public void addDaDetalleEncuestaLarvaria(DaDetalleEncuestaLarvaria dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
            else
                throw new Exception("");
        }catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Actualiza un registro ya sea en el Maestro Encuesta
     *
     * @param dto
     * @throws Exception
     */
    public void updateDaDetalleEncuestaLarvaria(DaDetalleEncuestaLarvaria dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
                throw new Exception("");
        }catch (Exception ex){
            throw ex;
        }
    }

    /**
     *
     * @param idLocalidad
     * @param idEncuestaMaestro
     * @return
     * @throws Exception
     */
    public List<DaDetalleEncuestaLarvaria> getDetalleEncuestaByLocalidad(String idLocalidad, String idEncuestaMaestro) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from DaDetalleEncuestaLarvaria as a join a.maeEncuesta b where a.codLocalidad = :idLocalidad and b.encuestaId = :idMaestro";
        Query q = session.createQuery(query);
        q.setString("idLocalidad",idLocalidad);
        q.setString("idMaestro",idEncuestaMaestro);
        return q.list();
    }

    /**
     * Retorna los detalles de encuestas asociados a un maestro, según id de maestro informado
     * @param idEncuestaMaestro : String con el id del maestro a obtener detalle
     * @return List<DaDetalleEncuestaLarvaria> : Detalles encontrados
     * @throws Exception
     */
    public List<DaDetalleEncuestaLarvaria> getDetalleEncuestaByIdMaestro(String idEncuestaMaestro) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from DaDetalleEncuestaLarvaria as a join a.maeEncuesta as b where b.encuestaId = :idMaestro order by a.feRegistro asc";
        Query q = session.createQuery(query);
        q.setString("idMaestro",idEncuestaMaestro);
        return q.list();
    }

    public DaDetalleEncuestaLarvaria getDetalleEncuestaByIdDetalle(String idEncuestaDetalle) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        String query = "from DaDetalleEncuestaLarvaria where detaEncuestaId = :idEncuestaDetalle";
        Query q = session.createQuery(query);
        q.setString("idEncuestaDetalle",idEncuestaDetalle);
        return (DaDetalleEncuestaLarvaria)q.uniqueResult();
    }
}