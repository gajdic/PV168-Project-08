package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteRepeatingTransactionAction extends DeleteAction {

    private final JTable repeatingTransactionTable;
    private boolean yesToAll = false;


    public DeleteRepeatingTransactionAction(JTable repeatingTransactionTable) {
        super();

        this.repeatingTransactionTable = repeatingTransactionTable;

        putValue(SHORT_DESCRIPTION, "Deletes selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionTable.getModel();
        Arrays.stream(repeatingTransactionTable.getSelectedRows())
                .map(repeatingTransactionTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(index -> {
                    var deleteOption = -1;

                    if (!yesToAll) {
                        var message = String.format("Are you sure you want to delete repeating transaction %s?",
                                repeatingTransactionTableModel.getEntity(index).getName());

                        deleteOption = confirmDelete(message);
                        yesToAll = deleteOption == 1;
                    }

                    if (deleteOption == 0 || yesToAll) {
                        repeatingTransactionTableModel.deleteRow(index);
                    }
                });

        yesToAll = false;
    }
}
