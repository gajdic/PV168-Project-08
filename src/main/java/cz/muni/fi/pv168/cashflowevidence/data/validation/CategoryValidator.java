package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.model.Category;

public  class CategoryValidator {

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValid(Category category) {
        return category.getName() != null && !category.getName().isBlank();
    }
}
