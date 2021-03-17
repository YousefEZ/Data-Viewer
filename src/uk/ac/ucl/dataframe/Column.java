package uk.ac.ucl.dataframe;

import java.util.ArrayList;
import java.util.List;

public class Column {

    private String columnName;
    private List<String> rows;

    public Column(String columnName){
        this.columnName = columnName;
        this.rows = new ArrayList<String>();
    }

    public Column(String columnName, ArrayList<String> rows){
        this.columnName = columnName;
        this.rows = rows;
    }

    public String getName(){
        return this.columnName;
    }

    public int getSize(){
        return this.rows.size();
    }

    public String getRowValue(int index){
        return rows.get(index);
    }

    public void setRowValue(int index, String value){
        rows.set(index, value);
    }

    public void addRowValue(String row){
        rows.add(row);
    }

}
