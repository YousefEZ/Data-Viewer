package uk.ac.ucl.controller;

import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;
import uk.ac.ucl.model.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private Model model; // the model as to where the data should be gotten from.
    private List<Integer> displayedRows; // the rows that are to be displayed on the GUI.
    private List<Boolean> displayedColumns; // the columns that are to be displayed on the GUI.

    public Controller(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        this.model = new Model(fileName);
        resetRows();
        resetColumns();
    }

    // show any hidden rows.
    public void resetRows(){
        int rowCount = model.getRowCount();
        displayedRows = new ArrayList<>();
        for (int row = 0; row < rowCount; row++){
            displayedRows.add(row);
        }
    }

    // show any hidden columns.
    public void resetColumns(){
        int columnCount = model.getColumnNames().size();
        displayedColumns = new ArrayList<>();
        for (int column = 0; column < columnCount; column++){
            displayedColumns.add(true);
        }
    }

    public int getRowCount(){
        return displayedRows.size();
    }

    public int getColumnIndex(String columnName){
        return model.getColumnIndex(columnName);
    }

    public void showColumn(int columnIndex) {
        displayedColumns.set(columnIndex, true);
    }

    public void hideColumn(int columnIndex) {
        displayedColumns.set(columnIndex, false);
    }

    public List<Boolean> getDisplayedColumns(){
        return displayedColumns;
    }

    public List<String> getColumnNames(){
        List<String> displayedColumnNames = new ArrayList<>();
        List<String> allColumnNames = model.getColumnNames();
        int columnCount = allColumnNames.size();

        for (int column = 0; column < columnCount; column++){
            if (displayedColumns.get(column)){
                displayedColumnNames.add(allColumnNames.get(column));
            }
        }
        return displayedColumnNames;
    }

    public List<String> getAllColumnNames(){
        return model.getColumnNames();
    }

    public List<String> getColumnData(int columnIndex){
        List<String> columnData = model.getColumnData(columnIndex);
        List<String> displayedColumnData = new ArrayList<>();
        for (int row: displayedRows){
            displayedColumnData.add(columnData.get(row));
        }
        return displayedColumnData;
    }

    public String getColumnName(int columnIndex) {
        return getColumnNames().get(columnIndex);
    }

    // hides any displayed rows that does not match the text given at the columnIndex given.
    public void configureRowsToMatch(int columnIndex, String text){
        List<String> columnData = model.getColumnData(columnIndex);
        displayedRows.removeAll(Find.findMatchedStringsIndices(columnData, displayedRows, text));
    }

    // hides all rows except the data that fulfill the criteria of the operation (i.e. largest -> largest value)
    public void configureRowsToFindOperation(int columnIndex, int searchType, int operation) throws InvalidDateStringException {
        int row = matchSearchType(columnIndex, searchType, operation);
        displayedRows.clear();
        displayedRows.add(row);
    }

    // map the search type to the method.
    private int matchSearchType(int columnIndex, int searchType, int operation) throws InvalidDateStringException {
        switch (searchType){
            case Find.NUMBER:
                return searchNumber(columnIndex, operation);
            case Find.DATE:
                return searchYear(columnIndex, operation);
            default:
                throw new IllegalStateException("Unexpected value: " + searchType);
        }
    }

    // map the operation to the method in the Find helper class.
    private int searchNumber(int columnIndex, int operation){
        List<String> columnData = model.getColumnData(columnIndex);
        int index = -1;
        if (operation == Find.LARGEST) {
            index = Find.findLargestNumberIndex(columnData, displayedRows);
        } else if (operation == Find.SMALLEST) {
            index = Find.findSmallestNumberIndex(columnData, displayedRows);
        }
        return index;
    }

    // map the operation to the method in the Find helper class.
    private int searchYear(int columnIndex, int operation) throws InvalidDateStringException {
        List<String> columnData = model.getColumnData(columnIndex);

        int index = -1;
        if (operation == Find.LARGEST){
            index = Find.findLargestYearIndex(columnData, displayedRows);
        } else if (operation == Find.SMALLEST){
            index = Find.findSmallestYearIndex(columnData, displayedRows);
        }
        return index;
    }

    // exports the data into JSON
    public void exportToJSON(String filename) throws IOException {
        model.exportToJSON(filename);
    }
}
