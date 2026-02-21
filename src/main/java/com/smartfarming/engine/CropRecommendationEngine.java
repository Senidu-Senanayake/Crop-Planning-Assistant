package com.smartfarming.engine;

import com.smartfarming.datastructures.DecisionTree;
import com.smartfarming.datastructures.MarketGraph;
import com.smartfarming.datastructures.TreeNode;
import com.smartfarming.model.Farm;
import com.smartfarming.model.Market;
import com.smartfarming.model.RecommendationResult;

import java.util.List;

public class CropRecommendationEngine {

    private final DecisionTree decisionTree = new DecisionTree();
    private final MarketGraph  marketGraph  = new MarketGraph();

    public RecommendationResult analyze(Farm farm) {

        // 1. Traverse decision tree (Soil → Rainfall → Season)
        TreeNode leaf = decisionTree.recommend(farm);
        if (leaf == null) {
            throw new IllegalArgumentException(
                    "No recommendation for: " + farm.getSoilType() +
                            " / " + farm.getRainfall() + " / " + farm.getSeason());
        }

        // 2. Run Dijkstra from the farm's district to all wholesale markets
        List<Market> markets = marketGraph.findMarketsFromDistrict(farm.getLocation());
        Market optimal = markets.stream()
                .filter(Market::isOptimal)
                .findFirst()
                .orElse(markets.isEmpty() ? null : markets.get(0));

        return new RecommendationResult(
                leaf.getCrops(),
                markets,
                optimal,
                leaf.getSoilTip(),
                leaf.getRiskLevel()
        );
    }
}