/**
 * A shoe of cards
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

public class Shoe {
	private int size, deckPt, runningCount;
	private Card[] deck;
	
	//Create a show of n decks
	public Shoe(int size) {
		deckPt = 0;
		this.size = size;
		deck = new Card[this.size * 52];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i);
		}
		
		shuffle();
	}
	
	//Reshuffle the shoe
	public Shoe shuffle() {
		for (int i = deck.length - 1; i > 1; i--) {
			int rndPos = (int)(Math.random() * deck.length);
			Card temp = deck[i];
			deck[i] = deck[rndPos];
			deck[rndPos] = temp;
		}
		
		deckPt = 0;
		return this;
	}
	
	//Return the running high-low count
	public int runningCount() {
		return runningCount;
	}
	
	//Return the true high-low count
	public int trueCount() {
		return runningCount(); 
	}
	
	public int decksRemaining() {
		return length() / 52;
	}
	
	public int length() {
		return deck.length - deckPt;
	}
	
	public Card draw() {
		Card card = deck[deckPt++];
		runningCount += card.highLow();
		return card;
	}
}
