package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.irag.CondicionEgreso;
import ni.gob.minsa.alerta.domain.irag.Respuesta;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ficha_rotavirus", schema = "alerta")
public class FichaRotavirus implements Serializable, Auditable {
    private static final long serialVersionUID = 1L;

    private DaNotificacion daNotificacion;
    String numExpediente;
    String codigo;
    //datos generales
    String nombreTutorAcompana;
    String telefonoTutor;
    Boolean enGuarderia;
    String nombreGuarderia;
    //datos clínicos
    Date fechaInicioDiarrea;
    Integer noEvacuaciones24Hrs;
    Respuesta fiebre;
    Respuesta vomito;
    Integer noVomito24Hrs;
    Date fechaInicioVomito;
    CaracteristaHeces caracteristaHeces;
    String otraCaracteristicaHeces;
    GradoDeshidratacion gradoDeshidratacion;
    Integer diasHospitalizacion;
    Date fechaAlta;
    //tratamiento
    Respuesta usoAntibioticoPrevio;
    Boolean planB;
    Boolean planC;
    Respuesta antibioticoHospital;
    String cualAntibiotico;
    Boolean UCI;
    Integer diasUCI;
    Boolean altaUCIDiarrea;
    Date fechaTerminoDiarrea;
    Boolean ignoradoFechaTD;
    //historia vacunacion
    Respuesta vacunado;
    RegistroVacuna registroVacuna;
    TipoVacunaRotavirus tipoVacunaRotavirus;
    Boolean dosi1;
    Date fechaAplicacionDosis1;
    Boolean dosi2;
    Date fechaAplicacionDosis2;
    Boolean dosi3;
    Date fechaAplicacionDosis3;
    //datos laboratorio
    Respuesta tomoMuestraHeces;
    //Clasificación final
    ClasificacionFinalRotavirus clasificacionFinal;
    CondicionEgreso condicionEgreso;
    //Responsable Información
    String nombreLlenaFicha;
    String nombreTomoMx;
    String epidemiologo;

    SalaRotaVirus sala;
    Date fechaIngreso;

    private String actor;
    private String id;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID_FICHA", nullable = false, insertable = true, updatable = true, length = 36)
    public String getId(){return this.id;}

    public void setId(String id){this.id = id;}

    @OneToOne(targetEntity=DaNotificacion.class)
    @JoinColumn(name = "ID_NOTIFICACION", referencedColumnName = "ID_NOTIFICACION")
    public DaNotificacion getDaNotificacion() {
        return daNotificacion;
    }

    public void setDaNotificacion(DaNotificacion daNotificacion) {
        this.daNotificacion = daNotificacion;
    }

    @Basic
    @Column(name = "NOMBRE_TUTOR", nullable = true, length = 150)
    public String getNombreTutorAcompana() {
        return nombreTutorAcompana;
    }

    public void setNombreTutorAcompana(String nombreAcompana) {
        this.nombreTutorAcompana = nombreAcompana;
    }

    @Basic
    @Column(name = "EN_GUARDERIA", nullable = true)
    public Boolean getEnGuarderia() {
        return enGuarderia;
    }

    public void setEnGuarderia(Boolean enGuarderia) {
        this.enGuarderia = enGuarderia;
    }

    @Basic
    @Column(name = "NOMBRE_GUARDERIA", nullable = true, length = 150)
    public String getNombreGuarderia() {
        return nombreGuarderia;
    }

    public void setNombreGuarderia(String nombreGuarderia) {
        this.nombreGuarderia = nombreGuarderia;
    }

    @Basic
    @Column(name = "FECHA_INICIO_DIARREA", nullable = true)
    public Date getFechaInicioDiarrea() {
        return fechaInicioDiarrea;
    }

    public void setFechaInicioDiarrea(Date fechaInicioDiarrea) {
        this.fechaInicioDiarrea = fechaInicioDiarrea;
    }

    @Basic
    @Column(name = "NO_EVACU_24HRS", nullable = true)
    public Integer getNoEvacuaciones24Hrs() {
        return noEvacuaciones24Hrs;
    }

    public void setNoEvacuaciones24Hrs(Integer noEvacuaciones24Hrs) {
        this.noEvacuaciones24Hrs = noEvacuaciones24Hrs;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="FIEBRE", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_FIEBRE_FK")
    public Respuesta getFiebre() {
        return fiebre;
    }

    public void setFiebre(Respuesta fiebre) {
        this.fiebre = fiebre;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="VOMITO", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_VOMITO_FK")
    public Respuesta getVomito() {
        return vomito;
    }

    public void setVomito(Respuesta vomito) {
        this.vomito = vomito;
    }

    @Basic
    @Column(name = "NO_VOMITO_24HRS", nullable = true)
    public Integer getNoVomito24Hrs() {
        return noVomito24Hrs;
    }

    public void setNoVomito24Hrs(Integer noVomito24Hrs) {
        this.noVomito24Hrs = noVomito24Hrs;
    }

    @Basic
    @Column(name = "FECHA_INICIO_VOMITO", nullable = true)
    public Date getFechaInicioVomito() {
        return fechaInicioVomito;
    }

    public void setFechaInicioVomito(Date fechaInicioVomito) {
        this.fechaInicioVomito = fechaInicioVomito;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="HECES", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_HECES_FK")
    public CaracteristaHeces getCaracteristaHeces() {
        return caracteristaHeces;
    }

    public void setCaracteristaHeces(CaracteristaHeces caracteristaHeces) {
        this.caracteristaHeces = caracteristaHeces;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="GRADO_DESHIDRATACION", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_GRADODH_FK")
    public GradoDeshidratacion getGradoDeshidratacion() {
        return gradoDeshidratacion;
    }

    public void setGradoDeshidratacion(GradoDeshidratacion gradoDeshidratacion) {
        this.gradoDeshidratacion = gradoDeshidratacion;
    }

    @Basic
    @Column(name = "DIAS_HOSPITALIZADO", nullable = true)
    public Integer getDiasHospitalizacion() {
        return diasHospitalizacion;
    }

    public void setDiasHospitalizacion(Integer diasHospitalizacion) {
        this.diasHospitalizacion = diasHospitalizacion;
    }

    @Basic
    @Column(name = "FECHA_ALTA", nullable = true)
    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="USO_ANTIB_PREVIO", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_ANTIBIOTICO_PREVIO_FK")
    public Respuesta getUsoAntibioticoPrevio() {
        return usoAntibioticoPrevio;
    }

    public void setUsoAntibioticoPrevio(Respuesta usoAntibioticoPrevio) {
        this.usoAntibioticoPrevio = usoAntibioticoPrevio;
    }

    @Basic
    @Column(name = "PLANB", nullable = true)
    public Boolean getPlanB() {
        return planB;
    }

    public void setPlanB(Boolean planB) {
        this.planB = planB;
    }

    @Basic
    @Column(name = "PLANC", nullable = true)
    public Boolean getPlanC() {
        return planC;
    }

    public void setPlanC(Boolean planC) {
        this.planC = planC;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="ANTIBIOTICO_HOSP", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_ANTIBIOTICO_HOSP_FK")
    public Respuesta getAntibioticoHospital() {
        return antibioticoHospital;
    }

    public void setAntibioticoHospital(Respuesta antibioticoHospital) {
        this.antibioticoHospital = antibioticoHospital;
    }

    @Basic
    @Column(name = "CUAL_ANTIBIOTICO", nullable = true, length = 150)
    public String getCualAntibiotico() {
        return cualAntibiotico;
    }

    public void setCualAntibiotico(String cualAntibiotico) {
        this.cualAntibiotico = cualAntibiotico;
    }

    @Basic
    @Column(name = "INGRESADO_UCI", nullable = true)
    public Boolean getUCI() {
        return UCI;
    }

    public void setUCI(Boolean UCI) {
        this.UCI = UCI;
    }

    @Basic
    @Column(name = "DIAS_UCI", nullable = true)
    public Integer getDiasUCI() {
        return diasUCI;
    }

    public void setDiasUCI(Integer diasUCI) {
        this.diasUCI = diasUCI;
    }

    @Basic
    @Column(name = "ALTA_UCI_DIARREA", nullable = true)
    public Boolean getAltaUCIDiarrea() {
        return altaUCIDiarrea;
    }

    public void setAltaUCIDiarrea(Boolean altaUCIDiarrea) {
        this.altaUCIDiarrea = altaUCIDiarrea;
    }

    @Basic
    @Column(name = "FECHA_TERM_DIARREA", nullable = true)
    public Date getFechaTerminoDiarrea() {
        return fechaTerminoDiarrea;
    }

    public void setFechaTerminoDiarrea(Date fechaTerminoDiarrea) {
        this.fechaTerminoDiarrea = fechaTerminoDiarrea;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="VACUNADO", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_VACUNADO_FK")
    public Respuesta getVacunado() {
        return vacunado;
    }

    public void setVacunado(Respuesta vacunado) {
        this.vacunado = vacunado;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="REGISTRO_VACUNA", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_REG_VACUNA_FK")
    public RegistroVacuna getRegistroVacuna() {
        return registroVacuna;
    }

    public void setRegistroVacuna(RegistroVacuna registroVacuna) {
        this.registroVacuna = registroVacuna;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="TIPO_VACUNA", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_TIPO_VACUNA_FK")
    public TipoVacunaRotavirus getTipoVacunaRotavirus() {
        return tipoVacunaRotavirus;
    }

    public void setTipoVacunaRotavirus(TipoVacunaRotavirus tipoVacunaRotavirus) {
        this.tipoVacunaRotavirus = tipoVacunaRotavirus;
    }

    @Basic
    @Column(name = "DOSIS1", nullable = true)
    public Boolean getDosi1() {
        return dosi1;
    }

    public void setDosi1(Boolean dosi1) {
        this.dosi1 = dosi1;
    }

    @Basic
    @Column(name = "FECHA_APLICACION_D1", nullable = true)
    public Date getFechaAplicacionDosis1() {
        return fechaAplicacionDosis1;
    }

    public void setFechaAplicacionDosis1(Date fechaAplicacionDosis1) {
        this.fechaAplicacionDosis1 = fechaAplicacionDosis1;
    }

    @Basic
    @Column(name = "DOSIS2", nullable = true)
    public Boolean getDosi2() {
        return dosi2;
    }

    public void setDosi2(Boolean dosi2) {
        this.dosi2 = dosi2;
    }

    @Basic
    @Column(name = "FECHA_APLICACION_D2", nullable = true)
    public Date getFechaAplicacionDosis2() {
        return fechaAplicacionDosis2;
    }

    public void setFechaAplicacionDosis2(Date fechaAplicacionDosis2) {
        this.fechaAplicacionDosis2 = fechaAplicacionDosis2;
    }

    @Basic
    @Column(name = "DOSIS3", nullable = true)
    public Boolean getDosi3() {
        return dosi3;
    }

    public void setDosi3(Boolean dosi3) {
        this.dosi3 = dosi3;
    }

    @Basic
    @Column(name = "FECHA_APLICACION_D3", nullable = true)
    public Date getFechaAplicacionDosis3() {
        return fechaAplicacionDosis3;
    }

    public void setFechaAplicacionDosis3(Date fechaAplicacionDosis3) {
        this.fechaAplicacionDosis3 = fechaAplicacionDosis3;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="CLASIFICACION_FINAL", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_CLASIFICACION_FINAL_FK")
    public ClasificacionFinalRotavirus getClasificacionFinal() {
        return clasificacionFinal;
    }

    public void setClasificacionFinal(ClasificacionFinalRotavirus clasificacionFinal) {
        this.clasificacionFinal = clasificacionFinal;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="CONDICION_EGRESO", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_CONDICION_EGRESO_FK")
    public CondicionEgreso getCondicionEgreso() {
        return condicionEgreso;
    }

    public void setCondicionEgreso(CondicionEgreso condicionEgreso) {
        this.condicionEgreso = condicionEgreso;
    }

    @Basic
    @Column(name = "NOMBRE_LLENA_FICHA", nullable = true, length = 150)
    public String getNombreLlenaFicha() {
        return nombreLlenaFicha;
    }

    public void setNombreLlenaFicha(String nombreLlenaFicha) {
        this.nombreLlenaFicha = nombreLlenaFicha;
    }

    @Basic
    @Column(name = "NOMBRE_EPIDEMIOLOGO", nullable = true, length = 150)
    public String getEpidemiologo() {
        return epidemiologo;
    }

    public void setEpidemiologo(String epidemiologo) {
        this.epidemiologo = epidemiologo;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="SALA", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_SALA_FK")
    public SalaRotaVirus getSala() {
        return sala;
    }

    public void setSala(SalaRotaVirus sala) {
        this.sala = sala;
    }

    @Basic
    @Column(name = "FECHA_INGRESO", nullable = true)
    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Basic
    @Column(name = "OTRA_HECES", nullable = true, length = 255)
    public String getOtraCaracteristicaHeces() {
        return otraCaracteristicaHeces;
    }

    public void setOtraCaracteristicaHeces(String otraCaracteristicaHeeces) {
        this.otraCaracteristicaHeces = otraCaracteristicaHeeces;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class, optional = true)
    @JoinColumn(name="MX_HECES", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "RT_TOMOMUESTRAHECES_FK")
    public Respuesta getTomoMuestraHeces() {
        return tomoMuestraHeces;
    }

    public void setTomoMuestraHeces(Respuesta tomoMuestraHeces) {
        this.tomoMuestraHeces = tomoMuestraHeces;
    }

    @Basic
    @Column(name = "NUM_EXPEDIENTE", nullable = true, length = 32)
    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    @Basic
    @Column(name = "CODIGO", nullable = true, length = 32)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "IGNORADO_FTD", nullable = true)
    public Boolean getIgnoradoFechaTD() {
        return ignoradoFechaTD;
    }

    public void setIgnoradoFechaTD(Boolean ignoradoFechaTD) {
        this.ignoradoFechaTD = ignoradoFechaTD;
    }

    @Basic
    @Column(name = "NOMBRE_TOMOMX", nullable = true, length = 156)
    public String getNombreTomoMx() {
        return nombreTomoMx;
    }

    public void setNombreTomoMx(String nombreTomoMx) {
        this.nombreTomoMx = nombreTomoMx;
    }

    @Basic
    @Column(name = "TELEFONO_TUTOR", nullable = true, length = 32)
    public String getTelefonoTutor() {
        return telefonoTutor;
    }

    public void setTelefonoTutor(String telefonoTutor) {
        this.telefonoTutor = telefonoTutor;
    }

    @Override
    public boolean isFieldAuditable(String fieldname) {
        if (fieldname.matches("daNotificacion")) return false;
        else  return true;
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

    @Override
    public String toString() {
        return "{" +
                "idFichaRotavirus='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FichaRotavirus)) return false;

        FichaRotavirus that = (FichaRotavirus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
