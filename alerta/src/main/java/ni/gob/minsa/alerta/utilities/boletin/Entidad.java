package ni.gob.minsa.alerta.utilities.boletin;

/**
 * Created by souyen-ics.
 */
public class Entidad {

    String idEntidad;
    String nombreEntidad;
    Patologia patologia;
    Long totalPoblacion;

    public String getIdEntidad() { return idEntidad; }

    public void setIdEntidad(String idEntidad) { this.idEntidad = idEntidad; }

    public String getNombreEntidad() { return nombreEntidad; }

    public void setNombreEntidad(String nombreEntidad) { this.nombreEntidad = nombreEntidad; }

    public Patologia getPatologia() { return patologia; }

    public void setPatologia(Patologia patologia) { this.patologia = patologia; }

    public Long getTotalPoblacion() { return totalPoblacion; }

    public void setTotalPoblacion(Long totalPoblacion) { this.totalPoblacion = totalPoblacion; }
}
