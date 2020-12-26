/**
 * Console Based Blackjack Game Trainer
 * Require UTF8 encoding to view properly (see Card for more info)
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BlackJack {
	private static int sets, reserve;
	private static double rate;
	private static Dealer dealer = new Dealer();
	private static Player[] players;
	private static Shoe shoe;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	//Setup the game parameters: number of decks in the shoe, deck penetration, number of players, and payout rate
	public static void setUp() {
		//Deck size
		String input;
		System.out.println("How many sets of decks to play with?");
		while (true) {
			try {
				input = reader.readLine();
				sets = Integer.parseInt(input);
				if (sets <= 0) {
					System.out.println("Please enter a positive integer.");
				} else {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("Please enter a positive integer.");
			}
		}
		shoe = new Shoe(sets);

		//Payout rate
		System.out.println("What is the payout rate?");
		System.out.println("1. 3:2\n2. 6:5");
		while (rate == 0.0) {
			try {
				input = reader.readLine().trim();
				switch (input) {
				case "1":
					rate = 2.5;
					break;
				case "2":
					rate = 2.2;
					break;
				default:
					System.out.println("Please enter \"1\" for 3:2 and \"2\" for 6:5.");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Number of players
		System.out.println("How many players?");
		while (true) {
			try {
				input = reader.readLine();
				int num = Integer.parseInt(input);
				if (num <= 0 || num > 8) {
					System.out.println("Please enter a positive integer between 1 and 8.");
				} else {
					players = new Player[num];
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("Please enter a positive integer between 1 and 8.");
			}
		}

		//Deck penetration
		int minReserve = 5 * (1 + players.length);
		System.out.println("How deep you want to go down the deck?");
		System.out.println("Enter as a decimal. e.g. 0.65 for 65%.");
		while (true) {
			try {
				input = reader.readLine();
				double percentage = Double.parseDouble(input);
				reserve = (int) (52 * sets * (1 - percentage));
				if (percentage <= 0 || percentage >= 1) {
					System.out.println("Please enter a number greater than 0 and less than 1.");
				} else if (reserve < minReserve) {
					System.out.println("You may run out of cards mid-round. Please choose a lower deck penetration");
				} else {break;}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number greater than 0 and less than 1.");
			}
		}
	}

	//Set the type and bankroll of players
	public static void choosePlayer() {
		//Player type
		for (int i = 0; i < players.length; i++) {
			int type = 0;
			System.out.println("What kind of player is player" + i + "?");
			System.out.println("1. Human");
			System.out.println("2. Basic Strat CPU");
			while (true) {
				try {
					type = Integer.parseInt(reader.readLine());
					if (type <= 0 || type > 2) {
						System.out.println("Please enter a valid integer.");
					} else {
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Please enter a valid integer.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//Player bankroll
			System.out.println("How many betting units does this player have in their bankroll?");
			while (true) {
				try {
					int bankroll = Integer.parseInt(reader.readLine());
					if (bankroll <= 0) {
						System.out.println("Please enter a valid integer.");
					} else {
						switch (type) {
						case 1:
							players[i] = new HumanPlayer(bankroll);
							break;
						case 2:
							players[i] = new BasicStratPlayer(bankroll);
						}
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					System.out.println("Please enter a valid integer.");
				}
			}
		}
	}

	//Return the shoe
	public static Shoe shoe() {
		return shoe;
	}
	
	//Setup game and players and play blackjack until all players lose all of their bankroll
	public static void main(String[] args) {
		//Setup game and players
		setUp();
		choosePlayer();
		System.out.println();
		
		//Play game
		while (true) {
			while (shoe.length() > reserve) {
				DealerHand dealerHand = dealer.newHand(); //Dealer draw card
				
				//Players place bets
				int counter = 0; //counter keeps track of the number of players who lost all their bankroll (ruined)
				for (int i = 0; i < players.length; i++) {
					if (!players[i].ruined()) {
						System.out.println("Player" + i + ":");
						players[i].newHand();
						players[i].placeBets();
						System.out.println();
					} else {
						counter++;
					}
				}
				
				//If all players lost their bankroll, end the game
				if (counter == players.length) {
					System.out.println("All players are ruined.");
					return;
				}
				
				//Players play their hands
				for (int i = 0; i < players.length; i++) {
					if (!players[i].ruined()) {
						System.out.println("Dealer: " + dealerHand);
						System.out.print("Player" + i + ": ");
						players[i].play(dealerHand); //Dealer's one-card hand is passed as parameter
						System.out.println();
					}
				}

				//Dealer plays its hand and players win or lose
				dealerHand = dealer.play();
				System.out.println("Dealer: " + dealerHand);
				for (int i = 0; i < players.length; i++) {
					Player player = players[i];
					if (!player.ruined()) {
						System.out.println("Player" + i + ": ");
						ArrayList<PlayerHand> playerHands = player.hands();
						
						//For each player's hand, compare with the dealer's to determine the winner
						for (int j = 0; j < playerHands.size(); j++) {
							System.out.println(playerHands.get(j));
							int comparison = dealerHand.compareTo(playerHands.get(j));
							switch (comparison) {
							case 2:
								System.out.println("BlackJack!");
								player.update(rate * playerHands.get(j).bet()); 
								break;
							case 1:
								System.out.println("Wins");
								player.update(2.0 * playerHands.get(j).bet());
								break;
							case 0:
								System.out.println("Push");
								player.update(playerHands.get(j).bet());
								break;
							case -1:
								System.out.println("Loses");
								player.update(0.0); //Require to update whether a player is ruined
								break;
							case -2:
								System.out.println("Busts");
								player.update(0.0);
								break;
							}
						}
						
						System.out.println();
					}
				}
			}
			
			//Reshuffle if end of a shoe is reached
			shoe.shuffle();
			System.out.println("Reshuffling");
		}
	}
}
