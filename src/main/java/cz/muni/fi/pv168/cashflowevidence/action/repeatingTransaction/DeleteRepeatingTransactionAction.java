package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.action.DeleteAction;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DeleteRepeatingTransactionAction extends DeleteAction {

    private final JTable repeatingTransactionTable;


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

        List<Integer> transactionIndexes = new ArrayList<>();
        Arrays.stream(repeatingTransactionTable.getSelectedRows())
                .map(repeatingTransactionTable::convertRowIndexToModel)
                .boxed()
                .forEach(transactionIndexes::add);

        var sb = new StringBuilder("Are you sure you want to delete repeating transactions: ");
        if (transactionIndexes.size() == 1) {
            sb = new StringBuilder("Are you sure you want to delete repeating transaction ");
        }

        var idx = 0;
        while (idx < 5 && idx != transactionIndexes.size()) {
            sb.append(repeatingTransactionTableModel.getEntities().get(transactionIndexes.get(idx)).getName()).append(", ");
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
            try {
            transactionIndexes.stream().sorted(Comparator.reverseOrder()).forEach(repeatingTransactionTableModel::deleteRow);
            } catch (DataManipulationException ex) {
                JOptionPane.showMessageDialog(
                        null, "We were unable to delete the repeating transaction. Please try again.");
            }
        }
    }
}
