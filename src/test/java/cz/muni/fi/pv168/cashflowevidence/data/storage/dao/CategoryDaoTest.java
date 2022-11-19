package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CategoryDaoTest {
    private DatabaseManager manager;

    private CategoryDao categoryDao;

    private TransactionDao transactionDao;

    private RepeatingTransactionDao repeatingTransactionDao;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance();
        this.categoryDao = new CategoryDao(manager::getConnectionHandler);
        this.transactionDao = new TransactionDao(manager::getConnectionHandler);
        this.repeatingTransactionDao = new RepeatingTransactionDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllCategories() {
        var allCategories = categoryDao.findAll();
        assertThat(allCategories)
                .as("There should be 4 testing categories")
                .hasSize(4);
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
        var newCategoryParams = new CategoryEntity("category", "#bada55");

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
                .skip(allCategories.size() - 1).findFirst();

        assertThat(category).isPresent();

        categoryDao.deleteById(category.get().id());

        var categoriesAfterDelete = categoryDao.findAll();

        assertThat(categoriesAfterDelete.size()).isEqualTo(3);
    }

    @Test
    void deleteCategoryUsedByTransaction() {
        var allCategories = categoryDao.findAll();
        var category = allCategories.stream()
                .findFirst();

        assertThat(category).isPresent();

        categoryDao.deleteById(category.get().id());

        var categoriesAfterDelete = categoryDao.findAll();
        assertThat(categoriesAfterDelete.size()).isEqualTo(3);

        var transactionsAfterDelete = transactionDao.findAll();
        assertThat(transactionsAfterDelete.size()).isEqualTo(4);
        assertThat(transactionsAfterDelete.stream().filter(t -> t.categoryId() == 0).count()).isEqualTo(2);

        var repeatingTransactionsAfterDelete = repeatingTransactionDao.findAll();
        assertThat(repeatingTransactionsAfterDelete.size()).isEqualTo(4);
        assertThat(repeatingTransactionsAfterDelete.stream().filter(t -> t.categoryId() == 0).count()).isEqualTo(2);
    }

    @Test
    void deleteCategoryTwice() {
        var allCategories = categoryDao.findAll();
        var category = allCategories.stream()
                .skip(allCategories.size() - 1).findFirst()
                .orElseThrow();

        // delete once
        categoryDao.deleteById(category.id());

        // delete again -> should throw exception
        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> categoryDao.deleteById(category.id()));
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
