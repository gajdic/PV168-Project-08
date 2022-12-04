package cz.muni.fi.pv168.cashflowevidence.action.category;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DeleteCategoryAction extends DeleteAction {

    private final JTable categoryTable;
    private final JTable transactionTable;
    private final JTable repeatingTransactionTable;
    private boolean yesToAll = false;


    public DeleteCategoryAction(JTable categoryTable, JTable transactionTable, JTable repeatingTransactionTable) {
        super();

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));

        this.categoryTable = categoryTable;
        this.transactionTable = transactionTable;
        this.repeatingTransactionTable = repeatingTransactionTable;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        TransactionTableModel transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        RepeatingTransactionTableModel repeatingTransactionTableModel =
                (RepeatingTransactionTableModel) repeatingTransactionTable.getModel();

        List<Category> categoriesToBeDeleted = new ArrayList<>();

        Arrays.stream(categoryTable.getSelectedRows())
                .map(categoryTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(index -> {
                    var message = String.format("Are you sure you want to delete category %s?",
                            categoryTableModel.getEntity(index).getName());

                    var category = categoryTableModel.getEntity(index);

                    if (transactionTableModel.getEntities().stream()
                            .anyMatch(t -> category == t.getCategory()) ||
                        repeatingTransactionTableModel.getEntities().stream()
                            .anyMatch(t -> category == t.getCategory())) {

                        message += String.format("\n%s is used in existing transaction",
                                categoryTableModel.getEntity(index).getName());
                    }

                    var deleteOption = -1;

                    if (!yesToAll) {
                        deleteOption = confirmDelete(message);
                        yesToAll = deleteOption == 1;
                    }

                    if (deleteOption == 0 || yesToAll) {
                        categoriesToBeDeleted.add(categoryTableModel.getEntity(index));
                        categoryTableModel.deleteRow(index);
                    }
                });

        transactionTableModel.getEntities().stream()
                .filter(t -> categoriesToBeDeleted.contains(t.getCategory()))
                .forEach(t -> t.setCategory(null));

        repeatingTransactionTableModel.getEntities().stream()
                .filter(t -> categoriesToBeDeleted.contains(t.getCategory()))
                .forEach(t -> t.setCategory(null));

        yesToAll = false;
    }
}
