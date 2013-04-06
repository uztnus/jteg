package org.maharshak.teg.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.GameManager;
import org.maharshak.teg.game.GameSave;
import org.maharshak.teg.game.GameState;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.ExchangeCards;
import org.maharshak.teg.game.request.PlayerRequest;

@RunWith(Parameterized.class)
public class SerializedGamesTest {
  public static String RESOURCE_PREFIX = "src/test/resources/";
	private final String _gameSavePath;
  private TegGame _g;
  private GameSave _save;


  @Parameters
  public static Collection<Object[]> getSerializedGameFiles() throws TegRequestException {
    File f = new File(RESOURCE_PREFIX);
    File[] gameDirs = f.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
				return pathname.isDirectory();
      }
    });

    List<Object[]> res = new ArrayList<Object[]>();
    for (File file : gameDirs) {
			res.add(new Object[] { file.getAbsolutePath() });
    }
    return res;


  }

	public SerializedGamesTest(String f) {
    _gameSavePath = f;
  }

  @Before
	public void init() throws IOException, JAXBException {
    GameSave gs = GameSave.load(_gameSavePath);

    GameDefinition def = gs.getDef();
		def.setGameManager(new GameManager());
		_g = def.getManager().create(def.getId());
    _save = gs;
  }

  /**
   * @param gameFile
   * @throws TegRequestException
   */
  @Test
  public void testAllValidAndNoExeptions() throws TegRequestException {
    int reqNum = 0;
    Iterator<PlayerRequest> it = _save.iterator(_g);
    while (it.hasNext()) {
      PlayerRequest req = it.next();

			if (!req.validate(_g))
				Assert.fail("#" + reqNum + "." + req.human() + " not vaid due "
						+ req.whyNotValid(_g));
			req.perform(_g);
      reqNum++;
    }
    System.err.println("HAd " + reqNum + " reqs");
  }

  /**
   * @param gameFile
   * @throws TegRequestException
   */
  @Test
  public void testExchanges() throws TegRequestException {
    Iterator<PlayerRequest> it = _save.iterator(_g);
    while (it.hasNext()) {
      PlayerRequest req = it.next();
      int exchangesBefore = -1;
      if (req instanceof ExchangeCards)
				exchangesBefore = _g
						.getExchangesPerformed(_g.getPlayer(req.getOwner()));
			req.perform(_g);
      if (exchangesBefore > -1) {
				Assert.assertEquals("exchange amount should be updated",
						exchangesBefore + 1,
						_g.getExchangesPerformed(_g.getPlayer(req.getOwner())));
      }
    }
  }


  // /**
  // * @param gameFile
  // * @throws TegRequestException
  // */
  // @Test
  // public void testRoundStarter() throws TegRequestException {
  // List<Player> players=null;
  // Player prev=null;
  // Player curr=null;
  // while(true){
  // PlayerRequest req = _man.next();
  // if(req==null)
  // break;
  //
  // if(req instanceof StartGameRequest){//start cheking
  // players = _g.getPlaying();
  // }
  // if(players!=null){
  // prev=curr;
  // curr=_g.getRoundStarter();
  // //FIXME
  // }
  // }
  // }



  /**
   * @param gameFile
   * @throws TegRequestException
   */
  @Test
  public void testCountriesOk() throws TegRequestException {
    int reqNum = 0;
    Iterator<PlayerRequest> it = _save.iterator(_g);
    while (it.hasNext()) {
      PlayerRequest req = it.next();
			if (_g.getState().atLeast(GameState.FIRST_PUT)) {
        for (Country c : _g.getBoard().getCountries()) {
          if (_g.getOwner(c) == null || _g.getArmies(c) < 1)
            Assert.fail("#" + reqNum + ".Country " + c.getName() + " is owned by " + (_g.getOwner(c)) + " with armies " + _g.getArmies(c)
                + " at req " + req);
        }
      }
			req.perform(_g);
			reqNum++;
    }
  }



  private void printBoard(TegGame g) {
    for (Country c : g.getBoard().getCountries()) {
      System.out.println(c.getName() + "|" + (g.getOwner(c)) + "|" + g.getArmies(c));
    }

  }

  @Override
  public String toString() {
		return _gameSavePath;
  }
}
