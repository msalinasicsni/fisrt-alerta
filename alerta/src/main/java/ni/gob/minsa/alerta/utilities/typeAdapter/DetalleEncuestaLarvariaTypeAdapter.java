package ni.gob.minsa.alerta.utilities.typeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaDetalleEncuestaLarvaria;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaMaeEncuesta;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FIRSTICT on 9/5/2014.
 */
public class DetalleEncuestaLarvariaTypeAdapter extends TypeAdapter<DaDetalleEncuestaLarvaria>{

    private SessionFactory sessionFactory;

    public DetalleEncuestaLarvariaTypeAdapter() { }

    public DetalleEncuestaLarvariaTypeAdapter(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out JSON que serializamos y retornamos a la vista
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(final JsonWriter out, final DaDetalleEncuestaLarvaria value) throws IOException {
        out.beginObject();
        out.name("detaEncuestaId").value(value.getDetaEncuestaId());
        out.name("encuestaId").value(value.getMaeEncuesta().getEncuestaId());
        out.name("codLocalidad").value(value.getLocalidad().getCodigo());
        out.name("localidad").value(value.getLocalidad().getNombre());
        out.name("pilaInfestado").value(value.getPilaInfestado());
        out.name("llantaInfestado").value(value.getLlantaInfestado());
        out.name("barrilInfestado").value(value.getBarrilInfestado());
        out.name("floreroInfestado").value(value.getFloreroInfestado());
        out.name("bebederoInfestado").value(value.getBebederoInfestado());
        out.name("artEspecialInfes").value(value.getArtEspecialInfes());
        out.name("otrosDepositosInfes").value(value.getOtrosDepositosInfes());
        out.name("cisterInfestado").value(value.getCisterInfestado());
        out.name("inodoroInfestado").value(value.getInodoroInfestado());
        out.name("barroInfestado").value(value.getBarroInfestado());
        out.name("plantaInfestado").value(value.getPlantaInfestado());
        out.name("arbolInfestado").value(value.getArbolInfestado());
        out.name("pozoInfestado").value(value.getPozoInfestado());
        out.name("especieAegypti").value(value.getEspecieAegypti());
        out.name("especieAlbopic").value(value.getEspecieAlbopic());
        out.name("especieCulexQuinque").value(value.getEspecieCulexQuinque());
        out.name("especieCulexNigrip").value(value.getEspecieCulexNigrip());
        out.name("especieCulexCoronat").value(value.getEspecieCulexCoronat());
        out.name("especieCulexErratico").value(value.getEspecieCulexErratico());
        out.name("especieCulexTarsalis").value(value.getEspecieCulexTarsalis());
        out.name("especieCulexFatigans").value(value.getEspecieCulexFatigans());
        out.name("especieCulexAlbim").value(value.getEspecieCulexAlbim());
        out.name("feRegistro").value(value.getFeRegistro().toString());

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
    public DaDetalleEncuestaLarvaria read(JsonReader in) throws IOException {
        final DaDetalleEncuestaLarvaria detalleEncuesta = new DaDetalleEncuestaLarvaria();
        in.beginObject();
        in.endObject();

        return detalleEncuesta;
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
