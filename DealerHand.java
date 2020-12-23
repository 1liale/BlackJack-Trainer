/**
 * A dealer's Hand, extends Hand
 * Author: Yifan Zong
 * Created on: 23/12/2020
 */
public class DealerHand extends Hand implements Comparable<PlayerHand> {
	public DealerHand(Card...cards) {
		super(cards);
	}
	
	@Override
	// -2: Player Busts
	// -1: Player Loses
	// 0: Push
	// 1: Player Wins
	// 2: Player Wins by BlackJack
	public int compareTo(PlayerHand o) {
		int val1 = val(), val2 = o.val();
		if (val2 > 21) {
			return -2;
		}
		if (val1 > 21 || val2 > val1) {
			if (val2 == 21 && o.size() == 2) {
				return 2;
			}
			return 1;
		}
		if (val2 == val1) {
			return 0;
		}
		return -1;
	}
}
