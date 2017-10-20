package ni.gob.minsa.alerta.domain.sive;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by FIRSTICT on 2/16/2016.
 * V1.0
 */
@Entity
@javax.persistence.Table(name = "sive_informe_diario", schema = "sive")
public class SiveInformeDiario implements Serializable, Auditable {
    private String silais;
    private String actor;

    @Id
    @javax.persistence.Column(name = "SILAIS", nullable = false, insertable = true, updatable = true, length = 2)
    public String getSilais() {
        return silais;
    }

    public void setSilais(String silais) {
        this.silais = silais;
    }

    private String municipio;

    @Id
    @javax.persistence.Column(name = "MUNICIPIO", nullable = false, insertable = true, updatable = true, length = 4)
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    private UnidadesVwEntity unidad;

    @Id
    @ManyToOne
    @JoinColumn(name = "UNIDAD_SALUD", referencedColumnName = "UNIDAD_ID")//@javax.persistence.Column(name = "UNIDAD_SALUD", nullable = false, insertable = true, updatable = true, precision = -127)
    public UnidadesVwEntity getUnidad() { return unidad; }
    public void setUnidad(UnidadesVwEntity unidad) { this.unidad = unidad; }

    private Date fechaNotificacion;

    @Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @javax.persistence.Column(name = "FECHA", nullable = false, insertable = true, updatable = true)
    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fecha) {
        this.fechaNotificacion = fecha;
    }

    private Integer semana;

    @Basic
    @javax.persistence.Column(name = "SEMANA_EPI", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getSemana() {
        return semana;
    }

    public void setSemana(Integer semanaEpi) {
        this.semana = semanaEpi;
    }

    private SivePatologias patologia;

    @Id
    @ManyToOne
    @JoinColumn(name="COD_PATOLOGIA",referencedColumnName = "CODIGO") //@javax.persistence.Column(name = "COD_PATOLOGIA", nullable = false, insertable = true, updatable = true, length = 6)
    public SivePatologias getPatologia() {
        return patologia;
    }

    public void setPatologia(SivePatologias patologia) {
        this.patologia = patologia;
    }

    private Integer g01f;

    @Basic
    @javax.persistence.Column(name = "GRUPO1_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG01f() {
        return g01f;
    }

    public void setG01f(Integer grupo1F) {
        this.g01f = grupo1F;
    }

    private Integer g01m;

    @Basic
    @javax.persistence.Column(name = "GRUPO1_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG01m() {
        return g01m;
    }

    public void setG01m(Integer grupo1M) {
        this.g01m = grupo1M;
    }

    private Integer g02f;

    @Basic
    @javax.persistence.Column(name = "GRUPO2_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG02f() {
        return g02f;
    }

    public void setG02f(Integer grupo2F) {
        this.g02f = grupo2F;
    }

    private Integer g02m;

    @Basic
    @javax.persistence.Column(name = "GRUPO2_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG02m() {
        return g02m;
    }

    public void setG02m(Integer grupo2M) {
        this.g02m = grupo2M;
    }

    private Integer g03f;

    @Basic
    @javax.persistence.Column(name = "GRUPO3_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG03f() {
        return g03f;
    }

    public void setG03f(Integer grupo3F) {
        this.g03f = grupo3F;
    }

    private Integer g03m;

    @Basic
    @javax.persistence.Column(name = "GRUPO3_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG03m() {
        return g03m;
    }

    public void setG03m(Integer grupo3M) {
        this.g03m = grupo3M;
    }

    private Integer g04f;

    @Basic
    @javax.persistence.Column(name = "GRUPO4_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG04f() {
        return g04f;
    }

    public void setG04f(Integer grupo4F) {
        this.g04f = grupo4F;
    }

    private Integer g04m;

    @Basic
    @javax.persistence.Column(name = "GRUPO4_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG04m() {
        return g04m;
    }

    public void setG04m(Integer grupo4M) {
        this.g04m = grupo4M;
    }

    private Integer g05f;

    @Basic
    @javax.persistence.Column(name = "GRUPO5_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG05f() {
        return g05f;
    }

    public void setG05f(Integer grupo5F) {
        this.g05f = grupo5F;
    }

    private Integer g05m;

    @Basic
    @javax.persistence.Column(name = "GRUPO5_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG05m() {
        return g05m;
    }

    public void setG05m(Integer grupo5M) {
        this.g05m = grupo5M;
    }

    private Integer g06f;

    @Basic
    @javax.persistence.Column(name = "GRUPO6_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG06f() {
        return g06f;
    }

    public void setG06f(Integer grupo6F) {
        this.g06f = grupo6F;
    }

    private Integer g06m;

    @Basic
    @javax.persistence.Column(name = "GRUPO6_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG06m() {
        return g06m;
    }

    public void setG06m(Integer grupo6M) {
        this.g06m = grupo6M;
    }

    private Integer g07f;

    @Basic
    @javax.persistence.Column(name = "GRUPO7_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG07f() {
        return g07f;
    }

    public void setG07f(Integer grupo7F) {
        this.g07f = grupo7F;
    }

    private Integer g07m;

    @Basic
    @javax.persistence.Column(name = "GRUPO7_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG07m() {
        return g07m;
    }

    public void setG07m(Integer grupo7M) {
        this.g07m = grupo7M;
    }

    private Integer g08f;

    @Basic
    @javax.persistence.Column(name = "GRUPO8_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG08f() {
        return g08f;
    }

    public void setG08f(Integer grupo8F) {
        this.g08f = grupo8F;
    }

    private Integer g08m;

    @Basic
    @javax.persistence.Column(name = "GRUPO8_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG08m() {
        return g08m;
    }

    public void setG08m(Integer grupo8M) {
        this.g08m = grupo8M;
    }

    private Integer g09f;

    @Basic
    @javax.persistence.Column(name = "GRUPO9_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG09f() {
        return g09f;
    }

    public void setG09f(Integer grupo9F) {
        this.g09f = grupo9F;
    }

    private Integer g09m;

    @Basic
    @javax.persistence.Column(name = "GRUPO9_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG09m() {
        return g09m;
    }

    public void setG09m(Integer grupo9M) {
        this.g09m = grupo9M;
    }

    private Integer g10f;

    @Basic
    @javax.persistence.Column(name = "GRUPO10_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG10f() {
        return g10f;
    }

    public void setG10f(Integer grupo10F) {
        this.g10f = grupo10F;
    }

    private Integer g10m;

    @Basic
    @javax.persistence.Column(name = "GRUPO10_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG10m() {
        return g10m;
    }

    public void setG10m(Integer grupo10M) {
        this.g10m = grupo10M;
    }

    private Integer g11f;

    @Basic
    @javax.persistence.Column(name = "GRUPO11_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG11f() {
        return g11f;
    }

    public void setG11f(Integer grupo11F) {
        this.g11f = grupo11F;
    }

    private Integer g11m;

    @Basic
    @javax.persistence.Column(name = "GRUPO11_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG11m() {
        return g11m;
    }

    public void setG11m(Integer grupo11M) {
        this.g11m = grupo11M;
    }

    private Integer g12f;

    @Basic
    @javax.persistence.Column(name = "GRUPO12_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG12f() {
        return g12f;
    }

    public void setG12f(Integer grupo12F) {
        this.g12f = grupo12F;
    }

    private Integer g12m;

    @Basic
    @javax.persistence.Column(name = "GRUPO12_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG12m() {
        return g12m;
    }

    public void setG12m(Integer grupo12M) {
        this.g12m = grupo12M;
    }

    private Integer g13f;

    @Basic
    @javax.persistence.Column(name = "GRUPO13_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG13f() {
        return g13f;
    }

    public void setG13f(Integer grupo13F) {
        this.g13f = grupo13F;
    }

    private Integer g13m;

    @Basic
    @javax.persistence.Column(name = "GRUPO13_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG13m() {
        return g13m;
    }

    public void setG13m(Integer grupo13M) {
        this.g13m = grupo13M;
    }

    private Integer descf;

    @Basic
    @javax.persistence.Column(name = "DESC_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescf() {
        return descf;
    }

    public void setDescf(Integer descF) {
        this.descf = descF;
    }

    private Integer descm;

    @Basic
    @javax.persistence.Column(name = "DESC_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescm() {
        return descm;
    }

    public void setDescm(Integer descM) {
        this.descm = descM;
    }

    private Integer totalf;

    @Basic
    @javax.persistence.Column(name = "TOTAL_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalf() {
        return totalf;
    }

    public void setTotalf(Integer totalF) {
        this.totalf = totalF;
    }

    private Integer totalm;

    @Basic
    @javax.persistence.Column(name = "TOTAL_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalm() {
        return totalm;
    }

    public void setTotalm(Integer totalM) {
        this.totalm = totalM;
    }

    private Integer bloqueado;

    @Basic
    @javax.persistence.Column(name = "BLOQUEADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Integer bloqueado) {
        this.bloqueado = bloqueado;
    }

    private Date fecharegistro;

    @Basic
    @Temporal(TemporalType.DATE)
    @javax.persistence.Column(name = "FECHAREGISTRO", nullable = true, insertable = true, updatable = true)
    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    private String usuarioregistro;

    @Basic
    @javax.persistence.Column(name = "USUARIOREGISTRO", nullable = true, insertable = true, updatable = true, length = 20)
    public String getUsuarioregistro() {
        return usuarioregistro;
    }

    public void setUsuarioregistro(String usuarioregistro) {
        this.usuarioregistro = usuarioregistro;
    }

    private Date fechamodificacion;

    @Basic
    @Temporal(TemporalType.DATE)
    @javax.persistence.Column(name = "FECHAMODIFICACION", nullable = true, insertable = true, updatable = true)
    public Date getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    private String usuariomodificacion;

    @Basic
    @javax.persistence.Column(name = "USUARIOMODIFICACION", nullable = true, insertable = true, updatable = true, length = 20)
    public String getUsuariomodificacion() {
        return usuariomodificacion;
    }

    public void setUsuariomodificacion(String usuariomodificacion) {
        this.usuariomodificacion = usuariomodificacion;
    }

    private Integer numeroOrden;

    @Basic
    @javax.persistence.Column(name = "NUMERO_ORDEN", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    private Integer codSisniven;

    @Basic
    @javax.persistence.Column(name = "COD_SISNIVEN", nullable = true, insertable = true, updatable = true, precision = -127)
    public Integer getCodSisniven() {
        return codSisniven;
    }

    public void setCodSisniven(Integer codSisniven) {
        this.codSisniven = codSisniven;
    }

    private Integer anio;

    @Basic
    @javax.persistence.Column(name = "ANIO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "{" +
                "si='" + silais + '\'' +
                ", mun='" + municipio + '\'' +
                ", uni=" + unidad.getCodigo() +
                ", fNot=" + fechaNotificacion +
                ", pat=" + patologia.getCodigo() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiveInformeDiario that = (SiveInformeDiario) o;

        if (!fechaNotificacion.equals(that.fechaNotificacion)) return false;
        if (!municipio.equals(that.municipio)) return false;
        if (!patologia.equals(that.patologia)) return false;
        if (!silais.equals(that.silais)) return false;
        if (!unidad.equals(that.unidad)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = silais.hashCode();
        result = 31 * result + municipio.hashCode();
        result = 31 * result + unidad.hashCode();
        result = 31 * result + fechaNotificacion.hashCode();
        result = 31 * result + patologia.hashCode();
        return result;
    }

    @Override
    public boolean isFieldAuditable(String fieldname) {
        return true;
    }


    @Override
    @Transient
    public String getActor() {
        return this.actor;
    }

    @Override
    public void setActor(String actor) {
        this.actor = actor;
    }


}
