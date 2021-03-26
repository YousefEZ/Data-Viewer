package uk.ac.ucl.charts;

import javax.swing.*;
import java.awt.*;


public class Chart extends JPanel {

    private String titleLabel;

    public static final int width = 1000;
    public static final int height = 800;

    protected Chart(){
        setSize(width, height);
        setTitle("");
    }

    protected void setTitle(String title){
        titleLabel = title;
    }

    protected void paintTitle(Graphics2D graphics){
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        graphics.drawString(titleLabel, (float) (width*(0.5 - (float) titleLabel.length()/200)), (float) (height*0.05));
    }


}
