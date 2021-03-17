package uk.ac.ucl.jsonhandler;

import uk.ac.ucl.dataframe.DataFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {

    public static void write(String fileName, DataFrame dataFrame) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> columnNames = dataFrame.getColumnNames();
        List<List<String>> table = dataFrame.getTable();

        for (int row = 0; row < dataFrame.getRowCount(); row++) {
            stringBuilder.append(createJSONString(row, columnNames, table));
        }

        writeToFile(fileName, stringBuilder.toString());
    }

    private static String createJSONString(int row, List<String> columnNames, List<List<String>> table){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{\n");
        int columnIndex;
        for (columnIndex = 0; columnIndex < columnNames.size() - 1; columnIndex++) {
            stringBuilder.append(createJSONField(columnNames.get(columnIndex), table.get(columnIndex).get(row)));
            stringBuilder.append(",\n");
        }
        stringBuilder.append(createJSONField(columnNames.get(columnIndex), table.get(columnIndex).get(row)));
        stringBuilder.append("\n}\n");

        return stringBuilder.toString();
    }

    private static String createJSONField(String field, String value){
        return "\t\"" + field + "\": \"" + value + "\"";
    }

    private static void writeToFile(String fileName, String text) throws IOException {
        FileWriter myWriter = new FileWriter(fileName + ".json");
        myWriter.write(text);
        myWriter.close();
    }



}
