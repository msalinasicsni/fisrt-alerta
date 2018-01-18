package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.examen.*;
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
@Service("organizationChartService")
@Transactional
public class OrganizationChartService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public OrganizationChartService() {
    }

    /***
     * Obtiene el departamento al que pertenece un area dentro de un laboratorio determinado
     * @param codLaboratorio a filtrar
     * @param idArea a filtrar
     * @return Direccion
     */
    public Departamento getDepartamentoAreaByLab(String codLaboratorio, int idArea){
        String query = "select depDir.departamento from DireccionLaboratorio  dirLab, DepartamentoDireccion  depDir, AreaDepartamento aDep " +
                "where depDir.direccionLab.idDireccionLab = dirLab.idDireccionLab and aDep.depDireccion.idDepartDireccion = depDir.idDepartDireccion " +
                "and dirLab.pasivo = false and dirLab.direccion.pasivo = false and depDir.pasivo = false and aDep.pasivo = false " +
                "and dirLab.laboratorio.codigo = :codLaboratorio and aDep.area.idArea = :idArea";
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        q.setParameter("codLaboratorio",codLaboratorio);
        q.setParameter("idArea", idArea);
        return (Departamento)q.uniqueResult();
    }
}
