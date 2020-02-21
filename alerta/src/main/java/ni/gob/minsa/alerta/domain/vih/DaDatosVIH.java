package ni.gob.minsa.alerta.domain.vih;

import ni.gob.minsa.alerta.domain.audit.Auditable;
import ni.gob.minsa.alerta.domain.notificacion.DaNotificacion;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "da_datos_vih", schema = "alerta", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_NOTIFICACION"}))
public class DaDatosVIH implements Serializable, Auditable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String idDatosVIH;
    private DaNotificacion idNotificacion;

    private String resA1;
    private String resA2;
    private Timestamp fechaDxVIH;
    private String embarazo;
    private String estadoPx;
    private String infOport;
    private String estaTx;
    private Timestamp fechaTAR;
    private String exposicionPeri;
    private String cesarea;
    private String actor;
    

    public DaDatosVIH() {
        super();
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID_DATOS_VIH", nullable = false, insertable = true, updatable = true, length = 36)
    public String getIdDatosVIH() {
        return idDatosVIH;
    }

    public void setIdDatosVIH(String idDatosVIH) {
        this.idDatosVIH = idDatosVIH;
    }

    @OneToOne(targetEntity=DaNotificacion.class)
    @JoinColumn(name = "ID_NOTIFICACION", referencedColumnName = "ID_NOTIFICACION")
    public DaNotificacion getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(DaNotificacion idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    @Column(name = "RES_A1", nullable = true, length = 20)
    public String getResA1() {
		return resA1;
	}


	public void setResA1(String resA1) {
		this.resA1 = resA1;
	}

	@Column(name = "RES_A2", nullable = true, length = 20)
	public String getResA2() {
		return resA2;
	}


	public void setResA2(String resA2) {
		this.resA2 = resA2;
	}

   

	@Basic
    @Column(name = "FECHA_DX_VIH")
    public Timestamp getFechaDxVIH() {
		return fechaDxVIH;
	}


	public void setFechaDxVIH(Timestamp fechaDxVIH) {
		this.fechaDxVIH = fechaDxVIH;
	}
	
	

	@Column(name = "EMBARAZO", nullable = true, length = 20)
	public String getEmbarazo() {
		return embarazo;
	}


	public void setEmbarazo(String embarazo) {
		this.embarazo = embarazo;
	}

	@Column(name = "ESTADO", nullable = true, length = 20)
	public String getEstadoPx() {
		return estadoPx;
	}


	public void setEstadoPx(String estadoPx) {
		this.estadoPx = estadoPx;
	}

	@Column(name = "INF_OPORT", nullable = true, length = 20)
	public String getInfOport() {
		return infOport;
	}


	public void setInfOport(String infOport) {
		this.infOport = infOport;
	}

	@Column(name = "TRATAMIENTO", nullable = true, length = 20)
	public String getEstaTx() {
		return estaTx;
	}


	public void setEstaTx(String estaTx) {
		this.estaTx = estaTx;
	}
	
	@Column(name = "FECHA_TAR", nullable = true)
	public Timestamp getFechaTAR() {
		return fechaTAR;
	}


	public void setFechaTAR(Timestamp fechaTAR) {
		this.fechaTAR = fechaTAR;
	}

	@Column(name = "EXP_PERI", nullable = true, length = 20)
	public String getExposicionPeri() {
		return exposicionPeri;
	}


	public void setExposicionPeri(String exposicionPeri) {
		this.exposicionPeri = exposicionPeri;
	}

	@Column(name = "CESAREA", nullable = true, length = 20)
	public String getCesarea() {
		return cesarea;
	}


	public void setCesarea(String cesarea) {
		this.cesarea = cesarea;
	}


	@Override
    public boolean isFieldAuditable(String fieldname) {
        if (fieldname.matches("idDatosVIH") || fieldname.matches("idNotificacion"))
            return  false;
        else
            return true;
    }

    @Override
    @Transient
    public String getActor(){
        return this.actor;
    }

    @Override
    public void setActor(String actor){
        this.actor = actor;
    }

	@Override
    public String toString() {
        return idDatosVIH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaDatosVIH)) return false;

        DaDatosVIH that = (DaDatosVIH) o;

        if (!idDatosVIH.equals(that.idDatosVIH)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return idDatosVIH.hashCode();
    }
}
