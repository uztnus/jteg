package org.maharshak.teg.player;

public enum PLAYER_STATE {
	CONNECTED,	/**< connected */
	GAMEOVER,		/**< game over */
	ENABLED,	/**< enabled with color */
	START,		/**< started the gamej */
	FIRST_PUT,		/**< placing the initial armies */
	POST_FIRST_PUT,	/**< post */
	SECOND_PUT,		/**< placing the 2nd initial armies */
	POST_SECOND_PUT,	/**< post */
	IDLE,		/**< waiting turn */
	PUT_ARMIES,		/**< placing continets armies */
	EXCHANGE,		/**< exchaging cards */
	POST_ARMIES_PUT,	/**< post */
	DEAL,		/**< making a deal */
	TURNOSTART,	/**< starting an attack */
	ATAQUE,		/**< attacking */
	MOVE_AFTER_CONQUEST,		/**< moving armies to new country */
	REGROUP,		/**< regrouping armies */
	REQUEST_CARD,		/**< asking for a card */
	TURN_END,		/**< ending turn */
	LAST		;/**< unreacheble state */
	
	public boolean atLeast(PLAYER_STATE p){
		return this.ordinal()>=p.ordinal();
	}
	
	public boolean lessEqualThen(PLAYER_STATE p){
		 return this.ordinal()<=p.ordinal();
	}
	
	public boolean lessThen(PLAYER_STATE p){
		 return this.ordinal()<p.ordinal();
	}

	public PLAYER_STATE next() {
		return values()[this.ordinal()+1];
	}
}
