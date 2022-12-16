package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;


import cz.muni.fi.pv168.cashflowevidence.app.dialog.RepeatingTransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddRepeatingTransactionAction extends AbstractAction {

    private final JTable repeatingTransactionsTable;

    private final Repository<Category> categoryRepository;
    private final Validator<RepeatingTransaction> validator;

    public AddRepeatingTransactionAction(JTable repeatingTransactionsTable, Repository<Category> categoryRepository, Validator<RepeatingTransaction> validator) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);

        this.repeatingTransactionsTable = repeatingTransactionsTable;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionsTable.getModel();
        var repeatingTransactionDialog = new RepeatingTransactionDialog("New Repeating Transaction", categoryRepository, validator);
        var repeatingTransaction = repeatingTransactionDialog.showDialog();

        if (repeatingTransaction != null && validator.validate(repeatingTransaction).isValid()) {
            try {
                repeatingTransactionTableModel.addRow(repeatingTransaction);
            } catch (DataManipulationException ex) {
                JOptionPane.showMessageDialog(
                        null, "We were unable to create the repeating transaction. Please try again.");
            }
        }
    }

    @Override public String toString() {
        return "Repeating Transaction";
    }
}
