package cz.muni.fi.pv168.cashflowevidence.model;

import java.time.LocalDateTime;
import java.util.Date;

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

    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, LocalDateTime dateTime, Category category) {
        this.transactionType = transactionType;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
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
         return String.format("%,.0f", amount).replace(",", " ");
     }

     public String getAmountStr() {
         return formatAmount(amount);
     }

     public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
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
}
