import java.util.ArrayList;

abstract class Player extends BlackJack {
	protected ArrayList<PlayerHand> hands;
	protected  double bankroll, profit;
	protected boolean ruined;

	public Player(double bankroll, Card...cards){
		this.bankroll = bankroll;
		profit = 0;
		hands = new ArrayList<PlayerHand>(1);
		hands.add(new PlayerHand(cards));
		ruined = false;
	}

	public ArrayList<PlayerHand> newHand() {
		PlayerHand temp = hands.get(0);
		temp.clear().add(shoe.draw()).add(shoe.draw());
		hands.clear();
		hands.add(temp);

		return hands;
	}
	
	public abstract ArrayList<PlayerHand> play(DealerHand dealerHand);
	public abstract ArrayList<PlayerHand> placeBets();
	
	public Player update(double amount) {
		bankroll += amount;
		profit += amount;
		if (bankroll <= 0) {ruined = true;}
		return this;
	}
	
	public boolean ruined() {
		return ruined;
	}
	
	public double profit() {
		return profit;
	}
	
	public double bankroll() {
		return bankroll;
	}
	
	@Override
	public String toString() {
		return hands.toString();
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
		if (bankroll == 0 || bankroll < bet) {throw new BetExceedsBankrollException();}
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(hand.bet());
		System.out.println(hand.add(shoe.draw()));
		hand.done();
		return hand;
	}
	
	class InvalidSplitException extends Exception{};
	ArrayList<PlayerHand> split(PlayerHand hand) throws InvalidSplitException, BetExceedsBankrollException{
		if (hand.size() != 2 || hand.get(0).rank() != hand.get(1).rank()) {throw new InvalidSplitException();}
		double bet = hand.bet();
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		bankroll -= bet;
		profit -= bet;
		Card card1 = hand.get(0), card2 = hand.get(1);
		hand.clear().add(card1);
		hand.add(shoe.draw());
		hand.placeBet(bet);
		
		hands.add(new PlayerHand(card2, shoe.draw()).placeBet(bet));
		
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
