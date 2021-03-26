package uk.ac.ucl.charts.chartdata;

import java.util.ArrayList;
import java.util.List;

// container for data that is used for sub classes of PointGraph.
public class CoordinatesData extends ChartData {

    private List<Double> xValues;
    private List<Double> yValues;

    public CoordinatesData(String horizontalLabel, String verticalLabel, List<String> xValues, List<String> yValues){
        this.xValues = convertListStringToDouble(xValues);
        this.yValues = convertListStringToDouble(yValues);
        setHorizontalLabel(horizontalLabel);
        setVerticalLabel(verticalLabel);
        setTitleLabel(verticalLabel + " / " + horizontalLabel);
    }

    // creates a new list where all the strings in a list of values are doubles.
    private static List<Double> convertListStringToDouble(List<String> values){
        List<Double> valuesInDouble = new ArrayList<>();
        for (String value: values){
            valuesInDouble.add(Double.parseDouble(value));
        }
        return valuesInDouble;
    }

    public List<Double> getXValues(){
        return xValues;
    }

    public List<Double> getYValues(){
        return yValues;
    }

}
