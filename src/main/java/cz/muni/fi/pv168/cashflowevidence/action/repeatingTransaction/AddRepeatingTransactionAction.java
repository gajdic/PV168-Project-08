package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;


import cz.muni.fi.pv168.cashflowevidence.app.dialog.RepeatingTransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddRepeatingTransactionAction extends AbstractAction {

    private final JTable repeatingTransactionsTable;

    private final CategoryListModel categories;
    private final Validator<RepeatingTransaction> validator;

    public AddRepeatingTransactionAction(JTable repeatingTransactionsTable, CategoryListModel categoryListModel, Validator<RepeatingTransaction> validator) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

        this.repeatingTransactionsTable = repeatingTransactionsTable;
        this.categories = categoryListModel;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionsTable.getModel();
        var repeatingTransactionDialog = new RepeatingTransactionDialog("New Repeating Transaction", categories, validator);
        var repeatingTransaction = repeatingTransactionDialog.showDialog();

        if (validator.validate(repeatingTransaction).isValid()) {
            repeatingTransactionTableModel.addRow(repeatingTransaction);
        }
    }
}
