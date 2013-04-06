package org.maharshak.teg.test;

import java.util.Iterator;
import java.util.List;

import org.maharshak.teg.game.Dice;
import org.maharshak.teg.game.ThrowResult;

public class RecoredDice extends Dice {

  private Iterator<Integer> _recorded;

  public RecoredDice(List<Integer> dice) {
    _recorded = dice.iterator();

  }

  public ThrowResult throwDice(int attack, int defend) {
    return new ThrowResult(attack, defend, loadDice(attack + defend));

  }

  private int[] loadDice(int amount) {
    int[] res = new int[amount];
    for (int i = 0; i < res.length && _recorded.hasNext(); i++) {
      res[i] = _recorded.next();
    }
    return res;
  };

}
