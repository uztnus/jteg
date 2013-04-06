package org.maharshak.teg.game;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.util.MissionAdapter;

@XmlJavaTypeAdapter(MissionAdapter.class)
public class Mission implements Serializable {
	public static Mission g_missions[]=new Mission[]{
		new Mission( 	/* Default mission for a game without missions */
				"Conquer the world",
				new int[]{6, 10, 6, 4, 9, 16},
				0,
				0 ,MissionType.MISSION_CONQWORLD),
				new Mission(	/* Objetivo comun */
						"Conquer 30 countries",
						new int[]{0, 0, 0, 0, 0, 0},
						0,
						30 ,MissionType.MISSION_COMMON),
						new Mission( 
								"Conquer &2,\n5 countries of &1 and\n4 countries of &4",
								new int[]{0, 5, 6, 0, 4, 0},
								0,
								0,MissionType.MISSION_COMMON ),


								/* 
								 * esta mision es dificil de soportar porque los countries limitrofes
								 * tiene que estar fuera de los continentes, y el manual no dice nada,
								 * etc, etc, encontes para evitar malos entendidos la desabilito
								 */
								new Mission( 
										"Conquer &0,\n7 countries of &4 and\n3 frontier countries",
										new int[]{6, 0, 0, 0, 7, 0},
										3,
										0,MissionType.MISSION_COMMON),


										new Mission( 
												"Conquer &5 and\n2 countries of &0",
												new int[]{2, 0, 0, 0, 0, 16},
												0,
												0,MissionType.MISSION_COMMON),

												new Mission( 
														"Conquer &4,\n4 countries of &5 and,\n2 of &0",
														new int[]{2, 0, 0, 0, 9, 4},
														0,
														0,MissionType.MISSION_COMMON),

														new Mission( 
																"Conquer &1,\n2 countries of &3 and\n4 countries of &5",
																new int[]{0, 10, 0, 2, 0, 4},
																0,
																0,MissionType.MISSION_COMMON),

																new Mission( 
																		"Conquer 2 countries of &3,\n2 of &2, 2 of &0,\n3 of &4, 4 of &1,\nand 3 of &5",
																		new int[]{2, 4, 2, 2, 3, 3},
																		0,
																		0,MissionType.MISSION_COMMON),

																		new Mission( 
																				"Conquer &3,\nconquer &1 and,\n2 countries of &4",
																				new int[]{0, 10, 0, 4, 2, 0},
																				0,
																				0,MissionType.MISSION_COMMON),

																				new Mission( 
																						"Conquer &0,\nconquer &2 and,\n4 countries of &5",
																						new int[]{6, 0, 6, 0, 0, 4},
																						0,
																						0,MissionType.MISSION_COMMON),

																						new Mission( 
																								"Conquer &3,\nconquer &2 and,\n5 countries of &1",
																								new int[]{0, 5, 6, 4, 0, 0},
																								0,
																								0,MissionType.MISSION_COMMON),

	};


	



	private String _name;			/**< name del mission */
	private int	_minCountriesPerContToOwn[];	/**< continentes a conquistar *///countries per cont to conquer
	private int	_minNeigboringCountries;			/**< countries limitrofes a tener */ //amount of bordering countries
	private int	_minCountriesToOwn;			/**< cantidad de countries a conquistar */ //number of countries to conquer
	private MissionType type;

	public 	enum MissionType{
		MISSION_CONQWORLD,			/**< mission 0. Conquistar el mundo */
		MISSION_COMMON,			/**< mission comun: Conquistar 30 countries */
	};


	private Mission(String name, int[] minCountriesPerContToOwn,
			 int minNeigboringCountries, int minCountriesToOwn,MissionType type) {
		_name = name;
		_minCountriesPerContToOwn=minCountriesPerContToOwn;
		_minNeigboringCountries= minNeigboringCountries;
		_minCountriesToOwn=minCountriesToOwn;
		this.type=type;
	}

	@XmlElement
	public String getName() {
		return _name;
	}

	public static int missions_cant()
	{
		return g_missions.length;
	}



	public MissionType getType() {
		return type;
	}

	

	public int getMinCountriesToOwn() {
		return _minCountriesToOwn;
	}


	public int[] getMinCountriesPerContToOwn() {
		return _minCountriesPerContToOwn;
	}


	public int getMinNeigboringCountries() {
		return _minNeigboringCountries;
	}

	

	/**
		 * @param pJ
		 * @param neighboursToHave
		 * @param game
		 * @return
		 */
	  private static boolean haveMinNegihbours(Player pJ, int neighboursToHave ) {
			int currentNighbours;
			TegGame game=pJ.getGame();
			boolean salir=false;
			for (Country j : game.getBoard().getCountries()) {
				if(game.getOwner(j) != pJ)
					continue;
				currentNighbours=0;
				for (Country k : game.getBoard().getCountries()) {
					if( game.getOwner(k) != pJ)
						continue;
					if(game.getBoard().haveBorders(j,k) ) {
						if( ++currentNighbours >= neighboursToHave ) {
							/* mission cumplido, salir */
							salir=true;
							return true;
						}
					}
				}
				if(salir)
					break;
			}
			
	
			return false;
		}



		/* Do player pJ accomplished his secret mission ? */
		public static boolean isMissionAccomplished( Player pJ )
		{
			int i;
	    TegGame game = pJ.getGame();
	
			assert( pJ !=null);
	
	
	
			Map<Continent, List<Country>> countries = Player.countryPerContinent( pJ);
	
			/* 1st part. Check that the player has the correct number of countries */
	    if (game.getConf().hasCommonMission() && pJ.getMission().getType() != MissionType.MISSION_CONQWORLD) {
				if( game.countriesOf(pJ).size()  >= g_missions[MissionType.MISSION_COMMON.ordinal()].getMinCountriesToOwn()) {
	//				pJ.getMission().setType(MissionType.MISSION_COMMON);
					return true;
				}
			};
	
			/* 2da parte. Chequear countries por contienente */
			//FIXME the order of continents is problemetic
			int tt=0;
			for (Continent co : pJ.getGame().getBoard().getConts()) {
				List<Country> list = countries.get( co);
				if( list.size() <pJ.getMission().getMinCountriesPerContToOwn()[tt++] )
					return false;
			}
			
	
			/* TODO: 3ra parte. Chequear si vencio a los otros playeres */
	
			/* TODO: 4ta parte. Chequear si tiene los countries limitrofes que se piden */
			i=pJ.getMission().getMinNeigboringCountries();
			if(  i>0) {
				if(!haveMinNegihbours(pJ, i)){
					return false;
				}
			}
	
			/* tiene todo, entonces gano! */
			return true;
	
	
		}



	public static int indexOf(Mission mission) {
		for (int i = 0; i < g_missions.length; i++) {
			if(mission==g_missions[i])
				return i;

		}
		return -1;
	}


}
