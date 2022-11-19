package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import jdk.jshell.spi.ExecutionControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TransactionDao implements DatabaseAccessObject<TransactionEntity>{
    Supplier<ConnectionHandler> connections;

    public TransactionDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    public TransactionEntity create(TransactionEntity entity) {
        String sql = "INSERT INTO Transaction" +
                "(name, description, amount, type, categoryId)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            if (findById(entity.categoryId()) == null){
                throw new DataStorageException("Category with id %d does not exist.".formatted(entity.categoryId()));
            }

            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setDouble(3, entity.amount());
            statement.setString(4, entity.type().name());
            statement.setLong(5, entity.categoryId());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                long transactionId;

                if (keys.next()) {
                    transactionId = keys.getLong(1);
                } else {
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

            return null;
        } catch (SQLException e) {
            throw new DataStorageException("Unable to retrieve transaction by id: " + id);
        }
    }

    public List<TransactionEntity> findAll() {
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
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to delete transaction with id %d %s".formatted(id, ex));
        }
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
