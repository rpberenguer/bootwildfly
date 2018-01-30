package fantasymanager.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fantasymanager.data.Estadistica;
import fantasymanager.data.dto.StatisticDto;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Integer> {

	@Query("SELECT new fantasymanager.data.dto.StatisticDto (j.nombre as nombre, avg(e.valoracion) as avg, sum(e.valoracion) as tot, avg(e.puntos) as avgPuntos,"
			+ " avg(e.rebotes) as avgRebotes, avg(e.asistencias) as avgAsistencias, avg(e.robos) as avgRobos, avg(e.tapones) as avgTapones,"
			+ " avg(e.perdidas) as avgPerdidas) FROM Estadistica e JOIN e.jugador j JOIN e.partido p"
			+ " WHERE p.fecha >= ?1 AND p.fecha <= ?2 GROUP BY j.nombre ORDER BY avg(e.valoracion) DESC")
	List<StatisticDto> findAvgAndSumStatisticsByPlayer(Date fechaIni, Date fechaFin);

	@Query("SELECT new fantasymanager.data.dto.StatisticDto (j.nombre as nombre, avg(e.valoracion) as avg, sum(e.valoracion) as tot, avg(e.puntos) as avgPuntos,"
			+ " avg(e.rebotes) as avgRebotes, avg(e.asistencias) as avgAsistencias, avg(e.robos) as avgRobos, avg(e.tapones) as avgTapones,"
			+ " avg(e.perdidas) as avgPerdidas) FROM Estadistica e JOIN e.jugador j JOIN e.partido p"
			+ " WHERE LOWER(j.nombre) like LOWER(concat('%', ?1, '%')) AND p.fecha >= ?2 AND p.fecha <= ?3"
			+ " GROUP BY j.nombre ORDER BY avg(e.valoracion) DESC")
	List<StatisticDto> findAvgAndSumStatisticsByPlayer(String nameFilter, Date fechaIni, Date fechaFin);

	@Query("SELECT e FROM Estadistica e JOIN e.jugador j JOIN e.partido p"
			+ " WHERE j.nombre = ?1 AND p.fecha >= ?2 AND p.fecha <= ?3 ORDER BY p.fecha DESC")
	List<Estadistica> findStatisticsByPlayer(String nombre, Date fechaIni, Date fechaFin);

}