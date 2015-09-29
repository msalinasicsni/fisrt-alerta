package ni.gob.minsa.alerta.domain.sive;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "sive_patologias_vig_tipo", schema = "alerta", uniqueConstraints = @UniqueConstraint(columnNames = "CODIGO"))
public class SivePatologiasTipo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private SivePatologias patologia;
	private Integer factor;
	private String tipoPob;
	
	
	@Id
	@Column(name = "ID_PATOLOGIA", nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@OneToOne(optional = true)
    @JoinColumn(name = "CODIGO", referencedColumnName = "CODIGO")
    @ForeignKey(name = "TIPO_PATOLOGIA_FK")
	public SivePatologias getPatologia() {
		return patologia;
	}
	public void setPatologia(SivePatologias patologia) {
		this.patologia = patologia;
	}
	@Column(name = "FACTOR", nullable = true)
	public Integer getFactor() {
		return factor;
	}
	public void setFactor(Integer factor) {
		this.factor = factor;
	}
	@Column(name = "TIPO_POB", nullable = true, length = 25)
	public String getTipoPob() {
		return tipoPob;
	}
	public void setTipoPob(String tipoPob) {
		this.tipoPob = tipoPob;
	}
}
