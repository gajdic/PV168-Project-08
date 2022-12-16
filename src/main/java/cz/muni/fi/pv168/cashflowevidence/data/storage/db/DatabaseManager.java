package cz.muni.fi.pv168.cashflowevidence.data.storage.db;

import javax.sql.DataSource;

import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import org.h2.jdbcx.JdbcConnectionPool;
import org.tinylog.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class DatabaseManager {

    private static final String PROJECT_NAME = "cash-flow-evidence";
    private static final String DB_PROPERTIES_STRING = "DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";

    private final DataSource dataSource;
    private final SqlFileExecutor sqlFileExecutor;

    private DatabaseManager(String jdbcUri) {
        this.dataSource = JdbcConnectionPool.create(jdbcUri, "", "");
        this.sqlFileExecutor = new SqlFileExecutor(this::getTransactionHandler, DatabaseManager.class);
    }

    public TransactionHandler getTransactionHandler() {
        try {
            return new TrasactionHandlerImpl(dataSource.getConnection());
        } catch (SQLException e) {
            throw new DataStorageException("Unable to get a new connection", e);
        }
    }

    public ConnectionHandler getConnectionHandler() {
        try {
            return new ConnectionHandlerImpl(dataSource.getConnection());
        } catch (SQLException e) {
            throw new DataStorageException("Unable to get a new connection", e);
        }
    }



    public static DatabaseManager createProductionInstance() {
        String connectionString = "jdbc:h2:%s;%s".formatted(createDbFileSystemPath(), DB_PROPERTIES_STRING);

        Logger.info("JDBC connection URI: " + connectionString);

        return new DatabaseManager(connectionString);
    }

    public static DatabaseManager createTestInstance() {
        String connectionString = "jdbc:h2:mem:%s;%s".formatted(PROJECT_NAME, DB_PROPERTIES_STRING);
        var databaseManager = new DatabaseManager(connectionString);
        databaseManager.initSchema();
        databaseManager.initData("test");

        return databaseManager;
    }

    public void destroySchema() {
        sqlFileExecutor.execute("drop.sql");
    }

    public void initSchema() {
        sqlFileExecutor.execute("init.sql");
    }

    public void initData(String environment) {
        sqlFileExecutor.execute("data_%s.sql".formatted(environment));
    }

    private static Path createDbFileSystemPath() {
        String userHomeDir = System.getProperty("user.home");
        Path projectDbPath = Paths.get(userHomeDir, "pv168", "db", PROJECT_NAME);

        File parentDir = projectDbPath.getParent().toFile();

        Logger.info("Created DB with file path " + parentDir.getPath());

        if (!parentDir.exists()) {
            throw new DataStorageException("Unable to create database root directory");
        }

        return projectDbPath;
    }
}
