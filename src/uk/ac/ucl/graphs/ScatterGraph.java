package uk.ac.ucl.graphs;

import uk.ac.ucl.graphs.graphdata.PointGraphData;

import java.awt.*;


public class ScatterGraph extends PointGraph {

    public ScatterGraph(PointGraphData graphData){
        super(graphData);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintScatter(graphics2D);
        paintGraph(graphics2D);
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
