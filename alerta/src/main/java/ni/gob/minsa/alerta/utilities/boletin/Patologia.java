package ni.gob.minsa.alerta.utilities.boletin;

/**
 * Created by souyen-ics.
 */
public class Patologia {

    String idPatologia;
    String nombre;
    String tipoPoblacion;
    Integer factor;

    public String getIdPatologia() { return idPatologia; }

    public void setIdPatologia(String idPatologia) { this.idPatologia = idPatologia; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoPoblacion() { return tipoPoblacion; }

    public void setTipoPoblacion(String tipoPoblacion) { this.tipoPoblacion = tipoPoblacion; }

    public Integer getFactor() {  return factor;  }

    public void setFactor(Integer factor) { this.factor = factor; }
}
