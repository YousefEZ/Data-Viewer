package uk.ac.ucl.controller;

import java.util.ArrayList;
import java.util.List;

public class DataMatcher {


    public final static int NUMBER = 0;
    public final static int DATEYEAR = 1;
    public final static int DATEDAY = 2;

    public final static int LARGEST = 0;
    public final static int SMALLEST = 1;

    public final static String[] FINDTYPES = {"NUMBER", "YYYY/MM/DD", "DD/MM/YYYY"};
    public final static int[] FINDVALUES  = {NUMBER, DATEYEAR, DATEDAY};
    public final static String[] OPERATIONS = {"LARGEST", "SMALLEST"};
    public final static int[] OPERATIONVALUES  = {NUMBER, DATEYEAR, DATEDAY};

    static List<Integer> getMatchedPhraseIndices(List<String> allData, List<Integer> subsetIndices, String criteria) {
        List<Integer> matchedRows = new ArrayList<>();
        for (int row : subsetIndices) {
            if (!allData.get(row).equals(criteria)) {
                matchedRows.add(row);
            }
        }
        return matchedRows;
    }

    static int getLargestNumberIndex(List<String> allData, List<Integer> subsetIndices) {
        int largestIndex = subsetIndices.get(0);
        int largestNumber = Integer.parseInt(allData.get(largestIndex));
        for (int index : subsetIndices) {
            int element = Integer.parseInt(allData.get(index));
            if (element > largestNumber) {
                largestNumber = element;
                largestIndex = index;
            }
        }
        return largestIndex;
    }

    static int getSmallestNumberIndex(List<String> allData, List<Integer> subsetIndices) {
        int smallestIndex = subsetIndices.get(0);
        int smallestNumber = Integer.parseInt(allData.get(smallestIndex));
        for (int index : subsetIndices) {
            int element = Integer.parseInt(allData.get(index));
            if (element < smallestNumber) {
                smallestNumber = element;
                smallestIndex = index;
            }
        }
        return smallestIndex;
    }

    private static int[] sanitiseYearFormat(String date, int format) {
        String[] dateList = date.split("[/-]");

        if (format == DATEDAY) {
            String day = dateList[0];
            dateList[0] = dateList[2];
            dateList[2] = day;
        }

        return new int[]{Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]), Integer.parseInt(dateList[2])};
    }

    static int getLargestYearIndex(List<String> allData, List<Integer> subsetIndices, int yearFormat) {
        int largestIndex = subsetIndices.get(0);
        int[] largestDate = DataMatcher.sanitiseYearFormat(allData.get(largestIndex), yearFormat);

        for (int index : subsetIndices) {
            int[] date = DataMatcher.sanitiseYearFormat(allData.get(index), yearFormat);
            for (int dateIndex = 0; dateIndex < 3; dateIndex++) {
                if (date[dateIndex] > largestDate[dateIndex]) {
                    largestDate = date;
                    largestIndex = index;
                } else if (date[dateIndex] < largestDate[dateIndex]) {
                    break;
                }
            }
        }
        return largestIndex;
    }

    static int getSmallestYearIndex(List<String> allData, List<Integer> subsetIndices, int yearFormat) {
        int smallestIndex = subsetIndices.get(0);
        int[] smallestDate = DataMatcher.sanitiseYearFormat(allData.get(smallestIndex), yearFormat);

        for (int index : subsetIndices) {
            int[] date = DataMatcher.sanitiseYearFormat(allData.get(index), yearFormat);
            for (int dateIndex = 0; dateIndex < 3; dateIndex++) {
                if (date[dateIndex] < smallestDate[dateIndex]) {
                    smallestDate = date;
                    smallestIndex = index;
                } else if (date[dateIndex] > smallestDate[dateIndex]) {
                    break;
                }
            }
        }
        return smallestIndex;
    }



}
