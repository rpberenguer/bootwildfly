package fantasymanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fantasymanager.data.Partido;
import fantasymanager.data.dto.StatisticRequest;
import fantasymanager.exceptions.FantasyManagerException;
import fantasymanager.exceptions.FantasyManagerParserException;
import fantasymanager.repository.PartidoRepository;
import fantasymanager.services.ServicioParserEspnImpl;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {

	@Autowired
	private ServicioParserEspnImpl service;

	@Autowired
	private PartidoRepository repository;

	@GetMapping("/hello")
	public String test2() {
		return "Hola FantasyManager!!!!!!!!!!";
	}

	@PostMapping("/parser/teams")
	public void getTeams() {

		log.info("Inicio parseo equipos");

		try {
			service.getRosters();
		} catch (final FantasyManagerException e) {
			log.error("Error parseando equipos");
		}

		log.info("Fin parseo equipos");
	}

	@GetMapping("/partidos/{id}")
	public Partido getGame(@PathVariable("id") Integer id) {

		return repository.findOne(id);
	}

	@PostMapping(value = "/parser/statistics")
	public String parser(@RequestBody StatisticRequest request) {
		try {
			log.info("Inicio parseo estadisticas");
			service.getStatistics(request.getStartDate(), request.getEndDate());
			log.info("Fin parseo estadisticas");

		} catch (final FantasyManagerParserException e) {
			e.printStackTrace();
		}

		return "Parseo OK";
	}
}
