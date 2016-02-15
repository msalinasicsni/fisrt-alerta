package ni.gob.minsa.alerta.domain.sive;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by FIRSTICT on 2/15/2016.
 * V1.0
 */
@Entity
@javax.persistence.Table(name = "sive_informe_diario", schema = "sive")
public class SiveInformeDiario {
    private String silais;

    @EmbeddedId SiveInformeDiarioId id;

    @Basic
    @javax.persistence.Column(name = "SILAIS", nullable = true, insertable = true, updatable = true, length = 2)
    public String getSilais() {
        return silais;
    }

    public void setSilais(String silais) {
        this.silais = silais;
    }

    private String municipio;

    @Basic
    @javax.persistence.Column(name = "MUNICIPIO", nullable = true, insertable = true, updatable = true, length = 4)
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    private BigDecimal unidadSalud;

    @Basic
    @javax.persistence.Column(name = "UNIDAD_SALUD", nullable = true, insertable = true, updatable = true, precision = -127)
    public BigDecimal getUnidadSalud() {
        return unidadSalud;
    }

    public void setUnidadSalud(BigDecimal unidadSalud) {
        this.unidadSalud = unidadSalud;
    }

    private Timestamp fecha;

    @Basic
    @javax.persistence.Column(name = "FECHA", nullable = true, insertable = true, updatable = true)
    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    private BigInteger semanaEpi;

    @Basic
    @javax.persistence.Column(name = "SEMANA_EPI", nullable = true, insertable = true, updatable = true, precision = 0)
    public BigInteger getSemanaEpi() {
        return semanaEpi;
    }

    public void setSemanaEpi(BigInteger semanaEpi) {
        this.semanaEpi = semanaEpi;
    }

    private String codPatologia;

    @Basic
    @javax.persistence.Column(name = "COD_PATOLOGIA", nullable = true, insertable = true, updatable = true, length = 6)
    public String getCodPatologia() {
        return codPatologia;
    }

    public void setCodPatologia(String codPatologia) {
        this.codPatologia = codPatologia;
    }

    private Integer grupo1F;

    @Basic
    @javax.persistence.Column(name = "GRUPO1_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo1F() {
        return grupo1F;
    }

    public void setGrupo1F(Integer grupo1F) {
        this.grupo1F = grupo1F;
    }

    private Integer grupo1M;

    @Basic
    @javax.persistence.Column(name = "GRUPO1_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo1M() {
        return grupo1M;
    }

    public void setGrupo1M(Integer grupo1M) {
        this.grupo1M = grupo1M;
    }

    private Integer grupo2F;

    @Basic
    @javax.persistence.Column(name = "GRUPO2_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo2F() {
        return grupo2F;
    }

    public void setGrupo2F(Integer grupo2F) {
        this.grupo2F = grupo2F;
    }

    private Integer grupo2M;

    @Basic
    @javax.persistence.Column(name = "GRUPO2_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo2M() {
        return grupo2M;
    }

    public void setGrupo2M(Integer grupo2M) {
        this.grupo2M = grupo2M;
    }

    private Integer grupo3F;

    @Basic
    @javax.persistence.Column(name = "GRUPO3_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo3F() {
        return grupo3F;
    }

    public void setGrupo3F(Integer grupo3F) {
        this.grupo3F = grupo3F;
    }

    private Integer grupo3M;

    @Basic
    @javax.persistence.Column(name = "GRUPO3_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo3M() {
        return grupo3M;
    }

    public void setGrupo3M(Integer grupo3M) {
        this.grupo3M = grupo3M;
    }

    private Integer grupo4F;

    @Basic
    @javax.persistence.Column(name = "GRUPO4_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo4F() {
        return grupo4F;
    }

    public void setGrupo4F(Integer grupo4F) {
        this.grupo4F = grupo4F;
    }

    private Integer grupo4M;

    @Basic
    @javax.persistence.Column(name = "GRUPO4_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo4M() {
        return grupo4M;
    }

    public void setGrupo4M(Integer grupo4M) {
        this.grupo4M = grupo4M;
    }

    private Integer grupo5F;

    @Basic
    @javax.persistence.Column(name = "GRUPO5_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo5F() {
        return grupo5F;
    }

    public void setGrupo5F(Integer grupo5F) {
        this.grupo5F = grupo5F;
    }

    private Integer grupo5M;

    @Basic
    @javax.persistence.Column(name = "GRUPO5_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo5M() {
        return grupo5M;
    }

    public void setGrupo5M(Integer grupo5M) {
        this.grupo5M = grupo5M;
    }

    private Integer grupo6F;

    @Basic
    @javax.persistence.Column(name = "GRUPO6_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo6F() {
        return grupo6F;
    }

    public void setGrupo6F(Integer grupo6F) {
        this.grupo6F = grupo6F;
    }

    private Integer grupo6M;

    @Basic
    @javax.persistence.Column(name = "GRUPO6_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo6M() {
        return grupo6M;
    }

    public void setGrupo6M(Integer grupo6M) {
        this.grupo6M = grupo6M;
    }

    private Integer grupo7F;

    @Basic
    @javax.persistence.Column(name = "GRUPO7_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo7F() {
        return grupo7F;
    }

    public void setGrupo7F(Integer grupo7F) {
        this.grupo7F = grupo7F;
    }

    private Integer grupo7M;

    @Basic
    @javax.persistence.Column(name = "GRUPO7_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo7M() {
        return grupo7M;
    }

    public void setGrupo7M(Integer grupo7M) {
        this.grupo7M = grupo7M;
    }

    private Integer grupo8F;

    @Basic
    @javax.persistence.Column(name = "GRUPO8_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo8F() {
        return grupo8F;
    }

    public void setGrupo8F(Integer grupo8F) {
        this.grupo8F = grupo8F;
    }

    private Integer grupo8M;

    @Basic
    @javax.persistence.Column(name = "GRUPO8_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo8M() {
        return grupo8M;
    }

    public void setGrupo8M(Integer grupo8M) {
        this.grupo8M = grupo8M;
    }

    private Integer grupo9F;

    @Basic
    @javax.persistence.Column(name = "GRUPO9_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo9F() {
        return grupo9F;
    }

    public void setGrupo9F(Integer grupo9F) {
        this.grupo9F = grupo9F;
    }

    private Integer grupo9M;

    @Basic
    @javax.persistence.Column(name = "GRUPO9_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo9M() {
        return grupo9M;
    }

    public void setGrupo9M(Integer grupo9M) {
        this.grupo9M = grupo9M;
    }

    private Integer grupo10F;

    @Basic
    @javax.persistence.Column(name = "GRUPO10_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo10F() {
        return grupo10F;
    }

    public void setGrupo10F(Integer grupo10F) {
        this.grupo10F = grupo10F;
    }

    private Integer grupo10M;

    @Basic
    @javax.persistence.Column(name = "GRUPO10_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo10M() {
        return grupo10M;
    }

    public void setGrupo10M(Integer grupo10M) {
        this.grupo10M = grupo10M;
    }

    private Integer grupo11F;

    @Basic
    @javax.persistence.Column(name = "GRUPO11_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo11F() {
        return grupo11F;
    }

    public void setGrupo11F(Integer grupo11F) {
        this.grupo11F = grupo11F;
    }

    private Integer grupo11M;

    @Basic
    @javax.persistence.Column(name = "GRUPO11_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo11M() {
        return grupo11M;
    }

    public void setGrupo11M(Integer grupo11M) {
        this.grupo11M = grupo11M;
    }

    private Integer grupo12F;

    @Basic
    @javax.persistence.Column(name = "GRUPO12_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo12F() {
        return grupo12F;
    }

    public void setGrupo12F(Integer grupo12F) {
        this.grupo12F = grupo12F;
    }

    private Integer grupo12M;

    @Basic
    @javax.persistence.Column(name = "GRUPO12_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo12M() {
        return grupo12M;
    }

    public void setGrupo12M(Integer grupo12M) {
        this.grupo12M = grupo12M;
    }

    private Integer grupo13F;

    @Basic
    @javax.persistence.Column(name = "GRUPO13_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo13F() {
        return grupo13F;
    }

    public void setGrupo13F(Integer grupo13F) {
        this.grupo13F = grupo13F;
    }

    private Integer grupo13M;

    @Basic
    @javax.persistence.Column(name = "GRUPO13_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getGrupo13M() {
        return grupo13M;
    }

    public void setGrupo13M(Integer grupo13M) {
        this.grupo13M = grupo13M;
    }

    private Integer descF;

    @Basic
    @javax.persistence.Column(name = "DESC_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescF() {
        return descF;
    }

    public void setDescF(Integer descF) {
        this.descF = descF;
    }

    private Integer descM;

    @Basic
    @javax.persistence.Column(name = "DESC_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getDescM() {
        return descM;
    }

    public void setDescM(Integer descM) {
        this.descM = descM;
    }

    private Integer totalF;

    @Basic
    @javax.persistence.Column(name = "TOTAL_F", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalF() {
        return totalF;
    }

    public void setTotalF(Integer totalF) {
        this.totalF = totalF;
    }

    private Integer totalM;

    @Basic
    @javax.persistence.Column(name = "TOTAL_M", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getTotalM() {
        return totalM;
    }

    public void setTotalM(Integer totalM) {
        this.totalM = totalM;
    }

    private BigInteger bloqueado;

    @Basic
    @javax.persistence.Column(name = "BLOQUEADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public BigInteger getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(BigInteger bloqueado) {
        this.bloqueado = bloqueado;
    }

    private Timestamp fecharegistro;

    @Basic
    @javax.persistence.Column(name = "FECHAREGISTRO", nullable = true, insertable = true, updatable = true)
    public Timestamp getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Timestamp fecharegistro) {
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

    private Timestamp fechamodificacion;

    @Basic
    @javax.persistence.Column(name = "FECHAMODIFICACION", nullable = true, insertable = true, updatable = true)
    public Timestamp getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Timestamp fechamodificacion) {
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

    private BigDecimal codSisniven;

    @Basic
    @javax.persistence.Column(name = "COD_SISNIVEN", nullable = true, insertable = true, updatable = true, precision = -127)
    public BigDecimal getCodSisniven() {
        return codSisniven;
    }

    public void setCodSisniven(BigDecimal codSisniven) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiveInformeDiario that = (SiveInformeDiario) o;

        if (anio != null ? !anio.equals(that.anio) : that.anio != null) return false;
        if (bloqueado != null ? !bloqueado.equals(that.bloqueado) : that.bloqueado != null) return false;
        if (codPatologia != null ? !codPatologia.equals(that.codPatologia) : that.codPatologia != null) return false;
        if (codSisniven != null ? !codSisniven.equals(that.codSisniven) : that.codSisniven != null) return false;
        if (descF != null ? !descF.equals(that.descF) : that.descF != null) return false;
        if (descM != null ? !descM.equals(that.descM) : that.descM != null) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (fechamodificacion != null ? !fechamodificacion.equals(that.fechamodificacion) : that.fechamodificacion != null)
            return false;
        if (fecharegistro != null ? !fecharegistro.equals(that.fecharegistro) : that.fecharegistro != null)
            return false;
        if (grupo10F != null ? !grupo10F.equals(that.grupo10F) : that.grupo10F != null) return false;
        if (grupo10M != null ? !grupo10M.equals(that.grupo10M) : that.grupo10M != null) return false;
        if (grupo11F != null ? !grupo11F.equals(that.grupo11F) : that.grupo11F != null) return false;
        if (grupo11M != null ? !grupo11M.equals(that.grupo11M) : that.grupo11M != null) return false;
        if (grupo12F != null ? !grupo12F.equals(that.grupo12F) : that.grupo12F != null) return false;
        if (grupo12M != null ? !grupo12M.equals(that.grupo12M) : that.grupo12M != null) return false;
        if (grupo13F != null ? !grupo13F.equals(that.grupo13F) : that.grupo13F != null) return false;
        if (grupo13M != null ? !grupo13M.equals(that.grupo13M) : that.grupo13M != null) return false;
        if (grupo1F != null ? !grupo1F.equals(that.grupo1F) : that.grupo1F != null) return false;
        if (grupo1M != null ? !grupo1M.equals(that.grupo1M) : that.grupo1M != null) return false;
        if (grupo2F != null ? !grupo2F.equals(that.grupo2F) : that.grupo2F != null) return false;
        if (grupo2M != null ? !grupo2M.equals(that.grupo2M) : that.grupo2M != null) return false;
        if (grupo3F != null ? !grupo3F.equals(that.grupo3F) : that.grupo3F != null) return false;
        if (grupo3M != null ? !grupo3M.equals(that.grupo3M) : that.grupo3M != null) return false;
        if (grupo4F != null ? !grupo4F.equals(that.grupo4F) : that.grupo4F != null) return false;
        if (grupo4M != null ? !grupo4M.equals(that.grupo4M) : that.grupo4M != null) return false;
        if (grupo5F != null ? !grupo5F.equals(that.grupo5F) : that.grupo5F != null) return false;
        if (grupo5M != null ? !grupo5M.equals(that.grupo5M) : that.grupo5M != null) return false;
        if (grupo6F != null ? !grupo6F.equals(that.grupo6F) : that.grupo6F != null) return false;
        if (grupo6M != null ? !grupo6M.equals(that.grupo6M) : that.grupo6M != null) return false;
        if (grupo7F != null ? !grupo7F.equals(that.grupo7F) : that.grupo7F != null) return false;
        if (grupo7M != null ? !grupo7M.equals(that.grupo7M) : that.grupo7M != null) return false;
        if (grupo8F != null ? !grupo8F.equals(that.grupo8F) : that.grupo8F != null) return false;
        if (grupo8M != null ? !grupo8M.equals(that.grupo8M) : that.grupo8M != null) return false;
        if (grupo9F != null ? !grupo9F.equals(that.grupo9F) : that.grupo9F != null) return false;
        if (grupo9M != null ? !grupo9M.equals(that.grupo9M) : that.grupo9M != null) return false;
        if (municipio != null ? !municipio.equals(that.municipio) : that.municipio != null) return false;
        if (numeroOrden != null ? !numeroOrden.equals(that.numeroOrden) : that.numeroOrden != null) return false;
        if (semanaEpi != null ? !semanaEpi.equals(that.semanaEpi) : that.semanaEpi != null) return false;
        if (silais != null ? !silais.equals(that.silais) : that.silais != null) return false;
        if (totalF != null ? !totalF.equals(that.totalF) : that.totalF != null) return false;
        if (totalM != null ? !totalM.equals(that.totalM) : that.totalM != null) return false;
        if (unidadSalud != null ? !unidadSalud.equals(that.unidadSalud) : that.unidadSalud != null) return false;
        if (usuariomodificacion != null ? !usuariomodificacion.equals(that.usuariomodificacion) : that.usuariomodificacion != null)
            return false;
        if (usuarioregistro != null ? !usuarioregistro.equals(that.usuarioregistro) : that.usuarioregistro != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = silais != null ? silais.hashCode() : 0;
        result = 31 * result + (municipio != null ? municipio.hashCode() : 0);
        result = 31 * result + (unidadSalud != null ? unidadSalud.hashCode() : 0);
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (semanaEpi != null ? semanaEpi.hashCode() : 0);
        result = 31 * result + (codPatologia != null ? codPatologia.hashCode() : 0);
        result = 31 * result + (grupo1F != null ? grupo1F.hashCode() : 0);
        result = 31 * result + (grupo1M != null ? grupo1M.hashCode() : 0);
        result = 31 * result + (grupo2F != null ? grupo2F.hashCode() : 0);
        result = 31 * result + (grupo2M != null ? grupo2M.hashCode() : 0);
        result = 31 * result + (grupo3F != null ? grupo3F.hashCode() : 0);
        result = 31 * result + (grupo3M != null ? grupo3M.hashCode() : 0);
        result = 31 * result + (grupo4F != null ? grupo4F.hashCode() : 0);
        result = 31 * result + (grupo4M != null ? grupo4M.hashCode() : 0);
        result = 31 * result + (grupo5F != null ? grupo5F.hashCode() : 0);
        result = 31 * result + (grupo5M != null ? grupo5M.hashCode() : 0);
        result = 31 * result + (grupo6F != null ? grupo6F.hashCode() : 0);
        result = 31 * result + (grupo6M != null ? grupo6M.hashCode() : 0);
        result = 31 * result + (grupo7F != null ? grupo7F.hashCode() : 0);
        result = 31 * result + (grupo7M != null ? grupo7M.hashCode() : 0);
        result = 31 * result + (grupo8F != null ? grupo8F.hashCode() : 0);
        result = 31 * result + (grupo8M != null ? grupo8M.hashCode() : 0);
        result = 31 * result + (grupo9F != null ? grupo9F.hashCode() : 0);
        result = 31 * result + (grupo9M != null ? grupo9M.hashCode() : 0);
        result = 31 * result + (grupo10F != null ? grupo10F.hashCode() : 0);
        result = 31 * result + (grupo10M != null ? grupo10M.hashCode() : 0);
        result = 31 * result + (grupo11F != null ? grupo11F.hashCode() : 0);
        result = 31 * result + (grupo11M != null ? grupo11M.hashCode() : 0);
        result = 31 * result + (grupo12F != null ? grupo12F.hashCode() : 0);
        result = 31 * result + (grupo12M != null ? grupo12M.hashCode() : 0);
        result = 31 * result + (grupo13F != null ? grupo13F.hashCode() : 0);
        result = 31 * result + (grupo13M != null ? grupo13M.hashCode() : 0);
        result = 31 * result + (descF != null ? descF.hashCode() : 0);
        result = 31 * result + (descM != null ? descM.hashCode() : 0);
        result = 31 * result + (totalF != null ? totalF.hashCode() : 0);
        result = 31 * result + (totalM != null ? totalM.hashCode() : 0);
        result = 31 * result + (bloqueado != null ? bloqueado.hashCode() : 0);
        result = 31 * result + (fecharegistro != null ? fecharegistro.hashCode() : 0);
        result = 31 * result + (usuarioregistro != null ? usuarioregistro.hashCode() : 0);
        result = 31 * result + (fechamodificacion != null ? fechamodificacion.hashCode() : 0);
        result = 31 * result + (usuariomodificacion != null ? usuariomodificacion.hashCode() : 0);
        result = 31 * result + (numeroOrden != null ? numeroOrden.hashCode() : 0);
        result = 31 * result + (codSisniven != null ? codSisniven.hashCode() : 0);
        result = 31 * result + (anio != null ? anio.hashCode() : 0);
        return result;
    }
}
