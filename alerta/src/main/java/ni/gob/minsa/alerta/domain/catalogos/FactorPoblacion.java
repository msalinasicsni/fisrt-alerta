package ni.gob.minsa.alerta.domain.catalogos;
import javax.persistence.*;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
@NamedQueries({
        @NamedQuery(
                name = "obtenerFactorPoblacionPorCodigo",
                query = "select fac from FactorPoblacion fac where fac.codigo = :pCodigo"
        )
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="FACTORPOB")

public class FactorPoblacion extends Catalogo {

    private static final long serialVersionUID = 1L;
    public FactorPoblacion(){}
}
