package ni.gob.minsa.alerta.domain.agrupaciones;


import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.portal.Usuarios;
import ni.gob.minsa.alerta.domain.sive.SivePatologias;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by FIRSTICT on 6/16/2016.
 * V1.0
 */
@Entity
@Table(name = "grupo_patologia", schema = "alerta")
public class GrupoPatologia implements Auditable {
    Integer idGrupoPatologia;
    Grupo grupo;
    SivePatologias patologia;
    Date fechaRegistro;
    Usuarios usuarioRegistro;
    private String actor;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    @Column(name = "ID_GRUPO_PATO", nullable = false, insertable = true, updatable = true)
    public Integer getIdGrupoPatologia() {
        return idGrupoPatologia;
    }

    public void setIdGrupoPatologia(Integer idGrupoPatologia) {
        this.idGrupoPatologia = idGrupoPatologia;
    }

    @ManyToOne()
    @JoinColumn(name="ID_GRUPO", referencedColumnName="ID_GRUPO", nullable=false)
    @ForeignKey(name = "grupo_patologia_grupo_fk")
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @ManyToOne()
    @JoinColumn(name="ID_PATOLOGIA", referencedColumnName="ID_PATOLOGIA", nullable=false)
    @ForeignKey(name = "grupo_patologia_patologia_fk")
    public SivePatologias getPatologia() {
        return patologia;
    }

    public void setPatologia(SivePatologias patologia) {
        this.patologia = patologia;
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
    @ForeignKey(name = "grupo_patologia_usuario_fk")
    public Usuarios getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuarios usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public boolean isFieldAuditable(String fieldname) {
            return true;
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
        return "GrupoPatologia{" + idGrupoPatologia +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrupoPatologia)) return false;

        GrupoPatologia that = (GrupoPatologia) o;

        if (!idGrupoPatologia.equals(that.idGrupoPatologia)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return idGrupoPatologia.hashCode();
    }
}
