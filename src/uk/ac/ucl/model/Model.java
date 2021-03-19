package uk.ac.ucl.model;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.filehandlers.DataLoader;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private DataFrame dataFrame;

    public Model(String fileName) throws FileNotFoundException, InvalidJSONFileFormat{
        dataFrame = DataLoader.read(fileName);
    }

    public int getRowCount(){
        return dataFrame.getRowCount();
    }

    public List<String> getColumnNames(){
        return dataFrame.getColumnNames();
    }

    public List<String> getColumnData(int columnIndex){
        try {
            return dataFrame.getColumn(columnIndex);
        } catch (ColumnDoesNotExistException exception){
            return new ArrayList<>();
        }
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


    public DataFrame getDataFrame() {
        return dataFrame;
    }
}
