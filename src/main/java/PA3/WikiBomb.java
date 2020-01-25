import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import scala.Tuple2;
import com.google.common.collect.Iterables;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.PairFunction;
import java.io.Serializable;

public class WikiBomb {
    
    private static class Sum implements Function2<Double, Double, Double> {
        public Double call(Double a, Double b) {
            return a + b;
        }
    }
    
    public static void main(String[] args) {
    
        String linksFilename = "/PA3/links-simple-sorted.txt";
        String titlesFilename = "/PA3/titles-sorted.txt";
        String rockyLineDocId = "4290745";
        
        SparkSession spark = SparkSession.builder().appName("Surf links").getOrCreate();
        //JavaRDD<String> lines = spark.read().textFile(linksFilename).javaRDD();
        
        JavaRDD<String> linkLines = spark.read().textFile(linksFilename).javaRDD();
        JavaPairRDD<String, String> titles = spark.read().textFile(titlesFilename)
            .javaRDD().zipWithIndex().mapToPair(x -> 
                new Tuple2<>(new Long(x._2()+1).toString(), x._1().toString())
        );
    
        JavaPairRDD<String, String> surfTitles = titles.filter(
            new Function<Tuple2<String, String>, Boolean>() {
                public Boolean call(Tuple2<String, String> t) {
                    return t._2().toLowerCase().contains("surfing");
                }
            }
        );
        
        JavaRDD<String> surfIds = surfTitles.keys();
        JavaPairRDD<String, Iterable<String>> surfLinks = surfIds.mapToPair(s -> {
            ArrayList<String> tmp = new ArrayList<>();
            return new Tuple2<String, Iterable<String>>(s, tmp);
        });
        
        JavaPairRDD<String, Iterable<String>> links = linkLines.mapToPair(s -> {
            String[] data = s.split(":");
            return new Tuple2<>(data[0], Arrays.asList(data[1].split("\\s")));
        });
        
        surfLinks = surfLinks.join(links).mapValues((Function<Tuple2<Iterable<String>, Iterable<String>>, Iterable<String>>) r -> {
            return r._2();
        });
        
        //surfLinks.mapValues(s -> { return s._2() + rockyLineDocId; });
        
        surfIds.coalesce(1).saveAsTextFile("/PA3/surfIds");
        
        /*long numPages = 5716808;
        
        JavaPairRDD<String, Double> ranks = links.mapValues(rs -> 1.0 / numPages);
    
        for (int i = 0; i < 25; ++i) {
            JavaPairRDD<String, Double> tempRanks = links.join(ranks).values().flatMapToPair(
                    s -> {
                        int urlCount = Iterables.size(s._1());
                        List<Tuple2<String, Double>> results = new ArrayList<>();
                        for (String url : s._1) {
                            if (!url.isEmpty())
                                results.add(new Tuple2<>(url, (double)s._2() / urlCount));
                        }
                        return results.iterator();
                    });
            ranks = tempRanks.reduceByKey(new Sum());
        }
        
        class MapToRank implements Function<Tuple2<String, Tuple2<Double, String>>, PageRank> {
            public PageRank call(Tuple2<String, Tuple2<Double, String>> tup) {
                return new PageRank(tup._1(), tup._2()._1(), tup._2()._2());
            }
        };
        
        ranks.join(titles).map(new MapToRank()).sortBy(new Function<PageRank, Double>() {
            public Double call(PageRank pr) {
                return pr.rank;
            }
        }, false, 1).saveAsTextFile("/PA3/results_bomb");*/
    
        spark.stop();
    
    }
  
}
