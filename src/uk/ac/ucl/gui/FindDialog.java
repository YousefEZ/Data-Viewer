package uk.ac.ucl.gui;

import uk.ac.ucl.controller.DataMatcher;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FindDialog extends JDialog{

    private JButton submitButton;
    private JButton cancelButton;

    private JRadioButton[] typeButtons;
    private JRadioButton[] operationButtons;

    private JComboBox columnOptions;

    private JPanel buttonPanel;
    private JPanel columnSelectPanel;
    private JPanel typeSelectionPanel;
    private JPanel operationSelectionPanel;

    private int type;
    private int response;
    private int operation;

    public final static int SUBMITTED = 1;

    private List<String> columnNames;

    public FindDialog(JFrame frame, List<String> columnNames){
        super(frame, true);
        this.columnNames = columnNames;

        setupFrame();
        initComponents();
        placeComponents();
        response = 0; // Changed to 1 when submit is clicked.

        this.setVisible(true);
    }

    private void setupFrame(){
        this.setSize(400, 200);
        this.setMinimumSize(new Dimension(300, 200));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout( new GridLayout(2, 1));
        this.setLocationRelativeTo(null);
    }

    // <------------ initialising components -------------->

    private void initComponents(){
        createPanels();
        createButtons();
        createRadioButtons();
        createComboBox();
    }

    private void createPanels(){
        buttonPanel = new JPanel();
        columnSelectPanel = new JPanel();
        typeSelectionPanel = new JPanel(new GridLayout(DataMatcher.FINDTYPES.length, 1));
        operationSelectionPanel = new JPanel(new GridLayout(DataMatcher.OPERATIONS.length, 1));
    }

    private void createButtons(){
        submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> submit());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> cancel());

        this.add(submitButton);
    }

    private void createRadioButtons(){
        createTypeRadioButtons();
        createOperationRadioButtons();
    }

    private void createTypeRadioButtons(){
        typeButtons = createButtonGroup(DataMatcher.FINDTYPES);

        for (int value: DataMatcher.FINDVALUES) {
            typeButtons[value].addActionListener(event -> type = value);
        }
    }

    private void createOperationRadioButtons(){
        operationButtons = createButtonGroup(DataMatcher.OPERATIONS);

        for (int value: DataMatcher.OPERATIONVALUES)  {
            operationButtons[value].addActionListener(event -> operation = value);
        }

    }

    private JRadioButton[] createButtonGroup(String[] buttonNames){
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[buttonNames.length];

        for (int index = 0; index < buttonNames.length; index++) {
            radioButtons[index] = new JRadioButton(buttonNames[index]);
            buttonGroup.add(radioButtons[index]);
        }
        return radioButtons;
    }

    private void createComboBox(){
        columnOptions = new JComboBox(columnNames.toArray());
    }

    // <-------------- Placing Components -------------->

    private void placeComponents(){
        placePanels();
        placeButtons();
        placeRadioButtons();
        placeColumnComboBox();
    }

    private void placePanels(){
        JPanel selectionPanel = new JPanel(new GridLayout(1, 3));

        selectionPanel.add(columnSelectPanel);
        selectionPanel.add(typeSelectionPanel);
        selectionPanel.add(operationSelectionPanel);
        this.add(selectionPanel);
        this.add(buttonPanel);
    }

    private void placeButtons(){
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
    }

    private void placeRadioButtons(){
        for (JRadioButton radioButton: typeButtons){
            typeSelectionPanel.add(radioButton);
        }

        for (JRadioButton radioButton: operationButtons) {
            operationSelectionPanel.add(radioButton);
        }
    }

    private void placeColumnComboBox(){
        columnSelectPanel.add(new JLabel("Select a Column:"));
        columnSelectPanel.add(columnOptions);
    }

    // <------------- Button Listener Function ------------->

    private void submit(){
        response = SUBMITTED;
        this.dispose();
    }

    private void cancel(){
        this.dispose();
    }

    // <-------------- Getters -------------->

    public int getResponse(){
        return response;
    }

    public int getSearchType(){
        return type;
    }

    public int getOperation(){
        return operation;
    }

    public int getColumnIndex(){
        return columnOptions.getSelectedIndex();
    }

}
