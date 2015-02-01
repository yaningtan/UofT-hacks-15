package yaning.uofthacks15;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ImageParser {
    public static String parse(String html) {
        Document doc = Jsoup.parse(html);
        Elements elems = doc.select("img");
        String url = elems.first().attr("src");

        return url;
    }

}
