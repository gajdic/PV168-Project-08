package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.app.InputNumberFormatter;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;

public class RepeatingTransactionDialog extends AbstractTransactionDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JPanel buttonsPanel;
    private JPanel contentPanel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JFormattedTextField amountField;
    private JRadioButton outgoingRadioButton;
    private JRadioButton incomingRadioButton;
    private JComboBox<TransactionFrequency> frequencyComboBox;
    private JLabel frequencyLabel;
    private JLabel repeatToLabel;
    private JLabel repeatFromLabel;
    private JLabel descriptionLabel;
    private JLabel nameLabel;
    private JLabel amountLabel;
    private DatePicker dateFromPicker;
    private DatePicker dateToPicker;
    private JComboBox<Category> categoryComboBox;
    private JFormattedTextField customFrequencyField;
    private JLabel customFrequencyLabel;
    private JPanel customFrequencyPanel;

    private RepeatingTransaction repeatingTransaction;

    public RepeatingTransactionDialog(String title, CategoryListModel categories) {
        super(title, categories);

        setupUIComponents();
        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 400));
    }

    public RepeatingTransactionDialog(String title, RepeatingTransaction repeatingTransaction, CategoryListModel categories) {
        super(title, categories);

        this.repeatingTransaction = repeatingTransaction;

        setupUIComponents();
        setupForEdit();
        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 400));
    }

    public void onSave() {
        if (repeatingTransaction == null) {
            repeatingTransaction = new RepeatingTransaction();
        }

        TransactionValidator validator = new TransactionValidator();
        RepeatingTransaction tmpTransaction = validateAndGetRepeatingTransaction(validator);

        if (tmpTransaction != null) {
            repeatingTransaction.setAll(tmpTransaction);
            dispose();
            return;
        }

        showWarningDialog(validator.getErrorMessage());
    }

    public RepeatingTransaction showDialog() {
        setVisible(true);

        return repeatingTransaction;
    }

    private void setupUIComponents() {
        setupHiddenElements();
        setupCategories();
        setupIncomingOutgoing();
        setupFrequencyComboBox();
        setupDatePickers();
    }

    private void setupForEdit() {
        if (repeatingTransaction.getTransactionType() == TransactionType.Incoming) {
            incomingRadioButton.setSelected(true);
        } else {
            outgoingRadioButton.setSelected(true);
        }

        nameField.setText(repeatingTransaction.getName());
        descriptionField.setText(repeatingTransaction.getDescription());
        amountField.setText(String.format("%.2f", repeatingTransaction.getAmount()));

        int index = Arrays.stream(getCategoriesList().getCategories()).toList().indexOf(repeatingTransaction.getCategory());
        categoryComboBox.setSelectedIndex(index + 1); // +1 to skip empty category

        dateFromPicker.setDate(repeatingTransaction.getDateFrom());
        dateToPicker.setDate(repeatingTransaction.getDateTo());

        frequencyComboBox.setSelectedItem(repeatingTransaction.getFrequency());
        if (repeatingTransaction.getFrequency() == TransactionFrequency.Custom) {
            customFrequencyField.setValue(repeatingTransaction.getDays() / 30);
        }
    }

    private void setupHiddenElements() {
        customFrequencyPanel.setVisible(false);
    }

    private void setupCategories() {
        for (Category category: getCategoriesList().getCategories()) {
            categoryComboBox.addItem(category);
        }
        categoryComboBox.insertItemAt(null, 0);
        categoryComboBox.setSelectedIndex(0);
    }

    private void setupIncomingOutgoing() {
        ButtonGroup group = new ButtonGroup();
        group.add(incomingRadioButton);
        group.add(outgoingRadioButton);
        outgoingRadioButton.setSelected(true);
    }

    private void setupFrequencyComboBox () {
        for (TransactionFrequency frequency : TransactionFrequency.values()) {
            frequencyComboBox.addItem(frequency);
        }
        frequencyComboBox.addActionListener(e -> onCustomFrequency());
    }

    private void setupDatePickers() {
        dateFromPicker.setDate(LocalDate.now());
        dateToPicker.setDate(LocalDate.now().plusDays(1));
    }

    private void onCustomFrequency() {
        if (frequencyComboBox.getSelectedItem() == TransactionFrequency.Custom) {
            setBounds(getX(), getY(), 600, 430);
            customFrequencyPanel.setVisible(true);
        }
        else {
            setBounds(getX(), getY(), 600, 400);
            customFrequencyPanel.setVisible(false);
        }
    }

    private RepeatingTransaction validateAndGetRepeatingTransaction(TransactionValidator validator) {
        Transaction tmpTransaction = validateAndGetTransaction(
                validator,
                (Category) categoryComboBox.getSelectedItem(),
                incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing,
                amountField.getText(),
                descriptionField.getText(),
                nameField.getText());

        if (tmpTransaction == null) {
            return null;
        }

        RepeatingTransaction tmpRepeatingTransaction = new RepeatingTransaction(
                tmpTransaction.getTransactionType(), tmpTransaction.getName(), tmpTransaction.getDescription(),
                tmpTransaction.getAmount(), tmpTransaction.getCategory(), dateFromPicker.getDate(), dateToPicker.getDate(),
                TransactionFrequency.Daily);

        tmpRepeatingTransaction.setFrequency((TransactionFrequency) frequencyComboBox.getSelectedItem());
        if (tmpRepeatingTransaction.getFrequency() == TransactionFrequency.Custom) {
            tmpRepeatingTransaction.setDays(
                    // formatter adds &ngsp; instead of spaces
                    Integer.parseInt(customFrequencyField.getText().replace("\u00a0", "")) * 30);
        } else {
            tmpRepeatingTransaction.setDays(tmpRepeatingTransaction.getFrequency().frequencyValue);
        }

        return validator.isValidRepeatingTransaction(tmpRepeatingTransaction) ? tmpRepeatingTransaction : null;
    }

    private void createUIComponents() {
        amountField = new JFormattedTextField(new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));
        customFrequencyField = new JFormattedTextField(new InputNumberFormatter(NumberFormat.getIntegerInstance()));
    }
}
