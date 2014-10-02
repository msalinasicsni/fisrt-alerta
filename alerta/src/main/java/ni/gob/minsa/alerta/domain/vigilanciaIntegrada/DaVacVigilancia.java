package ni.gob.minsa.alerta.domain.vigilanciaIntegrada;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by souyen-ics
 */
@Entity
@Table(name = "DA_VAC_VIGILANCIA", schema = "ALERTA")
public class DaVacVigilancia {

    private Integer idVacuna;
    private DaVigilanciaIntegrada idFichaVigilancia;
    private Catalogo codNombreVacuna;
    private Catalogo codAplicada;
    private Catalogo codTipoVacuna;
    private Integer dosis;
    private Date fechaUltimaDosis;
    private Timestamp fechaRegistro;
    private boolean pasivo;
    private Usuarios usuario;


@Id
@GeneratedValue(strategy=GenerationType.AUTO)
@Column(name = "ID_VACUNA", nullable = false, updatable = true, insertable = true, precision = 0)
    public Integer getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(Integer idVacuna) {
        this.idVacuna = idVacuna;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="ID_FICHA_VIGILANCIA", referencedColumnName = "ID_FICHA_VIGILANCIA")
    @ForeignKey(name = "ID_FVIG_VAC_FK")
    public DaVigilanciaIntegrada getIdFichaVigilancia() { return idFichaVigilancia; }

    public void setIdFichaVigilancia(DaVigilanciaIntegrada idFichaVigilancia) { this.idFichaVigilancia = idFichaVigilancia; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_NOMBRE_VACUNA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_VACUNA_FK")
    public Catalogo getCodNombreVacuna() { return codNombreVacuna; }

    public void setCodNombreVacuna(Catalogo codNombreVacuna) { this.codNombreVacuna = codNombreVacuna; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_APLICADA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_APLICADA_FK")
    public Catalogo getCodAplicada() { return codAplicada; }

    public void setCodAplicada(Catalogo codAplicada) {  this.codAplicada = codAplicada; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_TVACUNA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_TVACUNA_FK")
    public Catalogo getCodTipoVacuna() { return codTipoVacuna; }

    public void setCodTipoVacuna(Catalogo codTipoVacuna) { this.codTipoVacuna = codTipoVacuna; }

    @Basic
    @Column(name = "DOSIS", nullable = true, updatable = true, insertable = true, precision = 0)
    public Integer getDosis() {
        return dosis;
    }

    public void setDosis(Integer dosis) {
        this.dosis = dosis;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_ULT_DOSIS", nullable = true, insertable = true, updatable = true)
    public Date getFechaUltimaDosis() {
        return fechaUltimaDosis;
    }

    public void setFechaUltimaDosis(Date fechaUltimaDosis) {
        this.fechaUltimaDosis = fechaUltimaDosis;
    }

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

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_VAC_FK")
    public Usuarios getUsuario() { return usuario; }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaVacVigilancia that = (DaVacVigilancia) o;

        if (pasivo != that.pasivo) return false;
        if (!codAplicada.equals(that.codAplicada)) return false;
        if (!codNombreVacuna.equals(that.codNombreVacuna)) return false;
        if (!codTipoVacuna.equals(that.codTipoVacuna)) return false;
        if (!dosis.equals(that.dosis)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!fechaUltimaDosis.equals(that.fechaUltimaDosis)) return false;
        if (!idFichaVigilancia.equals(that.idFichaVigilancia)) return false;
        if (!idVacuna.equals(that.idVacuna)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idVacuna.hashCode();
        result = 31 * result + idFichaVigilancia.hashCode();
        result = 31 * result + codNombreVacuna.hashCode();
        result = 31 * result + codAplicada.hashCode();
        result = 31 * result + codTipoVacuna.hashCode();
        result = 31 * result + dosis.hashCode();
        result = 31 * result + fechaUltimaDosis.hashCode();
        result = 31 * result + fechaRegistro.hashCode();
        result = 31 * result + (pasivo ? 1 : 0);
        result = 31 * result + usuario.hashCode();
        return result;
    }
}
