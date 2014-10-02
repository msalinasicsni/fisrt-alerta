package ni.gob.minsa.alerta.domain.vigilanciaIntegrada;

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
@Table (name = "DA_CONDPRE_VIGILANCIA", schema = "ALERTA", catalog = "")
public class DaCondPreVigilancia implements Serializable {

    private Integer idCondicionPre;
    private DaVigilanciaIntegrada idFichaVigilancia;
    private Catalogo codCondicion;
    private Catalogo codRespuesta;
    private Integer semEmb;
    private String nombreOtraCondicion;
    private Timestamp fechaRegistro;
    private Usuarios usuario;
    private boolean pasivo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CONDICION_PRE", nullable = false, updatable = true, insertable = true, precision = 0)
    public Integer getIdCondicionPre() {
        return idCondicionPre;
    }

    public void setIdCondicionPre(Integer idCondicionPre) {
        this.idCondicionPre = idCondicionPre;
    }


    @ManyToOne(optional=false)
    @JoinColumn(name="ID_FICHA_VIGILANCIA", referencedColumnName = "ID_FICHA_VIGILANCIA")
    @ForeignKey(name = "ID_FVIG_CPRE_FK")
    public DaVigilanciaIntegrada getIdFichaVigilancia() { return idFichaVigilancia; }

    public void setIdFichaVigilancia(DaVigilanciaIntegrada idFichaVigilancia) { this.idFichaVigilancia = idFichaVigilancia; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_CONDICION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CONDICION_FK")
    public Catalogo getCodCondicion() { return codCondicion; }

    public void setCodCondicion(Catalogo codCondicion) { this.codCondicion = codCondicion; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_RESPUESTA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CONDRESPUESTA_FK")
    public Catalogo getCodRespuesta() { return codRespuesta; }

    public void setCodRespuesta(Catalogo codRespuesta) { this.codRespuesta = codRespuesta; }

    @Basic
    @Column(name = "SEM_EMB", nullable = true, updatable = true, insertable = true, precision = 0)
    public Integer getSemEmb() {
        return semEmb;
    }

    public void setSemEmb(Integer semEmb) {
        this.semEmb = semEmb;
    }

    @Basic
    @Column(name = "NOMBRE_OTRA_CONDICION", nullable = true, insertable = true, updatable = true, length = 100)
    public String getNombreOtraCondicion() {
        return nombreOtraCondicion;
    }

    public void setNombreOtraCondicion(String nombreOtraCondicion) {
        this.nombreOtraCondicion = nombreOtraCondicion;
    }

    @Column(name = "FECHA_REGISTRO", nullable = false, insertable = true, updatable = true)
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_CPRE_FK")
    public Usuarios getUsuario() { return usuario; }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }

    @Basic
    @Column(name = "PASIVO", nullable = true, insertable = true, updatable = true)
    public boolean isPasivo() { return pasivo; }

    public void setPasivo(boolean pasivo) { this.pasivo = pasivo; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaCondPreVigilancia that = (DaCondPreVigilancia) o;

        if (pasivo != that.pasivo) return false;
        if (!codCondicion.equals(that.codCondicion)) return false;
        if (!codRespuesta.equals(that.codRespuesta)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!idCondicionPre.equals(that.idCondicionPre)) return false;
        if (!idFichaVigilancia.equals(that.idFichaVigilancia)) return false;
        if (!nombreOtraCondicion.equals(that.nombreOtraCondicion)) return false;
        if (!semEmb.equals(that.semEmb)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idCondicionPre.hashCode();
        result = 31 * result + idFichaVigilancia.hashCode();
        result = 31 * result + codCondicion.hashCode();
        result = 31 * result + codRespuesta.hashCode();
        result = 31 * result + semEmb.hashCode();
        result = 31 * result + nombreOtraCondicion.hashCode();
        result = 31 * result + fechaRegistro.hashCode();
        result = 31 * result + usuario.hashCode();
        result = 31 * result + (pasivo ? 1 : 0);
        return result;
    }
}
