package cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction;

import cz.muni.fi.pv168.cashflowevidence.data.validation.ValidationResult;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FrequencyValidatorTest {

    FrequencyValidator validator = new FrequencyValidator("Frequency", 0);

    @Test
    void validFrequency() {

        List<TransactionFrequency> frequencies = new ArrayList<>();

        frequencies.add(TransactionFrequency.Custom);
        frequencies.add(TransactionFrequency.EverySixMonths);
        frequencies.add(TransactionFrequency.valueOf("Weekly"));

        for (TransactionFrequency f : frequencies) {
            ValidationResult result = validator.validate(f);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void invalidFrequency() {
        try {
            RepeatingTransaction rt = new RepeatingTransaction(
                    TransactionType.Outgoing,
                    "name",
                    "description",
                    100000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(50),
                    -3
            );

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Frequency cannot be smaller or equal to 0.");
        }

    }
}
