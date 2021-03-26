package uk.ac.ucl.charts.chartdata.categoricaldata.specialcategories;

import uk.ac.ucl.charts.chartdata.categoricaldata.exceptions.TooManyValuesUnableToChart;
import uk.ac.ucl.controller.Find;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.charts.chartdata.SortPlots;
import uk.ac.ucl.charts.chartdata.categoricaldata.FrequencyData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgeFrequencyData extends FrequencyData {

    public AgeFrequencyData(List<String> ages) throws InvalidDateStringException, TooManyValuesUnableToChart {
        super(ages);
        setHorizontalLabel("Age Groups");
        setVerticalLabel("Frequency");
        setTitleLabel("Age Distribution");
        setFrequencyToAge();
        validateSize();
    }

    private boolean ageExists(List<Double> ages, double age){
        for (int ageIndex = 0; ageIndex < ages.size(); ageIndex++) {
            if (ages.get(ageIndex) == age){
                frequency.set(ageIndex, frequency.get(ageIndex) + 1);
                return true;
            }
        }
        return false;
    }

    private int getYearDifference(int[] currentDate, String comparedDate) throws InvalidDateStringException {
        int[] compareDateValues = Find.sanitiseYearFormat(comparedDate);
        int age = currentDate[0] - compareDateValues[0];
        if (currentDate[1] > compareDateValues[1]){
            age++;
        } else if (currentDate[1] == compareDateValues[1] && currentDate[2] >= compareDateValues[2]){
            age++;
        }
        return age;
    }

    private void setFrequencyToAge() throws InvalidDateStringException {
        int[] currentDate = Find.sanitiseYearFormat(LocalDate.now().toString());
        List<Double> ages = new ArrayList<>();
        for (String date: xAxis){
            int age = (getYearDifference(currentDate, date)/5) * 5;
            if (!ageExists(ages, age)) {
                frequency.add(1.0);
                ages.add((double) age);
            }
        }
        transformMatchesToAgeGroups(ages, frequency);
    }

    private void transformMatchesToAgeGroups(List<Double> ages, List<Double> frequency) {
        SortPlots.quickSort(ages, frequency);
        for (double age: ages){
            matches.add((int) age + " - " + (int) (age + 5));
        }
    }

}
