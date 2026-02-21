package com.smartfarming.datastructures;

import com.smartfarming.model.Market;

import java.util.*;

/**
 * Full Sri Lanka district-level weighted graph.
 * Nodes  = all 25 districts + major wholesale markets.
 * Edges  = road distances in km (approximate).
 * Dijkstra's algorithm finds shortest path from the farm's district
 * to every wholesale market, returning the full traversal path.
 */
public class MarketGraph {

    // ── Wholesale market nodes (destinations) ──────────────────────────────
    public static final String MARKET_COLOMBO      = "Colombo Market";
    public static final String MARKET_DAMBULLA     = "Dambulla Market";
    public static final String MARKET_KANDY        = "Kandy Market";
    public static final String MARKET_JAFFNA       = "Jaffna Market";
    public static final String MARKET_GALLE        = "Galle Market";
    public static final String MARKET_KURUNEGALA   = "Kurunegala Market";
    public static final String MARKET_ANURADHAPURA = "Anuradhapura Market";
    public static final String MARKET_BATTICALOA   = "Batticaloa Market";

    private final Map<String, GraphNode> nodes = new LinkedHashMap<>();

    public MarketGraph() {
        buildGraph();
    }

    // ── Graph construction ─────────────────────────────────────────────────
    private void buildGraph() {

        // ---- District nodes ----
        String[] districts = {
                "Colombo","Gampaha","Kalutara","Kandy","Matale","Nuwara Eliya",
                "Galle","Matara","Hambantota","Jaffna","Kilinochchi","Mannar",
                "Mullaitivu","Vavuniya","Anuradhapura","Polonnaruwa","Badulla",
                "Moneragala","Ratnapura","Kegalle","Kurunegala","Puttalam",
                "Ampara","Trincomalee","Batticaloa"
        };
        for (String d : districts) addNode(d);

        // ---- Market nodes ----
        addNode(MARKET_COLOMBO);
        addNode(MARKET_DAMBULLA);
        addNode(MARKET_KANDY);
        addNode(MARKET_JAFFNA);
        addNode(MARKET_GALLE);
        addNode(MARKET_KURUNEGALA);
        addNode(MARKET_ANURADHAPURA);
        addNode(MARKET_BATTICALOA);

        // ---- District ↔ District edges (road km) ----
        addEdge("Colombo",      "Gampaha",       30);
        addEdge("Colombo",      "Kalutara",       40);
        addEdge("Colombo",      "Kegalle",        75);
        addEdge("Gampaha",      "Kurunegala",     80);
        addEdge("Gampaha",      "Kegalle",        65);
        addEdge("Kalutara",     "Ratnapura",      65);
        addEdge("Kalutara",     "Galle",          80);
        addEdge("Kandy",        "Matale",         25);
        addEdge("Kandy",        "Kegalle",        50);
        addEdge("Kandy",        "Nuwara Eliya",   75);
        addEdge("Kandy",        "Kurunegala",     90);
        addEdge("Kandy",        "Polonnaruwa",   130);
        addEdge("Matale",       "Dambulla",       20);   // via Dambulla town (inside Matale)
        addEdge("Matale",       "Anuradhapura",  100);
        addEdge("Nuwara Eliya", "Badulla",        75);
        addEdge("Galle",        "Matara",         30);
        addEdge("Matara",       "Hambantota",     60);
        addEdge("Hambantota",   "Moneragala",     95);
        addEdge("Jaffna",       "Kilinochchi",    45);
        addEdge("Kilinochchi",  "Mannar",         80);
        addEdge("Kilinochchi",  "Mullaitivu",     60);
        addEdge("Mannar",       "Vavuniya",       65);
        addEdge("Mullaitivu",   "Vavuniya",       75);
        addEdge("Vavuniya",     "Anuradhapura",   65);
        addEdge("Anuradhapura", "Kurunegala",    100);
        addEdge("Anuradhapura", "Puttalam",       90);
        addEdge("Anuradhapura", "Polonnaruwa",   110);
        addEdge("Polonnaruwa",  "Matale",         90);
        addEdge("Polonnaruwa",  "Trincomalee",    85);
        addEdge("Polonnaruwa",  "Ampara",        110);
        addEdge("Badulla",      "Moneragala",     65);
        addEdge("Badulla",      "Ampara",         90);
        addEdge("Ampara",       "Batticaloa",     55);
        addEdge("Ampara",       "Trincomalee",   100);
        addEdge("Trincomalee",  "Vavuniya",      120);
        addEdge("Kurunegala",   "Puttalam",       65);
        addEdge("Kurunegala",   "Kegalle",        55);
        addEdge("Ratnapura",    "Kegalle",        50);
        addEdge("Ratnapura",    "Moneragala",     95);
        addEdge("Puttalam",     "Mannar",        120);

        // ---- District ↔ Market edges ----
        // Colombo Market (Pettah / Manning)
        addEdge("Colombo",      MARKET_COLOMBO,    5);
        addEdge("Gampaha",      MARKET_COLOMBO,   35);
        addEdge("Kalutara",     MARKET_COLOMBO,   50);
        addEdge("Kegalle",      MARKET_COLOMBO,   80);

        // Dambulla Dedicated Economic Centre
        addEdge("Matale",       MARKET_DAMBULLA,  22);
        addEdge("Anuradhapura", MARKET_DAMBULLA,  85);
        addEdge("Polonnaruwa",  MARKET_DAMBULLA, 100);
        addEdge("Kurunegala",   MARKET_DAMBULLA, 110);
        addEdge("Kandy",        MARKET_DAMBULLA,  55);

        // Kandy Market (Goods Shed)
        addEdge("Kandy",        MARKET_KANDY,      4);
        addEdge("Nuwara Eliya", MARKET_KANDY,     75);
        addEdge("Matale",       MARKET_KANDY,     28);
        addEdge("Kegalle",      MARKET_KANDY,     55);

        // Jaffna Market
        addEdge("Jaffna",       MARKET_JAFFNA,     5);
        addEdge("Kilinochchi",  MARKET_JAFFNA,    45);
        addEdge("Mannar",       MARKET_JAFFNA,   110);

        // Galle Market
        addEdge("Galle",        MARKET_GALLE,      4);
        addEdge("Matara",       MARKET_GALLE,     33);
        addEdge("Hambantota",   MARKET_GALLE,     95);
        addEdge("Kalutara",     MARKET_GALLE,     85);

        // Kurunegala Market
        addEdge("Kurunegala",   MARKET_KURUNEGALA,  5);
        addEdge("Puttalam",     MARKET_KURUNEGALA, 68);
        addEdge("Kegalle",      MARKET_KURUNEGALA, 60);
        addEdge("Gampaha",      MARKET_KURUNEGALA, 82);

        // Anuradhapura Market
        addEdge("Anuradhapura", MARKET_ANURADHAPURA, 5);
        addEdge("Vavuniya",     MARKET_ANURADHAPURA, 68);
        addEdge("Puttalam",     MARKET_ANURADHAPURA, 93);
        addEdge("Mannar",       MARKET_ANURADHAPURA,120);

        // Batticaloa Market
        addEdge("Batticaloa",   MARKET_BATTICALOA,   5);
        addEdge("Ampara",       MARKET_BATTICALOA,  58);
        addEdge("Trincomalee",  MARKET_BATTICALOA, 100);
    }

    private void addNode(String name) {
        nodes.put(name, new GraphNode(name));
    }

    private void addEdge(String a, String b, double km) {
        GraphNode na = nodes.get(a);
        GraphNode nb = nodes.get(b);
        if (na == null || nb == null) return;
        na.addEdge(new GraphNode.GraphEdge(nb, km));
        nb.addEdge(new GraphNode.GraphEdge(na, km));
    }

    // ── Dijkstra from a given district ────────────────────────────────────
    /**
     * Runs Dijkstra from farmDistrict and returns a result for every
     * wholesale market: shortest distance, transport cost, and full path.
     */
    public List<Market> findMarketsFromDistrict(String farmDistrict) {

        if (!nodes.containsKey(farmDistrict)) {
            // fallback – treat as Colombo
            farmDistrict = "Colombo";
        }

        // dist map
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();   // for path reconstruction
        for (String n : nodes.keySet()) dist.put(n, Double.MAX_VALUE);
        dist.put(farmDistrict, 0.0);

        // Priority queue on distance
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(farmDistrict);

        while (!pq.isEmpty()) {
            String cur = pq.poll();
            GraphNode node = nodes.get(cur);
            for (GraphNode.GraphEdge edge : node.getEdges()) {
                String nb = edge.getTarget().getName();
                double nd = dist.get(cur) + edge.getWeight();
                if (nd < dist.get(nb)) {
                    dist.put(nb, nd);
                    prev.put(nb, cur);
                    pq.remove(nb);   // re-insert with updated priority
                    pq.add(nb);
                }
            }
        }

        // Build Market objects for each market node
        String[] marketNames = {
                MARKET_COLOMBO, MARKET_DAMBULLA, MARKET_KANDY,
                MARKET_JAFFNA, MARKET_GALLE, MARKET_KURUNEGALA,
                MARKET_ANURADHAPURA, MARKET_BATTICALOA
        };
        // demand and price data per market
        Map<String, String[]> marketInfo = buildMarketInfo();

        List<Market> results = new ArrayList<>();
        for (String mName : marketNames) {
            double km = dist.getOrDefault(mName, Double.MAX_VALUE);
            if (km == Double.MAX_VALUE) continue;

            String[] info = marketInfo.get(mName);
            String demand   = info[0];
            String priceKg  = info[1];
            double cost     = Math.round(km * 70); // Rs. 70 per km (truck rate)

            Market m = new Market(mName, km, cost, demand);
            m.setPricePerKg(priceKg);
            m.setShortestPath(reconstructPath(prev, farmDistrict, mName));
            results.add(m);
        }

        // Sort: score = distance penalised by low demand
        results.sort((a, b) -> {
            double scoreA = scoreMarket(a);
            double scoreB = scoreMarket(b);
            return Double.compare(scoreA, scoreB);
        });

        if (!results.isEmpty()) results.get(0).setOptimal(true);
        return results;
    }

    /** Lower score = better market choice */
    private double scoreMarket(Market m) {
        double demandMultiplier = m.getDemand().equals("High") ? 0.8
                : m.getDemand().equals("Medium") ? 1.0 : 1.3;
        return m.getDistanceKm() * demandMultiplier;
    }

    /** Reconstruct Dijkstra path from prev map */
    private List<String> reconstructPath(Map<String, String> prev, String src, String dst) {
        LinkedList<String> path = new LinkedList<>();
        String cur = dst;
        while (cur != null && !cur.equals(src)) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        path.addFirst(src);
        return new ArrayList<>(path);
    }

    private Map<String, String[]> buildMarketInfo() {
        Map<String, String[]> info = new HashMap<>();
        // { demand, pricePerKg }
        info.put(MARKET_COLOMBO,      new String[]{"High",   "Rs. 85/kg"});
        info.put(MARKET_DAMBULLA,     new String[]{"High",   "Rs. 78/kg"});
        info.put(MARKET_KANDY,        new String[]{"High",   "Rs. 80/kg"});
        info.put(MARKET_JAFFNA,       new String[]{"Medium", "Rs. 72/kg"});
        info.put(MARKET_GALLE,        new String[]{"Medium", "Rs. 74/kg"});
        info.put(MARKET_KURUNEGALA,   new String[]{"Medium", "Rs. 70/kg"});
        info.put(MARKET_ANURADHAPURA, new String[]{"Low",    "Rs. 65/kg"});
        info.put(MARKET_BATTICALOA,   new String[]{"Low",    "Rs. 63/kg"});
        return info;
    }

    /** Returns all district names for the UI dropdown */
    public static List<String> getAllDistricts() {
        return Arrays.asList(
                "Colombo","Gampaha","Kalutara","Kandy","Matale","Nuwara Eliya",
                "Galle","Matara","Hambantota","Jaffna","Kilinochchi","Mannar",
                "Mullaitivu","Vavuniya","Anuradhapura","Polonnaruwa","Badulla",
                "Moneragala","Ratnapura","Kegalle","Kurunegala","Puttalam",
                "Ampara","Trincomalee","Batticaloa"
        );
    }
}