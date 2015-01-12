package ni.gob.minsa.alerta.domain.catalogos;

import javax.persistence.*;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
@NamedQueries({
@NamedQuery(
	name = "obtenerSemanasPorCodigo",
	query = "select cat from Semanas cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="SEMANASEPI")
public class Semanas extends Catalogo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Semanas() {
		
	}

}
