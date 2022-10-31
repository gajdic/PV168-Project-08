package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionSerializer;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class TransactionPerLineImporter implements TransactionImporter{

    private final TransactionSerializer serializer;

    public TransactionPerLineImporter (TransactionSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public Collection<Transaction> importRecords(String filePath) throws InvalidImportException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            List<Transaction> transactions = reader.lines()
                    .map(csvLine -> {
                        try {
                            return serializer.deserialize(csvLine);
                        } catch (InvalidCSVFormatException e) {
                            throw new InvalidImportException(e);
                        }
                    })
                    .toList();
            if (!transactions.stream().allMatch(TransactionValidator::isValid)) {
                throw new InvalidImportException("The provided transactions are not valid");
            }
            return transactions;

        } catch (FileNotFoundException exception) {
            throw new InvalidImportException("File does not exist", exception);
        } catch (IOException exception) {
            throw new InvalidImportException("Unable to read file", exception);
        }

    }

    @Override
    public ImportFormat getFormat() {
        return ImportFormat.csv;
    }
}
