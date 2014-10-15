package ni.gob.minsa.alerta.service;

import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import ni.gob.minsa.alerta.domain.vih.VihFicha;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by USER on 13/10/2014.
 */
@Service("vihFichaService")
@Transactional
public class VihFichaService {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory(){
        return
                sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        if (this.sessionFactory == null) {
            this.sessionFactory = sessionFactory;
        }
    }

    public List<VihFicha> getAllFichasVih() throws Exception {
        String query = "from VihFicha where fechaBaja is null order by id_silais asc";

        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery(query);
        return  q.list();
    }

    public List<VihFicha> searchMaestroFichaByFiltros(Integer codSilais, Integer codUnidadSalud,String codUsuario) {
        List<VihFicha> aux = null;
        try {
            String query="";

            if(codUsuario.equals(""))
            {
            //silais, unidad salud
              query = "from VihFicha as a join a.entidadesAdtva as b " +
                    "join a.unidadSalud as c " +
                    "where b.codigo =:codSilais and c.codigo =:unidadSalud";//" and feFinEncuesta =: fecInicio and feFinEncuesta =: fecFin";
            }
            else
            {
                query = "from VihFicha as a join a.entidadesAdtva as b " +
                        "join a.unidadSalud as c " +
                        "where b.codigo =:codSilais and c.codigo =:unidadSalud and a.codigo_usuario_vih=:codUsuario";
            }
            Session session = sessionFactory.getCurrentSession();
            Query q = session.createQuery(query);
            q.setInteger("codSilais", codSilais);
            q.setInteger("unidadSalud",codUnidadSalud);
            if(!codUsuario.equals("")) {
                q.setString("codUsuario", codUsuario);
            }
            /*q.setInteger("anioEpi", anioEpi);
            q.setInteger("mesEpi", mesEpi);
            q.setString("modelo", codModeloEncu);*/
            aux = q.list();

        } catch (Exception ex) {
            try {
                throw new Exception(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }
}
