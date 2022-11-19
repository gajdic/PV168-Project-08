package cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.PropertyValidator;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;

public class FrequencyValidator extends PropertyValidator<TransactionFrequency> {

    private final int days;

    public FrequencyValidator(String name, int days) {
        super(name);
        this.days = days;
    }

    @Override
    public ValidationResult validate(TransactionFrequency frequency) {
        var result = new ValidationResult();

        if (frequency == TransactionFrequency.Custom && days < 0) {
            result.add("'%s' Months cannot be less than 0."
                    .formatted(getName(frequency)));
        }

        return result;
    }
}