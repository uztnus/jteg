package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.Dice;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.ThrowResult;
import org.maharshak.teg.game.event.EventType;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


@XmlRootElement
public class AttackRequest extends PlayerRequest{



	private Country _dst;
	private Country _src;
	private ThrowResult _dr;
	private volatile boolean _conqueredCountry=false;

	public AttackRequest() {

	}

	public AttackRequest(Person p, Country src, Country dst) {
		super(p);
		_src=src;
		_dst=dst;
	}

	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctState=p.getState().atLeast(PLAYER_STATE.TURNOSTART)&&p.getState().lessEqualThen(PLAYER_STATE.MOVE_AFTER_CONQUEST);
		boolean countryOk= _src!=null && _dst!=null;
		boolean correctOwnership = p == p.getGame().getOwner(_src)
				&& p != p.getGame().getOwner(_dst);
		boolean bordersAndArmies = p.getGame().getArmies(_src) > 1
				&& p.getGame().getBoard().haveBorders(_src, _dst);
		boolean valid = correctState&&countryOk&&correctOwnership&&bordersAndArmies;
		return valid;

	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		clearMoveFromPreviousConquest(); 
		Player p = g.getPlayer(getOwner());
		p.getGame().startAttack(p, _src, _dst);
		Player defender = p.getGame().getOwner(_dst);
		/* so far, attack... */
		if (_dr == null) {// debug
			_dr = Dice.getDice().throwDice(
					Dice.getDice().getAttack(p.getGame(), _src, true),
					Dice.getDice().getAttack(p.getGame(), _dst, false));
		}
		diceThrown(_dr, p.getGame(), _src, _dst);
	}

	/**
	 * @param src
	 * @param dst
	 * @param defender
	 * @return if country conquered
	 */
	protected static void diceThrown(ThrowResult dr, TegGame g, Country src,
			Country dst) {
		Player attacker = g.getOwner(src);
		Player defender = g.getOwner(dst);
		g.setArmies(src, g.getArmies(src) - dr.getArmiesLostBySource());
		g.setArmies(dst, g.getArmies(dst) - dr.getArmiesLostByDestenation());
		/* updated statistics */
		attacker.getPlayer_stats().setArmies_killed(
				attacker.getPlayer_stats().getArmies_killed()
						+ dr.getArmiesLostByDestenation());
		defender.getPlayer_stats().setArmies_killed(
				defender.getPlayer_stats().getArmies_killed()
						+ dr.getArmiesLostBySource());
		/* conquisto el country */
		if (g.getArmies(dst) == 0) {
			g.getTurnData().addCounqueredCountry();
			g.setOwner(dst, attacker);
			g.setArmies(dst, g.getArmies(dst) + 1); /* se pasa automaticamente */
			g.setArmies(src, g.getArmies(src) - 1); /* un ejercito */
			attacker.setState(PLAYER_STATE.MOVE_AFTER_CONQUEST);
			/* updated statistics */
			attacker.getPlayer_stats().setCountries_won(
					attacker.getPlayer_stats().getCountries_won() + 1);
		}
		/* tell everybody the result of the attack */

		g.gameStateChanged();
		g.notifyObservers(new GameChangedEvent(EventType.ATTACK_FINISHED, attacker)
				.putData(DataType.DICE_ATTACKER, dr.printDiceSrc()).putData(
						DataType.DICE_DEFENDER, dr.printDiceDst())

		);

	}




	private String encode(ThrowResult dr) {
		return dr.printDiceSrc()+":"+dr.printDiceDst();
	}


	/**
	 * @param p
	 */
	protected void clearMoveFromPreviousConquest() {

	}

	@Override
	public String human() {
		return "Attack  " +((_conqueredCountry)?"[!!!]":"")+_src.getName()+"(-"+_dr.getArmiesLostBySource()+")" +"->>"+_dst.getName()+ 
				"(-"+_dr.getArmiesLostByDestenation()+")" + "Dice:" + _dr.printDiceSrc()+" -> "+_dr.printDiceDst();
	}

	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctState=p.getState().atLeast(PLAYER_STATE.TURNOSTART)&&p.getState().lessEqualThen(PLAYER_STATE.MOVE_AFTER_CONQUEST);
		boolean countryOk= _src!=null && _dst!=null;
		boolean correctOwnership = p == p.getGame().getOwner(_src)
				&& p != p.getGame().getOwner(_dst);
		boolean bordersAndArmies = p.getGame().getArmies(_src) > 1
				&& p.getGame().getBoard().haveBorders(_src, _dst);
		boolean valid = correctState&&countryOk&&correctOwnership&&bordersAndArmies;
		if(valid)
			return "valid";
		if(!correctState)
			return "Wrong state " +p.getState();
		if(!countryOk)
			return "bad country ";
		if(!correctOwnership)
			return "doesnt own src , or owns dest";
		if(!bordersAndArmies)
			return "not enough armies or no borders";
		return super.whyNotValid(g);
	}

	public boolean isConqueredCountry() {
		return _conqueredCountry;
	}

	private static int[] getDice(String string) {
		String[] tmp = string.split(",");
		int[] res = new int[tmp.length];
		for (int i = 0; i < res.length; i++) {
			res[i]=Integer.parseInt(tmp[i]);
		}
		return res;
	}

	public ThrowResult getDr() {
		return _dr;
	}

	public void setDr(ThrowResult dr) {
		_dr = dr;
	}

	public Country getDst() {
		return _dst;
	}

	public Country getSrc() {
		return _src;
	}

	public void setConqueredCountry(boolean conqueredCountry) {
		_conqueredCountry = conqueredCountry;
	}

	public void setDst(Country dst) {
		_dst = dst;
	}

	public void setSrc(Country src) {
		_src = src;
	}

}
