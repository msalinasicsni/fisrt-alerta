package ni.gob.minsa.alerta.domain.muestra;

import ni.gob.minsa.alerta.domain.examen.Area;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/**
 * Created by souyen-ics.
 */
@Entity
@Table(name = "catalogo_dx", schema = "alerta")
public class Catalogo_Dx {

    private Integer idDiagnostico;
    private String nombre;
    private boolean pasivo;
    private Area area;


    @Id
    @Column(name = "ID_DIAGNOSTICO", nullable = false, insertable = true, updatable = true, length = 10)
    public Integer getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(Integer idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }


    @Basic
    @Column(name = "NOMBRE", nullable = false, insertable = true, updatable = true, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "PASIVO", nullable = true, insertable = true, updatable = true)
    public boolean isPasivo() {
        return pasivo;
    }

    public void setPasivo(boolean pasivo) {
        this.pasivo = pasivo;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="ID_AREA", referencedColumnName = "ID_AREA", nullable = false)
    @ForeignKey(name = "DX_AREA_FK")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
