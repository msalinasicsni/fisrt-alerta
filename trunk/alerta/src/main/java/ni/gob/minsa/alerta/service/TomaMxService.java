package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.DaEnvioOrden;
import ni.gob.minsa.alerta.domain.muestra.DaOrdenExamen;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by souyen-ics on 11-05-14.
 */
@Service("tomaMxService")
@Transactional
public class TomaMxService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;


    public DaOrdenExamen getOrdenExamenById(String idOrden){
        String query = "from DaOrdenExamen where idOrdenExamen = :idOrden ";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("idOrden", idOrden);
        return (DaOrdenExamen)q.uniqueResult();
    }

    public void updateOrdenExamen(DaOrdenExamen dto) throws Exception {
        try {
            if (dto != null) {
                Session session = sessionFactory.getCurrentSession();
                session.update(dto);
            }
            else
                throw new Exception("Objeto Orden Examen es NULL");
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

}
