package org.maharshak.teg.game;

public enum GameState{
	WAIT_FOR_PLAYERS,	/* no se comenzo y pueder ingresar playeres */
	REGISTRATION_FULL,	/* no pueden entrar mas playeres */
	FIRST_PUT,	/* poner las 5 fichas iniciales */
	SECOND_PUT,	/* poner las 3 fichas iniciales */
	ARMY_PUT,
	ATTACK	/* se esta en pleno ataque */,
	GAME_OVER;
	

	public boolean atLeast(GameState state) {
		return this.ordinal()>=state.ordinal();
	}
}