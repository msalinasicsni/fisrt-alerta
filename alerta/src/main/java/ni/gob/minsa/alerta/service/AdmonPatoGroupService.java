package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.agrupaciones.Grupo;
import ni.gob.minsa.alerta.domain.agrupaciones.GrupoPatologia;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by FIRSTICT on 6/16/2016.
 * V1.0
 */
@Service("admonPatoGroupService")
@Transactional
public class AdmonPatoGroupService {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Grupo> getGrupos(){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Grupo");
        return query.list();
    }

    public Grupo getGrupoById(Integer grupo){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Grupo where idGrupo = :idGrupo");
        query.setParameter("idGrupo",grupo);
        return (Grupo)query.uniqueResult();
    }

    public List<GrupoPatologia> getGrupoPatologias(Integer grupo){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GrupoPatologia where grupo.idGrupo = :idGrupo");
        query.setParameter("idGrupo",grupo);
        return query.list();
    }

    public GrupoPatologia getGrupoPatologiaById(Integer id){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GrupoPatologia where idGrupoPatologia = :id");
        query.setParameter("id",id);
        return (GrupoPatologia)query.uniqueResult();
    }

    public List<SivePatologias> getPalogiasDisponibles(Integer grupo){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from SivePatologias sp where sp.id not in (select gp.patologia.id from GrupoPatologia gp where gp.grupo.idGrupo = :idGrupo ) order by nombre");
        query.setParameter("idGrupo",grupo);
        return query.list();
    }

    public SivePatologias getPatologiaById(Integer idPatologia){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from SivePatologias where id = :id");
        query.setParameter("id",idPatologia);
        return (SivePatologias)query.uniqueResult();
    }

    /**
     * Actualiza o agrega un grupo al que se asocian patologías
     * @param dto Objeto a actualizar o agregar
     * @throws Exception
     */
    public void addOrUpdateGroup(Grupo dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.saveOrUpdate(dto);
            }
            else
                throw new Exception("Objeto NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Actualiza o agrega una patologia asociada a un grupo
     * @param dto Objeto a actualizar o agregar
     * @throws Exception
     */
    public void addPatoGroup(GrupoPatologia dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.save(dto);
            }
            else
                throw new Exception("Objeto NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Elimina la asociación de patologia a un grupo
     * @param dto Objeto a eliminar
     * @throws Exception
     */
    public void deletePatoGroup(GrupoPatologia dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.delete(dto);
            }
            else
                throw new Exception("Objeto NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Elimina a un grupo de patologia
     * @param dto Objeto a eliminar
     * @throws Exception
     */
    public void deleteGroup(Grupo dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.delete(dto);
            }
            else
                throw new Exception("Objeto NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }
}
