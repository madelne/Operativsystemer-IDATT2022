
import java.io.*;

class HashTabell{
  class HashNode{
    public HashNode next;
    public String data;

    public HashNode(String data, HashNode next){
      this.data = data;
      this.next = next;
    }
    public HashNode(){}

  }
  private HashNode nodes[];
  private int length;
  private int collisions;

  public HashTabell(int length){
    this.nodes = new HashNode[length/2];
    this.length = length/2;
    this.collisions = 0;
  }
  public int hash(String s) {
    int hash = 0;
    for (int i = s.length(); i-- > 0;) {
      hash = (7 * hash + s.charAt(i)) % nodes.length;
    }
    return hash;
  }


  public void insert(String s){
    String output = "";
    int h = hash(s);
    if (nodes[h] == null) {
      nodes[h] = new HashNode(s, null);
    } else {
      output += nodes[h].data + "-->";
      nodes[h] = new HashNode(s, nodes[h]);
      output += nodes[h].data;
      collisions++;
      System.out.println(output);
    }
  }

  public HashNode find(String s) {
    String output = "";
    int h = hash(s);
    HashNode temp = nodes[h];


    if (temp == null) {
      System.out.println("String not found");
      return null;
    }

    // Append the first node's data
    output += temp.data;

    // Traverse the linked list
    while (!temp.data.equals(s)) {
      temp = temp.next;

      // If we reach the end without finding the string
      if (temp == null) {
        System.out.println(output + " --> null");
        System.out.println("String not found");
        return null;
      }

      // Append the next node's data to the output
      output += "-->" + temp.data;
    }

    // Print the traversal path
    System.out.println(output);

    // Return the node containing the string
    return temp;
  }

  public void print(){
    HashNode temp;
    int people = 0;
    for(HashNode n : nodes){
      if(n != null){
        temp = n;
        people++;
        while(temp.next != null) {
          temp = temp.next;
          people++;
        }

      }
    }
    System.out.println();
    System.out.println("nr of collisions: "+ collisions);
    System.out.println("Load factor: " + (double)people/length);
    System.out.println("average collisions pr person: "+((double)collisions/(double)people));

  }

  public static void main(String[] args) {
    HashTabell ht = new HashTabell(227);
    try {
      BufferedReader b = new BufferedReader(new FileReader(new File("navn.txt")));
      String text;
      while((text = b.readLine() )!= null){
        ht.insert(text);
      }

    } catch (IOException e){
      e.printStackTrace();
    }
    ht.print();
    System.out.println(ht.find("Nathalie Graidy Andreassen").data);

  }
}
