package org.maharshak.teg.game;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.game.rules.RULES;
import org.maharshak.teg.game.rules.RuleBook;
import org.maharshak.teg.game.rules.TegRuleBook;

@XmlRootElement
public class GameConfiguration implements Serializable {
  public static enum CONF_PARAMETER {
	  ConquerWorld, CommonSecretMission, InitialArmiesToPut, FogOfWar;
	}

	private int _firstRoundPut;
  private int _secondRoundPut;
  private int _maxPlayers;
  private boolean _fogOfWar;
  private boolean _mutabale;
  private boolean _secretMission;
  private boolean _commonMission;
  private RULES _rules;
	private int _maxCardsToHold;
	private RuleBook _ruleBook;
	private Board _board;

  public GameConfiguration() {
    _mutabale = true;
    _firstRoundPut = 5;
    _secondRoundPut = 3;
    _maxPlayers = 6;
    _fogOfWar = false;
    _secretMission = false;
    _commonMission = true;
    _rules = RULES.TEG_RULES_TEG;
		_maxCardsToHold = 5;
		_ruleBook = new TegRuleBook();
  }


  public int getMaxPlayers() {
    return _maxPlayers;
  }

  public int getArmiesInFirstRound() {
    return _firstRoundPut;
  }

  public int getArmiesInSecondRound() {
    return _secondRoundPut;
  }


  public boolean isFogOfWar() {
    return _fogOfWar;
  }

  /**
   * @param firstRoundPut
   *          the firstRoundPut to set
   */
  public GameConfiguration setFirstRoundPut(int firstRoundPut) {
    if (_mutabale)
      _firstRoundPut = firstRoundPut;
    return this;
  }

  /**
   * @param secondRoundPut
   *          the secondRoundPut to set
   */
  public GameConfiguration setSecondRoundPut(int secondRoundPut) {
    if (_mutabale)
      _secondRoundPut = secondRoundPut;
    return this;
  }

  /**
   * @param maxPlayers
   *          the maxPlayers to set
   */
  public GameConfiguration setMaxPlayers(int maxPlayers) {
    if (_mutabale)
      _maxPlayers = maxPlayers;
    return this;
  }

  /**
   * @param fogOfWar
   *          the fogOfWar to set
   */
  public GameConfiguration setFogOfWar(boolean fogOfWar) {
    if (_mutabale)
      _fogOfWar = fogOfWar;
    return this;
  }


  public void setImMutabale() {
    _mutabale = false;
  }


  public boolean hasSecretMission() {
    return _secretMission;
  }


  public GameConfiguration setSecretMission(boolean secretMission) {
    if (_mutabale)
      _secretMission = secretMission;
    return this;
  }


  public boolean hasCommonMission() {
    return _commonMission;
  }


  public GameConfiguration setCommonMission(boolean commonMission) {
    if (_mutabale)
      _commonMission = commonMission;
    return this;
  }

  public RULES getRules() {
    return _rules;
  }

  public GameConfiguration setRules(RULES rules) {
    if (_mutabale)
      _rules = rules;
    return this;
  }

	public int getMaxCardsToHold() {
		return _maxCardsToHold;
	}

	public GameConfiguration setMaxCardsToHold(int maxCardsToHold) {
		if (_mutabale)
			_maxCardsToHold = maxCardsToHold;
		return this;
	}

	@XmlTransient
	public RuleBook getRuleBook() {
		return _ruleBook;
	}


	public GameConfiguration setRuleBook(RuleBook ruleBook) {
		if (_mutabale)
			_ruleBook = ruleBook;
		return this;
	}

  public String toPrettyPrint() {
    return "GameConfiguration [\n\tFirst put:" + _firstRoundPut + "\n\tSecond put: " + _secondRoundPut + "\n\tFog of war " + _fogOfWar
        + "\n\tSecret mission:" + _secretMission + "\n\tCommon secret:" + _commonMission + "]";
  }

}
