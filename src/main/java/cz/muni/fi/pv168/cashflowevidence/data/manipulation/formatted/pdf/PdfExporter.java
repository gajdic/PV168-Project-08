package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.pdf;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

public class PdfExporter implements BatchExporter {
    @Override
    public void exportBatch(Batch batch, String filePath) {

    }

    @Override
    public Format getFormat() {
        return null;
    }
}
