package uk.ac.ucl.charts.axischarts.coordinatescharts;

import uk.ac.ucl.charts.chartdata.CoordinatesData;

import java.awt.*;

public class LineChart extends CoordinatesChart {

    public LineChart(CoordinatesData graphData){
        super(graphData);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(2));
        paintLines(graphics2D);
        graphics2D.setStroke(new BasicStroke(1));
        paintBackground(graphics2D);
    }

    private void paintLines(Graphics2D graphics){
        for (int pointIndex = 0; pointIndex < xValues.size()-1; pointIndex++){
            int currentX = drawPointX + mapValueToWidth(xValues.get(pointIndex));
            int currentY = drawPointY - mapValueToHeight(yValues.get(pointIndex));
            int nextX = drawPointX + mapValueToWidth(xValues.get(pointIndex+1));
            int nextY = drawPointY - mapValueToHeight(yValues.get(pointIndex+1));
            graphics.drawLine(currentX, currentY, nextX, nextY);
        }
    }

}
