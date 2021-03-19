package uk.ac.ucl.gui;


import uk.ac.ucl.controller.Controller;
import uk.ac.ucl.controller.DataMatcher;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.dataframe.exceptions.ColumnDoesNotExistException;
import uk.ac.ucl.filehandlers.JSONWriter;
import uk.ac.ucl.filehandlers.exceptions.InvalidJSONFileFormat;

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
    private JButton searchButton;
    private JButton resetButton;
    private JButton findButton;
    private JButton exportButton;

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

        searchButton = new JButton("Search");
        searchButton.addActionListener(event -> searchData());

        findButton = new JButton("Find");
        findButton.addActionListener(event -> findData());

        exportButton = new JButton("Export");
        exportButton.addActionListener(event -> exportData());
    }

    private void initialiseButtons(){
        createButtons();
        searchButton.setEnabled(false);
        resetButton.setEnabled(false);
        findButton.setEnabled(false);
        exportButton.setEnabled(false);
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
    private void placeComponents(){
        buttonPanel.add(loadButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(findButton);
        buttonPanel.add(exportButton);

        sidePanel.add(new JLabel(" FILTERS: "), BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add( new JScrollPane(sidePanel), BorderLayout.WEST);

        frame.add( new JScrollPane(table), BorderLayout.CENTER); //make it scrollable
    }

    private void placeCheckBoxes(){
        for (JCheckBox checkBox: checkBoxes){
            sidePanel.add(checkBox); // place the checkbox
        }
    }

    // <--------------- Setting up components with data  ---------------->
    private void setupController(String fileName) throws FileNotFoundException, InvalidJSONFileFormat{
        controller = new Controller(fileName); // sets up controller.
        frame.setTitle(fileName + " - Data Viewer");
        searchButton.setEnabled(true);
        resetButton.setEnabled(true);
        findButton.setEnabled(true);
        exportButton.setEnabled(true);
        setupData();
    }

    private void setupData() {
        setupSidePanel();
        setupTable();
        initialiseCheckBoxes();
        setupCheckboxes();
        placeCheckBoxes();
        frame.setVisible(true);
    }

    private void setupTable(){
        table.removeAll();
        DefaultTableModel tableData = new DefaultTableModel();
        List<String> columns = controller.getAllColumnNames();
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            tableData.addColumn(columns.get(columnIndex), controller.getColumnData(columnIndex).toArray());
        }
        table.setModel(tableData);
    }

    private void clearCheckBoxListeners(JCheckBox checkBox){
        ActionListener[] actionListeners = checkBox.getActionListeners();
        for (ActionListener actionListener: actionListeners){
            checkBox.removeActionListener(actionListener);
        }
    }

    private void integrateCheckBox(TableColumn column, JCheckBox checkBox){
        int columnIndex = column.getModelIndex();
        clearCheckBoxListeners(checkBox);
        checkBox.addActionListener(event -> showColumn(column, columnIndex, checkBox.isSelected()));
    }

    private void setupCheckboxes() {
        List<String> columnNames =  controller.getColumnNames();
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex);
            JCheckBox checkBox = checkBoxes.get(columnIndex); // make a checkbox for this column
            integrateCheckBox(tableColumn, checkBox); // integrates the checkbox with the column.
        }
    }

    private void setupSidePanel(){
        sidePanel.removeAll();
        sidePanel.setLayout(new GridLayout(controller.getColumnNames().size()+1, 1));
        sidePanel.add(new JLabel(" FILTERS: "));
    }

    // <-------------- Action Functions -------------->

    // triggered by checkboxes on the sidePanel.
    private void showColumn(TableColumn column, int columnIndex, boolean show){
        if (show) {
            table.addColumn(column);
            if (table.getColumnCount() - 1 > columnIndex) { // move back to original position.
                table.moveColumn(table.getColumnCount() - 1, columnIndex);
            }
            controller.showColumn(columnIndex);
        } else {
            table.removeColumn(column);
            controller.hideColumn(columnIndex);
        }
    }

    private void loadData(){
        String filename = UserDialogInput.getFileName(frame, false);
        if (filename.equals("")) return; // not to trigger error message
        try {
            setupController(filename);
            JOptionPane.showMessageDialog(frame, "File has been loaded");
        } catch (FileNotFoundException exception){
            JOptionPane.showMessageDialog(frame,"File selected could not be found.","File not Found",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidJSONFileFormat exception){
            JOptionPane.showMessageDialog(frame,"File given doesn't follow the preferred JSON format",
                    "File not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetData(){
        controller.resetRows();
        controller.resetColumns();
        setupData();
        JOptionPane.showMessageDialog(frame, "Rows have been reset");
    }

    private void hideColumns(){
        List<Boolean> columnsDisplayed = controller.getDisplayedColumns();
        int offset = 0;
        for (int columnIndex = 0; columnIndex < columnsDisplayed.size(); columnIndex++) {
            if (!columnsDisplayed.get(columnIndex)) {
                TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex - offset);
                showColumn(tableColumn, columnIndex, false);
                offset++;
            }
        }
    }

    private void rebuildTable(){
        setupTable();
        setupCheckboxes();
        hideColumns();
    }

    private void searchData(){
        String column = UserDialogInput.getSearchColumn(frame, controller.getColumnNames());
        if (column == null) return;
        String text = UserDialogInput.getTextInput(frame);
        try {
            controller.configureRowsToMatch(column, text);
            rebuildTable();
            JOptionPane.showMessageDialog(frame, "Search Completed, Matched: " + controller.getRowCount()  + " Rows",
                    "Search", JOptionPane.INFORMATION_MESSAGE);
        } catch (ColumnDoesNotExistException exception){
            JOptionPane.showMessageDialog(frame,"Column specified is not recognised","Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findData(){
        FindDialog findDialog = UserDialogInput.getFindCriteria(frame, controller.getColumnNames());
        if (findDialog.getResponse() == FindDialog.SUBMITTED){
            try {
                controller.configureRowsToMatch(findDialog.getColumnIndex(), findDialog.getSearchType(),
                        findDialog.getOperation());
                rebuildTable();
                JOptionPane.showMessageDialog(frame, "Find Operation Completed", "Find",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(frame,"Column does not conform to the type given","Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidDateStringException exception) {
                JOptionPane.showMessageDialog(frame, exception.getexceptionAt() + " doesn't fit the format of " +
                        DataMatcher.FINDTYPES[findDialog.getSearchType()], "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportData(){
        String filename = UserDialogInput.getFileName(frame, true);
        if (filename.equals("")) return;
        try {
            JSONWriter.write(filename, controller.getDataFrame());
            JOptionPane.showMessageDialog(frame, "successfully exported data to: " + filename, "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception){
            JOptionPane.showMessageDialog(frame, "error has occurred whilst exporting data.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


}
