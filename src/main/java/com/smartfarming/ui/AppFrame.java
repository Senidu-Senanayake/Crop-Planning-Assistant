package com.smartfarming.ui;

import com.smartfarming.engine.CropRecommendationEngine;
import com.smartfarming.model.Farm;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    public static final String CARD_HOME       = "HOME";
    public static final String CARD_INPUT      = "INPUT";
    public static final String CARD_CROP_REC   = "CROP_REC";
    public static final String CARD_SCHEDULER  = "SCHEDULER";
    public static final String CARD_PROFIT     = "PROFIT";
    public static final String CARD_MARKET     = "MARKET";
    public static final String CARD_FINAL      = "FINAL";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final CropRecommendationEngine engine = new CropRecommendationEngine();

    // Panels that need data
    private CropRecommendationPanel cropRecPanel;
    private CropSchedulerPanel schedulerPanel;
    private ProfitComparisonPanel profitPanel;
    private MarketOptimizationPanel marketPanel;
    private FinalRecommendationPanel finalPanel;

    public AppFrame() {
        setTitle("SCA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 940);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 700));

        buildPanels();
        add(cardPanel);
        showCard(CARD_HOME);
    }

    private void buildPanels() {
        // Static panels
        cardPanel.add(new HomePanel(this), CARD_HOME);
        cardPanel.add(new InputFormPanel(this), CARD_INPUT);

        // Dynamic result panels (will be refreshed after analysis)
        cropRecPanel  = new CropRecommendationPanel(this, null);
        schedulerPanel = new CropSchedulerPanel(this, null, null);
        profitPanel   = new ProfitComparisonPanel(this, null);
        marketPanel   = new MarketOptimizationPanel(this, null);
        finalPanel    = new FinalRecommendationPanel(this, null);

        cardPanel.add(cropRecPanel,  CARD_CROP_REC);
        cardPanel.add(schedulerPanel, CARD_SCHEDULER);
        cardPanel.add(profitPanel,   CARD_PROFIT);
        cardPanel.add(marketPanel,   CARD_MARKET);
        cardPanel.add(finalPanel,    CARD_FINAL);
    }

    public void runAnalysis(Farm farm) {
        try {
            RecommendationResult result = engine.analyze(farm);

            // Rebuild result panels with fresh data
            cardPanel.remove(cropRecPanel);
            cardPanel.remove(schedulerPanel);
            cardPanel.remove(profitPanel);
            cardPanel.remove(marketPanel);
            cardPanel.remove(finalPanel);

            cropRecPanel   = new CropRecommendationPanel(this, result);
            schedulerPanel = new CropSchedulerPanel(this, result, farm.getSeason());
            profitPanel    = new ProfitComparisonPanel(this, result);
            marketPanel    = new MarketOptimizationPanel(this, result);
            finalPanel     = new FinalRecommendationPanel(this, result);

            cardPanel.add(cropRecPanel,  CARD_CROP_REC);
            cardPanel.add(schedulerPanel, CARD_SCHEDULER);
            cardPanel.add(profitPanel,   CARD_PROFIT);
            cardPanel.add(marketPanel,   CARD_MARKET);
            cardPanel.add(finalPanel,    CARD_FINAL);

            cardPanel.revalidate();
            showCard(CARD_CROP_REC);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Analysis error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }
}
