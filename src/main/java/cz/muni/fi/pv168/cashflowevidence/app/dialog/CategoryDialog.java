package cz.muni.fi.pv168.cashflowevidence.app.dialog;

import cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class representing category dialog
 */
public class CategoryDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JLabel nameLabel;
    private JLabel colorLabel;
    private JTextField nameField;
    private JPanel contentPanel;
    private JPanel btnPanel;
    private JButton colorChooserBtn;
    private JPanel colorPanel;
    private JPanel colorChooserPanel;
    private Color color;
    private final Category category;
    private Validator<Category> validator;


    /**
     * Constructor used to create new categories
     * @param title Title used for the dialog
     */
    public CategoryDialog(String title, Validator<Category> validator) {
        super(CashFlowEvidence.frame, title);
        this.category = new Category();
        this.color = Color.RED;
        this.validator = validator;

        dialogSetup();
    }

    /**
     * Constructor used for editing existing categories
     * @param title Title used for the dialog
     * @param category Category which is being edited
     */
    public CategoryDialog(String title, Category category, Validator<Category> validator) {
        super(CashFlowEvidence.frame, title);
        this.category = category;
        this.validator = validator;

        dialogSetup();

        nameField.setText(category.getName());
        color = category.getColor();
    }

    /**
     * Shows the dialog and makes it possible to return category when dialog is closed.
     * @return Category, which was either created or edited
     */
    public Category showDialog() {
        setVisible(true);

        return category;
    }

    private void dialogSetup() {
        setBounds(0, 0, 600, 250);
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

    private void onSave() {
        var validation = validator.validate(new Category(nameField.getText(), color));

        if (!validation.isValid()) {
            Object[] options = { "OK", "CANCEL" };
            JOptionPane.showOptionDialog(null, String.join("\n",
                            validation.getValidationErrors()), "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return;
        }

        category.setName(nameField.getText());
        category.setColor(color);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        colorPanel = new JPanel();

        colorPanel.setBackground(Color.RED);
        if (this.category != null) {
            colorPanel.setBackground(category.getColor());
        }

        colorChooserBtn = new JButton();
        colorChooserBtn.addActionListener(e -> pickColor());
    }

    private void pickColor() {
        Color initialColor = Color.RED;

        if (this.category != null) {
            initialColor = category.getColor();
        }

        color = JColorChooser.showDialog(this, "Select a color", initialColor);
        colorPanel.setBackground(color);
    }
}
