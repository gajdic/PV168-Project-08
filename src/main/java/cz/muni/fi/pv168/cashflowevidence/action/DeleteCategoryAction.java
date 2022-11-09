package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DeleteCategoryAction extends AbstractAction {

    private final JTable categoryTable;
    private final JTable transactionTable;

    private final CategoryListModel categoryListModel;

    public DeleteCategoryAction(JTable categoryTable, JTable transactionTable, CategoryListModel categoryListModel) {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));

        this.categoryTable = categoryTable;
        this.transactionTable = transactionTable;
        this.categoryListModel = categoryListModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        TransactionTableModel transactionTableModel = (TransactionTableModel) transactionTable.getModel();

        List<Category> categoriesToBeDeleted = new ArrayList<>();

        Arrays.stream(categoryTable.getSelectedRows())
                .map(categoryTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(index -> {
                    categoriesToBeDeleted.add(categoryTableModel.getEntity(index));
                    categoryTableModel.deleteRow(index);
                    categoryListModel.deleteCategory(index);
                });

        transactionTableModel.getEntities().stream()
                .filter(t -> categoriesToBeDeleted.contains(t.getCategory()))
                .forEach(t -> t.setCategory(null));
    }
}
