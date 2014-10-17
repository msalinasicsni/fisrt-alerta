package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.irag.*;
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

    @SuppressWarnings("unchecked")
    public List<Captacion> getCaptacion(){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM Captacion capta where capta.pasivo = false order by orden");
       //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Clasificacion> getClasificacion(){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM Clasificacion clas where clas.pasivo = false order by orden");
        //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Respuesta> getRespuesta(){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM Respuesta res where res.pasivo = false  order by orden");
        //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ViaAntibiotico> getViaAntibiotico(){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM ViaAntibiotico via where via.pasivo = false order by orden");
        //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ResultadoRadiologia> getResultadoRadiologia(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM ResultadoRadiologia resRad where resRad.pasivo = false order by orden");
        //retrieve all
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<CondicionEgreso> getCondicionEgreso(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM CondicionEgreso cond where cond.pasivo = false order by orden");
        //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ClasificacionFinal> getClasificacionFinal(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM ClasificacionFinal clas where clas.pasivo = false order by orden");
        //retrieve all
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Vacuna> getVacuna(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM Vacuna vac where vac.pasivo = false order by orden");
        //retrieve all
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<TipoVacuna> getTipoVacuna(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoVacuna tvac where tvac.pasivo = false order by orden");
        //retrieve all
        return query.list();

    }

    public List<TipoVacuna> getTipoVacunaHib(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoVacuna tvac where tvac.pasivo = false and tvac.codigo like :codigo order by orden");
        query.setString("codigo","TVAC|HIB1");
        //retrieve all
        return query.list();
    }

    public List<TipoVacuna> getTipoVacunaMeningococica(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoVacuna tvac where tvac.pasivo = false and tvac.codigo like :codigo order by orden");
        query.setString("codigo","%"+"TVAC|MENING"+"%");
        //retrieve all
        return query.list();
    }

    public List<TipoVacuna> getTipoVacunaNeumococica(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoVacuna tvac where tvac.pasivo = false and tvac.codigo like :codigo order by orden");
        query.setString("codigo","%"+"TVAC|NEUMO"+"%");
        //retrieve all
        return query.list();
    }

    public List<TipoVacuna> getTipoVacunaFlu(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM TipoVacuna tvac where tvac.pasivo = false and tvac.codigo like :codigo order by orden");
        query.setString("codigo","%"+"TVAC|FLU"+"%");
        //retrieve all
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<CondicionPrevia> getCondicionPrevia(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM CondicionPrevia cond where cond.pasivo = false order by orden");
        //retrieve all
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<ManifestacionClinica> getManifestacionClinica(){
        //Retrieve session Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.createQuery("FROM ManifestacionClinica mani where mani.pasivo = false order by orden");
        //retrieve all
        return query.list();

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


    public Captacion getCaptacion(String captacion){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("getCaptacionByCodigo").setString("pCodigo", captacion);
        //Retrieve all
        return (Captacion) query.uniqueResult();
    }

    public Clasificacion getClasificacion(String clasificacion){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("getClasificacionByCodigo").setString("pCodigo", clasificacion);
        //Retrieve all
        return (Clasificacion) query.uniqueResult();
    }

    public Respuesta getRespuesta(String respuesta){
        //Retrieve session from Hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("getRespuestaByCodigo").setString("pCodigo", respuesta);
        //Retrieve all
        return (Respuesta) query.uniqueResult();
    }

    public ViaAntibiotico getViaAntibiotico(String via){
        //Retrieve session from hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a Hibernate query (HQL)
        Query query = session.getNamedQuery("getViaAntibioticoByCodigo").setString("pCodigo", via);
        //Retrieve all
        return (ViaAntibiotico) query.uniqueResult();
    }

    public ResultadoRadiologia getResultadoRadiologia (String res){
        //Retrieve session from hibernate
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getResRadiologiaByCodigo").setString("pCodigo", res);
        //Retrieve all
        return (ResultadoRadiologia) query.uniqueResult();
    }

    public CondicionEgreso getCondicionEgreso (String cond){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getCondicionEgresoByCodigo").setString("pCodigo", cond);
        //Retrieve all
        return (CondicionEgreso) query.uniqueResult();
    }

    public ClasificacionFinal getClasificacionFinal (String clas){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getClasificacionFinalByCodigo").setString("pCodigo", clas);
        //Retrieve all
        return (ClasificacionFinal) query.uniqueResult();
    }

    public Vacuna getVacuna (String vac){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getVacunaByCodigo").setString("pCodigo", vac);
        //Retrieve all
        return (Vacuna) query.uniqueResult();
    }

    public TipoVacuna getTipoVacuna (String tvac){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getTipoVacunaByCodigo").setString("pCodigo", tvac);
        //Retrieve all
        return (TipoVacuna) query.uniqueResult();
    }

    public CondicionPrevia getCondicionPrevia (String cond){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getCondicionPreviaByCodigo").setString("pCodigo", cond);
        //Retrieve all
        return (CondicionPrevia) query.uniqueResult();
    }


    public ManifestacionClinica getManifestacionClinica(String mani){
        //Retrieve session from hibernated
        Session session = sessionFactory.getCurrentSession();
        //Create a hibernate query (HQL)
        Query query = session.getNamedQuery("getManifestacionClinicaByCodigo").setString("pCodigo", mani);
        //Retrieve all
        return (ManifestacionClinica) query.uniqueResult();
    }

}