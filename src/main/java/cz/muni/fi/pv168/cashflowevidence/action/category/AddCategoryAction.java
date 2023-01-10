package cz.muni.fi.pv168.cashflowevidence.action.category;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddCategoryAction extends AbstractAction {

    private final JTable categoryTable;
    private final Validator<Category> validator;

    public AddCategoryAction(JTable categoryTable, Validator<Category> validator) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);

        this.categoryTable = categoryTable;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        CategoryDialog categoryDialog = new CategoryDialog("Add Category", validator);
        Category category = categoryDialog.showDialog();

        if (category != null && validator.validate(category).isValid()) {
            try {
                categoryTableModel.addRow(category);
            } catch (DataManipulationException ex) {
                JOptionPane.showMessageDialog(
                        null, "We were unable to create the category. Please try again.");
            }
        }
    }

    @Override public String toString() {
        return "Category";
    }
}
