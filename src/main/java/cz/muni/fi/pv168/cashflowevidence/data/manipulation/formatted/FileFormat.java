package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted;

/**
 * The interface for providing an observer of the {@link Format}
 */
public interface FileFormat {

    /**
     * Gets the {@link Format} of the implementation.
     */
    Format getFormat();

}
