package uk.ac.ucl.filehandlers.exceptions;

public class InvalidJSONFileFormat extends Exception {

    public InvalidJSONFileFormat(String contents){
        super();
        System.out.println(contents);
    }
}
