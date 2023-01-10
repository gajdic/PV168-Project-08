package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DoubleMinValueValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleMinValueValidatorTest {

    DoubleMinValueValidator validator = new DoubleMinValueValidator(0, "Amount");

    @Test
    void validDouble() {

        List<Double> doubleList = new ArrayList<>();

        doubleList.add(100.0);
        doubleList.add(1111111.132);
        doubleList.add(0.1);

        for (double d : doubleList) {
            ValidationResult result = validator.validate(d);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void invalidDouble() {

        List<Double> doubleList = new ArrayList<>();

        doubleList.add(-0.1);
        doubleList.add(-132.3);

        for (double invalidDouble : doubleList) {
            ValidationResult result = validator.validate(invalidDouble);
            assertThat(result.isValid()).isFalse();
        }
    }
}
