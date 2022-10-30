package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import cz.muni.fi.pv168.cashflowevidence.adapter.TransactionAdapter;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonTransactionImporter implements TransactionImporter {
    private static final Type TRANSACTION_TYPE = new TypeToken<List<Transaction>>() {
    }.getType();

    public Collection<Transaction> importJson(String filePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionAdapter())
                .create();

        List<Transaction> transactions;

        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            transactions = gson.fromJson(reader, TRANSACTION_TYPE);
        }
        catch (IOException e) {
            //TODO: what to do on error
            throw new RuntimeException();
        }

        return transactions;
    }
}
