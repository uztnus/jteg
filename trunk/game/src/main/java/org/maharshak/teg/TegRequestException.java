package org.maharshak.teg;

public class TegRequestException extends Exception{

	public TegRequestException(String string) {
		super(string);
		System.err.println("Having error "+string);
	}

  public TegRequestException(String string, Throwable t) {
    super(string, t);
    System.err.println("Having error " + string + " due to " + t);
  }
	

}
