package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditCategoryAction extends AbstractAction {

    private final JTable categoryTable;

    public EditCategoryAction(JTable categoryTable) {
        super("Edit", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Edits selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));

        this.categoryTable = categoryTable;
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
        var categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        int modelRow = categoryTable.convertRowIndexToModel(selectedRows[0]);

        var categoryDialog = new CategoryDialog("Edit Category", categoryTableModel.getEntity(modelRow));
        var category = categoryDialog.showDialog();
        categoryTableModel.updateRow(category);
    }
}