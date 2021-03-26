package uk.ac.ucl.controller;

import uk.ac.ucl.controller.exceptions.InvalidDateStringException;

import java.util.ArrayList;
import java.util.List;

public class Find {

    public final static int NUMBER = 0;
    public final static int STRING = 1;
    public final static int DATE = 2;

    public final static int LARGEST = 0;
    public final static int SMALLEST = 1;

    public final static int CONTAINS = 2;
    public final static int MATCHES = 3;

    public final static int NEWEST = 4;
    public final static int OLDEST = 5;

    static List<Integer> findUnmatchedStringsIndices(List<String> allData, List<Integer> subsetIndices, String criteria) {
        List<Integer> matchedRows = new ArrayList<>();
        for (int row : subsetIndices) {
            if (!allData.get(row).equals(criteria)) {
                matchedRows.add(row);
            }
        }
        return matchedRows;
    }

    static List<Integer> findStringsNotContainingString(List<String> allData, List<Integer> subsetIndices, String criteria) {
        List<Integer> matchedRows = new ArrayList<>();
        for (int row : subsetIndices) {
            if (allData.get(row).length() < criteria.length() || !allData.get(row).contains(criteria)) {
                matchedRows.add(row);
            }
        }
        return matchedRows;
    }

    static int findLargestNumberIndex(List<String> allData, List<Integer> subsetIndices) {
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

    static int findSmallestNumberIndex(List<String> allData, List<Integer> subsetIndices) {
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

    // helper method.
    public static int[] sanitiseYearFormat(String date) throws InvalidDateStringException{
        String[] dateList = date.split("[/-]");

        if (dateList.length != 3) throw new InvalidDateStringException(date);

        if (dateList[0].length() <= 2){
            String day = dateList[0];
            dateList[0] = dateList[2];
            dateList[2] = day;
        }

        if (dateList[0].length() > 4 || dateList[1].length() > 2 || dateList[2].length() > 2){
            throw new InvalidDateStringException(date);
        }
        return new int[]{Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]), Integer.parseInt(dateList[2])};
    }

    static int findNewestIndex(List<String> allData, List<Integer> subsetIndices) throws InvalidDateStringException {
        int largestIndex = subsetIndices.get(0);
        int[] largestDate = sanitiseYearFormat(allData.get(largestIndex));

        for (int index : subsetIndices) {
            int[] date = sanitiseYearFormat(allData.get(index));
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

    static int findOldestIndex(List<String> allData, List<Integer> subsetIndices) throws InvalidDateStringException {
        int smallestIndex = subsetIndices.get(0);
        int[] smallestDate = sanitiseYearFormat(allData.get(smallestIndex));

        for (int index : subsetIndices) {
            int[] date = sanitiseYearFormat(allData.get(index));
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
