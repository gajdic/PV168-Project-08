package cz.muni.fi.pv168.cashflowevidence.app.overview;


import cz.muni.fi.pv168.cashflowevidence.app.RightClickMenu;
import cz.muni.fi.pv168.cashflowevidence.renderer.AmountCellRenderer;
import cz.muni.fi.pv168.cashflowevidence.renderer.DateTimeCellRenderer;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class RepeatingTransactionsOverview extends AbstractOverview {

    public RepeatingTransactionsOverview(JTable repeatingTransactionsTable, ArrayList<Action> actions) {
        super(repeatingTransactionsTable, actions);
        setLayout();

        JPanel topPanel = getTopPanel();
        topPanel.add(getCrudPanel(), BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(overviewTable);
        scrollPane.setComponentPopupMenu(new RightClickMenu(actions));
        add(scrollPane);

        setAmountCellRenderer();
        setDateTimeCellRenderer();
    }

    private void setAmountCellRenderer() {
        overviewTable.getColumnModel().getColumn(2).setCellRenderer(new AmountCellRenderer());
    }

    private void setDateTimeCellRenderer() {
        overviewTable.getColumnModel().getColumn(3).setCellRenderer(new DateTimeCellRenderer());
    }
}
