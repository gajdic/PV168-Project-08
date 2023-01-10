package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.pdf.PdfExporter;
import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PdfExporterTest {

    private final ExportImportTestData testData = new ExportImportTestData();

    @TempDir
    private static Path testDir;
    private final String exportFilePath = testDir.resolve("export.json").toAbsolutePath().toString();
    private final PdfExporter transactionExporter = new PdfExporter();

    @Test
    void fileCreated() throws DataManipulationException {
        var batch = new Batch(List.of(testData.getRedCategory()), List.of(testData.getTransactionRedCategory(),
                testData.getTransactionNullCategory()));
        transactionExporter.exportBatch(batch, exportFilePath);

        assertTrue(Files.exists(Path.of(exportFilePath)));
    }
}
