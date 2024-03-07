import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountChar {

  private static Scanner input;
  private static File file;

  public static void main(String[] args) {
    input = new Scanner(System.in);
    file = new File("input.txt");

    try {
      input = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }


}
