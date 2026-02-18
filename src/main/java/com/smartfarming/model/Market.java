package com.smartfarming.model;

public class Market {
    private String name;
    private double distanceKm;
    private double transportCost;
    private String demand; // "High", "Medium", "Low"
    private boolean isOptimal;

    public Market(String name, double distanceKm, double transportCost, String demand) {
        this.name = name;
        this.distanceKm = distanceKm;
        this.transportCost = transportCost;
        this.demand = demand;
        this.isOptimal = false;
    }

    public String getName() { return name; }
    public double getDistanceKm() { return distanceKm; }
    public double getTransportCost() { return transportCost; }
    public String getDemand() { return demand; }
    public boolean isOptimal() { return isOptimal; }
    public void setOptimal(boolean optimal) { this.isOptimal = optimal; }
}
