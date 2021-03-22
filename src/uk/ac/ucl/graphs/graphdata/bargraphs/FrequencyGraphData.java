package uk.ac.ucl.graphs.graphdata.bargraphs;

import java.util.List;

public class FrequencyGraphData extends BarGraphData {

    public FrequencyGraphData(String xAxis, List<String> xAxisData){
        super(xAxisData);
        setHorizontalLabel(xAxis);
        setVerticalLabel("Frequency");
        setTitleLabel("Frequency of each " + xAxis);
        setFrequencyToMatches();
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
