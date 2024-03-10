import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountChar_Multithreading {

    private Scanner inputScanner;
    private File file;
    private Lock lock;
    private Condition condition;
    private StringBuilder finalString;
    private int whitespaceCount;
    private boolean lineRead;

    public CountChar_Multithreading(String filePath, int numThreads) {
        file = new File(filePath);
        lock = new ReentrantLock();
        condition = lock.newCondition();
        finalString = new StringBuilder();
        whitespaceCount = 0;
        lineRead = false;
        try {
            inputScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void countWhitespace() {
        Thread readingThread = new Thread(() -> {
            while (inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                lock.lock();
                try {
                    finalString.append(line);
                    lineRead = true;
                    condition.signal();
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            lock.lock();
            try {
                finalString.append('\0');
                lineRead = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        });

        Thread countingThread = new Thread(() -> {
            int index = 0;
            while (true) {
                lock.lock();
                try {
                    while (!lineRead) {
                        condition.await();
                    }
                    if (finalString.charAt(index) == '\0') {
                        break;
                    }
                    int count = 0;
                    while (index < finalString.length() && finalString.charAt(index) != '\n') {
                        if (Character.isWhitespace(finalString.charAt(index))) {
                            count++;
                        }
                        index++;
                    }
                    whitespaceCount += count;
                    lineRead = false;
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        readingThread.start();
        countingThread.start();

        try {
            readingThread.join();
            countingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total Char: " + finalString.length());
        System.out.println("Total Whitespace: " + whitespaceCount);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Main");

        String filePath = "input.txt";
        int numThreads = 2;

        CountChar_Multithreading counter = new CountChar_Multithreading(filePath, numThreads);
        counter.countWhitespace();

        long endTime = System.currentTimeMillis();
        System.out.println("Thread execution time: " + (endTime - startTime) + " milliseconds");
    }
}
