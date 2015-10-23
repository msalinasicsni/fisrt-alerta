package ni.gob.minsa.alerta.utilities.boletin;

/**
 * Created by souyen-ics.
 */
public class Entidad {

    Integer idEntidad;
    String nombreEntidad;
    Patologia idPatologia;

    public Integer getIdEntidad() { return idEntidad; }

    public void setIdEntidad(Integer idEntidad) { this.idEntidad = idEntidad; }

    public String getNombreEntidad() { return nombreEntidad; }

    public void setNombreEntidad(String nombreEntidad) { this.nombreEntidad = nombreEntidad; }

    public Patologia getIdPatologia() { return idPatologia; }

    public void setIdPatologia(Patologia idPatologia) { this.idPatologia = idPatologia; }
}
