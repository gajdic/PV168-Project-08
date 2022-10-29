package cz.muni.fi.pv168.cashflowevidence.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;

public class DateTimeCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        LocalDateTime dateTime = (LocalDateTime) value;
        String formattedDateTime = String.format("%s %s", dateTime.toLocalDate(), dateTime.toLocalTime());

        return super.getTableCellRendererComponent(table, formattedDateTime, isSelected, hasFocus, rowIndex, vColIndex);
    }
}
