import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TestInvertedIndex {
  static final String[] paths=new String[]{"cf74","cf75","cf76","cf77","cf78","cf79"};
  public static void main(String[] args) throws IOException {
    Inverted_Index instance=new Inverted_Index();
    for(String path:paths){
      String curPath="C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfc\\"+path;
      instance.upload(curPath);
    }
    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Enter query");
    String query = scanner.nextLine();
    List<String> list=Inverted_Index.resMap.get(query);
    Collections.sort(list);
    System.out.println(list.toString());
  }
}
