package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.muestra.RecepcionMx;
import ni.gob.minsa.alerta.utilities.reportes.DatosRecepcionMx;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRSTICT on 12/10/2014.
 * V 1.0
 */
@Service("recepcionMxService")
@Transactional
public class RecepcionMxService {

    private Logger logger = LoggerFactory.getLogger(RecepcionMxService.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public RecepcionMxService(){}

    public RecepcionMx getRecepcionMxByCodUnicoMx(String codigoUnicoMx, String codLaboratorio){
        try {
            String query = "select a from RecepcionMx as a inner join a.tomaMx as t where (t.codigoUnicoMx= :codigoUnicoMx or t.codigoLab = :codigoUnicoMx) " +
                    "and a.labRecepcion.codigo = :codLaboratorio";

            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setString("codigoUnicoMx", codigoUnicoMx);
            q.setString("codLaboratorio",codLaboratorio);
            return  (RecepcionMx)q.uniqueResult();
        }catch (Exception ex){
            throw  ex;
        }

    }

    public DatosRecepcionMx getRecepcionMxByCodUnicoMxV2(String codigoUnicoMx, String codLaboratorio){
        try {
            String query = "select a.fechaHoraRecepcion as fechaHoraRecepcion, a.fechaRecibido as fechaRecibido, a.horaRecibido as horaRecibido, " +
                    "coalesce((select c.valor from CalidadMx c where c.codigo = a.calidadMx.codigo ), null) as calidadMx   " +
                    "from RecepcionMx as a inner join a.tomaMx as t where (t.codigoUnicoMx= :codigoUnicoMx or t.codigoLab = :codigoUnicoMx) " +
                    "and a.labRecepcion.codigo = :codLaboratorio";

            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setString("codigoUnicoMx", codigoUnicoMx);
            q.setString("codLaboratorio",codLaboratorio);
            q.setResultTransformer(Transformers.aliasToBean(DatosRecepcionMx.class));
            return  (DatosRecepcionMx)q.uniqueResult();
        }catch (Exception ex){
            throw  ex;
        }

    }
}
