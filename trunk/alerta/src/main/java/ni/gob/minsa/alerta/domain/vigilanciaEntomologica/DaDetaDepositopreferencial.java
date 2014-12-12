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
@Table(name = "da_deta_depositopreferencial", schema = "alerta")
public class DaDetaDepositopreferencial {
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
    private Integer manzana;
    private Integer vivienda;
    private String nombre;
    private String decripOtroDeposito;
    private String decripcionCister;
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
    @Column(name = "MANZANA", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getManzana() {
        return manzana;
    }

    public void setManzana(Integer manzana) {
        this.manzana = manzana;
    }

    @Basic
    @Column(name = "VIVIENDA", nullable = true, insertable = true, updatable = true, precision = 0)
    public Integer getVivienda() {
        return vivienda;
    }

    public void setVivienda(Integer vivienda) {
        this.vivienda = vivienda;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = true, insertable = true, updatable = true, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "DECRIP_OTRO_DEPOSITO", nullable = true, insertable = true, updatable = true, length = 100)
    public String getDecripOtroDeposito() {
        return decripOtroDeposito;
    }

    public void setDecripOtroDeposito(String decripOtroDeposito) {
        this.decripOtroDeposito = decripOtroDeposito;
    }

    @Basic
    @Column(name = "DECRIPCION_CISTER", nullable = true, insertable = true, updatable = true, length = 100)
    public String getDecripcionCister() {
        return decripcionCister;
    }

    public void setDecripcionCister(String decripcionCister) {
        this.decripcionCister = decripcionCister;
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
    @ForeignKey(name = "MAENCU_ENCUDEPOSITO_FK")
    public DaMaeEncuesta getMaeEncuesta() {
        return maeEncuesta;
    }

    public void setMaeEncuesta(DaMaeEncuesta maeEncuesta) {
        this.maeEncuesta = maeEncuesta;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_REGISTRO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "ENCUDEPOSITO_USUARIO_FK")
    public Usuarios getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuarios usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="COD_LOCALIDAD", referencedColumnName = "CODIGO")
    @ForeignKey(name = "ENCUDEPOSITO_COMUNIDAD_FK")
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

        DaDetaDepositopreferencial that = (DaDetaDepositopreferencial) o;

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
        if (decripOtroDeposito != null ? !decripOtroDeposito.equals(that.decripOtroDeposito) : that.decripOtroDeposito != null)
            return false;
        if (decripcionCister != null ? !decripcionCister.equals(that.decripcionCister) : that.decripcionCister != null)
            return false;
        if (floreroInfestado != null ? !floreroInfestado.equals(that.floreroInfestado) : that.floreroInfestado != null)
            return false;
        if (inodoroInfestado != null ? !inodoroInfestado.equals(that.inodoroInfestado) : that.inodoroInfestado != null)
            return false;
        if (llantaInfestado != null ? !llantaInfestado.equals(that.llantaInfestado) : that.llantaInfestado != null)
            return false;
        if (manzana != null ? !manzana.equals(that.manzana) : that.manzana != null) return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (otrosDepositosInfes != null ? !otrosDepositosInfes.equals(that.otrosDepositosInfes) : that.otrosDepositosInfes != null)
            return false;
        if (pilaInfestado != null ? !pilaInfestado.equals(that.pilaInfestado) : that.pilaInfestado != null)
            return false;
        if (plantaInfestado != null ? !plantaInfestado.equals(that.plantaInfestado) : that.plantaInfestado != null)
            return false;
        if (pozoInfestado != null ? !pozoInfestado.equals(that.pozoInfestado) : that.pozoInfestado != null)
            return false;
        if (vivienda != null ? !vivienda.equals(that.vivienda) : that.vivienda != null) return false;

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
        result = 31 * result + (manzana != null ? manzana.hashCode() : 0);
        result = 31 * result + (vivienda != null ? vivienda.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (decripOtroDeposito != null ? decripOtroDeposito.hashCode() : 0);
        result = 31 * result + (decripcionCister != null ? decripcionCister.hashCode() : 0);
        return result;
    }

}
