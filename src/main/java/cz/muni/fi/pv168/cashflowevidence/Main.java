package cz.muni.fi.pv168.cashflowevidence;

import cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence;
import cz.muni.fi.pv168.cashflowevidence.wiring.DependencyProvider;
import cz.muni.fi.pv168.cashflowevidence.wiring.ProductionDependencyProvider;

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
        final DependencyProvider dependencyProvider = new ProductionDependencyProvider();
        new CashFlowEvidence(dependencyProvider);
    }
}
