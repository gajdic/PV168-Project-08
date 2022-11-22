package cz.muni.fi.pv168.cashflowevidence.service;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.RepeatingTransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.TransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.*;
import cz.muni.fi.pv168.cashflowevidence.wiring.DependencyProvider;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RepeatingTaskScheduler {

    private static final long MILLISECONDS_IN_A_DAY = 1000L * 60L * 60L * 24L;

    private final Repository<Transaction> transactionRepository;
    private final Repository<RepeatingTransaction> repeatingTransactionRepository;

    private Timer timer = new Timer();

    public RepeatingTaskScheduler(DependencyProvider dependencyProvider) {
        this.transactionRepository = dependencyProvider.getTransactionRepository();
        this.repeatingTransactionRepository = dependencyProvider.getRepeatingTransactionRepository();
    }

    public void scheduleExistingTransactions() {
        repeatingTransactionRepository.findAll().forEach(
                this::createPastTransactions);

        repeatingTransactionRepository.findAll().forEach(
                this::registerTransaction);
    }


    public void setListenerForNewRepeatingTransaction(JTable repeatingTransactionTable, TransactionTableModel transactionTableModel) {
        RepeatingTransactionTableModel repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionTable.getModel();
        repeatingTransactionTableModel.addTableModelListener( tableModelEvent -> {
            // Dropping and replanning all transactions to not have to deal with looking which
            // to delete and recreate on update. This is not expected to run that often, so
            // the overhead should not be that big problem
            // TODO: Fix the bugs that are there with planning and rescheduling the transactions

            if (tableModelEvent.getType() == TableModelEvent.DELETE) {
                refreshPlannedTransactions();
                return;
            }

            RepeatingTransaction transaction = repeatingTransactionTableModel.getEntity(tableModelEvent.getFirstRow());

            if (!transaction.isDoNotReschedule()) {
                refreshPlannedTransactions();
            }

            transactionTableModel.fireTableDataChanged();
            }
        );
    }


    private void createPastTransactions(RepeatingTransaction repeatingTransaction){
        while ( repeatingTransaction.getNextCreateAt().isBefore(
                LocalDateTime.now()))
        {
            createTransaction(repeatingTransaction);
            repeatingTransaction = repeatingTransactionRepository.findByID(repeatingTransaction.getId()).orElseThrow();
        }
    }

    private void createTransaction(RepeatingTransaction repeatingTransaction) {
        Transaction transaction = repeatingTransaction.createNextTransaction();
        transactionRepository.create(transaction);
        repeatingTransaction.setDoNotReschedule(true);

        repeatingTransactionRepository.update(repeatingTransaction);
    }


    public void registerTransaction(RepeatingTransaction repeatingTransaction) {

        timer = new Timer();
        TimerTask createTransactionTask = new TimerTask() {
            @Override
            public void run() {
                if (repeatingTransaction.getNextCreateAt().isAfter(
                        repeatingTransaction.getDateTimeTo())) {
                    cancel();
                    return;
                }

                transactionRepository.refresh();
                createTransaction(repeatingTransaction);
            }
        };

        Date start = convertToDate(repeatingTransaction.getNextCreateAt());
        long delay = ((repeatingTransaction.getFrequency() == TransactionFrequency.EachSecond) ? 1000L : MILLISECONDS_IN_A_DAY )
                * repeatingTransaction.getDays();

        timer.scheduleAtFixedRate(createTransactionTask, start, delay);
    }

    public void refreshPlannedTransactions() {
        timer.cancel();
        repeatingTransactionRepository.findAll().forEach(
                this::registerTransaction);
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
