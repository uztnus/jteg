package org.maharshak.teg.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


public class GameManager {


	private List<TegGame> _runningGames;
	private List<GameDefinition> _tbdGames;


	public GameManager() {
		_runningGames = new ArrayList<TegGame>();
		_tbdGames = new ArrayList<GameDefinition>();
	}

	public GameDefinition newDefinition() {
		GameDefinition res = new GameDefinition(this);
		_tbdGames.add(res);
		return res;
	}

	public GameDefinition newDefinition(String uuid) {
		GameDefinition res = new GameDefinition(UUID.fromString(uuid), this);
		_tbdGames.add(res);
		return res;
	}

	public TegGame create(String definitionId) throws IOException {
		GameDefinition def = getGameDefinition(definitionId);
		TegGame res = new TegGame(def);
		_tbdGames.remove(def);
		_runningGames.add(res);
		def.lock();
		return res;
	}

	public GameDefinition getGameDefinition(String definitionId) {
		for (GameDefinition def : _tbdGames) {
			if (def.getId().compareTo(definitionId) == 0)
				return def;
		}
		return null;
	}

	/* Deletes a player */
	public void player_del_hard(Person pJ)
	{
		// assert( pJ !=null);
		// /* close the connection */
		// try {
		// Net.net_close(pJ.getPerson());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//
		// if( isPlaying( pJ ) ) {
		//
		// pJ.setState(PLAYER_STATE.DISCONNECTED);
		// return TEG_STATUS.TEG_STATUS_SUCCESS;
		//
		// } else if( pJ.getState() == PLAYER_STATE.GAMEOVER ) {
		// pJ.setState(PLAYER_STATE.DISCONNECTED);
		// return TEG_STATUS.TEG_STATUS_SUCCESS;
		// }
		// deleteDisconnectedPlayer(pJ);
		//    return TEG_STATUS.TEG_STATUS_SUCCESS;
	}



	public void deleteDisconnectedPlayer(Player pJ) {
		// FIXME
		// setChanged();
		// notifyObservers(new GameChangedEvent(EventType.DISMISS_PLAYER, pJ));
	}

	public TegGame getGame(UUID id) {
		for (TegGame g : _runningGames) {
			if (g.getGameID().toString().compareTo(id.toString()) == 0) {
				return g;
			}
		}
		return null;
	}

	public List<GameDefinition> getGameDefinitions() {
		return _tbdGames;
	}

	public void addDefinition(GameDefinition gameDefinition) {
		_tbdGames.add(gameDefinition);

	}

	public List<String> getAllUids() {
		List<String> res = new ArrayList<String>();
		for (TegGame g : _runningGames) {
			res.add(g.getGameID().toString());
		}
		for (GameDefinition g : _tbdGames) {
			res.add(g.getId());
		}
		return res;
	}

}