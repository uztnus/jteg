package org.maharshak.teg.common.ref;

import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;

public class Data {

	private String _value;
	private Person _person;
	private TegGame _game;
  private GameDefinition _definition;



  public Data(Person p, String value, GameDefinition def) {
		_person=p;
		_value=value;
    _game = def.getGame();
    _definition = def;
	}

	public Person getPerson() {
		return _person;
	}
	public String getVal() {
		return _value;
	}

	public TegGame getGame() {
		return _game;
	}

  public GameDefinition getDefinition() {
    return _definition;
  }
}
