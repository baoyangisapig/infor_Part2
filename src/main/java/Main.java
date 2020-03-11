import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javafx.util.Pair;

public class Main {
  public static void main(String[] args) throws IOException {
    new BM25().initialize();
    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Enter query");
    String query = scanner.nextLine();
    Queue<Pair<String,Double>> queue=new BM25().selectTopK(query,20);
    List<Integer> list=new ArrayList<>();
    while (!queue.isEmpty()){
      list.add(Integer.valueOf(queue.poll().getKey()));
    }
    Collections.sort(list);
    System.out.println("TOP 20 record_id:"+list.toString());
  }
}
