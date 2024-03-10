import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountChar {

    private static Scanner inputScanner = new Scanner(System.in);
    private static File file = new File("input.txt");
    private static StringBuilder finalString = new StringBuilder(); // shared string builder
    private static int whitespaceCount = 0; // shared whitespace coun
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Main");

        try {
            inputScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String content = getString();
        countWhitespace(content);

        System.out.println("Total Char: " + content.length());
        System.out.println("Total Whitespace: " + whitespaceCount);

        long endTime = System.currentTimeMillis();
        System.out.println("Thread execution time: " + (endTime - startTime) + " milliseconds");
    }

    public static String getString() {
        System.out.println("getString method");
//        StringBuilder finalString = new StringBuilder();

        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            finalString.append(line);
        }

        return finalString.toString();
    }

    public static int countWhitespace(String str) {
    	
        
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
            	whitespaceCount++;
            }
        }
        return whitespaceCount;
    }
}
