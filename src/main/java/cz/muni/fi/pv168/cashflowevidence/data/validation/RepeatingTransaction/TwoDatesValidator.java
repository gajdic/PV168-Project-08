package cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.PropertyValidator;

import java.time.LocalDate;

public class TwoDatesValidator extends PropertyValidator<LocalDate> {

    private final LocalDate dateTo;
    private final Long id;

    public TwoDatesValidator(String name, LocalDate dateTo, Long id) {
        super(name);
        this.dateTo = dateTo;
        this.id = id;
    }

    @Override
    public ValidationResult validate(LocalDate dateFrom) {
        var result = new ValidationResult();

        if (dateFrom == null) {
            result.add("Repeat from field cannot be empty.");
            return result;
        }

        if (id == null && dateFrom.isBefore(LocalDate.now())) {
            result.add("Repeating transaction cannot start in the past.");
        }

        if (dateFrom.isAfter(dateTo)) {
            result.add("Repeat from date cannot be after Repeat to date");
        }

        return result;
    }
}
