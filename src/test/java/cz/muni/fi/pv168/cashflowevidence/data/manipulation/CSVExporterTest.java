package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionCSVSerializer;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.csv.CsvExporter;
import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CSVExporterTest {

    private final ExportImportTestData testData = new ExportImportTestData();

    @TempDir
    private static Path testDir;
    private final String exportFilePath = testDir.resolve("export.csv").toAbsolutePath().toString();
    private final CsvExporter transactionExporter = new CsvExporter();

    @Test
    void noTransaction() throws DataManipulationException, IOException {
        var batch = new Batch(new ArrayList<>(), new ArrayList<>());
        transactionExporter.exportBatch(batch, exportFilePath);
        assertExportedContent("");
    }

    @Test
    void singleTransactionEmptyCategory() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()), List.of(testData.getTransactionNullCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.getTransactionNullCategoryCsv());
    }

    @Test
    void singleTransactionWithCategory() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()), List.of(testData.getTransactionRedCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.getTransactionRedCategoryCsv());
    }

    @Test
    void multipleTransactions() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()),
                List.of(testData.getTransactionRedCategory(), testData.getTransactionNullCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.multiTransactionCsv());
    }

    private void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(Path.of(exportFilePath))).isEqualToIgnoringWhitespace(expectedContent);
    }

}
