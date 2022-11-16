package cz.muni.fi.pv168.cashflowevidence.data.storage.db;

import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class TrasactionHandlerImpl implements TransactionHandler {
    private final ConnectionHandler connectionHandler;

    public TrasactionHandlerImpl(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "Missing connection object");
        connection.setAutoCommit(false);
        this.connectionHandler = new ConnectionHandlerImpl(connection);
    }

    @Override
    public ConnectionHandler connection() {
        return connectionHandler;
    }

    @Override
    public void commit() {
        try {
            connectionHandler.use().commit();
        } catch (SQLException e) {
            throw new DataStorageException("Unable to commit transaction", e);
        }
    }

    @Override
    public void close() {
        try {
            connectionHandler.use().close();
        } catch (SQLException e) {
            throw new DataStorageException("Unable to close database connection", e);
        }

    }
}
