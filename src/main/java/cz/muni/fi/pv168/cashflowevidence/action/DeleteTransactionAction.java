package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteTransactionAction extends AbstractAction {

    private final JTable transactionTable;

    public DeleteTransactionAction(JTable transactionTable) {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));

        this.transactionTable = transactionTable;

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var transactionTableModel = (TransactionTableModel) transactionTable.getModel();
        Arrays.stream(transactionTable.getSelectedRows())
                .map(transactionTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(transactionTableModel::deleteRow);
    }
}
