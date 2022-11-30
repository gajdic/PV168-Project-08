package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;

import java.time.LocalDateTime;

public class DateTimeValidator extends PropertyValidator<LocalDateTime> {

    public DateTimeValidator(String name) {
        super(name);
    }

    @Override
    public ValidationResult validate(LocalDateTime dateTime) {
        var result = new ValidationResult();

        if (dateTime == null || dateTime.isAfter(LocalDateTime.now())) {
            result.add("%s is invalid. Date cannot be after today"
                    .formatted(getName(dateTime)));
        }

        return result;
    }
}
