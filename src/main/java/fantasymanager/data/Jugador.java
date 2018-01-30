package fantasymanager.data;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Entity
@Component
@Scope(value = "prototype")
public class Jugador implements Serializable {
	private static final long serialVersionUID = 3175982358364460153L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pkid;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(name = "id_nba")
	private String idNba;

	private Double altura, peso;

	@Column(name = "fecha_nacimiento")
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;

	@Column(name = "anyos_profesional")
	private Integer añosProfesional;

	@Column(name = "url_detalle", length = 100, unique = true)
	private String urlDetalle;

	@Transient
	private String urlTransaccion;

	@Transient
	private Boolean titular;

	// @ManyToMany(cascade = CascadeType.MERGE)
	// @JoinTable(joinColumns = @JoinColumn(name = "id_jugador",
	// referencedColumnName = "pkid"), inverseJoinColumns = @JoinColumn(name =
	// "id_posicion", referencedColumnName = "pkid"))
	// private List<Posicion> posiciones;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_equipo")
	private Equipo equipo;

	@OneToMany(mappedBy = "jugador")
	private List<Estadistica> estadisticas;

	// @ManyToOne(cascade = CascadeType.MERGE)
	// @JoinColumn(name = "id_equipo_fantasy")
	// private EquipoFantasy equipoFantasy;

	public Jugador() {
	}

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(final Integer pkid) {
		this.pkid = pkid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public String getIdNba() {
		return idNba;
	}

	public void setIdNba(final String idNba) {
		this.idNba = idNba;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(final Double altura) {
		this.altura = altura;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(final Double peso) {
		this.peso = peso;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(final Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Integer getAñosProfesional() {
		return añosProfesional;
	}

	public void setAñosProfesional(final Integer añosProfesional) {
		this.añosProfesional = añosProfesional;
	}

	// public List<Posicion> getPosiciones() {
	// if (posiciones == null) {
	// posiciones = new ArrayList<Posicion>();
	// }
	//
	// return posiciones;
	// }
	//
	// public void setPosiciones(final List<Posicion> posiciones) {
	// this.posiciones = posiciones;
	// }

	public String getUrlDetalle() {
		return urlDetalle;
	}

	public void setUrlDetalle(final String urlDetalle) {
		this.urlDetalle = urlDetalle;
	}

	public String getUrlTransaccion() {
		return urlTransaccion;
	}

	public void setUrlTransaccion(final String urlTransaccion) {
		this.urlTransaccion = urlTransaccion;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(final Equipo equipo) {
		this.equipo = equipo;
	}

	// public EquipoFantasy getEquipoFantasy() {
	// return equipoFantasy;
	// }
	//
	// public void setEquipoFantasy(final EquipoFantasy equipoFantasy) {
	// this.equipoFantasy = equipoFantasy;
	// }

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

	public Boolean isTitular() {
		return titular;
	}

	public void setTitular(final Boolean titular) {
		this.titular = titular;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Jugador[");
		sb.append("nombre=").append(nombre != null ? "'" + nombre + "'" : nombre).append(", ");
		sb.append("idNba=").append(idNba != null ? "'" + idNba + "'" : idNba).append("]");

		return sb.toString();
	}
}
