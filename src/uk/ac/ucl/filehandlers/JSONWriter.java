package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {

    public static void write(String fileName, DataFrame dataFrame) throws IOException {
        List<String> columnNames = dataFrame.getColumnNames();
        List<List<String>> table = dataFrame.getTable();

        StringBuilder JSON = new StringBuilder();
        JSON.append("{\n");
        int column;
        for (column = 0; column < columnNames.size()-1; column++){
            JSON.append(buildColumn(columnNames.get(column), table.get(column)));
            JSON.append(",\n");
        }
        JSON.append(buildColumn(columnNames.get(column), table.get(column)));
        JSON.append("\n}");

        writeToFile(fileName, JSON.toString());
    }

    private static String buildColumn(String columnName, List<String> rows){
        StringBuilder columnJSON = new StringBuilder();

        columnJSON.append("\t\"");
        columnJSON.append(columnName);
        columnJSON.append("\" : [");

        for (String row: rows){
            columnJSON.append("\n\t\t\"");
            columnJSON.append(row);
            columnJSON.append("\",");
        }

        columnJSON.deleteCharAt(columnJSON.length() - 1);
        columnJSON.append(']');

        return columnJSON.toString();
    }


    private static void writeToFile(String fileName, String text) throws IOException {
        FileWriter myWriter = new FileWriter(fileName + ".json");
        myWriter.write(text);
        myWriter.close();
    }



}
