/**
 * A Hand of Cards
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */
import java.util.ArrayList;

abstract class Hand {
	private ArrayList<Card> cards;
	private int aces, val;

	//Initialize the value, number of aces, and an ArrayList of Cards
	public Hand(Card...cards) {
		val = 0;
		aces = 0;
		this.cards = new ArrayList<Card>(Math.max(3, cards.length));
		for (int i = 0; i < cards.length; i++) {
			add(cards[i]);
		}
	}

	//Reset all the value, number of aces, and clear the ArrayList of Cards
	public Hand clear() {
		val = 0;
		aces = 0;
		cards.clear();
		return this;
	}

	//Update the value and number of aces given a new Card
	protected void updateVal(Card card) {
		int temp = card.val();
		val += temp;
		
		//Update aces
		if (temp == 11) {
			aces++;
		}

		//If value exceeds 21 and there are aces, make the ace represent 1 instead of 11
		while ((aces > 0) && (val > 21)) {
			val -= 10;
			aces--;
		}
	}
	
	//Add a new card to hand
	public Hand add(Card card) {
		cards.add(card);
		updateVal(card);
		return this;
	}

	//Return the ArrayList of Cards
	public ArrayList<Card> cards() {
		return cards;
	}

	//Return the number of cards
	public int size() {
		return cards.size();
	}
	
	//Return the Hand's value
	public int val() {
		return val;
	}
	
	public Card get(int i) {
		return cards.get(i);
	}
	
	@Override
	//Return ArrayList of Cards as a String
	public String toString() {
		return cards.toString();
	}
}
