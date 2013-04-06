/**
 * 
 */
package org.maharshak.teg.server.infra;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.backoffice.DefinitionManager;
import org.maharshak.teg.backoffice.IGameRunner;
import org.maharshak.teg.backoffice.ai.AiObserver;
import org.maharshak.teg.common.Common;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.GameConfiguration;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.GameManager;
import org.maharshak.teg.game.request.PlayerRequest;
import org.maharshak.teg.game.request.StartGameRequest;
import org.maharshak.teg.player.ChipsColor;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.server.Console;
import org.maharshak.teg.server.Helper;
import org.maharshak.teg.server.ai.AiManager;
import org.maharshak.teg.server.game.ConcoleObserver;
import org.maharshak.teg.server.game.PlayerManagerOldTegObserver;
import org.maharshak.teg.server.request.RequestConteiner;



/**
 * @author uztnus
 *
 */
public class Server {

	private Logger _log = Logger.getLogger("tegserver");

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("conf/log4j.properties");
		Server s = new Server();
		configureUsingArguments(s, args);
		s.start();
	}

	public void start() throws IOException {
		_log.info("Startign server : " + toString());
		Net.createSelector();
		Net.createServerSocket(_hostName, _port);
		_log.info("Server socket created on " + _hostName + ":" + _port);
		System.out.println("Server started on " + _hostName + ":" + _port);
		_gameRunner.init();
		while (true) {
			try {
				Iterator<SelectionKey> it = Net.listen(100);
				while (it.hasNext()) {
					// Get the selection key
					SelectionKey selKey = it.next();
					// Remove it from the list to indicate that it is being
					// processed
					it.remove();
					if (!selKey.isValid()) {
						continue;
					}
					// Check what event is available and deal with it
					if (selKey.isAcceptable()) {
						Net.accept(selKey);
					} else if (selKey.isReadable()) {
						read(selKey);
					}
				}
			} catch (Exception e) {
				_log.error("Erro in main loop", e);
				System.err.println("Erro in main loop " + e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TegProtocolOneGameServer [" + _hostName + "| " + _port + "]";
	}

	private int _port;
	private String _hostName;

	private IGameRunner _gameRunner;
	private RequestCreator _reqCreator;
	private DefinitionManager _definitionManager;

	public void setPort(int port) {
		_port = port;

	}

	public Server() {
		_log.info(String.format("%s v0.1 server - by Roman Magarshak\n\n",
				Common.TEG_NAME));
		setPort(Common.TEG_DEFAULT_PORT);
		try {
			setName(InetAddress.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
			_log.fatal("Failed to get host name", e);
			e.printStackTrace();
			System.exit(1);
		}

		GameManager gameManager = new GameManager();
		_gameRunner = new SingleThreadedGameRunner();
		_definitionManager = new DefinitionManager(gameManager.newDefinition());

		_reqCreator = new RequestCreator(_definitionManager.getDef());
		initGame(new AiManager(_gameRunner), _definitionManager.getDef());
	}

	public void setGame(GameDefinition game) {


	}

	public void setName(String canonicalHostName) {
		_hostName = canonicalHostName;

	}

	private void initGame(AiManager aiManager, GameDefinition gd) {
		gd.addObserver(new ConcoleObserver());
		gd.addObserver(new PlayerManagerOldTegObserver());
		gd.addObserver(new AiObserver(aiManager));
		gd.addObserver(new SaveGameObserver());
	}

	/* parse the arguments */
	private static void configureUsingArguments(Server s,
			String[] argv) {
		int i;

		i = 1;
		while (i < argv.length) {
			if ("--help".compareTo(argv[i]) == 0) {
				System.err.println(String
						.format("Usage: %s [option ...]\nValid options are:\n",
								argv[0]));
				System.err
.println(String
						.format("  -h, --help\t\tPrint a summary of the options\n"));
				System.err
						.println(String
								.format("  -p, --port PORT\tBind the server to port PORT (default 2000)\n"));
				System.err.println(String
						.format("  -s, --seed SEED\tNew seed for random\n"));
				System.err
.println(String
						.format("  -v, --version\t\tPrint the version number\n"));
				System.exit(0);
			} else if ("--version".compareTo(argv[i]) == 0) {
				System.err.println(String.format(Common.TEG_NAME + " v"
						+ Common.VERSION + "\n\n"));
				System.exit(0);
			} else if ("--port".compareTo(argv[i]) == 0) {
				i++;
				s.setPort(Integer.parseInt(argv[i]));
			} else {
				System.err.println(String.format(
						"Unrecognized option: \"%s\"\n", argv[i]));
				System.exit(1);
			}
			i++;
		}
	}

	private void read(SelectionKey selKey) {
		// Get channel with bytes to read
		SocketChannel sChannel = (SocketChannel) selKey.channel();
		ByteBuffer b = ByteBuffer.allocate(Protocol.PROT_MAX_LEN);
		boolean read = readData(Net.getPerson(sChannel), b);
		if (!read)
			return;
		List<PlayerRequest> commands = createCommands(Net.getPerson(sChannel), b);

		_gameRunner.runCommands(commands, _definitionManager.getDef().getGame());
	}

	/* Read the file descriptor and call the apropiate function */
	public boolean readData(Person p, ByteBuffer b) {
		int readBytes;

		readBytes = Net.net_readline(p, b, Protocol.PROT_MAX_LEN);
		if (readBytes < 1) {

			// PlayerToBeData pJ = _gameDefinition.getPlayerData(p);

			// _gameDefinition.getManager().player_del_hard(p);

			return false;
		}

		return true;
	}




	private List<PlayerRequest> createCommands(Person p, ByteBuffer b) {
		List<RequestConteiner> requests = createRequestConteiners(p, b);
		List<PlayerRequest> res = new ArrayList<PlayerRequest>();
		for (RequestConteiner req : requests) {
			try {
				if (!(noGameToken(req))) {
					if (!_definitionManager.getDef().isLocked()) {
							changeGameDefinition(req);
						if (_definitionManager.getDef().isLocked()) {// game started
							res.add(new StartGameRequest(p));
						}
					} else {
						PlayerRequest command = _reqCreator.create(req);
						res.add(command);
					}
				}
			} catch (TegRequestException e) {
				_log.error(e);
			}
		}


		return res;
	}




	private boolean noGameToken(RequestConteiner req) throws TegRequestException {
		switch (req.getToken()) {
		case TOKEN_TEST:
		case TOKEN_CVERSION:
			break;
		case TOKEN_HELP:
			Net.net_printf(req.getPerson(), "No help!");
			break;
		case TOKEN_REM:
			Net.net_printf(req.getPerson(), "%s=%s\n", TOKEN.TOKEN_REM,
					"Para que me envias un rem?");
			break;
		case TOKEN_QUMM:
			Net.net_printf(req.getPerson(), "%s=%s\n", TOKEN.TOKEN_REM,
					("Yo tambien quiero un mundo mejor!"));
			break;
		case TOKEN_SVERSION:
			Net.net_printf(req.getPerson(), "%s=%s %s\n", TOKEN.TOKEN_SVERSION,
					"TEG server version ", Common.VERSION);
			break;
		case TOKEN_PVERSION:
			String[] vers = req.getValue().split(",");
			int hi = Integer.parseInt(vers[0]);
			Net.net_printf(req.getPerson(), "%s=%d,%d\n", TOKEN.TOKEN_PVERSION,
					Protocol.PROTOCOL_HIVER, Protocol.PROTOCOL_LOVER);

			if (hi != Protocol.PROTOCOL_HIVER) {
				Console
						.con_text_out(
								Common.M_ERR,
								String
										.format(
												"Client with incompatible protocol version (server:%d , client:%d)\n",
												Protocol.PROTOCOL_HIVER, hi));

				// game.player_del_hard(fd);
				// FIXME
				throw new TegRequestException("bad protocol");
			}
			break;
		case TOKEN_ECHO:
			Net.net_printf(req.getPerson(), "%s\n", req.getValue());
			break;
		default:
			return false;
		}
		return true;
	}

	private void changeGameDefinition(RequestConteiner req)
			throws TegRequestException {
		if (_definitionManager.getDef().isLocked())
			throw new TegRequestException("Definition is locked");
		switch (req.getToken()) {
		case TOKEN_COLOR:
			_definitionManager.setColor(req.getPerson(),
					ChipsColor.getColour(Integer.parseInt(req.getValue())));
			break;
		case TOKEN_START:
			_definitionManager.lock(req.getPerson());
			break;
		case TOKEN_PLAYERID:
			String[] data = req.getValue().split(",");
			String name = data[0];
			boolean isPlaying = (Integer.parseInt(data[1]) == 1);
			// FIXME for observers
			_definitionManager.createPlayer(name, req.getPerson());
			break;
		case TOKEN_ROBOT:
			_definitionManager.createAi();
			break;
		case TOKEN_SET:
			String[] tmp = req.getValue().split("=");
			String token = tmp[0];
			String value = (tmp.length > 1) ? tmp[1] : null;
			GameConfiguration.CONF_PARAMETER param = getParam(token);
			_definitionManager.setConf(req.getPerson(), param, value);
			break;
		case TOKEN_STATUS:
			String strout = Helper.aux_token_stasta(_definitionManager.getDef());
			Net.net_printf(req.getPerson(), "%s=%s\n", TOKEN.TOKEN_STATUS, strout);
			break;
		default:
			throw new TegRequestException("Unknown " + req);
		}
	}
	/**
	 * @param fd
	 * @param b
	 * @return
	 */
	private static List<RequestConteiner> createRequestConteiners(Person p,
			ByteBuffer b) {
		List<String> cleanLines = getCleanLines(b);
		List<RequestConteiner> requests = new ArrayList<RequestConteiner>();
		for (String line : cleanLines) {
			RequestConteiner cont = RequestConteiner.createRequestConteiner(
					line, p);
			requests.add(cont);
		}
		return requests;
	}


	/**
	 * @param b
	 * @return
	 */
	private static List<String> getCleanLines(ByteBuffer b) {
		List<String> lines = Net.decodeMessageToStrings(b);
		List<String> cleanLines = new ArrayList<String>();
		for (String line : lines) {
			cleanLines.addAll(Arrays.asList(line.split(";")));
		}
		return cleanLines;
	}

	public static GameConfiguration.CONF_PARAMETER getParam(String token) {
		TOKEN t = TOKEN.getToken(token);
		switch (t) {
		case OPTION_CONQWORLD:
			return GameConfiguration.CONF_PARAMETER.ConquerWorld;
		case OPTION_CMISSION:
			return GameConfiguration.CONF_PARAMETER.CommonSecretMission;
		case OPTION_ARMIES:
			return GameConfiguration.CONF_PARAMETER.InitialArmiesToPut;
		case OPTION_FOG_OF_WAR:
			return GameConfiguration.CONF_PARAMETER.FogOfWar;
		default:
			return null;
		}

	}
}


