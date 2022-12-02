package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import java.awt.*;
import java.time.LocalDateTime;

public class ExportImportTestData {
    private final Category category = new Category("category", Color.RED);
    private final Transaction transactionNullCategory = new Transaction(
            TransactionType.Outgoing,
            "nákup v Lidlu",
            "popis",
            100,
            LocalDateTime.of(2020, 12, 12, 12, 12),
            null
    );

    private final Transaction transactionRedCategory = new Transaction(
            TransactionType.Outgoing,
            "nákup v Lidlu",
            "popis",
            100,
            LocalDateTime.of(2020, 12, 12, 12, 12),
            category
    );

    public Transaction getTransactionRedCategory() {
        return transactionRedCategory;
    }

    public Transaction getTransactionNullCategory() {
        return transactionNullCategory;
    }
}
