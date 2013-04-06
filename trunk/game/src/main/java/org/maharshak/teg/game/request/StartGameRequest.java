/**
 * 
 */
package org.maharshak.teg.game.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class StartGameRequest extends PlayerRequest {

	private int _gameStarter = -1;
	private List<List<Country>> _putCountries;

	public StartGameRequest(Person p) {
		super(p);
	}



	public StartGameRequest() {
		super(null);
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {

		Player p = g.getPlayer(getOwner());
		boolean gameState = !g.isInPlay();

		boolean isPlayer = p.isHuman();
		return gameState && isPlayer;
	}

	public void setGameStarter(int gameStarter) {
		_gameStarter = gameStarter;
	}

	public int getGameStarter() {
		return _gameStarter;
	}

	public void setPutCountries(Countries[] putCountries) {
		_putCountries = new ArrayList<List<Country>>();
		for (Countries countries : putCountries) {
			_putCountries.add(countries.get());
		}
	}

	@XmlElementWrapper(name = "country-split")
	@XmlElement(name = "perPlayer")
	public Countries[] getPutCountries() {
		return listToArr(_putCountries);
	}
	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {

		Player p = g.getPlayer(getOwner());
		if(_putCountries==null){

			_putCountries = p.getGame().getBoard()
					.splitCountries(p.getGame().getPlayerList().size());
		}

		/* initializes the round. Called before the game starts */
		if (_gameStarter == -1) {// debug support
			_gameStarter = (int) (Math.random() * p.getGame().getPlayerList().size());
		}
		p.getGame().start(_putCountries, _gameStarter);
		if (_gameStarter == -1)
			throw new TegRequestException("Failed to find someone to start the game");

	}




	@Override
	public String human() {
		return "Startign game : player number " + _gameStarter;
	}

	public static Countries[] listToArr(List<List<Country>> putCountries) {
		Countries[] res = new Countries[putCountries.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = new Countries(putCountries.get(i));
		}
		return res;
	}

	@XmlRootElement
	public static class Countries {

		private Country[] _countries;

		public Countries() {
		}

		public Countries(List<Country> cs) {
			setCountries(cs);
		}

		public void setCountries(Country[] countries) {
			_countries = countries;
		}

		public void setCountries(List<Country> countries) {
			_countries = countries.toArray(new Country[] {});
		}

		@XmlElement(name = "country")
		public Country[] getCountries() {
			return _countries;
		}

		public List<Country> get() {
			return Arrays.asList(_countries);
		}

		@Override
		public String toString() {
			String res = "";
			for (Country c : _countries) {
				res += c.getName() + ",";
			}
			return res;
		}

	}

}
