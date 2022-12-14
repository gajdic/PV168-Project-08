package cz.muni.fi.pv168.cashflowevidence.renderer;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class CategoryColorListCellRenderer extends JLabel implements ListCellRenderer<Category> {

    @Override
    public Component getListCellRendererComponent(JList list, Category value, int index, boolean isSelected, boolean cellHasFocus) {
        setOpaque(true);

        if (value == null) {
            setText("None");
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);

            if (isSelected) {
                list.setSelectionBackground(Color.WHITE);
                list.setSelectionForeground(Color.BLACK);
            }
        } else {
            setText(value.getName());
            setBackground(value.getColor());
            setForeground(pickTextColorBasedOnBgColor(getBackground()));

            if (isSelected) {
                list.setSelectionBackground(getBackground());
                list.setSelectionForeground(pickTextColorBasedOnBgColor(getBackground()));
            }
        }

        return this;
    }

    // Calculates text color based on background contrast following the W3C guidelines
    // More info about values and how it is calculated at https://www.w3.org/TR/WCAG20/#relativeluminancedef
    private Color pickTextColorBasedOnBgColor(Color background) {
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
