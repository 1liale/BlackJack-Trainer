import java.util.ArrayList;

abstract class Player extends BlackJack {
	ArrayList<PlayerHand> hands;
	double bankroll, profit;

	public Player(double bankroll, Card...cards) {
		this.bankroll = bankroll;
		profit = 0;
		hands = new ArrayList<PlayerHand>(1);
		hands.add(new PlayerHand(cards));
	}

	public Player newHand() {
		for (int i = 0; i < hands.size(); i++) {
			hands.get(i).clear();
			hands.get(i).add(shoe.draw()).add(shoe.draw());
		}

		return this;
	}

	public abstract ArrayList<PlayerHand> play();
	
	public Player win(double winnings) {
		bankroll += winnings;
		profit += winnings;
		return this;
	}

	public double profit() {
		return profit;
	}
	
	public double bankroll() {
		return bankroll;
	}
	
	@Override
	public String toString() {
		return "[" + profit + " " + bankroll + " " + hands + "]";
	}

}
