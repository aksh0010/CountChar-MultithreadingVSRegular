import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountChar {

    // Class variables
    private static Scanner inputScanner = new Scanner(System.in); // Scanner object for input
    private static File file = new File("input.txt"); // File object for input file
    private static StringBuilder finalString = new StringBuilder(); // StringBuilder to store file content
    private static int whitespaceCount = 0; // Count of whitespace characters
    private static int newlineChars = 0; // Count of newline characters

    // Main method to execute the program
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Main");

        try {
            inputScanner = new Scanner(file); // Initialize scanner with file
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Print stack trace if file not found
        }

        String content = getString(); // Get content of the file as a string
        countWhitespace(content); // Count whitespace characters in the content

        // Output results
        
       
        System.out.println("Total Char: " + (content.length() + newlineChars));
        System.out.println("Total Whitespace: " + (whitespaceCount+newlineChars));
        
        // End time for measuring execution time
        long endTime = System.currentTimeMillis();
        System.out.println("Thread execution time: " + (endTime - startTime) + " milliseconds");
    }

    // Method to read the content of the file and store it in a StringBuilder
    public static String getString() {
        System.out.println("getString method");

        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            newlineChars++; // Increment newline count
            finalString.append(line);
        }

        return finalString.toString(); // Return the content as a string
    }

    // Method to count whitespace characters in a string
    public static int countWhitespace(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                whitespaceCount++; // Increment whitespace count
            }
        }
        return whitespaceCount; // Return the count of whitespace characters
    }
}
