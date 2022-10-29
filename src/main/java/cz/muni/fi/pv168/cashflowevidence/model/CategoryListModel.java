package cz.muni.fi.pv168.cashflowevidence.model;

import cz.muni.fi.pv168.cashflowevidence.model.Category;

import javax.swing.AbstractListModel;
import java.util.List;


public class CategoryListModel extends AbstractListModel<Category> {

    private final List<Category> categories;

    public CategoryListModel(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.get(index);
    }
}
