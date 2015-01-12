package ni.gob.minsa.alerta.domain.catalogos;

import javax.persistence.*;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
@NamedQueries({
@NamedQuery(
	name = "obtenerAniosPorCodigo",
	query = "select cat from Anios cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ANIOSEPI")
public class Anios extends Catalogo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Anios() {
		
	}

}
