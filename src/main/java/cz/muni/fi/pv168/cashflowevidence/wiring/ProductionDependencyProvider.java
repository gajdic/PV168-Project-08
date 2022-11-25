package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;

public class ProductionDependencyProvider extends CommonDependencyProvider {

    public ProductionDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        databaseManager.initSchema();

        return databaseManager;
    }
}