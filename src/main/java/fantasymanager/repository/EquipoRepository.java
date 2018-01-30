package fantasymanager.repository;

import org.springframework.data.repository.CrudRepository;

import fantasymanager.data.Equipo;

public interface EquipoRepository extends CrudRepository<Equipo, Integer> {

	Equipo findByCodigoCorto(String codigoCorto);
}