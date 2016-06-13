package ni.gob.minsa.alerta.domain.estructura;

import javax.persistence.*;

/**
 * Created by FIRSTICT on 6/9/2016.
 * V1.0
 */
@NamedQueries({
        @NamedQuery(
                name = "obtenerZonasPorCodigo",
                query = "select ze from ZonaEspecial ze where ze.codigo = :pCodigo"
        )
})
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ZONACM")
public class ZonaEspecial extends Catalogo {

    public ZonaEspecial(){}
}
