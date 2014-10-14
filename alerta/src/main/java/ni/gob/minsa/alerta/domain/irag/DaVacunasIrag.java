package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by souyen-ics
 */
@Entity
@Table(name = "DA_VACUNAS_IRAG", schema = "ALERTA")
public class DaVacunasIrag implements Serializable {



    private Integer idVacuna;
    private DaIrag idIrag;
    private Vacuna codVacuna;
    private Respuesta codAplicada;
    private TipoVacuna codTipoVacuna;
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
    @JoinColumn(name="ID_IRAG", referencedColumnName = "ID_IRAG")
    @ForeignKey(name = "ID_IRAG_VAC_FK")

    public DaIrag getIdIrag() { return idIrag; }

    public void setIdIrag(DaIrag idIrag) { this.idIrag = idIrag; }


    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_VACUNA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_VACUNA_FK")
    public Vacuna getCodVacuna() { return codVacuna; }

    public void setCodVacuna(Vacuna codVacuna) { this.codVacuna = codVacuna; }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_APLICADA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_APLICADA_FK")
    public Respuesta getCodAplicada() { return codAplicada; }

    public void setCodAplicada(Respuesta codAplicada) { this.codAplicada = codAplicada; }


    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_TIPO_VACUNA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_TVACUNA_FK")
    public TipoVacuna getCodTipoVacuna() { return codTipoVacuna; }

    public void setCodTipoVacuna(TipoVacuna codTipoVacuna) { this.codTipoVacuna = codTipoVacuna; }


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
    @Column(name = "FECHA_ULTIMA_DOSIS", nullable = true, insertable = true, updatable = true)
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

        DaVacunasIrag that = (DaVacunasIrag) o;

        if (pasivo != that.pasivo) return false;
        if (!codAplicada.equals(that.codAplicada)) return false;
        if (!codTipoVacuna.equals(that.codTipoVacuna)) return false;
        if (!codVacuna.equals(that.codVacuna)) return false;
        if (!dosis.equals(that.dosis)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!fechaUltimaDosis.equals(that.fechaUltimaDosis)) return false;
        if (!idIrag.equals(that.idIrag)) return false;
        if (!idVacuna.equals(that.idVacuna)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idVacuna.hashCode();
        result = 31 * result + idIrag.hashCode();
        result = 31 * result + codVacuna.hashCode();
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
