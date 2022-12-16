package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

import java.util.Collection;

public interface FileFormats {

    /**
     * Gets the {@link Format} of the implementation.
     */
    Collection<Format> getFormats();

}