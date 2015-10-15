package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by souyen-ics on 10-10-14.
 */

@NamedQueries({
        @NamedQuery(
                name = "getClasificacionFinalRotavirusByCodigo",
                query = "select clas from ClasificacionFinalRotavirus clas where clas.codigo = :pCodigo"
        )
})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "CLASFIRT")
public class ClasificacionFinalRotavirus extends Catalogo {

    private static final long serialVersionUID = 5091358381320252870L;

    public ClasificacionFinalRotavirus() {
    }
}
