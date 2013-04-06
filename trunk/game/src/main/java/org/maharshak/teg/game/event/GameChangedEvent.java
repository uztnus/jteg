/**
 * 
 */
package org.maharshak.teg.game.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.maharshak.teg.board.Card;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
public class GameChangedEvent {

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "GameChangedEvent [" + _type + "| " + _player.getName() + "| "
				+ (_data != null ? toString(_data.entrySet(), maxLen) : null) + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	private EventType _type;
	private Player _player;
	private Map<DataType,String> _data;
	
  public static enum DataType {
    PERSON_UUID, NEW_STATE, CARDS, ARMIES, DST, SRC, MSG, DICE_ATTACKER, DICE_DEFENDER, REASON, COLORS;

    public static String createCardIdData(Card c1, Card c2, Card c3) {
      return c1.getCountry().getName() + "," + c2.getCountry().getName() + "," + c3.getCountry().getName();
    }

  };

	public GameChangedEvent(EventType type, Player p) {
		_type=type;
		_player=p;
		_data=new HashMap<GameChangedEvent.DataType, String>();
	}

	public EventType getType() {
		return _type;
	}
	public GameChangedEvent putData(DataType type,String value ){
		_data.put(type,value);
		return this;
	}
	
	public String get(DataType type){
		return _data.get(type);
	}

	public String getAll() {
		String res="{";
		for (DataType dt : _data.keySet()) {
			res+=dt+":"+get(dt)+",";
		}
		return res+"}";
	}
	
	public Player getPlayer() {
		return _player;
	}
}
