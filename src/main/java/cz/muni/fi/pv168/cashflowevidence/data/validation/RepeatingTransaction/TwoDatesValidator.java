package cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.PropertyValidator;

import java.time.LocalDate;

public class TwoDatesValidator extends PropertyValidator<LocalDate> {

    private final LocalDate dateTo;

    public TwoDatesValidator(String name, LocalDate dateTo) {
        super(name);
        this.dateTo = dateTo;
    }

    @Override
    public ValidationResult validate(LocalDate dateFrom) {
        var result = new ValidationResult();

        if (dateFrom == null || dateFrom.isAfter(dateTo)) {
            result.add("Repeat from is either empty or after the Repeat to");
        }

        return result;
    }
}
