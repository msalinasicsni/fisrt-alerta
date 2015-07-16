package ni.gob.minsa.alerta.domain.muestra;

import ni.gob.minsa.alerta.domain.examen.Area;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by FIRSTICT on 2/24/2015.
 * V1.0
 */
@Entity
@Table(name = "catalogo_estudio", schema = "laboratorio")
public class Catalogo_Estudio implements Serializable {

    private static final long serialVersionUID = 5110985758665058146L;
    private Integer idEstudio;
    private String nombre;
    private boolean pasivo;
    private Area area;
    private String codigo;

    @Id
    @Column(name = "ID_ESTUDIO", nullable = false, insertable = true, updatable = true, length = 10)
    public Integer getIdEstudio() {
        return idEstudio;
    }

    public void setIdEstudio(Integer idEstudio) {
        this.idEstudio = idEstudio;
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
    @ForeignKey(name = "ESTUDIO_AREA_FK")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Basic
    @Column(name = "CODIGO", nullable = false, insertable = true, updatable = true, length = 16)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
