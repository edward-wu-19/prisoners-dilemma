import java.util.*;
import java.io.*;

public class simulate{
    public static void main(String[] args){
        double[][] nash = {{0,0},{10,9.99}}; // nash[other player][your choice]

        // int[] test = playRandom(1, 0, nash);
        // System.out.println(test[0] + " and " + test[1]);

        int input = Integer.parseInt(args[0]);
        double[][] results = generateResults(input, nash);
        printTable(results);
        writeToCsv(results);
    }

    public static void writeToCsv(double[][] arr){
        try {
  
            // attach a file to FileWriter
            FileWriter fw
                = new FileWriter("./file.txt");

            String str = "";

            for (double[] row : arr){
                for (double x : row) {
                    str+= Math.round(100 * x) / 100.0;
                    str+= ",\t";
                }
                str+= "\n";
            }
  
            // read each character from string and write
            // into FileWriter
            for (int i = 0; i < str.length(); i++)
                fw.write(str.charAt(i));
  
            // close the file
            fw.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void printTable(double[][] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.print("Player has probability " + i + "/" + (arr.length - 1) + " to defect \t");
            for (int j = 0; j < arr[0].length; j++){
                System.out.print((Math.round(100 * arr[i][j]) / 100.0) + "\t");
            }
            System.out.println();
        }
    }

    // increment = 7 means p=0, 1/7, 2/7, ... , 6/7, 1
    public static double[][] generateResults(int increment, double[][] nash){
        double[][] results = new double[increment+1][increment+1];
        double p1 = 0, p2 = 0;
        double[] test = new double[2];
        for (int i = 0; i <= increment; i++){
            p2 = 0;
            for (int j = 0; j <= increment; j++){
                test = testRandom(p1, p2, nash);
                results[i][j] = test[0];
                p2 += 1/ (double) increment;
            }
            p1 += 1/ (double) increment;
        }
        return results;
    }

    // plays 100 matches between two strategies and returns average
    public static double[] testRandom(double p1, double p2, double[][] nash){
        double[] total = new double[2];
        double[] match = new double[2];
        for (int game = 1; game <= 100; game++){
            match = playRandom(p1, p2, nash);
            total[0] += match[0];
            total[1] += match[1];
        }
        total[0]/= 100;
        total[1]/= 100;
        return total;
    }

    // plays 100 rounds
    public static double[] playRandom(double p1, double p2, double[][] nash){
        double[] results = new double[2]; // nash[i][j] is you do i and opponent does j
        int player1, player2;
        for (int round = 1; round <= 100; round++){
            player1 = randomMove(p1);
            player2 = randomMove(p2);
            results[0] += nash[player1][player2];
            results[1] += nash[player2][player1];
        }
        return results;
    }

    // randomly decides to defect or not
    public static int randomMove(double p){ // p is probability of cheat
        if (Math.random() < p) return 0; // 0 is defect
        else return 1; // 1 is cooperate
    }
}