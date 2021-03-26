package uk.ac.ucl.charts.regularcharts;

import uk.ac.ucl.charts.Chart;
import uk.ac.ucl.charts.chartdata.categoricaldata.FrequencyData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PieChart extends Chart {

    private List<String> labels;
    private List<Double> frequencies;
    private List<Color> colours;

    private final int diameter = (int) (Math.min(width, height) / 1.25);
    private double totalFrequency;

    private final Color[] STANDARD_COLOURS = {Color.BLUE, Color.CYAN, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};

    public PieChart(FrequencyData chartData){
        super();
        absorbData(chartData);
        colours = new ArrayList<>();
        loadColours();
    }

    private void absorbData(FrequencyData chartData) {
        this.labels = chartData.getLabels();
        this.frequencies = chartData.getFrequency();
        getFrequencySum();
        setTitle(chartData.getTitleLabel());
    }

    private void getFrequencySum(){
        totalFrequency = 0;
        for (double frequency: this.frequencies){
            totalFrequency += frequency;
        }
    }

    private void loadColours(){
        Color color;
        for (int labelIndex = 0; labelIndex < labels.size(); labelIndex++) {
            if (colours.size() < STANDARD_COLOURS.length) {
                color = STANDARD_COLOURS[colours.size()];
            } else {
                color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            }
            colours.add(color);
        }
    }

    @Override
    public void paint(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintPie(graphics2D);
        paintKey(graphics2D);
        paintTitle(graphics2D);
    }

    private void paintPie(Graphics2D graphics){
        int startAngle = 0;
        int drawPiePointY = (int) (height * 0.125);
        int drawPiePointX = (int) (width * 0.30);

        for (int index = 0; index < frequencies.size(); index++) {
            int nextAngle = (int) ((frequencies.get(index) * 360) / totalFrequency);
            graphics.setPaint(colours.get(index));
            graphics.fillArc(drawPiePointX, drawPiePointY, diameter, diameter, startAngle, nextAngle);
            startAngle += nextAngle;
        }
        graphics.fillArc(drawPiePointX, drawPiePointY, diameter, diameter, startAngle, 360-startAngle);
    }

    private void drawKeyBoxColour(Graphics2D graphics, int x, int y, int width, int height, Color color){
        graphics.setPaint(Color.WHITE);
        graphics.fillRect(x, y, width, height);
        graphics.setPaint(Color.BLACK);
        graphics.drawRect(x, y, width, height);
        graphics.setPaint(color);
        graphics.fillRect(x+width/8, y+height/8, width*7/8, height*7/8);
    }

    private void paintKey(Graphics2D graphics){
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        int gapHeight = graphics.getFontMetrics().getHeight() + 10;

        int drawKeyPointX = (int) (width * 0.05);
        int drawKeyPointY = (height - (gapHeight * frequencies.size())) / 2; // centres the label.
        int keyBoxSideLength = 20;
        for (int labelIndex = 0; labelIndex < frequencies.size(); labelIndex++){
            drawKeyBoxColour(graphics, drawKeyPointX, drawKeyPointY, keyBoxSideLength, keyBoxSideLength, colours.get(labelIndex));
            graphics.setPaint(Color.BLACK);
            graphics.drawString(labels.get(labelIndex), drawKeyPointX + keyBoxSideLength + 5,  drawKeyPointY + gapHeight/2);
            drawKeyPointY += gapHeight;
        }
    }


}
