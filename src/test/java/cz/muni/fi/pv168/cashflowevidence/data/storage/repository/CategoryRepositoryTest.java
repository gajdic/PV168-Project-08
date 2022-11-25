package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.CategoryDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.CategoryMapper;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


final class CategoryRepositoryTest {

    private DatabaseManager databaseManager;

    private Repository<Category> categoryRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        categoryRepository = dependencyProvider.getCategoryRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewCategory() {
        Category categoryToCreate = new Category(
                "Groceries",
                Color.CYAN);
        categoryRepository.create(categoryToCreate);

        Category storedCategory = categoryRepository
                .findByIndex(categoryRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedCategory.getName()).isEqualTo(categoryToCreate.getName());
        assertThat(storedCategory.getColor()).isEqualTo(categoryToCreate.getColor());
        assertThat(storedCategory.getId()).isNotNull();
    }

    @Test
    void updateCategory() {
        Category category = findGreenCategory();

        assertThat(category.getColor()).isNotEqualTo(Color.GREEN);

        category.setColor(Color.GREEN);
        categoryRepository.update(category);

        Category updatedCategory = findGreenCategory();

        assertThat(updatedCategory.getColor()).isEqualTo(Color.GREEN);
    }

    private Category findGreenCategory() {
        return categoryRepository
                .findAll()
                .stream()
                .filter(c -> c.getName().equals("GreenCategory"))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void findAll() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(4);
    }

    @Test
    void findByIndex() {
        Optional<Category> category = categoryRepository.findByIndex(0);
        assertThat(category).isPresent();
    }

    @Test
    void IdTooHigh() {
        Optional<Category> category = categoryRepository.findByIndex(Integer.MAX_VALUE);
        assertThat(category.isPresent()).isFalse();
    }

    @Test
    void deleteByIndex() {
        categoryRepository.deleteByIndex(0);
        assertThat(categoryRepository.getSize()).isEqualTo(3);
    }
}