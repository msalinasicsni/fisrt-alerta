package ni.gob.minsa.alerta.domain.vigilanciaIntegrada;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by souyen-ics
 */
@Entity
@Table(name = "DA_VIGILANCIA_INTEGRADA", schema = "ALERTA")
public class DaVigilanciaIntegrada {

    private String idFichaVigilancia;
    private SisPersona persona;
    private EntidadesAdtvas codSilaisAtencion;
    private Unidades codUnidadAtencion;
    private Date fechaConsulta;
    private Date fechaPrimeraConsulta;
    private String codExpediente;
    private Catalogo codClasificacion;
    private String nombreMadreTutor;
    private Catalogo codProcedencia;
    private Catalogo codCaptacion;
    private String diagnostico;
    private boolean tarjetaVacuna;
    private Date fechaInicioSintomas;
    private Catalogo codAntbUlSem;
    private Integer cantidadAntib;
    private String nombreAntibiotico;
    private Date fechaPrimDosisAntib;
    private Date fechaUltDosisAntib;
    private Integer noDosisAntib;
    private Catalogo codViaAntb;
    private boolean usoAntivirales;
    private String nombreAntiviral;
    private Date fechaPrimDosisAntiviral;
    private Date fechaUltDosisAntiviral;
    private Integer noDosisAntiviral;
    private Catalogo codResRadiologia;
    private String otroResultadoRadiologia;
    private boolean uci;
    private Integer noDiasHospitalizado;
    private boolean ventilacionAsistida;
    private String diagnostico1Egreso;
    private String diagnostico2Egreso;
    private Date fechaEgreso;
    private Catalogo codCondEgreso;
    private Catalogo codClasFCaso;
    private String agenteBacteriano;
    private String serotipificacion;
    private String agenteViral;
    private String agenteEtiologico;
    private boolean fichaCompleta;
    private boolean anulada;
    private Timestamp fechaAnulacion;
    private Timestamp fechaRegistro;
    private Usuarios usuario;


    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID_FICHA_VIGILANCIA", nullable = false, insertable = true, updatable = true, length = 32)
    public String getIdFichaVigilancia() {
        return idFichaVigilancia;
    }

    public void setIdFichaVigilancia(String idFichaVigilancia) {
        this.idFichaVigilancia = idFichaVigilancia;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="PERSONA_ID", referencedColumnName = "PERSONA_ID")
    @ForeignKey(name = "PERSONA_ID_VIG_FK")
    public SisPersona getPersona() { return persona; }

    public void setPersona(SisPersona persona) { this.persona = persona; }


    @ManyToOne(optional=false)
    @JoinColumn(name="COD_SILAIS_ATENCION", referencedColumnName = "ENTIDAD_ADTVA_ID")
    @ForeignKey(name = "COD_SILAIS_FK")
    public EntidadesAdtvas getCodSilaisAtencion() { return codSilaisAtencion;
    }

    public void setCodSilaisAtencion(EntidadesAdtvas codSilaisAtencion) { this.codSilaisAtencion = codSilaisAtencion;}

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_UNIDAD_ATENCION", referencedColumnName = "UNIDAD_ID")
    @ForeignKey(name = "COD_UNIDAD_FK")
    public Unidades getCodUnidadAtencion() { return codUnidadAtencion;
    }

    public void setCodUnidadAtencion(Unidades codUnidadAtencion) { this.codUnidadAtencion = codUnidadAtencion;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_CONSULTA", nullable = false, insertable = true, updatable = true)
    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_PRIMERA_CONSULTA", nullable = true, insertable = true, updatable = true)
    public Date getFechaPrimeraConsulta() {
        return fechaPrimeraConsulta;
    }

    public void setFechaPrimeraConsulta(Date fechaPrimeraConsulta) {
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
    }

    @Basic
    @Column(name = "COD_EXPEDIENTE", nullable = true, insertable = true, updatable = true, length = 30)

    public String getCodExpediente() {
        return codExpediente;
    }

    public void setCodExpediente(String codExpediente) {
        this.codExpediente = codExpediente;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_CLASIFICACION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CLASIF_FK")
    public Catalogo getCodClasificacion() {  return codClasificacion;  }

    public void setCodClasificacion(Catalogo codClasificacion) { this.codClasificacion = codClasificacion; }

    @Basic
    @Column(name = "NOMBRE_MADRE_TUTOR", nullable = true, insertable = true, updatable = true, length = 100)
    public String getNombreMadreTutor() {
        return nombreMadreTutor;
    }

    public void setNombreMadreTutor(String nombreMadreTutor) {
        this.nombreMadreTutor = nombreMadreTutor;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_PROCEDENCIA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_PROC_FK")
    public Catalogo getCodProcedencia() { return codProcedencia;  }

    public void setCodProcedencia(Catalogo codProcedencia) { this.codProcedencia = codProcedencia; }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_CAPTACION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CAPT_FK")
    public Catalogo getCodCaptacion() { return codCaptacion;  }

    public void setCodCaptacion(Catalogo codCaptacion) { this.codCaptacion = codCaptacion; }

    @Basic
    @Column(name = "DIAGNOSTICO", nullable = true, insertable = true, updatable = true, length = 100)
    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    @Basic
    @Column(name = "TARJETA_VACUNA", nullable = true, insertable = true, updatable = true)
    public boolean isTarjetaVacuna() {
        return tarjetaVacuna;
    }

    public void setTarjetaVacuna(boolean tarjetaVacuna) {
        this.tarjetaVacuna = tarjetaVacuna;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_INICIO_SINTOMAS", nullable = true, insertable = true, updatable = true)
    public Date getFechaInicioSintomas() {
        return fechaInicioSintomas;
    }

    public void setFechaInicioSintomas(Date fechaInicioSintomas) {
        this.fechaInicioSintomas = fechaInicioSintomas;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_ANTB_ULSEM", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_ANTB_ULSEM_FK")
    public Catalogo getCodAntbUlSem() { return codAntbUlSem;     }

    public void setCodAntbUlSem(Catalogo codAntbUlSem) { this.codAntbUlSem = codAntbUlSem;}

    @Basic
    @Column(name = "CANT_ANTIBIOTICOS", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getCantidadAntib() {
        return cantidadAntib;
    }

    public void setCantidadAntib(Integer cantidadAntib) {
        this.cantidadAntib = cantidadAntib;
    }


    @Basic
    @Column(name = "NOMBRE_ANTIBIOTICO", nullable = true, insertable = true, updatable = true, length = 100)
    public String getNombreAntibiotico() {
        return nombreAntibiotico;
    }

    public void setNombreAntibiotico(String nombreAntibiotico) {
        this.nombreAntibiotico = nombreAntibiotico;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_PRIM_DOSIS_ANTIB", nullable = true, insertable = true, updatable = true)
    public Date getFechaPrimDosisAntib() {
        return fechaPrimDosisAntib;
    }

    public void setFechaPrimDosisAntib(Date fechaPrimDosisAntib) {
        this.fechaPrimDosisAntib = fechaPrimDosisAntib;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_ULT_DOSIS_ANTIB", nullable = true, insertable = true, updatable = true)
    public Date getFechaUltDosisAntib() {
        return fechaUltDosisAntib;
    }

    public void setFechaUltDosisAntib(Date fechaUltDosisAntib) {
        this.fechaUltDosisAntib = fechaUltDosisAntib;
    }

    @Basic
    @Column(name = "NO_DOSIS_ANTIB", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoDosisAntib() {
        return noDosisAntib;
    }

    public void setNoDosisAntib(Integer noDosisAntib) {
        this.noDosisAntib = noDosisAntib;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_VIA_ANTB", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_VIA_ANTB_FK")
    public Catalogo getCodViaAntb() { return codViaAntb; }

    public void setCodViaAntb(Catalogo codViaAntb) { this.codViaAntb = codViaAntb; }

    @Basic
    @Column(name = "USO_ANTIVIRALES", nullable = true, insertable = true, updatable = true)
    public boolean isUsoAntivirales() {
        return usoAntivirales;
    }

    public void setUsoAntivirales(boolean usoAntivirales) {
        this.usoAntivirales = usoAntivirales;
    }


    @Basic
    @Column(name = "NOMBRE_ANTIVIRAL", nullable = true, insertable = true, updatable = true, length = 100)
    public String getNombreAntiviral() {
        return nombreAntiviral;
    }

    public void setNombreAntiviral(String nombreAntiviral) {
        this.nombreAntiviral = nombreAntiviral;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_PRIM_DOSIS_ANTIV", nullable = true, insertable = true, updatable = true)
    public Date getFechaPrimDosisAntiviral() {
        return fechaPrimDosisAntiviral;
    }

    public void setFechaPrimDosisAntiviral(Date fechaPrimDosisAntiviral) {
        this.fechaPrimDosisAntiviral = fechaPrimDosisAntiviral;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_ULT_DOSIS_ANTIV", nullable = true, insertable = true, updatable = true)
    public Date getFechaUltDosisAntiviral() {
        return fechaUltDosisAntiviral;
    }

    public void setFechaUltDosisAntiviral(Date fechaUltDosisAntiviral) {
        this.fechaUltDosisAntiviral = fechaUltDosisAntiviral;
    }

    @Basic
    @Column(name = "NO_DOSIS_ANTIV", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoDosisAntiviral() {
        return noDosisAntiviral;
    }

    public void setNoDosisAntiviral(Integer noDosisAntiviral) {
        this.noDosisAntiviral = noDosisAntiviral;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_RES_RADIOLOGIA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_RESRADIOLOGIA_FK")
    public Catalogo getCodResRadiologia() { return codResRadiologia; }

    public void setCodResRadiologia(Catalogo codResRadiologia) { this.codResRadiologia = codResRadiologia; }

    @Basic
    @Column(name = "OTRO_RES_RADIOLOGIA", nullable = true, insertable = true, updatable = true, length = 50)
    public String getOtroResultadoRadiologia() { return otroResultadoRadiologia;
    }

    public void setOtroResultadoRadiologia(String otroResultadoRadiologia) {
        this.otroResultadoRadiologia = otroResultadoRadiologia;
    }

    @Basic
    @Column(name = "UCI", nullable = true, insertable = true, updatable = true)
    public boolean isUci() {
        return uci;
    }

    public void setUci(boolean uci) {
        this.uci = uci;
    }

    @Basic
    @Column(name = "NO_DIAS_HOSP", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoDiasHospitalizado() {
        return noDiasHospitalizado;
    }

    public void setNoDiasHospitalizado(Integer noDiasHospitalizado) {
        this.noDiasHospitalizado = noDiasHospitalizado;
    }

    @Basic
    @Column(name = "VENTILACION_ASISTIDA", nullable = true, insertable = true, updatable = true)
    public boolean isVentilacionAsistida() {
        return ventilacionAsistida;
    }

    public void setVentilacionAsistida(boolean ventilacionAsistida) {
        this.ventilacionAsistida = ventilacionAsistida;
    }

    @Basic
    @Column(name = "DIAG1_EGRESO", nullable = true, insertable = true, updatable = true, length = 100)
    public String getDiagnostico1Egreso() {
        return diagnostico1Egreso;
    }

    public void setDiagnostico1Egreso(String diagnostico1Egreso) {
        this.diagnostico1Egreso = diagnostico1Egreso;
    }

    @Basic
    @Column(name = "DIAG2_EGRESO", nullable = true, insertable = true, updatable = true, length = 100)
    public String getDiagnostico2Egreso() {
        return diagnostico2Egreso;
    }

    public void setDiagnostico2Egreso(String diagnostico2Egreso) {
        this.diagnostico2Egreso = diagnostico2Egreso;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_EGRESO", nullable = true, insertable = true, updatable = true)
    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_COND_EGRESO", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CONDEGRESO_FK")
    public Catalogo getCodCondEgreso() { return codCondEgreso; }

    public void setCodCondEgreso(Catalogo codCondEgreso) { this.codCondEgreso = codCondEgreso; }


    @ManyToOne(optional=false)
    @JoinColumn(name="COD_CLASF_CASO", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CLASFCASO_FK")
    public Catalogo getCodClasFCaso() { return codClasFCaso; }

    public void setCodClasFCaso(Catalogo codClasFCaso) { this.codClasFCaso = codClasFCaso; }

    @Basic
    @Column(name = "AGENTE_BACTERIANO", nullable = true, insertable = true, updatable = true, length = 50)
    public String getAgenteBacteriano() {
        return agenteBacteriano;
    }

    public void setAgenteBacteriano(String agenteBacteriano) {
        this.agenteBacteriano = agenteBacteriano;
    }

    @Basic
    @Column(name = "SEROTIPIFICACION", nullable = true, insertable = true, updatable = true, length = 50)
    public String getSerotipificacion() {
        return serotipificacion;
    }

    public void setSerotipificacion(String serotipificacion) {
        this.serotipificacion = serotipificacion;
    }

    @Basic
    @Column(name = "AGENTE_VIRAL", nullable = true, insertable = true, updatable = true, length = 50)
    public String getAgenteViral() {
        return agenteViral;
    }

    public void setAgenteViral(String agenteViral) {
        this.agenteViral = agenteViral;
    }

    @Basic
    @Column(name = "AGENTE_ETIOLOGICO", nullable = true, insertable = true, updatable = true, length = 50)
    public String getAgenteEtiologico() {
        return agenteEtiologico;
    }

    public void setAgenteEtiologico(String agenteEtiologico) {
        this.agenteEtiologico = agenteEtiologico;
    }

    @Basic
    @Column(name = "FICHA_COMPLETA", nullable = true, insertable = true, updatable = true)
    public boolean isFichaCompleta() {
        return fichaCompleta;
    }

    public void setFichaCompleta(boolean fichaCompleta) {
        this.fichaCompleta = fichaCompleta;
    }

    @Basic
    @Column(name = "ANULADA", nullable = true, insertable = true, updatable = true)
    public boolean isAnulada() {
        return anulada;
    }

    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
    }

    @Column(name = "FECHA_ANULACION", nullable = true, insertable = true, updatable = true)
    public Timestamp getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Timestamp fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    @Basic
    @Column(name = "FECHA_REGISTRO", nullable = false, insertable = true, updatable = true)
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_FICHAI_FK")
    public Usuarios getUsuario() { return usuario; }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario;   }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaVigilanciaIntegrada that = (DaVigilanciaIntegrada) o;

        if (anulada != that.anulada) return false;
        if (fichaCompleta != that.fichaCompleta) return false;
        if (tarjetaVacuna != that.tarjetaVacuna) return false;
        if (uci != that.uci) return false;
        if (usoAntivirales != that.usoAntivirales) return false;
        if (ventilacionAsistida != that.ventilacionAsistida) return false;
        if (!agenteBacteriano.equals(that.agenteBacteriano)) return false;
        if (!agenteEtiologico.equals(that.agenteEtiologico)) return false;
        if (!agenteViral.equals(that.agenteViral)) return false;
        if (!cantidadAntib.equals(that.cantidadAntib)) return false;
        if (!codAntbUlSem.equals(that.codAntbUlSem)) return false;
        if (!codCaptacion.equals(that.codCaptacion)) return false;
        if (!codClasFCaso.equals(that.codClasFCaso)) return false;
        if (!codClasificacion.equals(that.codClasificacion)) return false;
        if (!codCondEgreso.equals(that.codCondEgreso)) return false;
        if (!codExpediente.equals(that.codExpediente)) return false;
        if (!codProcedencia.equals(that.codProcedencia)) return false;
        if (!codResRadiologia.equals(that.codResRadiologia)) return false;
        if (!codSilaisAtencion.equals(that.codSilaisAtencion)) return false;
        if (!codUnidadAtencion.equals(that.codUnidadAtencion)) return false;
        if (!codViaAntb.equals(that.codViaAntb)) return false;
        if (!diagnostico.equals(that.diagnostico)) return false;
        if (!diagnostico1Egreso.equals(that.diagnostico1Egreso)) return false;
        if (!diagnostico2Egreso.equals(that.diagnostico2Egreso)) return false;
        if (!fechaAnulacion.equals(that.fechaAnulacion)) return false;
        if (!fechaConsulta.equals(that.fechaConsulta)) return false;
        if (!fechaEgreso.equals(that.fechaEgreso)) return false;
        if (!fechaInicioSintomas.equals(that.fechaInicioSintomas)) return false;
        if (!fechaPrimDosisAntib.equals(that.fechaPrimDosisAntib)) return false;
        if (!fechaPrimDosisAntiviral.equals(that.fechaPrimDosisAntiviral)) return false;
        if (!fechaPrimeraConsulta.equals(that.fechaPrimeraConsulta)) return false;
        if (!fechaRegistro.equals(that.fechaRegistro)) return false;
        if (!fechaUltDosisAntib.equals(that.fechaUltDosisAntib)) return false;
        if (!fechaUltDosisAntiviral.equals(that.fechaUltDosisAntiviral)) return false;
        if (!idFichaVigilancia.equals(that.idFichaVigilancia)) return false;
        if (!noDiasHospitalizado.equals(that.noDiasHospitalizado)) return false;
        if (!noDosisAntib.equals(that.noDosisAntib)) return false;
        if (!noDosisAntiviral.equals(that.noDosisAntiviral)) return false;
        if (!nombreAntibiotico.equals(that.nombreAntibiotico)) return false;
        if (!nombreAntiviral.equals(that.nombreAntiviral)) return false;
        if (!nombreMadreTutor.equals(that.nombreMadreTutor)) return false;
        if (!otroResultadoRadiologia.equals(that.otroResultadoRadiologia)) return false;
        if (!persona.equals(that.persona)) return false;
        if (!serotipificacion.equals(that.serotipificacion)) return false;
        if (!usuario.equals(that.usuario)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idFichaVigilancia.hashCode();
        result = 31 * result + persona.hashCode();
        result = 31 * result + codSilaisAtencion.hashCode();
        result = 31 * result + codUnidadAtencion.hashCode();
        result = 31 * result + fechaConsulta.hashCode();
        result = 31 * result + fechaPrimeraConsulta.hashCode();
        result = 31 * result + codExpediente.hashCode();
        result = 31 * result + codClasificacion.hashCode();
        result = 31 * result + nombreMadreTutor.hashCode();
        result = 31 * result + codProcedencia.hashCode();
        result = 31 * result + codCaptacion.hashCode();
        result = 31 * result + diagnostico.hashCode();
        result = 31 * result + (tarjetaVacuna ? 1 : 0);
        result = 31 * result + fechaInicioSintomas.hashCode();
        result = 31 * result + codAntbUlSem.hashCode();
        result = 31 * result + cantidadAntib.hashCode();
        result = 31 * result + nombreAntibiotico.hashCode();
        result = 31 * result + fechaPrimDosisAntib.hashCode();
        result = 31 * result + fechaUltDosisAntib.hashCode();
        result = 31 * result + noDosisAntib.hashCode();
        result = 31 * result + codViaAntb.hashCode();
        result = 31 * result + (usoAntivirales ? 1 : 0);
        result = 31 * result + nombreAntiviral.hashCode();
        result = 31 * result + fechaPrimDosisAntiviral.hashCode();
        result = 31 * result + fechaUltDosisAntiviral.hashCode();
        result = 31 * result + noDosisAntiviral.hashCode();
        result = 31 * result + codResRadiologia.hashCode();
        result = 31 * result + otroResultadoRadiologia.hashCode();
        result = 31 * result + (uci ? 1 : 0);
        result = 31 * result + noDiasHospitalizado.hashCode();
        result = 31 * result + (ventilacionAsistida ? 1 : 0);
        result = 31 * result + diagnostico1Egreso.hashCode();
        result = 31 * result + diagnostico2Egreso.hashCode();
        result = 31 * result + fechaEgreso.hashCode();
        result = 31 * result + codCondEgreso.hashCode();
        result = 31 * result + codClasFCaso.hashCode();
        result = 31 * result + agenteBacteriano.hashCode();
        result = 31 * result + serotipificacion.hashCode();
        result = 31 * result + agenteViral.hashCode();
        result = 31 * result + agenteEtiologico.hashCode();
        result = 31 * result + (fichaCompleta ? 1 : 0);
        result = 31 * result + (anulada ? 1 : 0);
        result = 31 * result + fechaAnulacion.hashCode();
        result = 31 * result + fechaRegistro.hashCode();
        result = 31 * result + usuario.hashCode();
        return result;
    }
}
