package ni.gob.minsa.alerta.domain.catalogos;

import javax.persistence.*;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
@NamedQueries({
@NamedQuery(
	name = "obtenerAreaRepPorCodigo",
	query = "select cat from AreaRep cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="AREAREP")
public class AreaRep extends Catalogo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AreaRep() {
		
	}

}
