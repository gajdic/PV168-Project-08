package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.TransactionDialog;


import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


// TODO: Consider parametrization of either add/edit/delete actions or
// TODO: Category/Transaction action based on shared implementation
public class AddTransactionAction extends AbstractAction {

    private final JFrame frame;

    public AddTransactionAction(JFrame frame) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Adds new record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        TransactionDialog transactionDialog = new TransactionDialog(frame, "New Transaction");
        transactionDialog.setVisible(true);
    }
}
