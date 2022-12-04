package cz.muni.fi.pv168.cashflowevidence.app.workers;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Exporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

import javax.swing.*;
import java.util.Collection;

public class AsyncExporter implements Exporter {

    private final Exporter exporter;
    private final Runnable onFinish;

    public AsyncExporter(Exporter exporter, Runnable onFinish) {
        this.exporter = exporter;
        this.onFinish = onFinish;
    }

    @Override
    public Collection<Format> getFormats() {
        return exporter.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        SwingWorker<Void, Void> asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                exporter.exportData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                onFinish.run();
            }
        };
        asyncWorker.execute();
    }
}

