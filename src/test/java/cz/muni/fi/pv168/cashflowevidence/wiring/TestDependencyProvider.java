package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;

public final class TestDependencyProvider extends CommonDependencyProvider {

    public TestDependencyProvider(DatabaseManager databaseManager) {
        super(databaseManager);
    }
}