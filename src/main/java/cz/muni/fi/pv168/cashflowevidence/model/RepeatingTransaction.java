package cz.muni.fi.pv168.cashflowevidence.model;

import java.util.Date;

/**
 * Class representing repeating transactions which are an extension
 * of basic transaction
 */
public class RepeatingTransaction extends Transaction{

    private Date repeatFrom;
    private Date repeatTo;
    private int repeating;

    public RepeatingTransaction(TransactionType transactionType, String name, String description,
                                double amount, Date date, int time, Category category) {
        super(transactionType, name, description, amount, date, time, category);
    }

    public Date getRepeatFrom() {
        return repeatFrom;
    }

    public void setRepeatFrom(Date repeatFrom) {
        this.repeatFrom = repeatFrom;
    }

    public Date getRepeatTo() {
        return repeatTo;
    }

    public void setRepeatTo(Date repeatTo) {
        this.repeatTo = repeatTo;
    }

    public int getRepeating() {
        return repeating;
    }

    public void setRepeating(int repeating) {
        this.repeating = repeating;
    }
}
