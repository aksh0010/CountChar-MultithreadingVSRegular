import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountChar_Multithreading {

  private static Scanner input_Scanner= new Scanner(System.in);
  private static File file;
 
  private static int total_threads;
  private static Lock lock = new ReentrantLock();
  private static Condition condition = lock.newCondition();
  

  public CountChar_Multithreading(String filename, int total) {
	  
	   total_threads = total;
	  try {
		file = new File (filename);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
  
  public static void main(String[] args)throws InterruptedException {
	  
		 long startTime = System.currentTimeMillis();   
		 String filename = "input.txt"; // Change the filename here
	     int numThreads = 1; // Change the number of threads as needed

	
		CountChar_Multithreading threading_object= new CountChar_Multithreading(filename, numThreads);

		
		for (int i =0 ; i < numThreads;i++) {
			
			Thread thread = new Thread(()-> {
	            lock.lock();
	            try {
	                // Produce data
	                System.out.println("Producing data...");
	                threading_object.getString();
	                condition.signal(); // Signal consumer
	            } finally {
	                lock.unlock();
	            }
	        });

			thread.start();
		}
		
		  lock.lock();
	        try {
	            while (total_threads > 0) {
	                condition.await();
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } finally {
	            lock.unlock();
	        }
		
		

    long endTime = System.currentTimeMillis();
    System.out.println("Thread execution time: " + (endTime - startTime) + " milliseconds");
  }

  public String getString() {
	int whitespace_count=0;
	System.out.println("getstring method");
    StringBuilder final_string = new StringBuilder();
    try {
		input_Scanner = new Scanner(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    lock.lock();
    try {
    	
    	  while (input_Scanner.hasNext()) {
    	      
    	    	final_string.append(input_Scanner.next());
    	    	whitespace_count+=1;
    	    }
	    	  total_threads--;
	          if (total_threads == 0) {
	              condition.signalAll();
	          }
    	    System.out.println("Total Char :"+final_string.toString().length());
    	    System.out.println("Total Whitespace :"+whitespace_count);
    	    return final_string.toString();
    } finally {
    	
    	lock.unlock();
    }
  
  }
  

}
