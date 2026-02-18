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
    private final MarketGraph marketGraph = new MarketGraph();

    public RecommendationResult analyze(Farm farm) {
        // 1. Traverse decision tree
        TreeNode leaf = decisionTree.recommend(farm);

        if (leaf == null) {
            throw new IllegalArgumentException("No recommendation found for the given farm conditions.");
        }

        // 2. Get optimal markets via Dijkstra
        List<Market> markets = marketGraph.findOptimalMarkets();
        Market optimal = markets.stream().filter(Market::isOptimal).findFirst().orElse(markets.get(0));

        return new RecommendationResult(
                leaf.getCrops(),
                markets,
                optimal,
                leaf.getSoilTip(),
                leaf.getRiskLevel()
        );
    }
}
