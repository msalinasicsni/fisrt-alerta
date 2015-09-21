package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.Catalogo_Estudio;
import ni.gob.minsa.alerta.domain.muestra.Estudio_UnidadSalud;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics.
 */

@Service("studiesUsService")
@Transactional
public class AdmonStudiesUsService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;


    private Logger logger = LoggerFactory.getLogger(AdmonStudiesUsService.class);

    /**
     * Obtiene una lista del catalogo de estudios activos
     */
    @SuppressWarnings("unchecked")
    public List<Catalogo_Estudio> getStudies() throws Exception {
        String query = "from Catalogo_Estudio where pasivo = false" ;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return q.list();
    }

    /**
     * Obtiene una lista de Unidades de Salud asociadas a un estudio
     * @param id id
     */
    @SuppressWarnings("unchecked")
    public List<Estudio_UnidadSalud> getUsByIdEstudio(Integer id){
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(Estudio_UnidadSalud.class, "estUs");
        cr.createAlias("estUs.estudio", "est");
        cr.add(Restrictions.eq("est.idEstudio", id));
        cr.add(Restrictions.eq("estUs.pasivo", false));
        return cr.list();
    }

    /**
     * Obtiene un registro de Estudio_UnidadSalud por el idEstudio y codigo de Unidad de Salud
     * @param idEstudio
     * @param us
     */
    public Estudio_UnidadSalud getStudyUsByIdEstUs(Integer idEstudio, Integer us){
        String query = "from Estudio_UnidadSalud est where est.estudio.id = :idEstudio and est.unidad.codigo = :us and est.pasivo = false";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setInteger("idEstudio", idEstudio);
        q.setInteger("us", us);
        return (Estudio_UnidadSalud) q.uniqueResult();
    }

    /**
     * Actualiza o agrega una asociacion de Estudio Unidad de Salud
     * @param dto Objeto a actualizar o agregar
     * @throws Exception
     */
    public void addOrUpdateStudyUs(Estudio_UnidadSalud dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.saveOrUpdate(dto);
            }
            else
                throw new Exception("Objeto NULL");
        }catch (Exception ex){
            logger.error("Error al agregar o actualizar asociacion Estudio Unidad de Salud",ex);
            throw ex;
        }
    }

    /**
     * Obtiene un registro de Estudio_UnidadSalud por el id de Registro
     * @param idRecord
     */
    public Estudio_UnidadSalud getStudyUsByIdRecord(Integer idRecord){
        String query = "from Estudio_UnidadSalud est where est.idEstudioUnidad = :idRecord  and est.pasivo = false";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setInteger("idRecord", idRecord);
        return (Estudio_UnidadSalud) q.uniqueResult();
    }

}
