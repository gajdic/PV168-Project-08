package cz.muni.fi.pv168.cashflowevidence.data.storage.entity;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import java.time.LocalDateTime;

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
        long categoryId,
        String name,
        String description,
        double amount,
        TransactionType type,
        LocalDateTime createdAt
) {
    public TransactionEntity(long categoryId,
                             String name,
                             String description,
                             double amount,
                             TransactionType type,
                             LocalDateTime createdAt) {
        this(null, categoryId, name, description, amount, type, createdAt);
    }
}
