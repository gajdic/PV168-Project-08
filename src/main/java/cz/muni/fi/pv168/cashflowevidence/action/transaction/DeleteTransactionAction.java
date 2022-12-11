package cz.muni.fi.pv168.cashflowevidence.action.transaction;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DeleteTransactionAction extends DeleteAction {

    private final JTable transactionTable;


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

        List<Integer> transactionIndexes = new ArrayList<>();
        Arrays.stream(transactionTable.getSelectedRows())
                .map(transactionTable::convertRowIndexToModel)
                .boxed()
                .forEach(transactionIndexes::add);

        var sb = new StringBuilder("Are you sure you want to delete transactions: ");

        var idx = 0;
        while (idx < 5 && idx != transactionIndexes.size()) {
            sb.append(transactionTableModel.getEntities().get(transactionIndexes.get(idx)).getName()).append(", ");
            idx += 1;
        }
        sb.deleteCharAt(sb.toString().length()-2);

        var remainingCategories = transactionIndexes.size() - idx;
        if (remainingCategories > 0) {
            sb.append(String.format("and %d more ", remainingCategories));
        }
        sb.append("?");

        var deleteOption = confirmDelete(sb.toString());
        if (deleteOption == 0) {
            transactionIndexes.stream().sorted(Comparator.reverseOrder()).forEach(transactionTableModel::deleteRow);
        }
    }
}
