package uk.ac.ucl.gui;


import uk.ac.ucl.controller.Controller;
import uk.ac.ucl.controller.Find;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.dataframe.exceptions.ColumnAlreadyExistsException;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;
import uk.ac.ucl.graphs.Graph;
import uk.ac.ucl.gui.dialogs.FindDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    // model for viewing data, and controller for operations.
    private Controller controller;

    // components
    private JFrame frame;

    private JButton loadButton;
    private JButton resetButton;
    private JButton findButton;
    private JButton exportButton;
    private JButton graphButton;

    private List<JCheckBox> checkBoxes;

    private JTable table;

    private JPanel sidePanel;
    private JPanel buttonPanel;

    public GUI(){
        initialiseComponents();
        placeComponents();
        frame.setVisible(true);
    }

    // <-------------  Initialisation of Component ------------->
    private void initialiseComponents(){
        initialiseFrame();
        initialiseTable();
        initialiseButtons();

        buttonPanel = new JPanel();
        sidePanel =  new JPanel();
    }

    private void initialiseFrame(){
        frame = new JFrame("Untitled - Data Viewer");
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    private void initialiseTable(){
        table = new JTable(){
            public boolean isCellEditable(int data, int columns){
                return false;
            }
        };
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(1050,800));
        table.setFillsViewportHeight(true);
    }

    private void createButtons(){
        loadButton = new JButton("Load");
        loadButton.addActionListener(event -> loadData());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(event -> resetData());

        findButton = new JButton("Find");
        findButton.addActionListener(event -> findData());

        exportButton = new JButton("Export");
        exportButton.addActionListener(event -> exportData());

        graphButton = new JButton("Graph");
        graphButton.addActionListener(event -> graphData());
    }

    private void setButtonsEnabled(boolean enabled){
        resetButton.setEnabled(enabled);
        findButton.setEnabled(enabled);
        exportButton.setEnabled(enabled);
        graphButton.setEnabled(enabled);
    }

    private void initialiseButtons(){
        createButtons();
        setButtonsEnabled(false);
    }

    private void initialiseCheckBoxes(){
        checkBoxes = new ArrayList<>();
        for (String column: controller.getColumnNames()){
            JCheckBox checkBox = new JCheckBox(column); // make a checkbox for this column
            checkBox.setSelected(true); // default it to true, as all columns are visible.
            checkBoxes.add(checkBox);
        }
    }

    // <---------------- Placing components on the frame ---------------->

    // places the components into their respective panels, and onto the frame.
    private void placeComponents(){
        buttonPanel.add(loadButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(findButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(graphButton);

        sidePanel.add(new JLabel(" FILTERS: "), BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add( new JScrollPane(sidePanel), BorderLayout.WEST);
        frame.add( new JScrollPane(table), BorderLayout.CENTER); //make it scrollable
    }

    // places the checkboxes onto the side panel.
    private void placeCheckBoxes(){
        for (JCheckBox checkBox: checkBoxes){
            sidePanel.add(checkBox);
        }
    }

    // <--------------- Setting up components with data  ---------------->

    // sets up the controller with the file given by the user.
    private void setupController(String fileName) throws FileNotFoundException, InvalidJSONFileFormat, ColumnAlreadyExistsException {
        controller = new Controller(fileName);
        frame.setTitle(fileName + " - Data Viewer");
        setButtonsEnabled(true);
        setupData();
    }

    // when data is loaded, view needs to construct the table, and setup the checkboxes.
    private void setupData() {
        setupSidePanel();
        setupTable();
        initialiseCheckBoxes();
        setupCheckboxes();
        placeCheckBoxes();
        frame.setVisible(true);
    }

    // sets up the table with data.
    private void setupTable(){
        table.removeAll();
        DefaultTableModel tableData = new DefaultTableModel();
        List<String> columns = controller.getAllColumnNames();
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            tableData.addColumn(columns.get(columnIndex), controller.getColumnData(columnIndex).toArray());
        }
        table.setModel(tableData);
    }

    // clears any action listeners that have been attached to the checkbox.
    private void clearCheckBoxListeners(JCheckBox checkBox){
        ActionListener[] actionListeners = checkBox.getActionListeners();
        for (ActionListener actionListener: actionListeners){
            checkBox.removeActionListener(actionListener);
        }
    }

    // integrates the checkbox with an ActionListener that shows/hides a column when selected.
    private void integrateCheckBox(TableColumn column, JCheckBox checkBox){
        int columnIndex = column.getModelIndex();
        clearCheckBoxListeners(checkBox);
        checkBox.addActionListener(event -> toggleColumn(column, columnIndex, checkBox.isSelected()));
    }

    // triggered by Filter checkboxes on the sidePanel.
    private void toggleColumn(TableColumn column, int columnIndex, boolean show){
        if (show) {
            showColumn(column, columnIndex);
            controller.showColumn(columnIndex);
        } else {
            table.removeColumn(column);
            controller.hideColumn(columnIndex);
        }
    }

    // creates the checkboxes and integrates them.
    private void setupCheckboxes() {
        List<String> columnNames =  controller.getColumnNames();
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex);
            JCheckBox checkBox = checkBoxes.get(columnIndex); // make a checkbox for this column
            integrateCheckBox(tableColumn, checkBox); // integrates the checkbox with the column.
        }
    }

    // sets up the side panel which holds the checkboxes.
    private void setupSidePanel(){
        sidePanel.removeAll();
        sidePanel.setLayout(new GridLayout(Math.max(controller.getColumnNames().size() + 1, 15), 1));
        sidePanel.add(new JLabel(" FILTERS: "));
    }

    // <----------------- Button Action Listeners --------------->

    // loads the data from a file that a user has given (.JSON / .csv)
    private void loadData(){
        String filename = UserDialogInput.getFileName(frame, false);
        if (filename.equals("")) return; // not to trigger error message
        try {
            setupController(filename);
            JOptionPane.showMessageDialog(frame, "File has been loaded", "Loaded", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException exception){
            JOptionPane.showMessageDialog(frame,"File selected could not be found.","File not Found.",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidJSONFileFormat exception){
            JOptionPane.showMessageDialog(frame,"File given doesn't follow the set JSON format.",
                    "Parse Error", JOptionPane.ERROR_MESSAGE);
        } catch (ColumnAlreadyExistsException exception){
            JOptionPane.showMessageDialog(frame,"File given have duplicate columns of: " + exception.getColumn(),
                    "Invalid File", JOptionPane.ERROR_MESSAGE);
        }
    }

    // resets the rows, and columns.
    private void resetData(){
        controller.resetRows();
        controller.resetColumns();
        setupData();
        JOptionPane.showMessageDialog(frame, "Rows have been reset");
    }

    // finds and displays data based on the user's input.
    private void findData() {
        FindDialog findDialog = UserDialogInput.getFindCriteria(frame, controller.getColumnNames());
        if (findDialog.getResponse() == FindDialog.SUBMITTED) {
            if (findDialog.getSearchType() == Find.STRING) {
                displayMatchedStrings(findDialog.getColumnIndex(), findDialog.getSearchPhrase());
            } else {
                displayExtremeData(findDialog.getColumnIndex(), findDialog.getSearchType(), findDialog.getOperation());
            }
            rebuildTable();
        }
    }

    // exports the data into JSON.
    private void exportData(){
        String filename = UserDialogInput.getFileName(frame, true);
        if (filename.equals("")) return;
        try {
            controller.exportToJSON(filename);
            JOptionPane.showMessageDialog(frame, "successfully exported data to: " + filename + ".json",
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception){
            JOptionPane.showMessageDialog(frame, "error has occurred whilst exporting data.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // graphs the data given, based on user input.
    private void graphData(){
        try {
            Graph graph = UserDialogInput.getGraph(frame, controller);
            if (graph != null) new GraphDisplay(graph); // if null then don't display anything.
        } catch (InvalidDateStringException exception) {
            JOptionPane.showMessageDialog(frame, "column does not have correctly formatted dates", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException exception){
            JOptionPane.showMessageDialog(frame, "column does not have correctly formatted values for a graph",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // <--------------- Auxiliary Methods --------------->

    // places the column in it's correct position.
    private void showColumn(TableColumn column, int columnIndex){
        table.addColumn(column);
        for (int currentColumnIndex = table.getColumnCount() - 2; currentColumnIndex >= 0; currentColumnIndex--){
            if (columnIndex > controller.getColumnIndex(table.getColumnName(currentColumnIndex))){
                table.moveColumn(table.getColumnCount() - 1, currentColumnIndex+1);
                return;
            }
        }
        table.moveColumn(table.getColumnCount() - 1, 0);
    }

    // used when rebuilding the table, to hide the columns that have been hid before the rebuild.
    private void hideColumns(){
        List<Boolean> columnsDisplayed = controller.getDisplayedColumns();
        int offset = 0;
        for (int columnIndex = 0; columnIndex < columnsDisplayed.size(); columnIndex++) {
            if (!columnsDisplayed.get(columnIndex)) {
                TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex - offset);
                toggleColumn(tableColumn, columnIndex, false);
                offset++;
            }
        }
    }

    // rebuilds the table when a different set of rows need to be displayed.
    private void rebuildTable(){
        setupTable();
        setupCheckboxes();
        hideColumns();
    }

    // displays the rows that have a string that matches the given text at the columnIndex.
    private void displayMatchedStrings(int columnIndex, String text){
        if (text == null) return;
        controller.configureRowsToMatch(columnIndex, text);
        rebuildTable();
        JOptionPane.showMessageDialog(frame, "Search Completed, Matched: " + controller.getRowCount()  + " Rows",
                "Search", JOptionPane.INFORMATION_MESSAGE);
    }

    //  displays the most extreme data (largest / smallest).
    private void displayExtremeData(int columnIndex, int searchType, int operation){
        try {
            controller.configureRowsToFindOperation(columnIndex, searchType, operation);
            rebuildTable();
            JOptionPane.showMessageDialog(frame, "Find Operation Completed", "Find",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame,"Column does not conform to the type given","Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidDateStringException exception) {
            JOptionPane.showMessageDialog(frame, exception.getexceptionAt() + " doesn't fit the format of a date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
