import java.io.Serializable;
import java.util.Arrays;

public class BTree implements Serializable {

    static final int t = 100;
    static final int MAXNODES = (2 * t) - 1;
    Leaf head;
    String webpage;
    HashTable nodes;

    public BTree(String webpage, HashTable t) throws Exception {
        head = new Leaf();
        this.webpage = webpage.substring(30);
        nodes = t;
    }

    public void add(HashNode k) {
        Leaf r = head;
        if (r.count == MAXNODES) {
            Leaf s = new Leaf();
            head = s;
            s.leaf = false;
            s.subs[0] = r;
            split(s, 0, r);
            addNonFull(s, k);
        } else addNonFull(r, k);
    }

    public HashNode search(String k) {
        int i = 0;
        while (i <= head.count && head.nodes[i].key.compareTo(k) < 0) {
            i++;
        }
        if (i <= head.count && k == head.nodes[i].key) {
            return head.nodes[i];
        }
        if (head.leaf) {
            return null;
        }
        return search(head.subs[i], k);
    }

    public HashNode search(Leaf l, String k) {
        int i = 0;
        while (i <= l.count && l.nodes[i].key.compareTo(k) < 0) {
            i++;
        }
        if (i <= l.count && k == l.nodes[i].key) {
            return l.nodes[i];
        }
        if (l.leaf) {
            return null;
        }
        return search(l.subs[i], k);
    }

    private void addNonFull(Leaf x, HashNode k) {
        int i = x.count;
        if (x.leaf) {
            x.nodes[MAXNODES - 1] = k;
            Arrays.sort(x.nodes, (o1, o2) -> {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return o1.key.compareTo(o2.key);
            });
            x.count++;
        } else {
            while (i >= 0 && k.compareTo(x.nodes[i]) < 0) i--;
            i++;
            if (x.subs[i].isFull()) {
                split(x, i, x.subs[i]);
                if (k.compareTo(x.nodes[i]) > 0) {
                    i++;
                }
            }
            addNonFull(x.subs[i], k);
        }
    }

    private void split(Leaf x, int index, Leaf y) {
        Leaf z = new Leaf();
        z.leaf = y.leaf;
        z.count = (MAXNODES / 2);

        for (int i = 0; i < t - 1; i++)
            z.nodes[i] = y.nodes[i + t];

        if (!y.leaf)
            for (int i = 0; i <= (MAXNODES / 2); i++)
                z.subs[i] = y.subs[i + (y.nodes.length / 2)];

        y.count = t - 1;

        for (int i = x.count + 1; i >= index + 1; i--)
            x.subs[i + 1] = x.subs[i];

        x.subs[index + 1] = z;

        for (int i = x.count; i >= index; i--)
            x.nodes[i + 1] = x.nodes[i];

        x.nodes[index] = y.nodes[y.nodes.length / 2];
        for (int j = y.count; j < MAXNODES; j++) {
            y.nodes[j] = null;
        }
        x.count++;
    }

    class Leaf implements Serializable {
        HashNode[] nodes;
        Leaf[] subs;
        int count;
        boolean leaf;

        public Leaf() {
            nodes = new HashNode[MAXNODES];
            subs = new Leaf[MAXNODES + 1];
            leaf = true;
            count = 0;
        }

        public boolean isFull() {
            return count == nodes.length;
        }
    }
}
