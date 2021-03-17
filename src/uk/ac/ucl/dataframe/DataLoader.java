package uk.ac.ucl.dataframe;

import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataLoader {

    private File file;

    public DataLoader(final String fileName){
        this.file = new File(fileName);
    }

    public DataFrame getDataFrame(String delimiter) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        List<String> columns = Arrays.asList(scanner.nextLine().split(","));
        DataFrame dataFrame = new DataFrame(columns);
        while (scanner.hasNextLine()){
            List<String> fields = fragmentRecord(scanner.nextLine(), delimiter);
            for (int column = 0; column < columns.size(); column++){
                dataFrame.addValue(column, fields.get(column));
            }
        }
        scanner.close();
        return dataFrame;
    }

    private List<String> fragmentRecord(String record, String delimiter){
        List<String> fields = new ArrayList<String>();

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


    public static void main(String[] args) throws FileNotFoundException, ColumnDoesNotExistException {
        System.out.println(new DataLoader("../PatientsViewer/data/Patients100.csv").getDataFrame(","));
    }




}
