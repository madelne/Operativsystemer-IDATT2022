package org.example;

public class Edge {
  public Node from;
  public Node to;
  public int weight;
  public double distance;
  public double speedLimit;

  public Edge(Node from, Node to, int weight, double distance, double speedLimit) {
    this.from = from;
    this.to = to;
    this.weight = weight;
    this.distance = distance;
    this.speedLimit = speedLimit;
  }
}
