package cz.muni.fi.pv168.cashflowevidence.data.storage.mapper;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Optional;

public class RepeatingTransactionMapper implements EntityMapper<RepeatingTransactionEntity, RepeatingTransaction> {
    @FunctionalInterface
    public interface Lookup<T> {
        Optional<T> get(Long id);
    }

    private final Lookup<Category> categorySupplier;
    private final Validator<RepeatingTransaction> validator;

    public RepeatingTransactionMapper(Lookup<Category> categorySupplier, Validator<RepeatingTransaction> validator) {
        this.categorySupplier = categorySupplier;
        this.validator = validator;
    }

    @Override
    public RepeatingTransactionEntity mapToEntity(RepeatingTransaction source) {
        validator.validate(source).intoException();

        return new RepeatingTransactionEntity(
                source.getId(),
                source.getCategory() != null ? source.getCategory().getId() : null,
                source.getName(),
                source.getDescription(),
                source.getAmount(),
                source.getTransactionType(),
                source.getDateTime(),
                source.getNextCreateAt(),
                source.getDays(),
                source.getDateTimeFrom(),
                source.getDateTimeTo(),
                source.getFrequency());
    }

    @Override
    public RepeatingTransaction mapToModel(RepeatingTransactionEntity entity) {
        Category category = categorySupplier.get(entity.categoryId()).orElse(null);

        return new RepeatingTransaction(
                entity.id(),
                entity.type(),
                entity.name(),
                entity.description(),
                entity.amount(),
                category,
                entity.createdAt(),
                entity.nextCreatedAt(),
                entity.dateFrom(),
                entity.dateTo(),
                entity.frequency(),
                entity.days()
        );
    }
}
