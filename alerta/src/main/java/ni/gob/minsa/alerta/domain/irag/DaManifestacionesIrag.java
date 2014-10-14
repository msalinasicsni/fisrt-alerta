package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by souyen-ics
 */
@Entity
@Table(name = "DA_MANIFESTACIONES_IRAG", schema = "ALERTA")
public class DaManifestacionesIrag implements Serializable {

    private Integer idManifestacion;
    private DaIrag idIrag;
    private ManifestacionClinica codManifestacion;
    private Respuesta codRespuestaM;
    private String otraManifestacion;
    private Timestamp fechaRegistro;
    private Usuarios usuario;
    private boolean pasivo;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID_MANIFESTACION", nullable = false, updatable = true, insertable = true, precision = 0)
    public Integer getIdManifestacion() {
        return idManifestacion;
    }

    public void setIdManifestacion(Integer idManifestacion) {
        this.idManifestacion = idManifestacion;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="ID_IRAG", referencedColumnName = "ID_IRAG")
    @ForeignKey(name = "ID_IRAG_MCLIN_FK")
    public DaIrag getIdIrag() {  return idIrag; }

    public void setIdIrag(DaIrag idIrag) { this.idIrag = idIrag;     }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_MANIFESTACION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_MANICLINICA_FK")
    public ManifestacionClinica getCodManifestacion() { return codManifestacion; }

    public void setCodManifestacion(ManifestacionClinica codManifestacion) { this.codManifestacion = codManifestacion; }


    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_RESPUESTAM", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_RESPMANI_FK")
    public Respuesta getCodRespuestaM() { return codRespuestaM; }

    public void setCodRespuestaM(Respuesta codRespuestaM) { this.codRespuestaM = codRespuestaM; }


    @Basic
    @Column(name = "FECHA_REGISTRO", nullable = false, insertable = true, updatable = true)
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Basic
    @Column(name = "PASIVO", nullable = true, insertable = true, updatable = true)
    public boolean isPasivo() { return pasivo; }

    public void setPasivo(boolean pasivo) { this.pasivo = pasivo; }

    @Basic
    @Column(name = "OTRA_MANIFESTACION", nullable = true, insertable = true, updatable = true, length = 50)
    public String getOtraManifestacion() { return otraManifestacion; }

    public void setOtraManifestacion(String otraManifestacion) { this.otraManifestacion = otraManifestacion; }


    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_MCLIN_FK")
    public Usuarios getUsuario() { return usuario;  }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaManifestacionesIrag that = (DaManifestacionesIrag) o;

        if (pasivo != that.pasivo) return false;
        if (!codManifestacion.equals(that.codManifestacion)) return false;
        if (!codRespuestaM.equals(that.codRespuestaM)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!idIrag.equals(that.idIrag)) return false;
        if (!idManifestacion.equals(that.idManifestacion)) return false;
        if (!otraManifestacion.equals(that.otraManifestacion)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idManifestacion.hashCode();
        result = 31 * result + idIrag.hashCode();
        result = 31 * result + codManifestacion.hashCode();
        result = 31 * result + codRespuestaM.hashCode();
        result = 31 * result + otraManifestacion.hashCode();
        result = 31 * result + fechaRegistro.hashCode();
        result = 31 * result + usuario.hashCode();
        result = 31 * result + (pasivo ? 1 : 0);
        return result;
    }
}
