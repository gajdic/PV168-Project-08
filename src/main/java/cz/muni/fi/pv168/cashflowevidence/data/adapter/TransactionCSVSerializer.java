package cz.muni.fi.pv168.cashflowevidence.data.adapter;

import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionType;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


// TODO: Add tests
public class TransactionCSVSerializer implements TransactionSerializer
{
    public Transaction deserialize(String csvLine) throws InvalidCSVFormatException {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 7) {
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
        return transaction.getName() + ',' +
                transaction.getDescription() + ',' +
                transaction.getAmount() + ',' +
                transaction.getDateTime() + ',' +
                transaction.getCategory().getName() + ',' +
                transaction.getCategory().getColorString() + ',' +
                transaction.getTransactionType();
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

    private Category convertCategory(String categoryName, String categoryColor) throws InvalidCSVFormatException {
        // TODO: Update when data layer exists
        Category category = new Category(categoryName, Color.decode(categoryColor));
        return category;
    }

    private TransactionType convertType(String name) {
        return TransactionType.valueOf(name);

    }

}
