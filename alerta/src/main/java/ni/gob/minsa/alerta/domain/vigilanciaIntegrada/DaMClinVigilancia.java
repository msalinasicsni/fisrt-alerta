package ni.gob.minsa.alerta.domain.vigilanciaIntegrada;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by souyen-ics
 */
@Entity
@Table(name = "DA_MANCLI_VIGILANCIA", schema = "ALERTA")
public class DaMClinVigilancia {

    private Integer idManifestacion;
    private DaVigilanciaIntegrada idFichaVigilancia;
    private Catalogo codManiClinica;
    private Catalogo codRespMani;
    private String otraManifestacionCli;
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
    @JoinColumn(name="ID_FICHA_VIGILANCIA", referencedColumnName = "ID_FICHA_VIGILANCIA")
    @ForeignKey(name = "ID_FVIG_MCLIN_FK")
    public DaVigilanciaIntegrada getIdFichaVigilancia() { return idFichaVigilancia;}

    public void setIdFichaVigilancia(DaVigilanciaIntegrada idFichaVigilancia) { this.idFichaVigilancia = idFichaVigilancia;}

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_MANICLINICA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_MANICLINICA_FK")
    public Catalogo getCodManiClinica() { return codManiClinica; }

    public void setCodManiClinica(Catalogo codManiClinica) { this.codManiClinica = codManiClinica; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_RESPMANI", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_RESPMANI_FK")
    public Catalogo getCodRespMani() { return codRespMani; }

    public void setCodRespMani(Catalogo codRespMani) { this.codRespMani = codRespMani; }

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
    @Column(name = "OTRA_MANIFESTACIONCLI", nullable = true, insertable = true, updatable = true, length = 50)
    public String getOtraManifestacionCli() { return otraManifestacionCli; }

    public void setOtraManifestacionCli(String otraManifestacionCli) { this.otraManifestacionCli = otraManifestacionCli; }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_MCLIN_FK")
    public Usuarios getUsuario() { return usuario;  }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaMClinVigilancia that = (DaMClinVigilancia) o;

        if (pasivo != that.pasivo) return false;
        if (!codManiClinica.equals(that.codManiClinica)) return false;
        if (!codRespMani.equals(that.codRespMani)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!idFichaVigilancia.equals(that.idFichaVigilancia)) return false;
        if (!idManifestacion.equals(that.idManifestacion)) return false;
        if (!otraManifestacionCli.equals(that.otraManifestacionCli)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idManifestacion.hashCode();
        result = 31 * result + idFichaVigilancia.hashCode();
        result = 31 * result + codManiClinica.hashCode();
        result = 31 * result + codRespMani.hashCode();
        result = 31 * result + otraManifestacionCli.hashCode();
        result = 31 * result + fechaRegistro.hashCode();
        result = 31 * result + usuario.hashCode();
        result = 31 * result + (pasivo ? 1 : 0);
        return result;
    }
}
