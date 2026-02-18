package com.smartfarming.model;

import java.util.List;

public class RecommendationResult {
    private List<Crop> recommendedCrops;
    private List<Market> markets;
    private Market optimalMarket;
    private String soilTip;
    private String riskLevel;

    public RecommendationResult(List<Crop> recommendedCrops, List<Market> markets,
                                 Market optimalMarket, String soilTip, String riskLevel) {
        this.recommendedCrops = recommendedCrops;
        this.markets = markets;
        this.optimalMarket = optimalMarket;
        this.soilTip = soilTip;
        this.riskLevel = riskLevel;
    }

    public List<Crop> getRecommendedCrops() { return recommendedCrops; }
    public List<Market> getMarkets() { return markets; }
    public Market getOptimalMarket() { return optimalMarket; }
    public String getSoilTip() { return soilTip; }
    public String getRiskLevel() { return riskLevel; }

    public Crop getBestCrop() {
        return recommendedCrops.stream()
                .max((a, b) -> Double.compare(a.getProfit(), b.getProfit()))
                .orElse(null);
    }
}
