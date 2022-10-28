package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.CategoryDialog;
import cz.muni.fi.pv168.cashflowevidence.app.TransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// TODO: Consider parametrization of either add/edit/delete actions or
// TODO: Category/Transaction action based on shared implementation

public class EditTransactionAction extends AbstractAction {

    private final JTable transactionTable;
    private final CategoryListModel categoryListModel;

    public EditTransactionAction(JTable transactionTable, CategoryListModel categoryListModel) {
        super("Edit", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Edits selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));

        this.transactionTable = transactionTable;
        this.categoryListModel = categoryListModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int[] selectedRows = transactionTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }

        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        int modelRow = transactionTable.convertRowIndexToModel(selectedRows[0]);

        var categoryDialog = new TransactionDialog("Edit Transaction", transactionTableModel.getEntity(modelRow), categoryListModel);
        var transaction = categoryDialog.showDialog();
        transactionTableModel.updateRow(transaction);
    }
}