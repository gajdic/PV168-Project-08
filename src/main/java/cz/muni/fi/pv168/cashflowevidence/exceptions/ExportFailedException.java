package cz.muni.fi.pv168.cashflowevidence.exceptions;

public class ExportFailedException extends Exception {
    public ExportFailedException(String s) {
        super(s);
    }

    public ExportFailedException(String s, Throwable e) {
        super(e);
    }

    public ExportFailedException(Throwable e) {
        super(e);
    }
}
