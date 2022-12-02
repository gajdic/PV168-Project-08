package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
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
    private final JsonTransactionExporter transactionExporter = new JsonTransactionExporter();

    @Test
    void noTransaction() throws ExportFailedException, IOException {
        transactionExporter.exportTransactions(new ArrayList<>(), exportFilePath);
        assertExportedContent("[]");
    }

    @Test
    void singleTransactionEmptyCategory() throws ExportFailedException, IOException {


        transactionExporter.exportTransactions(List.of(testData.getTransactionNullCategory()), exportFilePath);

        assertExportedContent(testData.getTransactionNullCategoryJson());
    }

    @Test
    void singleTransactionWithCategory() throws ExportFailedException, IOException {
        transactionExporter.exportTransactions(List.of(testData.getTransactionRedCategory()), exportFilePath);

        assertExportedContent(testData.getTransactionRedCategoryJson());

    }


    @Test
    void multipleTransactions() throws ExportFailedException, IOException {

        transactionExporter.exportTransactions(List.of(testData.getTransactionRedCategory(),
                testData.getTransactionNullCategory()), exportFilePath);

        assertExportedContent(testData.multiTransactionsJson());
    }

    private void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(Path.of(exportFilePath))).isEqualToIgnoringWhitespace(expectedContent);
    }

}
