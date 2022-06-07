import java.util.*;
import java.io.*;

public class sim_adv{

    public static double[][] points = {{0,0},{5,4}};
    public static String[] given_strategies = {"Always defect", "Always cooperate", "Tit for Tat", "Tit for Two Tats", "Two Tits for Tat", "Grudge", "Majority, default Defect", "Majority, default Cooperate", "Second Chance"};

    public static String[] full_strategies = {"Always cooperate", "Random, 10% defect", "Random, 20% defect", "Random, 30% defect", "Random, 40% defect", "Random, 50% defect", "Random, 60% defect", "Random, 70% defect", "Random, 80% defect", "Random, 90% defect", "Always defect", "Tit for Tat", "Tit for Two Tats", "Two Tits for Tat", "Grudge", "Majority, default Defect", "Majority, default Cooperate", "Second Chance"};

    public static void main(String[] args){
        System.out.println(); 

        // int[] test = playRandom(1, 0, nash);
        // System.out.println(test[0] + " and " + test[1]);

        // double input = Double.parseDouble(args[0]);
        // double input2 = Double.parseDouble(args[1]);

        // System.out.println("Average score Strategy 1 wins against Strategy 2 is " + average(input, input2));

        int n = 10;

        runExperimentAndWrite(n);

        // double[][] results = runExperiment(n);

        // writeToCsv(results, "results.csv");

        // double[] averages = average(results, n);

        // writeToCsv(averages, "averages.csv");
    }

    public static void printResults(double[][] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.printf("%-26s\t", full_strategies[i]);
            double[] row = arr[i];
            for (double x : row){
                System.out.print(x + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printResults(double[] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.printf("%-26s\t", full_strategies[i]);
            System.out.println(arr[i]);
        }
        System.out.println();
    }

    public static double[] runExperimentGetAverages(int n){
        double[][] results = runExperiment(n);

        writeToCsv(results, "results.csv");

        double[] averages = average(results, n);

        writeToCsv(averages, "averages.csv");

        return averages;
    }

    public static void runExperimentAndWrite(int n){
        double[][] results = runExperiment(n);

        writeToCsv(results, "results.csv");

        System.out.println("Results");
        printResults(results);

        double[] averages = average(results, n);

        writeToCsv(averages, "averages.csv");

        System.out.println("Averages");
        printResults(averages);
    }

    public static double[] average(double[][] results, int n){
        double[] averages = new double[n+8];
        for (int i = 0; i < averages.length; i++){
            for (int j = 0; j < averages.length; j++){
                averages[i]+= results[i][j];
            }
            averages[i]/= (n+7);
            averages[i] = Math.round(100.0 * averages[i]) / 100.0;
        }
        return averages;
    }

    public static double[][] runExperiment(int n){
        double[][] results = new double[n+8][n+8];
        double[] keys = {2.0, 2.1, 2.2, 3.0, 4.0, 4.1, 5.0};
        for (int i = 0; i < n+1; i++){
            // two random players
            for (int j = 0; j < n+1; j++){
                results[i][j] = average(i/(double)n, j/(double)n);
            }

            // i is random, other player is not random
            for (int j = 0; j < 7; j++){
                results[i][j + n + 1] = average(i/(double)n, keys[j]);
                results[j+n+1][i] = average(keys[j], i/(double)n);
            }
        }

        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                results[i+n+1][j+n+1] = average(keys[i], keys[j]);
            }
        }

        return results;

        // Hashtable<Double, String> hash = new Hashtable<Double, String>();
        // hash.put(0.0, "Always cooperate");
        // hash.put(1.0, "Always defect");
        // hash.put(2.0, "Tit for tat");
        // hash.put(2.1, "Tit for two tats");
        // hash.put(2.2, "Two tits for tat");
        // hash.put(3.0, "Grudge");
        // hash.put(4.0, "Majority, default defect");
        // hash.put(4.1, "Majority, default cooperate");
        // hash.put(5.0, "Second chance");
    }

    {// public static void writeToCsv(double[][] arr, String file_name){
        // try{
        //     // attach a file to FileWriter
        //     FileWriter fw
        //         = new FileWriter("./results.txt");

        //     String str = "Yours (down)\t";

        //     String[] strategies = {"TfT", "Tf2T", "2TfT", "GRD", "MAJD", "MAJC", "SC"};

        //     for (int i = 0; i < arr.length-7; i++){
        //         str+= "RND " + (i / (double)(arr.length - 7));
        //         str+= ",\t";
        //     }
        //     for (int i = 0; i < 7; i++){
        //         str+= strategies[i] + ",\t";
        //     }
        //     str+="\n";

        //     for (int i = 0; i < arr.length; i++){
        //         double[] row = arr[i];

        //         if (i < arr.length-7) str+= "RND " + (i / (double)(arr.length - 7)) + ",\t";
        //         else str+= strategies[i] + ",\t";

        //         for (double x : row) {
        //             str+= Math.round(100 * x) / 100.0;
        //             str+= ",\t";
        //         }
        //         str+= "\n";
        //     }
  
        //     // read each character from string and write
        //     // into FileWriter
        //     for (int i = 0; i < str.length(); i++)
        //         fw.write(str.charAt(i));
  
        //     // close the file
        //     fw.close();
        // }
    // }
        }

    public static void writeToCsv(double[][] arr, String file_name){
        try {
  
            // attach a file to FileWriter
            FileWriter fw
                = new FileWriter(file_name);

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

    public static void writeToCsv(double[] arr, String file_name){
        try {
  
            // attach a file to FileWriter
            FileWriter fw
                = new FileWriter(file_name);

            String str = "";

            for (double x : arr){
                str+= Math.round(100 * x) / 100.0;
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

    // generate a new game sheet
    public static int[][] generateBlankMatch(){
        int[][] game = new int[2][100];
        for (int i = 0; i < 100; i++){
            game[0][i] = -1;
            game[1][i] = -1;
        }
        return game;
    }

    // determine the average score of strategy1 against strategy2 over 100 games
    public static double average(double strategy1, double strategy2){
        double total = 0.0;
        double[] match = new double[2];
        for (int game = 1; game <= 100; game++){
            match = playMatch(strategy1, strategy2);
            total += match[0];
        }
        total/= 100;
        return total;
    }

    public static double[] playMatch(double strategy1, double strategy2){
        int[][] game = generateBlankMatch(); // nash[i][j] is you do i and opponent does j, holds 0s and 1s

        double[] score = new double[2];
        int player1, player2;
        for (int round = 1; round <= 100; round++){
            player1 = makeMove(strategy1, game, round, 1);
            player2 = makeMove(strategy2, game, round, 2);
            game[0][round-1] = player1;
            game[1][round-1] = player2;
            // System.out.println("Round " + round + " " + player1 + " " + player2);
            score[0] += points[player2][player1];
            score[1] += points[player1][player2];
        }
        return score;
    }

    // make a move for each player given the previous move and the strategy chosen
    // game has two rows, first row is 
    // 0 is defect, 1 is cooperate, and -1 is not played yet
    public static int makeMove(double id, int[][] game, int round, int player){
        if (id <= 1) return randomMove(id); // 0<=id<=1 -> random with probability to defect = id
        else if (id == 2) return TftMove(game, round, player); // 2 = tit for tat
        else if (id == 2.1) return Tf2tMove(game, round, player); // 2.1 = tit for 2 tats
        else if (id == 2.2) return T2ftMove(game, round, player); // 2.2 = 2 tits for tat
        else if (id == 3) return grudge(game, round, player); // 3 = grudge
        else if (id == 4.0) return majDef(game, round, player); // 4.1 = majority, default defect
        else if (id == 4.1) return majCoop(game, round, player); // 4.2 = majority, default cooperate
        else if (id == 5) return secondChance(game, round, player); // 5 = second chance
        else return -1;
    }

     // randomly decides to defect or not
    public static int randomMove(double p){ // p is probability of cheat
        if (Math.random() < p) return 0; // 0 is defect
        else return 1; // 1 is cooperate
    }

    public static int TftMove(int[][] game, int round, int player){ // round is 1-100 and player is 1-2
        if (round == 1) return 1;
        else return game[2-player][round-2];
    }

    public static int Tf2tMove(int[][] game, int round, int player){
        if (round == 1 || round == 2) return 1;
        else if (game[2-player][round-2] == 0 && game[2-player][round-3] == 0) return 0;
        else return 1;
    }

    public static int T2ftMove(int[][] game, int round, int player){
        if (round == 1 || (round == 2 && game[2-player][round-2] == 1)) return 1;
        else if (game[2-player][round-2] == 1 && game[2-player][round-3] == 1) return 1;
        else return 0;
    }

    public static int grudge(int[][] game, int round, int player){ // round is 1-100 and player is 1-2
        if (round == 1) return 1;
        else if (game[2-player][round-2] == 0) return 0;
        else return game[player-1][round-2];
    }

    public static int majCoop(int[][] game, int round, int player){
        if (round == 1) return 1;
        else{
            double sum = 0;
            for (int i = 0; i < round-1; i++){
                sum+= game[2-player][round-2];
            }
            if (sum / (round-1) >= 0.5) return 1;
            else return 0;
        }
    }

    public static int majDef(int[][] game, int round, int player){
        if (round == 1) return 0;
        else{
            double sum = 0;
            for (int i = 0; i < round-1; i++){
                sum+= game[2-player][round-2];
            }
            if (sum / (round-1) > 0.5) return 1;
            else return 0;
        }
    }

    public static int secondChance(int[][] game, int round, int player){
        int sum = 0;
        for (int i = 0; i < round-1; i++){
            sum+= 1 - game[2-player][round-2];
        }
        if (sum < 2) return 1;
        else return 0;
    }
}