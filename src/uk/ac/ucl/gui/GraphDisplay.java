package uk.ac.ucl.gui;

import uk.ac.ucl.graphs.Graph;

import javax.swing.*;
import java.awt.*;


class GraphDisplay extends JFrame {

    // displays a graph.
    GraphDisplay(Graph graph){
        setupFrame();
        add(graph);

        JButton okButton = createOkButton();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void setupFrame(){
        setSize(Graph.width, (int) (Graph.height*1.1));
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
