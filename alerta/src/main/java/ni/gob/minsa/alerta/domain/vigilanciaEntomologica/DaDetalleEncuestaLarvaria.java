package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.poblacion.Comunidades;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by MSalinas
 */
@Entity
@Table(name = "da_detalle_encuesta_larvaria", schema = "alerta")
public class DaDetalleEncuestaLarvaria implements Auditable {
    private String detaEncuestaId;
    private Comunidades localidad;
    private Integer pilaInfestado;
    private Integer llantaInfestado;
    private Integer barrilInfestado;
    private Integer floreroInfestado;
    private Integer bebederoInfestado;
    private Integer artEspecialInfes;
    private Integer otrosDepositosInfes;
    private Integer cisterInfestado;
    private Integer inodoroInfestado;
    private Integer barroInfestado;
    private Integer plantaInfestado;
    private Integer arbolInfestado;
    private Integer pozoInfestado;
    private Integer especieAegypti;
    private Integer especieAlbopic;
    private Integer especieCulexQuinque;
    private Integer especieCulexNigrip;
    private Integer especieCulexCoronat;
    private Integer especieCulexErratico;
    private Integer especieCulexTarsalis;
    private Integer especieCulexFatigans;
    private Integer especieCulexAlbim;
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
    @Column(name = "PILA_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getPilaInfestado() {
        return pilaInfestado;
    }

    public void setPilaInfestado(Integer pilaInfestado) {
        this.pilaInfestado = pilaInfestado;
    }

    @Basic
    @Column(name = "LLANTA_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getLlantaInfestado() {
        return llantaInfestado;
    }

    public void setLlantaInfestado(Integer llantaInfestado) {
        this.llantaInfestado = llantaInfestado;
    }

    @Basic
    @Column(name = "BARRIL_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getBarrilInfestado() {
        return barrilInfestado;
    }

    public void setBarrilInfestado(Integer barrilInfestado) {
        this.barrilInfestado = barrilInfestado;
    }

    @Basic
    @Column(name = "FLORERO_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getFloreroInfestado() {
        return floreroInfestado;
    }

    public void setFloreroInfestado(Integer floreroInfestado) {
        this.floreroInfestado = floreroInfestado;
    }

    @Basic
    @Column(name = "BEBEDERO_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getBebederoInfestado() {
        return bebederoInfestado;
    }

    public void setBebederoInfestado(Integer debederoInfestado) {
        this.bebederoInfestado = debederoInfestado;
    }

    @Basic
    @Column(name = "ART_ESPECIAL_INFES", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getArtEspecialInfes() {
        return artEspecialInfes;
    }

    public void setArtEspecialInfes(Integer artEspecialInfes) {
        this.artEspecialInfes = artEspecialInfes;
    }

    @Basic
    @Column(name = "OTROS_DEPOSITOS_INFES", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getOtrosDepositosInfes() {
        return otrosDepositosInfes;
    }

    public void setOtrosDepositosInfes(Integer otrosDepositosInfes) {
        this.otrosDepositosInfes = otrosDepositosInfes;
    }

    @Basic
    @Column(name = "CISTER_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getCisterInfestado() {
        return cisterInfestado;
    }

    public void setCisterInfestado(Integer cisterInfestado) {
        this.cisterInfestado = cisterInfestado;
    }

    @Basic
    @Column(name = "INODORO_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getInodoroInfestado() {
        return inodoroInfestado;
    }

    public void setInodoroInfestado(Integer inodoroInfestado) {
        this.inodoroInfestado = inodoroInfestado;
    }

    @Basic
    @Column(name = "BARRO_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getBarroInfestado() {
        return barroInfestado;
    }

    public void setBarroInfestado(Integer barroInfestado) {
        this.barroInfestado = barroInfestado;
    }

    @Basic
    @Column(name = "PLANTA_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getPlantaInfestado() {
        return plantaInfestado;
    }

    public void setPlantaInfestado(Integer plantaInfestado) {
        this.plantaInfestado = plantaInfestado;
    }

    @Basic
    @Column(name = "ARBOL_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getArbolInfestado() {
        return arbolInfestado;
    }

    public void setArbolInfestado(Integer arbolInfestado) {
        this.arbolInfestado = arbolInfestado;
    }

    @Basic
    @Column(name = "POZO_INFESTADO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getPozoInfestado() {
        return pozoInfestado;
    }

    public void setPozoInfestado(Integer pozoInfestado) {
        this.pozoInfestado = pozoInfestado;
    }

    @Basic
    @Column(name = "ESPECIE_AEGYPTI", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieAegypti() {
        return especieAegypti;
    }

    public void setEspecieAegypti(Integer especieAegypti) {
        this.especieAegypti = especieAegypti;
    }

    @Basic
    @Column(name = "ESPECIE_ALBOPIC", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieAlbopic() {
        return especieAlbopic;
    }

    public void setEspecieAlbopic(Integer especieAlbopic) {
        this.especieAlbopic = especieAlbopic;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_QUINQUE", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexQuinque() {
        return especieCulexQuinque;
    }

    public void setEspecieCulexQuinque(Integer especieCulexQuinque) {
        this.especieCulexQuinque = especieCulexQuinque;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_NIGRIP", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexNigrip() {
        return especieCulexNigrip;
    }

    public void setEspecieCulexNigrip(Integer especieCulexNigrip) {
        this.especieCulexNigrip = especieCulexNigrip;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_CORONAT", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexCoronat() {
        return especieCulexCoronat;
    }

    public void setEspecieCulexCoronat(Integer especieCulexCoronat) {
        this.especieCulexCoronat = especieCulexCoronat;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_ERRATICO", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexErratico() {
        return especieCulexErratico;
    }

    public void setEspecieCulexErratico(Integer especieCulexErratico) {
        this.especieCulexErratico = especieCulexErratico;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_TARSALIS", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexTarsalis() {
        return especieCulexTarsalis;
    }

    public void setEspecieCulexTarsalis(Integer especieCulexTarsalis) {
        this.especieCulexTarsalis = especieCulexTarsalis;
    }

        @Basic
    @Column(name = "ESPECIE_CULEX_FATIGANS", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexFatigans() {
        return especieCulexFatigans;
    }

    public void setEspecieCulexFatigans(Integer especieCulexFatigans) {
        this.especieCulexFatigans = especieCulexFatigans;
    }

    @Basic
    @Column(name = "ESPECIE_CULEX_ALBIM", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getEspecieCulexAlbim() {
        return especieCulexAlbim;
    }

    public void setEspecieCulexAlbim(Integer especieCulexAlbim) {
        this.especieCulexAlbim = especieCulexAlbim;
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
    @JoinColumn(name="ENCUESTA_ID", referencedColumnName = "ENCUESTA_ID")
    @ForeignKey(name = "MAENCUESTA_ENCUESTALARVARIA_FK")
    public DaMaeEncuesta getMaeEncuesta() {
        return maeEncuesta;
    }

    public void setMaeEncuesta(DaMaeEncuesta maeEncuesta) {
        this.maeEncuesta = maeEncuesta;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_REGISTRO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "ENCUESTALARVARIA_USUARIO_FK")
    public Usuarios getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuarios usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_LOCALIDAD", referencedColumnName = "CODIGO")
    @ForeignKey(name = "ENCUESTALARVARIA_COMUNIDAD_FK")
    public Comunidades getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Comunidades localidad) {
        this.localidad = localidad;
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
                "LdetaEncuestaId='" + detaEncuestaId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaDetalleEncuestaLarvaria)) return false;

        DaDetalleEncuestaLarvaria that = (DaDetalleEncuestaLarvaria) o;

        if (detaEncuestaId != null ? !detaEncuestaId.equals(that.detaEncuestaId) : that.detaEncuestaId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return detaEncuestaId != null ? detaEncuestaId.hashCode() : 0;
    }
}
