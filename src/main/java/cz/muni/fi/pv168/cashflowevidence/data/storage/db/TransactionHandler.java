package cz.muni.fi.pv168.cashflowevidence.data.storage.db;

import java.io.Closeable;

public interface TransactionHandler extends Closeable {

    ConnectionHandler connection();

    void commit();

    void close();
}
