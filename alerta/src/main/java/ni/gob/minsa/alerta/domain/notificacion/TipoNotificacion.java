package ni.gob.minsa.alerta.domain.notificacion;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by souyen-ics on 11-17-14.
 */

@NamedQueries({
        @NamedQuery(name ="getTipoNotifCodigo",
                query = "select noti from TipoNotificacion noti where noti.codigo = :pCodigo")})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "TPNOTI")
public class TipoNotificacion extends Catalogo {

    private static final long serialVersionUID = 4790779190824937485L;

    public TipoNotificacion() {
    }
}
