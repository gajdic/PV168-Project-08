package cz.muni.fi.pv168.cashflowevidence.model;

import java.util.Date;

/**
 * Class representing transactions
 */
public class Transaction {
    private TransactionType transactionType;
    private String name;
    private String description;
    private double amount;
    private Date date;
    private int time;
    private Category category;

    public Transaction(TransactionType transactionType, String name, String description,
                       double amount, Date date, int time, Category category) {
        this.transactionType = transactionType;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.time = time;
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
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
