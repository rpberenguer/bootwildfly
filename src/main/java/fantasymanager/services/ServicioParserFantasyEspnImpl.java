package fantasymanager.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import fantasymanager.data.Jugador;
import fantasymanager.exceptions.FantasyManagerParserException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Deprecated
public class ServicioParserFantasyEspnImpl implements ServicioParserFantasyEspn {

	private Map<String, String> posiciones;
	private WebClient webClient;

	private static final String ACCION_ADDDROP = "Add/Drop";
	private static final String ACCION_DRAFT = "Draft";
	private static final String ACCION_WAIVER = "Waiver";

	private static final String FECHA_INICIO_TRANSACCIONES = "20091023";

	private final List<Jugador> jugadoresLibres = new ArrayList<Jugador>();

	private static final String URL_ESPN = "http://games.espn.go.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=97189";
	// private static final String URL_ESPN =
	// "http://games.espn.go.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=103454";

	@Override
	public List<Jugador> getJugadoresLibres() throws FantasyManagerParserException {
		try {
			// Login
			final HtmlPage leaguePage = login();

			// Players
			final HtmlDivision div = (HtmlDivision) leaguePage.getByXPath("//div[@class='fantasynavtab playerstab']")
					.get(0);
			final HtmlAnchor linkPlayers = (HtmlAnchor) div.getFirstChild();
			HtmlPage freeAgentsPage = linkPlayers.click();

			final URL urlFreeAgents = freeAgentsPage.getWebResponse().getRequestSettings().getUrl();
			HtmlDivision divPagination = null;
			String onClickNext = null;
			String nextPlayers = null;

			getJugadoresLibresPorPagina(freeAgentsPage);
			divPagination = (HtmlDivision) freeAgentsPage.getByXPath("//div[@class='paginationNav']").get(0);

			while (divPagination != null) {
				final int size = divPagination.getChildNodes().getLength();
				HtmlAnchor next = null;

				if (size == 1) {
					next = (HtmlAnchor) divPagination.getFirstChild();
					if (next != null && next.getTextContent().contains("PREVIOUS")) {
						break;
					}
				} else {
					next = (HtmlAnchor) divPagination.getLastChild();
				}

				onClickNext = next.getOnClickAttribute();
				nextPlayers = onClickNext.substring("players('leagueId=103454&teamId=1".length(),
						onClickNext.indexOf("'); return false;"));

				freeAgentsPage = webClient.getPage(urlFreeAgents + nextPlayers);
				getJugadoresLibresPorPagina(freeAgentsPage);
				divPagination = (HtmlDivision) freeAgentsPage.getByXPath("//div[@class='paginationNav']").get(0);
			}

			return jugadoresLibres;
		} catch (final MalformedURLException mue) {
			return null;
		} catch (final IOException ioe) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private void getJugadoresLibresPorPagina(HtmlPage freeAgentsPage) {
		List<HtmlTableDataCell> cellsPlayers = null;
		Jugador jugador = null;

		cellsPlayers = (List<HtmlTableDataCell>) freeAgentsPage.getByXPath("//td[@class='playertablePlayerName']");

		for (final HtmlTableDataCell cell : cellsPlayers) {
			final HtmlAnchor linkJugador = (HtmlAnchor) cell.getFirstChild();

			jugador = new Jugador();
			jugador.setNombre(linkJugador.asText());
			jugador.setUrlTransaccion(freeAgentsPage.getWebResponse().getRequestSettings().getUrl().toString());

			// System.out.println("Jugador="+linkJugador.asText());
			// System.out.println("urlJugador="+freeAgentsPage.getWebResponse().getRequestSettings().getUrl());

			jugadoresLibres.add(jugador);
		}

	}

	private HtmlPage login() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		final HtmlPage espnPage = webClient.getPage(URL_ESPN);

		try {
			final HtmlForm loginForm = espnPage.getFormByName("loginForm");
			final HtmlTextInput username = loginForm.getInputByName("username");
			final HtmlPasswordInput password = loginForm.getInputByName("password");
			final HtmlSubmitInput submitBtn = loginForm.getInputByName("submit");

			// Pagina principal de la liga
			username.setValueAttribute("bravo fisico");
			password.setValueAttribute("8ad3aah4");
			return submitBtn.click();
		} catch (final ElementNotFoundException enfe) {
			// Ya estamos logueados
			return espnPage;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fichajeEspn(Jugador jugadorAFichar, Jugador jugadorAVender) throws FantasyManagerParserException {
		try {
			final URL url = new URL(jugadorAFichar.getUrlTransaccion());
			final HtmlPage page = webClient.getPage(url);
			final List<HtmlTableDataCell> cellsJugadores = (List<HtmlTableDataCell>) page
					.getByXPath("//td[@class='playertablePlayerName']");

			boolean encontrado = false;
			HtmlAnchor linkJugador = null;
			HtmlAnchor linkTransaccion = null;
			HtmlTableRow rowJugador = null;
			HtmlTableCell cellTransaccion = null;

			for (final HtmlTableDataCell cell : cellsJugadores) {
				linkJugador = (HtmlAnchor) cell.getFirstChild();

				if (linkJugador.asText().equals(jugadorAFichar.getNombre())) {
					rowJugador = cell.getEnclosingRow();
					cellTransaccion = rowJugador.getCell(3);
					linkTransaccion = (HtmlAnchor) cellTransaccion.getFirstChild();
					encontrado = true;
					break;
				}
			}

			if (encontrado) {
				// Adjust Roster
				final HtmlPage adjustRoster = linkTransaccion.click();
				final HtmlCheckBoxInput check = getCheckJugadorAVender(adjustRoster, jugadorAVender);
				check.setChecked(true);

				final HtmlForm formAdjustRoster = adjustRoster.getFormByName("playerTableFramedForm");
				final HtmlButtonInput button = (HtmlButtonInput) formAdjustRoster.getInputByName("btnSubmit");

				// Confirm Page
				final HtmlPage confirmPage = (HtmlPage) button.click();
				final HtmlForm confirmForm = confirmPage.getFormByName("confirmForm");
				final HtmlSubmitInput confirmBtn = confirmForm.getInputByName("confirmBtn");
				confirmBtn.click();

				System.out.println("Fichaje OK!");
			} else {
				System.out.println("Jugador no encontrado");
			}

		} catch (final Exception e) {
			throw new FantasyManagerParserException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private HtmlCheckBoxInput getCheckJugadorAVender(HtmlPage adjustRoster, Jugador jugadorAVender) {
		List<HtmlTableDataCell> cells = null;
		cells = (List<HtmlTableDataCell>) adjustRoster.getByXPath("//td[@class='playertableCheckbox']");
		HtmlAnchor linkJugador = null;
		HtmlTableRow rowJugador = null;

		for (final HtmlTableDataCell cell : cells) {
			if (cell.getFirstChild() != null) {
				rowJugador = cell.getEnclosingRow();
				linkJugador = (HtmlAnchor) rowJugador.getCell(2).getFirstChild();

				System.out.println("Check Jugador=" + linkJugador.asText());

				if (linkJugador.asText().equals(jugadorAVender.getNombre())) {
					final HtmlCheckBoxInput check = (HtmlCheckBoxInput) cell.getFirstChild();
					return check;
				}
			}
		}

		return null;
	}

	private List<Jugador> getJugadoresMiEquipo(HtmlPage page) throws Exception {
		final HtmlTable table = page.getHtmlElementById("playertable_0");

		if (table != null) {
			Boolean titular = true;
			final List<Jugador> jugadoresMiEquipo = new ArrayList<Jugador>();

			for (final HtmlTableRow row : table.getRows()) {
				if (row.getAttribute("class").contains("playertableSectionHeader")) {
					final List<HtmlTableHeaderCell> htmlTableHeaderCells = row.getElementsByAttribute(
							HtmlTableHeaderCell.TAG_NAME, "class", "playertableSectionHeaderFirst");

					if (htmlTableHeaderCells != null && !htmlTableHeaderCells.isEmpty()) {
						final HtmlTableHeaderCell th = htmlTableHeaderCells.get(0);

						if (th != null && th.asText().equals("BENCH")) {
							titular = false;
						}
					}
				} else if (row.getId() != null && row.getId().startsWith("pncPlayerRow")) {
					Jugador jugador = null;
					// Posicion posicion = null;

					final String playerId = row.getId().replace("pncPlayerRow_", "");
					final HtmlTableDataCell td = row.getElementById("pncSlot_" + playerId);

					if (td != null) {
						final String pos = posiciones.get(td.asText().trim().toUpperCase());

						if (pos != null) {
							// posicion = new Posicion(pos);
						}
					}

					final List<HtmlTableDataCell> htmlTableDataCells = row
							.getElementsByAttribute(HtmlTableDataCell.TAG_NAME, "class", "playertablePlayerName");

					// Si en el siguiente bloque if no conseguimos encontrar el
					// nombre del jugador ignoraremos toda la row
					if (htmlTableDataCells != null && !htmlTableDataCells.isEmpty()) {
						final List<HtmlAnchor> htmlAnchors = htmlTableDataCells.get(0)
								.getElementsByAttribute(HtmlAnchor.TAG_NAME, "class", "popplayercard");

						if (htmlAnchors != null && !htmlAnchors.isEmpty()) {
							final String nombre = htmlAnchors.get(0).asText().trim();

							if (!nombre.equals("")) {
								jugador = new Jugador();
								jugador.setNombre(nombre);
								jugador.setTitular(titular);

								// if (posicion != null) {
								// jugador.getPosiciones().add(posicion);
								// posicion.getJugadores().add(jugador);
								// }

								jugadoresMiEquipo.add(jugador);
							}
						}
					}
				}
			}

			return jugadoresMiEquipo;
		}

		return null;
	}

	@PostConstruct
	@SuppressWarnings("unused")
	private void postConstruct() {

		webClient = new WebClient(BrowserVersion.FIREFOX_3);
		webClient.setThrowExceptionOnScriptError(false);
	}
}
