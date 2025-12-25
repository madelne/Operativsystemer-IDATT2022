
import java.util.HashMap;

public class DoubleHashingHashTable {

  class HashNode {
    public String data;

    public HashNode(String data) {
      this.data = data;
    }
  }

  private HashNode nodes[];
  private int length;
  private int collisions;

  public DoubleHashingHashTable(int length) {
    // Hashtables size with maximum loadfactor (into 35% bigger)
    this.nodes = new HashNode[(int) (length * 1.35)];
    this.length = length;
    this.collisions = 0;
  }

  // Primary hashfunction: based on the string
  public int hash1(String s) {
    int hash = 0;
    for (int i = s.length(); i-- > 0;) {
      hash = (7 * hash + s.charAt(i)) % nodes.length;
    }
    return hash;
  }
  
  // secondary hashfunction for double hashing
  public int hash2(String s) {
    int hash = 0;
    for (int i = s.length(); i-- > 0;) {
      hash = (31 * hash + s.charAt(i)) % (nodes.length - 1);
    }
    return (hash % (nodes.length - 1)) + 1; //steplength must be minimum 1
  }

  //insert method for double hashing
  public void insert(String s) {
    int h1 = hash1(s);
    int h2 = hash2(s);

    int index = h1 % nodes.length;
    int i = 0;

    //search after one free place with double hashing
    while (nodes[index] != null) {
      collisions++;
      i++;
      index = (index + h2) % nodes.length;

      // to avoid a forever loop, we check if the table is full
      if (i == nodes.length) {
        System.out.println("The table is full.");
        return;
      }

      index = (index + nodes.length) % nodes.length;
    }
    nodes[index] = new HashNode(s);
  }

  public HashNode find(String s) {
    int h1 = hash1(s);
    int h2 = hash2(s);

    int index = h1;
    int i = 0;

    // search for element with double hashing
    while (nodes[index] != null && !nodes[index].data.equals(s)) {
      i++;
      index = (h1 + i * h2) % nodes.length;

      // if we have searched through the whole table
      if (i == nodes.length) {
        break;
      }
    }


    if (nodes[index] != null && nodes[index].data.equals(s)) {
      return nodes[index];
    } else {
      System.out.println("String not found");
      return null;
    }
  }

  public void printStatus() {
    int people = 0;
    for (HashNode node : nodes) {
      if (node != null) {
        people++;
      }
    }
    System.out.println();
    System.out.println("nr of collisions: " + collisions);
    System.out.println("Load factor: " + (people > 0 ? (double) people / length : 0));
    System.out.println("average collisions per person: " + (people > 0 ? ((double) collisions / people) : 0));
  }

  public static void main(String[] args) {
    DoubleHashingHashTable ht = new DoubleHashingHashTable(13499999);
    HashMap<Integer, String> hm = new HashMap<>();String[] randomList = new String[10000000];
    // generate random strings for testing
    for (int i = 0; i < randomList.length; i++) {
      String randomString = "String" + i;
      randomList[i] = randomString;
    }
    Long timeStart1 = System.nanoTime();
    for (int i = 0; i < randomList.length; i++) {
      hm.put(i, randomList[i]);
    }
    Long timeEnd1 = System.nanoTime();

    Long timeStart = System.nanoTime();
    //insert into hashtable
    for (int i = 0; i < randomList.length; i++) {
      ht.insert(randomList[i]);
    }
    Long timeEnd = System.nanoTime();


    System.out.println("Time elapsed Double hashing Hashtable: " + (timeEnd - timeStart)/1000000 + " ms");
    System.out.println("Time elapsed Hashmap: " + (timeEnd1 - timeStart1)/1000000 + " ms");

    ht.printStatus();

    // Test for finding elements
    HashNode result = ht.find("String5000000");
    if (result != null) {
      System.out.println("Funnet element: " + result.data);
    }
  }
}
