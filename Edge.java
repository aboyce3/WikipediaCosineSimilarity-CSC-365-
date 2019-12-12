import java.io.Serializable;

public class Edge implements Serializable {
    WikiPage src, dest;
    double weight;

    public Edge(WikiPage s, WikiPage d) {
        src = s;
        dest = d;
        weight = 1 - s.cosineSimilarity(s.words, d.words); //Weight is determined by cosine similarity
    }

}