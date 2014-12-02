package ni.gob.minsa.alerta.utilities.typeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ni.gob.minsa.alerta.domain.vigilanciaEntomologica.DaDetalleEncuestaAedes;
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
public class DetalleEncuestaAedesTypeAdapter extends TypeAdapter<DaDetalleEncuestaAedes>{

    private SessionFactory sessionFactory;

    public DetalleEncuestaAedesTypeAdapter() { }

    public DetalleEncuestaAedesTypeAdapter(SessionFactory sessionFactory){
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
    public void write(final JsonWriter out, final DaDetalleEncuestaAedes value) throws IOException {
        Double indiceViviendas = 0D;
        Double indiceManzanas = 0D;
        Double indiceDepositos = 0D;
        Double indicePupas = 0D;
        Double indiceBrete = 0D;
        out.beginObject();
        out.name("detaEncuestaId").value(value.getDetaEncuestaId());
        out.name("encuestaId").value(value.getMaeEncuesta().getEncuestaId());
        out.name("codSector").value(value.getLocalidad().getSector().getCodigo());
        out.name("codLocalidad").value(value.getLocalidad().getCodigo());
        out.name("localidad").value(value.getLocalidad().getNombre());
        out.name("viviendasInspec").value(value.getViviendaInspeccionada());
        out.name("viviendasPosit").value(value.getViviendaPositiva());
        out.name("manzanasInspec").value(value.getManzanaInspeccionada());
        out.name("manzanasPosit").value(value.getManzanaPositiva());
        out.name("depositosInspec").value(value.getDepositoInspeccionado());
        out.name("depositosPosit").value(value.getDepositoPositivo());
        out.name("pupasPosit").value(value.getPupaPositiva());
        out.name("noAbati").value((value.getNoAbatizado()!=null?value.getNoAbatizado():0));
        out.name("noElimin").value((value.getNoEliminado()!=null?value.getNoEliminado():0));
        out.name("noNeutr").value((value.getNoNeutralizado()!=null?value.getNoNeutralizado():0));
        out.name("fechaAbat").value((value.getFeAbatizado()!=null?DateToString(value.getFeAbatizado()):""));
        out.name("fechaReport").value((value.getFeRepot()!=null?DateToString(value.getFeRepot()):""));
        out.name("fechaVEnt").value((value.getFeVEnt()!=null?DateToString(value.getFeVEnt()):""));
        out.name("feRegistro").value(value.getFeRegistro().toString());
        indiceViviendas = ((value.getViviendaPositiva()) / (double)(value.getViviendaInspeccionada()))*100;
        indiceManzanas = ((value.getManzanaPositiva()) / (double)(value.getManzanaInspeccionada()))*100;
        indiceDepositos = ((value.getDepositoPositivo()) / (double)(value.getDepositoInspeccionado()))*100;
        indicePupas = ((value.getPupaPositiva()) / (double)(value.getViviendaInspeccionada()))*100;
        indiceBrete = ((value.getDepositoPositivo()) / (double)(value.getViviendaInspeccionada()))*100;
        out.name("indiceViviendas").value(redondear(indiceViviendas,1));
        out.name("indiceManzanas").value(redondear(indiceManzanas,1));
        out.name("indiceDepositos").value(redondear(indiceDepositos,1));
        out.name("indicePupas").value(redondear(indicePupas,1));
        out.name("indiceBrete").value(redondear(indiceBrete,1));
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
    public DaDetalleEncuestaAedes read(JsonReader in) throws IOException {
        final DaDetalleEncuestaAedes detalleEncuestaAedes = new DaDetalleEncuestaAedes();
        return detalleEncuestaAedes;
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


    /**
     * Método para redondear una cantidad a n decimales
     * @param numero cantidad a redondear
     * @param decimales posiciones decimales que se requieren
     * @return
     */
    public double redondear( double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }

}
