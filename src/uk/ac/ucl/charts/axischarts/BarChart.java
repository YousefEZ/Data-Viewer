package uk.ac.ucl.charts.axischarts;

import uk.ac.ucl.charts.chartdata.categoricaldata.FrequencyData;

import java.awt.*;
import java.util.List;

public class BarChart extends Axis {

    private final int barWidth;
    private final int barSpacing;
    private Font horizontalLabelFont;
    private List<String> xValues;


    public BarChart(FrequencyData chartData) {
        super(chartData.getFrequency());
        this.xValues = chartData.getLabels();
        barWidth = (width - (width/5)) / xValues.size();
        barSpacing = barWidth / 20;
        setTitle(chartData.getTitleLabel());
        setHorizontalLabel(chartData.getHorizontalLabel());
        setVerticalLabel(chartData.getVerticalLabel());
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintBars(graphics2D);
        paintBackground(graphics2D);
    }

    private void paintBars(Graphics2D graphics){
        int fontSize = 26 - (xValues.size()/2);
        horizontalLabelFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        graphics.setFont(createRotatedFont(fontSize));
        for (int barIndex = 0; barIndex < xValues.size(); barIndex++){
            paintBar(graphics, barIndex);
        }
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    }

    private void paintBar(Graphics2D graphics, int barIndex){
        graphics.setPaint(Color.BLUE);
        int startX = drawPointX + (barWidth * barIndex);
        int barHeight = mapValueToHeight(yValues.get(barIndex));
        graphics.fillRect(startX, drawPointY-barHeight, barWidth-barSpacing, barHeight);
        graphics.setPaint(Color.BLACK);
        String string = xValues.get(barIndex);
        graphics.drawString(string, startX+(barWidth/2), (drawPointY+10+graphics.getFontMetrics(horizontalLabelFont).stringWidth(string)));
    }

}
