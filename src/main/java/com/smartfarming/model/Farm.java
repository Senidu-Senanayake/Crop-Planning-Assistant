package com.smartfarming.model;

public class Farm {
    private String soilType;    // "Loamy", "Clay", "Sandy"
    private String rainfall;    // "Low", "Medium", "High"
    private String season;      // "Yala", "Maha"
    private String location;

    public Farm(String soilType, String rainfall, String season, String location) {
        this.soilType = soilType;
        this.rainfall = rainfall;
        this.season = season;
        this.location = location;
    }

    public String getSoilType() { return soilType; }
    public String getRainfall() { return rainfall; }
    public String getSeason() { return season; }
    public String getLocation() { return location; }
}
