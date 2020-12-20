
public class PlayerHand extends Hand implements Comparable<DealerHand> {
	private int bet;

	public PlayerHand() {
		super();
		bet = 0;
	}

	public int bet() {
		return bet;
	}

	public int placeBet(int bet) {
		this.bet += bet;
		return this.bet;
	}

	@Override
	public PlayerHand clear() {
		super.clear();
		bet = 0;
		return this;
	}

	@Override
	// -2: Player Busts
	// -1: Player Loses
	// 0: Push
	// 1: Player Wins
	// 2: Player Wins by BlackJack
	public int compareTo(DealerHand o) {
		int val1 = val, val2 = o.val;
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
