
public class PlayerHand extends Hand implements Comparable<DealerHand> {
	private double bet;
	private boolean done;

	public PlayerHand(Card...cards) {
		super(cards);
		bet = 0;
		done = false;
	}

	public double bet() {
		return bet;
	}

	public PlayerHand placeBet(double bet) {
		this.bet += bet;
		return this;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public PlayerHand done() {
		done = true;
		return this;
	}
	
	@Override
	void updateVal(Card card) {
		super.updateVal(card);
		if (val() > 21) {done = true;}
	}
	
	@Override
	public PlayerHand clear() {
		super.clear();
		bet = 0;
		done = false;
		return this;
	}

	@Override
	// -2: Player Busts
	// -1: Player Loses
	// 0: Push
	// 1: Player Wins
	// 2: Player Wins by BlackJack
	public int compareTo(DealerHand o) {
		int val1 = val(), val2 = o.val();
		if (val1 > 21) {
			return -2;
		}
		if (val2 > 21 || val1 > val2) {
			if (val1 == 21 && size() == 2) {
				return 2;
			}
			return 1;
		}
		if (val1 == val2) {
			return 0;
		}
		return -1;
	}
}
