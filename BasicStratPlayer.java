import java.util.ArrayList;
import java.util.Scanner;

public class BasicStratPlayer extends Player {
    private final char[][][] basicChart;
    private final int[][] rowValues;
    private Scanner sc;

    public BasicStratPlayer(double bankroll, Card... cards) {
        super(bankroll, cards);
        FileIO fileReader = new FileIO();
        this.basicChart = fileReader.getChart();
        this.rowValues = fileReader.getRowValues();
        sc = new Scanner(System.in);
        //System.out.println("Basic strat player selected");
    }

    private void applyChart(PlayerHand playerHand, DealerHand dealerHand, int chartNum) {
        int firstVal = playerHand.get(0).val();
        int secondVal = playerHand.get(1).val();
        int playerSum = playerHand.val();
        int upCardVal = dealerHand.val();
        // System.out.println(playerSum + " " + upCardVal + " " + chartNum);

        int i;
        for (i = 0; i < rowValues.length; i++) {
            if (rowValues[chartNum][i] == playerSum && chartNum == 0)
                break;
            else if (rowValues[chartNum][i] == secondVal && chartNum == 1)
                break;
            else if (firstVal == secondVal && chartNum == 2)
                break;
        }

        for (int j = 0; j < basicChart[chartNum][i].length; j++) {
            if (j + 2 == upCardVal) {
                makeDecision(basicChart[chartNum][i][j], playerHand, dealerHand);
                // System.out.println(basicChart[chartNum][i][j]);
                break;
            }
        }
    }

    public void makeDecision(char action, PlayerHand playerHand, DealerHand dealerHand) {
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
                    doubleDown(playerHand, playerHand.bet());
                } catch (InvalidBetException e) {
                    e.printStackTrace();
                } catch (BetExceedsBankrollException e) {
                    e.printStackTrace();
                } catch (BetExceedsBetException e) {
                    e.printStackTrace();
                }
                break;
            case 'O':
            case 'Y':
                try {
                    split(playerHand);
                } catch (InvalidSplitException e) {
                    e.printStackTrace();
                } catch (BetExceedsBankrollException e) {
                    e.printStackTrace();
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
            int chartSelection = 0;
            int aces = 0;
            int tempVal = -1;
            int[] values = new int[hands().get(i).size()];

            for (int j = 0; j < hands().get(i).size(); j++) {
                values[j] = hands().get(i).get(j).val();
            }

            System.out.println(hands().get(i));
            while (!hands().get(i).isDone()) {
                // determine which chart to use (hard, soft, or split)
                for (int j = 0; j < hands().get(i).size(); j++) {
                    if (tempVal == hands().get(i).get(j).val() && hands().get(i).size() == 2)
                    {
                        chartSelection = 2;
                        break;
                    }
                    else if (hands().get(i).get(j).val() == 11)
                    {
                        aces++;
                        chartSelection = 1;
                    }
                    tempVal = hands().get(i).get(j).val();
                }
                if (hands().get(i).val() > 17 && aces == 0)
                {
                    makeDecision('S', hands().get(i), dealerHand);
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
            double bet = 0.0;
            System.out.println("How much to bet?");
            try {
                bet = Double.parseDouble(sc.nextLine());
                placeBet(hands().get(i), bet);
            } catch (NumberFormatException | InvalidBetException e) {
                System.out.println("Invalid bet, please enter again.");
                i--;
            } catch (BetExceedsBankrollException e) {
                System.out.println("Bet exceeds bankroll. Please place a lower bet.");
                i--;
            }
        }

        return hands();
    }
}
