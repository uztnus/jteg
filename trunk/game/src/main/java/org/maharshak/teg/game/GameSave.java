package org.maharshak.teg.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.maharshak.teg.game.request.PlayerRequest;

public class GameSave implements Serializable {
	private static String DEF_FILE = File.separator + "def.xml";
	private static String REQ_FILE = File.separator + "req";
	private static JAXBContext _jaxbContext;
	private GameDefinition _def;
	private List<PlayerRequest> _playedRequests;
  private List<Integer> _dice;

	public GameSave() {
		_playedRequests = new ArrayList<PlayerRequest>();
	}
	public GameSave(GameDefinition def) {
		this();
		_def = def;
	}

	public void addCommand(PlayerRequest req) {
		_playedRequests.add(req);
	}

	public void save(String path) throws JAXBException, IOException {
		File f = new File(path);
		f.mkdirs();

		saveJaxB(_def, path + DEF_FILE);
		saveJaxB(_playedRequests, path + REQ_FILE);

	}

	private <T> void saveJaxB(T t, String path) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		FileWriter fw = new FileWriter(path);
		jaxbMarshaller.marshal(t, fw);
	}

	private <T> void saveJaxB(List<T> def, String path) throws JAXBException,
			IOException {
		for (int i = 0; i < def.size(); i++) {

			String path2 = path + String.format("_%06d.xml", i);
			if (!new File(path2).exists())
				saveJaxB(def.get(i), path2);
		}
	}

	public static GameSave load(String saveDir) throws FileNotFoundException, JAXBException {
		GameSave gs = new GameSave();
		gs._def = (GameDefinition) loadJaxB(saveDir + DEF_FILE);
		File f = new File(saveDir);
		String[] reqs = f.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {

				return name.startsWith(REQ_FILE.substring(1));
			}
		});
		Arrays.sort(reqs);
		for (String reqPath : reqs) {
			gs._playedRequests.add((PlayerRequest) loadJaxB(saveDir + File.separator
					+ reqPath));
		}
    return gs;
  }

	public static JAXBContext generateContext() throws JAXBException {
		if (_jaxbContext == null)
			_jaxbContext = JAXBContext
					.newInstance("org.maharshak.teg.game.request:org.maharshak.teg.util:org.maharshak.teg.game");
		return _jaxbContext;
	}

	private static <T> T loadJaxB(String path) throws FileNotFoundException,
			JAXBException {
		JAXBContext jaxbContext = generateContext();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		FileReader reader = new FileReader(path);
		T res = (T) unmarshaller.unmarshal(reader);
		return res;
	}

	public GameDefinition getDef() {
    return _def;
  }

  public Iterator<PlayerRequest> iterator(TegGame g) {
		return _playedRequests.iterator();
  }

	public List<PlayerRequest> getRequests() {
    return _playedRequests;
  }

  public List<Integer> getDice() {
    return _dice;
  }
}
