package org.maharshak.teg.game;

import java.util.ArrayList;
import java.util.List;

import org.maharshak.teg.board.Country;

public class Dice {

  private static Dice _dice = new Dice();
  static {
    record();
  }
  private boolean _record = false;
  private List<Integer> _recordedDice;

  protected Dice() {

  }

  public static void record() {
    _dice._record = true;
    _dice._recordedDice = new ArrayList<Integer>();
  }

  public static Dice getDice() {
    return _dice;
  }

  public static void init(Dice d) {
    _dice = d;
  }

  public ThrowResult throwDice(int attack, int defend) {
    int[] throwDice = throwDice(attack + defend);
    if(_record){
      add(throwDice);
    }
    return new ThrowResult(attack, defend, throwDice);
  }

  private void add(int[] throwDice) {
    
    for (int i = 0; i < throwDice.length; i++) {
      _recordedDice.add(throwDice[i]);
    }
  }
  
  public List<Integer> getRecordedDice() {
    return _recordedDice;
  }

  private int[] throwDice(int diceNum) {

    int[] res = new int[diceNum];
    for (int i = 0; i < diceNum; i++) {
      res[i] = (int) ((Math.random() * 6) + 1);
    }
    return res;
  }

  public int getAttack(TegGame game, Country c, boolean attack) {
    int countryArmies = game.getArmies(c);
    if (attack)
      countryArmies--;
    if (countryArmies > 3)
      countryArmies = 3;
    return countryArmies;
  }
}
