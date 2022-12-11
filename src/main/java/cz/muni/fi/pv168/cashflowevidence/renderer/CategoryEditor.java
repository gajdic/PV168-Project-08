package cz.muni.fi.pv168.cashflowevidence.renderer;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

import static cz.muni.fi.pv168.cashflowevidence.renderer.CategoryColorListCellRenderer.pickTextColorBasedOnBgColor;

public class CategoryEditor extends BasicComboBoxEditor {
    private final JPanel panel = new JPanel();
    private final JLabel labelItem = new JLabel();
    private Category selectedValue;

    public CategoryEditor() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);

        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        labelItem.setForeground(Color.WHITE);

        panel.add(labelItem, constraints);
    }

    public Component getEditorComponent() {
        return this.panel;
    }

    public Object getItem() {
        return this.selectedValue;
    }

    public void setItem(Object item) {
        if (item == null) {
            labelItem.setText("None");
            labelItem.setForeground(Color.BLACK);
            panel.setBackground(Color.WHITE);
            return;
        }
        Category category = (Category) item;
        selectedValue = category;
        labelItem.setText(selectedValue.getName());
        labelItem.setForeground(pickTextColorBasedOnBgColor(selectedValue.getColor()));
        panel.setBackground(category.getColor());
    }
}
