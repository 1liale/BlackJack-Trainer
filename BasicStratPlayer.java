import java.util.ArrayList;
import java.util.Scanner;

/**
 * Purpose: A CPU Blackjack player using the Basic Strat Chart
 * Author: Alex Li
 * Created on: 23/12/2020
 */

public class BasicStratPlayer extends Player
{
    // Initialize and populate basicChart and rowValues
    private static FileIO fileReader = new FileIO();
    private static final char[][][] basicChart = fileReader.getChart();
    private static final int[][] rowValues = fileReader.getRowValues();
    private Scanner sc;
    private static int bet = 1;

    // Initializes basic strat player
    public BasicStratPlayer(double bankroll, Card... cards) {
        super(bankroll, cards);
        sc = new Scanner(System.in);
        //System.out.println("Basic strat player selected");
    }

    // Produces the next optimal move
    public static String getNextMove(PlayerHand playerHand, DealerHand dealerHand)
    {
        int chartNum = 0;
        int aces = 0;
        int tempVal = -1;

        //System.out.println(hands().get(i).val() + " " + aces);

        // determine which chart to use (hard, soft, or split)
        for (int j = 0; j < playerHand.size(); j++) {
            if (tempVal == playerHand.get(j).rank() && playerHand.size() == 2)
            {
                chartNum = 2;
                break;
            }
            else if (playerHand.get(j).val() == 11)
            {
                aces++;
                chartNum = 1;
            }
            tempVal = playerHand.get(j).rank();
        }

        if (playerHand.val() > 17 && aces == 0)
        {
            return "S";
        }

        if (playerHand.val()  < 8 && aces == 0)
        {
            return "H";
        }

        // Raw output from the basic strat chart
        char output = applyChart1(playerHand,dealerHand, chartNum);

        // processes output and produces it as a string
        switch (output)
        {
            case 'H':
                return "H";
            case 'S':
                return "S";
            case 'X':
            case 'D':
                return "D";
            case 'O':
            case 'Y':
                return "SP";
            case 'N':
                if(playerHand.val() <= 14)
                    return Character.toString(applyChart1(playerHand, dealerHand, 0));
                else
                    return "S";
        }
        return "";
    }

    // determines the corresponding character in the basic strat chart
    private static char applyChart1(PlayerHand playerHand, DealerHand dealerHand, int chartNum)
    {
        int firstVal = playerHand.get(0).val();
        int secondVal = playerHand.get(1).val();
        int playerSum = playerHand.val();
        int upCardVal = dealerHand.val();

        // find the row value (i)
        int i;
        for (i = 0; i < rowValues[chartNum].length; i++) {
            if (rowValues[chartNum][i] == playerSum && chartNum == 0){
                //System.out.println('X');
                break;}
            else if ((rowValues[chartNum][i] == secondVal || rowValues[chartNum][i] == firstVal)
                    && chartNum == 1) {
                //System.out.println('Y');
                break;
            }
            else if ((rowValues[chartNum][i] == secondVal || rowValues[chartNum][i] == firstVal)
                    && chartNum == 2) {
                //System.out.println('Z');
                break;
            }
        }

        // produces the corresponding character on the chart
        for (int j = 0; j < basicChart[chartNum][i].length; j++) {
            if (j + 2 == upCardVal) {
                //System.out.println(i + " " + j + " " + basicChart[chartNum][i][j]);
                return basicChart[chartNum][i][j];
            }
        }
        return ' ';
    }

    // See function applyChart1 for reference
    private void applyChart(PlayerHand playerHand, DealerHand dealerHand, int chartNum) {
        int firstVal = playerHand.get(0).val();
        int secondVal = playerHand.get(1).val();
        int playerSum = playerHand.val();
        int upCardVal = dealerHand.val();

        int i;
        for (i = 0; i < rowValues[chartNum].length; i++) {
            if (rowValues[chartNum][i] == playerSum && chartNum == 0){
                //System.out.println('X');
                break;}
            else if ((rowValues[chartNum][i] == secondVal || rowValues[chartNum][i] == firstVal)
                    && chartNum == 1) {
                //System.out.println('Y');
                break;
            }
            else if ((rowValues[chartNum][i] == secondVal || rowValues[chartNum][i] == firstVal)
                    && chartNum == 2) {
                //System.out.println('Z');
                break;
            }
        }

        for (int j = 0; j < basicChart[chartNum][i].length; j++) {
            if (j + 2 == upCardVal) {
                // System.out.println(i + " " + j + " " + basicChart[chartNum][i][j]);
                makeDecision(basicChart[chartNum][i][j], playerHand, dealerHand);
                break;
            }
        }
    }

    // Acts based on the character from basicChart
    private void makeDecision(char action, PlayerHand playerHand, DealerHand dealerHand) {
        switch (action) {
            case 'S':
                stand(playerHand);
                break;
            case 'H':
                hit(playerHand);
                break;
            case 'X':
            case 'D':
                try {
                    doubleDown(playerHand, bet);
                } catch (InvalidBetException e) {
                    e.printStackTrace();
                    System.exit(0);
                } catch (BetExceedsBankrollException e) {
                    e.printStackTrace();
                } catch (BetExceedsBetException e) {
                    e.printStackTrace();
                }
                break;
            case 'O':
            case 'Y':
                try {
                    // for (Hand hand : hands())
                        // System.out.println("split:" + hand);
                    split(playerHand);
                } catch (InvalidSplitException e) {
                    e.printStackTrace();
                    System.exit(0);
                } catch (BetExceedsBankrollException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                break;
            case 'N':
                if(playerHand.val() <= 14)
                    applyChart(playerHand, dealerHand, 0);
                else
                    stand(playerHand);
                break;
        }
    }

    @Override
    public ArrayList<PlayerHand> play(DealerHand dealerHand) {
        for (int i = 0; i < hands().size(); i++) {

            System.out.println(hands().get(i));
            while (!hands().get(i).isDone()) {
                int tempVal = -1;
                int chartSelection = 0;
                int aces = 0;
                // determine which chart to use (hard, soft, or split)

                //System.out.println(hands().get(i).val() + " " + aces);
                for (int j = 0; j < hands().get(i).size(); j++) {
                    if (tempVal == hands().get(i).get(j).rank() && hands().get(i).size() == 2)
                    {
                        chartSelection = 2;
                        break;
                    }
                    else if (hands().get(i).get(j).val() == 11)
                    {
                        aces++;
                        chartSelection = 1;
                    }
                    tempVal = hands().get(i).get(j).rank();
                }

                if (hands().get(i).val() == 21 || (hands().get(i).val() > 17 && aces == 0))
                {
                    //System.out.println('S');
                    makeDecision('S', hands().get(i), dealerHand);
                    continue;
                }

                if (hands().get(i).val() < 8 && aces == 0)
                {
                    //System.out.println('H');
                    makeDecision('H', hands().get(i), dealerHand);
                    continue;
                }
                applyChart(hands().get(i), dealerHand, chartSelection);

            }
        }
        return hands();
    }

    @Override
    public ArrayList<PlayerHand> placeBets() {
        System.out.println("Bankroll: " + bankroll() + "  Profit: " + profit());
        for (int i = 0; i < hands().size(); i++) {
            try
            {
                if (bankroll() >= 2)
                    placeBet(hands().get(i), bet);
                else {
                    ruined();
                }
            } catch (NumberFormatException | InvalidBetException e) {
                e.printStackTrace();
            } catch (BetExceedsBankrollException e) {
                e.printStackTrace();
            }
        }

        return hands();
    }

	@Override
	protected boolean updateRuined() {
		if (bankroll() < 2 * bet) {setRuined();}
		return ruined();
	}
}
