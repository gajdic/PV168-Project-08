package cz.muni.fi.pv168.cashflowevidence.data.manipulation.old;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionAdapter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class JsonTransactionImporter implements TransactionImporter {

    private final Repository<Category> categoryRepository;
    public JsonTransactionImporter(Repository<Category> categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    private static final Type TRANSACTION_TYPE = new TypeToken<List<Transaction>>() {
    }.getType();


    public Collection<Transaction> importRecords(String filePath) throws InvalidImportException {
        Logger.info("Loading transactions from {}", filePath);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionAdapter(categoryRepository))
                .create();

        List<Transaction> transactions;

        TransactionValidator validator = new TransactionValidator();

        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            transactions = gson.fromJson(reader, TRANSACTION_TYPE);
        }
        catch (IOException e) {
            Logger.error("Error while loading transactions");
            throw new InvalidImportException(e);
        }

        for (Transaction transaction: transactions) {
            if (!validator.isValidTransaction(transaction))
                throw new InvalidImportException("Transaction " + transaction + " is not valid." + validator.getErrorMessage());
        }

        Logger.info("Loaded {} records", transactions.size());
        return transactions;
    }

    @Override
    public ImportFormat getFormat() {
        return ImportFormat.json;
    }
}
