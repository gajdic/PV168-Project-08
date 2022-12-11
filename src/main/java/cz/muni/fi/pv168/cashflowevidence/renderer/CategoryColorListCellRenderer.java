package cz.muni.fi.pv168.cashflowevidence.renderer;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.*;
import java.awt.*;

public class CategoryColorListCellRenderer implements ListCellRenderer<Category> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Category value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        if (value == null) {
            renderer.setBackground(Color.WHITE);
            renderer.setText("None");
            return renderer;
        }

        renderer.setBackground(value.getColor());
        renderer.setForeground(pickTextColorBasedOnBgColor(renderer.getBackground()));
        return renderer;
    }

    // Calculates text color based on background contrast following the W3C guidelines
    // More info about values and how it is calculated at https://www.w3.org/TR/WCAG20/#relativeluminancedef
    public static Color pickTextColorBasedOnBgColor(Color background) {
        double[] rgb = new double[] {background.getRed(), background.getGreen(), background.getBlue()};
        double[] calculatedRGB = new double[3];

        for (int i = 0; i < rgb.length; i++) {
            double part = rgb[i];
            part = part / 255;
            calculatedRGB[i] = part <= 0.03928 ? part / 12.92 : Math.pow((part + 0.055) / 1.055 , 2.4);
        }

        // relative luminance
        var lum = (0.2126 * calculatedRGB[0]) + (0.7152 * calculatedRGB[1]) + (0.0722 * calculatedRGB[2]);

        return (lum > 0.179) ? Color.BLACK : Color.WHITE;
    }
}
