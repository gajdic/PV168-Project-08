package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.time.LocalDateTime;
import java.util.Objects;

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

    private Long id;

    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category) {
        this(null, transactionType, name, description, amount, dateTime, category);
    }

    public Transaction(Long id, TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category) {
        this.id = id;
        this.transactionType = transactionType;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
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

    public void setDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAll(Transaction transaction) {
        transactionType = transaction.getTransactionType();
        name = transaction.getName();
        description = transaction.getDescription();
        amount = transaction.getAmount();
        dateTime = transaction.getDateTime();
        category = transaction.getCategory();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType=" + transactionType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", category=" + category + '}';
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = Objects.requireNonNull(id, "when setting id, it must not be null");
    }
}
