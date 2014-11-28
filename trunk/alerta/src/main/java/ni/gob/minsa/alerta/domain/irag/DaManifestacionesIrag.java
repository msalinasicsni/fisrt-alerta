package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
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
    private DaIrag idNotificacion;
    private ManifestacionClinica codManifestacion;
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


    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="ID_NOTIFICACION", referencedColumnName = "ID_NOTIFICACION")
    @ForeignKey(name = "ID_NOTI_MCLIN_FK")
    public DaIrag getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(DaIrag idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_MANIFESTACION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_MANICLINICA_FK")
    public ManifestacionClinica getCodManifestacion() { return codManifestacion; }

    public void setCodManifestacion(ManifestacionClinica codManifestacion) { this.codManifestacion = codManifestacion; }

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


}
