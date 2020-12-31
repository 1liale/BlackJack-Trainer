package src;
/**
 * A src.Player of src.Blackjack
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */
import java.util.ArrayList;

abstract class Player {
	private ArrayList<PlayerHand> hands;
	private double bankroll, profit;
	private boolean ruined;

	// Set profit, bankroll to zero, ruined to false, and create a new empty
	// ArrayList of hands
	public Player(double bankroll) {
		this.bankroll = bankroll;
		profit = 0;
		hands = new ArrayList<PlayerHand>(1);
		hands.add(new PlayerHand());
		ruined = false;
	}

	// Clear and leave one empty hand
	public Player clearHands() {
		PlayerHand temp = hands.get(0);
		temp.clear();
		hands.clear();
		hands.add(temp);
		return this;
	}

	// Put 2 cards into the empty hand
	public ArrayList<PlayerHand> newHand() {
		hands.get(0).add(Blackjack.shoe().draw()).add(Blackjack.shoe().draw());
		return hands;
	}

	// Update the bankroll and profit given an amount and reset the hand
	// Must be called after every round to update ruined
	public Player update(double amount) {
		bankroll += amount;
		profit += amount;
		updateRuined();
		return this;
	}

	// Set ruined to true
	protected boolean setRuined() {
		ruined = true;
		return ruined;
	}

	// Return ruined (whether a player has no bankroll left)
	public boolean ruined() {
		return ruined;
	}

	// Return profit
	public double profit() {
		return profit;
	}

	// Return bankroll
	public double bankroll() {
		return bankroll;
	}

	// Return hands
	public ArrayList<PlayerHand> hands() {
		return hands;
	}

	// True if all hands have busted
	public boolean busted() {
		boolean temp = true;
		for (int i = 0; i < hands.size(); i++) {
			temp = temp && hands.get(i).busted();
		}
		return temp;
	}

	@Override
	// Return hands as a string
	public String toString() {
		return hands.toString();
	}

	// Place a bet for a hand
	protected class InvalidBetException extends Exception {
	};

	protected class ExceedsBankrollException extends Exception {
	};

	protected PlayerHand placeBet(PlayerHand hand, double bet) throws InvalidBetException, ExceedsBankrollException {
		// Check if bet is valid and possible
		if (bet <= 0.0) {
			throw new InvalidBetException();
		}
		if (bankroll < bet) {
			throw new ExceedsBankrollException();
		}

		// Update bankroll and profit and place bet
		bankroll -= bet;
		profit -= bet;
		hand.placeBet(bet);
		return hand;
	}

	// Place insurance for a hand
	protected PlayerHand placeInsurance(PlayerHand hand) throws ExceedsBankrollException {
		double amount = hand.bet() / 2.0;
		if (bankroll < amount) {
			throw new ExceedsBankrollException();
		}
		bankroll -= amount;
		profit -= amount;
		hand.placeInsurance();
		return hand;
	}

	// Double down a hand
	protected class ExceedsBetException extends Exception {
	};

	protected class InvalidDoubleDownException extends Exception {
	};

	protected PlayerHand doubleDown(PlayerHand hand, double bet)
			throws InvalidBetException, ExceedsBankrollException, ExceedsBetException, InvalidDoubleDownException {
		// Check if double down is valid and possible
		if (hand.size() != 2) {
			throw new InvalidDoubleDownException();
		}
		if (bet <= 0.0) {
			throw new InvalidBetException();
		}
		if (bet > hand.bet()) {
			throw new ExceedsBetException();
		}
		if (bankroll < bet) {
			throw new ExceedsBankrollException();
		}

		// Update bankroll and profit and double down
		bankroll -= bet;
		profit -= bet;
		hand.placeBet((int) hand.bet() + bet);
		System.out.print(hand.add(Blackjack.shoe().draw()));
		if (hand.busted()) {
			System.out.println(" Bust");
		} else {
			System.out.println();
		}
		hand.done(); // src.Hand is done after doubling down
		return hand;
	}

	// Split a src.Hand
	protected class InvalidSplitException extends Exception {
	};

	protected ArrayList<PlayerHand> split(PlayerHand hand) throws InvalidSplitException, ExceedsBankrollException {
		// Check if split is valid and possible
		if (hand.size() != 2 || hand.get(0).rank() != hand.get(1).rank()) {
			throw new InvalidSplitException();
		}
		double bet = hand.bet();
		if (bankroll < bet) {
			throw new ExceedsBankrollException();
		}

		// Update bankroll and profit and double down
		bankroll -= bet;
		profit -= bet;
		Card card1 = hand.get(0), card2 = hand.get(1);
		System.out.println(hand.clear().add(card1).add(Blackjack.shoe().draw()));
		hand.placeBet(bet);
		hands.add(new PlayerHand(card2, Blackjack.shoe().draw()).placeBet(bet));
		return hands;
	}

	// Hit, draw a new card
	protected PlayerHand hit(PlayerHand hand) {
		System.out.print(hand.add(Blackjack.shoe().draw()));
		if (hand.busted()) {
			System.out.println(" Bust");
			hand.done();
		} else {
			System.out.println();
		}
		return hand;
	}

	// Stand, hand is done
	protected PlayerHand stand(PlayerHand hand) {
		return hand.done();
	}

	public abstract ArrayList<PlayerHand> placeBets();

	public abstract ArrayList<PlayerHand> placeInsurances();

	public abstract ArrayList<PlayerHand> play(Card dealerCard);

	protected abstract boolean updateRuined();
}
