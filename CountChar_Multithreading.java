import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountChar_Multithreading {

    private File file;
    private StringBuilder finalString = new StringBuilder();
    private int whitespaceCount = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
//    private int total_newline=0;

    public CountChar_Multithreading(String filePath, int numThreads) {
        file = new File(filePath);
        if (numThreads < 1) {
            throw new IllegalArgumentException("Number of threads must be at least 1");
        }
    }

    public void countWhitespace(int numThreads) {
        long startTime = System.currentTimeMillis();
        System.out.println("Multithread Main");

        try {
            Scanner inputScanner = new Scanner(file);

            // Read the file content into a StringBuilder
            while (inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                finalString.append(line).append('\n');
//                total_newline++;
            }

            inputScanner.close();

            CountThread[] threads = new CountThread[numThreads]; // Number of threads
            int chunkSize = finalString.length() / numThreads;
            int start = 0;

            // Start threads for counting whitespace
            for (int i = 0; i < numThreads; i++) {
                int end = (i == numThreads - 1) ? finalString.length() : start + chunkSize;
                threads[i] = new CountThread(finalString.substring(start, end));
                threads[i].start();
                start = end;
            }

            // Join threads
            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
                whitespaceCount += threads[i].getWhitespaceCount();
            }

            // Output results
            System.out.println("Multithread Total Char: " + finalString.length());
            System.out.println("Multithread Total Whitespace: " + (whitespaceCount));

            long endTime = System.currentTimeMillis();
            System.out.println("Multithread Thread execution time: " + (endTime - startTime) + " milliseconds");

        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class CountThread extends Thread {
        private String chunk;
        private int localWhitespaceCount = 0;

        public CountThread(String chunk) {
            this.chunk = chunk;
        }

        @Override
        public void run() {
        	System.out.println("Inside one of the thread");
            for (int i = 0; i < chunk.length(); i++) {
                if (Character.isWhitespace(chunk.charAt(i))) {
                    localWhitespaceCount++;
                }
            }
        }

        public int getWhitespaceCount() {
            return localWhitespaceCount;
        }
    }

    public static void main(String[] args) {
        String filePath = "input.txt"; // File path
        int numThreads = 4; // Number of threads

        CountChar_Multithreading counter = new CountChar_Multithreading(filePath, numThreads);
        counter.countWhitespace(numThreads);
    }
}
