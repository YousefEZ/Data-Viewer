package uk.ac.ucl.gui.dialogs;

import uk.ac.ucl.graphs.LineGraph;
import uk.ac.ucl.graphs.ScatterGraph;
import uk.ac.ucl.graphs.graphdata.bargraphs.AgeGraphData;
import uk.ac.ucl.graphs.graphdata.bargraphs.BarGraphData;
import uk.ac.ucl.controller.Controller;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.graphs.BarGraph;
import uk.ac.ucl.graphs.Graph;
import uk.ac.ucl.graphs.graphdata.PointGraphData;
import uk.ac.ucl.graphs.graphdata.bargraphs.ColumnGraphData;
import uk.ac.ucl.graphs.graphdata.bargraphs.FrequencyGraphData;
import uk.ac.ucl.gui.dialogs.graphdialogs.BarGraphDialog;
import uk.ac.ucl.gui.dialogs.graphdialogs.PointGraphDialog;

import javax.swing.*;
import java.util.List;

public class GraphDialog {

    private final static String[] GRAPHS = {"Bar Graph", "Scatter Graph", "Line Graph"};
    private final static int BARGRAPH = 0;
    private final static int SCATTERGRAPH = 1;
    private final static int LINEGRAPH = 2;

    // public method that is run to get the Graph subclass.
    public static Graph getGraph(JFrame frame, Controller controller) throws InvalidDateStringException {
        int graphType = matchGraphTypeIndex(getGraphType(frame));
        if (graphType < 0) return null;
        return getGraphData(frame, graphType, controller);
    }

    // gets the graph type that the user wants.
    private static String getGraphType(JFrame frame){
        return (String) JOptionPane.showInputDialog(frame,"Please select the type of Graph", "Select Graph",
                JOptionPane.PLAIN_MESSAGE,null, GRAPHS, GRAPHS[0]);
    }

    // maps the string to the graph option. BARGRAPH (0), SCATTERGRAPH (1), LINEGRAPH(2).
    private static int matchGraphTypeIndex(String graph){
        if (graph == null) return -2;
        for (int index = 0; index < GRAPHS.length; index++){
            if (graph.equals(GRAPHS[index])){
                return index;
            }
        }
        return -1;
    }

    // gets the graph based on the graph type that the user requested.
    private static Graph getGraphData(JFrame frame, int graphType, Controller controller) throws InvalidDateStringException {
        switch (graphType){
            case BARGRAPH:
                BarGraphData barGraphData = getBarGraphData(frame, controller);
                return barGraphData == null ? null : new BarGraph(barGraphData);
            case SCATTERGRAPH:
                PointGraphData graphData = getPointsGraphData(frame, controller);
                return graphData == null ? null : new ScatterGraph(graphData);
            case LINEGRAPH:
                graphData = getPointsGraphData(frame, controller);
                return graphData == null ? null : new LineGraph(graphData);
            default:
                throw new IllegalStateException("Unexpected value: " + graphType);
        }
    }

    // gets the data required to display a Line Graph or a Scatter Graph.
    private static PointGraphData getPointsGraphData(JFrame frame, Controller controller){
        List<String> columnNames = controller.getColumnNames();
        PointGraphDialog dialog = new PointGraphDialog(frame, columnNames);
        // if submitted then return the Points Graph Data.
        return (dialog.getResponse() == PointGraphDialog.SUBMITTED) ? createPointGraphData(dialog, controller) : null;
    }

    // creates the points graph data based on the data from the user and the model (in controller).
    private static PointGraphData createPointGraphData(PointGraphDialog dialog, Controller controller){
        return new PointGraphData(controller.getColumnName(dialog.getXAxis()),
                controller.getColumnName(dialog.getYAxis()),
                controller.getColumnData(dialog.getXAxis()),
                controller.getColumnData(dialog.getYAxis()));
    }

    // creates a BarGraphData based on user input.
    private static BarGraphData getBarGraphData(JFrame frame, Controller controller) throws InvalidDateStringException {
        List<String> columnNames = controller.getColumnNames();
        BarGraphDialog dialog = new BarGraphDialog(frame, columnNames);
        if (dialog.getResponse() == BarGraphDialog.SUBMITTED) {
            return getSpecialBarGraphData(dialog, controller);
        }
        return null;
    }

    // creates a specialised bar graph data based on the user's input.
    private static BarGraphData getSpecialBarGraphData(BarGraphDialog input, Controller controller) throws InvalidDateStringException {
        int xAxisColumnIndex = input.getXAxis();
        int yAxisColumnIndex = input.getYAxis();
        switch (input.getMatchType()){
            case BarGraphDialog.YAXIS:
                return new ColumnGraphData(controller.getColumnName(xAxisColumnIndex), controller.getColumnName(yAxisColumnIndex),
                        controller.getColumnData(xAxisColumnIndex), controller.getColumnData(yAxisColumnIndex));
            case BarGraphDialog.FREQUENCY:
                return new FrequencyGraphData(controller.getColumnName(xAxisColumnIndex), controller.getColumnData(xAxisColumnIndex));
            case BarGraphDialog.AGE:
                return new AgeGraphData(controller.getColumnData(xAxisColumnIndex));
            default:
                throw new IllegalStateException("Unexpected value: " + input.getMatchType());
        }
    }
}
