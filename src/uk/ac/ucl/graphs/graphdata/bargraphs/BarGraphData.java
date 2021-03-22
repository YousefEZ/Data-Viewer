package uk.ac.ucl.graphs.graphdata.bargraphs;

import uk.ac.ucl.graphs.graphdata.GraphData;

import java.util.ArrayList;
import java.util.List;


public class BarGraphData extends GraphData {
    List<Double> frequency;
    List<String> matches;
    List<String> xAxis;

    BarGraphData(List<String> xAxis) {
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

}
