import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The class is for parsing the xml file.
 */
public class Parse {
  public static void main(String[] args) {
  }
  public static Map<String,String> parseXml(String path,Map<String,String> res){
    //1.创建DocumentBuilderFactory对象
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //2.创建DocumentBuilder对象
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document d = builder.parse(path);
      NodeList sList = d.getElementsByTagName("RECORD");
      //element(sList);
       node(sList,res);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  public static Map<String,String> node(NodeList list,Map<String,String> map){
    for (int i = 0; i <list.getLength() ; i++) {
      Node node = list.item(i);
      NodeList childNodes = node.getChildNodes();
      StringBuilder sb=new StringBuilder();
      String curNum="";
      for (int j = 0; j <childNodes.getLength() ; j++) {
        if (childNodes.item(j).getNodeType()==Node.ELEMENT_NODE) {
          String value=childNodes.item(j).getFirstChild().getNodeValue();
          if (value!=null) value=value.replaceAll("\n","");
          if(childNodes.item(j).getNodeName().equals("RECORDNUM")) {
            curNum = value;
          }
          if(childNodes.item(j).getNodeName().equals("MAJORSUBJ")||childNodes.item(j).getNodeName().equals("MINORSUBJ")) {
            NodeList nodeList=childNodes.item(j).getChildNodes();
            sb.append(readNodeList(nodeList)).append(" ");
          }
          else if(childNodes.item(j).getNodeName().equals("REFERENCES")){
            NodeList nodeList=childNodes.item(j).getChildNodes();
            sb.append(readParamter(nodeList)).append(" ");
          }
          else if(value!=null)
          sb.append(formatSequence(value)).append(" ");
        }
      }
      map.put(curNum,new String(sb));
    }
    return map;
  }

  private static String readNodeList(NodeList nodeList){
    StringBuilder sb=new StringBuilder();
    for(int i=0;i<=nodeList.getLength()-1;i++){
      if (nodeList.item(i).getNodeType()==Node.ELEMENT_NODE) {
        if(nodeList.item(i).getChildNodes().getLength()!=0)
        sb.append(formatSequence(nodeList.item(i).getFirstChild().getNodeValue())).append(" ");
      }

    }
    return new String(sb);
  }

  private static String readParamter(NodeList nodeList){
    StringBuilder sb=new StringBuilder();
    for(int i=0;i<=nodeList.getLength()-1;i++){
      if (nodeList.item(i).getNodeType()==Node.ELEMENT_NODE) {
          sb.append(formatSequence(nodeList.item(i).getAttributes().getNamedItem("author").getNodeValue())).append(" ");
      }
    }
    return new String(sb);
  }

  /**
   * formalize a string.
   * @param str
   * @return
   */
  public  static String formatSequence(String str){
    StringBuilder sb=new StringBuilder();
    for(String word:str.split("\\s+")){
      String[] values= Inverted_Index.format(word);
      for(String value:values){
      sb.append(value).append(" ");
      }
    }
    return new String(sb);
  }

}
