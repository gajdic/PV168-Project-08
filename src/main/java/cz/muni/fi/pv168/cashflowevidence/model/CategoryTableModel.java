package cz.muni.fi.pv168.cashflowevidence.model;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CategoryTableModel extends AbstractTableModel {

    private final List<Category> categories;

    private final List<Column<Category, ?>> columns = List.of(
            Column.editable("Name", String.class, Category::getName, Category::setName),
            Column.editable("Color", Color.class, Category::getColor, Category::setColor)
    );

    public CategoryTableModel(Collection<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(category);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, category);
    }

    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    public List<Category> getEntities() {
        return Collections.unmodifiableList(categories);
    }

    public void addRow(Category category) {
        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
}
