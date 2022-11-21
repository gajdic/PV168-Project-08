package cz.muni.fi.pv168.cashflowevidence.data.storage.mapper;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Optional;

/**
 * Mapper from the {@link TransactionEntity} to {@link Transaction}.
 */
public class TransactionMapper implements EntityMapper<TransactionEntity, Transaction>{
    @FunctionalInterface
    public interface Lookup<T> {
        Optional<T> get(Long id);
    }

    private final Lookup<Category> categorySupplier;
    private final Validator<Transaction> validator;

    public TransactionMapper(Lookup<Category> categorySupplier, Validator<Transaction> validator) {
        this.categorySupplier = categorySupplier;
        this.validator = validator;
    }

    @Override
    public TransactionEntity mapToEntity(Transaction source) {
        validator.validate(source).intoException();

        return new TransactionEntity(
                source.getId(),
                source.getCategory() != null ? source.getCategory().getId() : null,
                source.getName(),
                source.getDescription(),
                source.getAmount(),
                source.getTransactionType(),
                source.getDateTime());
    }

    @Override
    public Transaction mapToModel(TransactionEntity entity) {
        Category category = categorySupplier.get(entity.categoryId()).orElse(null);

        return new Transaction(
                entity.id(),
                entity.type(),
                entity.name(),
                entity.description(),
                entity.amount(),
                entity.createdAt(),
                category
        );
    }
}
