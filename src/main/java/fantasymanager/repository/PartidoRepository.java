package fantasymanager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasymanager.data.Partido;

public interface PartidoRepository extends CrudRepository<Partido, Integer> {

	List<Partido> findAllByOrderByFechaDesc();
}