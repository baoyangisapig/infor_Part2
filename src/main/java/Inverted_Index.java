import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tool class provides the method to construct the inverted indexes based on the input files.
 */
public class Inverted_Index {

  public static Map<String, List<String>> resMap = new HashMap<String, List<String>>();// the map used to store the whole inverted indexes.
  static Set<String> stopWords = new HashSet<String>();// the map used to store all stop words.

  /**
   * parse the input file and construct the inverted indexes data structure.
   *
   * @param path the path of the file.
   * @throws IOException
   */
  public void upload(String path) throws IOException {
    if (stopWords.size() == 0) {
      FileInputStream inputStream = new FileInputStream("C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfc\\stopWords.txt");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        System.out.println(str);
        stopWords.add(str.trim());
      }
    }
    String curId = "";
    Map<String, List<String>> map = new HashMap<String, List<String>>();

    FileInputStream inputStream = new FileInputStream(path);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

    String str = null;
    //get and update the current file number
    while ((str = bufferedReader.readLine()) != null) {
      if (str.length() >= 2 && str.substring(0, 2).equals("RN")) {
        curId = str.substring(3);
      }
      String[] strs = str.split("\\s+");
      // add each word in the file into the inverted indexes/
      for (String cur : strs) {
        String[] curs = format(cur);
        for(String temp:curs){
          if (temp.equals("")) continue;
          if (stopWords.contains(temp)) continue;
          if (!map.containsKey(temp)) {
            map.put(temp, new ArrayList<String>());
          } else {
            if (!map.get(temp).contains(curId))
              map.get(temp).add(curId);
          }
        }
      }
    }
    inputStream.close();
    bufferedReader.close();
    resMap = Merge(map, resMap);
  }

  /**
   * The method is used to merge the inverted indexes of the current file to the whole inverted
   * indexes structure.
   *
   * @param map1 inverted indexes of the current file.
   * @param map2 the whole inverted indexes structure.
   * @return merged inverted indexes.
   */
  public static Map<String, List<String>> Merge(Map<String, List<String>> map1, Map<String, List<String>> map2) {
    Map<String, List<String>> newMap = new HashMap<String, List<String>>();
    for (String key : map1.keySet()) {
      List<String> temp = map1.get(key);
      if (map2.containsKey(key)) {
        temp.addAll(map2.get(key));
        map2.remove(key);
      }
      newMap.put(key, temp);
    }
    newMap.putAll(map2);
    return newMap;
  }

  /**
   * parse and formalize the token.
   *
   * @param token the token that needs to be formalized.
   * @return formalized token.
   */
  public static String[] format(String token) {
    String[] tokens=token.replaceAll("[^a-zA-Z]","  ").split("\\s+");
    for(int i=0;i<=tokens.length-1;i++){
      tokens[i]=tokens[i].toLowerCase();
    }
    return tokens;
  }

}
