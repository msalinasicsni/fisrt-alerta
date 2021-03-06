package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by FIRSTICT on 10/7/2014.
 */
@NamedQueries({
        @NamedQuery(
                name = "obtenerDistritosPorCodigo",
                query = "select cat from Distritos cat where cat.codigo = :pCodigo"
        )
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value= "DISTRIT")
public class Distritos extends Catalogo {
    private static final long serialVersionUID = 1L;

    public Distritos() {
    }
}
