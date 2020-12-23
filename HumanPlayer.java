
/**
 * A human player that asks users for decisions
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HumanPlayer extends Player {
	private BufferedReader reader;
	private String input;

	public HumanPlayer(double bankroll, Card... cards) {
		super(bankroll, cards);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	// Ask the user for a new bet and double down
	private PlayerHand doubleDown(PlayerHand hand) throws ZeroBankrollException {
		// New bet
		System.out.println("Previous bet: " + hand.bet());
		System.out.println("How much to double down?");
		double bet = 0.0;
		while (true) {
			try {
				input = reader.readLine();
				bet = Double.parseDouble(input);
				doubleDown(hand, bet); // Double down
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException | InvalidBetException e) {
				System.out.println("Invalid bet, please enter again.");
			} catch (BetExceedsBankrollException e) {
				System.out.println("Bet exceeds bankroll. Please place a lower bet.");
			} catch (BetExceedsBetException e) {
				System.out.println("Bet exceeds previous bet. Please place a lower bet.");
			}
		}

		return hand;
	}

	@Override
	// Ask users to place a bet for each of their hands
	public ArrayList<PlayerHand> placeBets() {
		System.out.println("Bankroll: " + bankroll() + "  Profit: " + profit());
		for (int i = 0; i < hands().size(); i++) {
			double bet = 0.0;
			System.out.println("How much to bet?");
			try {
				input = reader.readLine();
				bet = Double.parseDouble(input);
				placeBet(hands().get(i), bet); // Place the bet for a hand
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException | InvalidBetException e) {
				System.out.println("Invalid bet, please enter again.");
				i--;
			} catch (BetExceedsBankrollException e) {
				System.out.println("Bet exceeds bankroll. Please place a lower bet.");
				i--;
			}
		}

		return hands();
	}

	@Override
	// Ask the user to play each of their hand
	public ArrayList<PlayerHand> play(DealerHand hand) {
		for (int i = 0; i < hands().size(); i++) {
			System.out.println(hands().get(i));
			// Loop until a Hand is done being played
			while (!hands().get(i).isDone()) {
				// Ask for player input
				System.out.println("What would you like to do? Press F for help.");
				try {
					input = reader.readLine().trim();
				} catch (IOException e) {
					e.printStackTrace();
				}

				switch (input.toUpperCase()) {
				case "D":
					//Double down
					try {
						doubleDown(hands().get(i));
					} catch (ZeroBankrollException e) {
						System.out.println("You don't have enough bankroll to double down.");
					}
					break;
				case "H":
					hit(hands().get(i)); // Hit
					break;
				case "S":
					stand(hands().get(i)); // Stand
					break;
				case "SP":
					// Split
					try {
						split(hands().get(i));
						System.out.println("You: " + hands().get(i));
					} catch (InvalidSplitException e) {
						System.out.println("You are not allowed to split this hand.");
					} catch (BetExceedsBankrollException e) {
						System.out.println("You don't have enough bankroll to split this hand.");
					}
					break;
				case "F":
					System.out.println("H to hit. S to stand. D to double down. SP to split.");
					break;
				default:
					System.out.println("Invalid input, please enter again.");
				}
			}
		}
		
		return hands();
	}
}
