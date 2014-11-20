package ni.gob.minsa.alerta.domain.muestra;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import org.hibernate.annotations.NamedQueries;

import javax.persistence.*;

/**
 * Created by souyen-ics on 11-13-14.
 */

@javax.persistence.NamedQueries({
        @NamedQuery(
                name = "getTipoMxByCodigo",
                query = "select tpmx from TipoMx tpmx where tpmx.codigo = :pCodigo"
        )
})

    @Entity
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorValue(value = "TPMX")
    public class TipoMx extends Catalogo {

    public TipoMx() {
    }
}


