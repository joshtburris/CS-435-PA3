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

public class PageRank implements Serializable, Comparable<PageRank> {
    
    public String id, title;
    public Double rank;
    
    public PageRank(String id, Double rank, String title) {
        this.id = id;
        this.rank = rank;
        this.title = title;
    }
    
    public String toString() {
        return id +"\t"+ title +"\t"+ rank;
    }
    
    public int compareTo(PageRank pr) {
        return rank.compareTo(pr.rank);
    }
    
}
