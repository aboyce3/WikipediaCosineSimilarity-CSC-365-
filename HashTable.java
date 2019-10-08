import java.util.*;
class HashNode<V> {
    String key;
    V value;
    int occurences;
    HashNode next;


    public HashNode(String k, V v, HashNode next) {
        this.key = k;
        this.value = v;
        this.occurences = 1;
        this.next = next;
    }

    public HashNode(String k, V v) {
        this.key = k;
        this.value = v;
        this.occurences = 1;
        this.next = null;
    }

    //checks if an element exists with the variable 's' as a key
    public Boolean exists(String s) {
        for (HashNode temp = this; temp != null; temp = temp.next) {
            if (temp.key.equals(s)) return true;
        }
        return false;
    }
}

@SuppressWarnings("unchecked")
public class HashTable<V> {
    public HashNode<V>[] table;
    public int count;
    public HashSet<Integer> hashes;

    public HashTable(int i) {
        table = new HashNode[i];
        count = 0;
        hashes = new HashSet<>();
    }

    //adds elements in the hashtable
    public void add(String k, V v) {
        int hash = k.hashCode() & (table.length - 1);
        if (count > ((2 * (table.length - 1)) / 3)) {
            resize();
        }
        if (table[hash] == null) {
            table[hash] = new HashNode(k, v);
            hashes.add(hash);
            count++;
        } else if (table[hash].exists(k)) {
            HashNode t = this.get(k);
            t.occurences++;
        } else {
            HashNode t = new HashNode(k, v, table[hash]);
            table[hash] = t;
            count++;
        }
    }

    //gets an element from the table and returns null if it doesn't exist
    public HashNode<V> get(String k) {
        int hash = k.hashCode() & (table.length - 1);
        if (table[hash] == null) return null;
        else if (table[hash].key.equals(k)) return table[hash];
        else {
            HashNode temp = table[hash];
            while (temp != null) {
                if (temp.key.equals(k)) {
                    return temp;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    //here but not used other than for testing
    public void printInOrder() {
        for (Integer i : hashes) {
            for (HashNode n = table[i]; n != null; n = n.next) {
                System.out.println(n.key + ":" + n.occurences + "\n");
            }
        }
    }

    //resizes the hastable by 2x its length
    private void resize() {
        HashTable temp = new HashTable(table.length * 2);
        for (Integer i : hashes) {
            for (HashNode n = table[i]; n != null; n = n.next) {
                temp.add(n.key, n.value);
                temp.get(n.key).occurences++;
            }
        }
        table = temp.table;
    }

}
