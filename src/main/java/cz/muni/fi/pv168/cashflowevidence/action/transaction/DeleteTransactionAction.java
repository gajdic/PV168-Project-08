package cz.muni.fi.pv168.cashflowevidence.action.transaction;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteTransactionAction extends DeleteAction {

    private final JTable transactionTable;
    private boolean yesToAll = false;


    public DeleteTransactionAction(JTable transactionTable) {
        super();

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
                .forEach(index -> {
                    var deleteOption = -1;

                    if (!yesToAll) {
                        var message = String.format("Are you sure you want to delete transaction %s?",
                                transactionTableModel.getEntity(index).getName());

                        deleteOption = confirmDelete(message);
                        yesToAll = deleteOption == 1;
                    }

                    if (deleteOption == 0 || yesToAll) {
                        transactionTableModel.deleteRow(index);
                    }
                });

        yesToAll = false;
    }
}
