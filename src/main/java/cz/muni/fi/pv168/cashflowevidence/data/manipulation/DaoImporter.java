package cz.muni.fi.pv168.cashflowevidence.data.manipulation;


import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.FormatMapping;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

//TODO implement
public class DaoImporter implements Importer {

    private final FormatMapping<BatchImporter> importers;


    public DaoImporter(Collection<BatchImporter> importers) {
        this.importers = new FormatMapping<>(importers);
    }

    //TODO implement
    @Override
    public void importData(String filePath) {

    }

    //TODO implement
    @Override
    public Collection<Format> getFormats() {
        return null;
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
