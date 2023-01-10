package cz.muni.fi.pv168.cashflowevidence.action.category;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.*;
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


    public DeleteCategoryAction(JTable categoryTable, JTable transactionTable, JTable repeatingTransactionTable) {
        super();

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));


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
        List<Integer> categoryIndexes = new ArrayList<>();

        Arrays.stream(categoryTable.getSelectedRows())
                .map(categoryTable::convertRowIndexToModel)
                .boxed()
                .forEach(index -> {
                    categoryIndexes.add(index);
                    categoriesToBeDeleted.add(categoryTableModel.getEntity(index));
                });

        var sb = new StringBuilder("Are you sure you want to delete categories: ");
        if (categoryIndexes.size() == 1) {
            sb = new StringBuilder("Are you sure you want to delete category ");
        }

        var idx = 0;
        while (idx < 5 && idx != categoriesToBeDeleted.size()) {
            sb.append(categoriesToBeDeleted.get(idx).getName()).append(", ");
            idx += 1;
        }
        sb.deleteCharAt(sb.toString().length() - 2);

        var remainingCategories = categoriesToBeDeleted.size() - idx;
        if (remainingCategories > 0) {
            sb.append(String.format("and %d more ", remainingCategories));
        }
        sb.append("?");

        if (transactionTableModel.getEntities().stream()
                .anyMatch(t -> categoriesToBeDeleted.contains(t.getCategory())) ||
                repeatingTransactionTableModel.getEntities().stream()
                        .anyMatch(t -> categoriesToBeDeleted.contains(t.getCategory()))) {
            if (categoryIndexes.size() == 1) {
                sb.append("\nIt is used in some existing transactions.");
            } else {
                sb.append("\nSome of them are used in existing transactions.");
            }
        }

        var deleteOption = confirmDelete(sb.toString());

        try {
            if (deleteOption == 0) {
                categoryIndexes.stream()
                        .sorted(Comparator.reverseOrder())
                        .forEach(categoryTableModel::deleteRow);
            } else {
                return;
            }

            transactionTableModel.getEntities().stream()
                    .filter(t -> categoriesToBeDeleted.contains(t.getCategory()))
                    .forEach(t -> t.setCategory(null));

            repeatingTransactionTableModel.getEntities().stream()
                    .filter(t -> categoriesToBeDeleted.contains(t.getCategory()))
                    .forEach(t -> t.setCategory(null));
        } catch (DataManipulationException ex) {
            JOptionPane.showMessageDialog(
                    null, "We were unable to delete the category. Please try again.");
        }
    }
}
