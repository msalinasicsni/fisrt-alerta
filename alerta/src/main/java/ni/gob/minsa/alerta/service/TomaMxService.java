package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by souyen-ics on 11-05-14.
 */
@Service("tomaMxService")
@Transactional
public class TomaMxService {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;






}
