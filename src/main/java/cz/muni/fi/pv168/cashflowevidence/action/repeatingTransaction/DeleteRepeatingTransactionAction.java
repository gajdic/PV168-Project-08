package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteRepeatingTransactionAction extends AbstractAction {

    private final JTable repeatingTransactionTable;

    public DeleteRepeatingTransactionAction(JTable repeatingTransactionTable) {
        super("Delete", UIManager.getIcon("FileChooser.fileIcon"));

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
                .forEach(repeatingTransactionTableModel::deleteRow);
    }
}
