package org.maharshak.teg.player;

public enum ChipsColor {

	RED("red"),
	YELLOW("yellow"),
	BLUE("blue"),
	BLACK("black"),
	PINK("pink"),
	GREEN("green"),
	NA("n/a");
	
	private String _name;

	private ChipsColor(String name){
		_name=name;
	}
	
	public String getName() {
		return _name;
	}

	public static ChipsColor getColour(int requestedColour) {
		return values()[requestedColour];
	}
	
	public static int getId(ChipsColor requestedColour) {
		assert requestedColour!=null:"cant return index of a null colour";
		for (int i = 0; i < values().length; i++) {
			if(values()[i]==requestedColour)
				return i;
		}
		throw new RuntimeException("No such color");
	}
	
}
