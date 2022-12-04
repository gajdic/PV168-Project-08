package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.tinylog.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.sql.Types.BIGINT;

public class TransactionDao implements DatabaseAccessObject<TransactionEntity>{
    Supplier<ConnectionHandler> connections;

    public TransactionDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    public TransactionEntity create(TransactionEntity entity) {
        Logger.debug("Storing new entity {}", entity);

        String sql = "INSERT INTO Transaction" +
                "(name, description, amount, type, categoryId, createdAt)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setDouble(3, entity.amount());
            statement.setString(4, entity.type().name());
            if (entity.categoryId() != null) {
                statement.setLong(5, entity.categoryId());
            }
            else {
                statement.setNull(5, BIGINT);
            }
            if (entity.createdAt() != null) {
                statement.setTimestamp(6, Timestamp.valueOf(entity.createdAt()));
            }
            else {
                statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                long transactionId;

                if (keys.next()) {
                    transactionId = keys.getLong(1);
                    Logger.debug("Successfully stored {}", transactionId);
                } else {
                    Logger.debug("Unable to store {}", entity);
                    throw new DataStorageException("Failed to retrieve generated key for: " + entity);
                }

                if (keys.next()) {
                    throw new DataStorageException("Retrieved multiple keys for: " + entity);
                }

                return findById(transactionId);
            }

        } catch (SQLException ex) {
            throw new DataStorageException("Unable to create transaction: %s %s".formatted(entity, ex));
        }
    }

    public TransactionEntity findById(long id) {
        Logger.debug("Fetching by id {}", id);

        String sql = "SELECT * FROM Transaction WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
            ) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            if (result.next()) {
                return resultSetToTransactionEntity(result);
            }

            Logger.debug("No entity for id {}", id);
            return null;
        } catch (SQLException e) {
            throw new DataStorageException("Unable to retrieve transaction by id: " + id);
        }
    }

    public List<TransactionEntity> findAll() {
        Logger.debug("Fetching all");

        String sql = "Select * FROM Transaction";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<TransactionEntity> transactions = new ArrayList<>();

            try (var results = statement.executeQuery()) {
                while (results.next()) {
                    var transaction = resultSetToTransactionEntity(results);
                    transactions.add(transaction);
                }
            }

            return transactions;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all transactions", ex);
        }
    }

    public void deleteById(long id) {
        Logger.debug("Deleting entity with id {}", id);

        String sql = "DELETE FROM Transaction WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
        ) {
            statement.setLong(1, id);
            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Transaction with id %d does not exist.".formatted(id));
            }

            if (changedRows > 1) {
                throw new DataStorageException("Multiple transactions were deleted. (%d)".formatted(changedRows));
            }

            Logger.debug("Successfully deleted entity with id {}", id);
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to delete transaction with id %d %s".formatted(id, ex));
        }
    }

    public TransactionEntity update(TransactionEntity entity) {
        Logger.debug("Updating entity {}", entity);

        String sql = "UPDATE Transaction SET " +
                "categoryId = ?, name = ?, description = ?, amount = ?, type = ? WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setObject(1, entity.categoryId());
            statement.setString(2, entity.name());
            statement.setString(3, entity.description());
            statement.setDouble(4, entity.amount());
            statement.setString(5, entity.type().name());
            statement.setLong(6, entity.id());

            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Transaction with id %d does not exist.".formatted(entity.id()));
            }

            if (changedRows > 1) {
                throw new DataStorageException("Multiple transactions were deleted. (%d)".formatted(changedRows));
            }

            Logger.debug("Update successful for {}", entity);
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to update transaction: %s %s".formatted(entity, ex));
        }
        return findById(entity.id());
    }

    private static TransactionEntity resultSetToTransactionEntity(ResultSet resultset) throws SQLException {
        return new TransactionEntity(
                resultset.getLong("id"),
                resultset.getLong("categoryId"),
                resultset.getString("name"),
                resultset.getString("description"),
                resultset.getDouble("amount"),
                resultset.getString("type").equals("incoming") ?
                        TransactionType.Incoming : TransactionType.Outgoing,
                resultset.getTimestamp("createdAt").toLocalDateTime()
        );
    }
}
