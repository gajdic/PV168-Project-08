package cz.muni.fi.pv168.cashflowevidence.service;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import cz.muni.fi.pv168.cashflowevidence.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;


public class RepeatingTaskSchedulerTest {
    private DatabaseManager databaseManager;

    private Repository<RepeatingTransaction> repeatingTransactionRepository;
    private Repository<Category> categoryRepository;

    private Repository<Transaction> transactionRepository;
    private RepeatingTaskScheduler scheduler;

    private RepeatingTransaction repeatingTransaction;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);

        repeatingTransactionRepository = dependencyProvider.getRepeatingTransactionRepository();
        categoryRepository = dependencyProvider.getCategoryRepository();
        transactionRepository = dependencyProvider.getTransactionRepository();


        int size = transactionRepository.findAll().size();
        for (int i = 0; i < size; i++) {
            transactionRepository.deleteByIndex(0);
        }


        int sizeRT = repeatingTransactionRepository.findAll().size();
        for (int i = 0; i < sizeRT; i++) {
            repeatingTransactionRepository.deleteByIndex(0);
        }

        scheduler = new RepeatingTaskScheduler(dependencyProvider);

    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void scheduleRepeatingTransactionPast() {
        repeatingTransaction = new RepeatingTransaction(TransactionType.Incoming, "repeatingTrEveryTenSeconds",
                "desc",666, null,
                LocalDateTime.now().minusSeconds(70), LocalDateTime.now().plusSeconds(180), 30);
        repeatingTransaction.setFrequency(TransactionFrequency.EachSecond);
        repeatingTransactionRepository.create(repeatingTransaction);

        scheduler.scheduleExistingTransactions();
        assertThat(transactionRepository.findAll().size()).isEqualTo(3);
    }


    @Test
    void scheduleRepeatingTransactionsFuture() {
        repeatingTransaction = new RepeatingTransaction(TransactionType.Incoming, "repeatingTrEveryTenSeconds",
                "desc",666, null,
                LocalDateTime.now().plusSeconds(2), LocalDateTime.now().plusSeconds(20), 5);
        repeatingTransaction.setFrequency(TransactionFrequency.EachSecond);


        repeatingTransactionRepository.create(repeatingTransaction);

        scheduler.scheduleExistingTransactions();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertThat(transactionRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void scheduleRepeatingTransactionFutureChangeDate() {
        // TODO:
    }

    @Test
    void repeatingTransactionMovedToPastNotRecreating() {
        // TODO: When repeating transaction is moved to past, it should not recreate already existing transactions
        // Or prohibit moving the transaction into past

    }

}
