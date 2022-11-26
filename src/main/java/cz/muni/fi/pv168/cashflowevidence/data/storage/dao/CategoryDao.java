package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import org.tinylog.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CategoryDao implements DatabaseAccessObject<CategoryEntity> {
    private final Supplier<ConnectionHandler> connections;

    public CategoryDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    public CategoryEntity create(CategoryEntity entity) {
        Logger.debug("Storing new entity {}", entity);

        String sql = "INSERT INTO Category (name, color) VALUES (?, ?)";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.color());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                long categoryId;

                if (keys.next()) {
                    categoryId = keys.getLong("id");
                    Logger.debug("Successfully stored {}", categoryId);
                } else {
                    Logger.debug("Unable to store {}", entity);
                    throw new DataStorageException("Failed to retrieve generated key for: " + entity);
                }

                if (keys.next()) {
                    throw new DataStorageException("Retrieved multiple keys for: " + entity);
                }

                return findById(categoryId);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to execute create for: %s %s".formatted(entity, ex));
        }
    }

    public CategoryEntity findById(long id) {
        Logger.debug("Fetching by id {}", id);

        String sql = "SELECT * FROM Category WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            if (result.next()) {
                return new CategoryEntity(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("color"));
            }

            Logger.debug("No entity for id {}", id);
            return null;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by id: " + id);
        }
    }

    public List<CategoryEntity> findAll() {
        Logger.debug("Fetching all");

        String sql = "SELECT * FROM Category";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<CategoryEntity> categories = new ArrayList<>();

            try (var results = statement.executeQuery()) {
                while (results.next()) {
                    var category = new CategoryEntity(
                            results.getLong("id"),
                            results.getString("name"),
                            results.getString("color")
                    );
                    categories.add(category);
                }
            }

            return categories;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    public void deleteById(long id) {
        Logger.debug("Deleting entity with id {}", id);

        String sql = "DELETE FROM Category WHERE id = ?";

        var transactionDao = new TransactionDao(connections);
        var allTransactions = transactionDao.findAll();
        removeTransactionCategoryIds(allTransactions, id, transactionDao);

        var repeatingDao = new RepeatingTransactionDao(connections);
        var allRepeating = repeatingDao.findAll();
        removeTransactionCategoryIds(allRepeating, id,  repeatingDao);

        var caught = false;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
        ) {
            statement.setLong(1, id);
            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                caught = true;
                throw new DataStorageException("Category with id %d does not exist.".formatted(id));
            }
            if (changedRows > 1) {
                caught = true;
                throw new DataStorageException("Multiple categories were deleted. (%d)".formatted(changedRows));
            }

            Logger.debug("Successfully deleted entity with id {}", id);
        } catch (SQLException ex) {
            caught = true;
            throw new DataStorageException("Unable to delete category with id %d %s".formatted(id, ex));
        } finally {
            if (caught) {
                allTransactions.forEach(transactionDao::update);
                allRepeating.forEach(repeatingDao::update);
            }
        }
    }

    public CategoryEntity update(CategoryEntity entity) {
        Logger.debug("Updating entity {}", entity);

        String sql = "UPDATE Category SET name = ?, color = ? WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.color());
            statement.setLong(3, entity.id());

            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Category with id %d does not exist.".formatted(entity.id()));
            }

            if (changedRows > 1) {
                throw new DataStorageException("Multiple categories were deleted. (%d)".formatted(changedRows));
            }

            Logger.debug("Update successful for {}", entity);
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to update category: %s %s".formatted(entity, ex));
        }
        return findById(entity.id());
    }

    /**
     * Changes categoryId of the provided TransactionEntity or RepeatingTransactionEntity.
     * ikr...
     *
     * @param entities TransactionEntities or RepeatingTransactionEntities to be updated.
     * @param <T> TransactionEntity or RepeatingTransactionEntity
     */
    private <T> void removeTransactionCategoryIds(List<T> entities, Long currentCategoryId, DatabaseAccessObject<T> dao) {
        for (var entity : entities) {
            if (entity.getClass().equals(TransactionEntity.class)) {
                if (!((TransactionEntity) entity).categoryId().equals(currentCategoryId))
                    continue;
                dao.update(changeTransactionCategoryId((TransactionEntity) entity, null));
            } else {
                if (!((RepeatingTransactionEntity) entity).categoryId().equals(currentCategoryId))
                    continue;
                dao.update(changeTransactionCategoryId((RepeatingTransactionEntity) entity, null));
            }
        }
    }

    private <T> T changeTransactionCategoryId(TransactionEntity entity, Long categoryId) {
        return (T) new TransactionEntity(
                entity.id(),
                categoryId,
                entity.name(),
                entity.description(),
                entity.amount(),
                entity.type(),
                entity.createdAt()
        );
    }

    private <T> T changeTransactionCategoryId(RepeatingTransactionEntity entity, Long categoryId) {
        return (T) new RepeatingTransactionEntity(
                entity.id(),
                categoryId,
                entity.name(),
                entity.description(),
                entity.amount(),
                entity.type(),
                entity.createdAt(),
                entity.nextCreatedAt(),
                entity.days(),
                entity.dateFrom(),
                entity.dateTo(),
                entity.frequency()
        );
    }
}
