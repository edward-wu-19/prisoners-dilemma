import java.util.*;
import java.io.*;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class game extends sim_adv{
    
    public static int str_length = 8;
    public static int trials = 4;

    public static void main(String[] args){
        
        print_rules();

        double[][] data = gather_results(trials);

        System.out.println("============================================================");
        System.out.println("How many of your answers aligned with each of the following strategies? Maximum is " + str_length + ".");
        printResults(data);

        // System.out.println(data[0][0]); // 0
        // System.out.println(data[1][1]); // 0
        // System.out.println(data[1][0]); // 8

        Matrix B = new Matrix(data);

        double[][] data_normalized = normalize(data);

        System.out.println("Normalized in each trial:");
        printResults(data_normalized);

        double[] data_averaged = averageAmongTrials(data_normalized);

        System.out.println("Averaged across trials:");
        printResults(data_averaged);

        double[] normalized_averages = normalize(data_averaged);

        System.out.println("Normalized averages");
        printResults(normalized_averages);

        
        // data from computer games
        double[] expectedScorePerStrategy = runExperimentGetAverages(10);

        double expectedScore = dotProduct(normalized_averages, expectedScorePerStrategy);

        System.out.println("Expected Score");
        System.out.println(expectedScore);

        // System.out.println(B.norm2());

        
        // SVDstuff(B);

    }

    // assumes arr1 and arr2 have same dimension
    public static double dotProduct(double[] arr1, double[] arr2){
        double sum = 0.0;
        for (int i = 0; i < arr1.length; i++){
            sum+= arr1[i] * arr2[i];
        }
        sum = Math.round(100.0 * sum) / 100.0;
        return sum;
    }

    public static double[] normalize(double[] arr){
        double mag = 0.0;
        for (double x : arr){
            mag+= x;
        }
        for (int i = 0; i < arr.length; i++){
            arr[i]/= mag;
            arr[i] = Math.round(100.0 * arr[i]) / 100.0;
        }
        return arr;
    }

    public static double[][] normalize(double[][] arr){
        for (int i = 0; i < arr[0].length; i++){
            double mag = 0.0;
            for (int j = 0; j < arr.length; j++){
                mag+= Math.pow(arr[j][i], 2);
            }
            mag = Math.pow(mag, 0.5);
            // System.out.println(mag);
            for (int j = 0; j < arr.length; j++){
                arr[j][i] = Math.round(100.0 * arr[j][i] / mag) / 100.0;
            }
        }
        return arr;
    }

    public static double[] averageAmongTrials(double[][] arr){
        double[] averages = new double[arr.length];
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                averages[i] += arr[i][j];
            }
            averages[i] /= arr[0].length;
            averages[i] = Math.round(100.0 * averages[i]) / 100.0;
        }
        return averages;
    }

    public static void SVDstuff(Matrix B){
        Matrix A = B.times(B.transpose());
        System.out.print("A = ");
        A.print(6, 3);

        SingularValueDecomposition s = A.svd();

        System.out.print("U = ");
        Matrix U = s.getU();
        U.print(6, 3);
        System.out.print("Sigma = ");
        Matrix S = s.getS();
        S.print(6, 3);
        System.out.print("V = ");
        Matrix V = s.getV();
        V.print(6, 3);
        System.out.println("rank = " + s.rank());
        System.out.println("condition number = " + s.cond());
        System.out.println("2-norm = " + s.norm2());

        // print out singular values
        System.out.print("singular values = ");
        Matrix svalues = new Matrix(s.getSingularValues(), 1);
        svalues.print(6, 3);
    }

    public static void print_rules(){
        System.out.println();

        System.out.println("If both cooperate, you each win " + points[1][1] + " points.");
        System.out.println("If one person defects, the person who defects wins " + points[1][0] + " points,\nwhile the cooperator wins " + points[0][1] + " points.");
        System.out.println("If both defect, each player wins " + points[0][0] + " points.");

        System.out.println();

        System.out.println("A 0 represents a defection and a 1 represents a cooperation.");

        System.out.println();
    }

    public static double[][] gather_results(int trials){
        double[][] total_results = new double[9][trials];
        
        for (int i = 0; i < trials; i++){
            System.out.println("============================================================");
            System.out.println("Game " + (i+1) + "!\n");

            // generate test string
            String str = randomString(str_length);

            String input = enterInfo(str);

            // System.out.println(input);

            // System.out.println("String " + str);
            // System.out.println("Always defect " + alwaysDefect(str));
            // System.out.println("Always cooperate " + alwaysCooperate(str));
            // System.out.println("tit for tat " + titForTat(str));
            // System.out.println("tit for two tats " + titForTwoTats(str));
            // System.out.println("two tits for tat " + twoTitsForTats(str));
            // System.out.println("grudge " + grudge(str));
            // System.out.println("majority, defect " + majDefect(str));
            // System.out.println("majority, cooperate " + majCooperate(str));
            // System.out.println("second chance " + secondChance(str));

            
            double[] game_results = analyze_input(str, input);

            // System.out.println(results[0]);
            // System.out.println(results[1]); // these should add to 8, the length of the string

            for (int j = 0; j < 9; j++){
                total_results[j][i] = game_results[j];
            }
        }

        return total_results;
    }

    public static void printArray(double[][] arr){
        for (double[] row : arr){
            for (double x : row){
                System.out.print(x + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void printResults(double[][] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.printf("%-30s\t", given_strategies[i]);
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
            System.out.printf("%-30s\t", given_strategies[i]);
            System.out.println(arr[i]);
        }
        System.out.println();
    }

    public static String randomString(int n){
        String s = "";
        for (int i = 0; i < n; i++){
            if (Math.random() < 0.5) s+= '0';
            else s+= '1';
        }
        return s;
    }

    public static double[] analyze_input(String q, String input){
        String[] answers = new String[9];

        answers[0] = alwaysDefect(q);
        answers[1] = alwaysCooperate(q);
        answers[2] = titForTat(q);
        answers[3] = titForTwoTats(q);
        answers[4] = twoTitsForTats(q);
        answers[5] = grudge(q);
        answers[6] = majDefect(q);
        answers[7] = majCooperate(q);
        answers[8] = secondChance(q);

        double[] results = new double[9];

        // score is how many they have in common
        for (int i = 0; i < 9; i++){
            results[i] = 0;
            for (int j = 0; j < q.length(); j++){
                if (answers[i].charAt(j) == input.charAt(j)) results[i]++;
            }
        }

        return results;
    }

    // Strategies
    
    public static String alwaysDefect(String q){
        String s = "";
        for (int i = 0; i < q.length(); i++) s+= "0";
        return s;
    }

    public static String alwaysCooperate(String q){
        String s = "";
        for (int i = 0; i < q.length(); i++) s+= "1";
        return s;
    }

    public static String titForTat(String q){
        String s = "";
        if (q.length() > 0) s+= "1";
        for (int i = 0; i < q.length()-1; i++){
            s+= q.charAt(i);
        }
        return s;
    }
    
    public static String titForTwoTats(String q){
        String s = "";
        if (q.length() > 0) s+= "1";
        if (q.length() > 1) s+= "1";
        for (int i = 0; i < q.length()-2; i++){
            if (charToInt(q.charAt(i)) + charToInt(q.charAt(i+1)) == 0) s+= "0";
            else s+= "1";
        }
        return s;
    }

    public static String twoTitsForTats(String q){
        String s = "";
        if (q.length() > 0) s+= "1";
        if (q.length() > 1) s+= q.charAt(0);
        for (int i = 0; i < q.length()-2; i++){
            if (charToInt(q.charAt(i)) + charToInt(q.charAt(i+1)) < 2) s+= "0";
            else s+= "1";
        }
        return s;
    }
    
    public static String grudge(String q){
        String s = "";
        double sum = 0.0;
        for (int i = 0; i < q.length(); i++){
            if (sum == i) s+= "1";
            else s+= "0";
            sum+= charToInt(q.charAt(i));
        }
        return s;
    }

    public static String majDefect(String q){
        String s = "";
        double sum = 0.0;
        if (q.length() > 0){
            s+= "0";
            sum+= charToInt(q.charAt(0));
        }
        for (int i = 1; i < q.length(); i++){
            if (sum / i <= 0.5) s+= "0";
            else s+= "1";
            sum += charToInt(q.charAt(i));
        }
        return s;
    }

    public static String majCooperate(String q){
        String s = "";
        double sum = 0.0;
        if (q.length() > 0){
            s+= "1";
            sum+= charToInt(q.charAt(0));
        }
        for (int i = 1; i < q.length(); i++){
            if (sum / i >= 0.5) s+= "1";
            else s+= "0";
            sum += charToInt(q.charAt(i));
        }
        return s;
    }

    public static String secondChance(String q){
        String s = "";
        int sum = 0;
        for (int i = 0; i < q.length(); i++){
            if (sum < i-1) s+= "0";
            else s+= "1";
            sum+= charToInt(q.charAt(i));
        }
        return s;
    }

    public static int charToInt(char c){
        return c - '0';
    }

    public static String enterInfo(String q){
        String s = "";
        
        // first move
        System.out.println("New game!\nWhat is your first move?");
        
        System.out.println("Please enter C for Cooperate and D for Defect.");
        s+= convert(readInput().charAt(0));
        System.out.println();

        for (int i = 0; i < q.length(); i++){
            System.out.println("You " + convert(s.charAt(i)) + "ed, and your opponent " + convert(q.charAt(i)) + "ed.");
            
            if (i != q.length() - 1){
                System.out.println("What do you play?");
                s+= convert(readInput().charAt(0));
                // System.out.println(s.charAt(i));
            }
            else{
                System.out.println("End of game!");
            }
            System.out.println();
        }

        System.out.println("You played           " + s);
        System.out.println("Your opponent played " + q);
        System.out.println();

        return s;
    }

    public static String readInput(){
        Scanner in = new Scanner(System.in);
 
        String s = in.nextLine();
        // System.out.println("You entered string " + s);

        // in.close();

        return s;
    }

    public static String convert(char c){
        switch(c){
            case '0':
            return "defect";
            case '1':
            return "cooperat";
            case 'C':
            return "1";
            case 'D':
            return "0";
            case 'c':
            return "1";
            case 'd':
            return "0";
            default:
            return "";
        }
    }
}