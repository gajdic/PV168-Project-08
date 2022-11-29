package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

abstract public class DeleteAction extends AbstractAction {

    public DeleteAction() {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));
    }

    protected int confirmDelete(String message, Object[] options) {
        return JOptionPane.showOptionDialog(
                null,
                message, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[1]
        );
    }

    protected int confirmDeleteCategory(String message) {
        Object[] options = { "Yes", "Yes to all", "Cancel" };
        return confirmDelete(message, options);
    }

    protected int confirmDeleteTransaction(String message) {
        Object[] options = { "Yes", "Cancel" };
        return confirmDelete(message, options);
    }
}