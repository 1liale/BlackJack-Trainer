import java.util.ArrayList;

public class Player extends BlackJack {
	private ArrayList<PlayerHand> hands;
	double profit;

	public Player() {
		profit = 0;
		hands = new ArrayList<PlayerHand>(1);
		hands.add(new PlayerHand());
	}

	public Player newHand() {
		for (int i = 0; i < hands.size(); i++) {
			hands.get(i).clear();
			hands.get(i).add(shoe.draw()).add(shoe.draw());
		}

		return this;
	}

	public ArrayList<PlayerHand> play() {
		for (int i = 0; i < hands.size(); i++) {
			hands.get(i).placeBet(1);
		}

		return hands;
	}

	public Player update(int comparison, int bet) {
		switch (comparison) {
		case 2:
			profit += rate * bet;
			break;
		case 1:
			profit += bet;
			break;
		case 0:
			break;
		default:
			profit -= bet;
			break;
		}

		return this;
	}

	@Override
	public String toString() {
		return Double.toString(profit);
	}

}
