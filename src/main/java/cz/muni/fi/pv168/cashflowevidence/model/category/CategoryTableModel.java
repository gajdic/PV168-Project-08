package cz.muni.fi.pv168.cashflowevidence.model.category;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.CategoryValidator;
import cz.muni.fi.pv168.cashflowevidence.model.Column;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CategoryTableModel extends AbstractTableModel {

    private final Repository<Category> categories;

    private final List<Column<Category, ?>> columns = List.of(
            Column.editable("Name", String.class, Category::getName, Category::setName),
            Column.editable("Color", Color.class, Category::getColor, Category::setColor)
    );

    public CategoryTableModel(Repository<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getRowCount() {
        return categories.getSize();
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
        return categories.findByIndex(rowIndex).orElseThrow();
    }

    public List<Category> getEntities() {
        return categories.findAll();
    }

    public void addRow(Category category) {
        int newRowIndex = categories.getSize();
        categories.create(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(int rowIndex, Category category) {
        categories.update(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        categories.deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
