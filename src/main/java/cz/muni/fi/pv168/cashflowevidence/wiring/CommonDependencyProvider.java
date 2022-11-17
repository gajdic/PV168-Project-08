package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;

public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
