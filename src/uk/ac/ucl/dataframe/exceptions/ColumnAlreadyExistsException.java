package uk.ac.ucl.dataframe.exceptions;

public class ColumnAlreadyExistsException extends Exception {

    private final String columnName;

    public ColumnAlreadyExistsException(String columnName){
        super("The column already exists");
        this.columnName = columnName;
    }

    public String getColumn() {
        return columnName;
    }
}