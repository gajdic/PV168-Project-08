package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.action.AddCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.AddTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.DeleteCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.DeleteTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.EditCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.EditTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.ExitApplicationAction;
import cz.muni.fi.pv168.cashflowevidence.action.ExportAction;
import cz.muni.fi.pv168.cashflowevidence.action.HelpAction;
import cz.muni.fi.pv168.cashflowevidence.action.ImportAction;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents the main layout of CashFlowEvidence application
 */
public class CashFlowEvidence {
    public static final JFrame frame = getFrame();

    // file menu
    private final Action importAction = new ImportAction();
    private final Action exportAction = new ExportAction();
    private final Action exitAction = new ExitApplicationAction();

    private final Action addTAction;
    private final Action editTAction;
    private final Action deleteTAction;
    private final Action addCAction;
    private final Action editCAction;
    private final Action deleteCAction;

    private final Action helpAction = new HelpAction();


    // help menu
    private final JMenuItem infoItem = new JMenuItem("Info");
    
    private final JLabel balanceLabel = new JLabel();
    private final JLabel filteredBalanceLabel = new JLabel();

    private final Action openCategoryDialog = new AbstractAction("Add category") {
        @Override
        public void actionPerformed(ActionEvent e) {
            CategoryDialog categoryDialog = new CategoryDialog("New Category");
            categoryDialog.setVisible(true);
        }
    };

    public CashFlowEvidence() {
        addTAction = new AddTransactionAction(frame);
        editTAction = new EditTransactionAction(frame);
        deleteTAction = new DeleteTransactionAction(frame);

        addCAction = new AddCategoryAction(frame);
        editCAction = new EditCategoryAction(frame);
        deleteCAction = new DeleteCategoryAction(frame);

        setLookAndFeel();

        JPanel content = getContent();
        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);

    }

    private JPanel getContent(){
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(getMenuBar());
        content.add(Box.createRigidArea(new Dimension(1, 10)));
        content.add(getActionsAndBalancePanel());
        content.add(getTabbedContentPanel());
        return content;
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Font balanceFont = new Font("Courier", Font.BOLD, 20);
        filteredBalanceLabel.setFont(balanceFont);
        balanceLabel.setFont(balanceFont);

    }

    private JTable createTransactionTable(List<Transaction> transactions) {
        return createTable(new TransactionTableModel(transactions));
    }

    private JTable createTable(AbstractTableModel model) {
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        changeActionsState(count);
    }

    private void changeActionsState(int selectedItemsCount) {
        //editAction.setEnabled(selectedItemsCount == 1);
        //deleteAction.setEnabled(selectedItemsCount >= 1);
    }

    private static JFrame getFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(1600, 800));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Cash Flow Evidence");
        frame.setMinimumSize(new Dimension(1000, 400));

        return frame;
    }

    private JMenuBar getMenuBar() {

        JMenu fileMenu = getMenu("File", KeyEvent.VK_F,
                new ArrayList<>(Arrays.asList(importAction, exportAction, exitAction)));
        JMenu transactionMenu = getMenu("Transaction", KeyEvent.VK_T,
                new ArrayList<>(Arrays.asList(addTAction, editTAction, deleteTAction)));
        JMenu categoryMenu = getMenu("Category", KeyEvent.VK_C,
                new ArrayList<>(Arrays.asList(addCAction, editCAction, deleteCAction)));

        JMenu helpMenu = getMenu("Help", KeyEvent.VK_H,
                new ArrayList<>(List.of(helpAction)));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(transactionMenu);
        menuBar.add(categoryMenu);
        menuBar.add(helpMenu);
        menuBar.setMaximumSize(new Dimension(frame.getWidth(), 30));

        return menuBar;
    }


    private JMenu getMenu(String name, int keyEvent, List<Action> actions) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(keyEvent);
        for (Action action: actions) {
            menu.add(action);
        }
        return menu;
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));

        // temporary vals and currency
        balanceLabel.setText(Transaction.formatAmount(12345678) + "$");
        filteredBalanceLabel.setText(Transaction.formatAmount(987) + "$");

        panel.add(new JLabel("Total balance:"));
        panel.add(balanceLabel);
        panel.add(new JLabel("Filtered balance:"));
        panel.add(filteredBalanceLabel);

        return panel;
    }

    private JPanel getActionsAndBalancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getBalancePanel(), BorderLayout.WEST);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 50));
        panel.setBorder(new EmptyBorder(0, 10, 0, 10));

        return panel;
    }

    private JPanel getTabbedContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);

        tabbedPane.addTab("Transactions", new TransactionsOverview(
                new ArrayList<Action>(Arrays.asList(addTAction, editTAction, deleteTAction))));
        tabbedPane.addTab("Categories", new CategoriesOverview(
               new ArrayList<Action>(Arrays.asList(addCAction, editCAction, deleteCAction))));

        panel.add(tabbedPane);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 900));
        panel.setBorder(new EmptyBorder(0, 10, 0, 10));

        return panel;
    }

}
