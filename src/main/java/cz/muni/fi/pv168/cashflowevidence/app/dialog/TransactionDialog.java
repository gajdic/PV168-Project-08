package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.privatejgoodies.common.base.SystemUtils;
import cz.muni.fi.pv168.cashflowevidence.app.InputNumberFormatter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.renderer.CategoryColorListCellRenderer;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * Class representing transaction dialog
 */
public class TransactionDialog extends AbstractTransactionDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JPanel buttonsPanel;
    private JPanel contentPanel;
    private JRadioButton incomingRadioButton;
    private JRadioButton outgoingRadioButton;
    private JTextField nameField;
    private JTextField descriptionField;
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel amountLabel;
    private JLabel dateLabel;
    private JLabel categoryLabel;
    private JComboBox<Category> categoryComboBox;
    private JFormattedTextField amountField;
    private DateTimePicker dateTimePicker;
    private Transaction transaction;
    private final Validator<Transaction> validator;

    /**
     * Constructor used to create new transaction
     * @param title Title used for the dialog
     */
    public TransactionDialog(String title, Repository<Category> categories, Validator<Transaction> validator) {
        super(title, categories);

        this.validator = validator;

        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 360));
    }

    /**
     * Constructor used to edit existing transaction
     * @param title Title used for the dialog
     * @param transaction Transaction that is being edited
     */
    public TransactionDialog(String title, Transaction transaction, Repository<Category> categories, Validator<Transaction> validator) {
        super(title, categories);
        this.transaction = transaction;
        this.validator = validator;

        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 360));
        setupForEdit();
    }

    private void setupForEdit() {
        if (transaction.getTransactionType() == TransactionType.Incoming) {
            incomingRadioButton.setSelected(true);
        } else {
            outgoingRadioButton.setSelected(true);
        }

        nameField.setText(transaction.getName());
        descriptionField.setText(transaction.getDescription());
        amountField.setText(String.format("%.2f", transaction.getAmount()));
        dateTimePicker.setDateTimeStrict(transaction.getDateTime());

        int index = getCategoriesList().indexOf(transaction.getCategory());
        categoryComboBox.setSelectedIndex(index + 1); // +1 to skip empty category
    }

    public Transaction showDialog() {
        setVisible(true);

        return transaction;
    }

    public void onSave() {
        if (this.transaction == null) {
            transaction = new Transaction();
        }

        Transaction tmpTransaction = prepareTmpTransaction();

        var validation = validator.validate(tmpTransaction);

        if (validation.isValid()) {
            //transaction.setAll(tmpTransaction);

            transaction.setTransactionType(incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing);
            transaction.setName(nameField.getText());
            transaction.setDescription(descriptionField.getText());
            transaction.setAmount(amountField.getText().isEmpty() ? -1 : Double.parseDouble(amountField.getText()
                    .replace("\u00a0", "").replace(",",
                            SystemUtils.IS_OS_WINDOWS ? "." : "")));
            transaction.setDate(dateTimePicker.getDateTimeStrict());
            transaction.setCategory((Category) categoryComboBox.getSelectedItem());
            dispose();
            return;
        }

        showWarningDialog(validation.getValidationErrors());
    }

    private Transaction prepareTmpTransaction() {

        return new Transaction(
                incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing,
                nameField.getText(),
                descriptionField.getText(),
                                amountField.getText().isEmpty() ? -1 : Double.parseDouble(amountField.getText()
                        .replace("\u00a0", "").replace(",",
                                                SystemUtils.IS_OS_WINDOWS ? "." : "")),
                dateTimePicker.getDateTimeStrict(),
                (Category) categoryComboBox.getSelectedItem());
    }

    private void createUIComponents() {
        categoryComboBox = new JComboBox<>();
        for (Category category : getCategoriesList()) {
            categoryComboBox.addItem(category);
        }
        categoryComboBox.insertItemAt(null, 0);
        categoryComboBox.setSelectedIndex(0);
        categoryComboBox.setRenderer(new CategoryColorListCellRenderer());

        dateTimePicker = new DateTimePicker();
        dateTimePicker.setDateTimeStrict(LocalDateTime.now());

        amountField = new JFormattedTextField(new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));
    }
}
