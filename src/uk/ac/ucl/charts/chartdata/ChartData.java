package uk.ac.ucl.charts.chartdata;

// Abstract Class that holds the basic data about a graph.
public class ChartData {

    private String horizontalLabel;
    private String verticalLabel;
    private String titleLabel;

    protected ChartData(){
        horizontalLabel = "";
        verticalLabel = "";
        titleLabel = "";
    }

    // <------------  Setters  --------------->

    protected void setHorizontalLabel(String label){
        horizontalLabel = label;
    }

    protected void setVerticalLabel(String label){
        verticalLabel = label;
    }

    protected void setTitleLabel(String label){
        titleLabel = label;
    }

    // <------------ Getters  --------------->

    public String getHorizontalLabel(){
        return horizontalLabel;
    }

    public String getVerticalLabel(){
        return verticalLabel;
    }

    public String getTitleLabel(){
        return titleLabel;
    }



}
