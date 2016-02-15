package ni.gob.minsa.alerta.domain.sive;

import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by FIRSTICT on 2/12/2016.
 * V1.0
 */
@Entity
@javax.persistence.Table(name = "sive_informe_diario", schema = "alerta")
public class SiveInformeDiario {

    @EmbeddedId
    private SiveInformeDiarioId id;
    private Integer semana;
    private Integer g01f;
    private Integer g01m;
    private Integer g02f;
    private Integer g02m;
    private Integer g03f;
    private Integer g03m;
    private Integer g04f;
    private Integer g04m;
    private Integer g05f;
    private Integer g05m;
    private Integer g06f;
    private Integer g06m;
    private Integer g07f;
    private Integer g07m;
    private Integer g08f;
    private Integer g08m;
    private Integer g09f;
    private Integer g09m;
    private Integer g10f;
    private Integer g10m;
    private Integer g11f;
    private Integer g11m;
    private Integer g12f;
    private Integer g12m;
    private Integer g13f;
    private Integer g13m;
    private Integer descf;
    private Integer descm;
    private Integer totalf;
    private Integer totalm;
    private int bloqueado = 0;
    private Timestamp fecharegistro;
    private String usuarioregistro;
    private Timestamp fechamodificacion;
    private String usuariomodificacion;
    private Integer numOrden;
    private Integer codSisniven;
    private Integer anio;

    public SiveInformeDiarioId getId() {
        return id;
    }

    public void setId(SiveInformeDiarioId id) {
        this.id = id;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "MUNICIPIO", referencedColumnName = "DIVISIONPOLITICA_ID")
    @ForeignKey(name = "NOTIFICACION_MUNICIPIO_FK")
    private Divisionpolitica municipio;

    @ManyToOne(optional = true)
    @JoinColumn(name = "UNIDAD_SALUD", referencedColumnName = "UNIDAD_ID")
    @ForeignKey(name = "NOTIFICACION_UNIDAD_FK")
    private Unidades unidad;

    @ManyToOne(optional = true)
    @JoinColumn(name = "COD_PATOLOGIA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "NOTIFICACION_PATOLOGIA_FK")
    private SivePatologias patologia;

    @Basic
    @javax.persistence.Column(name = "SEMANA_EPI", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getSemana() {
        return semana;
    }

    public void setSemana(Integer semanaEpi) {
        this.semana = semanaEpi;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO1_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG01f() {
        return g01f;
    }

    public void setG01f(Integer grupo1F) {
        this.g01f = grupo1F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO1_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG01m() {
        return g01m;
    }

    public void setG01m(Integer grupo1M) {
        this.g01m = grupo1M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO2_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG02f() {
        return g02f;
    }

    public void setG02f(Integer grupo2F) {
        this.g02f = grupo2F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO2_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG02m() {
        return g02m;
    }

    public void setG02m(Integer grupo2M) {
        this.g02m = grupo2M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO3_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG03f() {
        return g03f;
    }

    public void setG03f(Integer grupo3F) {
        this.g03f = grupo3F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO3_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG03m() {
        return g03m;
    }

    public void setG03m(Integer grupo3M) {
        this.g03m = grupo3M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO4_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG04f() {
        return g04f;
    }

    public void setG04f(Integer grupo4F) {
        this.g04f = grupo4F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO4_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG04m() {
        return g04m;
    }

    public void setG04m(Integer grupo4M) {
        this.g04m = grupo4M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO5_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG05f() {
        return g05f;
    }

    public void setG05f(Integer grupo5F) {
        this.g05f = grupo5F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO5_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG05m() {
        return g05m;
    }

    public void setG05m(Integer grupo5M) {
        this.g05m = grupo5M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO6_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG06f() {
        return g06f;
    }

    public void setG06f(Integer grupo6F) {
        this.g06f = grupo6F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO6_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG06m() {
        return g06m;
    }

    public void setG06m(Integer grupo6M) {
        this.g06m = grupo6M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO7_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG07f() {
        return g07f;
    }

    public void setG07f(Integer grupo7F) {
        this.g07f = grupo7F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO7_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG07m() {
        return g07m;
    }

    public void setG07m(Integer grupo7M) {
        this.g07m = grupo7M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO8_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG08f() {
        return g08f;
    }

    public void setG08f(Integer grupo8F) {
        this.g08f = grupo8F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO8_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG08m() {
        return g08m;
    }

    public void setG08m(Integer grupo8M) {
        this.g08m = grupo8M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO9_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG09f() {
        return g09f;
    }

    public void setG09f(Integer grupo9F) {
        this.g09f = grupo9F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO9_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG09m() {
        return g09m;
    }

    public void setG09m(Integer grupo9M) {
        this.g09m = grupo9M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO10_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG10f() {
        return g10f;
    }

    public void setG10f(Integer grupo10F) {
        this.g10f = grupo10F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO10_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG10m() {
        return g10m;
    }

    public void setG10m(Integer grupo10M) {
        this.g10m = grupo10M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO11_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG11f() {
        return g11f;
    }

    public void setG11f(Integer grupo11F) {
        this.g11f = grupo11F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO11_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG11m() {
        return g11m;
    }

    public void setG11m(Integer grupo11M) {
        this.g11m = grupo11M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO12_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG12f() {
        return g12f;
    }

    public void setG12f(Integer grupo12F) {
        this.g12f = grupo12F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO12_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG12m() {
        return g12m;
    }

    public void setG12m(Integer grupo12M) {
        this.g12m = grupo12M;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO13_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG13f() {
        return g13f;
    }

    public void setG13f(Integer grupo13F) {
        this.g13f = grupo13F;
    }

    @Basic
    @javax.persistence.Column(name = "GRUPO13_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getG13m() {
        return g13m;
    }

    public void setG13m(Integer grupo13M) {
        this.g13m = grupo13M;
    }

    @Basic
    @javax.persistence.Column(name = "DESC_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescf() {
        return descf;
    }

    public void setDescf(Integer descF) {
        this.descf = descF;
    }

    @Basic
    @javax.persistence.Column(name = "DESC_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescm() {
        return descm;
    }

    public void setDescm(Integer descM) {
        this.descm = descM;
    }

    @Basic
    @javax.persistence.Column(name = "TOTAL_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalf() {
        return totalf;
    }

    public void setTotalf(Integer totalF) {
        this.totalf = totalF;
    }

    @Basic
    @javax.persistence.Column(name = "TOTAL_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalm() {
        return totalm;
    }

    public void setTotalm(Integer totalM) {
        this.totalm = totalM;
    }

    @Basic
    @javax.persistence.Column(name = "BLOQUEADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public int getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(int bloqueado) {
        this.bloqueado = bloqueado;
    }

    @Basic
    @javax.persistence.Column(name = "FECHAREGISTRO", nullable = true, insertable = true, updatable = true)
    public Timestamp getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Timestamp fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    @Basic
    @javax.persistence.Column(name = "USUARIOREGISTRO", nullable = true, insertable = true, updatable = true, length = 20)
    public String getUsuarioregistro() {
        return usuarioregistro;
    }

    public void setUsuarioregistro(String usuarioregistro) {
        this.usuarioregistro = usuarioregistro;
    }

    @Basic
    @javax.persistence.Column(name = "FECHAMODIFICACION", nullable = true, insertable = true, updatable = true)
    public Timestamp getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Timestamp fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    @Basic
    @javax.persistence.Column(name = "USUARIOMODIFICACION", nullable = true, insertable = true, updatable = true, length = 20)
    public String getUsuariomodificacion() {
        return usuariomodificacion;
    }

    public void setUsuariomodificacion(String usuariomodificacion) {
        this.usuariomodificacion = usuariomodificacion;
    }

    @Basic
    @javax.persistence.Column(name = "NUMERO_ORDEN", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(Integer numeroOrden) {
        this.numOrden = numeroOrden;
    }

    @Basic
    @javax.persistence.Column(name = "COD_SISNIVEN", nullable = true, insertable = true, updatable = true, precision = -127)
    public Integer getCodSisniven() {
        return codSisniven;
    }

    public void setCodSisniven(Integer codSisniven) {
        this.codSisniven = codSisniven;
    }

    @Basic
    @javax.persistence.Column(name = "ANIO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Divisionpolitica getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Divisionpolitica municipio) {
        this.municipio = municipio;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public SivePatologias getPatologia() {
        return patologia;
    }

    public void setPatologia(SivePatologias patologia) {
        this.patologia = patologia;
    }
}
