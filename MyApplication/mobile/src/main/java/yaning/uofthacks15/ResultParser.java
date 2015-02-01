package yaning.uofthacks15;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ResultParser {
    public static String parse(String html) {
        try {
            Document doc = Jsoup.parse(html);
            Elements elems = doc.select("em"); // get bolded elements

            ArrayList<String> ret = new ArrayList<String>(); // to store element data
            ArrayList<Integer> freq = new ArrayList<Integer>();

            for (Iterator<Element> i = elems.iterator(); i.hasNext(); ) {
                Element e = i.next();
                ret.add(e.html().toLowerCase());


                System.out.println(e.html()); // debug
            }
            // get most frequent
            for (String s : ret)
                freq.add(Collections.frequency(ret, s));
            int max_ind = freq.indexOf(Collections.max(freq));

            return ret.get(max_ind);
        } catch (Exception e) {
            return "";
        }
    }

}
