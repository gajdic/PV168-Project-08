package cz.muni.fi.pv168.cashflowevidence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Class representing transactions
 */
public class Transaction {
    private TransactionType transactionType;
    private String name;
    private String description;
    private double amount;
    private LocalDateTime dateTime;
    private Category category;

    private LocalDate repeatFrom;
    private LocalDate repeatTo;

    private TransactionFrequency frequencyType;
    private int frequency;

    /**
     * Constructor used for creating non-repeating transaction.
     */
    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category) {
        this.transactionType = transactionType;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
    }

    /**
     * Constructor used for creating repeating transaction with custom repetition frequency.
     *
     * @param frequency repetition frequency of transaction in months.
     */
    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category,
                       LocalDate repeatFrom, LocalDate repeatTo, int frequency) {
        this(transactionType, name, description, amount, dateTime, category);
        this.repeatFrom = repeatFrom;
        this.repeatTo = repeatTo;
        this.frequencyType = TransactionFrequency.Custom;
        this.frequency = frequency;
    }

    /**
     * Constructor used for creating repeating transaction with predefined frequency.
     */
    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category,
                       LocalDate repeatFrom, LocalDate repeatTo, TransactionFrequency frequencyType) {
        this(transactionType, name, description, amount, dateTime, category);
        this.repeatFrom = repeatFrom;
        this.repeatTo = repeatTo;
        this.frequencyType = frequencyType;
        this.frequency = frequencyType.frequencyValue;
    }

    public Transaction() {
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    /**
     * Converts the amount to String and adds space as a separator between thousands
     *
     * @return Amount in String with spaces between thousands
     */
    public static String formatAmount(double amount) {
        return String.format("%,.2f", amount).replace("(\s+|,)", " ");
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getRepeatFrom() {
        return repeatFrom;
    }

    public void setRepeatFrom(LocalDate repeatFrom) {
        this.repeatFrom = repeatFrom;
    }

    public LocalDate getRepeatTo() {
        return repeatTo;
    }

    public void setRepeatTo(LocalDate repeatTo) {
        this.repeatTo = repeatTo;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public TransactionFrequency getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(TransactionFrequency frequencyType) {
        this.frequencyType = frequencyType;
    }

    public boolean isRepeating() {
        return frequency > 0 && repeatTo != null && repeatFrom != null;
    }

    public void setAll(Transaction transaction) {
        transactionType = transaction.getTransactionType();
        name = transaction.getName();
        description = transaction.getDescription();
        amount = transaction.getAmount();
        dateTime = transaction.getDateTime();
        category = transaction.getCategory();
        repeatFrom = transaction.getRepeatFrom();
        repeatTo = transaction.getRepeatTo();
        frequencyType = transaction.getFrequencyType();
        frequency = transaction.getFrequency();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType=" + transactionType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", category=" + category +
                ", repeatFrom=" + repeatFrom +
                ", repeatTo=" + repeatTo +
                ", frequencyType=" + frequencyType +
                ", frequency=" + frequency +
                '}';
    }
}
