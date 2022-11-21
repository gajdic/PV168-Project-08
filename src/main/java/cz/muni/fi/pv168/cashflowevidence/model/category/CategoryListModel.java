package cz.muni.fi.pv168.cashflowevidence.model.category;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;

import javax.swing.AbstractListModel;
import java.util.Collections;
import java.util.List;


public class CategoryListModel extends AbstractListModel<Category> {

    private final Repository<Category> categories;

    public CategoryListModel(Repository<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getSize() {
        return categories.getSize();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.findByIndex(index).orElseThrow();
    }

    public Category[] getCategories() {
        Category[] categoriesArr = new Category[categories.getSize()];
        return Collections.unmodifiableList(categories.findAll()).toArray(categoriesArr);
    }
}
