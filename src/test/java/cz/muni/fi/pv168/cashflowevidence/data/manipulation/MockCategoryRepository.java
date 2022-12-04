package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.util.List;
import java.util.Optional;

public class MockCategoryRepository implements Repository<Category> {

    ExportImportTestData testData = new ExportImportTestData();
    Category category = testData.getRedCategory();
    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Optional<Category> findByID(long id) {
        return Optional.ofNullable(category) ;
    }

    @Override
    public Optional<Category> findByIndex(int index) {
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() {
        return List.of(category);
    }

    @Override
    public void create(Category newEntity) {
    }

    @Override
    public void deleteByIndex(int index) {
    }

    @Override
    public void update(Category entity) {
    }

    @Override
    public void refresh() {
    }
}
