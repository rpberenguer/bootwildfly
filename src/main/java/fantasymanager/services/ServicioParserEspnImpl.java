package fantasymanager.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import fantasymanager.data.Equipo;
import fantasymanager.data.Estadistica;
import fantasymanager.data.Jugador;
import fantasymanager.data.Partido;
import fantasymanager.exceptions.FantasyManagerException;
import fantasymanager.exceptions.FantasyManagerParserException;
import fantasymanager.repository.EquipoRepository;
import fantasymanager.repository.EstadisticaRepository;
import fantasymanager.repository.JugadorRepository;
import fantasymanager.repository.PartidoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicioParserEspnImpl implements ServicioParserEspn {

	// private static final Log LOG =
	// LogFactory.getLog(ServicioParserEspnImpl.class);

	private WebClient webClient;

	@Autowired
	private transient EquipoRepository servicioEquipo;

	@Autowired
	private transient JugadorRepository servicioJugador;

	@Autowired
	private transient PartidoRepository servicioPartido;

	@Autowired
	private transient EstadisticaRepository servicioEstadistica;

	// private Map<String, String> posiciones;
	// private static final String ACCION_ADDDROP = "Add/Drop";
	// private static final String ACCION_DRAFT = "Draft";
	// private static final String ACCION_WAIVER = "Waiver";
	// private static final String FECHA_INICIO_TRANSACCIONES = "20091023";

	// private final List<Jugador> jugadoresLibres = new ArrayList<Jugador>();

	// private static final String URL_ESPN =
	// "http://games.espn.go.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=103454";

	// private static final String URL_SCOREBOARD =
	// "http://www.espn.com/nba/scoreboard/_/date/20161031";
	private static final String URL_SCHEDULE = "http://www.espn.com/nba/schedule/_/date/";
	private static final String URL_BOXSCORE_GAME = "http://espn.go.com/nba/boxscore?gameId=";
	private static final String URL_TEAMS = "http://espn.go.com/nba/teams";
	private static final String URL_TEAM = "http://www.espn.com/nba/team/_/name/";
	private static final String URL_TEAM_SHORT = "/nba/team/_/name/";
	private static final String URL_ROSTER = "http://www.espn.com/nba/team/roster/_/name/";
	private static final String URL_PLAYER_ID = "nba/player/_/id/";

	private static final String POSTPONED = "POSTPONED";

	// private final SimpleDateFormat sm = new SimpleDateFormat("EEEE, MMMM dd
	// yyyy", Locale.US);

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@SuppressWarnings("unchecked")
	public void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws FantasyManagerParserException {
		try {
			final HtmlPage scoreBoardPage = webClient.getPage(URL_SCHEDULE + dateTimeFrom.format(formatter));

			// DateTime dateTimeFrom = formatter.parseDateTime(gameDateFrom);
			// final DateTime dateTimeTo = formatter.parseDateTime(gameDateTo);

			// Lista de partidos de toda una semana entera
			final List<HtmlTable> partidosSemanaHtml = (List<HtmlTable>) scoreBoardPage
					.getByXPath("//table[@class='schedule has-team-logos align-left']");

			// Cada elemento es una tabla con los partidos de un dia
			for (final HtmlTable partidosDiaHtml : partidosSemanaHtml) {

				// INI partidos de un dia
				final List<HtmlTableRow> partidosHtml = partidosDiaHtml.getBodies().get(0).getRows();

				for (final HtmlTableRow partidoHtml : partidosHtml) {
					getMatchStatistics(dateTimeFrom, partidoHtml);
				}
				// FIN partidos de un dia

				// Sumamos un día a la fecha inicial
				dateTimeFrom = dateTimeFrom.plusDays(1);

				// Si hemos superado la fecha fin hemos llegado al final del
				// parseo
				if (dateTimeFrom.isAfter(dateTimeTo)) {
					log.info("Fin del parseo!!");
					return;
				}
			}

			// Si hemos llegado hasta aqui no hemos llegado a la fecha fin
			// pero hemos finalizado la lista de partidos de una semana
			// por lo que volvemos a cargar pagina, nos llamamos recursivamente
			// con nueva fecha inicial
			getStatistics(dateTimeFrom, dateTimeTo);

		} catch (final MalformedURLException mue) {
			log.info("Error al obtener las estadisticas: " + mue.getMessage());
		} catch (final IOException ioe) {
			log.info("Error al obtener las estadisticas: " + ioe.getMessage());
		} catch (final FantasyManagerException e) {
			log.info("Error al obtener las estadisticas: " + e.getMessage());
		}
	}

	/**
	 * @param dateTime
	 * @param partidoHtmlRow
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FantasyManagerException
	 */
	private void getMatchStatistics(final LocalDate dateTime, final HtmlTableRow partidoHtmlRow)
			throws IOException, MalformedURLException, FantasyManagerException {

		if (partidoHtmlRow.getCells().size() >= 3) {
			final HtmlTableCell resultCell = partidoHtmlRow.getCell(2);
			final HtmlAnchor resultLink = (HtmlAnchor) resultCell.getFirstChild();

			final HtmlAbbreviated abbr = (HtmlAbbreviated) partidoHtmlRow
					.getFirstByXPath(".//abbr[@title='Eastern Conf All-Stars']");

			// Validamos si el partido esta postpuesto o es el AllStar
			if (!POSTPONED.equals(StringUtils.upperCase(resultLink.asText())) && abbr == null) {

				final String gameId = StringUtils.substringAfter(resultLink.getHrefAttribute(), "gameId=");

				Partido partido = new Partido();
				partido.setIdNba(gameId);
				final Date date = Date.from(dateTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
				partido.setFecha(date);

				// *********************************************
				// ****** Obtenemos pagina del partido *********
				// *********************************************
				final HtmlPage gamePage = webClient.getPage(URL_BOXSCORE_GAME + gameId);

				// *********************************************
				// Parseamos informacion del equipo visitante
				// *********************************************
				final HtmlDivision divTeamAway = (HtmlDivision) gamePage.getFirstByXPath("//div[@class='team away']");

				// Resultado visitante
				HtmlDivision scoreDiv = divTeamAway.getFirstByXPath("//div[@class='score icon-font-after']");
				partido.setResEquipoVisitante(Integer.parseInt(scoreDiv.asText()));

				// Equipo visitante
				String teamInfoCod = getTeamInfoCod(divTeamAway);

				final Equipo equipoVisitante = servicioEquipo.findByCodigoCorto(teamInfoCod);
				partido.setEquipoVisitante(equipoVisitante);

				// *****************************************
				// Parseamos informacion del equipo local
				// *****************************************
				final HtmlDivision divTeamHome = (HtmlDivision) gamePage.getFirstByXPath("//div[@class='team home']");

				// Resultado local
				scoreDiv = divTeamHome.getFirstByXPath("//div[@class='score icon-font-before']");
				partido.setResEquipoLocal(Integer.parseInt(scoreDiv.asText()));

				// Equipo local
				teamInfoCod = getTeamInfoCod(divTeamHome);

				final Equipo equipoLocal = servicioEquipo.findByCodigoCorto(teamInfoCod);
				partido.setEquipoLocal(equipoLocal);

				partido = servicioPartido.save(partido);

				log.info(partido.toString());

				// ***************************************************
				// ************ Parseamos Estadisticas Visitantes
				// ***************
				// ***************************************************
				final HtmlDivision divStatisticsTeamAway = (HtmlDivision) gamePage
						.getFirstByXPath(".//div[@class='col column-one gamepackage-away-wrap']");
				final HtmlTable tableStatisticsTeamAway = (HtmlTable) divStatisticsTeamAway
						.getFirstByXPath(".//table[@class='mod-data']");

				for (final HtmlTableBody htmlTableBody : tableStatisticsTeamAway.getBodies()) {
					final List<HtmlTableRow> trPlayers = htmlTableBody.getRows();

					for (final HtmlTableRow htmlTableRow : trPlayers) {
						if (!"highlight".equals(htmlTableRow.getAttribute("class"))) {

							Estadistica estadistica = getPlayerStatistic(htmlTableRow);
							if (estadistica != null) {
								estadistica.setPartido(partido);

								estadistica = servicioEstadistica.save(estadistica);

								log.info(estadistica.toString());
							}
						}
					}
				}

				// ***************************************************
				// ************ Parseamos Estadisticas Locales ***************
				// ***************************************************
				final HtmlDivision divStatisticsTeamHome = (HtmlDivision) gamePage
						.getFirstByXPath(".//div[@class='col column-two gamepackage-home-wrap']");
				final HtmlTable tableStatisticsTeamHome = (HtmlTable) divStatisticsTeamHome
						.getFirstByXPath(".//table[@class='mod-data']");

				for (final HtmlTableBody htmlTableBody : tableStatisticsTeamHome.getBodies()) {
					final List<HtmlTableRow> trPlayers = htmlTableBody.getRows();

					for (final HtmlTableRow htmlTableRow : trPlayers) {
						if (!"highlight".equals(htmlTableRow.getAttribute("class"))) {

							Estadistica estadistica = getPlayerStatistic(htmlTableRow);
							if (estadistica != null) {
								estadistica.setPartido(partido);

								estadistica = servicioEstadistica.save(estadistica);

								log.info(estadistica.toString());
							}
						}
					}
				}
			}
		}
	}

	private Estadistica getPlayerStatistic(final HtmlTableRow htmlTableRow) throws FantasyManagerException {
		final Estadistica estadistica = new Estadistica();

		// Obtenemos jugador
		final HtmlAnchor playerAnchor = (HtmlAnchor) htmlTableRow.getCell(0).getFirstChild();

		final String playerId = StringUtils.substringBetween(playerAnchor.getHrefAttribute(), URL_PLAYER_ID, "/");
		// if (playerId == null) {
		log.info("playerId: " + playerId);
		// }
		Jugador jugador = servicioJugador.findJugadorByIdNba(playerId);
		// Jugador jugador = null;

		if (jugador == null) {
			jugador = new Jugador();
			jugador.setIdNba(playerId);
			jugador.setNombre(playerAnchor.getFirstChild().asText());
			jugador = servicioJugador.save(jugador);
		}

		estadistica.setJugador(jugador);

		final HtmlTableCell cell = htmlTableRow.getCell(1);
		if (!"dnp".equals(cell.getAttribute("class")) && NumberUtils.isNumber(cell.asText())) {

			// Obtenemos minutos
			final String minutos = cell.asText();

			final String[] tirosTotales = htmlTableRow.getCell(2).getTextContent().split("-");
			final String[] tiros3 = htmlTableRow.getCell(3).getTextContent().split("-");
			final String[] tirosLibres = htmlTableRow.getCell(4).getTextContent().split("-");

			Integer tirosTotAnotados = null;
			Integer tirosTotRealizados = null;
			if (tirosTotales.length == 2) {
				tirosTotAnotados = tirosTotales[0] != null && !"".equals(tirosTotales[0])
						? Integer.parseInt(tirosTotales[0]) : 0;
				tirosTotRealizados = tirosTotales[1] != null && !"".equals(tirosTotales[1])
						? Integer.parseInt(tirosTotales[1]) : 0;
			}

			Integer tiros3Anotados = null;
			Integer tiros3Realizados = null;
			if (tiros3.length == 2) {
				tiros3Anotados = tiros3[0] != null && !"".equals(tiros3[0]) ? Integer.parseInt(tiros3[0]) : 0;
				tiros3Realizados = tiros3[1] != null && !"".equals(tiros3[1]) ? Integer.parseInt(tiros3[1]) : 0;
			}

			final Integer tiros2Anotados = tirosTotAnotados - tiros3Anotados;
			final Integer tiros2Realizados = tirosTotRealizados - tiros3Realizados;

			Integer tirosLibresAnotados = new Integer(0);
			Integer tirosLibresRealizados = new Integer(0);
			if (tirosLibres.length == 2) {
				tirosLibresAnotados = tirosLibres[0] != null && !"".equals(tirosLibres[0])
						? Integer.parseInt(tirosLibres[0]) : 0;
				tirosLibresRealizados = tirosLibres[1] != null && !"".equals(tirosLibres[1])
						? Integer.parseInt(tirosLibres[1]) : 0;
			}

			final String rebotes = htmlTableRow.getCell(7).getTextContent();
			final String asistencias = htmlTableRow.getCell(8).getTextContent();
			final String robos = htmlTableRow.getCell(9).getTextContent();
			final String tapones = htmlTableRow.getCell(10).getTextContent();
			final String perdidas = htmlTableRow.getCell(11).getTextContent();
			final String faltas = htmlTableRow.getCell(12).getTextContent();
			final String puntos = htmlTableRow.getCell(14).getTextContent();

			estadistica.setMinutos(minutos);
			estadistica.setTiros2Anotados(tiros2Anotados);
			estadistica.setTiros2Realizados(tiros2Realizados);
			estadistica.setTiros3Anotados(tiros3Anotados);
			estadistica.setTiros3Realizados(tiros3Realizados);
			estadistica.setTirosLibresAnotados(tirosLibresAnotados);
			estadistica.setTirosLibresRealizados(tirosLibresRealizados);
			estadistica.setRebotes(rebotes != null && !"".equals(rebotes) ? Integer.parseInt(rebotes) : 0);
			estadistica
					.setAsistencias(asistencias != null && !"".equals(asistencias) ? Integer.parseInt(asistencias) : 0);
			estadistica.setFaltas(faltas != null && !"".equals(faltas) ? Integer.parseInt(faltas) : 0);
			estadistica.setRobos(robos != null && !"".equals(robos) ? Integer.parseInt(robos) : 0);
			estadistica.setPerdidas(perdidas != null && !"".equals(perdidas) ? Integer.parseInt(perdidas) : 0);
			estadistica.setTapones(tapones != null && !"".equals(tapones) ? Integer.parseInt(tapones) : 0);
			estadistica.setPuntos(puntos != null && !"".equals(puntos) ? Integer.parseInt(puntos) : 0);

			estadistica.setValoracion(estadistica.calculoValoracion());

			return estadistica;
		} else {
			return null;
		}
	}

	/**
	 * @param divTeam
	 * @return
	 */
	private String getTeamInfoCod(final HtmlDivision divTeam) {

		final HtmlDivision teamInfoDiv = divTeam.getFirstByXPath(".//div[@class='team-info']");
		final DomNode teamInfoNode = teamInfoDiv.getFirstChild();
		String teamInfoCod = "";
		if (teamInfoNode instanceof HtmlAnchor) {
			final HtmlAnchor teamInfoA = (HtmlAnchor) teamInfoNode;
			teamInfoCod = StringUtils.substringBetween(teamInfoA.getHrefAttribute(), URL_TEAM_SHORT, "/");
		} else if (teamInfoNode instanceof HtmlDivision) {
			final HtmlSpan teamInfoSpan = teamInfoNode.getFirstByXPath(".//span[@class='abbrev']");
			teamInfoCod = StringUtils.lowerCase(teamInfoSpan.asText());
		}

		return teamInfoCod;
	}

	@Override
	public void getRosters() throws FantasyManagerParserException, FantasyManagerException {
		try {
			final HtmlPage teamsPage = webClient.getPage(URL_TEAMS);
			@SuppressWarnings("unchecked")
			final List<HtmlAnchor> equiposHtml = (List<HtmlAnchor>) teamsPage.getByXPath("//a[@class='bi']");
			for (final HtmlAnchor equipoHtml : equiposHtml) {
				final String codigo = StringUtils.substringAfter(equipoHtml.getHrefAttribute(), URL_TEAM);
				final String codigoCorto = codigo.split("/")[0];
				final String codigoLargo = codigo.split("/")[1];

				final Equipo equipo = new Equipo();
				equipo.setNombre(equipoHtml.getTextContent());
				equipo.setCodigoCorto(codigoCorto);
				equipo.setCodigoLargo(codigoLargo);

				final Equipo newEquipo = servicioEquipo.save(equipo);

				// Parseamos roster
				final HtmlPage rosterPage = webClient.getPage(URL_ROSTER + codigoCorto);
				final HtmlTable rosterTableHtml = rosterPage.getFirstByXPath("//table[@class='tablehead']");
				final List<HtmlTableRow> rowsHtml = rosterTableHtml.getBodies().get(0).getRows();

				for (final HtmlTableRow htmlTableRow : rowsHtml) {
					final String attributeClass = htmlTableRow.getAttribute("class");
					if (StringUtils.contains(attributeClass, "player")) {

						final Jugador jugador = new Jugador();

						final HtmlAnchor a = (HtmlAnchor) htmlTableRow.getCell(1).getFirstChild();
						jugador.setNombre(a.asText());

						final String idNba = StringUtils.substringBetween(a.getHrefAttribute(), URL_PLAYER_ID, "/");
						jugador.setIdNba(idNba);

						jugador.setEquipo(newEquipo);

						servicioJugador.save(jugador);
					}
				}

				log.info("Equipo=" + codigoCorto + "/" + codigoLargo);
			}

			log.info("Parseo de equipos OK");

		} catch (final MalformedURLException mue) {
			log.info("Error al obtener los equipos");
		} catch (final IOException ioe) {
			log.info("Error al obtener los equipos");
		} catch (final Exception e) {
			throw new FantasyManagerException("Se ha producido un error al guardar el equipo.", e);
		}
	}

	@PostConstruct
	private void postConstruct() {
		webClient = new WebClient(BrowserVersion.FIREFOX_3);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setJavaScriptEnabled(false);
	}

}
