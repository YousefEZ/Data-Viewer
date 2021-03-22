package uk.ac.ucl.graphs;

import uk.ac.ucl.graphs.graphdata.bargraphs.BarGraphData;

import java.awt.*;
import java.util.List;

public class BarGraph extends Graph {

    private int barWidth;
    private int barSpacing;

    private List<String> xValues;

    public BarGraph(BarGraphData graphData){
        super(graphData.getFrequency());
        this.xValues = graphData.getLabels();
        barWidth = (width - (width/5)) / xValues.size();
        barSpacing = barWidth / 20;
        setTitle(graphData.getTitleLabel());
        setHorizontalLabel(graphData.getHorizontalLabel());
        setVerticalLabel(graphData.getVerticalLabel());
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintBars(graphics2D);
        paintGraph(graphics2D);
    }

    private void paintBars(Graphics2D graphics){
        int fontSize = 26 - (xValues.size()/2);
        graphics.setFont(createRotatedFont(fontSize));
        for (int barIndex = 0; barIndex < xValues.size(); barIndex++){
            paintBar(graphics, barIndex, fontSize);
        }
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    }

    private void paintBar(Graphics2D graphics, int barIndex, int fontSize){
        graphics.setPaint(Color.BLUE);
        int startX = drawPointX + (barWidth * barIndex);
        int barHeight = mapValueToHeight(yValues.get(barIndex));
        graphics.fillRect(startX, drawPointY-barHeight, barWidth-barSpacing, barHeight);
        graphics.setPaint(Color.BLACK);
        String string = xValues.get(barIndex);
        graphics.drawString(string, startX+(barWidth/2), drawPointY+((string.length()+1)*fontSize/2));
    }

}
