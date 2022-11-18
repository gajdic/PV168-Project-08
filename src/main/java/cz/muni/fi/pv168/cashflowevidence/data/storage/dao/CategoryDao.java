package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;

import java.util.Collection;

public class CategoryDao implements DatabaseAccessObject<CategoryEntity> {
    @Override
    public CategoryEntity create(CategoryEntity entity) {
        return null;
    }

    @Override
    public void deleteById(long id) {}

    @Override
    public Collection<CategoryEntity> findAll() {
        return null;
    }

    @Override
    public CategoryEntity findById(long id) {
        return null;
    }
}
