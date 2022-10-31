package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.TransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;


import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddTransactionAction extends AbstractAction {

    private final JTable transactionTable;
    private final CategoryListModel categories;

    public AddTransactionAction(JTable transactionTable, CategoryListModel categories) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));

        this.transactionTable = transactionTable;
        this.categories = categories;

        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        var transactionDialog = new TransactionDialog("New Transaction", categories);
        var transaction = transactionDialog.showDialog();

        if (transaction != null) {
            transactionTableModel.addRow(transaction);
        }
    }
}
