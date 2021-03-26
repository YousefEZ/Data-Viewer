package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CSVReader {

    static DataFrame read(String filename) throws FileNotFoundException, ColumnAlreadyExistsException {
        Scanner scanner = new Scanner(new File(filename));

        String[] columnNames = scanner.nextLine().split(",");
        DataFrame dataFrame = new DataFrame(columnNames);
        while (scanner.hasNextLine()){
            List<String> fields = fragmentRecord(scanner.nextLine()); // fragments the line on commas.
            for (int column = 0; column < columnNames.length; column++){
                dataFrame.addValue(column, fields.get(column)); // adds field into the dataFrame.
            }
        }
        scanner.close();
        return dataFrame;
    }

    private static List<String> fragmentRecord(String record){
        List<String> fields = new ArrayList<>();
        int startIndexOfCurrentWord = 0;

        for (int currentIndex = 0; currentIndex < record.length(); currentIndex++){
            // check every letter, and see if it matches the delimiter ","
            if (record.substring(currentIndex, currentIndex + 1).equals(",")) {
                // if it matches, then record the substring as a field.
                fields.add(record.substring(startIndexOfCurrentWord, currentIndex));
                startIndexOfCurrentWord =  currentIndex + 1; // begin on the next index.
            }
        }
        fields.add(record.substring(startIndexOfCurrentWord)); // add the last field.
        return fields;
    }

}
