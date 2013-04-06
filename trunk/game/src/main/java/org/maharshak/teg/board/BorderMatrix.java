package org.maharshak.teg.board;

import java.util.ArrayList;
import java.util.List;


public class BorderMatrix {

	
	private boolean [][] _hasBorders;
	private List<Country> _countries;

	public BorderMatrix(List<Country> countries) {
		_countries=new ArrayList<Country>(countries);
		_hasBorders=new boolean[_countries.size()][_countries.size()];
	}

	
	public boolean hasBorder(Country a,Country b){

    return _hasBorders[indexOf(a)][indexOf(b)];
	}


	private int indexOf(Country a) {
		assert a!=null : "bad input "+a;
		return _countries.indexOf(a);
	}

	public void setBorder(Country a, Country b){
		_hasBorders[indexOf(b)][indexOf(a)]=true;
		_hasBorders[indexOf(a)][indexOf(b)]=true;
	}
	
	public String print(){
		StringBuilder sb=new StringBuilder();
		
		sb.append("{\n");
		for (Country i : _countries) {
			sb.append("\t{");
			for (Country j : _countries) {
				sb.append(hasBorder(i, j)?1:0).append(",");
			}
			sb.append("}").append(i.getName()).append("\n");
		}
		sb.append("}\n");
		return sb.toString();
	}
}
