package com.smartfarming.model;

import java.util.List;

public class Market {
    private String name;
    private double distanceKm;
    private double transportCost;
    private String demand;
    private boolean isOptimal;
    private List<String> shortestPath;
    private String pricePerKg;

    public Market(String name, double distanceKm, double transportCost, String demand) {
        this.name = name;
        this.distanceKm = distanceKm;
        this.transportCost = transportCost;
        this.demand = demand;
        this.isOptimal = false;
    }

    public String getName()            { return name; }
    public double getDistanceKm()      { return distanceKm; }
    public double getTransportCost()   { return transportCost; }
    public String getDemand()          { return demand; }
    public boolean isOptimal()         { return isOptimal; }
    public void setOptimal(boolean v)  { isOptimal = v; }
    public List<String> getShortestPath()          { return shortestPath; }
    public void setShortestPath(List<String> path) { shortestPath = path; }
    public String getPricePerKg()                  { return pricePerKg; }
    public void setPricePerKg(String p)            { pricePerKg = p; }
}