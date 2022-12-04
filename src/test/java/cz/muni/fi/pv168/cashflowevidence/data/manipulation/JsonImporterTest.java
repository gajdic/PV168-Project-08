package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonImporterTest {

        private final ExportImportTestData testData = new ExportImportTestData();

        @TempDir
        private static Path testDir;
        private final String importFilePath = testDir.resolve("import.json").toAbsolutePath().toString();

        private final TransactionImporter importer = new JsonTransactionImporter(new MockCategoryRepository());

        private void prepareImportFile(String data) throws IOException {
            try (FileWriter fileWriter = new FileWriter(importFilePath)){
                fileWriter.write(data);
            }
        }

        @Test
        public void importWithoutCategory() throws IOException {
            prepareImportFile(testData.getTransactionNullCategoryJson());

            Collection<Transaction> importedTransactions = importer.importRecords(importFilePath);

            assertThat(testData.getTransactionNullCategory()).isIn(importedTransactions);
            assertThat(importedTransactions.size()).isEqualTo(1);
        }

        @Test
        public void importWithCategory() throws IOException {
            prepareImportFile(testData.getTransactionRedCategoryJson());

            Collection<Transaction> importedTransactions = importer.importRecords(importFilePath);

            assertThat(testData.getTransactionRedCategory()).isIn(importedTransactions);
            assertThat(importedTransactions.size()).isEqualTo(1);
        }

        @Test
        public void importMultipleCategories() throws IOException {
            prepareImportFile(testData.multiTransactionsJson());

            Collection<Transaction> importedTransactions = importer.importRecords(importFilePath);

            assertThat(testData.getTransactionRedCategory()).isIn(importedTransactions);
            assertThat(testData.getTransactionNullCategory()).isIn(importedTransactions);

            assertThat(importedTransactions.size()).isEqualTo(2);
        }

        @Test
        public void importInvalidPath() {
            assertThrows(InvalidImportException.class, () -> importer.importRecords("invalid path"));
        }

        @Test
        public void importInvalidFormat() throws IOException {
            prepareImportFile(testData.getTransactionNullCategoryCsv());

            assertThrows(InvalidImportException.class, () -> importer.importRecords(importFilePath));
        }
}
