package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


// TODO: Consider parametrization of either add/edit/delete actions or
// TODO: Category/Transaction action based on shared implementation

public class AddCategoryAction extends AbstractAction {

    private final JTable categoryTable;

    public AddCategoryAction(JTable categoryTable) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

        this.categoryTable = categoryTable;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var categoryTableModel = (CategoryTableModel) categoryTable.getModel();

        var categoryDialog = new CategoryDialog("Add Category");
        var category = categoryDialog.showDialog();
        categoryTableModel.addRow(category);
    }
}
