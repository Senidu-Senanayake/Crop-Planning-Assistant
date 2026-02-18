package com.smartfarming.datastructures;

import com.smartfarming.model.Crop;
import com.smartfarming.model.Farm;

import java.util.Arrays;
import java.util.List;

/**
 * Decision Tree for crop recommendation.
 * Structure: Soil Type -> Rainfall -> Season -> [Leaf: Crop List]
 */
public class DecisionTree {

    private TreeNode root;

    public DecisionTree() {
        buildTree();
    }

    private void buildTree() {
        root = new TreeNode("ROOT");

        // ── LOAMY SOIL ──────────────────────────────────────────────
        TreeNode loamy = new TreeNode("Soil:Loamy");
        root.addChild(loamy);

        // Loamy + High Rainfall
        TreeNode loamyHigh = new TreeNode("Rainfall:High");
        loamy.addChild(loamyHigh);
        loamyHigh.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Paddy", "Highly Suitable", 50000, 90000, 120),
                        new Crop("Maize", "Medium Suitable", 45000, 70000, 120)),
                "Add compost and organic fertilizer to enhance soil quality.",
                "High Risk"));
        loamyHigh.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Paddy", "Highly Suitable", 50000, 92000, 120),
                        new Crop("Green Gram", "Medium Suitable", 30000, 55000, 90)),
                "Maintain soil pH between 6.0–7.0 for optimal yield.",
                "Medium Risk"));

        // Loamy + Medium Rainfall
        TreeNode loamyMedium = new TreeNode("Rainfall:Medium");
        loamy.addChild(loamyMedium);
        loamyMedium.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Maize", "Highly Suitable", 45000, 75000, 120),
                        new Crop("Cowpea", "Medium Suitable", 20000, 38000, 80)),
                "Use drip irrigation to supplement medium rainfall.",
                "Medium Risk"));
        loamyMedium.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Maize", "Highly Suitable", 45000, 76000, 120),
                        new Crop("Soybean", "Medium Suitable", 35000, 60000, 100)),
                "Apply balanced NPK fertilizer for best results.",
                "Low Risk"));

        // Loamy + Low Rainfall
        TreeNode loamyLow = new TreeNode("Rainfall:Low");
        loamy.addChild(loamyLow);
        loamyLow.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Groundnut", "Highly Suitable", 30000, 58000, 110),
                        new Crop("Sorghum", "Medium Suitable", 20000, 35000, 100)),
                "Install drip irrigation; mulch to retain moisture.",
                "High Risk"));
        loamyLow.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Groundnut", "Highly Suitable", 30000, 60000, 110),
                        new Crop("Mung Bean", "Medium Suitable", 18000, 32000, 75)),
                "Conserve soil moisture with cover crops.",
                "High Risk"));

        // ── CLAY SOIL ────────────────────────────────────────────────
        TreeNode clay = new TreeNode("Soil:Clay");
        root.addChild(clay);

        TreeNode clayHigh = new TreeNode("Rainfall:High");
        clay.addChild(clayHigh);
        clayHigh.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Paddy", "Highly Suitable", 52000, 95000, 120),
                        new Crop("Taro", "Medium Suitable", 28000, 50000, 180)),
                "Improve drainage channels to prevent waterlogging.",
                "Medium Risk"));
        clayHigh.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Paddy", "Highly Suitable", 52000, 96000, 120),
                        new Crop("Banana", "Medium Suitable", 40000, 70000, 365)),
                "Add gypsum to improve clay soil structure.",
                "Low Risk"));

        TreeNode clayMedium = new TreeNode("Rainfall:Medium");
        clay.addChild(clayMedium);
        clayMedium.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Sugarcane", "Highly Suitable", 80000, 150000, 365),
                        new Crop("Paddy", "Medium Suitable", 52000, 88000, 120)),
                "Aerate soil before planting; avoid compaction.",
                "Medium Risk"));
        clayMedium.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Sugarcane", "Highly Suitable", 80000, 155000, 365),
                        new Crop("Sweet Potato", "Medium Suitable", 25000, 48000, 120)),
                "Mix organic matter to break up clay texture.",
                "Low Risk"));

        TreeNode clayLow = new TreeNode("Rainfall:Low");
        clay.addChild(clayLow);
        clayLow.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Cassava", "Highly Suitable", 22000, 45000, 270),
                        new Crop("Sorghum", "Medium Suitable", 20000, 36000, 100)),
                "Use ridge planting to maximise moisture use.",
                "High Risk"));
        clayLow.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Cassava", "Highly Suitable", 22000, 46000, 270),
                        new Crop("Cowpea", "Medium Suitable", 20000, 37000, 80)),
                "Apply mulch to reduce surface evaporation.",
                "High Risk"));

        // ── SANDY SOIL ───────────────────────────────────────────────
        TreeNode sandy = new TreeNode("Soil:Sandy");
        root.addChild(sandy);

        TreeNode sandyHigh = new TreeNode("Rainfall:High");
        sandy.addChild(sandyHigh);
        sandyHigh.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Watermelon", "Highly Suitable", 35000, 65000, 90),
                        new Crop("Pumpkin", "Medium Suitable", 20000, 38000, 110)),
                "Add organic matter and compost to increase water retention.",
                "Medium Risk"));
        sandyHigh.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Watermelon", "Highly Suitable", 35000, 66000, 90),
                        new Crop("Cucumber", "Medium Suitable", 22000, 40000, 65)),
                "Frequent small irrigation events work best on sandy soil.",
                "Medium Risk"));

        TreeNode sandyMedium = new TreeNode("Rainfall:Medium");
        sandy.addChild(sandyMedium);
        sandyMedium.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Groundnut", "Highly Suitable", 30000, 56000, 110),
                        new Crop("Sweet Potato", "Medium Suitable", 25000, 46000, 120)),
                "Use slow-release fertilizers suitable for sandy soil.",
                "Medium Risk"));
        sandyMedium.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Groundnut", "Highly Suitable", 30000, 57000, 110),
                        new Crop("Carrot", "Medium Suitable", 28000, 50000, 90)),
                "Sandy soil drains fast – plan irrigation carefully.",
                "Medium Risk"));

        TreeNode sandyLow = new TreeNode("Rainfall:Low");
        sandy.addChild(sandyLow);
        sandyLow.addChild(new TreeNode("Season:Yala",
                makeCrops(new Crop("Sorghum", "Highly Suitable", 20000, 38000, 100),
                        new Crop("Millet", "Medium Suitable", 15000, 28000, 90)),
                "Drought-resistant crops are essential on sandy soil with low rain.",
                "High Risk"));
        sandyLow.addChild(new TreeNode("Season:Maha",
                makeCrops(new Crop("Sorghum", "Highly Suitable", 20000, 39000, 100),
                        new Crop("Sesame", "Medium Suitable", 18000, 32000, 90)),
                "Consider contour farming to retain rainwater.",
                "High Risk"));
    }

    private List<Crop> makeCrops(Crop... crops) {
        return Arrays.asList(crops);
    }

    /**
     * Traverse the tree using DFS to find the matching leaf node for the given farm.
     */
    public TreeNode recommend(Farm farm) {
        return traverse(root, farm);
    }

    private TreeNode traverse(TreeNode node, Farm farm) {
        if (node.isLeaf()) return node;

        for (TreeNode child : node.getChildren()) {
            String cond = child.getCondition();
            boolean matches = false;

            if (cond.startsWith("Soil:")) {
                matches = cond.equals("Soil:" + farm.getSoilType());
            } else if (cond.startsWith("Rainfall:")) {
                matches = cond.equals("Rainfall:" + farm.getRainfall());
            } else if (cond.startsWith("Season:")) {
                matches = cond.equals("Season:" + farm.getSeason());
            } else if (cond.equals("ROOT")) {
                matches = true;
            }

            if (matches) {
                TreeNode result = traverse(child, farm);
                if (result != null) return result;
            }
        }
        return null;
    }
}
