package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

class JSONReader {

    private DataFrame dataFrame;
    private JSONBuffer buffer;
    private String currentColumn;
    private boolean bufferMustStartWithComma;

    private final String textRegex = "([^\"]|.)*";

    private JSONReader(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        buffer = new JSONBuffer(fileName);
        dataFrame = new DataFrame();
        scanFile();
    }

    // scans file and begins adding columns to the dataFrame.
    private void scanFile() throws InvalidJSONFileFormat, ColumnAlreadyExistsException {
        buffer.loadNextIntoBuffer();
        if (!buffer.startsWithChar('{')) throw new InvalidJSONFileFormat(buffer.getContents());
        buffer.discardFirstCharacter();

        while (!buffer.reachedEOF()) {
            loadNextColumn();
            adjustScannerToNextColumn();
        }
        buffer.close();
    }

    // <--------------- Parsing ---------------->

    private void loadNextColumn() throws ColumnAlreadyExistsException, InvalidJSONFileFormat {
        List<String> fields = fragmentRecord(loadNextKey(), ":"); // split on colon
        currentColumn = fields.get(0);
        String stringRegex = "\"" + textRegex + "\"[ \n\t]*";
        if (!currentColumn.matches(stringRegex)) throw new InvalidJSONFileFormat(buffer.getContents()); // needs to conform to regex.

        // strip the start and final ", and add the column name to the DataFrame.
        currentColumn = currentColumn.substring(currentColumn.indexOf('"'), currentColumn.lastIndexOf('"'));
        dataFrame.addColumn(currentColumn);
        buffer.setContents(fields.get(1));
        collateArray();
    }

    private String loadNextKey() {
        buffer.loadNextIfBufferEmpty();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buffer.getContents());
        while (buffer.excludes(":")) {
            // keep collecting the data until we hit the ':' token, which indicates the end of column name.
            buffer.loadNextIntoBuffer();
            stringBuilder.append(buffer.getContents());
        }
        return stringBuilder.toString(); // convert it to a string.
    }

    // collects the array data and places it directly into the DataFrame.
    private void collateArray() throws InvalidJSONFileFormat {
        buffer.loadNextIfBufferEmpty();
        if (!buffer.startsWithChar('[')) throw new InvalidJSONFileFormat(buffer.getContents());

        buffer.discardFirstCharacter(); // remove the '['
        buffer.loadNextIfBufferEmpty();
        // if the buffer just holds the start of a string i.e. "Hello then you need to load the rest of the line.
        if (buffer.stringClosureMarksAbsent()) {
            buffer.addNextLineIntoBuffer();
        }
        collateValues();
    }

    // collates values, and directly places them into the DataFrame.
    private void collateValues() throws InvalidJSONFileFormat {
        while (buffer.excludes("]")) {
            // while the scanner hasn't reached the line containing the close array symbol ']'
            addStringsToColumn(buffer.getContents());
            assembleNextValue();
        }
        // if this line does not only contain ']' then only take the substring from 0 to that index.
        if (!buffer.getContents().equals("]")) {
            addStringsToColumn(buffer.substringToCloseBracket());
        }
        buffer.shiftContentsToAfterCloseBracket(); // take the substring after the ']'
        buffer.loadNextIfBufferEmpty(); // if its empty get the next existing line.
    }

    // assembles the next value(s) in the buffer, and performs checks to ensure that they are valid.
    private void assembleNextValue() throws InvalidJSONFileFormat {
        buffer.loadNextIntoBuffer();
        // if a comma wasn't supplied after a string, and next sequence doesn't start with ',' then throw an error.
        if (bufferMustStartWithComma && !buffer.startsWithChar(',')) throw new InvalidJSONFileFormat(buffer.getContents());
        if (!buffer.getContents().equals("]") && buffer.stringClosureMarksAbsent()) {
            // if the buffer just holds the start of a string i.e. "Hello then you need to load the rest of the line.
            buffer.addNextLineIntoBuffer();
        }
        if (buffer.reachedEOF()) throw new InvalidJSONFileFormat(buffer.getContents());
    }

    // parses stringed elements into an array, and adds those elements into the DataFrame.
    private void addStringsToColumn(String stringedArray) throws InvalidJSONFileFormat {
        for (String element : convertStringedElementsToArray(stringedArray)) {
            dataFrame.addValue(currentColumn, element);
        }
    }

    /**
     * Converts an array that is in a form of a String "[row,row,...]" => {row, row, ...} into a List of type String.
     *
     * @param array one large string that represents an array. It gets split on the comma delimiter.
     * @return List of type String that contains all the strings.
     */
    private List<String> convertStringedElementsToArray(String array) throws InvalidJSONFileFormat {
        List<String> rows = new ArrayList<>();
        List<String> listElements = fragmentRecord(array, ",");
        bufferMustStartWithComma = true;
        for (int elementIndex = 0; elementIndex < listElements.size(); elementIndex++) {
            String cleanedElement = getCleanedElement(listElements, elementIndex);
            if (cleanedElement == null) return rows; // if null then return the rows.
            rows.add(cleanedElement);
        }
        return rows;
    }

    /** cleans the element, by trimming any whitespace, stripping the "" enclosures, and ensuring it abides by the regex.
     *
     * @param elements List of type String, that contains all the elements.
     * @param currentIndex Index of the element to retrieve and clean.
     * @return the "cleaned" element of type String.
     * @throws InvalidJSONFileFormat thrown if there is an illegal empty string.
     */
    private String getCleanedElement(List<String> elements, int currentIndex) throws InvalidJSONFileFormat {
        String element = elements.get(currentIndex).trim();
        if (element.equals("")){
            if (elements.size() - 1 == currentIndex) {
                // if there's a empty string at the last index, then the next value doesn't need to start with a comma.
                bufferMustStartWithComma = false;
                return null;
            }
            throw new InvalidJSONFileFormat(buffer.getContents()); // otherwise trigger an error.
        }
        return cleanElement(element);
    }

    private String cleanElement(String element) throws InvalidJSONFileFormat {
        element = element.substring(element.indexOf('"') + 1, element.lastIndexOf('"'));
        if (!element.matches(textRegex)) throw new InvalidJSONFileFormat(buffer.getContents());
        return element;
    }

    /**
     * breaks the string based on the delimiter given, and returns it in a list of type String.
     *
     * @param record    a line that needs to be split based on the delimiter
     * @param delimiter a string that indicates when to split the record.
     * @return List of type string that holds all the fragmented strings.
     */
    private static List<String> fragmentRecord(String record, String delimiter) {
        List<String> fields = new ArrayList<>();
        int startIndexOfCurrentWord = 0;

        for (int currentIndex = 0; currentIndex < record.length() - delimiter.length() + 1; currentIndex++) {
            if (record.substring(currentIndex, currentIndex + delimiter.length()).equals(delimiter)) {
                fields.add(record.substring(startIndexOfCurrentWord, currentIndex));
                startIndexOfCurrentWord = currentIndex + delimiter.length();
                currentIndex = currentIndex + delimiter.length() - 1;
            }
        }
        fields.add(record.substring(startIndexOfCurrentWord));
        return fields;
    }

    // moves the scanner in the buffer to begin reading the next column.
    private void adjustScannerToNextColumn() throws InvalidJSONFileFormat {
        if (!buffer.startsWithChar(',')) {
            if (buffer.checkBrace()) return;
            buffer.loadNextIntoBuffer();
            if (buffer.checkBrace()) return;
            if (!buffer.startsWithChar(',')) throw new InvalidJSONFileFormat(buffer.getContents());
        }
        buffer.discardFirstCharacter();
        buffer.trim();
    }

    private DataFrame getDataFrame() {
        return dataFrame;
    }

    // public method that instantiates the class and gets the dataFrame.
    static DataFrame read(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        return new JSONReader(fileName).getDataFrame();
    }

}
