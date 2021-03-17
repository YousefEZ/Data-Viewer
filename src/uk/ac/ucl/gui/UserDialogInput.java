package uk.ac.ucl.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

// Dialog
class UserDialogInput {

    static String getFileName(JFrame frame, boolean save){
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Supported Files (*.csv;*.JSON)", "json", "csv");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        int response = save ? fileChooser.showSaveDialog(frame) : fileChooser.showOpenDialog(frame);

        if (response == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return ""; // exited the file chooser.
    }


    static String getSearchColumn(JFrame frame, List<String> columns){
        return (String) JOptionPane.showInputDialog(frame,"Please select the column to search on", "Search",
                JOptionPane.PLAIN_MESSAGE,null, columns.toArray(), columns.get(0));
    }

    static String getTextInput(JFrame frame){
        return JOptionPane.showInputDialog(frame,"Please select the phrase to match", "Search",
                JOptionPane.PLAIN_MESSAGE);
    }

    static FindDialog getFindCriteria(JFrame frame, List<String> columnNames){
        return new FindDialog(frame, columnNames);
    }

}
