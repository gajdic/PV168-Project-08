package cz.muni.fi.pv168.cashflowevidence.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionType;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class TransactionAdapter extends TypeAdapter<Transaction> {
    @Override
    public void write(JsonWriter writer, Transaction transaction) throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value(transaction.getName());
        writer.name("description");
        writer.value(transaction.getDescription());
        writer.name("amount");
        writer.value(transaction.getAmount());
        writer.name("date");
        writer.value(transaction.getDateTime().toString());
        writer.name("category_name");
        writer.value(transaction.getCategory().getName());
        writer.name("category_color");
        writer.value("#"+Integer.toHexString(transaction.getCategory().getColor().getRGB()).substring(2));
        writer.name("type");
        writer.value(transaction.getTransactionType().toString());
        writer.endObject();
    }

    @Override
    public Transaction read(JsonReader reader) throws IOException {
        Transaction transaction = new Transaction();
        reader.beginObject();
        String fieldName = null;
        String categoryName = "";

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }

            if ("name".equals(fieldName)) {
                token = reader.peek();
                transaction.setName(reader.nextString());
            }

            if("description".equals(fieldName)) {
                token = reader.peek();
                transaction.setDescription(reader.nextString());
            }

            if ("amount".equals(fieldName)) {
                token = reader.peek();
                transaction.setAmount(reader.nextDouble());
            }

            if ("date".equals(fieldName)) {
                token = reader.peek();
                transaction.setDateTime(LocalDateTime.parse(reader.nextString()));
            }

            if ("category_name".equals(fieldName)) {
                token = reader.peek();
                categoryName = reader.nextString();
            }

            if ("category_color".equals(fieldName)) {
                token = reader.peek();
                transaction.setCategory(new Category(categoryName, Color.decode(reader.nextString())));
            }

            if ("type".equals(fieldName)) {
                token = reader.peek();
                transaction.setTransactionType(TransactionType.valueOf(reader.nextString()));
            }
        }
        reader.endObject();
        return transaction;
    }
}
