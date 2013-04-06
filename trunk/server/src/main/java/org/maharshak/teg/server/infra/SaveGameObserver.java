package org.maharshak.teg.server.infra;

import java.util.Observable;
import java.util.Observer;

import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.GameSave;
import org.maharshak.teg.game.event.GameChangedEvent;

public class SaveGameObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof GameChangedEvent)) {
			GameDefinition gd = (GameDefinition) o;
			if (!SingleThreadedGameRunner._saves.containsKey(gd.getId().toString())) {

				SingleThreadedGameRunner._saves.put(gd.getId().toString(),
						new GameSave(gd));

			}
		}

	}


}
