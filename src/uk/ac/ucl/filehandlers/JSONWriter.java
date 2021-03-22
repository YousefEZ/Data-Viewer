package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {

    public static void write(String fileName, DataFrame dataFrame) throws IOException {
        List<String> columnNames = dataFrame.getColumnNames();

        StringBuilder JSON = new StringBuilder();
        JSON.append("{\n");
        int column;
        // build every column and follow it by a comma.
        for (column = 0; column < columnNames.size()-1; column++){
            JSON.append(buildColumn(columnNames.get(column), dataFrame.getRows(column))).append(",\n");
        }
        // build last column and follow it with a right brace "}" to signify the end of the JSON.
        JSON.append(buildColumn(columnNames.get(column), dataFrame.getRows(column))).append("\n}");
        writeToFile(fileName, JSON.toString());
    }

    // structure =>  "column" : [(\n\t\t"row",)* "row']
    private static String buildColumn(String columnName, List<String> rows){
        StringBuilder columnJSON = new StringBuilder();
        columnJSON.append("\t\"").append(columnName).append("\" : ["); // builds => \"column\" : [
        for (String row: rows){
            columnJSON.append("\n\t\t\"").append(row).append("\",");  // builds this string for each row ->  \"row\",
        }
        // delete the last character which is always a comma, and add a ']' closing right bracket
        columnJSON.deleteCharAt(columnJSON.length() - 1);
        columnJSON.append(']');
        return columnJSON.toString();
    }

    private static void writeToFile(String fileName, String jsonString) throws IOException {
        FileWriter myWriter = new FileWriter(fileName + ".json");
        myWriter.write(jsonString);
        myWriter.close();
    }



}
