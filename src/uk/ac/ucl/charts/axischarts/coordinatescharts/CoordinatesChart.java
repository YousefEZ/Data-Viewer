package uk.ac.ucl.charts.axischarts.coordinatescharts;

import uk.ac.ucl.charts.axischarts.Axis;
import uk.ac.ucl.charts.chartdata.CoordinatesData;
import uk.ac.ucl.charts.chartdata.SortPlots;

import java.awt.*;
import java.util.List;

class CoordinatesChart extends Axis {

    private double widthRatio;
    private int numberOfHorizontalSpaces;
    private double horizontalValueSpacing;
    private Font labelFont;

    List<Double> xValues;

    CoordinatesChart(CoordinatesData graphData){
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
    protected void paintBackground(Graphics2D graphics2D){
        super.paintBackground(graphics2D);
        labelFont = new Font(null);
        graphics2D.setFont(labelFont);
        paintVerticalGridLines(graphics2D);
    }

    private void paintVerticalGridLines(Graphics2D graphics){
        int level = 0;
        int horizontalSpace = mapValueToWidth(horizontalValueSpacing);
        int currentX = drawPointX;
        for (int space = 0; space <= numberOfHorizontalSpaces; space++){
            String string = Integer.toString((int) (level * horizontalValueSpacing));
            paintVerticalGridLine(graphics, currentX, string);
            level++;
            currentX += horizontalSpace;
        }
    }

    private void paintVerticalGridLine(Graphics2D graphics, int xValue, String xLabel){
        graphics.setPaint(Color.gray);
        graphics.drawLine(xValue, drawPointY, xValue, drawPointY - maxGraphHeight);
        graphics.setPaint(Color.BLACK);
        graphics.drawString(xLabel, xValue-xLabel.length()*3, drawPointY + labelFont.getSize2D());
    }

}
