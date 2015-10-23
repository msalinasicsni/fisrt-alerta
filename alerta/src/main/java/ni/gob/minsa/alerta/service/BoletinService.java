package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.utilities.boletin.Detalle;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souyen-ics.
 */

@Service("boletinService")
@Transactional
public class BoletinService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    Query queryPoblacion = null;
    List<Detalle> resultadoTemp = new ArrayList<Detalle>();

}
