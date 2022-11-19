package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.data.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.util.List;
import static cz.muni.fi.pv168.cashflowevidence.data.validation.Validator.extracting;


public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category category) {
        var validators = List.of(
                extracting(Category::getName, new StringLengthValidator(1, 50, "Category name")),
                extracting(Category::getColorString, new StringLengthValidator(7, 50, "Category color"))
        );

        return Validator.compose(validators).validate(category);
    }
}
