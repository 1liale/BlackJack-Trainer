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
		for (int i = 0; i < hands.size(); i++) {
			System.out.println(hands.get(i));
			double bet = 0.0;
			System.out.println("How much to bet?");
			
			while (true) {
				try {
					input = reader.readLine();
					bet = Double.parseDouble(input);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {}
				
				if (bet <= 0.0) {
					System.out.println("Invalid bet, please enter again.");
				}
				else if (bankroll < bet) {
					System.out.println("Bet exceeds bankroll. Please enter a lower bet.");
				} else {
					break;
				}
			}
			
			bankroll -= bet;
			profit -= bet;
			hands.get(i).placeBet(bet);
		}

		return hands;
	}

	@Override
	public ArrayList<PlayerHand> play() {
		hands = placeBets();
		//To Do, implement player choice
		return hands;
	}
}
