package uk.ac.ucl.model;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.filehandlers.DataLoader;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;
import uk.ac.ucl.filehandlers.JSONWriter;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Model {

    private DataFrame dataFrame;

    public Model(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        dataFrame = DataLoader.read(fileName);
    }

    public int getRowCount(){
        return dataFrame.getRowCount();
    }

    public List<String> getColumnNames(){
        return dataFrame.getColumnNames();
    }

    public List<String> getColumnData(int columnIndex){
        return dataFrame.getRows(columnIndex);
    }

    public int getColumnIndex(String columnName) throws ColumnDoesNotExistException {
        List<String> columns = dataFrame.getColumnNames();
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++){
            if (columns.get(columnIndex).equals(columnName)){
                return columnIndex;
            }
        }
        throw new ColumnDoesNotExistException();
    }

    public void exportToJSON(String filename) throws IOException {
        JSONWriter.write(filename, dataFrame);
    }
}
