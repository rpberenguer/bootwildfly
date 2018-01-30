package fantasymanager.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Entity
@Component
@Scope(value = "prototype")
public class Estadistica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pkid;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_jugador")
	private Jugador jugador;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_partido")
	private Partido partido;

	private String minutos;
	private Integer puntos;
	private Integer rebotes;
	private Integer asistencias;
	private Integer robos;
	private Integer tapones;
	private Integer faltas;
	private Integer perdidas;
	private Double valoracion;

	@Column(name = "tiros_2_anotados")
	private Integer tiros2Anotados;

	@Column(name = "tiros_2_realizados")
	private Integer tiros2Realizados;

	@Column(name = "tiros_3_anotados")
	private Integer tiros3Anotados;

	@Column(name = "tiros_3_realizados")
	private Integer tiros3Realizados;

	@Column(name = "tiros_libres_anotados")
	private Integer tirosLibresAnotados;

	@Column(name = "tiros_libres_realizados")
	private Integer tirosLibresRealizados;

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(final Integer pkid) {
		this.pkid = pkid;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(final Jugador jugador) {
		this.jugador = jugador;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(final Partido partido) {
		this.partido = partido;
	}

	public String getMinutos() {
		return minutos;
	}

	public void setMinutos(final String minutos) {
		this.minutos = minutos;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(final Integer puntos) {
		this.puntos = puntos;
	}

	public Integer getRebotes() {
		return rebotes;
	}

	public void setRebotes(final Integer rebotes) {
		this.rebotes = rebotes;
	}

	public Integer getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(final Integer asistencias) {
		this.asistencias = asistencias;
	}

	public Integer getRobos() {
		return robos;
	}

	public void setRobos(final Integer robos) {
		this.robos = robos;
	}

	public Integer getTapones() {
		return tapones;
	}

	public void setTapones(final Integer tapones) {
		this.tapones = tapones;
	}

	public Integer getFaltas() {
		return faltas;
	}

	public void setFaltas(final Integer faltas) {
		this.faltas = faltas;
	}

	public Integer getPerdidas() {
		return perdidas;
	}

	public void setPerdidas(final Integer perdidas) {
		this.perdidas = perdidas;
	}

	public Integer getTiros2Anotados() {
		return tiros2Anotados;
	}

	public void setTiros2Anotados(final Integer tiros2Anotados) {
		this.tiros2Anotados = tiros2Anotados;
	}

	public Integer getTiros2Realizados() {
		return tiros2Realizados;
	}

	public void setTiros2Realizados(final Integer tiros2Realizados) {
		this.tiros2Realizados = tiros2Realizados;
	}

	public Integer getTiros3Anotados() {
		return tiros3Anotados;
	}

	public void setTiros3Anotados(final Integer tiros3Anotados) {
		this.tiros3Anotados = tiros3Anotados;
	}

	public Integer getTiros3Realizados() {
		return tiros3Realizados;
	}

	public void setTiros3Realizados(final Integer tiros3Realizados) {
		this.tiros3Realizados = tiros3Realizados;
	}

	public Integer getTirosLibresAnotados() {
		return tirosLibresAnotados;
	}

	public void setTirosLibresAnotados(final Integer tirosLibresAnotados) {
		this.tirosLibresAnotados = tirosLibresAnotados;
	}

	public Integer getTirosLibresRealizados() {
		return tirosLibresRealizados;
	}

	public void setTirosLibresRealizados(final Integer tirosLibresRealizados) {
		this.tirosLibresRealizados = tirosLibresRealizados;
	}

	public Double getValoracion() {
		return valoracion;
	}

	public void setValoracion(final Double valoracion) {
		this.valoracion = valoracion;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Estadistica[");
		sb.append(jugador).append(", ");
		sb.append("minutos=").append(minutos).append(", ");
		sb.append("puntos=").append(puntos).append(", ");
		sb.append("rebotes=").append(rebotes).append(", ");
		sb.append("asistencias=").append(asistencias).append(", ");
		sb.append("valoracion=").append(valoracion).append("]");

		return sb.toString();
	}

	public Double calculoValoracion() {
		final Integer tiros2Fallados = tiros2Realizados - tiros2Anotados;
		final Integer tiros3Fallados = tiros3Realizados - tiros3Anotados;
		final Integer tirosLibresFallados = tirosLibresRealizados - tirosLibresAnotados;

		final Double valoracion = new Double(puntos + rebotes + asistencias + robos + tapones - perdidas
				- tiros2Fallados - tiros3Fallados * 1.5 - tirosLibresFallados * 0.5);

		return valoracion;
	}

}