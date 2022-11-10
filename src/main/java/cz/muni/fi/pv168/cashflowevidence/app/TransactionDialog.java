package cz.muni.fi.pv168.cashflowevidence.app;

import com.github.lgooddatepicker.components.DateTimePicker;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;

import javax.swing.JButton;
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
    private DateTimePicker dateTimePicker;
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
        if (transaction.getTransactionType() == TransactionType.Incoming) {
            incomingRadioButton.setSelected(true);
        } else {
            outgoingRadioButton.setSelected(true);
        }

        nameField.setText(transaction.getName());
        descriptionField.setText(transaction.getDescription());
        amountField.setText(String.format("%.2f", transaction.getAmount()).replace(",", "."));
        dateTimePicker.setDateTimeStrict(transaction.getDateTime());

        int index = Arrays.stream(categories.getCategories()).toList().indexOf(transaction.getCategory());
        categoryComboBox.setSelectedIndex(index + 1); // +1 to skip empty category
    }

    private void displayDialog() {
        setBounds(0, 0, 600, 360);
        setLocationRelativeTo(CashFlowEvidence.frame);
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(saveBtn);

        addActionListenersOnSetup();

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

        Transaction validatedTransaction = validateInputs();
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
        Transaction tmpTransaction = new Transaction();

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


    private void createUIComponents() {
        categoryComboBox = new JComboBox<>(categories.getCategories());
        categoryComboBox.insertItemAt(null, 0);
        categoryComboBox.setSelectedIndex(0);

        dateTimePicker = new DateTimePicker();
        dateTimePicker.setDateTimeStrict(LocalDateTime.now());
    }
}
