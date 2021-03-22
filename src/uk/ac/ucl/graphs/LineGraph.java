package uk.ac.ucl.graphs;

import uk.ac.ucl.graphs.graphdata.PointGraphData;

import java.awt.*;

public class LineGraph extends PointGraph {

    public LineGraph(PointGraphData graphData){
        super(graphData);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(2));
        paintLines(graphics2D);
        graphics2D.setStroke(new BasicStroke(1));
        paintGraph(graphics2D);
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
