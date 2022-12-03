package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionSerializer;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class TransactionPerLineImporter implements TransactionImporter{

    private final TransactionSerializer serializer;
    private final Repository<Category> categoryRepository;

    public TransactionPerLineImporter (TransactionSerializer serializer, Repository<Category> categoryRepository) {
        this.serializer = serializer;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Collection<Transaction> importRecords(String filePath) throws InvalidImportException {
        TransactionValidator validator = new TransactionValidator();

        Logger.info("Loading transactions from {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            List<Transaction> transactions = reader.lines()
                    .map(csvLine -> {
                        try {
                            return serializer.deserialize(csvLine, categoryRepository);
                        } catch (InvalidCSVFormatException e) {
                            throw new InvalidImportException(e);
                        }
                    })
                    .toList();
            if (!transactions.stream().allMatch(validator::isValidTransaction)) {
                Logger.error("Error while loading transactions: invalid transactions");
                throw new InvalidImportException("The provided transactions are not valid");
            }

            Logger.info("Loaded {} records", transactions.size());
            return transactions;

        } catch (FileNotFoundException exception) {
            Logger.error("Error while loading transactions: file does not exist");
            throw new InvalidImportException("File does not exist", exception);
        } catch (IOException exception) {
            Logger.error("Error while loading transactions: unable to read file");
            throw new InvalidImportException("Unable to read file", exception);
        }

    }

    @Override
    public ImportFormat getFormat() {
        return ImportFormat.csv;
    }
}
