package uk.ac.ucl.charts.axischarts;

import uk.ac.ucl.charts.Chart;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import static java.lang.Math.pow;

public class Axis extends Chart {

    private double verticalValueSpacing;
    private double heightRatio;

    protected int drawPointX;
    protected int drawPointY;
    protected int maxGraphHeight;
    protected int maxGraphWidth;

    private int numberOfVerticalSpaces;

    protected List<Double> yValues;

    private String horizontalAxisLabel;
    private String verticalAxisLabel;

    protected Axis(List<Double> yValues){
        super();
        this.yValues = yValues;
        initValues();
    }

    void setHorizontalLabel(String label){
        horizontalAxisLabel = label;
    }

    void setVerticalLabel(String label){
        verticalAxisLabel = label;
    }

    private void initValues(){
        drawPointX = width / 10;
        maxGraphHeight = (int) (height * 0.7);
        maxGraphWidth = (int) (width * 0.8);
        drawPointY = (int) (height*0.8);

        double largest = getLargestValue(yValues);
        verticalValueSpacing = getSpacing(largest);
        numberOfVerticalSpaces = (int) Math.ceil(largest / verticalValueSpacing);

        heightRatio = maxGraphHeight / (numberOfVerticalSpaces * verticalValueSpacing);
    }

    protected static double getLargestValue(List<Double> values){
        double largestValue = values.get(0);
        for (double value: values){
            if  (value > largestValue) {
                largestValue = value;
            }
        }
        return largestValue;
    }

    protected static double getSpacing(double largestValue){
        int power = 0;
        while (largestValue > 10) {
            largestValue = largestValue / 10;
            power++;
        }
        while (largestValue < 0){
            largestValue = largestValue * 10;
            power--;
        }

        if (largestValue <= 2){
            return 0.5*pow(10, power);
        } else if (largestValue <= 5){
            return pow(10, power);
        } else {
            return 2*pow(10, power);
        }
    }

    protected int mapValueToHeight(double value) {
        return (int) (heightRatio * value);
    }

    // <---------------- Paint Methods -------------------->

    protected void paintBackground(Graphics2D graphics) {
        paintAxis(graphics);
        paintHorizontalGridLines(graphics);
        paintTitle(graphics);
        paintLabels(graphics);
    }

    private void paintAxis(Graphics2D graphics){
        // drawing axis
        int yAxisEndPoint = drawPointY - maxGraphHeight;
        int xAxisEndPoint = maxGraphWidth - drawPointX;
        graphics.drawLine(drawPointX, drawPointY, xAxisEndPoint, drawPointY);
        graphics.drawLine(drawPointX, drawPointY, drawPointX, yAxisEndPoint);
    }

    private void paintHorizontalGridLines(Graphics2D graphics){
        int level = 0;
        int verticalSpace = mapValueToHeight(verticalValueSpacing);
        int currentY = drawPointY;
        for (int space = 0; space <= numberOfVerticalSpaces; space++){
            String string = Integer.toString((int) (level * verticalValueSpacing));
            paintHorizontalGridLine(graphics, currentY, string);
            level++;
            currentY -= verticalSpace;
        }
    }

    private void paintHorizontalGridLine(Graphics2D graphics, int yValue, String yLabel){
        graphics.setPaint(Color.gray);
        graphics.drawLine(drawPointX, yValue, width - drawPointX, yValue);
        graphics.setPaint(Color.BLACK);
        graphics.drawString(yLabel, (float) (drawPointX* (1 - yLabel.length()*0.1)), yValue);
    }

    private void paintLabels(Graphics2D graphics){
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        graphics.drawString(horizontalAxisLabel,(float) (width*(0.5 - (float) horizontalAxisLabel.length()/200)), (float) (height*0.98));
        graphics.setFont(createRotatedFont(24));
        graphics.drawString(verticalAxisLabel, (float) ((width / 10)*0.35), (float) (height*0.48));
    }

    static Font createRotatedFont(int size){
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(-Math.PI/2, 0, 0);
        return font.deriveFont(affineTransform);
    }



}
