package cz.muni.fi.pv168.cashflowevidence.app;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

/**
 * Serving as a common superclass form all overviews.
 */
public class AbstractOverview extends JPanel {

    protected final JButton editRowButton = new JButton("Edit selected");
    protected final JButton deleteRowButton = new JButton("Delete selected");

    /**
     * Sets the basic layout and borders
     */
    public void setLayout() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
    }

    protected JPanel rowActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(editRowButton);
        panel.add(deleteRowButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.EAST);

        return panel;
    }

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
