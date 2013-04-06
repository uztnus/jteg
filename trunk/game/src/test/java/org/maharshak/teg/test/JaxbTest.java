package org.maharshak.teg.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;

import org.junit.Test;
import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.ThrowResult;
import org.maharshak.teg.game.request.AttackRequest;
import org.maharshak.teg.game.request.CardRequest;
import org.maharshak.teg.game.request.ExchangeCards;
import org.maharshak.teg.game.request.FinishTurnRequest;
import org.maharshak.teg.game.request.InitialPutArmiesRequest;
import org.maharshak.teg.game.request.MessageRequest;
import org.maharshak.teg.game.request.MoveAfterConquestRequest;
import org.maharshak.teg.game.request.PutArmyRequest;
import org.maharshak.teg.game.request.PutCardsTwoArmies;
import org.maharshak.teg.game.request.RegroupRequest;
import org.maharshak.teg.game.request.StartGameRequest;
import org.maharshak.teg.game.request.SurrenderRequest;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.util.PutAdapter.AdaptedPutMap;
import org.maharshak.teg.util.PutAdapter.AdaptedPutMap.Put;

public class JaxbTest {

	private static JAXBContext _jaxbContext;

	@Test
	public void testStartGame() throws JAXBException, IOException {
		Person p = new Person();
		StartGameRequest req = new StartGameRequest(p);
		Board b = Board.get(0);
		req.setPutCountries(StartGameRequest.listToArr(b.splitCountries(6)));

		req.setGameStarter(2);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		StartGameRequest r = load(tt);
		System.out.println(r.getGameStarter());
		System.out.println(Arrays.toString(r.getPutCountries()));
	}

	@Test
	public void testGetCard() throws JAXBException, IOException {
		Person p = new Person();
		CardRequest req = new CardRequest(p);
		Board b = Board.get(0);
		req.setCard(b.getDeck().get(3));

		String tt = save(req, req.getClass());
		System.out.println(tt);
		CardRequest r = load(tt);
		System.out.println(r.getCard());
	}

	@Test
	public void testExchange() throws JAXBException, IOException {
		Person p = new Person();
		Board b = Board.get(0);
		ExchangeCards req = new ExchangeCards(p, b.getDeck().get(23),
 b.getDeck()
				.get(25), b.getDeck().get(24));

		String tt = save(req, req.getClass());
		System.out.println(tt);
		ExchangeCards r = load(tt);
		System.out.println(r.human());
	}

	@Test
	public void testFinishTurn() throws JAXBException, IOException {
		Person p = new Person();

		FinishTurnRequest req = new FinishTurnRequest(p);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		FinishTurnRequest r = load(tt);
		System.out.println(r.human());
		System.out.println(r.getOwner());
		Assert.assertEquals(req.getOwner().getId(), r.getOwner().getId());
	}

	@Test
	public void testMessage() throws JAXBException, IOException {
		Person p = new Person();

		String msg = "this is a test";
		MessageRequest req = new MessageRequest(p, msg);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		MessageRequest r = load(tt);

		System.out.println(r.getOwner());
		System.out.println(r.getMsg());
		Assert.assertEquals(req.getOwner().getId(), r.getOwner().getId());
		Assert.assertEquals(req.getMsg(), r.getMsg());
	}

	@Test
	public void testMoveAfterConquest() throws JAXBException, IOException {
		Person p = new Person();

		String msg = "this is a test";
		Board b = Board.get(0);
		MoveAfterConquestRequest req = new MoveAfterConquestRequest(p, b
				.getCountries().get(23), b.getCountries().get(27), 127);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		MoveAfterConquestRequest r = load(tt);

		System.out.println(r.human());

		Assert.assertEquals(req.getAmount(), r.getAmount());
		Assert.assertEquals(req.getSource().getName(), r.getSource().getName());
		Assert.assertEquals(req.getDestination().getName(), r.getDestination()
				.getName());
	}

	@Test
	public void testPutTwoOnCard() throws JAXBException, IOException {
		Person p = new Person();
		Board b = Board.get(0);

		PutCardsTwoArmies req = new PutCardsTwoArmies(p, b.getDeck().get(22));

		String tt = save(req, req.getClass());
		System.out.println(tt);
		PutCardsTwoArmies r = load(tt);
		System.out.println(r.getCard());
		Assert.assertEquals(req.getCard().toString(), r.getCard().toString());
	}

	@Test
	public void testSurrender() throws JAXBException, IOException {
		Person p = new Person();

		SurrenderRequest req = new SurrenderRequest(p);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		SurrenderRequest r = load(tt);
		System.out.println(r.human());
		System.out.println(r.getOwner());
		Assert.assertEquals(req.getOwner().getId(), r.getOwner().getId());
		Assert.assertEquals(req.getState(), r.getState());
	}

	@Test
	public void testRegroup() throws JAXBException, IOException {
		Person p = new Person();

		Board b = Board.get(0);
		RegroupRequest req = new RegroupRequest(p, b.getCountries().get(23), b
				.getCountries().get(27), 127);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		RegroupRequest r = load(tt);

		System.out.println(r.human());

		Assert.assertEquals(req.getAmount(), r.getAmount());
		Assert.assertEquals(req.getSrc().getName(), r.getSrc().getName());
		Assert.assertEquals(req.getDst().getName(), r.getDst()
				.getName());
	}

	@Test
	public void testAttack() throws JAXBException, IOException {
		Person p = new Person();

		Board b = Board.get(0);
		AttackRequest req = new AttackRequest(p, b.getCountries().get(23), b
				.getCountries().get(27));
		req.setDr(new ThrowResult(2, 2, new int[] { 1, 2, 2, 1 }));
		String tt = save(req, req.getClass());
		System.out.println(tt);
		AttackRequest r = load(tt);

		System.out.println(r.human());

		Assert.assertEquals(req.getSrc().getName(), r.getSrc().getName());
		Assert.assertEquals(req.getDst().getName(), r.getDst().getName());
	}

	@Test
	public void testInitialPut() throws JAXBException, IOException {
		Person p = new Person();

		Board b = Board.get(0);
		Map<Country, Integer> cMap = new HashMap<Country, Integer>();
		cMap.put(b.getCountries().get(23), 26);
		cMap.put(b.getCountries().get(24), 17);
		cMap.put(b.getCountries().get(25), 86);
		InitialPutArmiesRequest req = new InitialPutArmiesRequest(p, cMap,
				PLAYER_STATE.FIRST_PUT);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		InitialPutArmiesRequest r = load(tt);

		System.out.println(r.human());
		Assert.assertEquals(req.getPlayerState(), r.getPlayerState());

	}

	@Test
	public void testInGamePut() throws JAXBException, IOException {
		Person p = new Person();

		Board b = Board.get(0);
		Map<Country, Integer> cMap = new HashMap<Country, Integer>();
		cMap.put(b.getCountries().get(23), 26);
		cMap.put(b.getCountries().get(24), 17);
		cMap.put(b.getCountries().get(25), 86);
		PutArmyRequest req = new PutArmyRequest(p, cMap);

		String tt = save(req, req.getClass());
		System.out.println(tt);
		PutArmyRequest r = load(tt);

		System.out.println(r.human());
		// Assert.assertEquals(req.getPlayerState(), r.getPlayerState());

	}

	@Test
	public void testPut() throws IOException, JAXBException {
		Board b = Board.get(0);
		Map<Country, Integer> cMap = new HashMap<Country, Integer>();
		cMap.put(b.getCountries().get(23), 26);
		cMap.put(b.getCountries().get(24), 17);
		cMap.put(b.getCountries().get(25), 86);
		AdaptedPutMap map = new AdaptedPutMap(cMap);
		String tt = save(map, map.getClass());
		AdaptedPutMap r = load(tt);
		Put[] dd = r.getPut();
		Assert.assertTrue(dd.length > 0);


	}



	public static <T> T load(String xml) throws JAXBException {
		// JAXBContext jaxbContext = JAXBContext.newInstance(c);
		JAXBContext jaxbContext = generateContext();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(xml);
		T res = (T) unmarshaller.unmarshal(reader);
		return res;
	}

	public static JAXBContext generateContext() throws JAXBException {
		if (_jaxbContext == null)
			_jaxbContext = JAXBContext
					.newInstance("org.maharshak.teg.game.request:org.maharshak.teg.util:org.maharshak.teg.game");
		return _jaxbContext;
	}

	public static <T> String save(T obj) throws JAXBException {

		JAXBContext jaxbContext = generateContext();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(obj, sw);
		return sw.toString();

	}

	public static <T> String save(T obj, Class<? extends T> c) throws JAXBException{


		JAXBContext jaxbContext = JAXBContext.newInstance(c);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw=new StringWriter();
		jaxbMarshaller.marshal(obj, sw);
		return sw.toString();

	}


}
