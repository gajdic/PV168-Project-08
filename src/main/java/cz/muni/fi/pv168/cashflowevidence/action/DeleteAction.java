package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

abstract public class DeleteAction extends AbstractAction {

    public DeleteAction() {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));
    }

    protected int confirmDelete() {
        Object[] options = { "Yes", "Cancel" };
        return JOptionPane.showOptionDialog(
                null,
                "Are you sure you want to delete this item?", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[1]
        );
    }
}