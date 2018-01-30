package fantasymanager.data;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Entity
@Component
@Scope(value = "prototype")
public class Partido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pkid;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_equipo_local")
	private Equipo equipoLocal;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_equipo_visitante")
	private Equipo equipoVisitante;

	@Column(name = "res_equipo_local")
	private int resEquipoLocal;

	@Column(name = "res_equipo_visitante")
	private int resEquipoVisitante;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name = "id_nba")
	private String idNba;

	@OneToMany(mappedBy = "partido")
	private List<Estadistica> estadisticas;

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(final Integer pkid) {
		this.pkid = pkid;
	}

	public Equipo getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(final Equipo equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public Equipo getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(final Equipo equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public int getResEquipoLocal() {
		return resEquipoLocal;
	}

	public void setResEquipoLocal(final int resEquipoLocal) {
		this.resEquipoLocal = resEquipoLocal;
	}

	public int getResEquipoVisitante() {
		return resEquipoVisitante;
	}

	public void setResEquipoVisitante(final int resEquipoVisitante) {
		this.resEquipoVisitante = resEquipoVisitante;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}

	public String getIdNba() {
		return idNba;
	}

	public void setIdNba(final String idNba) {
		this.idNba = idNba;
	}

	/**
	 * @return the estadisticas
	 */
	public List<Estadistica> getEstadisticas() {
		return estadisticas;
	}

	/**
	 * @param estadisticas
	 *            the estadisticas to set
	 */
	public void setEstadisticas(List<Estadistica> estadisticas) {
		this.estadisticas = estadisticas;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Partido[");
		sb.append("fecha=").append(fecha).append(", ");
		sb.append("equipo local=").append(equipoLocal.getNombre()).append(", ");
		sb.append("equipo visitante='").append(equipoVisitante.getNombre()).append("']");

		return sb.toString();
	}

}
