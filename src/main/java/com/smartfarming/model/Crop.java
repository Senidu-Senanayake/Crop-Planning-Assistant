package com.smartfarming.model;

public class Crop {
    private String name;
    private String suitabilityLevel; // "Highly Suitable", "Medium Suitable", "Low Suitable"
    private double cost;
    private double expectedIncome;
    private int growthDays;
    private String icon; // emoji or label

    public Crop(String name, String suitabilityLevel, double cost, double expectedIncome, int growthDays) {
        this.name = name;
        this.suitabilityLevel = suitabilityLevel;
        this.cost = cost;
        this.expectedIncome = expectedIncome;
        this.growthDays = growthDays;
    }

    public String getName() { return name; }
    public String getSuitabilityLevel() { return suitabilityLevel; }
    public double getCost() { return cost; }
    public double getExpectedIncome() { return expectedIncome; }
    public double getProfit() { return expectedIncome - cost; }
    public int getGrowthDays() { return growthDays; }

    public int getSuitabilityScore() {
        return switch (suitabilityLevel) {
            case "Highly Suitable" -> 3;
            case "Medium Suitable" -> 2;
            default -> 1;
        };
    }
}
