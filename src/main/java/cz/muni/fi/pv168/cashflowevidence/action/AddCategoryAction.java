package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.CategoryDialog;


import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


// TODO: Consider parametrization of either add/edit/delete actions or
// TODO: Category/Transaction action based on shared implementation

public class AddCategoryAction extends AbstractAction {

    public AddCategoryAction() {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CategoryDialog CategoryDialog = new CategoryDialog("New Category");
        CategoryDialog.setVisible(true);
    }
}
