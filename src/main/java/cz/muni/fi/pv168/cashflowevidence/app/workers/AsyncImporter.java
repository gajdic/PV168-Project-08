package cz.muni.fi.pv168.cashflowevidence.app.workers;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Importer;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;

import javax.swing.*;
import java.util.Collection;
import java.util.function.Consumer;

public class AsyncImporter implements Importer {

    private final Importer importer;
    private final Runnable onFinish;
    private final Consumer<Boolean> busyReporter;

    public AsyncImporter(Importer importer, Runnable onFinish, Consumer<Boolean> busyReporter) {
        this.importer = importer;
        this.onFinish = onFinish;
        this.busyReporter = busyReporter;
    }

    @Override
    public void importData(String filePath) {
        SwingWorker<Void, Void> asyncWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                busyReporter.accept(true);
                importer.importData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                busyReporter.accept(false);
                onFinish.run();
            }

        };

        asyncWorker.execute();
    }


    @Override
    public Collection<Format> getFormats() {
        return importer.getFormats();
    }
}
