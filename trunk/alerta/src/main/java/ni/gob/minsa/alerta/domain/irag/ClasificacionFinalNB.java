package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by souyen-ics on 11-07-14.
 */

@NamedQueries({
        @NamedQuery(
                name = "getClasificacionFinalNBByCodigo",
                query = "select clasi from ClasificacionFinalNB clasi where clasi.codigo = :pCodigo"
        )
})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "CLASFNB")
public class ClasificacionFinalNB extends Catalogo {


    private static final long serialVersionUID = 4228978080874804206L;

    public ClasificacionFinalNB() {
    }
}
