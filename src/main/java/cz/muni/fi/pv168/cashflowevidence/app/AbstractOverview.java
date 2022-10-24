package cz.muni.fi.pv168.cashflowevidence.app;


import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Serving as a common superclass form all overviews.
 */
abstract public class AbstractOverview extends JPanel {

    private final List<Action> actions;
    protected final JTable overviewTable;

    public AbstractOverview(JTable overviewTable, ArrayList<Action> actions) {
        // TODO: Actions now need JFrame frame, which is defined in CategoriesOverview,
        // so they are passed here as parameter. This may change with the introduction of table models.
        this.actions = actions;
        this.overviewTable = overviewTable;
        this.setComponentPopupMenu(new RightClickMenu(actions));
        this.overviewTable.setInheritsPopupMenu(true);


    }

    /**
     * Sets the basic layout and borders
     */
    public void setLayout() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));

    }

    protected void addActionsToPanel(JPanel panel) {

        for (Action action: actions) {
            JButton button = new JButton();
            button.setAction(action);
            panel.add(button);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.EAST);
    }

    protected JScrollPane scrollPane() {
        JScrollPane scrollPane = new JScrollPane(overviewTable);
        scrollPane.setInheritsPopupMenu(true);
        return scrollPane;
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
