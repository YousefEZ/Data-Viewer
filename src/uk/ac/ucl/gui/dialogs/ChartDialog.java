package uk.ac.ucl.gui.dialogs;

import uk.ac.ucl.charts.axischarts.coordinatescharts.LineChart;
import uk.ac.ucl.charts.axischarts.coordinatescharts.ScatterChart;
import uk.ac.ucl.charts.chartdata.categoricaldata.exceptions.TooManyValuesUnableToChart;
import uk.ac.ucl.charts.chartdata.categoricaldata.specialcategories.AgeFrequencyData;
import uk.ac.ucl.charts.chartdata.categoricaldata.FrequencyData;
import uk.ac.ucl.charts.regularcharts.PieChart;
import uk.ac.ucl.controller.Controller;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.charts.axischarts.BarChart;
import uk.ac.ucl.charts.Chart;
import uk.ac.ucl.charts.chartdata.CoordinatesData;
import uk.ac.ucl.charts.chartdata.categoricaldata.SuppliedFrequencyData;
import uk.ac.ucl.charts.chartdata.categoricaldata.specialcategories.StringFrequencyData;
import uk.ac.ucl.gui.dialogs.chartdialogs.CategoryDialog;
import uk.ac.ucl.gui.dialogs.chartdialogs.CoordinatesDialog;

import javax.swing.*;
import java.util.List;

public class ChartDialog {

    private final static String[] CHARTS = {"Bar Chart", "Pie Chart", "Scatter Chart", "Line Chart"};
    private final static int BARCHART = 0;
    private final static int PIECHART = 1;
    private final static int SCATTERCHART = 2;
    private final static int LINECHART = 3;

    // public method that is run to get the Graph subclass.
    public static Chart getChart(JFrame frame, Controller controller) throws InvalidDateStringException, TooManyValuesUnableToChart {
        int chartType = matchChartTypeIndex(getChartType(frame));
        if (chartType < 0) return null;
        return getChartData(frame, chartType, controller);
    }

    // gets the graph type that the user wants.
    private static String getChartType(JFrame frame){
        return (String) JOptionPane.showInputDialog(frame,"Please select the type of Chart", "Select Chart",
                JOptionPane.PLAIN_MESSAGE,null, CHARTS, CHARTS[0]);
    }

    // maps the string to the graph option. BARCHART (0), PIECHART(1), SCATTERCHART (2), LINECHART(3).
    private static int matchChartTypeIndex(String chart){
        if (chart == null) return -2;
        for (int index = 0; index < CHARTS.length; index++){
            if (chart.equals(CHARTS[index])){
                return index;
            }
        }
        return -1;
    }

    // gets the graph based on the graph type that the user requested.
    private static Chart getChartData(JFrame frame, int chartType, Controller controller)
            throws InvalidDateStringException, TooManyValuesUnableToChart {
        switch (chartType){
            case BARCHART:
                FrequencyData frequencyData = getCategoryData(frame, controller);
                return frequencyData == null ? null : new BarChart(frequencyData);
            case PIECHART:
                frequencyData = getCategoryData(frame, controller);
                return frequencyData == null ? null : new PieChart(frequencyData);
            case SCATTERCHART:
                CoordinatesData graphData = getCoordinatesChartData(frame, controller);
                return graphData == null ? null : new ScatterChart(graphData);
            case LINECHART:
                graphData = getCoordinatesChartData(frame, controller);
                return graphData == null ? null : new LineChart(graphData);
            default:
                throw new IllegalStateException("Unexpected value: " + chartType);
        }
    }

    // gets the data required to display a Line Graph or a Scatter Graph.
    private static CoordinatesData getCoordinatesChartData(JFrame frame, Controller controller){
        List<String> columnNames = controller.getColumnNames();
        CoordinatesDialog dialog = new CoordinatesDialog(frame, columnNames);
        // if submitted then return the Points Graph Data.
        return (dialog.getResponse() == CoordinatesDialog.SUBMITTED) ? createCoordinatesChartData(dialog, controller) : null;
    }

    // creates the points graph data based on the data from the user and the model (in controller).
    private static CoordinatesData createCoordinatesChartData(CoordinatesDialog dialog, Controller controller){
        return new CoordinatesData(controller.getColumnName(dialog.getXAxis()),
                controller.getColumnName(dialog.getYAxis()),
                controller.getColumnData(dialog.getXAxis()),
                controller.getColumnData(dialog.getYAxis()));
    }

    // creates a BarGraphData based on user input.
    private static FrequencyData getCategoryData(JFrame frame, Controller controller)
            throws InvalidDateStringException, TooManyValuesUnableToChart {
        List<String> columnNames = controller.getColumnNames();
        CategoryDialog dialog = new CategoryDialog(frame, columnNames);
        if (dialog.getResponse() == CategoryDialog.SUBMITTED) {
            return getSpecialCategoryData(dialog, controller);
        }
        return null;
    }

    // creates a specialised bar graph data based on the user's input.
    private static FrequencyData getSpecialCategoryData(CategoryDialog input, Controller controller)
            throws InvalidDateStringException, TooManyValuesUnableToChart {
        int xAxisColumnIndex = input.getXAxis();
        int yAxisColumnIndex = input.getYAxis();
        switch (input.getMatchType()){
            case CategoryDialog.YAXIS:
                return new SuppliedFrequencyData(controller.getColumnName(xAxisColumnIndex), controller.getColumnName(yAxisColumnIndex),
                        controller.getColumnData(xAxisColumnIndex), controller.getColumnData(yAxisColumnIndex));
            case CategoryDialog.FREQUENCY:
                return new StringFrequencyData(controller.getColumnName(xAxisColumnIndex), controller.getColumnData(xAxisColumnIndex));
            case CategoryDialog.AGE:
                return new AgeFrequencyData(controller.getColumnData(xAxisColumnIndex));
            default:
                throw new IllegalStateException("Unexpected value: " + input.getMatchType());
        }
    }
}
