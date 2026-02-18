package com.smartfarming.datastructures;

import com.smartfarming.model.Crop;
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String condition;       // e.g., "Soil:Loamy", "Rainfall:High", "Season:Yala"
    private List<Crop> crops;       // populated only at leaf nodes
    private String soilTip;
    private String riskLevel;
    private List<TreeNode> children;

    // Internal node constructor
    public TreeNode(String condition) {
        this.condition = condition;
        this.children = new ArrayList<>();
        this.crops = new ArrayList<>();
    }

    // Leaf node constructor
    public TreeNode(String condition, List<Crop> crops, String soilTip, String riskLevel) {
        this.condition = condition;
        this.crops = crops;
        this.soilTip = soilTip;
        this.riskLevel = riskLevel;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public String getCondition() { return condition; }
    public List<Crop> getCrops() { return crops; }
    public String getSoilTip() { return soilTip; }
    public String getRiskLevel() { return riskLevel; }
    public List<TreeNode> getChildren() { return children; }
    public boolean isLeaf() { return children.isEmpty(); }
}
