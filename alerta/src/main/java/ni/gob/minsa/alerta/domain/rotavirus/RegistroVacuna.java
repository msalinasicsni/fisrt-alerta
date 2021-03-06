package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name ="getRegistroVacunaByCodigo",
                query = "select cat from RegistroVacuna cat where cat.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "REGVACRT")
public class RegistroVacuna extends Catalogo {

    public RegistroVacuna(){}
}
