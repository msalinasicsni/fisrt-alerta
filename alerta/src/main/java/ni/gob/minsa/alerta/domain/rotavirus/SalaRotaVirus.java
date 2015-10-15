package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name ="getSalasRTByCodigo",
                query = "select cat from SalaRotaVirus cat where cat.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "SALART")
public class SalaRotaVirus extends Catalogo {

    public SalaRotaVirus(){}
}
