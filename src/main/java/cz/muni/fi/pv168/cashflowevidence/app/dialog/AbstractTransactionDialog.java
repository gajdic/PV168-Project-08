package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryListModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractTransactionDialog extends JDialog {
    private final Repository<Category> categories;

    public AbstractTransactionDialog(String title, Repository<Category> categories) {
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

    protected void showWarningDialog(List<String> messages) {
        Object[] options = { "OK" };
        String message = String.join("\n", messages);
        JOptionPane.showOptionDialog(null, message, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
    }

    public List<Category> getCategoriesList() {
        return categories.findAll();
    }
}
