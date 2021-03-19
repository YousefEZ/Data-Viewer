package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CSVReader {

    static DataFrame read(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        String[] columns = scanner.nextLine().split(",");
        DataFrame dataFrame = new DataFrame(columns);
        while (scanner.hasNextLine()){
            List<String> fields = fragmentRecord(scanner.nextLine());
            for (int column = 0; column < columns.length; column++){
                dataFrame.addValue(column, fields.get(column));
            }
        }
        scanner.close();
        return dataFrame;
    }

    private static List<String> fragmentRecord(String record){
        List<String> fields = new ArrayList<>();

        int startIndexOfCurrentWord = 0;
        for (int currentIndex = 0; currentIndex < record.length()- 2; currentIndex++){

            if (record.substring(currentIndex, currentIndex + 1).equals(",")) {
                fields.add(record.substring(startIndexOfCurrentWord, currentIndex));
                startIndexOfCurrentWord =  currentIndex + 1;
            }
        }

        fields.add(record.substring(startIndexOfCurrentWord));
        return fields;
    }

}
