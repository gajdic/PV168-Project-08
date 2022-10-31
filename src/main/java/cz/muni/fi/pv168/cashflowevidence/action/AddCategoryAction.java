package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddCategoryAction extends AbstractAction {

    private final JTable categoryTable;
    private final CategoryListModel categoryListModel;

    public AddCategoryAction(JTable categoryTable, CategoryListModel categoryListModel) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

        this.categoryTable = categoryTable;
        this.categoryListModel = categoryListModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var categoryTableModel = (CategoryTableModel) categoryTable.getModel();

        var categoryDialog = new CategoryDialog("Add Category");
        var category = categoryDialog.showDialog();
        categoryTableModel.addRow(category);
        categoryListModel.addCategory(category);
    }
}
