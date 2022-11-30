package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;

public final class NumberRangeValidator extends PropertyValidator<Integer> {

    private final int lowerBound;
    private final int upperBound;

    /**
     *
     * @param lowerBound inclusive lower bound
     * @param upperBound exclusive upper bound
     */
    public NumberRangeValidator(int lowerBound, int upperBound) {
        this(lowerBound, upperBound, null);
    }

    public NumberRangeValidator(int lowerBound, int upperBound, String name) {
        super(name);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public ValidationResult validate(Integer number) {
        var result = new ValidationResult();

        if ( lowerBound > number ||  number >= upperBound ) {
            result.add("%s is not between %d and %d"
                    .formatted(getName(number), lowerBound, upperBound)
            );
        }

        return result;
    }
}
