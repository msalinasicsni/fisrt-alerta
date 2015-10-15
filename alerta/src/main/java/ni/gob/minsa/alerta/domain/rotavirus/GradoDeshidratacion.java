package ni.gob.minsa.alerta.domain.rotavirus;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name ="getGradoDeshidratacionByCodigo",
                query = "select cat from GradoDeshidratacion cat where cat.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "GRADODESH")
public class GradoDeshidratacion extends Catalogo {

    public GradoDeshidratacion(){}
}
