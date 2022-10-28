package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

// TODO: Consider parametrization of either add/edit/delete actions or
// TODO: Category/Transaction action based on shared implementation

public class DeleteCategoryAction extends AbstractAction {

    private final JTable categoryTable;

    public DeleteCategoryAction(JTable categoryTable) {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));

        this.categoryTable = categoryTable;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        Arrays.stream(categoryTable.getSelectedRows())
                // view row index must be converted to model row index
                .map(categoryTable::convertRowIndexToModel)
                .boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder())
                .forEach(categoryTableModel::deleteRow);
    }
}
