package org.maharshak.teg.game;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ThrowResult {
  private final int MAX_DICE = 3;
	private int[] _srcDice = new int[MAX_DICE];
	private int[] _dstDice = new int[MAX_DICE];


	public ThrowResult() {
		// TODO Auto-generated constructor stub
	}

  public ThrowResult(int srcNumOfDice, int dstNumOfDice, int[] dice) {
    assert srcNumOfDice < MAX_DICE + 1 && dstNumOfDice < MAX_DICE + 1 : "wrong num of dice";
    assert srcNumOfDice > 0 && dstNumOfDice > 0 : "wrong num of dice";
    assert dice.length == srcNumOfDice + dstNumOfDice : " wrong amount of throws";
    for (int i = 0; i < MAX_DICE; i++) {
      if (i < srcNumOfDice)
        _srcDice[i] = dice[i];
      else
        _srcDice[i] = 0;
    }
    for (int i = 0; i < MAX_DICE; i++) {
      if (i < dstNumOfDice)
        _dstDice[i] = dice[i + srcNumOfDice];
      else
        _dstDice[i] = 0;
    }
    Arrays.sort(_srcDice);
    Arrays.sort(_dstDice);
    revert(_srcDice);
    revert(_dstDice);
  }

  /**
   * copied from appache commons ArrayUtil
   * 
   * @param array
   */
  private void revert(int[] array) {
    if (array == null) {
      return;
    }
    int i = 0;
    int j = array.length - 1;
    int tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

	@XmlAttribute
  public int getArmiesLostBySource() {
    int res = 0;
    for (int i = 0; i < 3 && _srcDice[i] != 0 && _dstDice[i] != 0; i++) {
			if (_srcDice[i] <= _dstDice[i])
        res++;
    }
    return res;
  }

	@XmlAttribute
  public int getArmiesLostByDestenation() {
    int res = 0;
    for (int i = 0; i < 3 && _srcDice[i] != 0 && _dstDice[i] != 0; i++) {
			if (!(_srcDice[i] <= _dstDice[i]))
        res++;
    }
    return res;
  }
  public String printDiceSrc() {
    return "" + _srcDice[0] + "," + _srcDice[1] + "," + _srcDice[2];
  }

  public String printDiceDst() {
    return "" + _dstDice[0] + "," + _dstDice[1] + "," + _dstDice[2];
  }

	public int[] getSrcDice() {
		return _srcDice;
	}

	public void setSrcDice(int[] srcDice) {
		_srcDice = srcDice;
	}

	public int[] getDstDice() {
		return _dstDice;
	}

	public void setDstDice(int[] dstDice) {
		_dstDice = dstDice;
	}
}
