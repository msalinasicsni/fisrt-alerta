package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaVacunasIrag;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
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
      @SuppressWarnings("unchecked")
      public List<DaVacunasIrag> getAllVaccinesByIdIrag(String id){

          String query = "select vacu FROM DaVacunasIrag vacu where vacu.idIrag = :id and vacu.pasivo = :pasivo";
          org.hibernate.Session session = sessionFactory.getCurrentSession();
          Query q = session.createQuery(query);
          q.setParameter("pasivo", false);
          q.setString("id",id);
          return q.list();
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


    public DaVacunasIrag searchVaccineRecord(String id, String vac, String tipo){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaVacunasIrag vac where vac.idIrag ='"+id+"'and vac.codVacuna = '"+vac+"' and vac.codTipoVacuna = '"+tipo+"' and vac.pasivo = false ");
        DaVacunasIrag vacu = (DaVacunasIrag) query.uniqueResult();
        return vacu;
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
