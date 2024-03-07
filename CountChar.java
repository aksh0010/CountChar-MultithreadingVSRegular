import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountChar {

  private static Scanner input_Scanner= new Scanner(System.in);
  private static File file= new File("input.txt");
  private static int whitespace_count=0;
  
  
  
  public static void main(String[] args) {
	  
	System.out.println("Main");
   
    
    try {
      input_Scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    getString();


  }

  public static String getString() {
	System.out.println("getstring method");
    StringBuilder final_string = new StringBuilder();
    
    while (input_Scanner.hasNext()) {
      
    	final_string.append(input_Scanner.next());
    	whitespace_count+=1;
    }
    
    System.out.println("Total Char :"+final_string.toString().length());
    System.out.println("Total Whitespace :"+whitespace_count);
    return final_string.toString();
  }
  

}
