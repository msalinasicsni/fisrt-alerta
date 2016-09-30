package ni.gob.minsa.alerta.domain.agrupaciones;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by FIRSTICT on 6/16/2016.
 * V1.0
 */
@Entity
@Table(name = "catalogo_grupo", schema = "alerta")
public class Grupo implements Auditable {

    Integer idGrupo;
    String nombre;
    Date fechaRegistro;
    Usuarios usuarioRegistro;
    private String actor;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    @Column(name = "ID_GRUPO", nullable = false, insertable = true, updatable = false)
    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = false, insertable = true, updatable = true, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "FECHA_REGISTRO", nullable = false)
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @ManyToOne()
    @JoinColumn(name="USUARIO_REGISTRO", referencedColumnName="USUARIO_ID", nullable=false)
    @ForeignKey(name = "grupo_usuario_fk")
    public Usuarios getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuarios usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public boolean isFieldAuditable(String fieldname) {
        if (fieldname.matches("nombre") || fieldname.matches("pasivo"))
            return true;
        else
            return false;
    }

    @Override
    @Transient
    public String getActor() {
        return this.actor;
    }

    @Override
    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "Grupo{" + idGrupo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo)) return false;

        Grupo grupo = (Grupo) o;

        if (idGrupo != null ? !idGrupo.equals(grupo.idGrupo) : grupo.idGrupo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return idGrupo != null ? idGrupo.hashCode() : 0;
    }
}
