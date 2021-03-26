package uk.ac.ucl.charts.chartdata.categoricaldata;

import uk.ac.ucl.charts.chartdata.categoricaldata.exceptions.TooManyValuesUnableToChart;
import uk.ac.ucl.charts.chartdata.ChartData;

import java.util.ArrayList;
import java.util.List;


public class FrequencyData extends ChartData {
    protected List<Double> frequency;
    protected List<String> matches;
    protected List<String> xAxis;

    protected FrequencyData(List<String> xAxis) {
        super();
        this.xAxis = xAxis;
        frequency = new ArrayList<>();
        matches = new ArrayList<>();
    }

    // <------------ Getters  --------------->

    public List<String> getLabels(){
        return matches;
    }

    public List<Double> getFrequency(){
        return frequency;
    }

    protected void validateSize() throws TooManyValuesUnableToChart {
        if (this.matches.size() > 30) throw new TooManyValuesUnableToChart(); // maximum number of labels.
    }

}
