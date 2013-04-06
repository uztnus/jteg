package org.maharshak.teg.game.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.util.PutAdapter;


public abstract class AbstractPutArmyRequest extends PlayerRequest{

	@XmlElement(name = "armiesToPut")
	@XmlJavaTypeAdapter(PutAdapter.class)
	private Map<Country, Integer> _armiesToPut;

	public AbstractPutArmyRequest() {
		// TODO Auto-generated constructor stub
	}

	protected AbstractPutArmyRequest(Person p, Map<Country, Integer> armies) {
		super(p);
		_armiesToPut=armies;

	}

	/**
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {

		Player p = g.getPlayer(getOwner());
		boolean status = validatePlayerStatus(g);

		boolean turnOwner = p.getGame().getTurnOwner() == p;
		String validPlacement = checkArmies(getHowManyToPut(g), p);
    boolean isValid = status && turnOwner && validPlacement == null;
		return isValid ;
	}
	
	

	abstract protected int getHowManyToPut(TegGame g);

	protected abstract boolean validatePlayerStatus(TegGame g);

  /**
	 * 
	 * @param maximo
	 *          armies to put
	 * @param p
	 * @return null if all ok , and error string otherwise
	 */
	private String checkArmies(int maximo, Player p) {

		List<Continent> conts = p.continentsOwned();
		Map<Continent,Integer> armiesPerCont= new HashMap<Continent, Integer>();
		int sum=0;
		for (Country c : _armiesToPut.keySet()) {
			if (p.getGame().getOwner(c) != p)
				return "Player " + p + " doesn't own " + c.getName();
			int armies = _armiesToPut.get(c);
			if(armies<0)
        return "Negative armies to put on " + armies;
			sum+=armies;
      if (conts.contains(c.getContinent())) {
				Continent con = c.getContinent();
				if(conts.contains(con)){
					Integer num = armiesPerCont.get(con);
					armiesPerCont.put(con, ((num==null)?armies:num+armies));
				}
			}

		}
		if( sum != maximo ){
      return "Wrong number of armies put  should be " + maximo + " but put " + sum;
		}
    return validateConts(armiesPerCont);
	}

  /**
   * 
   * @param armiesPerCont
   *          that are put
   * @return null if all ok or error string otherwise
   */
  private static String validateConts(Map<Continent, Integer> armiesPerCont) {
		/* Did I have to place armies in continents ? */

		for (Continent c : armiesPerCont.keySet()) {
      int armiesPut = armiesPerCont.get(c);
      if (armiesPut < c.getChips())
        return "For " + c.getName() + " should be put " + c.getChips() + " but put only " + armiesPut;
		}
    return null;
	}

	/**
	 * @see org.maharshak.teg.game.request.PlayerRequest#perform()
	 */
	@Override
	public void runCommand(TegGame g) throws TegRequestException {
		boolean real=true;
		for (Country c : _armiesToPut.keySet()) {
			int armiesToPut = _armiesToPut.get(c);		

			/* cuando real==1 se asignas los ejercitos, sino hace el test */
			/* When real== 1, assign armies but does the test */
			if(real) {
				
				Player p = g.getPlayer(getOwner());
				p.getGame().setArmies(c, p.getGame().getArmies(c) + armiesToPut);
			}
			if(!real) {
				real=true;
			}
		}
		postPut(g);
	}
	
	protected abstract void postPut(TegGame g) throws TegRequestException;
	
	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean status = validatePlayerStatus(g);
		
		boolean turnOwner = p.getGame().getTurnOwner() == p;
		String validInput = checkArmies(getHowManyToPut(g), p);
    boolean isValid = status && turnOwner && validInput == null;
		if(!status)
			return "Wrong player state "+p.getState();
		if(!turnOwner)
			return "Wrong turn owner , not " + p.getGame().getTurnOwner();
    if (validInput != null)
      return "Wrong placement " + validInput;
		return super.whyNotValid(g);
			
	}
	
	@Override
	public String human() {
		StringBuffer sb=new StringBuffer();
		sb.append("Putting armies :").append("\n");
		for (Country c : _armiesToPut.keySet()) {
			sb.append("\t").append(c.getName()).append(":").append(_armiesToPut.get(c)).append("\n");
		}
		return sb.toString();
	}
	
	

	public Map<Country, Integer> getArmiesToPut() {
		return _armiesToPut;
	}
	
  public static void print(Map<Country, Integer> in) {
    for (Country c : in.keySet()) {
      System.out.println(c.getContinent().getName() + "|" + c.getName() + ":" + in.get(c));
    }
  }
}
