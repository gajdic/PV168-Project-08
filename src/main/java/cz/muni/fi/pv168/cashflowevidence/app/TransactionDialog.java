package cz.muni.fi.pv168.cashflowevidence.app;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.validation.TransactionValidator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * Class representing transaction dialog
 */
public class TransactionDialog extends JDialog {
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
    private JTextField amountField;
    private JPanel repeatingTransactionPanel;
    private JLabel repeatToLabel;
    private JLabel repeatFromLabel;
    private JLabel frequencyLabel;
    private JComboBox<TransactionFrequency> frequencyComboBox;
    private JTextField customFrequencyField;
    private JLabel customFrequencyLabel;
    private JCheckBox repeatingTransactionCheckBox;
    private DateTimePicker dateTimePicker;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private Transaction transaction;

    private CategoryListModel categories;

    /**
     * Constructor used to create new transaction
     * @param title Title used for the dialog
     */
    public TransactionDialog(String title, CategoryListModel categories) {
        super(CashFlowEvidence.frame, title);
        this.categories = categories;

        displayDialog();
    }

    /**
     * Constructor used to edit existing transaction
     * @param title Title used for the dialog
     * @param transaction Transaction that is being edited
     */
    public TransactionDialog(String title, Transaction transaction, CategoryListModel categories) {
        super(CashFlowEvidence.frame, title);
        this.transaction = transaction;
        this.categories = categories;

        displayDialog();
        setupForEdit();
    }

    private void setupForEdit() {
        nameField.setText(transaction.getName());
        descriptionField.setText(transaction.getDescription());
        amountField.setText(String.format("%.2f", transaction.getAmount()));
        dateTimePicker.setDateTimeStrict(transaction.getDateTime());

        int index = Arrays.stream(categories.getCategories()).toList().indexOf(transaction.getCategory());
        categoryComboBox.setSelectedIndex(index + 1); // +1 to skip empty category

        if (transaction.isRepeating()) {
            repeatingTransactionCheckBox.setSelected(true);
            fromDatePicker.setDate(transaction.getRepeatFrom());
            toDatePicker.setDate(transaction.getRepeatTo());
            fromDatePicker.setDate(transaction.getRepeatFrom());
            frequencyComboBox.setSelectedIndex(
                    Arrays.stream(TransactionFrequency.values()).toList().indexOf(transaction.getFrequencyType()));

            onRepeatingTransaction();

            if (transaction.getFrequencyType() == TransactionFrequency.Custom) {
                customFrequencyField.setText(String.valueOf(transaction.getFrequency()));
                onCustomFrequency();
            }
        }
    }

    private void displayDialog() {
        setBounds(0, 0, 600, 380);
        setLocationRelativeTo(CashFlowEvidence.frame);
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(saveBtn);

        addActionListenersOnSetup();
        setupHiddenElements();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void addActionListenersOnSetup() {
        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> onCancel());
        repeatingTransactionCheckBox.addActionListener(e -> onRepeatingTransaction());
        frequencyComboBox.addActionListener(e -> onCustomFrequency());
    }

    private void setupHiddenElements() {
        repeatingTransactionPanel.setVisible(false);
        customFrequencyField.setVisible(false);
        customFrequencyLabel.setVisible(false);
    }

    private void onRepeatingTransaction() {
        if (repeatingTransactionCheckBox.isSelected()) {
            setBounds(getX(), getY(), 600, 530);
            repeatingTransactionPanel.setVisible(true);
        } else {
            setBounds(getX(), getY(), 600, 380);
            repeatingTransactionPanel.setVisible(false);
        }
    }

    private void onCustomFrequency() {
        if (frequencyComboBox.getSelectedItem() == TransactionFrequency.Custom) {
            customFrequencyField.setVisible(true);
            customFrequencyLabel.setVisible(true);
        }
        else {
            customFrequencyField.setVisible(false);
            customFrequencyLabel.setVisible(false);
        }
    }

    public Transaction showDialog() {
        setVisible(true);

        return transaction;
    }

    private void onCancel() {
        dispose();
    }

    private void onSave() {
        if (this.transaction == null) {
            transaction = new Transaction();
        }

        var validatedTransaction = validateInputs();
        if (validatedTransaction == null) {
            Object[] options = { "OK" };
            JOptionPane.showOptionDialog(null, "Invalid values.", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return;
        }

        transaction.setAll(validatedTransaction);
        dispose();
    }

    private Transaction validateInputs() {
        var tmpTransaction = new Transaction();

        if (repeatingTransactionCheckBox.isSelected() && (tmpTransaction = validateRepeatingInputs()) == null) {
            return null;
        }

        tmpTransaction.setCategory((Category) categoryComboBox.getSelectedItem());
        tmpTransaction.setDate(dateTimePicker.getDateTimeStrict());
        tmpTransaction.setTransactionType(incomingRadioButton.isSelected() ? TransactionType.Incoming : TransactionType.Outgoing);

        try {
            tmpTransaction.setAmount(Double.parseDouble(amountField.getText()));
        } catch (NumberFormatException ex) {
            return null;
        }

        tmpTransaction.setDescription(descriptionField.getText());
        tmpTransaction.setName(nameField.getText());

        return TransactionValidator.isValid(tmpTransaction) ? tmpTransaction : null;
    }

    private Transaction validateRepeatingInputs() {
        var tmpTransaction = new Transaction();

        var frequency = (TransactionFrequency) frequencyComboBox.getSelectedItem();
        tmpTransaction.setFrequencyType(frequency);

        if (frequency == TransactionFrequency.Custom) {
            try {
                tmpTransaction.setFrequency(Integer.parseInt(customFrequencyField.getText()));
            } catch (NumberFormatException ex) {
                return null;
            }
        } else {
            tmpTransaction.setFrequency(frequency.frequencyValue);
        }

        tmpTransaction.setRepeatFrom(fromDatePicker.getDate());
        tmpTransaction.setRepeatTo(toDatePicker.getDate());

        return tmpTransaction;
    }

    private void createUIComponents() {
        categoryComboBox = new JComboBox<>(categories.getCategories());
        categoryComboBox.insertItemAt(null, 0);
        categoryComboBox.setSelectedIndex(0);
        frequencyComboBox = new JComboBox<>(TransactionFrequency.values());

        dateTimePicker = new DateTimePicker();
        dateTimePicker.setDateTimeStrict(LocalDateTime.now());

        fromDatePicker = new DatePicker();
        fromDatePicker.setDate(LocalDate.now());

        toDatePicker = new DatePicker();
        toDatePicker.setDate(LocalDate.now().plusDays(1));
    }
}
