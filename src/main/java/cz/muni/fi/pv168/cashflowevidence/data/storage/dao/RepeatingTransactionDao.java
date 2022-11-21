package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.sql.Types.BIGINT;

public class RepeatingTransactionDao implements DatabaseAccessObject<RepeatingTransactionEntity> {
    Supplier<ConnectionHandler> connections;

    public RepeatingTransactionDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    public RepeatingTransactionEntity create(RepeatingTransactionEntity entity) {
        String sql = "INSERT INTO RepeatingTransaction" +
                "(name, description, amount, type, categoryId, days, dateFrom, dateTo, frequency)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            statement.setInt(6, entity.days());
            statement.setDate(7, Date.valueOf(entity.dateFrom()));
            statement.setDate(8, Date.valueOf(entity.dateTo()));
            statement.setString(9, entity.frequency().name());
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
            throw new DataStorageException("Unable to create repeating transaction: %s %s".formatted(entity, ex));
        }
    }

    public RepeatingTransactionEntity findById(long id) {
        String sql = "SELECT * FROM RepeatingTransaction WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
        ) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            if (result.next()) {
                return resultSetToRepeatingTransactionEntity(result);
            }

            return null;
        } catch (SQLException e) {
            throw new DataStorageException("Unable to retrieve repeating transaction by id: " + id);
        }
    }

    public List<RepeatingTransactionEntity> findAll() {
        String sql = "Select * FROM RepeatingTransaction";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<RepeatingTransactionEntity> repeatingTransactions = new ArrayList<>();

            try (var results = statement.executeQuery()) {
                while (results.next()) {
                    var repeatingTransaction = resultSetToRepeatingTransactionEntity(results);
                    repeatingTransactions.add(repeatingTransaction);
                }
            }

            return repeatingTransactions;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all repeating transactions. " + ex);
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM RepeatingTransaction WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
        ) {
            statement.setLong(1, id);
            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Repeating transaction with id %d does not exist.".formatted(id));
            }

            if (changedRows > 1000L) {
                throw new DataStorageException("Multiple repeating transactions were deleted. (%d)".formatted(changedRows));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to delete repeating transaction with id %d %s".formatted(id, ex));
        }
    }

    public RepeatingTransactionEntity update(RepeatingTransactionEntity entity) {
        String sql = "UPDATE RepeatingTransaction SET " +
                "categoryId = ?, name = ?, description = ?, amount = ?, type = ?, days = ?, dateFrom = ?, dateTo = ?, frequency = ? " +
                "WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setObject(1, entity.categoryId());
            statement.setString(2, entity.name());
            statement.setString(3, entity.description());
            statement.setDouble(4, entity.amount());
            statement.setString(5, entity.type().name());
            statement.setInt(6, entity.days());
            statement.setDate(7, Date.valueOf(entity.dateFrom()));
            statement.setDate(8, Date.valueOf(entity.dateTo()));
            statement.setString(9, entity.frequency().name());
            statement.setLong(10, entity.id());

            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Repeating transaction with id %d does not exist.".formatted(entity.id()));
            }

            if (changedRows > 1) {
                throw new DataStorageException("Multiple repeating transactions were updated. (%d)".formatted(changedRows));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to update repeating transaction: %s %s".formatted(entity, ex));
        }
        return findById(entity.id());
    }

    private static RepeatingTransactionEntity resultSetToRepeatingTransactionEntity(ResultSet resultset) throws SQLException {
        String frequencyStr = resultset.getString("frequency");
        var frequency = TransactionFrequency.valueOf(
                frequencyStr.substring(0, 1).toUpperCase() + frequencyStr.substring(1));

        return new RepeatingTransactionEntity(
                resultset.getLong("id"),
                resultset.getLong("categoryId"),
                resultset.getString("name"),
                resultset.getString("description"),
                resultset.getDouble("amount"),
                resultset.getString("type").equals("incoming") ?
                        TransactionType.Incoming : TransactionType.Outgoing,
                resultset.getTimestamp("createdAt").toLocalDateTime(),
                resultset.getInt("days"),
                resultset.getDate("dateFrom").toLocalDate(),
                resultset.getDate("dateTo").toLocalDate(),
                frequency
        );
    }
}
