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
		PlayerHand temp = hands.get(0);
		temp.clear().add(shoe.draw()).add(shoe.draw());
		hands.clear();
		hands.add(temp);

		return this;
	}
	
	public abstract ArrayList<PlayerHand> play();
	public abstract ArrayList<PlayerHand> placeBets();
	
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
	
	class InvalidBetException extends Exception {};
	class BetExceedsBankrollException extends Exception {};
	PlayerHand placeBet(PlayerHand hand, double bet) throws InvalidBetException, BetExceedsBankrollException{
		if (bet <= 0.0) {throw new InvalidBetException();}
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(bet);
		return hand;
	}
	
	class BetExceedsBetException extends Exception{};
	PlayerHand doubleDown(PlayerHand hand, double bet) throws InvalidBetException, BetExceedsBankrollException, BetExceedsBetException {
		if (bet <= 0.0) {throw new InvalidBetException();}
		if (bet > hand.bet()) {throw new BetExceedsBetException();}
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(hand.bet());
		System.out.println(hand.add(shoe.draw()));
		hand.done();
		return hand;
	}
	
	class InvalidSplitException extends Exception{};
	ArrayList<PlayerHand> split(PlayerHand hand) throws InvalidSplitException, BetExceedsBankrollException{
		Card card = hand.get(0);
		if (hand.size() != 2 || card.val() != hand.get(1).val()) {throw new InvalidSplitException();}
		double bet = hand.bet();
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		bankroll -= bet;
		profit -= bet;
		hand.clear().add(card);
		hand.placeBet(bet);
		
		hands.add(new PlayerHand(card).placeBet(bet));
		
		return hands;
	}
	
	PlayerHand hit(PlayerHand hand) {
		System.out.println(hand.add(shoe.draw()));
		return hand;
	}
	
	PlayerHand stand(PlayerHand hand) {
		return hand.done();
	}
	
	
}
