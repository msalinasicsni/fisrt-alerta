package ni.gob.minsa.alerta.utilities.boletin;

/**
 * Created by souyen-ics.
 */
public class Detalle {

    DatosAnio anio;
    String nombre;
    String valor;
    String valorAcum;
    String tasa;
    String ultimaSemana;

    public DatosAnio getAnio() { return anio; }

    public void setAnio(DatosAnio anio) { this.anio = anio; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getValor() { return valor; }

    public void setValor(String valor) { this.valor = valor;  }

    public String getValorAcum() { return valorAcum; }

    public void setValorAcum(String valorAcum) { this.valorAcum = valorAcum; }

    public String getTasa() {  return tasa;  }

    public void setTasa(String tasa) { this.tasa = tasa; }

    public String getUltimaSemana() { return ultimaSemana; }

    public void setUltimaSemana(String ultimaSemana) { this.ultimaSemana = ultimaSemana; }
}
