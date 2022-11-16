package cz.muni.fi.pv168.cashflowevidence.data.validation;


import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


// TODO: Test whole functionality
final class TransactionValidatorTest {

    Category category = new Category("Jou", Color.RED);

    @Test
    void validTransaction() {

        List<Transaction> transactions = new ArrayList<Transaction>();


        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.of(2020, 10,25, 10, 30, 1),
                category));

        transactions.add(new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.now(),
                category));

        transactions.add(new Transaction(TransactionType.Incoming, "name2", "desc2", 9999, LocalDateTime.now().minusDays(1), category));

        for (Transaction transaction: transactions) {
            assertThat(TransactionValidator.isValid(transaction)).isTrue();
        }

    }

    @Test
    void repeatingRepeatFromAfterTo() {

        Transaction transaction = new Transaction(
                TransactionType.Incoming,
                "name",
                "desc",
                10000000,
                LocalDateTime.of(2020, 10,25, 10, 30, 1),
                category,
                LocalDate.of(2020, 10,25),
                LocalDate.of(2010, 10,25),
                TransactionFrequency.Daily
                );

        assertThat(TransactionValidator.isValid(transaction)).isFalse();
    }
}