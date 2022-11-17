package cz.muni.fi.pv168.cashflowevidence.service;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RepeatingTaskScheduler {

    private static final long MILLISECONDS_IN_A_DAY = 1000L * 60L * 60L * 24L;
    private final TransactionTableModel transactionTableModel;
    private final RepeatingTransactionTableModel repeatingTransactionTableModel;

    private Timer timer = new Timer();

    public RepeatingTaskScheduler(TransactionTableModel transactionTableModel,
                                  JTable repeatingTransactionTable) {
        this.transactionTableModel = transactionTableModel;


        repeatingTransactionTableModel = (RepeatingTransactionTableModel) repeatingTransactionTable.getModel();
        repeatingTransactionTableModel.getEntities().forEach(
                this::createPastTransactions);

        repeatingTransactionTableModel.getEntities().forEach(
                this::registerTransaction);

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
        }
        );
    }

    private void createPastTransactions(RepeatingTransaction repeatingTransaction){
        while ( repeatingTransaction.getNextCreateAt().isBefore(
                LocalDateTime.now()))
        {
            createTransaction(repeatingTransaction);
        }
    }

    private void createTransaction(RepeatingTransaction repeatingTransaction) {
        Transaction transaction = repeatingTransaction.createNextTransaction();
        transactionTableModel.addRow(transaction);
        repeatingTransaction.setDoNotReschedule(true);
        repeatingTransactionTableModel.updateRow(repeatingTransaction);
    }


    public void registerTransaction(RepeatingTransaction repeatingTransaction) {

        timer = new Timer();
        TimerTask createTransactionTask = new TimerTask() {
            @Override
            public void run() {
                if (repeatingTransaction.getNextCreateAt().isAfter(
                        repeatingTransaction.getDateTimeTo())) {
                    cancel();
                }

                createTransaction(repeatingTransaction);
            }
        };

        Date start = convertToDate(repeatingTransaction.getNextCreateAt());
        long delay = MILLISECONDS_IN_A_DAY * repeatingTransaction.getDays();

        if (repeatingTransaction instanceof RepeatingTransactionEverySecond) {
            delay = 1000L * repeatingTransaction.getDays();
        }

        timer.scheduleAtFixedRate(createTransactionTask, start, delay);
    }

    public void refreshPlannedTransactions() {
        timer.cancel();
        repeatingTransactionTableModel.getEntities().forEach(
                this::registerTransaction);
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


}
