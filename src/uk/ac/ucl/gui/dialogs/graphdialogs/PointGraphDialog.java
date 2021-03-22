package uk.ac.ucl.gui.dialogs.graphdialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PointGraphDialog extends JDialog {

    private JButton submit;
    private JComboBox xAxis;
    private JComboBox yAxis; // bar values

    private List<String> columns;

    private int response;

    public final static int SUBMITTED = 1;

    public PointGraphDialog(JFrame frame, List<String> columns){
        super(frame, true);
        setTitle("Create Plotted Graph");
        response = 0; // changes to SUBMITTED (1) when submit button is clicked.
        this.columns = columns;
        setupFrame();
        initComponents();
        placeComponents();
        setVisible(true);
    }

    private void setupFrame() {
        setSize(300, 125);
        setMinimumSize(new Dimension(300, 125));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    // <------ Initialising the Components ------->

    private void initComponents(){
        submit = new JButton("Create Graph");
        submit.addActionListener(event -> submit());
        xAxis = createComboBox();
        yAxis = createComboBox();
    }


    private JComboBox createComboBox(){
        return new JComboBox<>(columns.toArray());
    }

    // <--------- Placing the Components ---------->

    private void placeComponents(){
        add(placeComboBoxes());
        add(submit, BorderLayout.SOUTH);
    }

    private JPanel placeComboBoxes(){
        JPanel comboBoxes = new JPanel(new GridLayout(2,2, -100, 0));
        comboBoxes.add(new JLabel("X-Axis"));
        comboBoxes.add(xAxis);
        comboBoxes.add(new JLabel("Y-Axis"));
        comboBoxes.add(yAxis);
        return comboBoxes;
    }

    // <------------- Action Listeners ------------>

    private void submit(){
        response = SUBMITTED;
        dispose();
    }

    // <----------------- Getters ----------------->

    public int getXAxis(){
        return xAxis.getSelectedIndex();
    }

    public int getYAxis(){
        return yAxis.getSelectedIndex();
    }

    public int getResponse(){
        return response;
    }

}
