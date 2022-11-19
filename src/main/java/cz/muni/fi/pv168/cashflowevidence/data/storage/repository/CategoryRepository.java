package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represent a repository for categories
 */
public class CategoryRepository implements Repository<Category> {

    private List<Category> categories = new ArrayList<>();

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Optional<Category> findByID(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> findByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void create(Category newEntity) {

    }

    @Override
    public void deleteByIndex(int index) {

    }
}
