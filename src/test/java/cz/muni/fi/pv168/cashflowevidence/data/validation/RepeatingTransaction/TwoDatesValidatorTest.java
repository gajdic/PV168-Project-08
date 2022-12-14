package cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TwoDatesValidatorTest {

    LocalDate dateTo = LocalDate.now().plusYears(10);
    TwoDatesValidator validator = new TwoDatesValidator("DateTo", dateTo);

    @Test
    void validDates() {

        List<LocalDate> dates = new ArrayList<>();
        dates.add(dateTo.minusDays(3));
        dates.add(dateTo.minusYears(7));

        for (LocalDate date : dates) {
            ValidationResult result = validator.validate(date);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void pastStart() {
        LocalDate date = LocalDate.now().minusDays(1);

        ValidationResult result = validator.validate(date);

        assertThat(result.isValid()).isFalse();
    }

    @Test
    void DateFromAfterDateTo() {
        LocalDate date = dateTo.plusDays(1);

        ValidationResult result = validator.validate(date);

        assertThat(result.isValid()).isFalse();
    }

    @Test
    void nullDateFrom() {

        LocalDate date = null;

        ValidationResult result = validator.validate(date);

        assertThat(result.isValid()).isFalse();
    }
}
