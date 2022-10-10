package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Serving as a common superclass form all overviews.
 */
public class AbstractOverview extends JPanel {

    /**
     * Method that creates a new image
     * @param rgb of the image
     * @return new image
     */
    public BufferedImage createImage(int rgb) {
        BufferedImage image = new BufferedImage(30, 10, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 10; j++) {
                image.setRGB(i, j, rgb);
            }
        }
        return image;
    }

    /**
     * Custom cell renderer
     */
    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                return (JLabel)value;
            }
            else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
}
