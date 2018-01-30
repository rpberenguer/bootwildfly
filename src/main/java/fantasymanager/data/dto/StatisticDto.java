package fantasymanager.data.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class StatisticDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String nombre;

	private Double avg;

	private Double tot;

	private Double avgPuntos;

	private Double avgRebotes;

	private Double avgAsistencias;

	private Double avgRobos;

	private Double avgTapones;

	private Double avgFaltas;

	private Double avgPerdidas;

	public StatisticDto() {
		super();
	}

	public StatisticDto(final String nombre, final Double avg, final Double tot, final Double avgPuntos, final Double avgRebotes,
			final Double avgAsistencias, final Double avgRobos, final Double avgTapones, final Double avgPerdidas) {
		super();
		this.nombre = nombre;
		this.avg = avg;
		this.tot = tot;
		this.avgPuntos = avgPuntos;
		this.avgRebotes = avgRebotes;
		this.avgAsistencias = avgAsistencias;
		this.avgRobos = avgRobos;
		this.avgTapones = avgTapones;
		this.avgPerdidas = avgPerdidas;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the avg
	 */
	public Double getAvg() {
		return avg;
	}

	/**
	 * @param avg
	 *            the avg to set
	 */
	public void setAvg(final Double avg) {
		this.avg = avg;
	}

	/**
	 * @return the tot
	 */
	public Double getTot() {
		return tot;
	}

	/**
	 * @param tot
	 *            the tot to set
	 */
	public void setTot(final Double tot) {
		this.tot = tot;
	}

	/**
	 * @return the avgPuntos
	 */
	public Double getAvgPuntos() {
		return avgPuntos;
	}

	/**
	 * @param avgPuntos the avgPuntos to set
	 */
	public void setAvgPuntos(final Double avgPuntos) {
		this.avgPuntos = avgPuntos;
	}

	/**
	 * @return the avgRebotes
	 */
	public Double getAvgRebotes() {
		return avgRebotes;
	}

	/**
	 * @param avgRebotes the avgRebotes to set
	 */
	public void setAvgRebotes(final Double avgRebotes) {
		this.avgRebotes = avgRebotes;
	}

	/**
	 * @return the avgAsistencias
	 */
	public Double getAvgAsistencias() {
		return avgAsistencias;
	}

	/**
	 * @param avgAsistencias the avgAsistencias to set
	 */
	public void setAvgAsistencias(final Double avgAsistencias) {
		this.avgAsistencias = avgAsistencias;
	}

	/**
	 * @return the avgRobos
	 */
	public Double getAvgRobos() {
		return avgRobos;
	}

	/**
	 * @param avgRobos the avgRobos to set
	 */
	public void setAvgRobos(final Double avgRobos) {
		this.avgRobos = avgRobos;
	}

	/**
	 * @return the avgTapones
	 */
	public Double getAvgTapones() {
		return avgTapones;
	}

	/**
	 * @param avgTapones the avgTapones to set
	 */
	public void setAvgTapones(final Double avgTapones) {
		this.avgTapones = avgTapones;
	}

	/**
	 * @return the avgFaltas
	 */
	public Double getAvgFaltas() {
		return avgFaltas;
	}

	/**
	 * @param avgFaltas the avgFaltas to set
	 */
	public void setAvgFaltas(final Double avgFaltas) {
		this.avgFaltas = avgFaltas;
	}

	/**
	 * @return the avgPerdidas
	 */
	public Double getAvgPerdidas() {
		return avgPerdidas;
	}

	/**
	 * @param avgPerdidas the avgPerdidas to set
	 */
	public void setAvgPerdidas(final Double avgPerdidas) {
		this.avgPerdidas = avgPerdidas;
	}

}
