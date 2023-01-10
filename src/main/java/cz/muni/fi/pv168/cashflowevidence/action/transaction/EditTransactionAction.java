package cz.muni.fi.pv168.cashflowevidence.action.transaction;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.TransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditTransactionAction extends AbstractAction {

    private final JTable transactionTable;
    private final Repository<Category> categoryRepository;
    private final Validator<Transaction> validator;

    public EditTransactionAction(JTable transactionTable, Repository<Category> categoryRepository, Validator<Transaction> validator) {
        super("Edit", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Edits selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));

        this.transactionTable = transactionTable;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int[] selectedRows = transactionTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }

        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        int modelRow = transactionTable.convertRowIndexToModel(selectedRows[0]);

        var categoryDialog = new TransactionDialog("Edit Transaction", transactionTableModel.getEntity(modelRow),categoryRepository, validator);
        var transaction = categoryDialog.showDialog();
        if (transaction != null && validator.validate(transaction).isValid()) {
            try {
                transactionTableModel.updateRow(modelRow, transaction);
            } catch (DataManipulationException ex) {
                JOptionPane.showMessageDialog(
                        null, "We were unable to update the transaction. Please try again.");
            }
        }
    }
}