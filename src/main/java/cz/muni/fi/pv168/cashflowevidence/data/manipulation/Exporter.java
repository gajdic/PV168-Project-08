package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

import javax.swing.*;
import java.util.Collection;

/**
 * Generic mechanism, allowing to export data to a file.
 */
public interface Exporter extends FileFormats {

    /**
     * Exports data to a file.
     *
     * @param filePath absolute path of the export file (to be created or overwritten)
     *
     * @throws BatchOperationException if the export cannot be done
     */
    void exportData(String filePath);

    void setTransactionTable(JTable transactionTable);
}
