/**
 * 
 */
package org.maharshak.teg.player;

import java.io.Serializable;

import org.maharshak.teg.game.Mission;


/**
 * @author uztnus
 */
public class PlayerToBeData implements Serializable {

	private Person _person;
	private ChipsColor _color;
	private String _name;
	private boolean _human;

	private Mission _mission;

	public PlayerToBeData() {
		_color = ChipsColor.NA;
		_mission = Mission.g_missions[0];
	}

	public PlayerToBeData(Person p) {
		this();
		_person = p;

	}

	/**
	 * @return the color
	 */
	public ChipsColor getColor() {
		return _color;
	}

	/**
	 * @param color
	 *          the color to set
	 */
	public void setColor(ChipsColor color) {
		_color = color;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		_name = name;
	}

	public Person getPerson() {
		return _person;
	}

	public void setPerson(Person person) {
		_person = person;
	}

	public boolean isHuman() {
		return _human;
	}

	public void setHuman(boolean human) {
		_human = human;
	}


	public Mission getMission() {
		return _mission;
	}

	public void setMission(Mission mission) {
		_mission = mission;
	}
}
