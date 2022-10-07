package cz.muni.fi.pv168.cashflowevidence.model;

/**
 * Enum for predefined transaction frequencies
 */
public enum TransactionFrequency {
    Daily (1),
    Weekly (7),
    Monthly (30),
    EveryThreeMonths (90),
    EverySixMonths (180),
    Yearly (365),
    Custom (0);

    public final int frequencyValue;
    TransactionFrequency(int frequencyValue) {
        this.frequencyValue = frequencyValue;
    }
}
