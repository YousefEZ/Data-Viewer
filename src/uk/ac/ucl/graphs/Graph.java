package uk.ac.ucl.graphs;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import static java.lang.Math.pow;

public class Graph extends JPanel {

    int drawPointX;
    int drawPointY;

    private double verticalValueSpacing;
    private double heightRatio;
    private int numberOfVerticalSpaces;

    int maxGraphHeight;
    int maxGraphWidth;

    List<Double> yValues;

    private String horizontalAxisLabel;
    private String verticalAxisLabel;
    private String titleLabel;

    public static final int width = 1000;
    public static final int height = 800;

    Graph(List<Double> yValues){
        this.yValues = yValues;
        setSize(width, height);
        initValues();
        initTitles();
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

    private void initTitles(){
        setHorizontalLabel("");
        setVerticalLabel("");
        setTitle("");
    }

    void setHorizontalLabel(String label){
        horizontalAxisLabel = label;
    }

    void setVerticalLabel(String label){
        verticalAxisLabel = label;
    }

    void setTitle(String title){
        titleLabel = title;
    }

    static double getLargestValue(List<Double> values){
        double largestValue = values.get(0);
        for (double value: values){
            if  (value > largestValue) {
                largestValue = value;
            }
        }
        return largestValue;
    }

    static double getSpacing(double largestValue){
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

    int mapValueToHeight(double value) {
        return (int) (heightRatio * value);
    }

    // <---------------- Paint Methods -------------------->

    void paintGraph(Graphics2D graphics) {
        paintAxis(graphics);
        paintHorizontalGridlines(graphics);
        paintTitles(graphics);
    }

    private void paintAxis(Graphics2D graphics){
        // drawing axis
        int yAxisEndPoint = drawPointY - maxGraphHeight;
        int xAxisEndPoint = maxGraphWidth - drawPointX;
        graphics.drawLine(drawPointX, drawPointY, xAxisEndPoint, drawPointY);
        graphics.drawLine(drawPointX, drawPointY, drawPointX, yAxisEndPoint);
    }

    private void paintHorizontalGridlines(Graphics2D graphics){
        int level = 0;
        int verticalSpace = mapValueToHeight(verticalValueSpacing);
        int currentY = drawPointY;
        for (int space = 0; space <= numberOfVerticalSpaces; space++){
            String string = Integer.toString((int) (level * verticalValueSpacing));
            paintHorizontalGridline(graphics, currentY, string);
            level++;
            currentY -= verticalSpace;
        }
    }

    private void paintHorizontalGridline(Graphics2D graphics, int yValue, String yLabel){
        graphics.setPaint(Color.gray);
        graphics.drawLine(drawPointX, yValue, width - drawPointX, yValue);
        graphics.setPaint(Color.BLACK);
        graphics.drawString(yLabel, (float) (drawPointX* (1 - yLabel.length()*0.1)), yValue);
    }

    private void paintTitles(Graphics2D graphics2D){
        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        graphics2D.drawString(horizontalAxisLabel,(float) (width*(0.5 - (float) horizontalAxisLabel.length()/200)), (float) (height*0.98));
        graphics2D.drawString(titleLabel, (float) (width*(0.5 - (float) titleLabel.length()/200)), (float) ((drawPointY - maxGraphHeight)*0.5));
        graphics2D.setFont(createRotatedFont(24));
        graphics2D.drawString(verticalAxisLabel, (float) (drawPointX*0.35), (float) (drawPointY*0.6));
    }

    static Font createRotatedFont(int size){
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(-Math.PI/2, 0, 0);
        return font.deriveFont(affineTransform);
    }


}
