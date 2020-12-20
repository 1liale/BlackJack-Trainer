import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;
	private int aces;
	int val;

	public Hand() {
		val = 0;
		aces = 0;
		this.cards = new ArrayList<Card>(3);
	}

	public Hand clear() {
		val = 0;
		aces = 0;
		cards.clear();
		return this;
	}

	private void updateVal(Card card) {
		int temp = card.val();
		val += temp;
		if (temp == 11) {
			aces++;
		}

		while ((aces > 0) && (val > 21)) {
			val -= 10;
			aces--;
		}
	}

	public Hand add(Card card) {
		cards.add(card);
		updateVal(card);
		return this;
	}

	public ArrayList<Card> cards() {
		return cards;
	}

	public int size() {
		return cards.size();
	}

	@Override
	public String toString() {
		return cards.toString();
	}
}
