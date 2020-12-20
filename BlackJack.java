import java.util.ArrayList;

public class BlackJack {
	static int sets = 1;
	static double rate = 2.5;
	static Shoe shoe = new Shoe(sets);

	public static void main(String[] args) {
		Dealer dealer = new Dealer();
		Player player = new HumanPlayer(100.0);
		
		while (shoe.length() > 6) {
			ArrayList<PlayerHand> playerHands = player.newHand().play();
			DealerHand dealerHand = dealer.newHand().play();
			System.out.println(dealerHand);
			
			for (int i = 0; i < playerHands.size(); i++) {
				PlayerHand playerHand = playerHands.get(i);
				System.out.println(playerHand);
				int comparison = dealerHand.compareTo(playerHands.get(i));
				switch (comparison) {
				case 2:
					System.out.println("BlackJack! " + player.win(rate * playerHand.bet()).profit());
					break;
				case 1:
					System.out.println("Player Wins " + player.win(2 * playerHand.bet()).profit());
					break;
				case 0:
					System.out.println("Push " + player.profit());
					break;
				case -1:
					System.out.println("Player Loses "  + player.profit());
					break;
				case -2:
					System.out.println("Player Busts "  + player.profit());
					break;
				}
				
			}
		}
	}
}
