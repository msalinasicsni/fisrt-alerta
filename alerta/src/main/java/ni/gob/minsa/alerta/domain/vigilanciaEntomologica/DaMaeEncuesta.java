package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.poblacion.Divisionpolitica;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by MSalinas
 */
@Entity
@Table(name = "da_mae_encuesta", schema = "alerta")
public class DaMaeEncuesta {
    private String encuestaId;
    private Divisionpolitica municipio;
    private String codDistrito;
    private String codArea;
    private Unidades unidadSalud;
    private Procedencia procedencia;
    private Date feInicioEncuesta;
    private Date feFinEncuesta;
    private Ordinal ordinalEncuesta;
    private ModeloEncuesta modeloEncuesta;
    private int semanaEpi;
    private int mesEpi;
    private int anioEpi;
    private Timestamp fechaRegistro;
    private EntidadesAdtvas entidadesAdtva;
    private Usuarios usuario;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ENCUESTA_ID", nullable = false, insertable = true, updatable = true, precision = 0)
    public String getEncuestaId() {
        return encuestaId;
    }
    public void setEncuestaId(String encuestaId) {
        this.encuestaId = encuestaId;
    }

    @Basic
    @Column(name = "COD_DISTRITO", nullable = true, insertable = true, updatable = true, length = 10)
    public String getCodDistrito() {
        return codDistrito;
    }
    public void setCodDistrito(String codDistrito) {
        this.codDistrito = codDistrito;
    }

    @Basic
    @Column(name = "COD_AREA", nullable = true, insertable = true, updatable = true, length = 10)
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FE_INICIO_ENCUESTA", nullable = false, insertable = true, updatable = true)
    public Date getFeInicioEncuesta() {
        return feInicioEncuesta;
    }
    public void setFeInicioEncuesta(Date feInicioEncuesta) {
        this.feInicioEncuesta = feInicioEncuesta;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FE_FIN_ENCUESTA", nullable = true, insertable = true, updatable = true)
    public Date getFeFinEncuesta() {
        return feFinEncuesta;
    }
    public void setFeFinEncuesta(Date feFinEncuesta) {
        this.feFinEncuesta = feFinEncuesta;
    }

    @Basic
    @Column(name = "SEMANA_EPI", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getSemanaEpi() {
        return semanaEpi;
    }
    public void setSemanaEpi(int semanaEpi) {
        this.semanaEpi = semanaEpi;
    }

    @Basic
    @Column(name = "MES_EPI", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getMesEpi() {
        return mesEpi;
    }
    public void setMesEpi(int mesEpi) {
        this.mesEpi = mesEpi;
    }


    @Basic
    @Column(name = "ANIO_EPI", nullable = false, insertable = true, updatable = true, precision = 0)
    public int getAnioEpi() {
        return anioEpi;
    }
    public void setAnioEpi(int anioEpi) {
        this.anioEpi = anioEpi;
    }

    @Basic
    @Column(name = "FECHA_REGISTRO", nullable = true, insertable = true, updatable = false)
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_SILAIS", referencedColumnName = "CODIGO")
    @ForeignKey(name = "MAENCUESTA_ENTIDAD_FK")
    public EntidadesAdtvas getEntidadesAdtva() {
        return entidadesAdtva;
    }

    public void setEntidadesAdtva(EntidadesAdtvas entidadesAdtva) {
        this.entidadesAdtva = entidadesAdtva;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_REGISTRO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "MAENCUESTA_USUARIOS_FK")
    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_UNIDAD_SALUD", referencedColumnName = "CODIGO")
    @ForeignKey(name = "MAENCUESTA_UNIDADES_FK")
    public Unidades getUnidadSalud() {
        return unidadSalud;
    }

    public void setUnidadSalud(Unidades unidadSalud) {
        this.unidadSalud = unidadSalud;
    }

    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_ORDINAL_ENCU", referencedColumnName = "CODIGO")
    @ForeignKey(name = "MAENCUESTA_ORDINALENCUESTA_FK")
    public Ordinal getOrdinalEncuesta() {
        return ordinalEncuesta;
    }

    public void setOrdinalEncuesta(Ordinal ordinalEncuesta) {
        this.ordinalEncuesta = ordinalEncuesta;
    }

    @ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_MODELO_ENCU", referencedColumnName = "CODIGO")
    @ForeignKey(name = "MAENCUESTA_MODELOENCUESTA_FK")
    public ModeloEncuesta getModeloEncuesta() {
        return modeloEncuesta;
    }

    public void setModeloEncuesta(ModeloEncuesta modeloEncuesta) {
        this.modeloEncuesta = modeloEncuesta;
    }

    @ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_PROCEDENCIA", referencedColumnName = "CODIGO", nullable = true)
    @ForeignKey(name = "MAENCUESTA_PROCEDENCIA_FK")
    public Procedencia getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(Procedencia procedencia) {
        this.procedencia = procedencia;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_MUNICIPIO", referencedColumnName = "CODIGO_NACIONAL")
    @ForeignKey(name = "MAENCUESTA_MUNICIPIO_FK")
    public Divisionpolitica getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Divisionpolitica municipio) {
        this.municipio = municipio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaMaeEncuesta that = (DaMaeEncuesta) o;

        if (anioEpi != that.anioEpi) return false;
        if (encuestaId != that.encuestaId) return false;
        if (codArea != null ? !codArea.equals(that.codArea) : that.codArea != null) return false;
        if (codDistrito != null ? !codDistrito.equals(that.codDistrito) : that.codDistrito != null) return false;
        if (feFinEncuesta != null ? !feFinEncuesta.equals(that.feFinEncuesta) : that.feFinEncuesta != null)
            return false;
        if (feInicioEncuesta != null ? !feInicioEncuesta.equals(that.feInicioEncuesta) : that.feInicioEncuesta != null)
            return false;
        if (fechaRegistro != null ? !fechaRegistro.equals(that.fechaRegistro) : that.fechaRegistro != null)
            return false;
        if (mesEpi != that.mesEpi) return false;
        if (semanaEpi != that.semanaEpi) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = encuestaId.hashCode();
        result = 31 * result + (codDistrito != null ? codDistrito.hashCode() : 0);
        result = 31 * result + (codArea != null ? codArea.hashCode() : 0);
        result = 31 * result + (feInicioEncuesta != null ? feInicioEncuesta.hashCode() : 0);
        result = 31 * result + (feFinEncuesta != null ? feFinEncuesta.hashCode() : 0);
        result = 31 * result + semanaEpi;
        result = 31 * result + mesEpi;
        result = 31 * result + anioEpi;
        result = 31 * result + (fechaRegistro != null ? fechaRegistro.hashCode() : 0);
        return result;
    }

}
