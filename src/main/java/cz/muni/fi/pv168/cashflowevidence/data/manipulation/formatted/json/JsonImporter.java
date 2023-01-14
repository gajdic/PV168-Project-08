package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionAdapter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class JsonImporter implements BatchImporter {

    private static final Format FORMAT = new Format("JSON", List.of("json"));

    private static final Type TRANSACTION_TYPE = new TypeToken<List<Transaction>>() {
    }.getType();

    private final Repository<Category> categoryRepository;

    public JsonImporter(Repository<Category> categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public Batch importBatch(String filePath) {
        var categories = new HashMap<String, Category>();

        Logger.info("Loading transactions from {}", filePath);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionAdapter(categoryRepository))
                .create();

        List<Transaction> transactions;

        TransactionValidator validator = new TransactionValidator();

        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            transactions = gson.fromJson(reader, TRANSACTION_TYPE);
        } catch (IOException ex) {
            Logger.error("Error while loading transactions");
            throw new DataManipulationException("Unable to read file", ex);
        } catch (JsonSyntaxException ex) {
            Logger.error("Error while loading transactions, the format of provided file is not a valid json");
            throw new InvalidImportException("Invalid json format", ex);
        }

        int idx = 0;
        for (Transaction transaction: transactions) {
            idx++;
            if (!validator.isValidTransaction(transaction)) {
                Logger.error("Transaction " + transaction + " has invalid format.");
                throw new InvalidImportException("Transaction on line " + idx + " has invalid format.");
            }
        }

        Logger.info("Loaded {} records", transactions.size());
        return new Batch(categories.values(), transactions);
    }
}
