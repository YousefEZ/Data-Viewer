package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class JSONReader {

    private DataFrame dataFrame;
    private String nextLine;
    private Scanner scanner;
    private boolean EOF;

    private JSONReader(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        File file = new File(fileName);
        scanner = new Scanner(file);
        dataFrame = new DataFrame();

        nextLine = "";
        EOF = false;
        scanFile();
    }

    // scans file and begins adding columns to the dataFrame.
    private void scanFile() throws InvalidJSONFileFormat, ColumnAlreadyExistsException {
        while (nextLine.indexOf('{') == -1){
            if (!scanner.hasNextLine()) throw new InvalidJSONFileFormat();
            nextLine = scanner.nextLine().trim();
        }
        while (!EOF){
            createNextColumn();
        }
        scanner.close();
    }

    // moves the scanner to a line where it's not empty.
    private String adjustScannerToRemoveLines(){
        nextLine = "";
        while(nextLine.equals("")){
            if (!scanner.hasNextLine()){
                EOF = true;
                break;
            }
            nextLine = scanner.nextLine().trim();
        }
        return nextLine;
    }

    /** moves the scanner to begin reading the next column.
     *
     * @throws InvalidJSONFileFormat if the reader detects that the file doesn't follow the required format.
     */
    private void adjustScannerToNextColumn() throws InvalidJSONFileFormat {
        if (nextLine.charAt(0) != ','){
            if (checkBrace()) return;
            nextLine = adjustScannerToRemoveLines().trim();
            if (checkBrace()) return;
            if (nextLine.charAt(0) != ',') throw new InvalidJSONFileFormat();
        }
        nextLine = nextLine.substring(1).trim();
    }

    // checks if there is a right brace that signals the end of file (EOF).
    private Boolean checkBrace(){
        if (nextLine.charAt(0) == '}'){
            EOF = true;
            return true;
        }
        return false;
    }

    /** Gathers all the elements in the array and places it onto one line, for easier parsing.
     *
     * @return String that holds the key and value in one line.
     * @throws InvalidJSONFileFormat if the reader detects that the file doesn't follow the required format.
     */
    private String collateField() throws InvalidJSONFileFormat {
        StringBuilder stringBuilder = new StringBuilder();
        while(nextLine.indexOf(']') == -1){
            // while the scanner hasn't reached the line containing the close array symbol ']'
            nextLine = adjustScannerToRemoveLines().trim();
            // if this line contains close array then only take the substring from 0 to that index.
            String element = nextLine.indexOf(']') == -1 ? nextLine : nextLine.substring(0, nextLine.indexOf(']'));
            stringBuilder.append(element);
            if (EOF) throw new InvalidJSONFileFormat(); // if we have reached the EOF without locating ']', throw error.
        }
        nextLine = nextLine.substring(nextLine.indexOf(']')+1).trim(); // take the substring after the ']'
        if (nextLine.equals("")) nextLine = adjustScannerToRemoveLines(); // if its empty get the next existing line.
        adjustScannerToNextColumn();
        return stringBuilder.toString();
    }

    /** Reads the next line(s), gathers the data, and places it into a Column.
     *
     * @throws InvalidJSONFileFormat if the reader detects that the file doesn't follow the required format.
     */
    private void createNextColumn() throws InvalidJSONFileFormat, ColumnAlreadyExistsException {

        List<String> fragmented = fragmentRecord(collateField(), ":"); // splits the line to key and value.

        String columnName = fragmented.get(0); // the key is the name of the column.
        columnName = columnName.substring(columnName.indexOf('"')+1, columnName.lastIndexOf('"')); // discard ""
        String rowsString = fragmented.get(1).trim();

        // if the first character isn't the start of the array then throw an error as being invalid.
        if (rowsString.charAt(0) != '['){
            throw new InvalidJSONFileFormat();
        }
        createColumn(columnName, rowsString);
    }

    /** creates a column in the dataFrame.
     *
     * @param columnName creates a column in the dataFrame with the given name.
     * @param rowsString all rows in a column in one string, that gets converted into a List and put in the dataFrame.
     */
    private void createColumn(String columnName, String rowsString) throws ColumnAlreadyExistsException {
        dataFrame.addColumn(columnName);
        List<String> rows = convertStringedArrayToArray(rowsString);
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++){
            dataFrame.putValue(columnName, rowIndex, rows.get(rowIndex));
        }
    }

    /** Converts an array that is in a form of a String "[row,row,...]" => {row, row, ...} into a List of type String.
     *
     * @param array one large string that represents an array. It gets split on the comma delimiter.
     * @return List of type String that contains all the strings.
     */
    private List<String> convertStringedArrayToArray(String array){
        List<String> rows = new ArrayList<>();
        List<String> listElements = fragmentRecord(array, ",");
        for (String element: listElements){
            element = element.trim();
            rows.add(element.substring(element.indexOf('"')+1, element.lastIndexOf('"')));
        }
        return rows;
    }

    /** breaks the string based on the delimiter given, and returns it in a list of type String.
     *
     * @param record a line that needs to be split based on the delimiter
     * @param delimiter a string that indicates when to split the record.
     * @return List of type string that holds all the fragmented strings.
     */
    private static List<String> fragmentRecord(String record, String delimiter){
        List<String> fields = new ArrayList<>();
        int startIndexOfCurrentWord = 0;

        for (int currentIndex = 0; currentIndex < record.length()-delimiter.length()+1; currentIndex++){
            if (record.substring(currentIndex, currentIndex + delimiter.length()).equals(delimiter)) {
                fields.add(record.substring(startIndexOfCurrentWord, currentIndex));
                startIndexOfCurrentWord =  currentIndex + delimiter.length();
                currentIndex = currentIndex + delimiter.length() - 1;
            }
        }
        fields.add(record.substring(startIndexOfCurrentWord));
        return fields;
    }

    private DataFrame getDataFrame(){
        return dataFrame;
    }

    // public method that instantiates the class and gets the dataFrame.
    static DataFrame read(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        return new JSONReader(fileName).getDataFrame();
    }

}
