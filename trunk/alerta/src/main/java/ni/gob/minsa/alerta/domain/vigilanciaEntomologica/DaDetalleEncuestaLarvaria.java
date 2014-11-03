package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

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
@Table(name = "DA_DETALLE_ENCUESTA_LARVARIA", schema = "ALERTA")
public class DaDetalleEncuestaLarvaria {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaDetalleEncuestaLarvaria that = (DaDetalleEncuestaLarvaria) o;

        if (detaEncuestaId != that.detaEncuestaId) return false;
        if (arbolInfestado != null ? !arbolInfestado.equals(that.arbolInfestado) : that.arbolInfestado != null)
            return false;
        if (artEspecialInfes != null ? !artEspecialInfes.equals(that.artEspecialInfes) : that.artEspecialInfes != null)
            return false;
        if (barrilInfestado != null ? !barrilInfestado.equals(that.barrilInfestado) : that.barrilInfestado != null)
            return false;
        if (barroInfestado != null ? !barroInfestado.equals(that.barroInfestado) : that.barroInfestado != null)
            return false;
        if (cisterInfestado != null ? !cisterInfestado.equals(that.cisterInfestado) : that.cisterInfestado != null)
            return false;
        if (bebederoInfestado != null ? !bebederoInfestado.equals(that.bebederoInfestado) : that.bebederoInfestado != null)
            return false;
        if (especieAegypti != null ? !especieAegypti.equals(that.especieAegypti) : that.especieAegypti != null)
            return false;
        if (especieAlbopic != null ? !especieAlbopic.equals(that.especieAlbopic) : that.especieAlbopic != null)
            return false;
        if (especieCulexAlbim != null ? !especieCulexAlbim.equals(that.especieCulexAlbim) : that.especieCulexAlbim != null)
            return false;
        if (especieCulexCoronat != null ? !especieCulexCoronat.equals(that.especieCulexCoronat) : that.especieCulexCoronat != null)
            return false;
        if (especieCulexErratico != null ? !especieCulexErratico.equals(that.especieCulexErratico) : that.especieCulexErratico != null)
            return false;
        if (especieCulexFatigans != null ? !especieCulexFatigans.equals(that.especieCulexFatigans) : that.especieCulexFatigans != null)
            return false;
        if (especieCulexNigrip != null ? !especieCulexNigrip.equals(that.especieCulexNigrip) : that.especieCulexNigrip != null)
            return false;
        if (especieCulexQuinque != null ? !especieCulexQuinque.equals(that.especieCulexQuinque) : that.especieCulexQuinque != null)
            return false;
        if (especieCulexTarsalis != null ? !especieCulexTarsalis.equals(that.especieCulexTarsalis) : that.especieCulexTarsalis != null)
            return false;
        if (floreroInfestado != null ? !floreroInfestado.equals(that.floreroInfestado) : that.floreroInfestado != null)
            return false;
        if (inodoroInfestado != null ? !inodoroInfestado.equals(that.inodoroInfestado) : that.inodoroInfestado != null)
            return false;
        if (llantaInfestado != null ? !llantaInfestado.equals(that.llantaInfestado) : that.llantaInfestado != null)
            return false;
        if (otrosDepositosInfes != null ? !otrosDepositosInfes.equals(that.otrosDepositosInfes) : that.otrosDepositosInfes != null)
            return false;
        if (pilaInfestado != null ? !pilaInfestado.equals(that.pilaInfestado) : that.pilaInfestado != null)
            return false;
        if (plantaInfestado != null ? !plantaInfestado.equals(that.plantaInfestado) : that.plantaInfestado != null)
            return false;
        if (pozoInfestado != null ? !pozoInfestado.equals(that.pozoInfestado) : that.pozoInfestado != null)
            return false;
        if (feRegistro != null ? !feRegistro.equals(that.feRegistro) : that.feRegistro != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = detaEncuestaId.hashCode();
        result = 31 * result + (pilaInfestado != null ? pilaInfestado.hashCode() : 0);
        result = 31 * result + (llantaInfestado != null ? llantaInfestado.hashCode() : 0);
        result = 31 * result + (barrilInfestado != null ? barrilInfestado.hashCode() : 0);
        result = 31 * result + (floreroInfestado != null ? floreroInfestado.hashCode() : 0);
        result = 31 * result + (bebederoInfestado != null ? bebederoInfestado.hashCode() : 0);
        result = 31 * result + (artEspecialInfes != null ? artEspecialInfes.hashCode() : 0);
        result = 31 * result + (otrosDepositosInfes != null ? otrosDepositosInfes.hashCode() : 0);
        result = 31 * result + (cisterInfestado != null ? cisterInfestado.hashCode() : 0);
        result = 31 * result + (inodoroInfestado != null ? inodoroInfestado.hashCode() : 0);
        result = 31 * result + (barroInfestado != null ? barroInfestado.hashCode() : 0);
        result = 31 * result + (plantaInfestado != null ? plantaInfestado.hashCode() : 0);
        result = 31 * result + (arbolInfestado != null ? arbolInfestado.hashCode() : 0);
        result = 31 * result + (pozoInfestado != null ? pozoInfestado.hashCode() : 0);
        result = 31 * result + (especieAegypti != null ? especieAegypti.hashCode() : 0);
        result = 31 * result + (especieAlbopic != null ? especieAlbopic.hashCode() : 0);
        result = 31 * result + (especieCulexQuinque != null ? especieCulexQuinque.hashCode() : 0);
        result = 31 * result + (especieCulexNigrip != null ? especieCulexNigrip.hashCode() : 0);
        result = 31 * result + (especieCulexCoronat != null ? especieCulexCoronat.hashCode() : 0);
        result = 31 * result + (especieCulexErratico != null ? especieCulexErratico.hashCode() : 0);
        result = 31 * result + (especieCulexTarsalis != null ? especieCulexTarsalis.hashCode() : 0);
        result = 31 * result + (especieCulexFatigans != null ? especieCulexFatigans.hashCode() : 0);
        result = 31 * result + (especieCulexAlbim != null ? especieCulexAlbim.hashCode() : 0);
        result = 31 * result + (feRegistro != null ? feRegistro.hashCode() : 0);
        return result;
    }
}
