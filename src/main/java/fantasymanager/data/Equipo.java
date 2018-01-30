package fantasymanager.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Equipo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pkid;

	@Column(nullable = false, unique = true, length = 50)
	private String nombre;

	@Column(name = "codigo_corto", unique = true, length = 3)
	private String codigoCorto;

	@Column(name = "codigo_largo", unique = true, length = 50)
	private String codigoLargo;

	// @OneToMany(mappedBy = "equipo")
	// private List<Jugador> jugadores;

	public Equipo() {
	}

	public Equipo(final String nombre) {
		setNombre(nombre);
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

	public String getCodigoCorto() {
		return codigoCorto;
	}

	public void setCodigoCorto(final String codigoCorto) {
		this.codigoCorto = codigoCorto;
	}

	public String getCodigoLargo() {
		return codigoLargo;
	}

	public void setCodigoLargo(final String codigoLargo) {
		this.codigoLargo = codigoLargo;
	}

	// /**
	// * @return the jugadores
	// */
	// public List<Jugador> getJugadores() {
	// return jugadores;
	// }
	//
	// /**
	// * @param jugadores the jugadores to set
	// */
	// public void setJugadores(List<Jugador> jugadores) {
	// this.jugadores = jugadores;
	// }

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Equipo[");
		sb.append("pkid=").append(pkid).append(", ");
		sb.append("nombre='").append(nombre).append("']");

		return sb.toString();
	}

}
