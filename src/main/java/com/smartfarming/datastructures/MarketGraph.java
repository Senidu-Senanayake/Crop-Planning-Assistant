package com.smartfarming.datastructures;

import com.smartfarming.model.Market;

import java.util.*;

/**
 * Weighted undirected graph representing farms and markets.
 * Uses Dijkstra's algorithm to find the shortest (cheapest) route.
 */
public class MarketGraph {

    private Map<String, GraphNode> nodes = new HashMap<>();
    private Map<String, Market> marketData = new HashMap<>();

    public MarketGraph() {
        buildDefaultGraph();
    }

    private void buildDefaultGraph() {
        // Add nodes
        addNode("Farm");
        addNode("Colombo");
        addNode("Gampaha");
        addNode("Kurunegala");
        addNode("Kandy");
        addNode("Matara");

        // Add undirected edges (distances in km)
        addEdge("Farm", "Colombo", 45);
        addEdge("Farm", "Gampaha", 30);
        addEdge("Farm", "Kurunegala", 60);
        addEdge("Farm", "Kandy", 80);
        addEdge("Farm", "Matara", 150);
        addEdge("Colombo", "Gampaha", 25);
        addEdge("Colombo", "Kurunegala", 95);
        addEdge("Gampaha", "Kurunegala", 70);
        addEdge("Kurunegala", "Kandy", 45);
        addEdge("Kandy", "Matara", 130);

        // Market details (cost per km ≈ Rs. 70 base calculation)
        marketData.put("Colombo", new Market("Colombo", 45, 3200, "High"));
        marketData.put("Gampaha", new Market("Gampaha", 30, 2100, "High"));
        marketData.put("Kurunegala", new Market("Kurunegala", 60, 4200, "Medium"));
        marketData.put("Kandy", new Market("Kandy", 80, 5600, "Medium"));
        marketData.put("Matara", new Market("Matara", 150, 10500, "Low"));
    }

    private void addNode(String name) {
        nodes.put(name, new GraphNode(name));
    }

    private void addEdge(String from, String to, double distance) {
        GraphNode a = nodes.get(from);
        GraphNode b = nodes.get(to);
        a.addEdge(new GraphNode.GraphEdge(b, distance));
        b.addEdge(new GraphNode.GraphEdge(a, distance));
    }

    /**
     * Dijkstra's algorithm: finds shortest distances from "Farm" to all markets.
     * Returns a list of Markets sorted by distance (optimal first).
     */
    public List<Market> findOptimalMarkets() {
        Map<String, Double> dist = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (String name : nodes.keySet()) dist.put(name, Double.MAX_VALUE);
        dist.put("Farm", 0.0);
        pq.add("Farm");

        while (!pq.isEmpty()) {
            String current = pq.poll();
            GraphNode node = nodes.get(current);
            for (GraphNode.GraphEdge edge : node.getEdges()) {
                String neighbor = edge.getTarget().getName();
                double newDist = dist.get(current) + edge.getWeight();
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    pq.add(neighbor);
                }
            }
        }

        // Build result list using shortest distances
        List<Market> result = new ArrayList<>();
        for (Map.Entry<String, Market> entry : marketData.entrySet()) {
            String marketName = entry.getKey();
            Market m = entry.getValue();
            double shortestDist = dist.getOrDefault(marketName, m.getDistanceKm());
            // Create a new market with the Dijkstra-computed distance
            Market optimized = new Market(m.getName(), shortestDist,
                    m.getTransportCost(), m.getDemand());
            result.add(optimized);
        }

        // Sort by transport cost ascending
        result.sort(Comparator.comparingDouble(Market::getTransportCost));

        // Mark the first as optimal
        if (!result.isEmpty()) result.get(0).setOptimal(true);

        return result;
    }
}
