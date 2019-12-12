import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;


public class Parser implements Serializable {
    String URL;
    transient Document document;
    transient Elements links;
    ArrayList<WikiPage> table;
    HashMap<WikiPage, ArrayList<Edge>> edges;
    int numberOfEdges = 0;

    public Parser(String s) throws Exception {
        File f = new File(System.getProperty("user.dir") + "/ParserOnDisk.ser");
        if (!f.exists()) {
            edges = new HashMap<>();
            table = new ArrayList();
            this.URL = s;
            this.document = Jsoup.connect(URL).get();
            links = document.select("a[href]");
            generateGraph();
            writeParserToDisk();
        }
    }

    public void resetVisited(){
        for(WikiPage p : table)
            p.setVisited(false);
    }

    public boolean hasURL(Element e) {
        for (WikiPage page : table)
            if (page.URL.equals(e.attr("abs:href"))) return true;
        return false;
    }

    public WikiPage page(Element e) {
        for (WikiPage page : table) {
            if (page.URL.equals(e.attr("abs:href"))) return page;
        }
        return null;
    }

    public boolean exists(WikiPage p, Element e){
        for(int i = 0; i < edges.get(p).size(); i++){
            if(edges.get(p).get(i).dest == page(e)){
                return true;
            }
        }
        return false;
    }

    public void link() throws Exception {
        for (WikiPage p : table) {
            if (p.self != null) {
                Document doc = Jsoup.connect(p.self.attr("abs:href")).get();
                Elements elements = doc.select("a[href]");
                elements.stream()
                        .distinct()
                        .forEach(e -> {
                            if (hasURL(e) && !exists(p,e) && page(e) != p){
                                edges.get(p).add(new Edge(p, page(e)));
                            }
                        });
            }
        }
        for(WikiPage p : edges.keySet()) {
            numberOfEdges += edges.get(p).size();
            p.edges = edges.get(p);
        }
    }

    public void generateGraph() throws Exception {
        WikiPage page = parse(Jsoup.connect(this.URL), this.URL);
        edges.put(page, new ArrayList<>());
        this.links.stream()
                .filter(e ->
                        e.attr("abs:href").contains("en.wikipedia.org/wiki/") &&
                                !e.attr("abs:href").contains("#") &&
                                !e.attr("abs:href").substring(30).contains("/") &&
                                !e.attr("abs:href").substring(30).contains(":") &&
                                !e.attr("abs:href").contains("Main_Page") &&
                                !e.attr("abs:href").contains("%") &&
                                !hasURL(e) && e != null)
                .limit(46)
                .distinct()
                .forEach(e -> {
                            try {
                                WikiPage p = parse(Jsoup.connect(e.attr("abs:href")), e.attr("abs:href"));
                                p.self = e;
                                edges.get(page).add(new Edge(page, p));
                                edges.put(p, new ArrayList<>());
                                helper(p, e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
        link();
    }

    public void helper(WikiPage source, Element element) throws Exception {
        Document doc = Jsoup.connect(element.attr("abs:href")).get();
        Elements elements = doc.select("a[href]");
        elements.stream().filter(e ->
                e.attr("abs:href").contains("en.wikipedia.org/wiki/") &&
                        !e.attr("abs:href").contains("#") &&
                        !e.attr("abs:href").substring(30).contains("/") &&
                        !e.attr("abs:href").substring(30).contains(":") &&
                        !e.attr("abs:href").contains("Main_Page") &&
                        !e.attr("abs:href").contains("%") &&
                        !hasURL(e) && e != null)
                .distinct()
                .limit(10)
                .forEach(e -> {
                            try {
                                WikiPage p = parse(Jsoup.connect(e.attr("abs:href")), e.attr("abs:href"));
                                edges.get(source).add(new Edge(source, p));
                                edges.put(p, new ArrayList<>());
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
    }

    //Parses the web page and adds to an ArrayList of web pages
    private WikiPage parse(Connection website, String s) throws Exception {
        HashTable temp = new HashTable(8);
        Document doc = Jsoup.parse(website.get().html());
        String page = doc.text().toLowerCase();
        String[] arr = page.split("\\W+");
        Arrays.sort(arr);
        Arrays.stream(arr).filter(e -> !e.equals("the")).forEach(e -> temp.add(e, e));
        WikiPage webPage = new WikiPage(s, temp);
        table.add(webPage);
        return webPage;
    }

    public WikiPage parseInput(Connection website) {
        try {
            HashTable temp = new HashTable(8);
            Document doc = Jsoup.parse(website.get().html());
            String page = doc.text().toLowerCase();
            String[] arr = page.split("\\W+");
            Arrays.sort(arr);
            Arrays.stream(arr).filter(e -> !e.equals("the")).forEach(e -> temp.add(e, e));
            WikiPage webPage = new WikiPage(doc.attr("abs:href"));
            webPage.setTable(temp);
            return webPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeParserToDisk() throws Exception {
        FileOutputStream f = new FileOutputStream(new File(System.getProperty("user.dir") + "/ParserOnDisk.ser"));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(this);
        o.close();
        f.close();
    }

}

