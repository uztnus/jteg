package org.maharshak.teg.test;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.util.BoardAdapter;

public class JaxbBoard {

	// @Test
	// public void testBoard() throws IOException, JAXBException {
	// Board b = Board.get(0);
	// String mm = saveBoard(b);
	// System.out.println(mm);
	// }

	@Test
	public void testBoard2() throws IOException, JAXBException {
		Board b = Board.get(0);
		BoardB bb = new BoardB();
		bb.setB(b);
		String mm = JaxbTest.save(bb, BoardB.class);
		System.out.println(mm);
	}

	public static String saveBoard(Board b) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(Board.class,
				BoardAdapter.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(b, sw);
		return sw.toString();

	}

	@Test
	public void testCountry() throws IOException, JAXBException {
		Country c = Board.get(0).getCountries().get(0);
		JAXBContext jaxbContext = JAXBContext.newInstance(c.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(c, sw);

		System.out.println(sw.toString());
	}

	@XmlRootElement
	public static class BoardB {
		private Board _b;

		public Board getB() {
			return _b;
		}

		public void setB(Board b) {
			_b = b;
		}

	}
}
