package cz.muni.fi.pv168.cashflowevidence.renderer;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        if (value == null) {
            setBackground(Color.WHITE);
            return this;
        }

        if (value.getClass().equals(Category.class)) {
            setBackground(((Category) value).getColor());
            return this;
        }

        setBackground((Color) value);
        return this;
    }
}
