package ni.gob.minsa.alerta.domain.sive;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import ni.gob.minsa.alerta.domain.estructura.Unidades;

@Entity
@Table(name = "sive_informe_diario", schema = "alerta")
public class SiveInformeDiario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String silais;
	private String municipio;
	private Unidades unidad;
	private Date fechaNotificacion;
	private Integer semana;
	private SivePatologias patologia;
	private Integer g01f = 0;
	private Integer g01m = 0;
	private Integer g02f = 0;
	private Integer g02m = 0;
	private Integer g03f = 0;
	private Integer g03m = 0;
	private Integer g04f = 0;
	private Integer g04m = 0;
	private Integer g05f = 0;
	private Integer g05m = 0;
	private Integer g06f = 0;
	private Integer g06m = 0;
	private Integer g07f = 0;
	private Integer g07m = 0;
	private Integer g08f = 0;
	private Integer g08m = 0;
	private Integer g09f = 0;
	private Integer g09m = 0;
	private Integer g10f = 0;
	private Integer g10m = 0;
	private Integer g11f = 0;
	private Integer g11m = 0;
	private Integer g12f = 0;
	private Integer g12m = 0;
	private Integer g13f = 0;
	private Integer g13m = 0;
	private Integer descf = 0;
	private Integer descm = 0;
	private Integer totalf = 0;
	private Integer totalm = 0;
	private int bloqueado = 0;
	private Date fechaRegistro;
	private String usuarioRegistro;
	private Date fechaMod;
	private String usuarioMod;
	private Integer numOrden;
	private Integer codSisniven;
	private Integer anio;
	
	@Id
	@Column(name = "ID_NOTIFICACION", nullable = false, length = 50)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "SILAIS", nullable = false, length = 2)
	public String getSilais() {
		return silais;
	}
	public void setSilais(String silais) {
		this.silais = silais;
	}
	@Column(name = "MUNICIPIO", nullable = false, length = 4)
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	@ManyToOne(optional = true)
    @JoinColumn(name = "UNIDAD_SALUD", referencedColumnName = "UNIDAD_ID")
    @ForeignKey(name = "NOTIFICACION_UNIDAD_FK")
	public Unidades getUnidad() {
		return unidad;
	}
	public void setUnidad(Unidades unidad) {
		this.unidad = unidad;
	}
	@Column(name = "FECHA", nullable = false)
	public Date getFechaNotificacion() {
		return fechaNotificacion;
	}
	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}
	@Column(name = "SEMANA_EPI", nullable = false, length = 2)
	public Integer getSemana() {
		return semana;
	}
	public void setSemana(Integer semana) {
		this.semana = semana;
	}
	@ManyToOne(optional = true)
    @JoinColumn(name = "COD_PATOLOGIA", referencedColumnName = "CODIGO")
    @ForeignKey(name = "NOTIFICACION_PATOLOGIA_FK")
	public SivePatologias getPatologia() {
		return patologia;
	}
	public void setPatologia(SivePatologias patologia) {
		this.patologia = patologia;
	}
	@Column(name = "GRUPO1_F", nullable = false, length = 8)
	public Integer getG01f() {
		return g01f;
	}
	public void setG01f(Integer g01f) {
		this.g01f = g01f;
	}
	@Column(name = "GRUPO1_M", nullable = false, length = 8)
	public Integer getG01m() {
		return g01m;
	}
	public void setG01m(Integer g01m) {
		this.g01m = g01m;
	}
	@Column(name = "GRUPO2_F", nullable = false, length = 8)
	public Integer getG02f() {
		return g02f;
	}
	public void setG02f(Integer g02f) {
		this.g02f = g02f;
	}
	@Column(name = "GRUPO2_M", nullable = false, length = 8)
	public Integer getG02m() {
		return g02m;
	}
	public void setG02m(Integer g02m) {
		this.g02m = g02m;
	}
	@Column(name = "GRUPO3_F", nullable = false, length = 8)
	public Integer getG03f() {
		return g03f;
	}
	public void setG03f(Integer g03f) {
		this.g03f = g03f;
	}
	@Column(name = "GRUPO3_M", nullable = false, length = 8)
	public Integer getG03m() {
		return g03m;
	}
	public void setG03m(Integer g03m) {
		this.g03m = g03m;
	}
	@Column(name = "GRUPO4_F", nullable = false, length = 8)
	public Integer getG04f() {
		return g04f;
	}
	public void setG04f(Integer g04f) {
		this.g04f = g04f;
	}
	@Column(name = "GRUPO4_M", nullable = false, length = 8)
	public Integer getG04m() {
		return g04m;
	}
	public void setG04m(Integer g04m) {
		this.g04m = g04m;
	}
	@Column(name = "GRUPO5_F", nullable = false, length = 8)
	public Integer getG05f() {
		return g05f;
	}
	public void setG05f(Integer g05f) {
		this.g05f = g05f;
	}
	@Column(name = "GRUPO5_M", nullable = false, length = 8)
	public Integer getG05m() {
		return g05m;
	}
	public void setG05m(Integer g05m) {
		this.g05m = g05m;
	}
	@Column(name = "GRUPO6_F", nullable = false, length = 8)
	public Integer getG06f() {
		return g06f;
	}
	public void setG06f(Integer g06f) {
		this.g06f = g06f;
	}
	@Column(name = "GRUPO6_M", nullable = false, length = 8)
	public Integer getG06m() {
		return g06m;
	}
	public void setG06m(Integer g06m) {
		this.g06m = g06m;
	}
	@Column(name = "GRUPO7_F", nullable = false, length = 8)
	public Integer getG07f() {
		return g07f;
	}
	public void setG07f(Integer g07f) {
		this.g07f = g07f;
	}
	@Column(name = "GRUPO7_M", nullable = false, length = 8)
	public Integer getG07m() {
		return g07m;
	}
	public void setG07m(Integer g07m) {
		this.g07m = g07m;
	}
	@Column(name = "GRUPO8_F", nullable = false, length = 8)
	public Integer getG08f() {
		return g08f;
	}
	public void setG08f(Integer g08f) {
		this.g08f = g08f;
	}
	@Column(name = "GRUPO8_M", nullable = false, length = 8)
	public Integer getG08m() {
		return g08m;
	}
	public void setG08m(Integer g08m) {
		this.g08m = g08m;
	}
	@Column(name = "GRUPO9_F", nullable = false, length = 8)
	public Integer getG09f() {
		return g09f;
	}
	public void setG09f(Integer g09f) {
		this.g09f = g09f;
	}
	@Column(name = "GRUPO9_M", nullable = false, length = 8)
	public Integer getG09m() {
		return g09m;
	}
	public void setG09m(Integer g09m) {
		this.g09m = g09m;
	}
	@Column(name = "GRUPO10_F", nullable = false, length = 8)
	public Integer getG10f() {
		return g10f;
	}
	public void setG10f(Integer g10f) {
		this.g10f = g10f;
	}
	@Column(name = "GRUPO10_M", nullable = false, length = 8)
	public Integer getG10m() {
		return g10m;
	}
	public void setG10m(Integer g10m) {
		this.g10m = g10m;
	}
	@Column(name = "GRUPO11_F", nullable = false, length = 8)
	public Integer getG11f() {
		return g11f;
	}
	public void setG11f(Integer g11f) {
		this.g11f = g11f;
	}
	@Column(name = "GRUPO11_M", nullable = false, length = 8)
	public Integer getG11m() {
		return g11m;
	}
	public void setG11m(Integer g11m) {
		this.g11m = g11m;
	}
	@Column(name = "GRUPO12_F", nullable = false, length = 8)
	public Integer getG12f() {
		return g12f;
	}
	public void setG12f(Integer g12f) {
		this.g12f = g12f;
	}
	@Column(name = "GRUPO12_M", nullable = false, length = 8)
	public Integer getG12m() {
		return g12m;
	}
	public void setG12m(Integer g12m) {
		this.g12m = g12m;
	}
	@Column(name = "GRUPO13_F", nullable = false, length = 8)
	public Integer getG13f() {
		return g13f;
	}
	public void setG13f(Integer g13f) {
		this.g13f = g13f;
	}
	@Column(name = "GRUPO13_M", nullable = false, length = 8)
	public Integer getG13m() {
		return g13m;
	}
	public void setG13m(Integer g13m) {
		this.g13m = g13m;
	}
	@Column(name = "DESC_F", nullable = false, length = 8)
	public Integer getDescf() {
		return descf;
	}
	public void setDescf(Integer descf) {
		this.descf = descf;
	}
	@Column(name = "DESC_M", nullable = false, length = 8)
	public Integer getDescm() {
		return descm;
	}
	public void setDescm(Integer descm) {
		this.descm = descm;
	}
	@Column(name = "TOTAL_F", nullable = false, length = 8)
	public Integer getTotalf() {
		return totalf;
	}
	public void setTotalf(Integer totalf) {
		this.totalf = totalf;
	}
	@Column(name = "TOTAL_M", nullable = false, length = 8)
	public Integer getTotalm() {
		return totalm;
	}
	public void setTotalm(Integer totalm) {
		this.totalm = totalm;
	}
	@Column(name = "BLOQUEADO", nullable = false, length = 1)
	public int getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(int bloqueado) {
		this.bloqueado = bloqueado;
	}
	@Column(name = "FECHAREGISTRO", nullable = false)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	@Column(name = "USUARIOREGISTRO", nullable = false, length = 20)
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	@Column(name = "FECHAMODIFICACION", nullable = true)
	public Date getFechaMod() {
		return fechaMod;
	}
	public void setFechaMod(Date fechaMod) {
		this.fechaMod = fechaMod;
	}
	@Column(name = "USUARIOMODIFICACION", nullable = true, length = 20)
	public String getUsuarioMod() {
		return usuarioMod;
	}
	public void setUsuarioMod(String usuarioMod) {
		this.usuarioMod = usuarioMod;
	}
	@Column(name = "NUMERO_ORDEN", nullable = true, length = 4)
	public Integer getNumOrden() {
		return numOrden;
	}
	public void setNumOrden(Integer numOrden) {
		this.numOrden = numOrden;
	}
	@Column(name = "COD_SISNIVEN", nullable = true)
	public Integer getCodSisniven() {
		return codSisniven;
	}
	public void setCodSisniven(Integer codSisniven) {
		this.codSisniven = codSisniven;
	}
	@Column(name = "ANIO", nullable = true, length = 4)
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
}
