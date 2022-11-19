package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.TransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.TransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jan Gajda
 */
class TransactionRepositoryTest {

    private DatabaseManager databaseManager;

    private Repository<Transaction> transactionRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        transactionRepository = new TransactionRepository(
                new TransactionDao(databaseManager::getConnectionHandler),
                new TransactionMapper(id -> Optional.empty()));
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewTransaction() {
        Transaction transactionToCreate = new Transaction(
                TransactionType.Incoming,
                "Test name",
                "Test description",
                200.0,
                LocalDateTime.now(),
                new Category("Test category", Color.RED));
        transactionRepository.create(transactionToCreate);

        Transaction storedTransaction = transactionRepository
                .findByIndex(transactionRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedTransaction.getTransactionType()).isEqualTo(transactionToCreate.getTransactionType());
        assertThat(storedTransaction.getName()).isEqualTo(transactionToCreate.getName());
        assertThat(storedTransaction.getDescription()).isEqualTo(transactionToCreate.getDescription());
        assertThat(storedTransaction.getAmount()).isEqualTo(transactionToCreate.getAmount());
        assertThat(storedTransaction.getDateTime()).isEqualTo(transactionToCreate.getDateTime());
        assertThat(storedTransaction.getCategory()).isEqualTo(transactionToCreate.getCategory());
        assertThat(storedTransaction.getId()).isNotNull();
    }
}