package org.maharshak.teg.backoffice;

import java.io.IOException;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.backoffice.ai.Misc;
import org.maharshak.teg.game.GameConfiguration;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.event.EventType;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.ChipsColor;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.PlayerToBeData;

public class DefinitionManager {

	private GameDefinition _def;

	public DefinitionManager(GameDefinition gd) {
		_def = gd;
	}



	public void createAi() throws TegRequestException {
		String name = "";
		boolean badName = false;
		do {
			name = Misc.getRandomName();
			badName = false;
			for (PlayerToBeData ptb : _def.getPlayersToBe()) {
				if (ptb.getName().compareTo(name) == 0) {
					badName = true;
					break;
				}
			}
		} while (badName);
		createAi(name);
	}

	public void createAi(String name) throws TegRequestException {
		Person owner = new Person();
		_def.addPlayer(owner, name, false);
		ChipsColor color = _def.getFreeColor();
		_def.setColour(owner, color);
	}

	public void lock(Person p) throws TegRequestException {
		boolean definitoinState = !(_def.getPlayersToBe().length < 2);

		boolean isPlayer = _def.getPlayerData(p).isHuman();
		if (!(definitoinState && isPlayer)) {
			throw new TegRequestException("Cant lock");
		}
		try {
			_def.getManager().create(_def.getId());
		} catch (IOException e) {
			throw new TegRequestException("Failed to initialize maps", e);
		}
	}

	public void setColor(Person p, ChipsColor colour) throws TegRequestException {
		PlayerToBeData ptb = _def.getPlayerData(p);
		boolean notRobot = ptb.isHuman() == true;
		boolean correctColour = (colour != null);
		boolean hasFreeColours = _def.getFreeColor() != null;
		if (!(notRobot && correctColour && hasFreeColours))
			throw new TegRequestException("Invalid request");
		ChipsColor _finalColor = colour;
		if (!_def.isColorFree(_finalColor)) {
			ChipsColor freeColor = _def.getFreeColor();
			_finalColor = freeColor;
		}
		_def.setColour(p, _finalColor);
	}



	public void setConf(Person person, GameConfiguration.CONF_PARAMETER param, String value)
			throws TegRequestException {
		switch (param) {
		case CommonSecretMission:
			boolean val1 = Integer.parseInt(value) == 0;
			_def.getConf().setCommonMission(val1);
			break;
		case ConquerWorld:
			boolean val2 = Integer.parseInt(value) == 0;
			_def.getConf().setCommonMission(val2);
			break;
		case FogOfWar:
			boolean val3 = Integer.parseInt(value) == 1;
			_def.getConf().setFogOfWar(val3);
			break;
		case InitialArmiesToPut:
			String[] tmp = value.split(",");
			int armies1 = Integer.parseInt(tmp[0]);
			int armies2 = Integer.parseInt(tmp[1]);
			_def.getConf().setFirstRoundPut(armies1);
			_def.getConf().setSecondRoundPut(armies2);
			break;
		default:
			throw new TegRequestException("Unknonw conf parameter " + param);
		}

	}

	public void createPlayer(String _name, Person p) throws TegRequestException {
		boolean badName = false;
		boolean alreadyPartOfGame = false;
		boolean tmp = false;
		for (PlayerToBeData ptb : _def.getPlayersToBe()) {
			if (ptb.getName().compareTo(_name) == 0) {
				badName = true;
			}
			if (ptb.getPerson() == p)
				if (!tmp)
					tmp = true;
				else
					alreadyPartOfGame = true;
		}
		if (!(!badName && !_def.isLocked() && !alreadyPartOfGame)) {
			throw new TegRequestException("In valid request");
		}
		_def.addPlayer(p, _name, true);

		boolean c[] = new boolean[_def.getConf().getMaxPlayers()];
		c = _def.freeColors();
		String colores = "";
		for (int i = 0; i < _def.getConf().getMaxPlayers(); i++) {
			colores += String.format(",%d", (c[i]) ? 1 : 0);
		}
		_def.definitionStateChanged();
		_def.notifyObservers(new GameConfigurationChangedEvent(
				EventType.PLAYER_ADDED, p)
.putData(DataType.COLORS, colores));
	}

	public GameDefinition getDef() {
		return _def;
	}


}
