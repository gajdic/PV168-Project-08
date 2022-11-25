package cz.muni.fi.pv168.cashflowevidence.exceptions;

public class DataStorageException extends RuntimeException {
    public DataStorageException(String s) {
        super(s);
    }

    public DataStorageException(String s, Throwable e) {
        super(s, e);
    }
}
