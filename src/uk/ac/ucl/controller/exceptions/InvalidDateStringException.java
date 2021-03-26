package uk.ac.ucl.controller.exceptions;

public class InvalidDateStringException extends Exception {

    private final String exceptionAt;

    public InvalidDateStringException(String date){
        super("Date String: \"" +  date + "\" is invalid.");
        exceptionAt = "Date String: \"" +  date + "\" is invalid.";
    }

    public String exceptionAt(){
        return exceptionAt;
    }


}
