package cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.app.dialog.RepeatingTransactionDialog;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransactionTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditRepeatingTransactionAction extends AbstractAction {

    private final JTable repeatingTransactionTable;
    private final Repository<Category> categoryRepository;
    private final Validator<RepeatingTransaction> validator;

    public EditRepeatingTransactionAction(JTable repeatingTransactionTable, Repository<Category> categoryRepository, Validator<RepeatingTransaction> validator) {
        super("Edit", UIManager.getIcon("FileChooser.fileIcon"));
        putValue(SHORT_DESCRIPTION, "Edits selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));

        this.repeatingTransactionTable = repeatingTransactionTable;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int[] selectedRows = repeatingTransactionTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }

        var repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionTable.getModel();
        int modelRow = repeatingTransactionTable.convertRowIndexToModel(selectedRows[0]);

        var repeatingTransactionDialog = new RepeatingTransactionDialog(
                "Edit Transaction", repeatingTransactionTableModel.getEntity(modelRow), categoryRepository, validator);
        var transaction = repeatingTransactionDialog.showDialog();
        if (validator.validate(transaction).isValid()) {
            repeatingTransactionTableModel.updateRow(modelRow, transaction);
        }
    }
}