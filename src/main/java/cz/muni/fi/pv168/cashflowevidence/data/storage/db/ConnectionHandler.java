package cz.muni.fi.pv168.cashflowevidence.data.storage.db;

import java.io.Closeable;
import java.sql.Connection;

public interface ConnectionHandler extends Closeable {

    Connection use();

    void close();
}
