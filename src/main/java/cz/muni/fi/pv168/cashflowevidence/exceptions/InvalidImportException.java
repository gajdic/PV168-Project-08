package cz.muni.fi.pv168.cashflowevidence.exceptions;

public class InvalidImportException extends RuntimeException {
    public InvalidImportException(String s) {
        super(s);
    }

    public InvalidImportException(String s, Throwable e) {
        super(e);
    }

    public InvalidImportException(Throwable e) {
        super(e);
    }
}
