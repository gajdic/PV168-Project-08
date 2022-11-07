package cz.muni.fi.pv168.cashflowevidence.renderer;

import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

public class AmountCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {

        String formattedAmount = Transaction.formatAmount((double) value);
        return super.getTableCellRendererComponent(table, formattedAmount, isSelected, hasFocus, rowIndex, vColIndex);
    }
}
