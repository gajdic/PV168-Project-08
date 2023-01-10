package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.StringLengthValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StringLenghtValidatorTest {

    StringLengthValidator validator = new StringLengthValidator(1, 10);

    @Test
    void validString() {

        String s  = "abcdefgh";

        ValidationResult result = validator.validate(s);

        assertThat(result.isValid()).isTrue();
    }

    @Test
    void stringTooLong() {

        String s = ("abcdefghijklmn");

        ValidationResult result = validator.validate(s);

        assertThat(result.isValid()).isFalse();

    }

    @Test
    void stringTooShort() {

        String s = "";

        ValidationResult result = validator.validate(s);

        assertThat(result.isValid()).isFalse();
    }
}
