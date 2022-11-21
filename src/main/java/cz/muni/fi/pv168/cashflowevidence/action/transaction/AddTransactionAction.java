package cz.muni.fi.pv168.cashflowevidence.action.transaction;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.TransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;


import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddTransactionAction extends AbstractAction {

    private final JTable transactionTable;
    private final Repository<Category> categories;
    private final Validator<Transaction> validator;

    public AddTransactionAction(JTable transactionTable, Repository<Category> categories, Validator<Transaction> validator) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));

        this.transactionTable = transactionTable;
        this.categories = categories;
        this.validator = validator;

        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        TransactionTableModel transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        TransactionDialog transactionDialog = new TransactionDialog("New Transaction", categories, validator);
        Transaction transaction = transactionDialog.showDialog();

        if (validator.validate(transaction).isValid()) {
            transactionTableModel.addRow(transaction);
        }
    }
}
