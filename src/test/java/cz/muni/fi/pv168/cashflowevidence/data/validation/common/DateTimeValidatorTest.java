package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DateTimeValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeValidatorTest {

    DateTimeValidator validator = new DateTimeValidator("Date");

    @Test
    void validDate() {
        ValidationResult result = validator.validate(LocalDateTime.now());

        assertThat(result.isValid()).isTrue();
    }

    @Test
    void invalidDate() {

        List<LocalDateTime> dates = new ArrayList<>();

        dates.add(null);
        dates.add(LocalDateTime.now().plusDays(2));

        for (LocalDateTime date : dates) {
            ValidationResult result = validator.validate(date);
            assertThat(result.isValid()).isFalse();
        }
    }
}
