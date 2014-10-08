package ni.gob.minsa.alerta.domain.vigilanciaEntomologica;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by FIRSTICT on 10/7/2014.
 */
@NamedQueries({
        @NamedQuery(
                name = "obtenerModelosEncuestaPorCodigo",
                query = "select cat from ModeloEncuesta cat where cat.codigo = :pCodigo"
        )
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value= "TIPOMODENCU")
public class ModeloEncuesta extends Catalogo {
    private static final long serialVersionUID = 1L;

    public ModeloEncuesta() {
    }
}
