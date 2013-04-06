package org.maharshak.teg.game.event;

import java.util.HashMap;
import java.util.Map;

import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.player.Person;

public class GameConfigurationChangedEvent {
  private EventType _type;
  private Person _owner;
  private Map<DataType, String> _data;

  public GameConfigurationChangedEvent(EventType type, Person p) {
    _type = type;
    _owner = p;
    _data = new HashMap<GameChangedEvent.DataType, String>();
  }


  public EventType getType() {
    return _type;
  }

  public GameConfigurationChangedEvent putData(DataType type, String value) {
    _data.put(type, value);
    return this;
  }

  public String get(DataType type) {
    return _data.get(type);
  }

  public String getAll() {
    String res = "{";
    for (DataType dt : _data.keySet()) {
      res += dt + ":" + get(dt) + ",";
    }
    return res + "}";
  }

  public Person getOwner() {
    return _owner;
  }

}
