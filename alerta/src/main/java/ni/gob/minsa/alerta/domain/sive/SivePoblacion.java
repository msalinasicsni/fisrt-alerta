package ni.gob.minsa.alerta.domain.sive;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "sive_poblacion", schema = "alerta", uniqueConstraints = @UniqueConstraint(columnNames = "GRUPO"))
public class SivePoblacion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	
	private String grupo;
	
	
	@Id
	@Column(name = "ID_PATOLOGIA", nullable = false)
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
}
