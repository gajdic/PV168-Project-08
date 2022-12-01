package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RepeatingTransactionDaoTest {
    private DatabaseManager manager;
    private RepeatingTransactionDao repeatingTransactionDao;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance();
        this.repeatingTransactionDao = new RepeatingTransactionDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllTransactions() {
        var reapeatingTransactions = repeatingTransactionDao.findAll();
        assertThat(reapeatingTransactions)
                .as("There should be 4 testing repeating transactions")
                .hasSize(4);
    }

    @Test
    void createNewRepeatingTransaction() {
        var params = new RepeatingTransactionEntity(
                1,
                "Transaction",
                "Description",
                9.99,
                TransactionType.Outgoing,
                LocalDateTime.now(),
                LocalDateTime.now(),

                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                TransactionFrequency.Daily
        );

        var createdRepeatingTransaction = repeatingTransactionDao.create(params);

        assertThat(createdRepeatingTransaction)
                .as("There should be a new transaction created")
                .isNotNull();

        assertThat(createdRepeatingTransaction.id())
                .isGreaterThan(1);


        assertThat(createdRepeatingTransaction).isEqualTo(params);

        var foundCreated = repeatingTransactionDao.findById(createdRepeatingTransaction.id());

        assertThat(foundCreated)
                .as("Created repeating transaction should be present")
                .isNotNull()
                .isEqualTo(createdRepeatingTransaction);
    }

    @Test
    void findRepeatingTransactionById() {
        var firstRepeatingTransaction = repeatingTransactionDao.findAll().
                stream().
                findFirst()
                .orElseThrow();

        var foundTransaction = repeatingTransactionDao.findById(firstRepeatingTransaction.id());

        assertThat(foundTransaction).isNotNull().isEqualTo(firstRepeatingTransaction);
    }

    @Test
    void deleteRepeatingTransaction() {
        var allRepeatingTransactions = repeatingTransactionDao.findAll();
        var repeatingTransaction = allRepeatingTransactions.stream()
                .findFirst();

        assertThat(repeatingTransaction).isNotNull();

        repeatingTransactionDao.deleteById(repeatingTransaction.map(RepeatingTransactionEntity::id).orElseThrow());

        var repeatingTransactionAfterDelete = repeatingTransactionDao.findAll();

        assertThat(repeatingTransactionAfterDelete.size()).isEqualTo(3);
    }

    @Test
    void deleteTransactionTwice() {
        var allRepeatingTransactions = repeatingTransactionDao.findAll();
        var repeatingTransaction = allRepeatingTransactions.stream()
                .findFirst()
                .orElseThrow();

        // delete once
        repeatingTransactionDao.deleteById(repeatingTransaction.id());

        // delete again -> should throw exception
        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> repeatingTransactionDao.deleteById(repeatingTransaction.id()))
                .withMessageContaining("Repeating transaction with id %d does not exist.".formatted(repeatingTransaction.id()));
    }

    @Test
    void deleteNotStoredTransaction() {
        var repeatingTransactionEntity = createRepeatingTransactionEntity();

        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> repeatingTransactionDao.deleteById(repeatingTransactionEntity.id()));
    }

    @Test
    void createNewRepeatingTransactionCustomFrequency() {
        var params = new RepeatingTransactionEntity(
                1,
                "Transaction",
                "Description",
                9.99,
                TransactionType.Outgoing,
                LocalDateTime.now(),
                LocalDateTime.now(),
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                TransactionFrequency.Custom
        );

        var createdRepeatingTransaction = repeatingTransactionDao.create(params);

        assertThat(createdRepeatingTransaction)
                .as("There should be a new transaction created")
                .isNotNull();

        assertThat(createdRepeatingTransaction.id())
                .isGreaterThan(1);



        assertThat(createdRepeatingTransaction).isEqualTo(params);

        var foundCreated = repeatingTransactionDao.findById(createdRepeatingTransaction.id());

        assertThat(foundCreated)
                .as("Created repeating transaction should be present")
                .isNotNull()
                .isEqualTo(createdRepeatingTransaction);
    }

    private static RepeatingTransactionEntity createRepeatingTransactionEntity() {
        return new RepeatingTransactionEntity(
                -1000L,
                1L,
                "Not inserted",
                "Description",
                9.99,
                TransactionType.Outgoing,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                TransactionFrequency.Daily
        );
    }



}
