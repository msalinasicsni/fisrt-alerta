package ni.gob.minsa.alerta.domain.vih;

import ni.gob.minsa.alerta.domain.estructura.Catalogo;
import ni.gob.minsa.alerta.domain.estructura.EntidadesAdtvas;
import ni.gob.minsa.alerta.domain.estructura.Unidades;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by USER on 13/10/2014.
 */
@Entity
@Table(name = "sive_vih_ficha", schema = "sive")
public class VihFicha {

    private String id_ficha_vih;
    private String codigo_usuario_vih;
    private Date fecha;
    private String entidadesAdtva;
    private Unidades unidadSalud;
    private String id_municipio;
    private Integer id_categoria_afiliacion;
    private String responsable_ficha;
    private Date fechaAlta;
    private String usuarioAlta;
    private Date fechaBaja;
    private String usuarioBaja;
    private Integer id_metodo_captacion;

    @Id
    @Column(name = "codigo_ficha_vih", nullable = false, insertable = true, updatable = true, precision = 0)
    public String getId_ficha_vih() {
        return id_ficha_vih;
    }

    public void setId_ficha_vih(String id) {
        this.id_ficha_vih = id;
    }

    public String getCodigo_usuario_vih() {
        return codigo_usuario_vih;
    }

    public void setCodigo_usuario_vih(String codigo_usuario_vih) {
        this.codigo_usuario_vih = codigo_usuario_vih;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name="ID_SILAIS")
    public String getEntidadesAdtva() {
        return entidadesAdtva;
    }

    public void setEntidadesAdtva(String entidadesAdtva) {
        this.entidadesAdtva = entidadesAdtva;
    }

    public String getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(String id_municipio) {
        this.id_municipio = id_municipio;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="ID_UNIDAD_SALUD", referencedColumnName = "CODIGO")
    @ForeignKey(name = "VIHFICHA_UNIDADES_FK")
    public Unidades getUnidadSalud() {
        return unidadSalud;
    }

    public void setUnidadSalud(Unidades unidadSalud) {
        this.unidadSalud = unidadSalud;
    }

    public Integer getId_categoria_afiliacion() {
        return this.id_categoria_afiliacion;
    }

    public void setId_categoria_afiliacion(Integer id_categoria_afiliacion) {
        this.id_categoria_afiliacion = id_categoria_afiliacion;
    }

    public String getResponsable_ficha() {
        return responsable_ficha;
    }

    public void setResponsable_ficha(String responsable_ficha) {
        this.responsable_ficha = responsable_ficha;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ALTA")
    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Column(name = "USUARIO_ALTA")
    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_BAJA")
    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Column(name = "USUARIO_BAJA")
    public String getUsuarioBaja() {
        return usuarioBaja;
    }

    public void setUsuarioBaja(String usuarioBaja) {
        this.usuarioBaja = usuarioBaja;
    }

    @Column(name = "ID_METODO_CAPTACION")
    public Integer getId_metodo_captacion() {
        return id_metodo_captacion;
    }

    public void setId_metodo_captacion(Integer id_metodo_captacion) {
        this.id_metodo_captacion = id_metodo_captacion;
    }





}
