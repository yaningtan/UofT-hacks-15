package yaning.uofthacks15;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

public class ResultParser {
    public static ArrayList<String> parse(String html) {
        Document doc = Jsoup.parse(html);
        Elements elems = doc.select("em"); // get bolded elements

        ArrayList<String> ret = new ArrayList<String>(); // to store element data

        for (Iterator<Element> i = elems.iterator(); i.hasNext();){
            Element e = i.next();
            ret.add(e.data());
            System.out.println(e.data());
        }


        return ret;
    }

}
