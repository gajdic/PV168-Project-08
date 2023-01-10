package cz.muni.fi.pv168.cashflowevidence.renderer;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.time.LocalDateTime;

public class DateTimeCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        LocalDateTime dateTime = (LocalDateTime) value;
        String formattedTime = String.format("%s:%02d", dateTime.getHour(), dateTime.getMinute());
        String formattedDateTime = String.format("%s %s", dateTime.toLocalDate(), formattedTime);

        return super.getTableCellRendererComponent(table, formattedDateTime, isSelected, hasFocus, rowIndex, vColIndex);
    }
}
