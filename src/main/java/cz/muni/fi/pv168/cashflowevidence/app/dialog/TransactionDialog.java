package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import com.github.lgooddatepicker.components.DateTimePicker;
import cz.muni.fi.pv168.cashflowevidence.app.InputNumberFormatter;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;

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

    /**
     * Constructor used to create new transaction
     * @param title Title used for the dialog
     */
    public TransactionDialog(String title, CategoryListModel categories) {
        super(title, categories);

        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 360));
    }

    /**
     * Constructor used to edit existing transaction
     * @param title Title used for the dialog
     * @param transaction Transaction that is being edited
     */
    public TransactionDialog(String title, Transaction transaction, CategoryListModel categories) {
        super(title, categories);
        this.transaction = transaction;

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

        int index = Arrays.stream(getCategoriesList().getCategories()).toList().indexOf(transaction.getCategory());
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

        TransactionValidator validator = new TransactionValidator();
        Transaction tmpTransaction = validateAndGetTransaction(validator);

        if (tmpTransaction != null) {
            transaction.setAll(tmpTransaction);
            dispose();
            return;
        }

        showWarningDialog(validator.getErrorMessage());
    }

    private Transaction validateAndGetTransaction(TransactionValidator validator) {
        Transaction tmpTransaction = super.validateAndGetTransaction(validator,
                (Category) categoryComboBox.getSelectedItem(),
                incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing,
                amountField.getText(),
                descriptionField.getText(),
                nameField.getText());

        if (tmpTransaction == null) {
            return null;
        }

        return validator.isValidDateTime(dateTimePicker.getDateTimeStrict()) ? tmpTransaction : null;
    }


    private void createUIComponents() {
        categoryComboBox = new JComboBox<>(getCategoriesList().getCategories());
        categoryComboBox.insertItemAt(null, 0);
        categoryComboBox.setSelectedIndex(0);

        dateTimePicker = new DateTimePicker();
        dateTimePicker.setDateTimeStrict(LocalDateTime.now());

        amountField = new JFormattedTextField(new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));
    }
}
