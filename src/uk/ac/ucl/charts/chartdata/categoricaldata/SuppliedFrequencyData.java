package uk.ac.ucl.charts.chartdata.categoricaldata;

import uk.ac.ucl.charts.chartdata.categoricaldata.exceptions.TooManyValuesUnableToChart;

import java.util.List;

public class SuppliedFrequencyData extends FrequencyData {

    public SuppliedFrequencyData(String xAxis, String yAxis, List<String> xAxisData, List<String> yAxisData)
            throws TooManyValuesUnableToChart {
        super(xAxisData);
        setHorizontalLabel(xAxis);
        setVerticalLabel(yAxis);
        setTitleLabel(yAxis + " / " + xAxis);
        setFrequencyToColumnValues(yAxisData);
        validateSize();
    }

    private void setFrequencyToColumnValues(List<String> values){
        this.matches = xAxis;
        for (String data: values){
            frequency.add(Double.parseDouble(data));
        }
    }

}
