package ni.gob.minsa.alerta.domain.irag;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.seguridad.Usuarios;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by souyen-ics
 */
@Entity
@Table (name = "DA_CONDICIONES_PREVIAS_IRAG", schema = "ALERTA")
public class DaCondicionesPreviasIrag implements Serializable {

    private Integer idCondicion;
    private DaIrag idIrag;
    private CondicionPrevia codCondicion;
    private Integer semanasEmbarazo;
    private String otraCondicion;
    private Timestamp fechaRegistro;
    private Usuarios usuario;
    private boolean pasivo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CONDICION", nullable = false, updatable = true, insertable = true, precision = 0)
    public Integer getIdCondicion() { return idCondicion; }

    public void setIdCondicion(Integer idCondicion) { this.idCondicion = idCondicion;  }


    @ManyToOne(optional=false)
    @JoinColumn(name="ID_IRAG", referencedColumnName = "ID_IRAG")
    @ForeignKey(name = "ID_IRAG_CPRE_FK")

    public DaIrag getIdIrag() { return idIrag; }

    public void setIdIrag(DaIrag idIrag) { this.idIrag = idIrag; }


    @ManyToOne(fetch=FetchType.LAZY,targetEntity=Catalogo.class)
    @JoinColumn(name="COD_CONDICION", referencedColumnName = "CODIGO")
    @ForeignKey(name = "COD_CONDICION_FK")
    public CondicionPrevia getCodCondicion() { return codCondicion; }

    public void setCodCondicion(CondicionPrevia codCondicion) { this.codCondicion = codCondicion; }

    @Basic
    @Column(name = "SEMANAS_EMBARAZO", nullable = true, updatable = true, insertable = true, precision = 0)
    public Integer getSemanasEmbarazo() { return semanasEmbarazo;  }

    public void setSemanasEmbarazo(Integer semanasEmbarazo) { this.semanasEmbarazo = semanasEmbarazo; }

    @Basic
    @Column(name = "OTRA_CONDICION", nullable = true, insertable = true, updatable = true, length = 100)
    public String getOtraCondicion() { return otraCondicion; }

    public void setOtraCondicion(String otraCondicion) { this.otraCondicion = otraCondicion; }


    @Column(name = "FECHA_REGISTRO", nullable = false, insertable = true, updatable = true)
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="USUARIO_ID", referencedColumnName = "USUARIO_ID")
    @ForeignKey(name = "USUARIO_CPRE_FK")
    public Usuarios getUsuario() { return usuario; }

    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }

    @Basic
    @Column(name = "PASIVO", nullable = true, insertable = true, updatable = true)
    public boolean isPasivo() { return pasivo; }

    public void setPasivo(boolean pasivo) { this.pasivo = pasivo; }


}
