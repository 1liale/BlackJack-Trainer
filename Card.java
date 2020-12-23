
public class Card {

	private int id;
	
	public Card(int id) {
		this.id = id%52;
	}
	
	public int rank() {
		return id%13;
	}
	
	public int suit() {
		return id/13;
	}
	
	public int val() {
		int val = rank();
		if (val > 9) {val = 9;}
		if (val == 0) {val = 10;}
		return val + 1;
	}
	
	public int highLow() {
		int rank = rank();
		
		if (rank == 0 || rank >= 9) {return -1;}
		if (rank <= 5) {return 1;}
		return 0;
	}
	
	@Override
	public String toString() {
		int temp = rank();
		String rank = null;
		switch (temp) {
		case 0: rank = "A";
		break;
		case 10: rank = "J";
		break;
		case 11: rank = "Q";
		break;
		case 12: rank = "K";
		break;
		default: rank = Integer.toString(temp + 1);
		break;
		}

		temp = suit();
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

