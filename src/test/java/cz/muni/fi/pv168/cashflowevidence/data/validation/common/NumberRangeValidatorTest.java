package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.NumberRangeValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberRangeValidatorTest {

    NumberRangeValidator validator = new NumberRangeValidator(100, 200, "Range");

    @Test
    void validNumber() {

        List<Integer> list = new ArrayList<>();

        list.add(132);
        list.add(199);
        list.add(100);

        for (int i : list) {
            ValidationResult result = validator.validate(i);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void invalidNumber() {

        List<Integer> list = new ArrayList<>();

        list.add(87);
        list.add(299);
        list.add(200);

        for (int i : list) {
            ValidationResult result = validator.validate(i);
            assertThat(result.isValid()).isFalse();
        }
    }
}
