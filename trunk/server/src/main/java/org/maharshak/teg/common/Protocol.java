package org.maharshak.teg.common;

public class Protocol {

	//verseion
	public static final int PROTOCOL_HIVER=6;

	public static final int PROTOCOL_LOVER=0;
	public static final int PROT_MAX_LEN=5000;

	/* Poner todo en minuscula */
	//Put everything in lower case

	/* tokens */
	public static enum TOKEN{
		TOKEN_CVERSION("client_version"),
		TOKEN_DISCON("disconnect"),
		TOKEN_EXIT("exit"),
		TOKEN_GAMEOVER("game_over"),
		TOKEN_HELP("help"),
		TOKEN_LOST("loser"),
		TOKEN_MESSAGE("msg"),
		TOKEN_PLAYERID(		"player_id"),
		TOKEN_RECONNECT(		"reconnect"),
		TOKEN_NEWPLAYER	(	"newplayer"),
		TOKEN_PVERSION(		"protocol_version"),
		TOKEN_QUMM(		"quiero_un_mundo_mejor"),
		TOKEN_REM(		"*"),
		TOKEN_ROBOT(		"quiero_jugar_con_un_robot"),
		TOKEN_SCORES(		"scores"),
		TOKEN_STATS	(	"stats"),
		TOKEN_SERVERFULL(	"el_servidor_esta_lleno"),
		TOKEN_GAMEINPROGRESS(	"game_in_progress"),
		TOKEN_SVERSION	(	"server_version"),
		TOKEN_ALONE	(	"solo_a_ningun_lado"),
		TOKEN_START	(	"start"),
		TOKEN_STATUS(		"status"),
		TOKEN_TEST	(	"eva_test"),
		TOKEN_TURNO	(	"turno"),
		TOKEN_WAIT	(	"wait"),
		TOKEN_WINNER(		"winner"),
		TOKEN_ERROR	(	"error"),
		TOKEN_OK	(	"ok"),
		TOKEN_COUNTRIES(		"paises"),
		TOKEN_FICHAS	(	"fichas"),
		TOKEN_FICHAS2	(	"fichas2"),
		TOKEN_FICHASC	(	"fichasc"),
		TOKEN_FICHAS3	(	"fichas3"),
		TOKEN_ATAQUE	(	"ataque"),
		TOKEN_TROPAS	(	"tropas"),
		TOKEN_COUNTRY	(	"pais"),
		TOKEN_REAGRUPE	(	"reagrupe"),
		TOKEN_TARJETA	(	"tarjeta"),
		TOKEN_DADOS		("dados"),
		TOKEN_CANJE		("canje"),
		TOKEN_EJER2		("ejer2"),
		TOKEN_MODALIDAD(		"modalidad"),
		TOKEN_MISSION	(	"mission"),
		TOKEN_SET	(	"set"),
		TOKEN_VIEW	(	"view"),
		TOKEN_COLOR	(	"color"),
		TOKEN_SAVE	(	"save"),
		TOKEN_LOQUE	(	"que_tengo_que_hacer"),
		TOKEN_ECHO	(	"echo"),
		TOKEN_GGZ	(	"avoding_perdig_effect"),
		TOKEN_PACTO_REQUEST	("pacto_request"),
		TOKEN_PACTO_REPLY	("pacto_reply"),
		TOKEN_SURRENDER	(	"soy_cobarde"),
		TOKEN_KICK	(	"kick"),
		TOKEN_ENUM_CARDS(	"enum_cards"),
		TOKEN_NEW_ROUND	(	"new_round"),
		TOKEN_METASERVER(	"metaserver"),
		/* options */
		 OPTION_CONQWORLD	("conquer_world",false,true),
		 OPTION_ARMIES		("armies",false,true),
		 OPTION_SEED		("seed",false,true),
		 OPTION_HELP		("help",false,true),
		 OPTION_CMISSION	(	"common_mission",false,true),
		 OPTION_FOG_OF_WAR	("fog_of_war",false,true),
		/* metaserver */
		 OPTION_META_LIST	("list",true,true),
		 OPTION_META_ON		("on",true,true),
		 OPTION_META_OFF	(	"off",true,true),
		 OPTION_META_ADDRESS(	"address",true,true),
		 OPTION_META_STATUS	("status",true,true)

		;
		private final boolean _hasCommand;
		private TOKEN(String s){
			_token=s;
			_hasCommand=false;
			
		}
		
		private TOKEN(String s, boolean hasCommand){
			_token=s;
			_hasCommand=hasCommand;
			
		}
		@Override
		public String toString() {
			return getToken();
		}

		private TOKEN(String s,boolean isMeta,boolean isOption){
			this(s);
			_isMeta=isMeta;
			_isOption=isOption;

		}


		private boolean _isMeta=false;
		private boolean _isOption=false;
		private String _token;
		public String getToken(){return _token;}
		public static TOKEN getToken(String token){
			for (TOKEN t : values()) {
				if(t._token.compareTo(token)==0)
					return t;
			}
			return null;
		}
		
		public boolean is_isMeta() {
			return _isMeta;
		}

	}



}
