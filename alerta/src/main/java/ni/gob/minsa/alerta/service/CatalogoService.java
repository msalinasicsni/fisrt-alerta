package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Servicio para el objeto de Catalogos
 *
 * @author Miguel Salinas
 */

@Service("catalogosService")
@Transactional
public class CatalogoService {

    private Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public CatalogoService() {

    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        if(this.sessionFactory == null){
            this.sessionFactory = sessionFactory;
        }
    }

    public List<Catalogo> ElementosCatalogos(String discriminador) throws Exception {
        //String query = "from Catalogo where pasivo = false and nodoPadre.codigo= :discriminador order by orden";
        String query = "from Catalogo";
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        //q.setString("discriminador", discriminador);
        return q.list();
    }
    
    public Catalogo getElementoByCodigo(String codigo) throws Exception {
        String query = "from Catalogo as a where pasivo = false and codigo= :codigo order by orden";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        q.setString("codigo", codigo);
        return  (Catalogo)q.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<ModeloEncuesta> getModeloEncuesta() {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM ModeloEncuesta where pasivo = :pasivo order by orden");
        query.setParameter("pasivo", false);
        // Retrieve all
        return  query.list();
    }
    @SuppressWarnings("unchecked")
         public List<Ordinal> getOrdinalEncuesta() {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM Ordinal where pasivo = :pasivo order by orden");
        query.setParameter("pasivo", false);
        // Retrieve all
        return  query.list();
    }
    @SuppressWarnings("unchecked")
    public List<Procedencia> getProcedencia() {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM Procedencia where pasivo = :pasivo order by orden");
        query.setParameter("pasivo", false);
        // Retrieve all
        return  query.list();
    }
    @SuppressWarnings("unchecked")
    public List<Distritos> getDistritos() {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM Distritos where pasivo = :pasivo order by orden");
        query.setParameter("pasivo", false);
        // Retrieve all
        return  query.list();
    }
    @SuppressWarnings("unchecked")
    public List<Areas> getAreas() {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.createQuery("FROM Areas where pasivo = :pasivo order by orden");
        query.setParameter("pasivo", false);
        // Retrieve all
        return  query.list();
    }

    public ModeloEncuesta getModeloEncuesta(String mencuesta) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("obtenerModelosEncuestaPorCodigo").setString("pCodigo", mencuesta);
        // Retrieve all
        return  (ModeloEncuesta) query.uniqueResult();
    }

    public Ordinal getOrdinalEncuesta(String oencuesta) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("obtenerOrdinalEncuestaPorCodigo").setString("pCodigo", oencuesta);
        // Retrieve all
        return  (Ordinal) query.uniqueResult();
    }
    public Procedencia getProcedencia(String procedencia) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("obtenerProcedenciaPorCodigo").setString("pCodigo", procedencia);
        // Retrieve all
        return  (Procedencia) query.uniqueResult();
    }
    public Distritos getDistritos(String distrito) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("obtenerOrdinalEncuestaPorCodigo").setString("pCodigo", distrito);
        // Retrieve all
        return  (Distritos) query.uniqueResult();
    }
    public Areas getAreas(String area) {
        // Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("obtenerAreasPorCodigo").setString("pCodigo", area);
        // Retrieve all
        return  (Areas) query.uniqueResult();
    }
}