package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaVacunasIrag;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by souyen-ics.
 */
@Service("daVacunasIragService")
@Transactional
public class DaVacunasIragService {

    static final Logger logger = LoggerFactory.getLogger(DaVacunasIrag.class);

   @Resource(name ="sessionFactory")
    public SessionFactory sessionFactory;


      /**
       * Retorna lista de Vacunas
       * @param id
       */
    public List<DaVacunasIrag> getAllVaccinesByIdFicha(String id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaVacunasIrag vac where vac.idFichaVigilancia = ' " + id + " ' and vac.pasivo = false ");
        return query.list();
    }



    /**
     * @param id
     * @return
     * @throws Exception
     */
    public DaVacunasIrag getVaccineById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaVacunasIrag vac where vac.idVacuna = '"+ id + "' ");
        DaVacunasIrag vac = (DaVacunasIrag) query.uniqueResult();
        return vac;
    }


    /**
     * Agrega Vacuna
     */
    public void addVaccine(DaVacunasIrag vac) {
        Session session = sessionFactory.getCurrentSession();
        session.save(vac);
    }



    /**
     * Actualiza Vacuna
     */
    public void updateVaccine(DaVacunasIrag vac) {
        Session session = sessionFactory.getCurrentSession();
        session.update(vac);
    }


}
