package fantasymanager.controller;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fantasymanager.data.Partido;
import fantasymanager.exceptions.FantasyManagerParserException;
import fantasymanager.repository.PartidoRepository;
import fantasymanager.services.ServicioParserEspnImpl;

@RestController
public class HomeController {

	@Autowired
	private ServicioParserEspnImpl service;

	@Autowired
	private PartidoRepository repository;

	@GetMapping("/hello")
	public String test2() {
		return "Hola FantasyManager!!!!!!!!!!";
	}

	@GetMapping("/partidos/{id}")
	public Partido getGame(@PathVariable("id") Integer id) {

		return repository.findOne(id);
	}

	@PostMapping(value = "/parser")
	public String parser(@RequestBody String dateIni) {
		try {
			// log.debug("***** Inicioooo del TEST *****");
			final LocalDate yesterday = LocalDate.of(2018, Month.JANUARY, 29);
			service.getStatistics(yesterday, yesterday);
			// log.debug("***** Fin del TEST *****");

		} catch (final FantasyManagerParserException e) {
			e.printStackTrace();
		}

		return "Parseo OK";
	}
}
