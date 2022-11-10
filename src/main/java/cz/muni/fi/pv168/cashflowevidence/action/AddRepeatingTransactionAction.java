package cz.muni.fi.pv168.cashflowevidence.action;


import cz.muni.fi.pv168.cashflowevidence.app.RepeatingTransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddRepeatingTransactionAction extends AbstractAction {

    private final JTable repeatingTransactionsTable;

    private final CategoryListModel categories;

    public AddRepeatingTransactionAction(JTable repeatingTransactionsTable, CategoryListModel categoryListModel) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

        this.repeatingTransactionsTable = repeatingTransactionsTable;
        this.categories = categoryListModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var repeatingTransactionDialog = new RepeatingTransactionDialog("New repeating transaction", categories);
        repeatingTransactionDialog.showDialog();
    }
}
