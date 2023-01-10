package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;

import java.util.function.Supplier;

@FunctionalInterface
public interface DaoSupplier<D> {

    DatabaseAccessObject<D> get(Supplier<ConnectionHandler> connections);
}
