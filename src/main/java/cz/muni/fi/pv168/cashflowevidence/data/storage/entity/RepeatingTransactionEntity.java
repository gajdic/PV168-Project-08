package cz.muni.fi.pv168.cashflowevidence.data.storage.entity;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents the Transaction entity
 *
 * @param id            Primary key in the database (sequential)
 * @param categoryId    Category reference - id of the category where the transaction belongs to
 * @param name          Name of the transaction
 * @param description   Description of the transaction
 * @param amount        Amount of the transaction
 * @param type          Transaction type
 * @param createdAt     Datetime of transaction creation
 * @param days          Number representing how often in days does the transaction repeat when custom on frequency
 * @param dateFrom      Date from when the transaction repeats
 * @param dateTo        Date to which the transaction repeats
 * @param frequency     Frequency representing how often in does the transaction repeat
 */
public record RepeatingTransactionEntity (
        Long id,
        Long categoryId,
        String name,
        String description,
        double amount,
        TransactionType type,
        LocalDateTime createdAt,

        LocalDateTime nextCreatedAt,
        int days,
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        TransactionFrequency frequency
) {
    public RepeatingTransactionEntity(long categoryId,
                             String name,
                             String description,
                             double amount,
                             TransactionType type,
                             LocalDateTime createdAt,
                             LocalDateTime nextCreateAt,
                             int days,
                             LocalDateTime dateFrom,
                             LocalDateTime dateTo,
                             TransactionFrequency frequency) {
        this(null, categoryId, name, description, amount, type, createdAt, nextCreateAt, days, dateFrom, dateTo, frequency);
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepeatingTransactionEntity that = (RepeatingTransactionEntity) o;
        return type == that.type &&
                frequency == that.frequency &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(
                        createdAt.truncatedTo(ChronoUnit.SECONDS), that.createdAt.truncatedTo(ChronoUnit.SECONDS)) &&
                Objects.equals(
                        nextCreatedAt.truncatedTo(ChronoUnit.SECONDS), that.nextCreatedAt.truncatedTo(ChronoUnit.SECONDS)) &&
                Objects.equals(
                        dateFrom.truncatedTo(ChronoUnit.SECONDS), that.dateFrom.truncatedTo(ChronoUnit.SECONDS)) &&
                Objects.equals(
                        dateTo.truncatedTo(ChronoUnit.SECONDS), that.dateTo.truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, name, description, amount, type, createdAt, nextCreatedAt, days, dateFrom, dateTo, frequency);
    }
}
