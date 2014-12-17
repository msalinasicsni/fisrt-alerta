package ni.gob.minsa.alerta.domain.muestra;

import javax.persistence.*;

/**
 * Created by souyen-ics on 11-13-14.
 */

@Entity
@Table(name = "tipo_muestra", schema = "alerta")
    public class TipoMx {

    Integer idTipoMx;
    String nombre;
    boolean pasivo;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID_TIPOMX", nullable = false, insertable = true, updatable = true)
    public Integer getIdTipoMx() {
        return idTipoMx;
    }

    public void setIdTipoMx(Integer idTipoMx) {
        this.idTipoMx = idTipoMx;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = false, insertable = true, updatable = true, length = 200)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "PASIVO", nullable = true, insertable = true, updatable = true)
    public boolean isPasivo() {
        return pasivo;
    }

    public void setPasivo(boolean pasivo) {
        this.pasivo = pasivo;
    }
}


