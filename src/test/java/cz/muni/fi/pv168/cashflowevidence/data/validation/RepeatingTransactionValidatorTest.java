package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RepeatingTransactionValidatorTest {

    Category category = new Category("Jou", Color.RED);

    RepeatingTransactionValidator validator = new RepeatingTransactionValidator();

    @Test
    void validRepeatingTransactions() {
        List<RepeatingTransaction> repeatingTransactions = new ArrayList<>();

        repeatingTransactions.add(new RepeatingTransaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Daily
        ));

        repeatingTransactions.add(new RepeatingTransaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(8),
                4
        ));

        repeatingTransactions.add(new RepeatingTransaction(
                TransactionType.Outgoing,
                "name",
                "desc",
                100000,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Weekly
        ));

        for (RepeatingTransaction repeatingTransaction : repeatingTransactions) {
            ValidationResult result = validator.validate(repeatingTransaction);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void nullCategory() {
        RepeatingTransaction rt = new RepeatingTransaction(
                TransactionType.Outgoing,
                "name",
                "description",
                100000,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Daily
        );

        ValidationResult result = validator.validate(rt);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void customFrequency() {
        RepeatingTransaction rt = new RepeatingTransaction(
                TransactionType.Outgoing,
                "name",
                "description",
                100000,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(120),
                60
        );

        ValidationResult result = validator.validate(rt);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void invalidName() {
        List<RepeatingTransaction> repeatingTransactions = new ArrayList<>();

        repeatingTransactions.add(new RepeatingTransaction(
                TransactionType.Incoming,
                "",
                "desc",
                10000,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Daily
        ));

        repeatingTransactions.add(new RepeatingTransaction(
                TransactionType.Incoming,
                "a".repeat(101),
                "desc",
                10000,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Daily
        ));

        for (RepeatingTransaction rt : repeatingTransactions) {
            ValidationResult result = validator.validate(rt);
            assertThat(result.isValid()).isFalse();
        }
    }

    @Test
    void invalidAmount() {
        RepeatingTransaction repeatingTransaction = new RepeatingTransaction(
                TransactionType.Incoming,
                "test",
                "desc",
                -100,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                TransactionFrequency.Daily
        );

        ValidationResult result = validator.validate(repeatingTransaction);
        assertThat(result.isValid()).isFalse();
    }
}
