import java.util.ArrayList;

abstract class Hand {
	private ArrayList<Card> cards;
	private int aces;
	int val;

	public Hand(Card...cards) {
		val = 0;
		aces = 0;
		this.cards = new ArrayList<Card>(Math.max(3, cards.length));
		for (int i = 0; i < cards.length; i++) {
			add(cards[i]);
		}
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
