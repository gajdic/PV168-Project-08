package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.data.error.RuntimeApplicationException;

import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeApplicationException {
    private final List<String> validationErrors;

    public ValidationException(String userMessage, String message, List<String> validationErrors) {
        super(userMessage, message);
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message, List<String> validationErrors) {
        this("Validation failed", message, validationErrors);
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }
}
