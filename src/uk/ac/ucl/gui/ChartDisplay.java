package uk.ac.ucl.gui;

import uk.ac.ucl.charts.Chart;

import javax.swing.*;
import java.awt.*;


class ChartDisplay extends JFrame {

    // displays a graph.
    ChartDisplay(Chart chart){
        setupFrame();
        add(chart);

        JButton okButton = createOkButton();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        add(new JScrollPane(buttonPanel), BorderLayout.SOUTH);
        setVisible(true);
    }

    private void setupFrame(){
        setSize(Chart.width, (int) (Chart.height*1.1));
        setTitle("Display Graph");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JButton createOkButton(){
        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> dispose());
        return okButton;
    }

}
