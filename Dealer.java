
public class Dealer extends BlackJack {
	private DealerHand hand;

	public Dealer() {
		hand = new DealerHand();
	}

	public Dealer newHand() {
		hand.clear();
		hand.add(shoe.draw()).add(shoe.draw());
		return this;
	}

	public DealerHand play() {
		while (hand.val < 16) {
			hand.add(shoe.draw());
		}

		return hand;
	}

	public String toSting() {
		return hand.toString();
	}
}
