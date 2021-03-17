package uk.ac.ucl.dataframe.exceptions;

public class ColumnDoesNotExistException extends Exception {

    public ColumnDoesNotExistException(){
        super("The column given does not exist");
    }

}
