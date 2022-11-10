package cz.muni.fi.pv168.cashflowevidence.app;


import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Action;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class RepeatingTransactionsOverview extends AbstractOverview {

    public RepeatingTransactionsOverview(JTable repeatingTransactionsTable, ArrayList<Action> actions) {
        super(repeatingTransactionsTable, actions);
        setLayout();

        add(getTopPanel(), BorderLayout.NORTH);

        var scrollPane = new JScrollPane(overviewTable);
        scrollPane.setComponentPopupMenu(new RightClickMenu(actions));
        add(scrollPane);
    }
}
