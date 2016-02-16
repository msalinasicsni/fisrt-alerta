package ni.gob.minsa.alerta.utilities.typeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import ni.gob.minsa.alerta.domain.vih.VihFicha;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.UnidadesService;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USER on 14/10/2014.
 */
public class VihFichaTypeAdapter extends TypeAdapter<VihFicha> {

    private SessionFactory sessionFactory;

    public VihFichaTypeAdapter(){

    }

    public VihFichaTypeAdapter(SessionFactory sessionFactory){
        this.sessionFactory=sessionFactory;
    }

    private DivisionPoliticaService divisionPoliticaService = new DivisionPoliticaService();

    private EntidadAdmonService silaisServce = new EntidadAdmonService();

    private UnidadesService unidadesService = new UnidadesService();

    private CatalogoService catalogosService = new CatalogoService();

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out JSON que serializamos y retornamos a la vista
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(final JsonWriter out, final VihFicha value) throws IOException {
        out.beginObject();
        out.name("id_ficha_vih").value(value.getId_ficha_vih());
        out.name("silais").value(value.getEntidadesAdtva());
        out.name("unidadSalud").value(value.getUnidadSalud().getNombre());
        out.name("cod_usuario").value(value.getCodigo_usuario_vih());
        out.name("fecha").value(DateToString(value.getFecha()));
        out.endObject();
    }

    @Override
    public VihFicha read(JsonReader in) throws IOException {
        final VihFicha fichavih= new VihFicha();

        return fichavih;
    }

    /* UTILITARIOS*/
    private Date StringToDate(String strFecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }

    private String DateToString(Date dtFecha)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(dtFecha!=null)
            return simpleDateFormat.format(dtFecha);
        else
            return null;
    }
}
