package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.data.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.util.List;

public class CategoryValidator implements Validator<Category> {

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValid(Category category) {
        return category.getName() != null && !category.getName().isBlank();
    }

    @Override
    public ValidationResult validate(Category category) {
        var validators = List.of(
                Validator.extracting(
                        Category::getName, new StringLengthValidator(1, 50, "Category name")),
                Validator.extracting(
                        Category::getColorString, new StringLengthValidator(7, 50, "Category color"))
        );

        return Validator.compose(validators).validate(category);
    }
}
