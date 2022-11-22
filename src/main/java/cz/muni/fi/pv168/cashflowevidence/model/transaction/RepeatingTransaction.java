package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class RepeatingTransaction extends Transaction {

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    private TransactionFrequency frequency;
    private int days;

    private Long id;
    private boolean doNotReschedule = false;

    protected LocalDateTime nextCreateAt;

    public RepeatingTransaction() {}


    /**
     * Constructor used in mappers to set all fields
     * */
    public RepeatingTransaction(Long id, TransactionType transactionType, String name, String description,
                                double amount, Category category,
                                LocalDateTime createdAt, LocalDateTime nextCreateAt, LocalDateTime dateFrom, LocalDateTime dateTo,
                                TransactionFrequency frequency, int days) {

        super(transactionType, name, description, amount, createdAt, category);

        this.id = id;
        this.dateFrom = dateFrom;
        this.nextCreateAt = nextCreateAt;
        this.dateTo = dateTo;
        this.frequency = frequency;
        this.days = days;
    }

    public RepeatingTransaction(TransactionType transactionType, String name, String description, double amount, Category category,
                                LocalDateTime dateFrom, LocalDateTime dateTo) {
        super(transactionType, name, description, amount, LocalDateTime.now(), category);

        this.dateFrom = dateFrom;
        this.nextCreateAt = dateFrom;
        this.dateTo = dateTo;
    }


    /**
     * Creates a RepeatingTransaction with predefined frequency.
     */
    public RepeatingTransaction(TransactionType transactionType, String name, String description, double amount, Category category,
                                LocalDateTime dateFrom, LocalDateTime dateTo,
                                TransactionFrequency frequency) {
        this(transactionType, name, description, amount, category, dateFrom, dateTo);

        if (frequency == TransactionFrequency.Custom) {
            throw new IllegalArgumentException("Frequency cannot be custom");
        }

        this.frequency = frequency;
        this.days = frequency.frequencyValue;
    }

    /**
     * Creates a RepeatingTransaction with custom frequency.
     */
    public RepeatingTransaction(TransactionType transactionType, String name, String description,
                                double amount, Category category,
                                LocalDateTime dateFrom, LocalDateTime dateTo, int days) {
        this(transactionType, name, description, amount, category, dateFrom, dateTo);

        if (days <= 0) {
            throw new IllegalArgumentException("Frequency cannot be smaller or equal to 0.");
        }

        this.frequency = TransactionFrequency.Custom;
        this.days = days;
    }




    public LocalDate getDateFrom() {
        if (dateFrom != null) {
            return dateFrom.toLocalDate();
        }
        return null;
    }

    public LocalDateTime getDateTimeFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom.atStartOfDay();
    }

    public void setDateTimeFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setNextCreateAt(LocalDateTime nextCreateAt) {
        this.nextCreateAt = nextCreateAt;
    }


    public LocalDate getDateTo() {
        if (dateTo != null) {
            return dateTo.toLocalDate();
        }
        return null;
    }

    public LocalDateTime getDateTimeTo() {
        return dateTo;
    }


    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo.atStartOfDay();
    }

    public void setDateTimeTo(LocalDateTime dateTo) {
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

        setDateFrom(transaction.getDateFrom());
        setNextCreateAt(transaction.getNextCreateAt());
        setDateTo(transaction.getDateTo());
        setFrequency(transaction.getFrequency());
        setDays(transaction.getDays());

        setDoNotReschedule(false);
    }

    public LocalDateTime getNextCreateAt() {
        return nextCreateAt;
    }

    public Transaction createNextTransaction() {
        Transaction transaction = new Transaction(
                this.getTransactionType(),
                this.getName(),
                this.getDescription(),
                this.getAmount(),
                this.getNextCreateAt(),
                this.getCategory()
        );
        this.shiftNextCreateAt();
        return transaction;
    }

    protected void shiftNextCreateAt() {
        if (this.frequency == TransactionFrequency.EachSecond) {
            this.nextCreateAt = this.nextCreateAt.plusSeconds(this.getDays());
        } else {
            this.nextCreateAt = this.nextCreateAt.plusDays(this.getDays());
        }

    }

    public boolean isDoNotReschedule() {
        return doNotReschedule;
    }

    public void setDoNotReschedule(boolean doNotReschedule) {
        this.doNotReschedule = doNotReschedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = Objects.requireNonNull(id, "when setting id, it must not be null");
    }
}
