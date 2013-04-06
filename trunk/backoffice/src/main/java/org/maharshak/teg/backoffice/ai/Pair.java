package org.maharshak.teg.backoffice.ai;

public class Pair<T,M> {
	
	private T _first;
	private M _second;
	/**
	 * @param first
	 * @param second
	 */
	public Pair(T first, M second) {
		_first = first;
		_second = second;
	}
	
	public T getFirst() {
		return _first;
	}
	public M getSecond() {
		return _second;
	}
	

}
