package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.json.JsonExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JsonExporterTest {

    private final ExportImportTestData testData = new ExportImportTestData();

    @TempDir
    private static Path testDir;
    private final String exportFilePath = testDir.resolve("export.json").toAbsolutePath().toString();
    private final JsonExporter transactionExporter = new JsonExporter();

    @Test
    void noTransaction() throws DataManipulationException, IOException {
        var batch = new Batch(new ArrayList<>(), new ArrayList<>());
        transactionExporter.exportBatch(batch, exportFilePath);
        assertExportedContent("[]");
    }

    @Test
    void singleTransactionEmptyCategory() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()), List.of(testData.getTransactionNullCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.getTransactionNullCategoryJson());
    }

    @Test
    void singleTransactionWithCategory() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()), List.of(testData.getTransactionRedCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.getTransactionRedCategoryJson());

    }


    @Test
    void multipleTransactions() throws DataManipulationException, IOException {
        var batch = new Batch(List.of(testData.getRedCategory()),
                List.of(testData.getTransactionRedCategory(), testData.getTransactionNullCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertExportedContent(testData.multiTransactionsJson());
    }

    private void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(Path.of(exportFilePath))).isEqualToIgnoringWhitespace(expectedContent);
    }

}
