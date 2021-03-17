package uk.ac.ucl.controller;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;
import uk.ac.ucl.model.Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private Model model;
    private List<Integer> displayedRows;
    private List<Boolean> displayedColumns;

    public Controller(String fileName) throws FileNotFoundException {
        this.model = new Model(fileName);
        resetRows();
        resetColumns();
    }

    public void resetRows(){
        int rowCount = model.getRowCount();
        displayedRows = new ArrayList<>();
        for (int row = 0; row < rowCount; row++){
            displayedRows.add(row);
        }
    }

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

    public void configureRowsToMatch(int columnIndex, String text){
        List<String> columnData = model.getColumnData(columnIndex);
        displayedRows.removeAll(DataMatcher.getMatchedPhraseIndices(columnData, displayedRows, text));
    }

    public void configureRowsToMatch(String columnName, String text) throws ColumnDoesNotExistException {
        configureRowsToMatch(model.getColumnIndex(columnName), text);
    }

    public void configureRowsToMatch(int columnIndex, int searchType, int operation) {

        int index = -1;
        if (searchType == DataMatcher.NUMBER){
            index = searchNumber(columnIndex, operation);
        } else if (searchType == DataMatcher.DATEYEAR || searchType == DataMatcher.DATEDAY){
            index = searchYear(columnIndex, operation, searchType);
        }
        assert(index != -1);
        displayedRows.clear();
        displayedRows.add(index);
    }

    private int searchNumber(int columnIndex, int operation){
        List<String> columnData = model.getColumnData(columnIndex);
        int index = -1;
        if (operation == DataMatcher.LARGEST) {
            index = DataMatcher.getLargestNumberIndex(columnData, displayedRows);
        } else if (operation == DataMatcher.SMALLEST) {
            index = DataMatcher.getSmallestNumberIndex(columnData, displayedRows);
        }
        return index;
    }

    private int searchYear(int columnIndex, int operation, int yearFormat){
        List<String> columnData = model.getColumnData(columnIndex);

        int index = -1;
        if (operation == DataMatcher.LARGEST){
            index = DataMatcher.getLargestYearIndex(columnData, displayedRows, yearFormat);
        } else if (operation == DataMatcher.SMALLEST){
            index = DataMatcher.getSmallestYearIndex(columnData, displayedRows, yearFormat);
        }
        return index;
    }

    public void configureRowsToMatch(String columnName, int searchType, int operation) throws ColumnDoesNotExistException {
        configureRowsToMatch(model.getColumnIndex(columnName), searchType, operation);
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

    public DataFrame getDataFrame() {
        return model.getDataFrame();
    }
}
