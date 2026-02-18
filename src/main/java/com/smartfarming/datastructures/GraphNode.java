package com.smartfarming.datastructures;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
    private String name;
    private List<GraphEdge> edges;

    public GraphNode(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public void addEdge(GraphEdge edge) { edges.add(edge); }
    public String getName() { return name; }
    public List<GraphEdge> getEdges() { return edges; }

    public static class GraphEdge {
        private GraphNode target;
        private double weight; // distance in km

        public GraphEdge(GraphNode target, double weight) {
            this.target = target;
            this.weight = weight;
        }

        public GraphNode getTarget() { return target; }
        public double getWeight() { return weight; }
    }
}
