package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.muni.fi.pv168.cashflowevidence.adapter.TransactionAdapter;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public final class JsonTransactionExporter implements TransactionExporter {

    public void exportJson(Collection<Transaction> transactions, String filePath){
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
            //TODO: what to do on error
            throw new RuntimeException();
        }
    }
}
