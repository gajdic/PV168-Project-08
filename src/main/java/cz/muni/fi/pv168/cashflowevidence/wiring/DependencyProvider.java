package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;

public interface DependencyProvider {

    DatabaseManager getDatabaseManager();

}
