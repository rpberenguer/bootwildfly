package fantasymanager.repository;

import org.springframework.data.repository.CrudRepository;

import fantasymanager.data.Jugador;

public interface JugadorRepository extends CrudRepository<Jugador, Integer> {

	Jugador findJugadorByIdNba(String idNba);

	Jugador findJugadorByNombre(String playerName);
}