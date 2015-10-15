package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name ="getTipoVacunaRotavirusByCodigo",
                query = "select cat from TipoVacunaRotavirus cat where cat.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "TIPOVACRT")
public class TipoVacunaRotavirus extends Catalogo {

    public TipoVacunaRotavirus(){}
}
