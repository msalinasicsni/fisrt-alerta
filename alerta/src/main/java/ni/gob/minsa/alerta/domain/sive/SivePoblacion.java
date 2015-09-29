package ni.gob.minsa.alerta.domain.sive;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

import ni.gob.minsa.alerta.domain.poblacion.Comunidades;

@Entity
@Table(name = "sive_poblacion_comunidades", schema = "alerta")
public class SivePoblacion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer anio;
	private Comunidades comunidad;
	private String grupo;
	private Integer masculino;
	private Integer femenino;
	private Integer total;
	
	
	@Id
	@Column(name = "ID_POBLACION", nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "GRUPO", nullable = true, length = 6)
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	@Column(name = "ANIO", nullable = false)
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	@ManyToOne(optional = true)
    @JoinColumn(name = "COMUNIDAD", referencedColumnName = "COMUNIDAD_ID")
    @ForeignKey(name = "POBLACION_COMUNIDAD_FK")
	public Comunidades getComunidad() {
		return comunidad;
	}
	public void setComunidad(Comunidades comunidad) {
		this.comunidad = comunidad;
	}
	@Column(name = "MASCULINO", nullable = false)
	public Integer getMasculino() {
		return masculino;
	}
	public void setMasculino(Integer masculino) {
		this.masculino = masculino;
	}
	@Column(name = "FEMENINO", nullable = false)
	public Integer getFemenino() {
		return femenino;
	}
	public void setFemenino(Integer femenino) {
		this.femenino = femenino;
	}
	@Column(name = "TOTAL", nullable = false)
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
