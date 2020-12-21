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

	private ArrayList<PlayerHand> placeBets() {
		System.out.println("Bankroll: " + bankroll + "  Profit: " + profit);
		for (int i = 0; i < hands.size(); i++) {
			double bet = 0.0;
			System.out.println("How much to bet?");
				try {
					input = reader.readLine();
					bet = Double.parseDouble(input);
					placeBet(hands.get(i), bet);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NumberFormatException|InvalidBetException e) {
					System.out.println("Invalid bet, please enter again.");
					i--;
				} catch (BetExceedsBankrollException e) {
					System.out.println("Bet exceeds bankroll. Please place a lower bet.");
					i--;
				}
		}

		return hands;
	}
	
	private PlayerHand doubleDown(PlayerHand hand) {
		System.out.println("Bet: " + hand.bet());
		System.out.println("How much to double down?");
		double bet = 0.0;
		for (int i = 0; i < 1; i++) {
			try {
				input = reader.readLine();
				bet = Double.parseDouble(input);
				doubleDown(hand, bet);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException|InvalidBetException e) {
				System.out.println("Invalid bet, please enter again.");
				i--;
			} catch (BetExceedsBankrollException e) {
				System.out.println("Bet exceeds bankroll. Please place a lower bet.");
				i--;
			} catch (BetExceedsBetException e) {
				System.out.println("Bet exceeds previous bet. Please place a lower bet.");
				i--;
			}
		}
		
		return hand;
	}
	
	@Override
	public ArrayList<PlayerHand> play() {
		hands = placeBets();
		for (int i = 0; i < hands.size(); i++) {
			System.out.println(hands.get(i));
			while (!hands.get(i).isDone()) {
				System.out.println("What would you like to do? Press F for help.");
				try {
					input = reader.readLine().trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				switch (input.toUpperCase()) {
				case "D": doubleDown(hands.get(i));
				break;
				case "H": hit(hands.get(i));
				break;
				case "S": stand(hands.get(i));
				break;
				case "SP": try {
						split(hands.get(i));
						System.out.println(hands.get(i));
					} catch (InvalidSplitException e) {
						System.out.println("You are not allowed to split this hand.");
					} catch (BetExceedsBankrollException e) {
						System.out.println("You don't have enough bankroll to split this hand.");
					}
				break;
				case "F": System.out.println("H to hit. S to stand. D to double down. SP to split.");
				break;
				default: System.out.println("Invalid input, please enter again.");
				}
			}
		}
		return hands;
	}
}
