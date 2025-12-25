package org.example;

import java.io.*;
import java.util.*;
import org.example.Node;

public class Graph {
  static Graph graph;
  Node[] nodes;
  int N;
  int E;
  PriorityQueue<Node> queue;
  Set<Node> visited;
  ArrayList<ArrayList<Integer>> landmarkDistancesAll;

  public Graph(BufferedReader readNodes, BufferedReader readEdges, BufferedReader readInterestPoints) throws Exception {
    landmarkDistancesAll = new ArrayList<ArrayList<Integer>>();
    readNodes(readNodes);
    readEdges(readEdges);
    readInterestPoints(readInterestPoints);
  }

  private void readNodes(BufferedReader reader) throws Exception {
    StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
    N = Integer.parseInt(tokenizer.nextToken());
    nodes = new Node[N];
    for (int i = 0; i < N; i++) {
      tokenizer = new StringTokenizer(reader.readLine());
      int id = Integer.parseInt(tokenizer.nextToken());
      double latitude = Double.parseDouble(tokenizer.nextToken());
      double longitude = Double.parseDouble(tokenizer.nextToken());
      nodes[i] = new Node(id, latitude, longitude);
    }
  }

  private void readEdges(BufferedReader reader) throws Exception {
    StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
    E = Integer.parseInt(tokenizer.nextToken());
    for (int i = 0; i < E; i++) {
      tokenizer = new StringTokenizer(reader.readLine());
      int from = Integer.parseInt(tokenizer.nextToken());
      int to = Integer.parseInt(tokenizer.nextToken());
      int weight = Integer.parseInt(tokenizer.nextToken());
      double distance = Double.parseDouble(tokenizer.nextToken());
      double speedLimit = Double.parseDouble(tokenizer.nextToken());
      nodes[from].edges.add(new Edge(nodes[from], nodes[to], weight, distance, speedLimit));
    }
  }

  public void readInverseEdges(BufferedReader reader) throws Exception {
    StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
    E = Integer.parseInt(tokenizer.nextToken());
    for (int i = 0; i < E; i++) {
      tokenizer = new StringTokenizer(reader.readLine());
      int from = Integer.parseInt(tokenizer.nextToken());
      int to = Integer.parseInt(tokenizer.nextToken());
      int weight = Integer.parseInt(tokenizer.nextToken());
      double distance = Double.parseDouble(tokenizer.nextToken());
      double speedLimit = Double.parseDouble(tokenizer.nextToken());
      nodes[to].edges.add(new Edge(nodes[to], nodes[from], weight, distance, speedLimit));
    }
  }

  private void readInterestPoints(BufferedReader reader) throws Exception {
    StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
    int I = Integer.parseInt(tokenizer.nextToken());
    for (int i = 0; i < I; i++) {
      tokenizer = new StringTokenizer(reader.readLine());
      int id = Integer.parseInt(tokenizer.nextToken());
      int type = Integer.parseInt(tokenizer.nextToken());
      String name = tokenizer.nextToken();
      nodes[id].type = type;
      nodes[id].name = name;
    }
  }

  public int dijkstra(int startNode, int endNode) {
    boolean[] visited = new boolean[nodes.length];
    queue = new PriorityQueue<Node>();
    for(int i = 0; i < nodes.length; i++) {
      nodes[i].distance = Integer.MAX_VALUE;
    }
    nodes[startNode].distance = 0;
    queue.add(nodes[startNode]);
    int count = 0;
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      if (visited[node.id]) {
        continue;
      }
      visited[node.id] = true;
      count++;
      if (node.id == endNode) {
        return count;
      }
      adjacent(node, visited);
    }
    return -1;
  }

  public void adjacent(Node node, boolean[] visited) {
    for (Edge edge : node.edges) {
      if (!visited[edge.to.id]) {
        int newDistance = node.distance + edge.weight;
        if (newDistance < edge.to.distance) {
          queue.remove(edge.to);
          edge.to.distance = newDistance;
          edge.to.from = node;
          queue.add(edge.to);
        }
      }
    }
  }

  public Node[] dijkstraType(int startNode, int type) {
    boolean[] visited = new boolean[nodes.length];
    queue = new PriorityQueue<Node>();
    Node[] foundNodes = new Node[8];
    for (int i = 0; i < N; i++) {
      nodes[i].distance = Integer.MAX_VALUE;
    }
    nodes[startNode].distance = 0;
    queue.add(nodes[startNode]);
    int count = 0;
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      visited[node.id] = true;
      if(node.type == type) {
        count++;
        foundNodes[count-1] = node;
      }
      if (count == 8) {
        break;
      }
      adjacent(node, visited);
    }
    return foundNodes;
  }

  public void printFoundNodes(Node[] foundNodes) {
    for (int i = 0; i < foundNodes.length; i++) {
      System.out.println(foundNodes[i].latitude + "," + foundNodes[i].longitude);
    }
  }

  public void getShortestPathDijkstra(int endNode) {
    int count = 0;
    Node node = nodes[endNode];
    while (node != null) {
      count++;
      System.out.println(node.latitude + "," + node.longitude);
      node = node.from;
    }
    System.out.println("Number of nodes in shortest path: " + count);
  }

  public void runDijkstra(int startNode, int endNode) {
    resetNodesandEdges();
    long startTime = System.nanoTime();
    int count = dijkstra(startNode, endNode);
    long duration = (System.nanoTime() - startTime) / 1000000;

    int seconds = nodes[endNode].distance / 100;
    int hours = seconds / 3600;
    int minutes = (seconds % 3600) / 60;
    seconds = seconds % 60;
    System.out.println("Dijkstra found a path that takes " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
    System.out.println("Dijkstra visited " + count + " nodes");
    System.out.println("Runtime was " + duration + " ms");
    getShortestPathDijkstra(endNode);
  }

  public int ALT(int startNode, int endNode) {
    visited = new HashSet<Node>();
    queue = new PriorityQueue<Node>(
            new Comparator<Node>() {
              @Override
              public int compare(Node o1, Node o2) {
                return o1.fullDistance - o2.fullDistance;
              }
            }
    );
    for (int i = 0; i < N; i++) {
      nodes[i].distance = Integer.MAX_VALUE;
    }
    nodes[startNode].distance = 0;
    queue.add(nodes[startNode]);
    int count = 0;
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      count++;
      visited.add(node);
      if (node.id == endNode) {
        return count;
      }
      for(Edge edge : node.edges) {
        if(!queue.contains(edge.to) && !visited.contains(edge.to)) {
          edge.to.distanceToEnd = estimate(edge.to.id,endNode);
          queue.add(edge.to);
        }

        int alternativeDistance = node.distance + edge.weight;
        if (alternativeDistance < edge.to.distance) {
          queue.remove(edge.to);
          edge.to.distance = alternativeDistance;
          edge.to.fullDistance = edge.to.distance + edge.to.distanceToEnd;
          edge.to.from = node;
          queue.add(edge.to);
        }
      }
    }
    return -1;
  }

  public void getShortestPathALT(int endNode) {
    int count = 0;
    Node node = nodes[endNode];
    while (node != null) {
      count++;
      System.out.println(node.latitude + "," + node.longitude);
      node = node.from;
    }
    System.out.println("Number of nodes in shortest path: " + count);
  }


  public void runALT(int startNode, int endNode) {
    resetNodesandEdges();
    readFiles();
    long startTime = System.nanoTime();
    int count = ALT(startNode, endNode);
    long duration = (System.nanoTime() - startTime) / 1000000;

    int seconds = nodes[endNode].distance / 100;
    int hours = seconds / 3600;
    int minutes = (seconds % 3600) / 60;
    seconds = seconds % 60;

    System.out.println("ALT found a path that takes " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
    System.out.println("ALT visited " + count + " nodes");
    System.out.println("Runtime was " + duration + " ms");
    getShortestPathALT(endNode);
  }

  public int estimate(int currentNode, int endNode) {
    ArrayList<Integer> landmarkDistances = new ArrayList<Integer>();
    ArrayList<Integer> landmarkDistancesReverse = new ArrayList<Integer>();
    int currentBest = 0;
    for(int i = 0; i < 3; i++) {
      landmarkDistances = this.landmarkDistancesAll.get(i);
      landmarkDistancesReverse = this.landmarkDistancesAll.get(i+3);

      int var1 = landmarkDistances.get(endNode);
      int var2 = landmarkDistances.get(currentNode);
      int var3 = var1 - var2;
      if(var3 < 0) {
        var3 = 0;
      }

      int var4 = landmarkDistancesReverse.get(currentNode);
      int var5 = landmarkDistancesReverse.get(endNode);
      int var6 = var4 - var5;

      int tempBest = 0;
      if(var3 > var6) {
        tempBest = var3;
      } else {
        tempBest = var6;
      }
      if(tempBest > currentBest) {
        currentBest = tempBest;
      }
    }
    return currentBest;
  }

  public void preprocessed() throws Exception {
    ArrayList<ArrayList<Integer>> list = new ArrayList<>();

    for(int i = 0; i < 6; i++) {
      list.add(new ArrayList<>());
    }

    int[] landmarkIds = new int[3];
    landmarkIds[0] = 2748806;
    landmarkIds[1] = 955599;
    landmarkIds[2] = 4048919;

    for(int i = 0; i < 3; i++) {
      System.out.println(i);
      dijkstra(landmarkIds[i], -1);
      for(int j = 0; j < nodes.length; j++) {
        list.get(i).add(nodes[j].distance);
      }
    }

    resetNodesandEdges();
    readInverseEdges(new BufferedReader(new FileReader("kanter.txt")));

    for(int i = 0; i < 3; i++) {
      System.out.println(i);
      dijkstra(landmarkIds[i], -1);
      for(int j = 0; j < nodes.length; j++) {
        list.get(i+3).add(nodes[j].distance);
      }
    }

    for(int i = 0; i < 6; i++) {
      writeToFile(list.get(i), "landmark" + i + ".txt");
    }
  }

  public ArrayList<Integer> readFile(String fileName) {
    BufferedReader reader = null;
    ArrayList<Integer> list = new ArrayList<Integer>();
    try {
      reader = new BufferedReader(new FileReader(fileName));
      String line = reader.readLine();
      while (line != null) {
        list.add(Integer.parseInt(line));
        line = reader.readLine();
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public void readFiles() {
    for(int i = 0; i < 6; i++) {
      String fileName = "landmark" + i + ".txt";
      landmarkDistancesAll.add(readFile(fileName));
    }
  }

  private void writeToFile(ArrayList<Integer> list, String fileName) {
    try {
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
      for (int i = 0; i < list.size(); i++) {
        writer.println(list.get(i));
      }
      System.out.println("File written: " + fileName);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void resetNodesandEdges() {
    for (int i = 0; i < N; i++) {
      nodes[i].distance = Integer.MAX_VALUE;
      nodes[i].from = null;
    }
  }

  public static void main(String[] args) throws Exception {
    BufferedReader nodeReader = new BufferedReader(new FileReader("noder.txt"));
    BufferedReader edgeReader = new BufferedReader(new FileReader("kanter.txt"));
    BufferedReader interestReader = new BufferedReader(new FileReader("interessepkt.txt"));
    graph = new Graph(nodeReader, edgeReader, interestReader);

    while (true) {
      Scanner scanner = new Scanner(System.in);
      System.out.println("1. Dijkstra" + "\n" + "2. Dijkstra for specific type" + "\n" + "3. ALT" + "\n" + "4. Preprocess" + "\n" + "5. Exit");
      int choice = scanner.nextInt();
      if(choice == 1) {
        System.out.println("Enter start node");
        int startNode = scanner.nextInt();
        System.out.println("Enter end node");
        int endNode = scanner.nextInt();
        graph.runDijkstra(startNode, endNode);
      } else if(choice == 2) {
        System.out.println("Enter start node");
        int startNode = scanner.nextInt();
        System.out.println("Enter type code:");
        int typeCode = scanner.nextInt();
        graph.printFoundNodes(graph.dijkstraType(startNode, typeCode));
      } else if(choice == 3) {
        System.out.println("Enter start node");
        int startNode = scanner.nextInt();
        System.out.println("Enter end node");
        int endNode = scanner.nextInt();
        graph.runALT(startNode, endNode);
      } else if(choice == 4) {
        graph.preprocessed();
      } else if(choice == 5) {
        break;
      }
    }
  }
}
