package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RepeatingTransaction extends Transaction {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private TransactionFrequency frequency;
    private int days;

    public RepeatingTransaction() {}

    /**
     * Creates a RepeatingTransaction with predefined frequency.
     */
    public RepeatingTransaction(TransactionType transactionType, String name, String description,
                                double amount, Category category, LocalDate dateFrom, LocalDate dateTo,
                                TransactionFrequency frequency) {
        super(transactionType, name, description, amount, LocalDateTime.now(), category);

        if (frequency == TransactionFrequency.Custom) {
            throw new IllegalArgumentException("Frequency cannot be custom");
        }

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.frequency = frequency;
        this.days = frequency.frequencyValue;
    }

    /**
     * Creates a RepeatingTransaction with custom frequency.
     */
    public RepeatingTransaction(TransactionType transactionType, String name, String description,
                                double amount, Category category,
                                LocalDate dateFrom, LocalDate dateTo, int days) {
        super(transactionType, name, description, amount, LocalDateTime.now(), category);

        if (days <= 0) {
            throw new IllegalArgumentException("Frequency cannot be smaller or equal to 0.");
        }

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.frequency = TransactionFrequency.Custom;
        this.days = days;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public TransactionFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(TransactionFrequency frequency) {
        this.frequency = frequency;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setAll(RepeatingTransaction transaction) {
        super.setAll(transaction);

        dateFrom = transaction.getDateFrom();
        dateTo = transaction.getDateTo();
        frequency = transaction.getFrequency();
        days = transaction.getDays();
    }
}
