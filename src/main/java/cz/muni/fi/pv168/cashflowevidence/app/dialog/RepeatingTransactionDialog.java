package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.app.InputNumberFormatter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private Validator<RepeatingTransaction> validator;

    public RepeatingTransactionDialog(String title, Repository<Category> categoryRepository, Validator<RepeatingTransaction> validator) {
        super(title, categoryRepository);

        this.validator = validator;

        setupUIComponents();
        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 400));
    }

    public RepeatingTransactionDialog(String title, RepeatingTransaction repeatingTransaction, Repository<Category> categoryRepository, Validator<RepeatingTransaction> validator) {
        super(title, categoryRepository);

        this.repeatingTransaction = repeatingTransaction;
        this.validator = validator;

        setupUIComponents();
        setupForEdit();
        displayDialog(contentPane, saveBtn, cancelBtn, new Dimension(600, 400));
    }

    public void onSave() {
        if (repeatingTransaction == null) {
            repeatingTransaction = new RepeatingTransaction();
        }

        RepeatingTransaction tmpTransaction = prepareTmpTransaction();

        var validation = validator.validate(tmpTransaction);

        if (validation.isValid()) {
            repeatingTransaction.setAll(tmpTransaction);
            dispose();
            return;
        }

        showWarningDialog(validation.getValidationErrors().toString());
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

        int index = getCategoriesList().indexOf(repeatingTransaction.getCategory());
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
        for (Category category: getCategoriesList()) {
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
            if (frequency == TransactionFrequency.EachSecond) {
                continue;
            }
            frequencyComboBox.addItem(frequency);
        }
        frequencyComboBox.addActionListener(e -> onCustomFrequency());
    }

    private void setupDatePickers() {
        dateFromPicker.setDate(LocalDate.now());
        dateToPicker.setDate(LocalDate.now().plusDays(1));
    }

    private void onCustomFrequency() {
        if (frequencyComboBox.getSelectedItem() == TransactionFrequency.Custom || frequencyComboBox.getSelectedItem() == TransactionFrequency.EachSecond
        ) {
            setBounds(getX(), getY(), 600, 430);
            customFrequencyPanel.setVisible(true);
        }
        else {
            setBounds(getX(), getY(), 600, 400);
            customFrequencyPanel.setVisible(false);
        }
    }

    private RepeatingTransaction prepareTmpTransaction() {

        var tmpRepeatingTransaction = new RepeatingTransaction(
                incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing,
                nameField.getText(),
                descriptionField.getText(),
                amountField.getText().isEmpty() ? -1 : Double.parseDouble(amountField.getText()
                        .replace("\u00a0", "").replace(",", ".")),
                (Category) categoryComboBox.getSelectedItem(),
                dateFromPicker.getDate().atTime(LocalTime.now()),
                dateToPicker.getDate().atTime(LocalTime.now()),
                TransactionFrequency.Daily
        );

        tmpRepeatingTransaction.setFrequency((TransactionFrequency) frequencyComboBox.getSelectedItem());
        if (tmpRepeatingTransaction.getFrequency() == TransactionFrequency.Custom) {
            if (customFrequencyField.getText().isEmpty()) {
                tmpRepeatingTransaction.setDays(-1);
            } else {
                int multiplier = tmpRepeatingTransaction.getFrequency() == TransactionFrequency.Custom ? 30 : 1;
                tmpRepeatingTransaction.setDays(
                        // formatter adds &ngsp; instead of spaces
                        Integer.parseInt(customFrequencyField.getText().replace("\u00a0", "")) * multiplier);
            }
        } else {
            tmpRepeatingTransaction.setDays(tmpRepeatingTransaction.getFrequency().frequencyValue);
        }

        return tmpRepeatingTransaction;
    }

    private void createUIComponents() {
        amountField = new JFormattedTextField(new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));
        customFrequencyField = new JFormattedTextField(new InputNumberFormatter(NumberFormat.getIntegerInstance()));
    }
}
