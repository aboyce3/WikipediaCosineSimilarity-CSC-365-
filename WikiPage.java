import java.util.ArrayList;
import java.util.Date;

public class WikiPage {
    String URL;
    HashTable words;
    Double cosine;
    Date date;

    public WikiPage(String link) {
        this.URL = link;
        this.words = new HashTable(128);
        this.cosine = 0.0;
        date = null;
    }

    public void setTable(HashTable t) {
        this.words = t;
    }

    public Double cosineSimilarity(HashTable main, HashTable target){
        ArrayList<Integer> first = new ArrayList();
        ArrayList<Integer> second = new ArrayList();
        //If a word matches then add the occurrences to their respective list
        for(HashNode n : main.table){
            for(HashNode node = n; n != null; n = n.next){
                if(target.get(node.key) != null) {
                    first.add(node.occurences);
                    second.add(target.get(node.key).occurences);
                }
            }
        }

        Double dotProduct = 0.0;//multiply the value at i of both sets and add them to dotProduct
        Double magMain = 0.0;//square the value in the set, add to itself, then get the square root after iteration
        Double magTarget = 0.0;//square the value in the set, add to itself, then get the square root after iteration

        for (int i = 0; i < first.size()-1; i++){
            dotProduct += first.get(i) * second.get(i);
            magMain += Math.pow(first.get(i), 2);
            magTarget += Math.pow(second.get(i), 2);
        }
        magMain = Math.sqrt(magMain);
        magTarget = Math.sqrt(magTarget);

        //Formula for returning the similarity
        return (dotProduct) / (magMain * magTarget);
    }
}
