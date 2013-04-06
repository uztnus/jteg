package org.maharshak.teg.game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.game.event.EventType;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.ChipsColor;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.PlayerToBeData;

@XmlRootElement
public class GameDefinition extends Observable implements Serializable {

	private List<PlayerToBeData> _playersToBe;
	private GameConfiguration _conf;
	private UUID _gameId;
	private volatile boolean _locked;
	private transient GameManager _manager;
	private int _boardId;

	public GameDefinition(){
		_manager=null;
		_gameId=null;
	}

	public GameDefinition(GameManager man) {
		this(UUID.randomUUID(), man);
		_playersToBe = new ArrayList<PlayerToBeData>();
		_conf = new GameConfiguration();
		_locked = false;
		_boardId = 0;

	}

	public GameDefinition(UUID id, GameManager man) {
		_gameId = id;
		_manager = man;
	}

	@XmlElement
	public String getId() {
		return _gameId.toString();
	}

	public void setId(String id) {
		_gameId = UUID.fromString(id);
	}

	@XmlTransient
	public GameManager getManager() {
		return _manager;
	}


	public ChipsColor getFreeColor() {
		ChipsColor[] colors = getFreeColors();
		if (colors.length > 0)
			return colors[0];
		return null;
	}

	@XmlElementWrapper(name = "freecolors")
	@XmlElement(name="color")
	public ChipsColor[] getFreeColors() {
		List<ChipsColor> colors = new ArrayList<ChipsColor>(Arrays.asList(ChipsColor.values()));

		for (PlayerToBeData player : _playersToBe) {
			colors.remove(player.getColor());
		}
		colors.remove(ChipsColor.NA);
		return colors.toArray(new ChipsColor[] {});
	}




	public boolean isColorFree(ChipsColor c) {
		if (c == null)
			return false;
		for (PlayerToBeData player : _playersToBe) {
			if (player.getColor() == c)
				return false;
		}
		return true;
	}

	public void addPlayer(Person p, String name, boolean isHuman) throws TegRequestException {
		if (!(_playersToBe.size() <= getConf().getMaxPlayers())) {
			setChanged();
			notifyObservers(new GameConfigurationChangedEvent(EventType.DISMISS_PLAYER, p).putData(GameChangedEvent.DataType.REASON,
					"server full")
					.putData(DataType.PERSON_UUID, p.getpId().toString()));
			throw new TegRequestException("To many players in game already");
		}

		if (isLocked()) {
			setChanged();
			notifyObservers(new GameChangedEvent(EventType.DISMISS_PLAYER, null).putData(DataType.REASON, "game in progress").putData(
					DataType.PERSON_UUID, "" + p));
			return;
		}

		PlayerToBeData pp = new PlayerToBeData(p);
		pp.setName(name);
		pp.setHuman(isHuman);
		_playersToBe.add(pp);
		setChanged();
		notifyObservers(new GameConfigurationChangedEvent(EventType.PLAYER_ADDED, p).putData(DataType.PERSON_UUID, p.getpId().toString()));
	}

	public void setColour(Person p, ChipsColor c) {
		if (c != ChipsColor.NA) {
			for (PlayerToBeData ptb : _playersToBe) {
				if (ptb.getPerson() == p) {
					ptb.setColor(c);
					setChanged();
					notifyObservers(new GameConfigurationChangedEvent(EventType.COLOUR_SET, p));
					break;
				}
			}
		}
	}

	public ChipsColor getColour(Person p) {
		for (PlayerToBeData ptb : _playersToBe) {
			if (ptb.getPerson() == p) {
				return ptb.getColor();
			}
		}
		return null;
	}

	public PlayerToBeData getPlayerData(Person owner) {
		for (PlayerToBeData ptb : _playersToBe) {
			if (ptb.getPerson() == owner)
				return ptb;
		}
		return null;
	}

	public Person getPerson(String string) {
		Person tmp = new Person(string);
		for (PlayerToBeData ptb : _playersToBe) {
			if (ptb.getPerson().equals(tmp))
				return ptb.getPerson();
		}
		return null;
	}

	@XmlElementWrapper(name = "players")
	@XmlElement(name = "player")
	public PlayerToBeData[] getPlayersToBe() {
		return Collections.unmodifiableList(_playersToBe).toArray(
				new PlayerToBeData[] {});
	}

	public void setPlayersToBe(PlayerToBeData[] playersToBe) {
		_playersToBe = new ArrayList<PlayerToBeData>();
		for (PlayerToBeData playerToBeData : playersToBe) {
			_playersToBe.add(playerToBeData);
		}
	}
	public Mission getPlayersMission(Person owner) {
		return getPlayerData(owner).getMission();
	}

	@XmlElement
	public GameConfiguration getConf() {
		return _conf;
	}

	public void setConf(GameConfiguration conf) {
		_conf = conf;
	}

	@XmlElement
	public boolean isLocked() {
		return _locked;
	}

	public void lock() {
		_locked = true;
		_conf.setImMutabale();
		setChanged();
		notifyObservers(new GameConfigurationChangedEvent(EventType.DEFINITION_LOCKED, null));
	}

	public void definitionStateChanged() {
		setChanged();
	}

	public TegGame getGame() {
		if (_locked) {
			return getManager().getGame(_gameId);
		}

		return null;
	}


	public int getPID(PlayerToBeData j) {
		return _playersToBe.indexOf(j);
	}

	/* tells which colors are availables */
	public boolean[] freeColors() {
		boolean[] res = new boolean[ChipsColor.values().length - 1];
		for (int i = 0; i < ChipsColor.values().length - 1; i++) {
			res[i] = !isColorFree(ChipsColor.values()[i]);
		}
		return res;
	}

	public void setGameManager(GameManager gameManager) {
		_manager = gameManager;
		_manager.addDefinition(this);

	}

	public int getBoardID() {
		return _boardId;
	}

	public void setBoardId(int boardId) {
		_boardId = boardId;
	}
}
