package org.maharshak.teg.server.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.maharshak.teg.backoffice.IGameRunner;
import org.maharshak.teg.backoffice.ai.AIStrategy;
import org.maharshak.teg.backoffice.ai.IAIManager;
import org.maharshak.teg.backoffice.ai.teg.TegStrategy;
import org.maharshak.teg.common.Common;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.server.Console;


public class AiManager implements IAIManager {

  private IGameRunner _runner;
  private static Logger _log = Logger.getLogger("ai");
  private Map<Person, AIStrategy> _ais;
  private AIStrategy _tegStrategy = new TegStrategy();
  private LinkedBlockingQueue<AiRequest> _reqQueue;

  private class AiRunner implements Runnable {

    @Override
    public void run() {
      while (true) {
        AiRequest command = null;
        try {
          command = _reqQueue.poll(300, TimeUnit.MILLISECONDS);
          if (command == null)
            continue;
          play(command.getP(), _tegStrategy, command.getNewState());
          command = null;

        } catch (InterruptedException e) {
        } catch (Exception e) {
          _log.error(e);
          Console.con_text_out(Common.M_ERR, "Error during ai %s performance %s", command, e);
        }
      }

    }


  }




  private class AiRequest {
    private Player _p;
    private PLAYER_STATE _newState;

    /**
     * @param p
     * @param newState
     * @param create
     */
    public AiRequest(Player p, PLAYER_STATE newState) {
      _p = p;
      _newState = newState;
    }

    /**
     * @return the p
     */
    public Player getP() {
      return _p;
    }

    /**
     * @return the newState
     */
    public PLAYER_STATE getNewState() {
      return _newState;
    }

  }

  public AiManager(IGameRunner runner) {
    _runner = runner;
    _ais = new HashMap<Person, AIStrategy>();
    _reqQueue = new LinkedBlockingQueue<AiRequest>();
    Thread t = new Thread(new AiRunner(), "AI Runner");
    t.start();
  }



  private void addRequest(AiRequest aiRequest) {
    try {
      _reqQueue.put(aiRequest);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }



  /**
   * @see org.maharshak.teg.IAIManager#stateChanged(org.maharshak.teg.player.Player,
   *      org.maharshak.teg.player.PLAYER_STATE)
   */
  @Override
  public void stateChanged(Player pl, PLAYER_STATE newState) {
    if (!_ais.containsKey(pl.getPerson()))
      return;
    if (pl.getGame().getTurnOwner() != pl)
      return;
    if (newState == PLAYER_STATE.FIRST_PUT || newState == PLAYER_STATE.SECOND_PUT || newState == PLAYER_STATE.TURNOSTART
        || newState == PLAYER_STATE.REGROUP || newState == PLAYER_STATE.PUT_ARMIES)
      addRequest(new AiRequest(pl, newState));
  }

  private void play(Player pl, AIStrategy str, PLAYER_STATE newState) {

    switch (newState) {
      case FIRST_PUT:
        str.onFirstPut(pl, _runner);
        break;
      case SECOND_PUT:
        str.onSecondPut(pl, _runner);
        break;
      case TURNOSTART:
        str.onAttack(pl, _runner);
        break;
      case REGROUP:
        str.onRegroup(pl, _runner);
        break;
      case PUT_ARMIES:
        str.onArmyPut(pl, _runner);
        break;
      default:
        break;
    }

  }

  @Override
  public void addAIPlayer(Person p) {
    _ais.put(p, _tegStrategy);
  }
}
