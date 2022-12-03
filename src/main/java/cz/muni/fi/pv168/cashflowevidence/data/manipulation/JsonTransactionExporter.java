package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionAdapter;
import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public final class JsonTransactionExporter implements TransactionExporter {

    public void exportTransactions(Collection<Transaction> transactions, String filePath) throws ExportFailedException {
        Logger.info("Exporting transactions to {}", filePath);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionAdapter())
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try (var writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write(gson.toJson(transactions));
        }
        catch (IOException e) {
            Logger.error("Error while exporting transactions");
            throw new ExportFailedException(e);
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.json;
    }
}
