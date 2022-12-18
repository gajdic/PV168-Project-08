package cz.muni.fi.pv168.cashflowevidence.app.workers;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Exporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

import javax.swing.*;
import java.util.Collection;
import java.util.function.Consumer;

public class AsyncExporter implements Exporter {

    private final Exporter exporter;
    private final Runnable onFinish;

    private final Consumer<Integer> busyReporter;

    public AsyncExporter(Exporter exporter, Runnable onFinish, Consumer<Integer> busyReporter) {
        this.exporter = exporter;
        this.onFinish = onFinish;
        this.busyReporter = busyReporter;
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
                busyReporter.accept(1);
                exporter.exportData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                busyReporter.accept(-1);
                onFinish.run();
            }
        };
        asyncWorker.execute();
    }
}

