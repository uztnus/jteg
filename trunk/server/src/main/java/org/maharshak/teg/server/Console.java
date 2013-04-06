package org.maharshak.teg.server;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.maharshak.teg.common.Common;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.TokenActionFinder;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.common.ref.TokenAction;
import org.maharshak.teg.game.GameManager;
import org.maharshak.teg.server.tokenaction.ExitAction;
import org.maharshak.teg.server.tokenaction.HelpAction;
import org.maharshak.teg.server.tokenaction.KickAction;
import org.maharshak.teg.server.tokenaction.SaveAction;
import org.maharshak.teg.server.tokenaction.SetAction;
import org.maharshak.teg.server.tokenaction.StatsAction;
import org.maharshak.teg.server.tokenaction.StatusAction;
import org.maharshak.teg.server.tokenaction.TestAction;
import org.maharshak.teg.server.tokenaction.ViewAction;


public class Console {
	private static Logger _log=Logger.getLogger("console");
	public static final int CONSOLE_FD =0;
	private TokenActionFinder<Data> _consoleActionRunner=new TokenActionFinder<Data>(new TokenAction[]{
			new StatusAction(),
			new HelpAction(this),
			new TestAction(),
			new ExitAction(),
			new SaveAction(),
			new SetAction(),
			new ViewAction(),	
			new KickAction(),
			new StatsAction(),
			//			new ScoresAction(),
	});
	private GameManager _game;


	public Console(GameManager g) {
		_game=g;
	}

//	private volatile boolean _run=true;
//
//	public void start(){
//		Thread t=new Thread(this,"Console");
//		t.start();
//	}
//
//	public void stop(){
//		_run=false;
//	}


//	@Override
//	public void run() {
//		Scanner in = new Scanner(System.in);
//		String line;
//		addPrompt();
//		while(_run){
//
//			line= in.nextLine();
//			try{
//				// Prints name and age to the console
//				if(line!=null&&!line.isEmpty()){
//					processLine(line);
//					addPrompt();
//				}
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}catch (Exception e) {
//				_log.error("Error in action "+line,e);
//			}
//		}
//		in.close();		
//	}

	private void processLine(String line) {
		//		System.out.println(line);
    // String[] tmp = line.split(" ");
    // for (String comm : tmp) {
    // TEG_STATUS ts = runConsoleCommand( comm );
    // /* input from console */
    // if(ts==TEG_STATUS.TEG_STATUS_GAMEOVER ||
    // ts==TEG_STATUS.TEG_STATUS_CONNCLOSED)
    // //Main.server_exit(0,_game);
    // System.exit(0);
    // }
    throw new UnsupportedOperationException("Currently not supported");

	}



	private static void write(String msg){
		System.out.print(msg);
	}
	


	private TEG_STATUS runConsoleCommand( String comm )
	{

    // int i;
    // String[] tmp = comm.split("=");
    //
    // TokenAction<Data> ta = _consoleActionRunner.find(tmp[0]);
    // if(ta!=null)
    // return ta.act(new Data(null ,(tmp.length>1)?tmp[1]:null,_game));
    // con_text_out(Common.M_ERR,"Command '%s' not recongnized\n",tmp[0]);
    // con_text_out(Common.M_MSG,"Type '%s' for help\n",TOKEN.TOKEN_HELP);
    // return TEG_STATUS.TEG_STATUS_TOKENNOTFOUND;
    throw new UnsupportedOperationException("Currently not supported");
	}




	private static Console _console;


	public static TEG_STATUS con_text_out( int level, String format, Object ... data )
	{
		Level l=Level.DEBUG;
		switch(level){
			case Common.M_INF:
				l=Level.INFO;
				break;
			case Common.M_IMP:
				l=Level.FATAL;
				break;
			case Common.M_ERR:
				l=Level.ERROR;
				break;
			case Common.M_ALL:
				l=Level.FATAL;
				break;
			case Common.M_MSG:
			default:
		}
		String str = String.format(format,data);
		_log.log(l, str);

		write(str);
		

		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}



	public static TEG_STATUS con_text_out_wop( int level, String format,Object  ... data)
	{
		String str = String.format(format, data);
		
			getConsole().write(">>>"+str);
		
		_log.debug(str);
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public static TokenAction[] getActions() {
		return getConsole()._consoleActionRunner.getActions().toArray(new TokenAction[]{});
	}

	public void write(String format, String ... param ) {
		write(String.format(format, param));		
	}

	public static Console getConsole() {
		return _console;
	}

	public static void setConsole(Console console) {
		_console = console;
	}

}
