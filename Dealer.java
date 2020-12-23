/**
 * Blackjack Dealer
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

public class Dealer extends BlackJack {
	private DealerHand hand;

	public Dealer(Card...cards) {
		hand = new DealerHand(cards);
	}

	//Dealer has one card in a new hand
	public DealerHand newHand() {
		hand.clear();
		hand.add(shoe().draw());
		return hand;
	}

	//Dealer draw cards until a 17 is reached
	public DealerHand play() {
		while (hand.val() < 16) {
			hand.add(shoe().draw());
		}
		
		return hand;
	}

	@Override
	//Return dealer's hand as a string
	public String toString() {
		return hand.toString();
	}
}
