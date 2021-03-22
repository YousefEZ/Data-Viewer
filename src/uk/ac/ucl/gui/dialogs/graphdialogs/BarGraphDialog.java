package uk.ac.ucl.gui.dialogs.graphdialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarGraphDialog extends JDialog {

    private JButton submit;
    private JComboBox xAxis;
    private JComboBox yAxis;

    private JRadioButton yAxisButton;
    private JRadioButton frequencyButton;
    private JRadioButton ageButton;

    private List<String> columns;

    private int response;
    private int matchType;

    public final static int YAXIS = 0;
    public final static int FREQUENCY = 1;
    public final static int AGE = 2;

    public final static int SUBMITTED = 1;

    public BarGraphDialog(JFrame frame, List<String> columns){
        super(frame, true);
        setTitle("Create Bar Graph");
        response = 0; // only changes if they submit.
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
        createRadioButtons();
        groupRadioButtons();
        xAxis = createComboBox();
        yAxis = createComboBox();
    }

    private void createRadioButtons(){
        yAxisButton = new JRadioButton("Y Axis");
        yAxisButton.addActionListener(event -> radioButtonClick(YAXIS));

        frequencyButton = new JRadioButton("Frequency");
        frequencyButton.addActionListener(event -> radioButtonClick(FREQUENCY));

        ageButton = new JRadioButton("Age");
        ageButton.addActionListener(event -> radioButtonClick(AGE));
    }

    private void groupRadioButtons(){
        ButtonGroup group = new ButtonGroup();
        group.add(yAxisButton);
        group.add(frequencyButton);
        group.add(ageButton);
        yAxisButton.setSelected(true);
    }

    private JComboBox createComboBox(){
        return new JComboBox<>(columns.toArray());
    }

    // <--------- Placing the Components ---------->

    private void placeComponents(){
        add(placeComboBoxes());
        add(placeRadioButtons(), BorderLayout.EAST);
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

    private JPanel placeRadioButtons(){
        JPanel radioButtons = new JPanel(new GridLayout(3, 1));
        radioButtons.add(yAxisButton);
        radioButtons.add(frequencyButton);
        radioButtons.add(ageButton);
        return radioButtons;
    }

    // <------------- Action Listeners ------------>

    private void radioButtonClick(int radioValue){
        yAxis.setEnabled(yAxisButton.isSelected()); // only enable the y-axis comboBox if the button is selected.
        matchType = radioValue;
    }

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

    public int getMatchType(){
        return matchType;
    }

    public int getResponse(){
        return response;
    }


}
