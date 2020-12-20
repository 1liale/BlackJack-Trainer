
public class Card {

	private int id;
	
	public Card(int id) {
		this.id = id%52;
	}
	
	public int val() {
		int val = id%13;
		if (val > 9) {val = 9;}
		if (val == 0) {val = 10;}
		return val + 1;
	}
	
	@Override
	public String toString() {
		int temp = id%13;
		String rank = null;
		switch (temp) {
		case 0: rank = "A";
		break;
		case 1: rank = "2";
		break;
		case 2: rank = "3";
		break;
		case 3: rank = "4";
		break;
		case 4: rank = "5";
		break;
		case 5: rank = "6";
		break;
		case 6: rank = "7";
		break;
		case 7: rank = "8";
		break;
		case 8: rank = "9";
		break;
		case 9: rank = "10";
		break;
		case 10: rank = "J";
		break;
		case 11: rank = "Q";
		break;
		case 12: rank = "K";
		break;
		}

		temp = id/13;
		String suit = null;
		switch (temp) {
		case 0: suit = "\u2666";
		break;
		case 1: suit = "\u2663";
		break;
		case 2: suit = "\u2665";
		break;
		case 3: suit = "\u2660";
		}
		
		return rank + suit;
	}
}

