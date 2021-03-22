package uk.ac.ucl.graphs.graphdata.bargraphs;

import java.util.List;

public class ColumnGraphData extends BarGraphData{

    public ColumnGraphData(String xAxis, String yAxis, List<String> xAxisData, List<String> yAxisData){
        super(xAxisData);
        setHorizontalLabel(xAxis);
        setVerticalLabel(yAxis);
        setTitleLabel(yAxis + " / " + xAxis);
        setFrequencyToColumnValues(yAxisData);
    }

    private void setFrequencyToColumnValues(List<String> values){
        this.matches = xAxis;
        for (String data: values){
            frequency.add(Double.parseDouble(data));
        }
    }

}
