package org.maharshak.teg.backoffice.ai;

public class Misc {

	private static String names[] = {
		"Coca Sarli",
		"Ciccolina",
		"Raton Perez",
		"Geniol",
		"Fantoche",
		"Jirafa",
		"Guaymayen",
		"Mono Mario",
		"Havanna",
		"Shakira",
		"Thalia",
		"Spice Girls",
		"Britney Spears",
		"Cameron Diaz",
		"Betty la fea",
		"Pitufina",
		"Topacio",
		"Batman",
		"Gatubela",
		"La cuca",
		"Bush",
		"Lewinsky",
		"Clinton",
		"Chupete",
		"Gaturro",
		"Mr.President",
		"Batistuta",
		"Perla",
		"Maradona",
		"Pele",
		"Comanche",
		"Tehuelche",
		"Pampita",
		"Pampa",
		"Pamela",
	};

	public static String getRandomName() {
		double ind = Math.random()*names.length;
		
		int round = (int) ind;
		return names[round];
	}
}
