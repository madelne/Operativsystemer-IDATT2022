package org.example;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
  public int id;
  public int type;
  String name;
  public double latitude;
  public double longitude;
  Node from;
  int distance;
  int distanceToEnd;
  int fullDistance;

  ArrayList<Edge> edges = new ArrayList<Edge>();

  public Node(int id, double latitude, double longitude) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public int compareTo(Node node) {
    if(this.distance < node.distance) {
      return -1;
    } else if(this.distance > node.distance) {
      return 1;
    } else {
      return 0;
    }
  }
}
