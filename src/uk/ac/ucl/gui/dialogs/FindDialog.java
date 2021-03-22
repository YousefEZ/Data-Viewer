package uk.ac.ucl.gui.dialogs;

import uk.ac.ucl.controller.Find;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FindDialog extends JDialog{
    private JFrame parent;

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
    private String searchPhrase;

    private List<String> columnNames;

    public final static int SUBMITTED = 1;

    private final static String[] TYPES = {"NUMBER", "STRING", "DATE"};
    private final static String[] OPERATIONS = {"LARGEST", "SMALLEST"};

    public FindDialog(JFrame frame, List<String> columnNames){
        super(frame, true);
        parent = frame;
        this.columnNames = columnNames;

        setupFrame();
        initComponents();
        placeComponents();
        response = 0; // Changed to 1 when submit is clicked.

        setVisible(true);
    }

    private void setupFrame(){
        setSize(400, 140);
        setMinimumSize(new Dimension(300, 140));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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
        typeSelectionPanel = new JPanel(new GridLayout(TYPES.length, 1));
        operationSelectionPanel = new JPanel(new GridLayout(OPERATIONS.length, 1));
    }

    private void createButtons(){
        submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> submit());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> cancel());

        add(submitButton);
    }

    private void createRadioButtons(){
        createOperationRadioButtons();
        createTypeRadioButtons();
    }

    // creates a list of JRadioButtons with names extracted from the given array.
    private JRadioButton[] createButtonGroup(String[] buttonNames){
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[buttonNames.length];

        for (int index = 0; index < buttonNames.length; index++) {
            radioButtons[index] = new JRadioButton(buttonNames[index]);
            buttonGroup.add(radioButtons[index]);
        }
        return radioButtons;
    }

    // creates the Radio buttons for the data types, and action listeners in order to read which type was given.
    private void createTypeRadioButtons(){
        typeButtons = createButtonGroup(TYPES);

        typeButtons[0].addActionListener(event -> typeButtonAction(Find.NUMBER));
        typeButtons[1].addActionListener(event -> typeButtonAction(Find.STRING));
        typeButtons[2].addActionListener(event -> typeButtonAction(Find.DATE));

        typeButtons[0].setSelected(true);
    }

    // creates the Radio buttons for the operations, such as getting the largest or smallest value.
    private void createOperationRadioButtons(){
        operationButtons = createButtonGroup(OPERATIONS);

        operationButtons[0].addActionListener(event -> operation = Find.LARGEST);
        operationButtons[1].addActionListener(event -> operation = Find.SMALLEST);
        operationButtons[0].setSelected(true);
    }

    // creates the comboBox that allows selection of a column.
    private void createComboBox(){
        columnOptions = new JComboBox<>(columnNames.toArray());
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
        add(selectionPanel);
        add(buttonPanel, BorderLayout.SOUTH);
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

    // <------------- Action Listeners ------------->

    private void typeButtonAction(int buttonIndex){
        type = buttonIndex;
        for (JRadioButton radioButton: operationButtons){
            radioButton.setEnabled(buttonIndex != Find.STRING); // if String is selected, then disable operations.
        }
    }

    private void submit(){
        response = SUBMITTED; // switch response in order to know if they clicked submit.
        dispose();
        if (type == Find.STRING){
            // if they selected a string then display a dialog to get the string to match.
            searchPhrase =  JOptionPane.showInputDialog(parent,"Please input the string to match","Search",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void cancel(){
        dispose();
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

    public String getSearchPhrase(){
        return searchPhrase;
    }

    public int getColumnIndex(){
        return columnOptions.getSelectedIndex();
    }

}
