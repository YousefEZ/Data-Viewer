package uk.ac.ucl.gui;

import uk.ac.ucl.controller.Controller;
import uk.ac.ucl.controller.exceptions.InvalidDateStringException;
import uk.ac.ucl.graphs.Graph;
import uk.ac.ucl.gui.dialogs.FindDialog;
import uk.ac.ucl.gui.dialogs.GraphDialog;

import javax.swing.*;
import java.util.List;

class UserDialogInput {


    static String getFileName(JFrame frame, boolean save){
        JFileChooser fileChooser = new JFileChooser();
        int response = save ? fileChooser.showSaveDialog(frame) : fileChooser.showOpenDialog(frame); // display dialog.

        if (response == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return ""; // exited the file chooser. Empty String to represent cancel.
    }

    // returns the find dialog which contains the user's choices.
    static FindDialog getFindCriteria(JFrame frame, List<String> columnNames){
        return new FindDialog(frame, columnNames);
    }

    // returns a Graph that can be displayed.
    static Graph getGraph(JFrame frame, Controller controller) throws InvalidDateStringException {
        return GraphDialog.getGraph(frame, controller);
    }

}
