package org.maharshak.teg.backoffice.ai;

import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


public interface IAIManager {


  public abstract void stateChanged(Player pl, PLAYER_STATE newState);

  public abstract void addAIPlayer(Person pl);

}