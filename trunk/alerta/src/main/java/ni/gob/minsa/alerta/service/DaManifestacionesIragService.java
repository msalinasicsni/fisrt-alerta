package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.irag.DaManifestacionesIrag;
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
 * Created by souyen-ics
 */
@Service("daManifestacionesIragService")
@Transactional
public class DaManifestacionesIragService {

    static final Logger logger = LoggerFactory.getLogger(DaManifestacionesIrag.class);

    @Resource(name="sessionFactory")
    public SessionFactory sessionFactory;

    /*Retorna lista de Manifestaciones Clinicas
     * @param id
     */
    public List<DaManifestacionesIrag> getAllManifestationsByIdIrag(String id){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select mani FROM DaManifestacionesIrag mani where mani.idIrag = '"+ id +"'");
        return query.list();
    }

    /*Busca registro de manifestaciones clinicas
    * @param1 manifestacion
    * @param2 id
    */
    public DaManifestacionesIrag searchManifestationRecord(String manifestacion, String id){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaManifestacionesIrag manif where manif.codManifestacion ='"+manifestacion+"' and  manif.idIrag = '"+id+"'");
        DaManifestacionesIrag mani = (DaManifestacionesIrag) query.uniqueResult();
        return mani;
    }


    /**
     * Retorna Manifestacin Clinica
     * @param id
     *
     */
    public DaManifestacionesIrag getManifestationById(Integer id) {

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM DaManifestacionesIrag mani where mani.idManifestacion = '" + id + "' ");
        DaManifestacionesIrag mani = (DaManifestacionesIrag) query.uniqueResult();
        return mani;

    }


    /**
     * Agrega Manifestacion Clinica
     *
     */
    public void addManifestation(DaManifestacionesIrag manCli) {
       Session session = sessionFactory.getCurrentSession();
        session.save(manCli);
    }


    /**
     * Actualiza Manifestacion Clinica
     */
    public void updateManifestation(DaManifestacionesIrag manCli) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.update(manCli);
    }


}

