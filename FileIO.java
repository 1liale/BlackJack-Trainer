import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileIO {
    protected char[][][] basicChart;
    protected int[][] rowValues;
    protected File myFile;

    public FileIO()
    {
        myFile = new File("BlackJackStrat.txt");
        basicChart = new char[3][10][10];
        rowValues = new int[3][10];
        readFile();
    }

    public char[][][] getChart()
    {
        return basicChart;
    }

    public int[][] getRowValues()
    {
        return rowValues;
    }

    private void readFile()
    {
        try {
            Scanner sc = new Scanner(myFile);
            int chartNum = 0;
            int i = 0, j = 0;
            while(sc.hasNextLine())
            {
                String rawLine= sc.nextLine();
                String[] splitLines = rawLine.split(":");
                int rowVal = Integer.parseInt(splitLines[0]);
                if(rowVal != -1)
                {
                    for(j = 0; j < 10; j++)
                    {
                        basicChart[chartNum][i][j] = splitLines[1].charAt(j);
                    }
                    rowValues[chartNum][i] = rowVal;
                    i++;
                }
                else {
                    i = 0;
                    chartNum++;
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("BlackJackStrat.txt not found");
            e.printStackTrace();
        }
    }
}
