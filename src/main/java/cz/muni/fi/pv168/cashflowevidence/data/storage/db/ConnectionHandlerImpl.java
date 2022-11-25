package cz.muni.fi.pv168.cashflowevidence.data.storage.db;

import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionHandlerImpl implements ConnectionHandler {
    private final Connection connection;

    public ConnectionHandlerImpl(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Missing connection object");
    }

    @Override
    public Connection use() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection.getAutoCommit()) {
                // Not transaction, connection can be closed
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataStorageException("Unable close database connection", e);
        }
    }
}
