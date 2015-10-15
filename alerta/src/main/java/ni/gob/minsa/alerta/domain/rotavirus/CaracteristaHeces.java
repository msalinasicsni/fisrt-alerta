package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name ="getCaracteristaHecesByCodigo",
                query = "select cat from CaracteristaHeces cat where cat.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "CARHECESRT")
public class CaracteristaHeces extends Catalogo {

    public CaracteristaHeces(){}
}
