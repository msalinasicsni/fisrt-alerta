package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by MSalinas
 */
@Entity
@Table(name = "da_detalle_encuesta_aedes", schema = "alerta")
public class DaDetalleEncuestaAedes implements Auditable {
    private String detaEncuestaId;
    private Comunidades localidad;
    private int viviendaInspeccionada;
    private int viviendaPositiva;
    private int manzanaInspeccionada;
    private int manzanaPositiva;
    private int depositoInspeccionado;
    private int depositoPositivo;
    private int pupaPositiva;
    private Integer noAbatizado;
    private Integer noEliminado;
    private Integer noNeutralizado;
    private Date feAbatizado;
    private Date feRepot;
    private Date feVEnt;
    private Timestamp feRegistro;
    private DaMaeEncuesta maeEncuesta;
    private Usuarios usuarioRegistro;

    private String actor;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "DETA_ENCUESTA_ID", nullable = false, insertable = true, updatable = true, precision = 0)
    public String getDetaEncuestaId() {
        return detaEncuestaId;
    }

    public void setDetaEncuestaId(String detaEncuestaId) {
        this.detaEncuestaId = detaEncuestaId;
    }

    @Basic
    @Column(name = "VIVIENDA_INSPECCIONADA", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getViviendaInspeccionada() {
        return viviendaInspeccionada;
    }

    public void setViviendaInspeccionada(int viviendaInspeccionada) {
        this.viviendaInspeccionada = viviendaInspeccionada;
    }

    @Basic
    @Column(name = "VIVIENDA_POSITIVA", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getViviendaPositiva() {
        return viviendaPositiva;
    }

    public void setViviendaPositiva(int viviendaPositiva) {
        this.viviendaPositiva = viviendaPositiva;
    }

    @Basic
    @Column(name = "MANZANA_INSPECCIONADA", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getManzanaInspeccionada() {
        return manzanaInspeccionada;
    }

    public void setManzanaInspeccionada(int manzanaInspeccionada) {
        this.manzanaInspeccionada = manzanaInspeccionada;
    }

    @Basic
    @Column(name = "MANZANA_POSITIVA", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getManzanaPositiva() {
        return manzanaPositiva;
    }

    public void setManzanaPositiva(int manzanaPositiva) {
        this.manzanaPositiva = manzanaPositiva;
    }

    @Basic
    @Column(name = "DEPOSITO_INSPECCIONADO", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getDepositoInspeccionado() {
        return depositoInspeccionado;
    }

    public void setDepositoInspeccionado(int depositoInspeccionado) {
        this.depositoInspeccionado = depositoInspeccionado;
    }

    @Basic
    @Column(name = "DEPOSITO_POSITIVO", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getDepositoPositivo() {
        return depositoPositivo;
    }

    public void setDepositoPositivo(int depositoPositivo) {
        this.depositoPositivo = depositoPositivo;
    }

    @Basic
    @Column(name = "PUPA_POSITIVA", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getPupaPositiva() {
        return pupaPositiva;
    }

    public void setPupaPositiva(int pupaPositiva) {
        this.pupaPositiva = pupaPositiva;
    }

    @Basic
    @Column(name = "NO_ABATIZADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoAbatizado() {
        return noAbatizado;
    }

    public void setNoAbatizado(Integer noAbatizado) {
        this.noAbatizado = noAbatizado;
    }

    @Basic
    @Column(name = "NO_ELIMINADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoEliminado() {
        return noEliminado;
    }

    public void setNoEliminado(Integer noEliminado) {
        this.noEliminado = noEliminado;
    }

    @Basic
    @Column(name = "NO_NEUTRALIZADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getNoNeutralizado() {
        return noNeutralizado;
    }

    public void setNoNeutralizado(Integer noNeutralizado) {
        this.noNeutralizado = noNeutralizado;
    }

    @Basic
    @Column(name = "FE_ABATIZADO", nullable = true, insertable = true, updatable = true)
    public Date getFeAbatizado() {
        return feAbatizado;
    }

    public void setFeAbatizado(Date feAbatizado) {
        this.feAbatizado = feAbatizado;
    }

    @Basic
    @Column(name = "FE_REPOT", nullable = true, insertable = true, updatable = true)
    public Date getFeRepot() {
        return feRepot;
    }

    public void setFeRepot(Date feRepot) {
        this.feRepot = feRepot;
    }

    @Basic
    @Column(name = "FE_V_ENT", nullable = true, insertable = true, updatable = true)
    public Date getFeVEnt() {
        return feVEnt;
    }

    public void setFeVEnt(Date feVEnt) {
        this.feVEnt = feVEnt;
    }

    @Basic
    @Column(name = "FE_REGISTRO", nullable = true, insertable = true, updatable = false)
    public Timestamp getFeRegistro() {
        return feRegistro;
    }

    public void setFeRegistro(Timestamp feRegistro) {
        this.feRegistro = feRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_LOCALIDAD", referencedColumnName = "CODIGO")
    @ForeignKey(name = "ENCUESTAAEDES_COMUNIDAD_FK")
    public Comunidades getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Comunidades localidad) {
        this.localidad = localidad;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_REGISTRO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "ENCUESTAAEDES_USUARIO_FK")
    public Usuarios getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuarios usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="ENCUESTA_ID", referencedColumnName = "ENCUESTA_ID")
    @ForeignKey(name = "MAENCUESTA_ENCUESTAAEDES_FK")
    public DaMaeEncuesta getMaeEncuesta() {
        return maeEncuesta;
    }

    public void setMaeEncuesta(DaMaeEncuesta maeEncuesta) {
        this.maeEncuesta = maeEncuesta;
    }

    @Override
    public boolean isFieldAuditable(String fieldname) {
        if (fieldname.matches("maeEncuesta") || fieldname.matches("usuarioRegistro") || fieldname.matches("feRegistro")) return false;
        else return  true;
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
                "AdetaEncuestaId='" + detaEncuestaId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaDetalleEncuestaAedes)) return false;

        DaDetalleEncuestaAedes that = (DaDetalleEncuestaAedes) o;

        if (detaEncuestaId != null ? !detaEncuestaId.equals(that.detaEncuestaId) : that.detaEncuestaId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return detaEncuestaId != null ? detaEncuestaId.hashCode() : 0;
    }
}
