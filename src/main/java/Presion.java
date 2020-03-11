import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javafx.util.Pair;

/**
 * Caculate the precision and recall after using BM25 algorithm.
 */
public class Presion {
  public static void main(String[] args) throws IOException {

  }
  public static void caculate( Map<String,Double> res_precision, Map<String,Double> res_recall) throws IOException {
    Map<String,List<Integer>> map=new HashMap<>();
    Map<String,String> queryMap=new HashMap<>();
    parseXml("C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfx\\cfquery.xml",map,queryMap);
    new BM25().initialize();

    for(String key:map.keySet()){
      String s=queryMap.get(key);
      Queue<Pair<String,Double>> queue=new BM25().selectTopK(queryMap.get(key),10);
      int cur=0;
      int size=queue.size();
      while (!queue.isEmpty()){
        if(map.get(key).contains(Integer.valueOf(queue.poll().getKey()))){
          cur++;
        }
      }
      res_precision.put(key,(double)cur/size);
      res_recall.put(key,(double)cur/map.get(key).size());
    }
  }

  public static Map<String,Double> caculateRecall() throws IOException {
    Map<String,Double> res=new HashMap<>();
    Map<String,List<Integer>> map=new HashMap<>();
    Map<String,String> queryMap=new HashMap<>();
    parseXml("C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfx\\cfquery.xml",map,queryMap);
    new BM25().initialize();

    for(String key:map.keySet()){
      Queue<Pair<String,Double>> queue=new BM25().selectTopK(queryMap.get(key),10);
      int cur=0;
      int size=queue.size();
      while (!queue.isEmpty()){
        if(map.get(key).contains(Integer.valueOf(queue.poll().getKey()))){
          cur++;
        }
      }
      res.put(key,(double)cur/map.get(key).size());
    }
    return res;
  }

  public static Map<String, List<Integer>> parseXml(String path, Map<String, List<Integer>> res,Map<String,String> queryMap ) {
    //1.创建DocumentBuilderFactory对象
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //2.创建DocumentBuilder对象
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document d = builder.parse(path);
      NodeList sList = d.getElementsByTagName("QUERY");
      //element(sList);
      node(sList, res,queryMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  public static void node(NodeList list,Map<String,List<Integer>> map,Map<String,String> queryMap){
    for (int i = 0; i <list.getLength() ; i++) {
      Node node = list.item(i);
      NodeList childNodes = node.getChildNodes();
      StringBuilder sb=new StringBuilder();
      String curNum="";
      for (int j = 0; j <childNodes.getLength() ; j++) {
        if (childNodes.item(j).getNodeType()==Node.ELEMENT_NODE) {
          String value=childNodes.item(j).getFirstChild().getNodeValue();
          if (value!=null) value=value.replaceAll("\n","");
          if(childNodes.item(j).getNodeName().equals("QueryNumber")) {
            curNum = value;
          }
          if(childNodes.item(j).getNodeName().equals("Records")) {
            NodeList nodeList=childNodes.item(j).getChildNodes();
            map.put(curNum,readNodeList(nodeList));
          }
          if (childNodes.item(j).getNodeName().equals("QueryText")){
            queryMap.put(curNum,value);
          }
        }
      }

    }
  }

  private static List<Integer> readNodeList(NodeList nodeList){
    List<Integer> res=new ArrayList<>();
    for(int i=0;i<=nodeList.getLength()-1;i++){
      if (nodeList.item(i).getNodeType()==Node.ELEMENT_NODE) {
        if(nodeList.item(i).getChildNodes().getLength()!=0)
          res.add(Integer.parseInt(nodeList.item(i).getFirstChild().getNodeValue()));
      }

    }
    return res;
  }
}




