package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class JSONReader {

    private DataFrame dataFrame;
    private String nextLine;
    private Scanner scanner;
    private boolean EOF;

    private JSONReader(String fileName) throws FileNotFoundException, InvalidJSONFileFormat {
        File file = new File(fileName);
        scanner = new Scanner(file);
        dataFrame = new DataFrame();

        nextLine = "";
        EOF = false;
        scanFile();

    }

    private void scanFile() throws InvalidJSONFileFormat {
        while (nextLine.indexOf('{') == -1){
            if (!scanner.hasNextLine()) throw new InvalidJSONFileFormat();
            nextLine = scanner.nextLine().trim();
        }

        while (!EOF){
            addColumn();
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

    // moves the scanner to the next column (key - value pair).
    private void adjustScannerToNextColumn() throws InvalidJSONFileFormat {
        if (nextLine.charAt(0) != ','){
            if (checkBrace()) return;
            nextLine = adjustScannerToRemoveLines().trim();
            if (checkBrace()) return;
            if (nextLine.charAt(0) != ',') throw new InvalidJSONFileFormat();
        }
        nextLine = nextLine.substring(1).trim();
    }

    private Boolean checkBrace(){
        if (nextLine.charAt(0) == '}'){
            EOF = true;
            return true;
        }
        return false;
    }

    private String collateField() throws InvalidJSONFileFormat {
        StringBuilder stringBuilder = new StringBuilder();
        while(nextLine.indexOf(']') == -1){
            nextLine = adjustScannerToRemoveLines().trim();
            String element = nextLine.indexOf(']') == -1 ? nextLine : nextLine.substring(0, nextLine.indexOf(']'));
            stringBuilder.append(element);
            if (EOF) throw new InvalidJSONFileFormat();
        }
        nextLine = nextLine.substring(nextLine.indexOf(']')+1).trim();
        if (nextLine.equals("")) nextLine = adjustScannerToRemoveLines();
        adjustScannerToNextColumn();
        return stringBuilder.toString();
    }

    private List<String> convertStringedArrayToArray(String array){
        List<String> rows = new ArrayList<>();
        List<String> keyValuePairs = fragmentRecord(array, ",");
        for (String keyValuePair: keyValuePairs){
            keyValuePair = keyValuePair.trim();
            rows.add(keyValuePair.substring(keyValuePair.indexOf('"')+1, keyValuePair.lastIndexOf('"')));
        }
        return rows;
    }

    private void addColumn() throws InvalidJSONFileFormat {

        List<String> fragmented = fragmentRecord(collateField(), ":");

        String columnName = fragmented.get(0);
        columnName = columnName.substring(columnName.indexOf('"')+1, columnName.lastIndexOf('"'));
        String rowsString = fragmented.get(1).trim();

        if (rowsString.charAt(0) != '['){
            throw new InvalidJSONFileFormat();
        }
        createColumn(columnName, rowsString);
    }

    private void createColumn(String columnName, String rowsString){
        dataFrame.addColumn(columnName);
        try {
            dataFrame.setRows(columnName, convertStringedArrayToArray(rowsString));
        } catch (ColumnDoesNotExistException e) {
            e.printStackTrace(); //should be impossible.
        }
    }

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

    static DataFrame read(String fileName) throws FileNotFoundException, InvalidJSONFileFormat {
        return new JSONReader(fileName).getDataFrame();
    }

}
