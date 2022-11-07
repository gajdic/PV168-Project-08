package cz.muni.fi.pv168.cashflowevidence.app;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionFrequency;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class RepeatingTransactionDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JPanel buttonsPanel;
    private JPanel contentPanel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField amountField;
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
    private CategoryListModel categories;

    public RepeatingTransactionDialog(String title, CategoryListModel categories) {
        super(CashFlowEvidence.frame, title);
        this.categories = categories;
        setupUIComponents();
        displayDialog();
    }

    private void displayDialog() {
        setBounds(0, 0, 600, 400);
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

    private void onSave() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void showDialog() {
        setVisible(true);
    }

    private void setupUIComponents() {
        setupCategories();
        setupIncomingOutgoing();
        setupFrequencies();
        setupDatePickers();
    }

    private void setupCategories() {
        for (Category category: categories.getCategories()) {
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

    private void setupFrequencies () {
        for (TransactionFrequency frequency : TransactionFrequency.values()) {
            frequencyComboBox.addItem(frequency);
        }
    }

    private void setupDatePickers() {
        dateFromPicker.setDate(LocalDate.now());
        dateToPicker.setDate(LocalDate.now().plusDays(1));
    }
}
