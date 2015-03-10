package ni.gob.minsa.alerta.domain.muestra;

import java.util.Date;

/**
 * Created by FIRSTICT on 11/21/2014.
 */
public class FiltroMx {
    String nombreApellido;
    Date fechaInicioTomaMx;
    Date fechaFinTomaMx;
    String codSilais;
    String codUnidadSalud;
    String codTipoMx;
    String codTipoSolicitud;
    String nombreSolicitud;

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public Date getFechaInicioTomaMx() {
        return fechaInicioTomaMx;
    }

    public void setFechaInicioTomaMx(Date fechaInicioTomaMx) {
        this.fechaInicioTomaMx = fechaInicioTomaMx;
    }

    public Date getFechaFinTomaMx() {
        return fechaFinTomaMx;
    }

    public void setFechaFinTomaMx(Date fechaFinTomaMx) {
        this.fechaFinTomaMx = fechaFinTomaMx;
    }

    public String getCodSilais() {
        return codSilais;
    }

    public void setCodSilais(String codSilais) {
        this.codSilais = codSilais;
    }

    public String getCodUnidadSalud() {
        return codUnidadSalud;
    }

    public void setCodUnidadSalud(String codUnidadSalud) {
        this.codUnidadSalud = codUnidadSalud;
    }

    public String getCodTipoMx() {
        return codTipoMx;
    }

    public void setCodTipoMx(String codTipoMx) {
        this.codTipoMx = codTipoMx;
    }

    public String getCodTipoSolicitud() { return codTipoSolicitud; }

    public void setCodTipoSolicitud(String codTipoSolicitud) { this.codTipoSolicitud = codTipoSolicitud; }

    public String getNombreSolicitud() { return nombreSolicitud; }

    public void setNombreSolicitud(String nombreSolicitud) { this.nombreSolicitud = nombreSolicitud; }
}
