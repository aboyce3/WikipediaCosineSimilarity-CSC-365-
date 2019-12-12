import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;

public class WikiPage implements Serializable, Comparable<WikiPage>{
    String URL;
    HashTable words;
    Double cosine;
    BTree tree;
    transient Element self;
    boolean medioid;
    ArrayList<Edge> edges;
    private double distanceFromSource = Double.MAX_VALUE;
    private boolean visited = false;
    int flag = -1;

    public WikiPage(String link, HashTable t) throws Exception {
        this.URL = link;
        this.words = t;
        this.cosine = 0.0;
        medioid = false;
        tree = new BTree(link, words);
        self = null;
    }

    public WikiPage(String link) {
        this.URL = link;
        this.cosine = 0.0;
    }

    public void setTable(HashTable t) {
        this.words = t;
    }

    public Double cosineSimilarity(HashTable main, HashTable target) {
        ArrayList<Integer> first = new ArrayList();
        ArrayList<Integer> second = new ArrayList();

        //If a word matches then add the occurrences to their respective list
        for (Object i : main.hashes) {
            for (HashNode node = main.table[(int) i]; node != null; node = node.next) {
                if (target.get(node.key) != null) {
                    first.add(node.occurences);
                    second.add(target.get(node.key).occurences);
                }
            }
        }

        Double dotProduct = 0.0;//multiply the value at i of both sets and add them to dotProduct
        Double magMain = 0.0;//square the value in the set, add to itself, then get the square root after iteration
        Double magTarget = 0.0;//square the value in the set, add to itself, then get the square root after iteration

        for (int i = 0; i < first.size() - 1; i++) {
            dotProduct += first.get(i) * second.get(i);
            magMain += Math.pow(first.get(i), 2);
            magTarget += Math.pow(second.get(i), 2);
        }
        magMain = Math.sqrt(magMain);
        magTarget = Math.sqrt(magTarget);

        //Formula for returning the similarity
        return this.cosine =(dotProduct) / (magMain * magTarget);
    }

    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public int compareTo(WikiPage otherVertex) {
        return Double.compare(this.distanceFromSource, otherVertex.distanceFromSource);
    }
}
