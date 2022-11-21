package cz.muni.fi.pv168.cashflowevidence.action.category;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;


import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddCategoryAction extends AbstractAction {

    private final JTable categoryTable;
    private final Validator<Category> validator;

    public AddCategoryAction(JTable categoryTable, Validator<Category> validator) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

        this.categoryTable = categoryTable;;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        CategoryDialog categoryDialog = new CategoryDialog("Add Category", validator);
        Category category = categoryDialog.showDialog();

        if (validator.validate(category).isValid()) {
            categoryTableModel.addRow(category);
        }
    }
}
