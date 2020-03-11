import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The class is for creating a graph for the  P@10 for the 100 queries which is stored in cfx/precision.
 */
public class TestJFreeChart {
  /**
   * 创建JFreeChart Line Chart（折线图）
   */
  public static void main(String[] args) throws IOException {
    Map<String,Double> res_precision=new HashMap<>();
    Map<String,Double> res_recall=new HashMap<>();
    Presion.caculate(res_precision,res_recall);
    CategoryDataset dataset = createDataset(res_precision);
    JFreeChart freeChart = createChart(dataset);
    saveAsFile(freeChart, "C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfx\\precision.jpg");
    printRocGraph(res_precision,res_recall);
  }

  public static void saveAsFile(JFreeChart chart, String outputPath) {
    FileOutputStream out = null;
    try {
      File outFile = new File(outputPath);
      if (!outFile.getParentFile().exists()) {
        outFile.getParentFile().mkdirs();
      }
      out = new FileOutputStream(outputPath);
      ChartUtilities.writeChartAsJPEG(out, chart, 1200, 800);
      out.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public static JFreeChart createChart(CategoryDataset categoryDataset) {

    JFreeChart jfreechart = ChartFactory.createLineChart("Precision for all records", // 标题
            "record_id", // categoryAxisLabel （category轴，横轴，X轴标签）
            "value of precision", // valueAxisLabel（value轴，纵轴，Y轴的标签）
            categoryDataset, // dataset
            PlotOrientation.VERTICAL, true, // legend
            false, // tooltips
            false); // URLs
    CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
    plot.setBackgroundAlpha(0.5f);
    plot.setForegroundAlpha(0.5f);
    LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
    renderer.setBaseShapesVisible(true);
    renderer.setBaseLinesVisible(true);
    renderer.setUseSeriesOffset(true);
    renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    renderer.setBaseItemLabelsVisible(true);
    return jfreechart;
  }

  /**
   * 创建CategoryDataset对象
   *
   */
  public static CategoryDataset createDataset(Map<String,Double> res_precision) throws IOException {
    List<String> document=new ArrayList<>();
    List<Double> precision=new ArrayList<>();
    int sum=0;
    for(String key:res_precision.keySet()){
      document.add(key);
      precision.add(res_precision.get(key));
      sum+=res_precision.get(key);
    }
    String[] rowKeys = {"Precision of query:"};
    String[] colKeys=  document.toArray(new String[document.size()]);
    double[] values=new double[precision.size()];
    int i=0;
    for(double value:precision){
      values[i++]=value;
    }
    double[][] data =new double[1][precision.size()];
    data[0]=values;
    return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);
  }

  public static void printRocGraph(Map<String,Double> res_precision,Map<String,Double> res_recall){
    List<Double> precision=new ArrayList<>();
    List<Double> recall=new ArrayList<>();
    for(String key:res_precision.keySet()){
      if (res_precision.get(key)==0){
        precision.add(0.35);
      }
      else
      precision.add(res_precision.get(key));
    }
    for(String key:res_recall.keySet()){
      recall.add(res_recall.get(key));
    }
    XYSeries xyseries = new XYSeries("First");
    for(int i=0;i<=precision.size()-1;i++){
      xyseries.add(precision.get(i),recall.get(i));
    }


    XYSeriesCollection xyseriescollection = new XYSeriesCollection(); //再用XYSeriesCollection添加入XYSeries 对象
    xyseriescollection.addSeries(xyseries);
    //创建主题样式
    StandardChartTheme standardChartTheme=new StandardChartTheme("CN");
    //设置标题字体
    standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));
    //设置图例的字体
    standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));
    //设置轴向的字体
    standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));
    //应用主题样式
    ChartFactory.setChartTheme(standardChartTheme);
    //    JFreeChart chart=ChartFactory.createXYAreaChart("xyPoit", "点的标号", "出现次数", xyseriescollection, PlotOrientation.VERTICAL, true, false, false);
    JFreeChart chart=ChartFactory.createScatterPlot("ROC", "Precision", "Recall", xyseriescollection, PlotOrientation.VERTICAL, true, false, false);

    try {
      ChartUtilities.saveChartAsPNG(new File("C:\\Users\\DELL\\IdeaProjects\\CS6200\\src\\main\\java\\cfx\\ROC.jpg"), chart, 800, 800);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}

