package ni.gob.minsa.alerta.domain.persona;

import javax.persistence.*;
import ni.gob.minsa.alerta.domain.estructura.Catalogo;

@NamedQueries({
@NamedQuery(
	name = "obtenerEstadoCivilPorCodigo",
	query = "select cat from EstadoCivil cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ESTCV")
public class EstadoCivil extends Catalogo {

	private static final long serialVersionUID = 1L;
	
	public EstadoCivil(){
	}
	
}
