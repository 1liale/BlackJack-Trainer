
public class Shoe {
	private int size, deckPt, count;
	private Card[] deck;
	
	public Shoe(int size) {
		deckPt = 0;
		this.size = size;
		deck = new Card[this.size * 52];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i);
		}
		
		shuffle();
	}
	
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
	
	public int count() {
		return count;
	}
	
	public int length() {
		return deck.length - deckPt;
	}
	
	public Card draw() {
		Card card = deck[deckPt++];
		count += card.highLow();
		return card;
	}
}
