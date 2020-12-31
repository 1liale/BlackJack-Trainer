/**
 * Console Based Blackjack Game Trainer
 * Standard Blackjack Rules: H17, DAS, RAS, 3:2 payout
 * Require UTF8 encoding to view properly (see Card for more info)
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Blackjack {
	private static int sets, cutLength;
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
		int minCutLength = 5 * (players.length) + 9;
		System.out.println("How deep you want to go down the deck?");
		System.out.println("Enter as a decimal. e.g. 0.65 for 65%.");
		while (true) {
			try {
				input = reader.readLine();
				double percentage = Double.parseDouble(input);
				cutLength = (int) (52 * sets * (1 - percentage));
				if (percentage <= 0 || percentage >= 1) {
					System.out.println("Please enter a number greater than 0 and less than 1.");
				} else if (cutLength < minCutLength) {
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
			System.out.println("2. Basic Strat AI");
			System.out.println("3. Card Counter AI");
			while (true) {
				try {
					type = Integer.parseInt(reader.readLine());
					if (type <= 0 || type > 3) {
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
							break;
						case 3:
							players[i] = new CardCountingPlayer(bankroll);
							break;
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
		System.out.println();
		choosePlayer();
		System.out.println();
		
		//Play game
		while (true) {
			while (shoe.length() > cutLength) {
				//Players place bets
				int ruined = 0; //counter keeps track of the number of players who lost all their bankroll (ruined)
				for (int i = 0; i < players.length; i++) {
					if (!players[i].ruined()) {
						System.out.println("Player" + i + ":");
						System.out.println("Bankroll: " + players[i].bankroll() + "  Profit: " + players[i].profit());
						players[i].clearHands().placeBets();
					} else {
						ruined++;
					}
				}
				System.out.println();
				
				//If all players are ruined, end the game
				if (ruined == players.length) {
					System.out.println("All players are ruined.");
					return;
				}
				
				//Print the dealer's upcard and players' cards
				dealer.newHand();
				Card dealerCard = dealer.first();
				System.out.println("Dealer: [" + dealerCard +"]");
				for (int i = 0; i < players.length; i++) {
					if (!players[i].ruined()) {
						players[i].newHand();
						System.out.println("Player" + i + ": " + players[i].hands().get(0));
					}
				}
				System.out.println();
				
				//If dealer has ace as the upcard
				if (dealerCard.val() == 11) {
					//Offer players insurance
					for (int i = 0; i < players.length; i++) {
						if (!players[i].ruined()) {
							System.out.println("Player" + i + ":");
							players[i].placeInsurances();
						}
					}
					System.out.println();
					
					//If dealer has blackjack, evaluate win/lose immediately, without the players playing
					if (dealer.hand().val() == 21) {
						System.out.println("Dealer: " + dealer.reveal()); //reveal() updates the running count
						for (int i = 0; i < players.length; i++) {
							if (! players[i].ruined()) {
								System.out.println("Player" + i + ":");
								for (int j = 0; j <  players[i].hands().size(); j++) {
									ArrayList<PlayerHand> playerHands =  players[i].hands();
									System.out.print(playerHands.get(j));
									if (playerHands.get(j).val() == 21) { //If player also has blackjack
										System.out.println(" Blackjack!");
										if (playerHands.get(j).insured()) {
											System.out.println("Even Money");
											 players[i].update(2.5 * playerHands.get(j).bet());
										} else {
											System.out.println("Push");
											 players[i].update(playerHands.get(j).bet());
										}
									} else {
										if (playerHands.get(j).insured()) { //If player doesn't have blackjack
											System.out.println(" Insured");
											players[i].update(1.5 * playerHands.get(j).bet());
										} else {
											System.out.println(" Lose");
											players[i].update(0.0);
										}
									}
								}
							}
						}
						
						System.out.println();
						continue;
					}
				}
				
				//Players play their hands
				boolean allBusted = true;
				for (int i = 0; i < players.length; i++) {
					if (!players[i].ruined()) {
						System.out.println("Player" + i + ": ");
						players[i].play(dealerCard); //Dealer's card is passed as parameter
						
						//Check if player has busted
						if (!players[i].busted()) {
							allBusted = false;
						} else {
							players[i].update(0.0);
						}
					}
				}
				System.out.println();

				
				//Dealer does not play if all players have busted
				if (allBusted) {
					System.out.println("All players have busted.");
					System.out.println("Dealer: " + dealer.reveal());
					System.out.println();
					continue;
				}
				
				//Dealer plays their hand
				boolean dealerBlackjack = false, dealerBusted = false;
				System.out.print("Dealer: " + dealer.play());
				//If dealer has blackjack
				if (dealer.hand().val() == 21 && dealer.hand().size() == 2) {
					dealerBlackjack = true;
					System.out.println(" Blackjack!");
				}
				//If dealer has busted
				if (dealer.hand().busted()) {
					dealerBusted = true;
					System.out.println(" Bust");
				} else {
					System.out.println();
				}
				
				
				//For each player's hand, compare with the dealer's to determine the winner
				for (int i = 0; i < players.length; i++) {
					Player player = players[i];
					if (!player.ruined()) {
						System.out.println("Player" + i + ": ");
						ArrayList<PlayerHand> playerHands = player.hands();
						for (int j = 0; j < playerHands.size(); j++) {
							System.out.print(playerHands.get(j));
							//Player busts
							if (playerHands.get(j).busted()) {
								System.out.println(" Bust"); 
								//No update since we already done so earlier
								continue;
							}
							//Player has blackjack but dealer does not
							if (playerHands.get(j).val() == 21 && playerHands.get(j).size() == 2 && !dealerBlackjack) {
								System.out.println(" Blackjack!");
								player.update(2.5 * playerHands.get(j).bet());
								continue;
							}
							//Both have blackjack or same value
							if (playerHands.get(j).val() == dealer.hand().val()) {
								System.out.println(" Push");
								player.update(playerHands.get(j).bet());
								continue;
							}
							//Dealer busts but player does not or player has a higher non-blackjack hand
							if (dealerBusted || playerHands.get(j).val() > dealer.hand().val()) {
								System.out.println(" Win");
								player.update(2.0 * playerHands.get(j).bet());
								continue;
							}
							//Player does not bust but has a lower value hand
							if (playerHands.get(j).val() <  dealer.hand().val()) {
								System.out.println(" Lose");
								player.update(0.0);
								continue;
							}
						}
					}
				}
				System.out.println();
			}
			
			//Reshuffle if end of a shoe is reached
			shoe.shuffle();
			System.out.println("Reshuffling");
			System.out.println();
		}
	}
}
