import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountChar {

  private static Scanner input_Scanner;
  private static File file;

  public static void main(String[] args) {
    input_Scanner = new Scanner(System.in);
    file = new File("input.txt");
    
    try {
      input_Scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // System.out.println(getString());


  }

  public static String getString() {
    StringBuilder Final_string = new StringBuilder();
    String each_line;
    while ((each_line = input_Scanner.nextLine()) != null) {
      Final_string.append(each_line);
    }

    return Final_string.toString();
  }
}
