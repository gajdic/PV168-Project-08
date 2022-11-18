package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CategoryDaoTest {
    private DatabaseManager manager;

    private CategoryDao categoryDao;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance();
        this.categoryDao = new CategoryDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllCategories() {
        var allCategories = categoryDao.findAll();
        assertThat(allCategories)
                .as("There should be 3 testing categories")
                .hasSize(3);
    }

    @Test
    void findCategoryById() {
        var firstCategory = categoryDao.findAll().
                stream().
                findFirst()
                .orElseThrow();

        var foundCategory = categoryDao.findById(firstCategory.id());

        assertThat(foundCategory).isNotNull().isEqualTo(firstCategory);
    }

    @Test
    void createNewCategory() {
        var newCategoryParams = new CategoryEntity("category", "red");

        var createdCategory = categoryDao.
                create(newCategoryParams);

        assertThat(createdCategory)
                .as("There should be a new category created")
                .isNotNull();
        assertThat(createdCategory.id()).isGreaterThan(1);
        assertThat(createdCategory.name()).isEqualTo(newCategoryParams.name());
        assertThat(createdCategory.color()).isEqualTo(newCategoryParams.color());

        var allCategories = categoryDao.findAll();

        assertThat(allCategories)
                .as("The newly created category has to be stored in the database")
                .contains(createdCategory);
    }

    @Test
    void deleteCategory() {
        var allCategories = categoryDao.findAll();
        var category = allCategories.stream()
                .findFirst();

        assertThat(category).isNotNull();

        categoryDao.deleteById(category.map(CategoryEntity::id).orElseThrow());

        var categoryAfterDelete = categoryDao.findAll().stream().findFirst();

        assertThat(categoryAfterDelete).isEmpty();
    }

    @Test
    void deleteCategoryTwice() {
        var allCategories = categoryDao.findAll();
        var category = allCategories.stream()
                .findFirst()
                .orElseThrow();

        // delete once
        categoryDao.deleteById(category.id());

        // delete again -> should throw exception
        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> categoryDao.deleteById(category.id()))
                .withMessageContaining("not found");
    }

    @Test
    void deleteNotStoredCategory() {
        var categoryEntity = createCategoryEntity();

        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> categoryDao.deleteById(categoryEntity.id()));
    }

    private static CategoryEntity createCategoryEntity() {
        return new CategoryEntity(
                -1000L,
                "this category is not stored",
                "red"
        );
    }
}
