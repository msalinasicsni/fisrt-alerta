package ni.gob.minsa.alerta.utilities;

/**
 * Created by FIRSTICT on 9/30/2015.
 * V1.0
 */
public class FiltrosReporte {
    String codArea;
    Long codSilais;
    Long codDepartamento;
    Long codMunicipio;
    Long codUnidad;
    String semInicial;
    String semFinal;
    String anioInicial;
    String anioFinal;
    String tipoNotificacion;
    Integer factor;

    public String getCodArea() {
        return codArea;
    }

    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }

    public Long getCodSilais() {
        return codSilais;
    }

    public void setCodSilais(Long codSilais) {
        this.codSilais = codSilais;
    }

    public Long getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(Long codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public Long getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(Long codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public Long getCodUnidad() {
        return codUnidad;
    }

    public void setCodUnidad(Long codUnidad) {
        this.codUnidad = codUnidad;
    }

    public String getSemInicial() {
        return semInicial;
    }

    public void setSemInicial(String semInicial) {
        this.semInicial = semInicial;
    }

    public String getSemFinal() {
        return semFinal;
    }

    public void setSemFinal(String semFinal) {
        this.semFinal = semFinal;
    }

    public String getAnioInicial() {
        return anioInicial;
    }

    public void setAnioInicial(String anioInicial) {
        this.anioInicial = anioInicial;
    }

    public String getAnioFinal() {
        return anioFinal;
    }

    public void setAnioFinal(String anioFinal) {
        this.anioFinal = anioFinal;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }
}
