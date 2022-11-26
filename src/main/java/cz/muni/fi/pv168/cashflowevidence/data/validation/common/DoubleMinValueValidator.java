package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;

public class DoubleMinValueValidator extends PropertyValidator<Double> {

    private final double min;

    public DoubleMinValueValidator(double min, String name) {
        super(name);
        this.min = min;
    }

    @Override
    public ValidationResult validate(Double value) {
        var result = new ValidationResult();

        if (value < 0) {
            result.add("'%s' cannot be less than %.0f"
                    .formatted(getName(value), min)
            );
        }

        return result;
    }
}
