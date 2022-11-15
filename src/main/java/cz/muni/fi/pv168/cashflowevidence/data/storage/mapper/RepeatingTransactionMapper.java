package cz.muni.fi.pv168.cashflowevidence.data.storage.mapper;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;

import java.util.Optional;

public class RepeatingTransactionMapper implements EntityMapper<RepeatingTransactionEntity, RepeatingTransaction> {
    @FunctionalInterface
    public interface Lookup<T> {
        Optional<T> get(Long id);
    }

    private final Lookup<Category> categorySupplier;

    public RepeatingTransactionMapper(Lookup<Category> categorySupplier) {
        this.categorySupplier = categorySupplier;
    }

    @Override
    public RepeatingTransactionEntity mapToEntity(RepeatingTransaction source) {
        return new RepeatingTransactionEntity(
                source.getId(),
                source.getCategory().getId(),
                source.getName(),
                source.getDescription(),
                source.getAmount(),
                source.getTransactionType(),
                source.getDateTime(),
                source.getDays(),
                source.getDateFrom(),
                source.getDateTo(),
                source.getFrequency());
    }

    @Override
    public RepeatingTransaction mapToModel(RepeatingTransactionEntity entity) {
        Category category = categorySupplier.get(entity.categoryId()).orElseThrow();

        return new RepeatingTransaction(
                entity.id(),
                entity.type(),
                entity.name(),
                entity.description(),
                entity.amount(),
                category,
                entity.dateFrom(),
                entity.dateTo(),
                entity.frequency()
        );
    }
}
