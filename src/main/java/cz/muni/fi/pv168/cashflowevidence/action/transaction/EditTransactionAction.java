package cz.muni.fi.pv168.cashflowevidence.action.transaction;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.TransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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