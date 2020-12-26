/**
 * A Player of Blackjack
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */
import java.util.ArrayList;

abstract class Player extends BlackJack {
	private ArrayList<PlayerHand> hands;
	private  double bankroll, profit;
	private boolean ruined;

	//Set profit, bankroll to zero, ruined to false, and create a new empty ArrayList of hands
	public Player(double bankroll, Card...cards){
		this.bankroll = bankroll;
		profit = 0;
		hands = new ArrayList<PlayerHand>(1);
		hands.add(new PlayerHand(cards));
		ruined = false;
	}

	//Draw two cards for a new hand
	public ArrayList<PlayerHand> newHand() {
		PlayerHand temp = hands.get(0);
		temp.clear().add(shoe().draw()).add(shoe().draw());
		hands.clear();
		hands.add(temp);
		return hands;
	}
	
	//Update the bankroll and profit given an amount.
	//Must be called after every round to update ruined
	public Player update(double amount) {
		bankroll += amount;
		profit += amount;
		if (bankroll <= 0) {updateRuined();}
		return this;
	}
	
	//Set ruined to true
	public boolean setRuined() {
		ruined = true;
		return ruined;
	}
	
	//Return ruined (whether a player has no bankroll left)
	public boolean ruined() {
		return ruined;
	}	
		
	//Return profit
	public double profit() {
		return profit;
	}
	
	//Return bankroll
	public double bankroll() {
		return bankroll;
	}
	
	//Return hands
	public ArrayList<PlayerHand> hands(){
		return hands;
	}
	
	@Override
	//Return hands as a string
	public String toString() {
		return hands.toString();
	}
	
	//Place a bet for a hand
	protected class InvalidBetException extends Exception {};
	protected class BetExceedsBankrollException extends Exception {};
	protected PlayerHand placeBet(PlayerHand hand, int bet) throws InvalidBetException, BetExceedsBankrollException{
		//Check if bet is valid and possible
		if (bet <= 0) {throw new InvalidBetException();}
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		
		//Update bankroll and profit and place bet
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(bet);
		return hand;
	}
	
	//Double down a hand
	protected class BetExceedsBetException extends Exception{};
	protected PlayerHand doubleDown(PlayerHand hand, int bet) throws InvalidBetException, BetExceedsBankrollException, BetExceedsBetException{
		//Check if bet is valid and possible
		if (bet <= 0.0) {throw new InvalidBetException();}
		if (bet > hand.bet()) {throw new BetExceedsBetException();}
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		
		//Update bankroll and profit and double down
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(hand.bet());
		System.out.println(hand.add(shoe().draw()));
		hand.done(); //Hand is done after doubling down
		return hand;
	}
	
	//Split a Hand
	protected class InvalidSplitException extends Exception{};
	protected ArrayList<PlayerHand> split(PlayerHand hand) throws InvalidSplitException, BetExceedsBankrollException{
		//Check if split is valid and possible
		if (hand.size() != 2 || hand.get(0).rank() != hand.get(1).rank()) {throw new InvalidSplitException();}
		int bet = (int) hand.bet();
		if (bankroll < bet) {throw new BetExceedsBankrollException();}
		
		//Update bankroll and profit and double down
		bankroll -= bet;
		profit -= bet;
		Card card1 = hand.get(0), card2 = hand.get(1);
		hand.clear().add(card1).add(shoe().draw());
		hand.placeBet(bet);
		hands.add(new PlayerHand(card2, shoe().draw()).placeBet(bet));
		return hands;
	}
	
	//Hit, draw a new card
	protected PlayerHand hit(PlayerHand hand) {
		System.out.println(hand.add(shoe().draw()));
		return hand;
	}
	
	//Stand, hand is done
	protected PlayerHand stand(PlayerHand hand) {
		return hand.done();
	}
	
	public abstract ArrayList<PlayerHand> play(DealerHand dealerHand);
	public abstract ArrayList<PlayerHand> placeBets();
	protected abstract boolean updateRuined();
}
