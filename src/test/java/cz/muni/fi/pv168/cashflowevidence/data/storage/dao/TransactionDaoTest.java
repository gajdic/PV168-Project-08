package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TransactionDaoTest {
    private DatabaseManager manager;
    private TransactionDao transactionDao;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance();
        this.transactionDao = new TransactionDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllTransactions()  {
        var transactions = transactionDao.findAll();
        assertThat(transactions)
                .as("There should be 4 testing transactions")
                .hasSize(4);
    }

    @Test
    void createNewTransaction()  {
        var params = new TransactionEntity(
                1L,
                "Transaction",
                "Description",
                9.99,
                TransactionType.Outgoing,
                LocalDateTime.now()
        );

        var createdTransaction = transactionDao.create(params);

        assertThat(createdTransaction)
                .as("There should be a new transaction created")
                .isNotNull();

        assertThat(createdTransaction.id())
                .isGreaterThan(1);

        assertThat(createdTransaction.name()).isEqualTo(params.name());
        assertThat(createdTransaction.description()).isEqualTo(params.description());
        assertThat(createdTransaction.amount()).isEqualTo(params.amount());
        assertThat(createdTransaction.type()).isEqualTo(params.type());
        assertThat(createdTransaction.categoryId()).isEqualTo(params.categoryId());

        var foundCreated = transactionDao.findById(createdTransaction.id());

        assertThat(foundCreated)
                .as("Created transaction should be present")
                .isNotNull()
                .isEqualTo(createdTransaction);
    }

    @Test
    void findTransactionById() {
        var firstTransaction = transactionDao.findAll().
                stream().
                findFirst()
                .orElseThrow();

        var foundTransaction = transactionDao.findById(firstTransaction.id());

        assertThat(foundTransaction).isNotNull().isEqualTo(firstTransaction);
    }

    @Test
    void deleteTransaction() {
        var allTransactions = transactionDao.findAll();
        var transaction = allTransactions.stream()
                .findFirst();

        assertThat(transaction).isNotNull();

        transactionDao.deleteById(transaction.map(TransactionEntity::id).orElseThrow());

        var transactionAfterDelete = transactionDao.findAll();

        assertThat(transactionAfterDelete.size()).isEqualTo(3);
    }

    @Test
    void deleteTransactionTwice() {
        var allTransactions = transactionDao.findAll();
        var transaction = allTransactions.stream()
                .findFirst()
                .orElseThrow();

        // delete once
        transactionDao.deleteById(transaction.id());

        // delete again -> should throw exception
        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> transactionDao.deleteById(transaction.id()))
                .withMessageContaining("Transaction with id %d does not exist.".formatted(transaction.id()));
    }

    @Test
    void deleteNotStoredTransaction() {
        var transactionEntity = createTransactionEntity();

        assertThatExceptionOfType(DataStorageException.class)
                .isThrownBy(() -> transactionDao.deleteById(transactionEntity.id()));
    }

    private static TransactionEntity createTransactionEntity() {
        return new TransactionEntity(
                -1000L,
                1L,
                "transaction",
                "description",
                9.99,
                TransactionType.Outgoing,
                LocalDateTime.now()
        );
    }
}
