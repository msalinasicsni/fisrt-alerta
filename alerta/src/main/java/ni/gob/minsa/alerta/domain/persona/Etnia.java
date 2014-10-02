package ni.gob.minsa.alerta.domain.persona;

import javax.persistence.*;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

@NamedQueries({
@NamedQuery(
	name = "obtenerEtniaPorCodigo",
	query = "select cat from Etnia cat where cat.codigo = :pCodigo"
	)
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ETNIA")
public class Etnia extends Catalogo {

    private static final long serialVersionUID = 1L;
	
    public Etnia() {
    }

}


