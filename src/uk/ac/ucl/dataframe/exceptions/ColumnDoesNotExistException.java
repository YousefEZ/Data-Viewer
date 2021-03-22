package uk.ac.ucl.dataframe.exceptions;

public class ColumnDoesNotExistException extends RuntimeException {

    public ColumnDoesNotExistException(){
        super("The column given does not exist");
    }

}
