package uk.ac.ucl.dataframe;

import java.util.ArrayList;
import java.util.List;

class Column {

    private final String columnName;
    private List<String> rows;

    Column(String columnName){
        this.columnName = columnName;
        this.rows = new ArrayList<>();
    }

    String getName(){
        return this.columnName;
    }

    int getSize(){
        return this.rows.size();
    }

    String getRowValue(int index){
        return rows.get(index);
    }

    void setRowValue(int index, String value){
        rows.set(index, value);
    }

    void addRowValue(String row){
        rows.add(row);
    }

}
