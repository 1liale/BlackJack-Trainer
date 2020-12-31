package src;

/**
 * src.Blackjack src.Dealer
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */

public class Dealer{
	private DealerHand hand;

	public Dealer(Card...cards) {
		hand = new DealerHand(cards);
	}

	//src.Dealer has one card in a new hand
	public DealerHand newHand() {
		hand.clear();
		hand.add(Blackjack.shoe().draw()).add(Blackjack.shoe().remove());
		return hand;
	}

	//Return dealer's hand
	public DealerHand hand() {
		return hand;
	}
	
	//Return the first card
	public Card first() {
		return hand.get(0);
	}
	
	//Reveal the second card (add the second card to the running count)
	public DealerHand reveal() {
		Blackjack.shoe().updateCount(hand.get(1));
		return hand;
	}
	
	//src.Dealer draw cards until a hard 17 is reached
	public DealerHand play() {
		reveal();
		
		//Handle soft 17
		if (hand.val() == 17 && hand.aces() == 1) {
			hand.add(Blackjack.shoe().draw());
		}
		
		while (hand.val() < 16) {
			hand.add(Blackjack.shoe().draw());
		}
		
		return hand;
	}

	@Override
	//Return dealer's hand as a string
	public String toString() {
		return hand.toString();
	}
}
