package ni.gob.minsa.alerta.domain.muestra;

import ni.gob.minsa.alerta.domain.examen.CatalogoExamenes;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/**
 * Created by FIRSTICT on 12/15/2014.
 */
@Entity
@Table(name = "examen_tipomx", schema = "alerta",uniqueConstraints=@UniqueConstraint(columnNames={"ID_EXAMEN", "ID_TIPOMX"}))
public class Examen_TipoMx {
    Integer idExamenTipoMx;
    CatalogoExamenes examen;
    TipoMx tipoMx;
    boolean pasivo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EXAMEN_TIPOMX", nullable = false, updatable = true, insertable = true, precision = 0)
    public Integer getIdExamenTipoMx() {
        return idExamenTipoMx;
    }

    public void setIdExamenTipoMx(Integer idExamenTipoMx) {
        this.idExamenTipoMx = idExamenTipoMx;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="ID_EXAMEN", referencedColumnName = "ID_EXAMEN", nullable = false)
    @ForeignKey(name = "EXTMX_EXAMEN_FK")
    public CatalogoExamenes getExamen() {
        return examen;
    }

    public void setExamen(CatalogoExamenes examen) {
        this.examen = examen;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="ID_TIPOMX", referencedColumnName = "ID_TIPOMX", nullable = false)
    @ForeignKey(name = "EXTMX_TIPOMX_FK")
    public TipoMx getTipoMx() {
        return tipoMx;
    }

    public void setTipoMx(TipoMx tipoMx) {
        this.tipoMx = tipoMx;
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
