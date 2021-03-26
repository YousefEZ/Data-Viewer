package uk.ac.ucl.charts.axischarts.coordinatescharts;

import uk.ac.ucl.charts.chartdata.CoordinatesData;

import java.awt.*;


public class ScatterChart extends CoordinatesChart {

    public ScatterChart(CoordinatesData graphData){
        super(graphData);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintScatter(graphics2D);
        paintBackground(graphics2D);
    }

    // paint the scatter points.
    private void paintScatter(Graphics2D graphics){
        for (int pointIndex = 0; pointIndex < xValues.size(); pointIndex++){
            int currentX = drawPointX + mapValueToWidth(xValues.get(pointIndex)) - 5;
            int currentY = drawPointY - mapValueToHeight(yValues.get(pointIndex)) - 5;
            graphics.fillOval(currentX, currentY, 10, 10);
        }
    }

}
