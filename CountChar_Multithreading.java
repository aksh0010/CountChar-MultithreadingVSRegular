import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountChar_Multithreading {

    private static File file = new File("input.txt");
    private static StringBuilder finalString = new StringBuilder();
    private static int whitespaceCount = 0;
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Main");

        try {
            Scanner inputScanner = new Scanner(file);

            // Read the file content into a StringBuilder
            while (inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                finalString.append(line).append('\n');
            }

            inputScanner.close();

            int numThreads = 2; // Number of threads to use
            CountThread[] threads = new CountThread[numThreads];

            // Calculate chunk size for each thread
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
            System.out.println("Total Char: " + finalString.length());
            System.out.println("Total Whitespace: " + whitespaceCount);

            long endTime = System.currentTimeMillis();
            System.out.println("Thread execution time: " + (endTime - startTime) + " milliseconds");

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
            lock.lock();
            try {
                for (int i = 0; i < chunk.length(); i++) {
                    if (Character.isWhitespace(chunk.charAt(i))) {
                        localWhitespaceCount++;
                    }
                }
                // Signal that this thread has finished counting
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public int getWhitespaceCount() {
            return localWhitespaceCount;
        }
    }
}
