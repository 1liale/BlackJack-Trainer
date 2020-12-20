import java.util.ArrayList;

public class BlackJack {
	static int sets = 1;
	static double rate = 1.5;
	static Shoe shoe = new Shoe(sets);

	public static void main(String[] args) {
		Dealer dealer = new Dealer();
		Player player = new Player();
		
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
					System.out.println("BlackJack! " + player.update(comparison, playerHand.bet()));
					break;
				case 1:
					System.out.println("Player Wins " + player.update(comparison, playerHand.bet()));
					break;
				case 0:
					System.out.println("Push " + player.update(comparison, playerHand.bet()));
					break;
				case -1:
					System.out.println("Player Loses " + player.update(comparison, playerHand.bet()));
					break;
				case -2:
					System.out.println("Player Busts " + player.update(comparison, playerHand.bet()));
					break;
				}
			}
		}
	}
}
