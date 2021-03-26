package uk.ac.ucl.charts.chartdata;

import java.util.List;

public class SortPlots {

    private static void swapPoint(List<Double> sortableValues, List<Double> correspondingValues, int index0, int index1) {
        double tempX = sortableValues.get(index0);
        sortableValues.set(index0, sortableValues.get(index1));
        sortableValues.set(index1, tempX);

        double tempY = correspondingValues.get(index0);
        correspondingValues.set(index0, correspondingValues.get(index1));
        correspondingValues.set(index1, tempY);
    }

    private static int partition(List<Double> sortableValues, List<Double>  correspondingValues, int low, int high) {
        double pivot = sortableValues.get(high);
        // smaller element index
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {
            // check if current element is less than or equal to pivot
            if (sortableValues.get(j) <= pivot) {
                i++;
                // swap the elements
                swapPoint(sortableValues, correspondingValues, i, j);
            }
        }
        // swap numArray[i+1] and numArray[high] (or pivot)
        swapPoint(sortableValues, correspondingValues, i+1, high);
        return i + 1;
    }


    public static void quickSort(List<Double> sortableValues, List<Double> correspondingValues) {
        //auxiliary stack
        int high = sortableValues.size()-1;
        int low;

        int[] stack = new int[high+1];

        stack[0] = 0;
        stack[1] = high;
        int top = 1;
        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];

            // partition the array.
            int pivot = partition(sortableValues, correspondingValues, low, high);

            if (pivot - 1 > low) {
                stack[++top] = low;
                stack[++top] = pivot - 1;
            }
            if (pivot + 1 < high) {
                stack[++top] = pivot + 1;
                stack[++top] = high;
            }
        }
    }

}
