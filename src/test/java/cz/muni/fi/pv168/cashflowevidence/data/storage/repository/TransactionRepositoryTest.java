package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.TransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.TransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class TransactionRepositoryTest {

    private DatabaseManager databaseManager;

    private Repository<Transaction> transactionRepository;
    private Repository<Category> categoryRepository;


    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        transactionRepository = dependencyProvider.getTransactionRepository();
        categoryRepository = dependencyProvider.getCategoryRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewTransaction() {
        var category = categoryRepository.findByID(1).orElse(null);

        Transaction transactionToCreate = new Transaction(
                1L,
                TransactionType.Incoming,
                "Test name",
                "Test description",
                200.0,
                LocalDateTime.now(),
                category);
        transactionRepository.create(transactionToCreate);

        Transaction storedTransaction = transactionRepository
                .findByIndex(transactionRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedTransaction.getTransactionType()).isEqualTo(transactionToCreate.getTransactionType());
        assertThat(storedTransaction.getName()).isEqualTo(transactionToCreate.getName());
        assertThat(storedTransaction.getDescription()).isEqualTo(transactionToCreate.getDescription());
        assertThat(storedTransaction.getAmount()).isEqualTo(transactionToCreate.getAmount());
        assertThat(storedTransaction.getCategory()).isEqualTo(transactionToCreate.getCategory());
        assertThat(storedTransaction.getId()).isNotNull();
    }
}