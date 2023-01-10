package cz.muni.fi.pv168.cashflowevidence.data.validation;


import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import javax.print.DocFlavor;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;


final class TransactionValidatorTest {

    Category category = new Category("Jou", Color.RED);

    TransactionValidator validator = new TransactionValidator();

    @Test
    void validTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.of(2020, 10, 25, 10, 30, 1),
                category));

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.now(),
                category));

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name2",
                "desc2",
                9999,
                LocalDateTime.now().minusDays(1),
                category));

        for (Transaction transaction : transactions) {
            ValidationResult result = validator.validate(transaction);
            assertThat(result.isValid()).isTrue();
        }
    }

    @Test
    void nullCategoryTransaction() {
        Transaction transaction = new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.of(2020, 10, 25, 10, 30, 1),
                null);

        ValidationResult result = validator.validate(transaction);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void nameTooLong() {
        Transaction transaction = new Transaction(
                TransactionType.Incoming,
                "a".repeat(101),
                "desc",
                10000000,
                LocalDateTime.of(2020, 10, 25, 10, 30, 1),
                category);

        ValidationResult result = validator.validate(transaction);
        assertThat(result.isValid()).isFalse();
    }

    @Test
    void negativeAmount() {
        Transaction transaction = new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                -100,
                LocalDateTime.of(2020, 10, 25, 10, 30, 1),
                category);

        ValidationResult result = validator.validate(transaction);
        assertThat(result.isValid()).isFalse();
    }

    @Test
    void invalidDates() {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                null,
                category));

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.now().plusDays(2),
                category));

        for (Transaction transaction : transactions) {
            ValidationResult result = validator.validate(transaction);
            assertThat(result.isValid()).isFalse();
        }
    }
}
