package fantasymanager.services;

import java.time.LocalDate;

import fantasymanager.exceptions.FantasyManagerException;
import fantasymanager.exceptions.FantasyManagerParserException;

public interface ServicioParserEspn {

	void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws FantasyManagerParserException;

	void getRosters() throws FantasyManagerParserException, FantasyManagerException;

}
