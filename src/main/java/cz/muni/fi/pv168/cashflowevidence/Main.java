package cz.muni.fi.pv168.cashflowevidence;

import cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence;

/**
 * Simple application runner.
 */
public final class Main {

    private Main() {
        // intentionally private to prevent instantiation
    }

    /**
     * Standard application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new CashFlowEvidence();
    }
}
