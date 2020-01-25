import java.util.Iterator;
import com.google.common.collect.Iterables;
import java.io.Serializable;

public class PageLine implements Serializable {
    public String id;
    public Iterable<String> links;
    
    public PageLine(String id, Iterable<String> links) {
        this.id = id;
        this.links = links;
    }
    
    public String toString() {
        Iterator<String> it = this.links.iterator();
        String ret = id + ":";
        while (it.hasNext()) {
            String next = it.next();
            if (next.trim().length() > 0)
                ret += " " + next;
        }
        return ret;
    }
}
