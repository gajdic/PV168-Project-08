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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jan Gajda
 */
class RepeatingTransactionRepositoryTest {

    DatabaseManager databaseManager;

    Repository<RepeatingTransaction> repeatingTransactionRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        repeatingTransactionRepository = new RepeatingTransactionRepository(
                new RepeatingTransactionDao(databaseManager::getConnectionHandler),
                new RepeatingTransactionMapper(id -> Optional.empty()));
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewRepeatingTransaction() {
        RepeatingTransaction repTransactionToCreate = new RepeatingTransaction(
                TransactionType.Outgoing,
                "Test name",
                "Test description",
                238.0,
                new Category("Test category", Color.CYAN),
                LocalDate.now(),
                LocalDate.now().plusDays(4),
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
}