package ni.gob.minsa.alerta.domain.vigilanciaSindFebril;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import ni.gob.minsa.alerta.domain.persona.SisPersona;
@Entity
@Table(name = "DA_FICHA_SINDFEB", schema = "ALERTA")
public class DaSindFebril {
	
	private String idFichaEpidem;
	private SisPersona persona;
	private EntidadesAdtvas codSilaisAtencion;
    private Unidades codUnidadAtencion;
    private String codExpediente;
    private String numFicha;
    private String idLab;
    private Date fechaFicha;
    
    private Integer semanaEpi;
    private Integer mesEpi;
    private Integer anioEpi;
    
    private String nombPadre;
    private String nombMadre;

	public DaSindFebril() {
		super();
	}

	public DaSindFebril(SisPersona persona, EntidadesAdtvas codSilaisAtencion,
			Unidades codUnidadAtencion, String codExpediente, String numFicha,
			String idLab, Date fechaFicha, Integer semanaEpi, Integer mesEpi,
			Integer anioEpi) {
		super();
		this.persona = persona;
		this.codSilaisAtencion = codSilaisAtencion;
		this.codUnidadAtencion = codUnidadAtencion;
		this.codExpediente = codExpediente;
		this.numFicha = numFicha;
		this.idLab = idLab;
		this.fechaFicha = fechaFicha;
		this.semanaEpi = semanaEpi;
		this.mesEpi = mesEpi;
		this.anioEpi = anioEpi;
	}

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID_FICHA_SINDFEB", nullable = false, insertable = true, updatable = true, length = 50)
	public String getIdFichaEpidem() {
		return idFichaEpidem;
	}

	public void setIdFichaEpidem(String idFichaEpidem) {
		this.idFichaEpidem = idFichaEpidem;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="PERSONA_ID", referencedColumnName = "PERSONA_ID")
    @ForeignKey(name = "PERSONA_SINDFEB_FK")
	public SisPersona getPersona() {
		return persona;
	}

	public void setPersona(SisPersona persona) {
		this.persona = persona;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="COD_SILAIS_ATENCION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "ENTIDADES_SINDFEB_FK")
	public EntidadesAdtvas getCodSilaisAtencion() {
		return codSilaisAtencion;
	}

	public void setCodSilaisAtencion(EntidadesAdtvas codSilaisAtencion) {
		this.codSilaisAtencion = codSilaisAtencion;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="COD_UNIDAD_ATENCION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "UNIDADES_SINDFEB_FK")
	public Unidades getCodUnidadAtencion() {
		return codUnidadAtencion;
	}

	public void setCodUnidadAtencion(Unidades codUnidadAtencion) {
		this.codUnidadAtencion = codUnidadAtencion;
	}

	@Column(name = "EXPEDIENTE", nullable = true, length = 50)
	public String getCodExpediente() {
		return codExpediente;
	}

	public void setCodExpediente(String codExpediente) {
		this.codExpediente = codExpediente;
	}

	@Column(name = "NUM_FICHA", nullable = true, length = 10)
	public String getNumFicha() {
		return numFicha;
	}

	public void setNumFicha(String numFicha) {
		this.numFicha = numFicha;
	}

	@Column(name = "ID_LAB", nullable = true, length = 15)
	public String getIdLab() {
		return idLab;
	}

	public void setIdLab(String idLab) {
		this.idLab = idLab;
	}

	@Column(name = "FECHA_FICHA", nullable = false)
	public Date getFechaFicha() {
		return fechaFicha;
	}

	public void setFechaFicha(Date fechaFicha) {
		this.fechaFicha = fechaFicha;
	}

	@Column(name = "SEMANA", nullable = true)
	public Integer getSemanaEpi() {
		return semanaEpi;
	}

	public void setSemanaEpi(Integer semanaEpi) {
		this.semanaEpi = semanaEpi;
	}

	@Column(name = "MES", nullable = true)
	public Integer getMesEpi() {
		return mesEpi;
	}

	public void setMesEpi(Integer mesEpi) {
		this.mesEpi = mesEpi;
	}

	@Column(name = "ANIO", nullable = true)
	public Integer getAnioEpi() {
		return anioEpi;
	}

	public void setAnioEpi(Integer anioEpi) {
		this.anioEpi = anioEpi;
	}

	@Column(name = "NOMB_MADRE", nullable = true, length = 150)
	public String getNombMadre() {
		return nombMadre;
	}

	public void setNombMadre(String nombMadre) {
		this.nombMadre = nombMadre;
	}

	@Column(name = "NOMB_PADRE", nullable = true, length = 150)
	public String getNombPadre() {
		return nombPadre;
	}

	public void setNombPadre(String nombPadre) {
		this.nombPadre = nombPadre;
	}
    
}
