package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;

import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter {

    private final DataFrame dataFrame;
    private FileWriter myWriter;

    public JSONWriter(String fileName, DataFrame dataFrame) throws IOException {
        this.dataFrame = dataFrame;
        myWriter = new FileWriter(fileName + ".json");
    }

    public void write() throws IOException {
        int column;
        int columnCount = dataFrame.getColumnNames().size();

        // build every column except last and follow it by a comma.
        myWriter.write("{\n");
        for (column = 0; column < columnCount-1; column++){
            buildNextColumn(column);
            myWriter.write(",\n");
        }
        // build last column and follow it with a right brace "}" to signify the end of the JSON.
        if (columnCount > 0) buildNextColumn(column);
        myWriter.write("\n}");
        myWriter.close();
    }

    private void buildNextColumn(int columnIndex) throws IOException {
        myWriter.write("\t\"");
        myWriter.write(dataFrame.getColumnName(columnIndex));
        myWriter.write("\" : [");
        int rowCount = dataFrame.getRowCount();
        int rowIndex;
        for (rowIndex = 0; rowIndex < rowCount-1; rowIndex++){
            // builds this string for each row ->  \"row\",
            myWriter.write("\n\t\t\"");
            myWriter.write(dataFrame.getValue(columnIndex, rowIndex));
            myWriter.append("\",");
        }
        // add a ']' closing right bracket instead of a comma
        if (rowCount > 0) {
            myWriter.write("\n\t\t\"");
            myWriter.write(dataFrame.getValue(columnIndex, rowIndex));
            myWriter.write("\"");
        }
        myWriter.write(']');
    }


}
