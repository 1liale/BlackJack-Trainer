/**
 * A shoe of cards Author: Yifan Zong Created on: 23/12/2020
 */

public class Shoe {
	private int decks, deckPt, runningCount;
	private Card[] deck;

	// Create a show of n decks
	public Shoe(int decks) {
		this.decks = decks;
		deck = new Card[this.decks * 52];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i);
		}

		shuffle();
	}

	// Shuffle the shoe
	public Shoe shuffle() {
		for (int i = deck.length - 1; i > 1; i--) {
			int rndPos = (int) (Math.random() * deck.length);
			Card temp = deck[i];
			deck[i] = deck[rndPos];
			deck[rndPos] = temp;
		}

		deckPt = 0;
		runningCount = 0;
		return this;
	}

	// Return the number of cards remaining in the shoe
	public int length() {
		return deck.length - deckPt;
	}

	// Return the number of decksRemaining in the shoe
	public double decksRemaining() {
		return (double) length() / 52.0;
	}

	// Return the number of decks in the shoe
	public int decks() {
		return decks;
	}

	// Draw a new card from the shoe
	public Card draw() {
		Card card = deck[deckPt++];
		runningCount += card.highLow();
		return card;
	}
	
	// Remove a card from the shoe without updating the count
	public Card remove() {
		return deck[deckPt++];
	}
	
	// Add a card to the running count
	public int updateCount(Card card) {
		return runningCount += card.highLow();
	}
	
	// Return the running count
	public int runningCount() {
		return runningCount;
	}
	
	// Return the true count
	public double trueCount() {
		return runningCount/decksRemaining();
	}
}
