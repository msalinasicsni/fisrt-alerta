package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
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
    @JoinColumn(name="COD_VACUNA", referencedColumnName = "CODIGO", nullable = false)
    @ForeignKey(name = "COD_VACUNA_FK")
    public Vacuna getCodVacuna() { return codVacuna; }

    public void setCodVacuna(Vacuna codVacuna) { this.codVacuna = codVacuna; }


    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_TIPO_VACUNA", referencedColumnName = "CODIGO", nullable = false)
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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
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

}
