import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javafx.util.Pair;

/**
 * BM25 is a algorithm to rank the relative documents.
 */
public class BM25 {
   final double k1=1.4,b=1.8;
   final static List<String> fileNames= Arrays.asList("cf74.xml","cf75.xml","cf76.xml","cf77.xml","cf78.xml","cf79.xml");
   static int averageLength = 0;
   public static Map<String,String> files=new Hashtable<String, String>();
   public static Map<String,Map<String,Integer>> wordFreq=new HashMap<String, Map<String, Integer>>();

  public static void main(String[] args) throws IOException {
    new BM25().initialize();
    Queue<Pair<String,Double>> queue=new BM25().selectTopK("Is CF mucus abnormal",20);
    while(!queue.isEmpty()){
      System.out.println(queue.poll().getKey());
    }
  }

  /**
   * Select top K pages within all the documents.
   * @param query  query String
   * @param k number of selected pages
   * @return
   */
    public Queue<Pair<String,Double>> selectTopK(String query,int k){
      Queue<Pair<String,Double>> queue=new PriorityQueue<Pair<String, Double>>((a,b)-> (int) (a.getValue()-b.getValue()));
      Map<String,Double> weight=getWeight(query);
      for(String recordId:files.keySet()){
        queue.offer(new Pair<>(recordId,scoreOfDocument(weight,recordId)));
        if(queue.size()>k){
          queue.poll();
        }
      }
      return queue;
    }

  /**
   * construct the word frequency foor all the documents.
   * @throws IOException
   */
  public void initialize() throws IOException {
    getWordFreq();
    averageLength=aveLength();
    }

  /**
   * caculate the score for a specific document.
   * @param weights  weights for all the words in a query.
   * @param recordId
   * @return
   */
   public double scoreOfDocument(Map<String,Double> weights,String recordId){
     double score=0;
     for(String key:weights.keySet()){
       score+=weights.get(key)*Rqd(key,recordId,aveLength());
     }
     return score;
   }

  /**
   * weight for each word in a query
   * @param query string
   * @return
   */
   public Map<String,Double> getWeight(String query){
     Map<String,Double> weights=new HashMap<String, Double>();
     Set<String> set=new HashSet<String>();
     String[] strs=query.replaceAll("[^a-zA-Z]","  ").split("\\s+");
     for(String str:strs){
      set.add(str);
     }
     for(String key:set){
       weights.put(key,Math.log10((files.size()-appearTimes(key)+0.5)/(appearTimes(key)+0.5)));
     }
     return weights;
   }

  /**
   * a component in the BM25 algorithm
   * @param word
   * @param recordId
   * @param averageLength
   * @return
   */
   public double Rqd(String word,String recordId,int averageLength){
     double K= k1*(1-b+b*files.get(recordId).length()/averageLength);
     double fi=wordFreq.get(recordId).getOrDefault(word,0);
     return fi*(k1+1)/(fi+K);
   }

   private int aveLength(){
     int total=0;
     for(String key:files.keySet()){
       total+=files.get(key).length();
     }
     return total/files.keySet().size();
   }

  /**
   * the appear times of a word
   * @param word
   * @return
   */
   public int appearTimes(String word){
     int res=0;
     for(String key:wordFreq.keySet()){
       if(wordFreq.get(key).containsKey(word)){
         res++;
       }
     }
     return res;
   }

  /**
   * construct the word frequency structire.
   * @throws IOException
   */
  public static void getWordFreq() throws IOException {
    for(String path:fileNames){
      Parse.parseXml("C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfx\\"+path,files);
    }
    for(String key:files.keySet()){
      wordFreq.put(key,new HashMap<String, Integer>());
      Map<String,Integer> cur=wordFreq.get(key);
      String[] words=files.get(key).split("\\s+");
      for(String word:words){
        if(!cur.containsKey(word)){
          cur.put(word,1);
        }
        else{
          cur.put(word,cur.get(word)+1);
        }
      }
    }
  }

}
