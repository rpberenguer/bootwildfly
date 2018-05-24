package fantasymanager.services;

import java.util.List;

import fantasymanager.data.Jugador;
import fantasymanager.exceptions.FantasyManagerParserException;

public interface ServicioParserFantasyEspn {

	List<Jugador> getJugadoresLibres() throws FantasyManagerParserException;

	void fichajeEspn(Jugador jugadorAFichar, Jugador jugadorAVender) throws FantasyManagerParserException;

}
