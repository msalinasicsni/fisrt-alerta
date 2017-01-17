package ni.gob.minsa.alerta.domain.sive;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by souyen-ics.
 */
@Entity
@Table(name = "UNIDADES_VW", schema = "ALERTA")
public class UnidadesVwEntity implements Serializable {
    private long unidadId;
    private String nombre;
    private String razonSocial;
    private long tipoUnidad;
    private Divisionpolitica municipio;
    private EntidadesAdtvas entidadAdtva;
    private long categoria;
    private long codigo;
    private char pasivo;
    private String zona;
    private Long unidadAdtva;



    @Basic
    @Id
    @Column(name = "UNIDAD_ID")
    public long getUnidadId() { return unidadId; }

    public void setUnidadId(long unidadId) { this.unidadId = unidadId; }

    @Basic
    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "RAZON_SOCIAL")
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Basic
    @Column(name = "TIPO_UNIDAD")
    public long getTipoUnidad() { return tipoUnidad;   }

    public void setTipoUnidad(long tipoUnidad) {  this.tipoUnidad = tipoUnidad;  }

    @ManyToOne(optional=true)
    @JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL", nullable=true)
    @ForeignKey(name = "UNIDAD_MUNICIPIO_FK")
    public Divisionpolitica getMunicipio() { return this.municipio; }

    public void setMunicipio(Divisionpolitica municipio) { this.municipio = municipio; }

    @ManyToOne(optional=true)
    @JoinColumn(name="ENTIDAD_ADTVA",referencedColumnName="CODIGO", nullable=true)
    @ForeignKey(name = "UNIDAD_SILAIS_FK")
    public EntidadesAdtvas getEntidadAdtva() { return entidadAdtva; }

    public void setEntidadAdtva(EntidadesAdtvas entidadAdtva) { this.entidadAdtva = entidadAdtva;   }

    @Basic
    @Column(name = "CATEGORIA")
    public long getCategoria() { return categoria; }

    public void setCategoria(long categoria) { this.categoria = categoria; }


    @Basic
    @Column(name = "CODIGO")
    public long getCodigo() { return codigo; }

    public void setCodigo(long codigo) { this.codigo = codigo; }


    @Basic
    @Column(name = "PASIVO", length = 1)
    public char getPasivo() {
        return pasivo;
    }

    public void setPasivo(char pasivo) {
        this.pasivo = pasivo;
    }

    @Basic
    @Column(name = "ZONA")
    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }


    @Column(name = "UNIDAD_ADTVA")
    public Long getUnidadAdtva() {  return unidadAdtva; }

    public void setUnidadAdtva(Long unidadAdtva) { this.unidadAdtva = unidadAdtva; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnidadesVwEntity that = (UnidadesVwEntity) o;

        if (unidadId != that.unidadId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (unidadId ^ (unidadId >>> 32));
    }
}
