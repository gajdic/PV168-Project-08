package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.RepeatingTransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.TransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.RepeatingTransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.TransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class RepeatingTransactionRepositoryTest {

    private DatabaseManager databaseManager;

    private Repository<RepeatingTransaction> repeatingTransactionRepository;
    private Repository<Category> categoryRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        repeatingTransactionRepository = dependencyProvider.getRepeatingTransactionRepository();
        categoryRepository = dependencyProvider.getCategoryRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewRepeatingTransaction() {
        var category = categoryRepository.findByID(1).orElse(null);

        RepeatingTransaction repTransactionToCreate = new RepeatingTransaction(
                TransactionType.Outgoing,
                "Test name",
                "Test description",
                238.0,

                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                TransactionFrequency.Daily);

        repeatingTransactionRepository.create(repTransactionToCreate);

        RepeatingTransaction storedRepTransaction = repeatingTransactionRepository
                .findByIndex(repeatingTransactionRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedRepTransaction.getTransactionType()).isEqualTo(repTransactionToCreate.getTransactionType());
        assertThat(storedRepTransaction.getName()).isEqualTo(repTransactionToCreate.getName());
        assertThat(storedRepTransaction.getDescription()).isEqualTo(repTransactionToCreate.getDescription());
        assertThat(storedRepTransaction.getAmount()).isEqualTo(repTransactionToCreate.getAmount());
        assertThat(storedRepTransaction.getCategory()).isEqualTo(repTransactionToCreate.getCategory());
        assertThat(storedRepTransaction.getDateFrom()).isEqualTo(repTransactionToCreate.getDateFrom());
        assertThat(storedRepTransaction.getDateTo()).isEqualTo(repTransactionToCreate.getDateTo());
        assertThat(storedRepTransaction.getFrequency()).isEqualTo(repTransactionToCreate.getFrequency());
        assertThat(storedRepTransaction.getId()).isNotNull();
    }

    @Test
    void createNewRepeatingTransactionCustomFrequency() {
        var category = categoryRepository.findByID(1).orElse(null);

        RepeatingTransaction repTransactionToCreate = new RepeatingTransaction(
                TransactionType.Outgoing,
                "Test name",
                "Test description",
                238.0,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                10);

        repeatingTransactionRepository.create(repTransactionToCreate);

        RepeatingTransaction storedRepTransaction = repeatingTransactionRepository
                .findByIndex(repeatingTransactionRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedRepTransaction.getTransactionType()).isEqualTo(repTransactionToCreate.getTransactionType());
        assertThat(storedRepTransaction.getName()).isEqualTo(repTransactionToCreate.getName());
        assertThat(storedRepTransaction.getDescription()).isEqualTo(repTransactionToCreate.getDescription());
        assertThat(storedRepTransaction.getAmount()).isEqualTo(repTransactionToCreate.getAmount());
        assertThat(storedRepTransaction.getCategory()).isEqualTo(repTransactionToCreate.getCategory());
        assertThat(storedRepTransaction.getDateFrom()).isEqualTo(repTransactionToCreate.getDateFrom());
        assertThat(storedRepTransaction.getDateTo()).isEqualTo(repTransactionToCreate.getDateTo());
        assertThat(storedRepTransaction.getFrequency()).isEqualTo(repTransactionToCreate.getFrequency());
        assertThat(storedRepTransaction.getDays()).isEqualTo(repTransactionToCreate.getDays());
        assertThat(storedRepTransaction.getId()).isNotNull();
    }


    @Test
    void updateRepeatingTransaction() {
        var category = categoryRepository.findByID(1).orElse(null);

        RepeatingTransaction repTransactionCreate = new RepeatingTransaction(
                TransactionType.Outgoing,
                "Test name",
                "Test description",
                238.0,
                category,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                TransactionFrequency.Daily);

        repeatingTransactionRepository.create(repTransactionCreate);

        int sizeBefore = repeatingTransactionRepository.getSize();

        RepeatingTransaction repTransactionUpdate = repeatingTransactionRepository
                .findByIndex(repeatingTransactionRepository.getSize() - 1) // get latest
                .orElseThrow();
        repTransactionUpdate.setNextCreateAt(LocalDateTime.now());
        repeatingTransactionRepository.update(repTransactionUpdate);

        assertThat(repeatingTransactionRepository.getSize()).isEqualTo(sizeBefore);


        RepeatingTransaction storedRepTransaction = repeatingTransactionRepository
                .findByIndex(repeatingTransactionRepository.getSize() - 1) // get latest
                .orElseThrow();

        assertThat(storedRepTransaction.getTransactionType()).isEqualTo(repTransactionUpdate.getTransactionType());
        assertThat(storedRepTransaction.getName()).isEqualTo(repTransactionUpdate.getName());
        assertThat(storedRepTransaction.getDescription()).isEqualTo(repTransactionUpdate.getDescription());
        assertThat(storedRepTransaction.getAmount()).isEqualTo(repTransactionUpdate.getAmount());
        assertThat(storedRepTransaction.getCategory()).isEqualTo(repTransactionUpdate.getCategory());
        assertThat(storedRepTransaction.getDateFrom()).isEqualTo(repTransactionUpdate.getDateFrom());
        assertThat(storedRepTransaction.getDateTo()).isEqualTo(repTransactionUpdate.getDateTo());
        assertThat(storedRepTransaction.getFrequency()).isEqualTo(repTransactionUpdate.getFrequency());
        assertThat(storedRepTransaction.getId()).isNotNull();
        assertThat(storedRepTransaction.getNextCreateAt().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(repTransactionUpdate.getNextCreateAt().truncatedTo(ChronoUnit.SECONDS));
    }
}