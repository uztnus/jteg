package org.maharshak.teg.server.infra;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.backoffice.IGameRunner;
import org.maharshak.teg.common.Common;
import org.maharshak.teg.game.GameSave;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.PlayerRequest;
import org.maharshak.teg.server.Console;
import org.maharshak.teg.server.request.ReadOnlyRequest;


public class SingleThreadedGameRunner implements IGameRunner{

	private LinkedBlockingQueue<PlayerRequest> _queue;
	private volatile boolean _alive=false;
	private  Logger _log=Logger.getLogger("request");
	private  Logger _logGame=Logger.getLogger("gameplay");
	private volatile TegGame _game;


	public static Map<String, GameSave> _saves = new HashMap<String, GameSave>();

	public SingleThreadedGameRunner() {
		_queue = new LinkedBlockingQueue<PlayerRequest>();
	}

	@Override
	public void init() {
		_queue.clear();
		_alive=true;
		Thread t = new Thread(new GameRequestConsumer(),"Request consumer");
		t.start();

	}

	@Override
	public void stop(){
		_alive=false;
		_queue.clear();
	}

	@Override
	public void addRequest(PlayerRequest command, TegGame g) {
		try {
			if (_game == null)
				_game = g;// Thats one ugly hack.....//FIXME
			_queue.put(command);
			if(!_alive)
				_queue.clear();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


	private class GameRequestConsumer implements Runnable{

		@Override
		public void run() {

			while(_alive){
				PlayerRequest command = null;
				try{
					command = _queue.poll(1000,TimeUnit.MILLISECONDS);
					if(command==null)
						continue;
					if (command.validate(_game)) {
						command.perform(_game);
						GameSave tt = _saves.get(_game.getGameID().toString());
						if (!(command instanceof ReadOnlyRequest)) {
							tt.addCommand(command);
							tt.save("target/save/" + _game.getGameID().toString());
						}
						_log.info("Performed command:"+command);
						_logGame.info(command.getOwner() + ":" + command.human());
					}else{
						_log.error("Not valid command sent " + command.whyNotValid(_game)
								+ " , " + command);
						Console.con_text_out(Common.M_ERR,
								"!!!ERR:Wrong command %s: %s , %s \n", command.getOwner(),
								command.whyNotValid(_game), command);
					}
				} catch (TegRequestException e) {
					_log.error(e); 
					Console.con_text_out(Common.M_ERR, "Error during request %s performance %s",command ,e);
				} catch (InterruptedException e) {
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}



	}


	@Override
	public void runCommands(List<PlayerRequest> commands, TegGame g) {
		for (PlayerRequest playerRequest : commands) {
			addRequest(playerRequest, g);
		}
	}



}
