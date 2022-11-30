package cz.muni.fi.pv168.cashflowevidence.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends TypeAdapter<Transaction> {

    private final Repository<Category> categoryRepository;

    public TransactionAdapter() {
        this.categoryRepository = null;
    }
    public TransactionAdapter(Repository<Category> categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
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
        if (transaction.getCategory() != null) {
            writer.value(transaction.getCategory().getName());
        } else {
            writer.value("");
        }
        writer.name("category_color");
        if (transaction.getCategory() != null) {
            writer.value(transaction.getCategory().getColorString());
        } else {
            writer.value("");
        }
        writer.name("type");
        writer.value(transaction.getTransactionType().toString());
        writer.endObject();
    }

    @Override
    public Transaction read(JsonReader reader) throws IOException {
        Transaction transaction = new Transaction();
        reader.beginObject();
        String fieldName = null;
        String categoryName = null;
        String categoryColor = null;

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
                transaction.setDate(LocalDateTime.parse(reader.nextString()));
            }

            if ("category_name".equals(fieldName)) {
                token = reader.peek();
                categoryName = reader.nextString();
            }

            if ("category_color".equals(fieldName)) {
                token = reader.peek();
                categoryColor = reader.nextString();
            }

            if ("type".equals(fieldName)) {
                token = reader.peek();
                transaction.setTransactionType(TransactionType.valueOf(reader.nextString()));
            }
        }
        transaction.setCategory(convertCategory(categoryName, categoryColor));
        reader.endObject();

        return transaction;
    }
    private Category convertCategory(String categoryName, String categoryColor) {
        assert categoryRepository != null;
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> Objects.equals(c.getName(), categoryName) &&
                        Objects.equals(c.getColorString(), categoryColor))
                .findFirst()
                .orElse(null);
    }
}
