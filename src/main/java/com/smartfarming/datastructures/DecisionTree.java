package com.smartfarming.datastructures;

import com.smartfarming.model.Crop;
import com.smartfarming.model.Farm;

import java.util.Arrays;
import java.util.List;

/**
 * Decision Tree for crop recommendation.
 * Structure: Soil Type -> Rainfall -> Season -> [Leaf: up to 4 crops + tip + risk]
 *
 * Soils   : Loamy, Clay, Sandy
 * Rainfall: Low, Medium, High
 * Seasons : Yala (Apr–Aug), Maha (Oct–Mar)
 *
 * 18 leaf nodes total (3 soils × 3 rainfalls × 2 seasons).
 * Each leaf has 3–4 recommended crops, a soil improvement tip, and a risk level.
 */
public class DecisionTree {

    private final TreeNode root;

    public DecisionTree() {
        root = new TreeNode("ROOT");
        buildTree();
    }

    // ──────────────────────────────────────────────────────────────────────
    private void buildTree() {

        // ════════════════════════════════════════════════════════════════
        //  LOAMY SOIL  (best all-round soil for Sri Lanka)
        // ════════════════════════════════════════════════════════════════
        TreeNode loamy = new TreeNode("Soil:Loamy");
        root.addChild(loamy);

        // --- Loamy + HIGH ---
        TreeNode lH = new TreeNode("Rainfall:High");
        loamy.addChild(lH);

        lH.addChild(leaf("Season:Yala",
                crops(
                        crop("Paddy (BG 352)",      "Highly Suitable",  50000, 95000, 120),
                        crop("Maize (Ruwan)",        "Highly Suitable",  42000, 75000, 115),
                        crop("Banana (Ambul)",       "Medium Suitable",  55000, 98000, 330),
                        crop("Green Gram",           "Medium Suitable",  28000, 52000,  80)
                ),
                "Apply compost (5 t/ha) and balanced NPK before planting. " +
                        "Maintain pH 6.0–6.5. Install drainage channels to avoid waterlogging.",
                "Medium Risk"
        ));

        lH.addChild(leaf("Season:Maha",
                crops(
                        crop("Paddy (AT 308)",       "Highly Suitable",  50000, 98000, 120),
                        crop("Sweet Potato",         "Highly Suitable",  30000, 58000, 100),
                        crop("Ginger",               "Medium Suitable",  70000,130000, 240),
                        crop("Cowpea",               "Medium Suitable",  22000, 42000,  75)
                ),
                "Maintain soil pH 6.0–7.0. Apply organic matter pre-season. " +
                        "Intercrop with legumes to fix nitrogen naturally.",
                "Low Risk"
        ));

        // --- Loamy + MEDIUM ---
        TreeNode lM = new TreeNode("Rainfall:Medium");
        loamy.addChild(lM);

        lM.addChild(leaf("Season:Yala",
                crops(
                        crop("Maize",                "Highly Suitable",  42000, 76000, 115),
                        crop("Soybean",              "Highly Suitable",  35000, 65000, 100),
                        crop("Tomato",               "Medium Suitable",  60000,110000,  90),
                        crop("Cowpea",               "Medium Suitable",  22000, 40000,  75)
                ),
                "Use drip irrigation to supplement medium rainfall. " +
                        "Apply mulch to conserve soil moisture. NPK 40:20:20.",
                "Medium Risk"
        ));

        lM.addChild(leaf("Season:Maha",
                crops(
                        crop("Maize (Ruwan)",        "Highly Suitable",  42000, 78000, 115),
                        crop("Green Gram",           "Highly Suitable",  28000, 54000,  80),
                        crop("Capsicum",             "Medium Suitable",  65000,118000,  90),
                        crop("Okra",                 "Medium Suitable",  25000, 48000,  70)
                ),
                "Apply balanced NPK (40:20:20). Add compost before cultivation. " +
                        "Weed control critical in first 30 days.",
                "Low Risk"
        ));

        // --- Loamy + LOW ---
        TreeNode lL = new TreeNode("Rainfall:Low");
        loamy.addChild(lL);

        lL.addChild(leaf("Season:Yala",
                crops(
                        crop("Groundnut",            "Highly Suitable",  30000, 60000, 110),
                        crop("Sorghum",              "Highly Suitable",  18000, 38000, 100),
                        crop("Sesame",               "Medium Suitable",  16000, 32000,  90),
                        crop("Mung Bean",            "Medium Suitable",  20000, 38000,  75)
                ),
                "Install drip irrigation. Mulch with paddy straw (5 t/ha). " +
                        "Choose drought-tolerant varieties; avoid heavy N application.",
                "High Risk"
        ));

        lL.addChild(leaf("Season:Maha",
                crops(
                        crop("Groundnut",            "Highly Suitable",  30000, 62000, 110),
                        crop("Mung Bean",            "Highly Suitable",  20000, 40000,  75),
                        crop("Finger Millet",        "Medium Suitable",  15000, 30000,  95),
                        crop("Cowpea",               "Medium Suitable",  22000, 42000,  75)
                ),
                "Conserve moisture with cover crops. Contour bunds reduce runoff. " +
                        "Avoid deep tillage to preserve residual moisture.",
                "High Risk"
        ));

        // ════════════════════════════════════════════════════════════════
        //  CLAY SOIL  (good water retention, drainage issues)
        // ════════════════════════════════════════════════════════════════
        TreeNode clay = new TreeNode("Soil:Clay");
        root.addChild(clay);

        // --- Clay + HIGH ---
        TreeNode cH = new TreeNode("Rainfall:High");
        clay.addChild(cH);

        cH.addChild(leaf("Season:Yala",
                crops(
                        crop("Paddy (BG 94-1)",      "Highly Suitable",  52000, 98000, 120),
                        crop("Taro (Kiri Ala)",       "Highly Suitable",  30000, 62000, 180),
                        crop("Banana",               "Medium Suitable",  55000, 95000, 330),
                        crop("Water Spinach",        "Medium Suitable",  15000, 30000,  60)
                ),
                "Dig sub-surface drainage channels (every 5 m). Add gypsum 2 t/ha to " +
                        "improve clay structure. Raise beds 30 cm for upland crops.",
                "Medium Risk"
        ));

        cH.addChild(leaf("Season:Maha",
                crops(
                        crop("Paddy (BG 300)",       "Highly Suitable",  52000,100000, 120),
                        crop("Sugarcane",            "Highly Suitable",  80000,155000, 365),
                        crop("Banana (Kolikuttu)",   "Medium Suitable",  55000, 96000, 330),
                        crop("Lotus Root",           "Medium Suitable",  40000, 78000, 180)
                ),
                "Improve drainage before Maha rains. Add organic matter 10 t/ha. " +
                        "Avoid heavy machinery to prevent compaction.",
                "Low Risk"
        ));

        // --- Clay + MEDIUM ---
        TreeNode cM = new TreeNode("Rainfall:Medium");
        clay.addChild(cM);

        cM.addChild(leaf("Season:Yala",
                crops(
                        crop("Sugarcane",            "Highly Suitable",  80000,152000, 365),
                        crop("Sweet Potato",         "Highly Suitable",  28000, 56000, 120),
                        crop("Beans (Country)",      "Medium Suitable",  35000, 65000,  90),
                        crop("Brinjal",              "Medium Suitable",  30000, 58000,  85)
                ),
                "Aerate soil with subsoil plough before planting. " +
                        "Mix compost to break clay texture. Ridge planting prevents waterlogging.",
                "Medium Risk"
        ));

        cM.addChild(leaf("Season:Maha",
                crops(
                        crop("Sugarcane",            "Highly Suitable",  80000,158000, 365),
                        crop("Cassava",              "Highly Suitable",  22000, 50000, 270),
                        crop("Bitter Gourd",         "Medium Suitable",  40000, 75000,  90),
                        crop("Long Bean",            "Medium Suitable",  25000, 48000,  80)
                ),
                "Apply gypsum + organic compost pre-season. Clay shrinks in dry spells — " +
                        "keep moisture consistent with furrow irrigation.",
                "Low Risk"
        ));

        // --- Clay + LOW ---
        TreeNode cL = new TreeNode("Rainfall:Low");
        clay.addChild(cL);

        cL.addChild(leaf("Season:Yala",
                crops(
                        crop("Cassava",              "Highly Suitable",  22000, 48000, 270),
                        crop("Sorghum",              "Highly Suitable",  18000, 38000, 100),
                        crop("Cowpea",               "Medium Suitable",  22000, 42000,  75),
                        crop("Finger Millet",        "Medium Suitable",  15000, 30000,  95)
                ),
                "Use ridge-and-furrow planting to maximise moisture use. " +
                        "Mulch with crop residues. Harvest rainwater in farm ponds.",
                "High Risk"
        ));

        cL.addChild(leaf("Season:Maha",
                crops(
                        crop("Cassava",              "Highly Suitable",  22000, 50000, 270),
                        crop("Mung Bean",            "Highly Suitable",  20000, 40000,  75),
                        crop("Groundnut",            "Medium Suitable",  30000, 58000, 110),
                        crop("Sesame",               "Medium Suitable",  16000, 32000,  90)
                ),
                "Apply surface mulch to reduce evaporation. " +
                        "Deep ripping before planting improves infiltration in clay.",
                "High Risk"
        ));

        // ════════════════════════════════════════════════════════════════
        //  SANDY SOIL  (fast-draining, low nutrients)
        // ════════════════════════════════════════════════════════════════
        TreeNode sandy = new TreeNode("Soil:Sandy");
        root.addChild(sandy);

        // --- Sandy + HIGH ---
        TreeNode sH = new TreeNode("Rainfall:High");
        sandy.addChild(sH);

        sH.addChild(leaf("Season:Yala",
                crops(
                        crop("Watermelon",           "Highly Suitable",  35000, 72000,  90),
                        crop("Pineapple",            "Highly Suitable",  45000, 88000, 540),
                        crop("Pumpkin",              "Medium Suitable",  20000, 40000, 110),
                        crop("Cucumber",             "Medium Suitable",  22000, 42000,  65)
                ),
                "Add organic matter 8 t/ha to improve water retention. " +
                        "Use slow-release fertilisers. Frequent small irrigations preferred.",
                "Medium Risk"
        ));

        sH.addChild(leaf("Season:Maha",
                crops(
                        crop("Watermelon",           "Highly Suitable",  35000, 74000,  90),
                        crop("Melon",                "Highly Suitable",  38000, 72000,  90),
                        crop("Cucumber",             "Medium Suitable",  22000, 44000,  65),
                        crop("Ash Plantain",         "Medium Suitable",  20000, 40000,  90)
                ),
                "Incorporate clay subsoil or vermicompost to bind sandy particles. " +
                        "Irrigation twice daily in dry spells.",
                "Medium Risk"
        ));

        // --- Sandy + MEDIUM ---
        TreeNode sM = new TreeNode("Rainfall:Medium");
        sandy.addChild(sM);

        sM.addChild(leaf("Season:Yala",
                crops(
                        crop("Groundnut",            "Highly Suitable",  30000, 60000, 110),
                        crop("Carrot",               "Highly Suitable",  28000, 55000,  90),
                        crop("Sweet Potato",         "Medium Suitable",  25000, 48000, 120),
                        crop("Radish",               "Medium Suitable",  15000, 30000,  45)
                ),
                "Use slow-release fertilisers. Sandy soils lose N fast — split applications. " +
                        "Mulch heavily to retain moisture.",
                "Medium Risk"
        ));

        sM.addChild(leaf("Season:Maha",
                crops(
                        crop("Groundnut",            "Highly Suitable",  30000, 62000, 110),
                        crop("Beetroot",             "Highly Suitable",  25000, 50000,  80),
                        crop("Carrot",               "Medium Suitable",  28000, 56000,  90),
                        crop("Leeks",                "Medium Suitable",  30000, 58000,  90)
                ),
                "Sandy soil drains fast — plan drip irrigation carefully. " +
                        "Add compost 6 t/ha before planting. Avoid heavy rains after germination.",
                "Medium Risk"
        ));

        // --- Sandy + LOW ---
        TreeNode sL = new TreeNode("Rainfall:Low");
        sandy.addChild(sL);

        sL.addChild(leaf("Season:Yala",
                crops(
                        crop("Sorghum",              "Highly Suitable",  18000, 40000, 100),
                        crop("Finger Millet (Kurakkan)", "Highly Suitable", 14000, 30000, 95),
                        crop("Sesame",               "Medium Suitable",  16000, 33000,  90),
                        crop("Millet (Pearl)",       "Medium Suitable",  14000, 28000,  85)
                ),
                "Only drought-resistant varieties viable. Contour farming slows " +
                        "runoff. Consider mulching with coconut husk. Plant windbreaks.",
                "High Risk"
        ));

        sL.addChild(leaf("Season:Maha",
                crops(
                        crop("Sorghum",              "Highly Suitable",  18000, 42000, 100),
                        crop("Mung Bean",            "Highly Suitable",  20000, 40000,  75),
                        crop("Sesame",               "Medium Suitable",  16000, 34000,  90),
                        crop("Cowpea",               "Medium Suitable",  22000, 43000,  75)
                ),
                "Moisture harvesting essential. Plant after first Maha showers. " +
                        "Organic mulch 5 t/ha reduces water loss by 30%.",
                "High Risk"
        ));
    }

    // ── Helpers ──────────────────────────────────────────────────────────
    private TreeNode leaf(String cond, List<Crop> crops, String tip, String risk) {
        return new TreeNode(cond, crops, tip, risk);
    }

    private List<Crop> crops(Crop... cs) { return Arrays.asList(cs); }

    private Crop crop(String name, String suit, double cost, double income, int days) {
        return new Crop(name, suit, cost, income, days);
    }

    // ── Public API ────────────────────────────────────────────────────────
    /**
     * DFS traversal: Soil → Rainfall → Season → leaf.
     */
    public TreeNode recommend(Farm farm) {
        return traverse(root, farm);
    }

    private TreeNode traverse(TreeNode node, Farm farm) {
        if (node.isLeaf()) return node;

        for (TreeNode child : node.getChildren()) {
            String cond = child.getCondition();
            boolean matches;

            if      (cond.startsWith("Soil:"))     matches = cond.equals("Soil:"     + farm.getSoilType());
            else if (cond.startsWith("Rainfall:")) matches = cond.equals("Rainfall:" + farm.getRainfall());
            else if (cond.startsWith("Season:"))   matches = cond.equals("Season:"   + farm.getSeason());
            else                                   matches = cond.equals("ROOT");

            if (matches) {
                TreeNode result = traverse(child, farm);
                if (result != null) return result;
            }
        }
        return null;
    }
}