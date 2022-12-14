package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryValidatorTest {

    CategoryValidator validator = new CategoryValidator();

    @Test
    void validCategories() {

        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category("Jou", Color.RED));
        categoryList.add(new Category("b", Color.GREEN));
        categoryList.add(new Category("a".repeat(50), Color.CYAN));

        for (Category category : categoryList) {
            ValidationResult result = validator.validate(category);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void invalidCategories() {

        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category());
        categoryList.add(new Category("a".repeat(51), Color.RED));

        for (Category category : categoryList) {
            ValidationResult result = validator.validate(category);
            assertThat(result.isValid()).isFalse();
        }
    }
}
