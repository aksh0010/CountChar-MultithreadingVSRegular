import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountChar_Multithreading {

    // Class variables
    private File file; // File object for input file
    private StringBuilder finalString = new StringBuilder(); // StringBuilder to store file content
    private int whitespaceCount = 0; // Count of whitespace characters
    private Lock lock = new ReentrantLock(); // Lock for synchronization
    private Condition condition = lock.newCondition(); // Condition variable for synchronization

    // Constructor
    public CountChar_Multithreading(String filePath, int numThreads) {
        file = new File(filePath); // Initialize the file object with provided file path
        
        // Check if the number of threads is valid
        if (numThreads < 1) {
            throw new IllegalArgumentException("Number of threads must be at least 1");
        }
    }

    // Method to count whitespace characters using multithreading
    public void countWhitespace(int numThreads) {
        System.out.println("Multithread Main");

        try {
            // Read file content into StringBuilder
            Scanner inputScanner = new Scanner(file);
            while (inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                finalString.append(line).append('\n');
            }
            inputScanner.close();

            // Start time for measuring execution time
            long startTime = System.currentTimeMillis();

            // Initialize array to hold thread objects
            CountThread[] threads = new CountThread[numThreads];
            int chunkSize = finalString.length() / numThreads;
            int start = 0;

            // Start threads for counting whitespace
            for (int i = 0; i < numThreads; i++) {
                int end = (i == numThreads - 1) ? finalString.length() : start + chunkSize;
                threads[i] = new CountThread(finalString.substring(start, end), lock); // Pass the lock to each thread
                threads[i].start();
                start = end;
            }

            // Join threads
            for (int i = 0; i < numThreads; i++) {
                threads[i].join(); // Wait for thread to finish
                whitespaceCount += threads[i].getWhitespaceCount(); // Accumulate whitespace counts
            }

            // Output results
            System.out.println("Multithread Total Char: " + finalString.length());
            System.out.println("Multithread Total Whitespace: " + (whitespaceCount));

            // End time for measuring execution time
            long endTime = System.currentTimeMillis();
            System.out.println("Multithread Thread execution time: " + (endTime - startTime) + " milliseconds");

        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace(); // Print stack trace if file not found or interrupted exception occurs
        }
    }

    // Inner class representing a thread for counting whitespace characters
    static class CountThread extends Thread {
        private String chunk; // Chunk of text to process
        private int localWhitespaceCount = 0; // Count of whitespace characters in the chunk
        private Lock lock; // Lock for synchronization

        // Constructor
        public CountThread(String chunk, Lock lock) {
            this.chunk = chunk;
            this.lock = lock;
        }

        // Run method for thread execution
        @Override
        public void run() {
            System.out.println("Inside one of the thread");
            for (int i = 0; i < chunk.length(); i++) {
                if (Character.isWhitespace(chunk.charAt(i))) {
                    // Acquire the lock before updating the shared variable
                    lock.lock();
                    try {
                        localWhitespaceCount++; // Increment local count of whitespace characters
                    } finally {
                        // Release the lock after updating the shared variable
                        lock.unlock();
                    }
                }
            }
        }

        // Method to retrieve the count of whitespace characters in the chunk
        public int getWhitespaceCount() {
            return localWhitespaceCount;
        }
    }

    // Main method to execute the program
    public static void main(String[] args) {
        String filePath = "input.txt"; // File path
        int numThreads = 4; // Number of threads

        // Create an instance of CountChar_Multithreading
        CountChar_Multithreading counter = new CountChar_Multithreading(filePath, numThreads);
        // Count whitespace characters using multithreading
        counter.countWhitespace(numThreads);
    }
}
