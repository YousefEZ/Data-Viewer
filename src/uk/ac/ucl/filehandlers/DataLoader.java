package uk.ac.ucl.filehandlers;

import uk.ac.ucl.dataframe.DataFrame;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

import java.io.FileNotFoundException;

public class DataLoader {

    public static DataFrame read(String filename) throws FileNotFoundException, InvalidJSONFileFormat {
        if (filename.indexOf('.') == -1){
            throw new FileNotFoundException();
        }
        String fileExtension = filename.substring(filename.indexOf('.'));

        switch (fileExtension){
            case ".csv":
                return CSVReader.read(filename);
            case ".json":
                return JSONReader.read(filename);
            default:
                throw new FileNotFoundException();
        }
    }

}
