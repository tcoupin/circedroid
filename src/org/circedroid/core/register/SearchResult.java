package org.circedroid.core.register;

public class SearchResult<T> {

	private float score;
	private T item; 
	public SearchResult(float score, T item){
		this.score = score;
		this.item = item;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public T getItem() {
		return item;
	}
	public void setItem(T item) {
		this.item = item;
	}
	
	public String toString(){
		return "SearchResult score:"+score+" "+item;
	}
	
}
