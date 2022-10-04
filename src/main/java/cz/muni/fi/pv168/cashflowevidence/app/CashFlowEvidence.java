package cz.muni.fi.pv168.cashflowevidence.app;

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
import javax.swing.WindowConstants;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;

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

    private final JButton transactionBtn = new JButton("Transaction");
    private final JButton categoryBtn = new JButton("Category");

    private final JLabel balanceLabel = new JLabel();
    private final JLabel filteredBalanceLabel = new JLabel();

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
        content.add(getFilteredBalancePanel());

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

        return frame;
    }

    private JMenuBar getMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(importItem);
        fileMenu.add(exportItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(infoItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        menuBar.setMaximumSize(new Dimension(frame.getWidth(), 30));

        return menuBar;
    }

    private JPanel getActionsBarPanel() {
        JPanel panel = new JPanel();
        panel.add(transactionBtn);
        panel.add(categoryBtn);
        categoryBtn.addActionListener(e ->
        {
            CategoryDialog categoryDialog = new CategoryDialog(frame, "New Category");
            categoryDialog.setVisible(true);
        });

        return panel;
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel();

        balanceLabel.setText("1234567€"); // temporary

        panel.add(new JLabel("Total balance:"));
        panel.add(balanceLabel);

        return panel;
    }

    private JPanel getActionsAndBalancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getActionsBarPanel(), BorderLayout.WEST);
        panel.add(getBalancePanel(), BorderLayout.EAST);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 50));

        return panel;
    }

    private JPanel getTabbedContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);

        String[] titles = new String[]{"Transactions", "Categories"};
        for (String title : titles) {
            // temporary panels to fill up the space in TabbedPane
            JPanel fillerPanel = new JPanel();
            fillerPanel.add(new Label("Useless tab belonging to " + title));
            tabbedPane.addTab(title, fillerPanel);
        }

        panel.add(tabbedPane);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 800));

        return panel;
    }

    private JPanel getFilteredBalancePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        filteredBalanceLabel.setText("987€"); // temporary

        panel.add(new JLabel("Filtered balance:"));
        panel.add(filteredBalanceLabel);
        panel.setMaximumSize(new Dimension(frame.getWidth(), 200));

        return panel;
    }
}
