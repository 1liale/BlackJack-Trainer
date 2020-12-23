
public class Dealer extends BlackJack {
	private DealerHand hand;

	public Dealer(Card...cards) {
		hand = new DealerHand(cards);
	}

	public DealerHand newHand() {
		hand.clear();
		hand.add(shoe.draw());
		return hand;
	}

	public DealerHand play() {
		while (hand.val() < 16) {
			hand.add(shoe.draw());
		}
		
		return hand;
	}

	public String toString() {
		return hand.toString();
	}
}
