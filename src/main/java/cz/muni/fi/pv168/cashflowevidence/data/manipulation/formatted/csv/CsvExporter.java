package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.csv;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvExporter implements BatchExporter {

    private static final String SEPARATOR = ",";
    private static final Format FORMAT = new Format("CSV", List.of("csv"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {
        Logger.info("Exporting batch to {}", filePath);

        try (var writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            for (var transaction : batch.getTransactions()) {
                String line = createCsvLine(transaction);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            Logger.error("Error while exporting batch");
            throw new DataManipulationException("Unable to write to file", exception);
        }
    }

    private String createCsvLine(Transaction transaction) {
        return String.join(SEPARATOR,
                transaction.getName(),
                transaction.getDescription(),
                String.valueOf(transaction.getAmount()),
                String.valueOf(transaction.getDateTime()),
                transaction.getCategory() != null ? transaction.getCategory().getName() : "null",
                transaction.getCategory() != null ? transaction.getCategory().getColorString() : "null",
                String.valueOf(transaction.getTransactionType()));
    }
}
