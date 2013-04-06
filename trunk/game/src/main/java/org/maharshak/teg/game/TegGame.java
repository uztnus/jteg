package org.maharshak.teg.game;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.event.EventType;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.player.PlayerToBeData;






public class TegGame extends Observable{

  private final Board _board;
  private volatile Map<Country, Player> _owned;
  private volatile Player _roundStarter;
  private volatile Map<Country, Integer> _armies;
  private volatile Map<Country, Integer> _howManyLeftToRegroupInThisTurn;

  private volatile Map<Card, Player> _ownedCards;
  private volatile Map<Card, Boolean> _usedCardsFor2Armies;
  private final GameConfiguration _conf;
  private final UUID _gameID;
  private volatile int _gameNumber = 0;
  private volatile int _roundNumber;
  private volatile Player _turnOwner;
  private volatile PLAYER_STATE _playerState;
  private volatile Map<Player, Integer> _exchangesPerformed;
  private final TurnData _turnData;
  private final List<Player> _players;
  private volatile GameState _state;
  private volatile List<Player> _lost;
  private volatile boolean _inPlay = false;
	private volatile List<Card> _freeCards;



  public TegGame(GameDefinition gd) throws IOException {
    _conf = gd.getConf();

    _gameNumber = -1;
		_gameID = UUID.fromString(gd.getId());
		_board = Board.get(gd.getBoardID());
    _playerState = null;// new HashMap<Player, PLAYER_STATE>();
    _owned = new ConcurrentHashMap<Country, Player>();
    _armies = new ConcurrentHashMap<Country, Integer>();
    _exchangesPerformed = new ConcurrentHashMap<Player, Integer>();
    _usedCardsFor2Armies = new ConcurrentHashMap<Card, Boolean>();
    _ownedCards = new ConcurrentHashMap<Card, Player>();
    _howManyLeftToRegroupInThisTurn = new ConcurrentHashMap<Country, Integer>();
    _turnData = new TurnData();
    _players = new ArrayList<Player>();
    _lost = new ArrayList<Player>();
    if (getConf().hasSecretMission() && !getConf().hasCommonMission()) {
      assignSecretMissions(gd);
    }
    for (PlayerToBeData ptb : gd.getPlayersToBe()) {
      _players.add(new Player(this, ptb));
    }
		setState(GameState.REGISTRATION_FULL);
  }

	public void start() {
		start((int) (Math.random() * _players.size()));
	}

	public void start(int roundStarter) {
		start(_board.splitCountries(_players.size()), roundStarter);
	}
  /**
   * 
   */
  public void start(List<List<Country>> putCountries, int roundStarterId) {
    _howManyLeftToRegroupInThisTurn.clear();
    _usedCardsFor2Armies.clear();
    _ownedCards.clear();
    _roundNumber = 0;
    _gameNumber++;
    _turnOwner = null;
    _turnData.init();
    _lost.clear();
    _inPlay = false;
    _owned.clear();
		_freeCards = new ArrayList<Card>(getBoard().getDeck());

    for (Player p : _players) {
      _exchangesPerformed.put(p, 0);
    }
    _inPlay = true;

    for (int i = 0; i < getStillPlaying(); i++) {
      Player owner = _players.get(i);
      for (Country toPut : putCountries.get(i)) {
        setOwner(toPut, owner);
        setArmies(toPut, 1);
      }
    }
    Player gameStarter = getPlayer(roundStarterId);
    setTurnOwner(gameStarter);
    setRoundStarter(gameStarter);
    setState(GameState.FIRST_PUT);
  }

  public void setOwner(Country p, Player newOwner) {
    Player previous = _owned.get(p);
    _owned.put(p, newOwner);
    if (_inPlay && previous != null)
      checkWinLose(newOwner, previous);
  }

  public Player getOwner(Country country) {
    return _owned.get(country);
  }


  public Player getOwner(Card t) {
    if (!_ownedCards.containsKey(t))
      return null;
    return _ownedCards.get(t);
  }


  public void freeCard(Card card) {
		_freeCards.add(card);
    _ownedCards.remove(card);
    _usedCardsFor2Armies.remove(card);
  }


	public void giveCardToPlayer(Card card, Player p) {
		_freeCards.remove(card);
		_ownedCards.put(card, p);
		_usedCardsFor2Armies.put(card, false);
    setChanged();
		notifyObservers(new GameChangedEvent(EventType.GOT_CARD, p).putData(
				DataType.CARDS, "" + card.getCountry().getName()));

  }

  public boolean isUsed(Card tarjeta) {
    return _usedCardsFor2Armies.get(tarjeta);
  }


  private void setUsedCard(Card tarjeta, boolean b) {
    _usedCardsFor2Armies.put(tarjeta, b);

  }

  public void useCard(Card t) {
    Player p = getOwner(t);
    setUsedCard(t, true);
    setArmies(t.getCountry(), getArmies(t.getCountry()) + 2);
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.USED_CARD_FOR_ARMY, p).putData(DataType.CARDS, "" + t.getCountry().getName()));

  }


	public Card findRandomFreeCard() {
		Collections.shuffle(_freeCards);
		return _freeCards.get(0);
	}

  public Card getCard(Country c) {
		for (Card t : _board.getDeck()) {
      if (t.getCountry() == c) {
        return t;
      }
    }
    return null;
  }

  public List<Card> getCards(Player pJ) {
    List<Card> res = new ArrayList<Card>();
    for (Card t : _ownedCards.keySet()) {
      if (_ownedCards.get(t) == pJ)
        res.add(t);
    }

    return res;
  }

  public Board getBoard() {
    return _board;
  }

  public void setArmies(Country c, int i) {
    _armies.put(c, i);
  }

  public List<Country> countriesOf(Player p) {
    List<Country> res = new ArrayList<Country>();
    for (Country c : getBoard().getCountries()) {
        if (getOwner(c) == p)
          res.add(c);
	}
    return res;
  }

  public int armiesHas(Player p) {
    int res = 0;
    for (Country c : countriesOf(p)) {
      res += getArmies(c);
    }
    return res;
  }

  public int getArmies(Country c) {
    Integer integer = _armies.get(c);
    return (integer != null) ? integer : -1;
  }



  public int leftForRegruping(Country c) {
    if (!_howManyLeftToRegroupInThisTurn.containsKey(c))
      _howManyLeftToRegroupInThisTurn.put(c, 0);
    return _howManyLeftToRegroupInThisTurn.get(c);
  }

  public void setLeftForRegrouping(Country c, int leftToRegroupe) {
    _howManyLeftToRegroupInThisTurn.put(c, leftToRegroupe);
  }

  public void clearRegoupingData() {
    _howManyLeftToRegroupInThisTurn.clear();
  }




  public UUID getGameID() {
    return _gameID;
  }

  public GameConfiguration getConf() {
    return _conf;
  }

  public int getGameNumber() {
    return _gameNumber;
  }

  public int getRoundNumber() {
    return _roundNumber;
  }

  public Player getRoundStarter() {
    return _roundStarter;
  }

  public void setRoundStarter(Player p) {
    _roundStarter = p;
    _roundNumber++;
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.NEW_ROUND, p));
  }


  public int getExchangesPerformed(Player player) {
    assert player.getGame() == this : "player is not part of this game";
    return _exchangesPerformed.get(player);
  }



  public PLAYER_STATE getPlayerState(Player player) {
    assert player.getGame() == this : "player is not part of this game";
    if (_lost.contains(player))
      return PLAYER_STATE.GAMEOVER;
    if (player != _turnOwner)
      return PLAYER_STATE.IDLE;
    return _playerState;

  }


  public void setState(Player player, PLAYER_STATE state) {
    if (player != _turnOwner) {
      throw new RuntimeException("Shit");
    }
    if (_playerState == state)
      return;
    _playerState = state;
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.PLAYER_STATE_CHANGED, player)
        .putData(DataType.NEW_STATE, state.name()));
  }

  public void setTurnOwner(Player turno) {
    if (_turnOwner != turno) {
      _turnOwner = turno;
      _turnData.init();
    }
  }

  public Player getTurnOwner() {
    return _turnOwner;
  }



  public TurnData getTurnData() {
    return _turnData;
  }


  public int getId(Player player) {
    assert player != null : "cant get index of null player";
    return _players.indexOf(player);
  }

  public Player getPlayer(int i) {
    if (i > -1 && i < _players.size())
      return _players.get(i);
    return null;
  }

  public List<Player> getPlayerList() {
    return Collections.unmodifiableList(_players);
  }

  /* finds a player given its name */
  public Player getPlayer(String name) {
    for (Player p : _players) {
      if (p.getName().compareTo(name) == 0)
        return p;
    }
    return null;
  }

  /* given a fd, return the player who owns it */
  public Player getPlayer(Person per) {
    for (Player p : _players) {
      if (p.getPerson().equals(per))
        return p;
    }
    return null;
  }

  /**
   * @param current
   * @param game
   * @return
   */
  private Player whosTurnIsNext(Player current) {
    Player tmp = null;
    int pos = getPlayerList().indexOf(current);
    for (int i = 1; i < _players.size(); i++) {
      Player look = _players.get((i + pos) % _players.size());
      if (isPlaying(look)) {
        tmp = look;
        break;
      }
    }
    return tmp;
  }

  public boolean isPlaying(Player p) {
    return !_lost.contains(p);
  }

  public boolean hasWon(Player p) {
    if (isInPlay()) {
      if (getStillPlaying() < 2 && p.isAtLeastAt(PLAYER_STATE.START)) {
        return true;
      } else {
        return Mission.isMissionAccomplished(p);
      }
    }
    return false;
  }



  public int getStillPlaying() {
    int res = 0;
    if (isInPlay()) {
      for (Player p : _players) {
        if (isPlaying(p))
          res++;
      }
      return res;
    } else {
      return 0;
    }
  }

  public boolean isInPlay() {
    return _inPlay;
  }

  public void turnFinished() {
    Player tmp = whosTurnIsNext(getTurnOwner());
    getTurnOwner().setState(PLAYER_STATE.IDLE);
    if (tmp != null) {
      setTurnOwner(tmp);

    } else {
      throw new RuntimeException("WTF");
    }

    if (!isRoundComplete()) {
      getTurnOwner().setState(getNextPlayerState());
    } else {
      roundFinished();
    }
  }

  /*
   * I want to know if the round is over. It is not enought to know if newturn
   * == started because if a player with the turn exit the game he will never
   * receive the turn again, but the started turn will point to him
   */
  /* return true if the round is complete */
  public boolean isRoundComplete() {

    Player roundStarter = getRoundStarter();
    if (!isPlaying(roundStarter))
      roundStarter = whosTurnIsNext(roundStarter);
    if (roundStarter == getTurnOwner())
      return true;

    return false;
  }

  private PLAYER_STATE getNextPlayerState() {
    switch (_state) {
      case FIRST_PUT:
        return PLAYER_STATE.FIRST_PUT;
      case SECOND_PUT:
        return PLAYER_STATE.SECOND_PUT;
      case ATTACK:
        return PLAYER_STATE.TURNOSTART;
      case ARMY_PUT:
        return PLAYER_STATE.PUT_ARMIES;
      default:
        break;
    }
    return null;
  }

  private void roundFinished() {
    GameState newState = null;
    switch (_state) {
      case FIRST_PUT:
        newState = GameState.SECOND_PUT;
        break;
      case ARMY_PUT:
      case SECOND_PUT:
        newState = GameState.ATTACK;
        break;
      case ATTACK:
        newState = GameState.ARMY_PUT;
        Player tmp2 = whosTurnIsNext(getRoundStarter());

        if (tmp2 != null) {
          setTurnOwner(tmp2);
        } else {
          throw new RuntimeException("Should never come here!!!");
        }
        setRoundStarter(tmp2);

        break;
      default:
        throw new RuntimeException("WTF");
    }

    setState(newState);
  }

  public void setState(GameState state) {
    _state = state;
    switch (state) {
      case REGISTRATION_FULL:
        getConf().setImMutabale();
        break;
      case FIRST_PUT:
        getTurnOwner().setState(PLAYER_STATE.FIRST_PUT);
        break;
      case SECOND_PUT:
        getTurnOwner().setState(PLAYER_STATE.SECOND_PUT);
        break;
      case ATTACK:
        getTurnOwner().setState(PLAYER_STATE.TURNOSTART);
        break;
      case ARMY_PUT:
        getTurnOwner().setState(PLAYER_STATE.PUT_ARMIES);
        break;
      default:
        break;
    }
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.GAME_STATE_CHANGED, null)
        .putData(DataType.NEW_STATE, state.name()));
  }

  public GameState getState() {
    return _state;
  }

  /* ends the game */
  public void gameWon(Player winner) {
    winner.setState(PLAYER_STATE.GAMEOVER);
    _inPlay = false;
    /* add points to the winner */

    if (winner != null && getRoundNumber() > 0) {
      int points = 350 / getRoundNumber();
      Stat s = winner.getPlayer_stats();
      s.setScore(s.getScore() + points);
    }

    /* update scors */
    
    /* send the last status to all the players */

    setChanged();
    notifyObservers(new GameChangedEvent(EventType.WON_GAME, winner));


  }

  public void gameStateChanged() {
    setChanged();
  }

  public boolean isLost(Player pJ) {
    assert (pJ != null);
    if (!isPlaying(pJ))
      return true;
    if (countriesOf(pJ).size() > 0)
      return false;
    return true;
  }

  public void lost(Player pJ) {
    assert (pJ != null);
    // assert( pJ.is_player == true );

    // if (pJ.getState() == PLAYER_STATE.DISCONNECTED) {
    // // XmlScores.scores_insert_player(pJ);
    // } else {
    //
    // }
    // We get called if a player's last country is conquered. If the player
    // surrendered before that happened we must NOT decrement the player count
    // again

    _lost.add(pJ);
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.LOST_GAME, pJ));
  }


  private void assignSecretMissions(GameDefinition def) {
    List<Mission> freeMissions = Arrays.asList(Mission.g_missions);
    Collections.shuffle(freeMissions);
		for (int i = 0; i < def.getPlayersToBe().length; i++) {
			def.getPlayersToBe()[i].setMission(freeMissions.get(i));
    }
  }

  public int performExchange(Player p, Card c1, Card c2, Card c3) throws TegRequestException {
    if (!Card.canExchangeBePerformed(c1, c2, c3) || !doOwnCards(p, c1, c2, c3))
      throw new TegRequestException("Can perfor exchange");
    assert p.getGame() == this : "player is not part of this game";
    _exchangesPerformed.put(p, _exchangesPerformed.get(p) + 1);
    _turnData.exchangePerformed();
    freeCard(c1);
    freeCard(c2);
    freeCard(c3);
    int armies = getConf().getRuleBook().armiesPerExchange(p.getExchangesSoFar());
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.EXCHANGE, p).putData(DataType.CARDS, DataType.createCardIdData(c1, c2, c3)).putData(
        DataType.ARMIES, "" + armies));
    return armies;
  }

  public boolean doOwnCards(Player p, Card c1, Card c2, Card c3) {
    if (getOwner(c1) != p || getOwner(c2) != p || getOwner(c3) != p)
      return false;
    return true;
  }

  public void sendMessage(Player from, String msg) {
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.MESSAGE, from).putData(DataType.MSG, msg));
  }

  public void moveTroops(Player player, Country source, Country destination, int amount) throws TegRequestException {
    assert player.getGame() == this : "player from this game";
    assert getOwner(source) == player && getOwner(destination) == player : "must own both countries";
    assert amount > 0 : "need somthing to move";
    setArmies(source, getArmies(source) - amount);
    setArmies(destination, getArmies(destination) + amount);
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.MOVE_TROOPS, player).putData(DataType.SRC, "" + source.getName()).putData(DataType.DST,
        "" + destination.getName()));


  }

  /**
   * @param p
   * @param src
   * @param dst
   * 
   */
  public void startAttack(Player p, Country src, Country dst) {
    p.setState(PLAYER_STATE.ATAQUE);
    getTurnData().setAttackSource(src);
    getTurnData().setAttackDst(dst);
    setChanged();
    notifyObservers(new GameChangedEvent(EventType.ATTACK_STARTED, p));
  }

  private void checkWinLose(Player attacker, Player defender) {
    if (isLost(defender)) {
      lost(defender);
    }
    if (hasWon(attacker)) {
      gameWon(attacker);
      return;
    }
  }
}
