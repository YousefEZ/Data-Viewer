package uk.ac.ucl.dataframe;

import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

public class DataFrame {

    private List<String> columnNames;
    private List<List<String>> table; // 2-D ArrayList

    public DataFrame(){
        this.columnNames = new ArrayList<String>();
        this.table = new ArrayList<List<String>>();
    }

    public DataFrame(List<String> columnNames){
        this.table = new ArrayList<List<String>>();
        this.columnNames = new ArrayList<String>();
        for (String column: columnNames){
            addColumn(column);
        }

    }

    public List<List<String>> getTable(){
        return table;
    }

    public void addColumn(String columnName){
        columnNames.add(columnName);
        List<String> rows = new ArrayList<>();

        int numberOfRows = getRowCount();
        for (int i = 0; i < numberOfRows; i++){
            rows.add("");
        }

        table.add(rows);
    }

    public List<String> getColumnNames(){
        return columnNames;
    }

    // the number of rows in a column,
    // all columns should have the same number of rows when the frame is fully loaded with data
    public int getRowCount(){
        return (table.size() == 0) ? 0 : table.get(0).size();
    }

    private int getColumnIndex(String columnName) throws ColumnDoesNotExistException {
        for (int i = 0; i < columnNames.size(); i++){
            if (columnNames.get(i).equals(columnName)){
                return i;
            }
        }
        throw new ColumnDoesNotExistException();
    }

    public List<String> getColumn(String columnName) throws ColumnDoesNotExistException {
        int columnIndex = getColumnIndex(columnName);
        return table.get(columnIndex);
    }

    public List<String> getColumn(int columnIndex) throws ColumnDoesNotExistException {
        return table.get(columnIndex);
    }

    public String getValue(String columnName, int row) throws ColumnDoesNotExistException {
        int columnIndex = getColumnIndex(columnName);
        return table.get(columnIndex).get(row);
    }

    public void putValue(String columnName, int row, String value) throws ColumnDoesNotExistException {
        int columnIndex = getColumnIndex(columnName);
        table.get(columnIndex).set(row, value);
    }

    public void addValue(String columnName, String value) throws ColumnDoesNotExistException {
        int columnIndex = getColumnIndex(columnName);
        table.get(columnIndex).add(value);
    }

    public void addValue(int columnIndex, String value){
        table.get(columnIndex).add(value);
    }

    public String toString(){
        int numberRows = getRowCount();
        int numberColumns = columnNames.size();
        List<List<String>> rows = new ArrayList<List<String>>();
        for (int r = 0; r < numberRows; r++){
            ArrayList<String> row = new ArrayList<String>();
            for (int c = 0; c < numberColumns; c++){
                row.add(table.get(c).get(r));
            }
            rows.add(row);
        }
        return rows.toString();
    }
}
