package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

public abstract class AbstractTransactionDialog extends JDialog {
    private final CategoryListModel categories;

    public AbstractTransactionDialog(String title, CategoryListModel categories) {
        super(CashFlowEvidence.frame, title);

        this.categories = categories;
    }

    protected void displayDialog(JPanel contentPane, JButton saveBtn, JButton cancelBtn, Dimension bounds) {
        setBounds(0, 0, bounds.width, bounds.height);
        setLocationRelativeTo(CashFlowEvidence.frame);
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(saveBtn);

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> onCancel());

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

    protected void onCancel() {
        dispose();
    }

    abstract void onSave();

    protected void showWarningDialog(String message) {
        Object[] options = { "OK" };
        JOptionPane.showOptionDialog(null, message, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
    }

    public CategoryListModel getCategoriesList() {
        return categories;
    }

    protected Transaction validateAndGetTransaction(TransactionValidator validator, Category category,
                                                  TransactionType transactionType, String amount,
                                                  String description, String name) {
        Transaction tmpTransaction = new Transaction();

        tmpTransaction.setCategory(category);
        tmpTransaction.setTransactionType(transactionType);
        tmpTransaction.setDate(LocalDateTime.now());
        try {
            tmpTransaction.setAmount(
                    Double.parseDouble(amount
                            // formatter adds '&nbsp;' to the text instead of space
                            .replace("\u00a0", "")
                            .replace(",", ".")
                    ));
        } catch (NumberFormatException ex) {
            validator.setErrorMessage("Amount cannot be empty");
            return null;
        }
        tmpTransaction.setDescription(description);
        tmpTransaction.setName(name);

        return validator.validate(tmpTransaction).isValid() ? tmpTransaction : null;
    }
}
