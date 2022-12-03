package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;

import java.util.Collection;

/**
 * Generic mechanism, allowing to import a {@link Batch} of entities from a file.
 *
 * <p>Both the <i>format</i> and <i>encoding</i> of the file are to be defined and
 * documented by implementations of this interface.
 */
public interface BatchImporter extends FileFormat {

    /**
     * Imports entities from a file to an ordered {@link Collection}.
     *
     * @param filePath absolute path of the file to import
     * @return imported bulk of entities
     * @throws DataManipulationException if the file to import does not exist,
     *                                   cannot be read or its format/encoding is invalid
     */
    Batch importBatch(String filePath);
}
