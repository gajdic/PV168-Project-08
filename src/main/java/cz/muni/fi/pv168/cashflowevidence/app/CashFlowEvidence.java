package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.action.ExitApplicationAction;
import cz.muni.fi.pv168.cashflowevidence.action.ExportAction;
import cz.muni.fi.pv168.cashflowevidence.action.HelpAction;
import cz.muni.fi.pv168.cashflowevidence.action.ImportAction;
import cz.muni.fi.pv168.cashflowevidence.action.category.AddCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.category.DeleteCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.category.EditCategoryAction;
import cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction.AddRepeatingTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction.DeleteRepeatingTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.repeatingTransaction.EditRepeatingTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.transaction.AddTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.transaction.DeleteTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.action.transaction.EditTransactionAction;
import cz.muni.fi.pv168.cashflowevidence.app.overview.CategoriesOverview;
import cz.muni.fi.pv168.cashflowevidence.app.overview.RepeatingTransactionsOverview;
import cz.muni.fi.pv168.cashflowevidence.app.overview.TransactionsOverview;
import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionCSVSerializer;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.JsonTransactionExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.JsonTransactionImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionPerLineExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionPerLineImporter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.category.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.*;
import cz.muni.fi.pv168.cashflowevidence.service.RepeatingTaskScheduler;
import cz.muni.fi.pv168.cashflowevidence.wiring.DependencyProvider;

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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


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
    private final Repository<Category> categories;
    private final Repository<Transaction> transactions;
    private final Repository<RepeatingTransaction> repeatingTransactions;
    private Action addTAction;
    private Action editTAction;
    private Action deleteTAction;

    private Action addRTAction;
    private Action editRTAction;
    private Action deleteRTAction;

    private Action addCAction;
    private Action editCAction;
    private Action deleteCAction;

    private final Action helpAction = new HelpAction();

    // help menu
    private final JMenuItem infoItem = new JMenuItem("Info");
    
    private final JLabel balanceLabel = new JLabel();
    private final JLabel filteredBalanceLabel = new JLabel();

    private final JTable categoryTable;

    private final JTable transactionTable;

    private final JTable repeatingTransactionTable;

    private final RepeatingTaskScheduler repeatingTaskScheduler;

    public CashFlowEvidence(DependencyProvider dependencyProvider) {
        setBalanceFont();

        this.categories = dependencyProvider.getCategoryRepository();
        this.transactions = dependencyProvider.getTransactionRepository();
        this.repeatingTransactions = dependencyProvider.getRepeatingTransactionRepository();

        categoryTable = createCategoryTable();
        repeatingTransactionTable = createRepeatingTransactionTable();
        transactionTable = createTransactionTable();

        TransactionTableModel transactionTableModel = (TransactionTableModel) transactionTable.getModel();

        balanceLabel.setText(calculateBalance(transactionTableModel));

        transactionTable.getModel().addTableModelListener(e -> {
            balanceLabel.setText(calculateBalance(transactionTableModel));
            filteredBalanceLabel.setText(calculateBalance(transactionTableModel));
        });

        initializeActions(dependencyProvider);

        TransactionImporter jsonImporter = new JsonTransactionImporter(categories);
        TransactionExporter jsonExporter = new JsonTransactionExporter();
        TransactionImporter csvImporter = new TransactionPerLineImporter(new TransactionCSVSerializer(), categories);
        TransactionExporter csvExporter = new TransactionPerLineExporter(new TransactionCSVSerializer());


        importAction = new ImportAction(transactionTable, csvImporter);
        exportAction = new ExportAction(transactionTable, csvExporter);
        importJsonAction = new ImportAction(transactionTable, jsonImporter);
        exportJsonAction = new ExportAction(transactionTable, jsonExporter);

        repeatingTaskScheduler = new RepeatingTaskScheduler(dependencyProvider, transactionTableModel);
        repeatingTaskScheduler.scheduleExistingTransactions();
        repeatingTaskScheduler.setListenerForNewRepeatingTransaction(repeatingTransactionTable);

        initializeActionsState();

        JPanel content = getContent();
        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeActions(DependencyProvider dependencyProvider) {
        var categoryValidator = dependencyProvider.getCategoryValidator();
        var transactionValidator = dependencyProvider.getTransactionValidator();
        var repeatingTransactionValidator = dependencyProvider.getRepeatingTransactionValidator();

        addTAction = new AddTransactionAction(transactionTable, categories, transactionValidator);
        editTAction = new EditTransactionAction(transactionTable,categories, transactionValidator);
        deleteTAction = new DeleteTransactionAction(transactionTable);

        addRTAction = new AddRepeatingTransactionAction(repeatingTransactionTable, categories, repeatingTransactionValidator);
        editRTAction = new EditRepeatingTransactionAction(repeatingTransactionTable, categories, repeatingTransactionValidator);
        deleteRTAction = new DeleteRepeatingTransactionAction(repeatingTransactionTable);

        addCAction = new AddCategoryAction(categoryTable, categoryValidator);
        editCAction = new EditCategoryAction(categoryTable, categoryValidator);
        deleteCAction = new DeleteCategoryAction(categoryTable, transactionTable);
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

    private JTable createCategoryTable() {
        var table = createTable(new CategoryTableModel(this.categories));
        table.getSelectionModel().addListSelectionListener(this::categoryRowSelectionChanged);

        return table;
    }

    private JTable createRepeatingTransactionTable() {
        var table = createTable(new RepeatingTransactionTableModel(this.repeatingTransactions));
        table.getSelectionModel().addListSelectionListener(this::repeatingTransactionRowSelectionChanged);

        return table;
    }

    private JTable createTransactionTable() {
        var table = createTable(new TransactionTableModel(this.transactions));
        table.getSelectionModel().addListSelectionListener(this::transactionRowSelectionChanged);

        return table;
    }

    private JTable createTable(AbstractTableModel model) {
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);

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

    private void repeatingTransactionRowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();

        editRTAction.setEnabled(count == 1);
        deleteRTAction.setEnabled(count >= 1);
    }

    private void initializeActionsState() {
        editTAction.setEnabled(false);
        deleteTAction.setEnabled(false);

        editCAction.setEnabled(false);
        deleteCAction.setEnabled(false);

        deleteRTAction.setEnabled(false);
        editRTAction.setEnabled(false);
    }

    private static JFrame getFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(1600, 800));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Cash Flow Evidence");
        frame.setMinimumSize(new Dimension(1600, 400));

        return frame;
    }

    private JMenuBar getMenuBar() {

        JMenu fileMenu = getMenu("File", KeyEvent.VK_F,
                new ArrayList<>(Arrays.asList(importAction, importJsonAction, exportAction, exportJsonAction, exitAction)));
        JMenu transactionMenu = getMenu("Transaction", KeyEvent.VK_T,
                new ArrayList<>(Arrays.asList(addTAction, editTAction, deleteTAction)));
        JMenu repeatingTransactionMenu = getMenu("Repeating Transaction", KeyEvent.VK_R,
                new ArrayList<>(Arrays.asList(addRTAction, editRTAction, deleteRTAction)));
        JMenu categoryMenu = getMenu("Category", KeyEvent.VK_C,
                new ArrayList<>(Arrays.asList(addCAction, editCAction, deleteCAction)));

        JMenu helpMenu = getMenu("Help", KeyEvent.VK_H,
                new ArrayList<>(List.of(helpAction)));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(transactionMenu);
        menuBar.add(repeatingTransactionMenu);
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
                new ArrayList<>(Arrays.asList(addTAction, editTAction, deleteTAction)), balanceLabel, filteredBalanceLabel, categories));
        //TODO actions
        tabbedPane.addTab("Repeating transactions", new RepeatingTransactionsOverview(repeatingTransactionTable,
                new ArrayList<>(Arrays.asList(addRTAction, editRTAction, deleteRTAction))));
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
        return Transaction.formatAmount(amount) + " €";
    }

    //TODO, imports
    private void refresh() {
        categories.refresh();
        transactions.refresh();
        repeatingTransactions.refresh();
    }
}
