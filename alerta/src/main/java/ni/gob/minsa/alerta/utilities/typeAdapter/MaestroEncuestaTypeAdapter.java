package ni.gob.minsa.alerta.utilities.typeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import ni.gob.minsa.alerta.service.CatalogoService;
import ni.gob.minsa.alerta.service.DivisionPoliticaService;
import ni.gob.minsa.alerta.service.EntidadAdmonService;
import ni.gob.minsa.alerta.service.UnidadesService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FIRSTICT on 9/4/2014.
 */
public class MaestroEncuestaTypeAdapter extends TypeAdapter<DaMaeEncuesta> {

    private SessionFactory sessionFactory;

    public MaestroEncuestaTypeAdapter() { }

    public MaestroEncuestaTypeAdapter(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
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
    public void write(final JsonWriter out, final DaMaeEncuesta value) throws IOException {
        out.beginObject();
        out.name("encuestaId").value(value.getEncuestaId());
        out.name("silais").value(value.getEntidadesAdtva().getNombre());
        out.name("departamento").value(value.getDepartamento().getNombre());
        out.name("municipio").value(value.getMunicipio().getNombre());
        out.name("distrito").value((value.getCodDistrito()!=null?value.getCodDistrito():""));
        out.name("area").value((value.getCodArea()!=null?value.getCodArea():""));
        out.name("unidadSalud").value(value.getUnidadSalud().getNombre());
        out.name("procedencia").value(value.getProcedencia().getValor());
        out.name("feInicioEncuesta").value(DateToString(value.getFeInicioEncuesta()));
        out.name("feFinEncuesta").value(DateToString(value.getFeFinEncuesta()));
        out.name("ordinalEncu").value(value.getOrdinalEncuesta().getValor());
        out.name("modeloEncu").value(value.getModeloEncuesta().getValor());
        out.name("semanaEpi").value(value.getSemanaEpi());
        out.name("mesEpi").value(value.getMesEpi());
        out.name("anioEpi").value(value.getAnioEpi());
        out.name("usuarioRegistro").value(value.getUsuario().getNombre());
        out.name("fechaRegistro").value(value.getFechaRegistro().toString());

        out.endObject();
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in Json entrante que vamos a Interprestar en el servidor
     * @return the converted Java object. May be null.
     */
    @Override
    public DaMaeEncuesta read(JsonReader in) throws IOException {
        final DaMaeEncuesta maeEncuesta = new DaMaeEncuesta();

        return maeEncuesta;
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
