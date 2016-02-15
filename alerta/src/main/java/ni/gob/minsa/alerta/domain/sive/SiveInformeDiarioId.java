package ni.gob.minsa.alerta.domain.sive;

import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by FIRSTICT on 2/15/2016.
 * V1.0
 */
@Embeddable
public class SiveInformeDiarioId implements Serializable {

    private String silais;
    private String municipio;
    private long unidadSalud;
    private Timestamp fecha;
    private String codPatologia;

    @Basic
    @javax.persistence.Column(name = "SILAIS", nullable = true, insertable = true, updatable = true, length = 2)
    public String getSilais() {
        return silais;
    }

    public void setSilais(String silais) {
        this.silais = silais;
    }

    @Basic
    @javax.persistence.Column(name = "MUNICIPIO", nullable = true, insertable = true, updatable = true)
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Basic
    @javax.persistence.Column(name = "UNIDAD_SALUD", nullable = true, insertable = true, updatable = true, precision = -127)
    public long getUnidadSalud() {
        return unidadSalud;
    }

    public void setUnidadSalud(long unidadSalud) {
        this.unidadSalud = unidadSalud;
    }

    @Basic
    @javax.persistence.Column(name = "FECHA", nullable = true, insertable = true, updatable = true)
    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    @Basic
    @javax.persistence.Column(name = "COD_PATOLOGIA", nullable = true, insertable = true, updatable = true, length = 6)
    public String getCodPatologia() {
        return codPatologia;
    }

    public void setCodPatologia(String codPatologia) {
        this.codPatologia = codPatologia;
    }


}
