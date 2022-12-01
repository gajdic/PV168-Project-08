package cz.muni.fi.pv168.cashflowevidence.data.storage.entity;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

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
 */
public record TransactionEntity(
        Long id,
        Long categoryId,
        String name,
        String description,
        double amount,
        TransactionType type,
        LocalDateTime createdAt
) {
    public TransactionEntity(Long categoryId,
                             String name,
                             String description,
                             double amount,
                             TransactionType type,
                             LocalDateTime createdAt) {
        this(null, categoryId, name, description, amount, type, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionEntity that = (TransactionEntity) o;
        return type == that.type &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(
                        createdAt.truncatedTo(ChronoUnit.SECONDS), that.createdAt.truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, name, description, amount, type, createdAt);
    }
}
