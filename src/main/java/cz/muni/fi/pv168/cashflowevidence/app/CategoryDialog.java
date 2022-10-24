package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.Category;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class representing category dialog
 */
public class CategoryDialog extends JDialog {
    // for showcase
    private static final Color[] COLORS =
            {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE};
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
    private Category category;

    /**
     * Constructor used to create new categories
     * @param owner Frame which the dialog is bound to
     * @param title Title used for the dialog
     */
    public CategoryDialog(Frame owner, String title) {
        super(owner, title);
        dialogSetup();
    }

    /**
     * Constructor used for editing existing categories
     * @param owner Frame which the dialog is bound to
     * @param title Title used for the dialog
     * @param category Category which is being edited
     */
    public CategoryDialog(Frame owner, String title, Category category) {
        super(owner, title);
        dialogSetup();

        this.category = category;
        nameField.setText(category.getName());
    }

    private void dialogSetup() {
        setBounds(600, 300, 600, 250);
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

        Color color = JColorChooser.showDialog(this, "Select a color", initialColor);
        colorPanel.setBackground(color);
    }
}
