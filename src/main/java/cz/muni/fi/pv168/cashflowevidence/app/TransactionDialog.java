package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionFrequency;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


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
    private JPanel datePanel;
    private JRadioButton repeatingTransactionRadioButton;
    private JPanel repeatingTransactionPanel;
    private JLabel repeatToLabel;
    private JLabel repeatFromLabel;
    private JLabel frequencyLabel;
    private JComboBox<TransactionFrequency> frequencyComboBox;
    private JPanel repeatFromPanel;
    private JPanel repeatToPanel;
    private JTextField customFrequencyField;
    private JLabel customFrequencyLabel;
    private Transaction transaction;

    /**
     * Constructor used to create new transaction
     * @param owner Frame which the dialog is bound to
     * @param title Title used for the dialog
     */
    public TransactionDialog(Frame owner, String title) {
        super(owner, title);
        displayDialog();
    }

    /**
     * Constructor used to edit existing transaction
     * @param owner Frame which the dialog is bound to
     * @param title Title used for the dialog
     * @param transaction Transaction that is being edited
     */
    public TransactionDialog(Frame owner, String title, Transaction transaction) {
        super(owner, title);
        displayDialog();

        this.transaction = transaction;
        //  todo for next milestone: fill values on edit
    }

    private void displayDialog() {
        setBounds(600, 300, 600, 350);
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
        repeatingTransactionRadioButton.addActionListener(e -> onRepeatingTransaction());
        frequencyComboBox.addActionListener(e -> onCustomFrequency());
    }

    private void setupHiddenElements() {
        repeatingTransactionPanel.setVisible(false);
        customFrequencyField.setVisible(false);
        customFrequencyLabel.setVisible(false);
    }

    private void onRepeatingTransaction() {
        if (repeatingTransactionRadioButton.isSelected()) {
            setBounds(getX(), getY(), 600, 480);
            repeatingTransactionPanel.setVisible(true);
        } else {
            setBounds(getX(), getY(), 600, 350);
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

    private void onSave() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        categoryComboBox = new JComboBox<>(getCategoriesForShowcase());
        frequencyComboBox = new JComboBox<>(TransactionFrequency.values());

        datePanel = new JPanel();
        datePanel.add(new JDatePicker());

        repeatFromPanel = new JPanel();
        repeatFromPanel.add(new JDatePicker());

        repeatToPanel = new JPanel();
        repeatToPanel.add(new JDatePicker());
    }

    // only for gui showcase
    private Category[] getCategoriesForShowcase() {
        String[] categoryNames = {"Zubar", "Lidl", "Kaufland", "NBSR"};
        Color[] categoryColors = {Color.GREEN, Color.BLUE, Color.RED, Color.BLACK};

        Category[] categories = new Category[4];

        for (int i = 0; i < 4; i++) {
            categories[i] = new Category(categoryNames[i], categoryColors[i]);
        }

        return categories;
    }
}
