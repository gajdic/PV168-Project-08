package cz.muni.fi.pv168.cashflowevidence.data.adapter;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;


// TODO: Add tests
public class TransactionCSVSerializer implements TransactionSerializer {

    Repository<Category> categoryRepository;
    public Transaction deserialize(String csvLine, Repository<Category> categoryRepository) throws InvalidCSVFormatException {
        this.categoryRepository = categoryRepository;
        String[] tokens = csvLine.split(",");
        if (tokens.length != 7) {
            JOptionPane.showMessageDialog(
                    null, "Format of the provided CSV file is invalid.\n\n Invalid Line: %s".formatted(csvLine));
            throw new InvalidCSVFormatException(
                    "The line: " + csvLine + " is not valid."
            );
        }

        String name = tokens[0];
        String desc = tokens[1];
        Double amount = convertAmount(tokens[2]);
        LocalDateTime dateTime = convertDate(tokens[3]);
        Category category = convertCategory(tokens[4], tokens[5]);
        TransactionType transactionType = convertType(tokens[6]);

        return new Transaction(transactionType, name, desc, amount, dateTime, category);
    }

    public String serialize(Transaction transaction) {
        String name = transaction.getName();
        String description = transaction.getDescription();
        double amount = transaction.getAmount();
        LocalDateTime date = transaction.getDateTime();
        String categoryName = null;
        String categoryColor = null;
        TransactionType type = transaction.getTransactionType();
        if (transaction.getCategory() != null) {
            categoryName = transaction.getCategory().getName();
            categoryColor = transaction.getCategory().getColorString();
        }
        return name + ',' + description + ',' + amount + ',' + date + ',' +
                categoryName + ',' + categoryColor + ',' + type;
    }

    private Double convertAmount(String token) throws InvalidCSVFormatException {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new InvalidCSVFormatException(
                    "The " + token + " is an invalid amount"
            );
        }
    }

    private LocalDateTime convertDate(String token) throws InvalidCSVFormatException {
        try {
            return LocalDateTime.parse(token);
        } catch (DateTimeParseException e) {
            throw new InvalidCSVFormatException(
                    "The " + token + " is an invalid date"
            );
        }
    }

    private Category convertCategory(String categoryName, String categoryColor) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> Objects.equals(c.getName(), categoryName) &&
                        Objects.equals(c.getColorString(), categoryColor))
                .findFirst()
                .orElse(null);
    }

    private TransactionType convertType(String name) {
        return TransactionType.valueOf(name);
    }
}
