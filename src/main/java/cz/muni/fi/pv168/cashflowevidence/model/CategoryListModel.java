package cz.muni.fi.pv168.cashflowevidence.model;

import javax.swing.AbstractListModel;
import java.util.Collections;
import java.util.List;


public class CategoryListModel extends AbstractListModel<Category> {

    private final List<Category> categories;

    public CategoryListModel(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void deleteCategory(int index) {
        this.categories.remove(index);
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.get(index);
    }

    public Category[] getCategories() {
        Category[] categoriesArr = new Category[categories.size()];
        return Collections.unmodifiableList(categories).toArray(categoriesArr);
    }
}
