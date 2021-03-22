package uk.ac.ucl.graphs;

import uk.ac.ucl.graphs.graphdata.PointGraphData;
import uk.ac.ucl.graphs.graphdata.SortPlots;

import java.awt.*;
import java.util.List;

class PointGraph extends Graph {

    private double widthRatio;
    private int numberOfHorizontalSpaces;
    private double horizontalValueSpacing;
    private Font labelFont;

    List<Double> xValues;

    PointGraph(PointGraphData graphData){
        super(graphData.getYValues());
        this.xValues = graphData.getXValues();
        SortPlots.quickSort(this.xValues, this.yValues);
        setupValues();
    }

    private void setupValues(){
        double largest = getLargestValue(xValues);
        horizontalValueSpacing = getSpacing(largest);
        numberOfHorizontalSpaces = (int) Math.ceil(largest / horizontalValueSpacing);
        widthRatio = maxGraphWidth / (numberOfHorizontalSpaces * horizontalValueSpacing);
    }

    int mapValueToWidth(double value) {
        return (int) (widthRatio * value);
    }

    @Override
    void paintGraph(Graphics2D graphics2D){
        super.paintGraph(graphics2D);
        labelFont = new Font(null);
        graphics2D.setFont(labelFont);
        paintVerticalGridlines(graphics2D);
    }

    private void paintVerticalGridlines(Graphics2D graphics){
        int level = 0;
        int horizontalSpace = mapValueToWidth(horizontalValueSpacing);
        int currentX = drawPointX;
        for (int space = 0; space <= numberOfHorizontalSpaces; space++){
            String string = Integer.toString((int) (level * horizontalValueSpacing));
            paintVerticalGridline(graphics, currentX, string);
            level++;
            currentX += horizontalSpace;
        }
    }

    private void paintVerticalGridline(Graphics2D graphics, int xValue, String xLabel){
        graphics.setPaint(Color.gray);
        graphics.drawLine(xValue, drawPointY, xValue, drawPointY - maxGraphHeight);
        graphics.setPaint(Color.BLACK);
        graphics.drawString(xLabel, xValue-xLabel.length()*3, drawPointY + labelFont.getSize2D());
    }

}
