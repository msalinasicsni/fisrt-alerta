package ni.gob.minsa.alerta.domain.muestra;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;

import javax.persistence.*;

/**
 * Created by souyen-ics on 11-13-14.
 */

@javax.persistence.NamedQueries({
        @NamedQuery(
                name = "getCausaRechazoMxByCodigo",
                query = "select causa from CausaRechazoMx causa where causa.codigo = :pCodigo"
        ),
        @NamedQuery(
                name = "getCausaRechazoMxRecepGeneral",
                query = "select causa from CausaRechazoMx causa where causa.codigo like '%|GRAL|%' order by orden"
        ),
        @NamedQuery(
                name = "getCausaRechazoMxRecepLab",
                query = "select causa from CausaRechazoMx causa where causa.codigo like '%|LAB|%' order by orden asc "
        )
})

    @Entity
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorValue(value = "CAUSARECHMX")
    public class CausaRechazoMx extends Catalogo {

    public CausaRechazoMx() {
    }
}


