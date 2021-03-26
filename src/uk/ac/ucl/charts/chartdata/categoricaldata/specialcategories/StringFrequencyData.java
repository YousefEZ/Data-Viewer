package uk.ac.ucl.charts.chartdata.categoricaldata.specialcategories;

import uk.ac.ucl.charts.chartdata.categoricaldata.FrequencyData;
import uk.ac.ucl.charts.chartdata.categoricaldata.exceptions.TooManyValuesUnableToChart;

import java.util.List;

public class StringFrequencyData extends FrequencyData {

    public StringFrequencyData(String xAxis, List<String> xAxisData) throws TooManyValuesUnableToChart {
        super(xAxisData);
        setHorizontalLabel(xAxis);
        setVerticalLabel("Frequency");
        setTitleLabel("Frequency of each " + xAxis);
        setFrequencyToMatches();
        validateSize();
    }

    private boolean matchString(String string){
        for (int matchIndex = 0; matchIndex < matches.size(); matchIndex++) {
            if (matches.get(matchIndex).equals(string)) {
                frequency.set(matchIndex, frequency.get(matchIndex) + 1);
                return true;
            }
        }
        return false;
    }

    private void setFrequencyToMatches(){
        for (String string: xAxis) {
            if (!matchString(string)) {
                frequency.add(1.0);
                matches.add(string);
            }
        }
    }

}
