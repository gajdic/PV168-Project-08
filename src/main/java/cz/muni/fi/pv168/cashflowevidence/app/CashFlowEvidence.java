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
import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionCSVSerializer;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.*;
import cz.muni.fi.pv168.cashflowevidence.model.*;
import cz.muni.fi.pv168.cashflowevidence.data.validation.CategoryValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;

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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents the main layout of CashFlowEvidence application
 */
public class CashFlowEvidence {
    public static final JFrame frame = getFrame();

    // file menu


    private final Action importAction;
    private final Action exportAction;
    private final Action exitAction = new ExitApplicationAction();
    private final Action importJsonAction;
    private final Action exportJsonAction;


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

    private final JTable categoryTable;
    private final JTable transactionTable;
    private final CategoryListModel categoryListModel;

    public CashFlowEvidence() {
        setBalanceFont();

        // tmp
        var categories = new ArrayList<Category>();
        var category = new Category("Jou", Color.RED);
        var category2 = new Category("Bu", Color.BLUE);
        categories.add(category);
        categories.add(category2);

        categoryTable = createCategoryTable(categories);

        // tmp
        var transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction(TransactionType.Incoming, "name", "desc", 10000000, LocalDateTime.now(), category));
        transactions.add(new Transaction(TransactionType.Incoming, "name2", "desc2", 9999, LocalDateTime.now().minusDays(1), category));
        transactions.add(new Transaction(TransactionType.Outgoing, "name3", "desc3", 189, LocalDateTime.now().minusDays(3), category));
        transactions.add(new Transaction(TransactionType.Outgoing, "name4", "desc4", 200, LocalDateTime.now().minusDays(7), category2));

        transactionTable = createTransactionTable(transactions);

        TransactionTableModel transactionTableModel = (TransactionTableModel) transactionTable.getModel();

        balanceLabel.setText(calculateBalance(transactionTableModel));

        transactionTable.getModel().addTableModelListener(e -> {
            balanceLabel.setText(calculateBalance(transactionTableModel));
            filteredBalanceLabel.setText(calculateBalance(transactionTableModel));
        });

        categoryListModel = new CategoryListModel(categories);

        addTAction = new AddTransactionAction(transactionTable, categoryListModel);
        editTAction = new EditTransactionAction(transactionTable, categoryListModel);
        deleteTAction = new DeleteTransactionAction(transactionTable);

        addCAction = new AddCategoryAction(categoryTable, categoryListModel);
        editCAction = new EditCategoryAction(categoryTable);
        deleteCAction = new DeleteCategoryAction(categoryTable, categoryListModel);

        TransactionImporter jsonImporter = new JsonTransactionImporter();
        TransactionExporter jsonExporter = new JsonTransactionExporter();
        TransactionImporter csvImporter = new TransactionPerLineImporter(new TransactionCSVSerializer());
        TransactionExporter csvExporter = new TransactionPerLineExporter(new TransactionCSVSerializer());


        importAction = new ImportAction(transactionTable, csvImporter);
        exportAction = new ExportAction(transactionTable, csvExporter);
        importJsonAction = new ImportAction(transactionTable, jsonImporter);
        exportJsonAction = new ExportAction(transactionTable, jsonExporter);

        initializeActionsState();

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

    private void setBalanceFont() {
        Font balanceFont = new Font("Courier", Font.BOLD, 20);
        filteredBalanceLabel.setFont(balanceFont);
        balanceLabel.setFont(balanceFont);
    }

    private JTable createCategoryTable(List<Category> categories) {
        var table = createTable(new CategoryTableModel(categories));
        table.getSelectionModel().addListSelectionListener(this::categoryRowSelectionChanged);

        return table;
    }

    private JTable createTransactionTable(List<Transaction> transactions) {
        var table = createTable(new TransactionTableModel(transactions));
        table.getSelectionModel().addListSelectionListener(this::transactionRowSelectionChanged);

        return table;
    }

    private JTable createTable(AbstractTableModel model) {
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::categoryRowSelectionChanged);

        return table;
    }

    private void categoryRowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();

        editCAction.setEnabled(count == 1);
        deleteCAction.setEnabled(count >= 1);
    }

    private void transactionRowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();

        editTAction.setEnabled(count == 1);
        deleteTAction.setEnabled(count >= 1);
    }

    private void initializeActionsState() {
        editTAction.setEnabled(false);
        deleteTAction.setEnabled(false);
        editCAction.setEnabled(false);
        deleteCAction.setEnabled(false);
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
                new ArrayList<>(Arrays.asList(importAction, importJsonAction, exportAction, exportJsonAction, exitAction)));
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

        tabbedPane.addTab("Transactions", new TransactionsOverview(transactionTable,
                new ArrayList<>(Arrays.asList(addTAction, editTAction, deleteTAction)), balanceLabel, filteredBalanceLabel, categoryListModel));
        tabbedPane.addTab("Categories", new CategoriesOverview(categoryTable,
               new ArrayList<>(Arrays.asList(addCAction, editCAction, deleteCAction))));


        panel.add(tabbedPane);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 900));
        panel.setBorder(new EmptyBorder(0, 10, 0, 10));

        return panel;
    }
    private String calculateBalance(TransactionTableModel tableModel) {
        double amount = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double cellAmount = tableModel.getEntity(i).getAmount();
            if (tableModel.getEntity(i).getTransactionType() == TransactionType.Incoming) {
                amount += cellAmount;
            }
            else {
                amount -= cellAmount;
            }
        }
        return Transaction.formatAmount(amount) + " $";
    }
}
