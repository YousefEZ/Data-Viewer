package uk.ac.ucl.dataframe;

import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

public class DataFrame {

    private List<Column> columns;

    public DataFrame(){
        this.columns = new ArrayList<>();
    }

    public DataFrame(String[] columnNames) throws ColumnAlreadyExistsException {
        this.columns = new ArrayList<>();
        for (String column: columnNames){
            addColumn(column);
        }
    }

    public List<String> getColumnNames(){
        List<String> columnNames = new ArrayList<>();
        for (Column column: columns){
            columnNames.add(column.getName());
        }
        return columnNames;
    }

    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    public void addColumn(String columnName) throws ColumnAlreadyExistsException {
        if (getColumnIndex(columnName) != -1) {
            throw new ColumnAlreadyExistsException(columnName);
        }
        this.columns.add(new Column(columnName));
    }

    public int getRowCount(){
        return this.columns.get(0).getSize();
    }

    private int getColumnIndex(String columnName){
        for (int i = 0; i < columns.size(); i++){
            if (columns.get(i).getName().equals(columnName)){
                return i;
            }
        }
        return -1;
    }

    /** @noinspection unused*/
    public String getValue(String columnName, int row) throws ColumnDoesNotExistException{
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1){
            throw new ColumnDoesNotExistException();
        }
        return getValue(columnIndex, row);
    }

    public String getValue(int columnIndex, int row) throws ColumnDoesNotExistException {
        return columns.get(columnIndex).getRowValue(row);
    }

    /** @noinspection unused*/
    public void putValue(String columnName, int row, String value) throws ColumnDoesNotExistException {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1) throw new ColumnDoesNotExistException();

        columns.get(columnIndex).setRowValue(row, value);
    }

    public void addValue(int columnIndex, String value){
        columns.get(columnIndex).addRowValue(value);
    }

    public void addValue(String columnName, String value) throws ColumnDoesNotExistException{
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1){
            throw new ColumnDoesNotExistException();
        }
        addValue(columnIndex, value);
    }

    public List<String> getRows(int columnIndex) {
        int rowCount = getRowCount();
        List<String> rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++){
            rows.add(getValue(columnIndex, rowIndex));
        }
        return rows;
    }
}
