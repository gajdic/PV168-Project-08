package cz.muni.fi.pv168.cashflowevidence.action.category;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditCategoryAction extends AbstractAction {

    private final JTable categoryTable;
    private final Validator<Category> validator;

    public EditCategoryAction(JTable categoryTable, Validator<Category> validator) {
        super("Edit", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Edits selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));

        this.categoryTable = categoryTable;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int[] selectedRows = categoryTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (categoryTable.isEditing()) {
            categoryTable.getCellEditor().cancelCellEditing();
        }
        CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        int modelRow = categoryTable.convertRowIndexToModel(selectedRows[0]);

        CategoryDialog categoryDialog = new CategoryDialog("Edit Category", categoryTableModel.getEntity(modelRow), validator);
        Category category = categoryDialog.showDialog();
        if (validator.validate(category).isValid()) {
            categoryTableModel.updateRow(category);
        }
    }
}