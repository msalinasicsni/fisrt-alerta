package ni.gob.minsa.alerta.domain.persona;

import javax.persistence.*;
import ni.gob.minsa.alerta.domain.estructura.Catalogo;

@NamedQueries({
@NamedQuery(
	name = "obtenerIdentificacionPorCodigo",
	query = "select cat from Identificacion cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TPOID")
public class Identificacion extends Catalogo {
   
    private static final long serialVersionUID = 1L;

    public Identificacion() {
    
    }
    
}


