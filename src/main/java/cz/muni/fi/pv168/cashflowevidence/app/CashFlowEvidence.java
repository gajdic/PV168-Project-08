package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

/**
 * Represents the main layout of CashFlowEvidence application
 */
public class CashFlowEvidence {
    private final JFrame frame = getFrame();

    // file menu
    private final JMenuItem importItem = new JMenuItem("Import");
    private final JMenuItem exportItem = new JMenuItem("Export");

    // help menu
    private final JMenuItem infoItem = new JMenuItem("Info");

    private final JButton transactionBtn = new JButton("Add transaction");
    private final JButton categoryBtn = new JButton("Add category");

    private final JLabel balanceLabel = new JLabel();
    private final JLabel filteredBalanceLabel = new JLabel();

    private final Action openTransactionDialog = new AbstractAction("Add transaction") {
        @Override
        public void actionPerformed(ActionEvent e) {
            TransactionDialog transactionDialog = new TransactionDialog(frame, "New Transaction");
            transactionDialog.setVisible(true);
        }
    };

    private final Action openCategoryDialog = new AbstractAction("Add category") {
        @Override
        public void actionPerformed(ActionEvent e) {
            CategoryDialog categoryDialog = new CategoryDialog(frame, "New Category");
            categoryDialog.setVisible(true);
        }
    };


    public CashFlowEvidence() {
        setLookAndFeel();

        Font balanceFont = new Font("Courier", Font.BOLD, 20);
        filteredBalanceLabel.setFont(balanceFont);
        balanceLabel.setFont(balanceFont);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(getMenuBar());
        content.add(Box.createRigidArea(new Dimension(1, 10)));
        content.add(getActionsAndBalancePanel());
        content.add(getTabbedContentPanel());

        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }
    private JFrame getFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(1920, 1080));
        frame.setLayout(new BorderLayout());
        frame.setTitle("Cash Flow Evidence");
        frame.setMinimumSize(new Dimension(1000, 400));

        return frame;
    }

    private JMenuBar getMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(importItem);
        fileMenu.add(exportItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(infoItem);

        JMenu transactionMenu = new JMenu("Transaction");
        transactionMenu.add("Add transaction");
        transactionMenu.add("Edit transaction");
        transactionMenu.add("Delete transaction");
        ((JMenuItem)transactionMenu.getMenuComponent(0)).setAction(openTransactionDialog);

        JMenu categoryMenu = new JMenu("Category");
        categoryMenu.add("Add category");
        categoryMenu.add("Edit category");
        categoryMenu.add("Delete category");
        ((JMenuItem)categoryMenu.getMenuComponent(0)).setAction(openCategoryDialog);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        menuBar.add(transactionMenu);
        menuBar.add(categoryMenu);
        menuBar.setMaximumSize(new Dimension(frame.getWidth(), 30));

        return menuBar;
    }

    private JPanel getActionsBarPanel() {
        JPanel panel = new JPanel();
        panel.add(transactionBtn);
        transactionBtn.setAction(openTransactionDialog);
        panel.add(categoryBtn);
        categoryBtn.setAction(openCategoryDialog);

        return panel;
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));

        // temporary
        balanceLabel.setText("1234567€");
        filteredBalanceLabel.setText("987€");

        panel.add(new JLabel("Total balance:"));
        panel.add(balanceLabel);
        panel.add(new JLabel("Filtered balance:"));
        panel.add(filteredBalanceLabel);

        return panel;
    }

    private JPanel getActionsAndBalancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getActionsBarPanel(), BorderLayout.WEST);
        panel.add(getBalancePanel(), BorderLayout.EAST);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 50));
        panel.setBorder(new EmptyBorder(0, 10, 0, 10));

        return panel;
    }

    private JPanel getTabbedContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);

        tabbedPane.addTab("Transactions", new TransactionsOverview());
        tabbedPane.addTab("Categories", new CategoriesOverview());

        panel.add(tabbedPane);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 900));
        panel.setBorder(new EmptyBorder(0, 10, 0, 10));

        return panel;
    }
}
