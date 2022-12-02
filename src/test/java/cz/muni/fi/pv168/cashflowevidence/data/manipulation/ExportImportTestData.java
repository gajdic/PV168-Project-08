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

    private final String transactionRedCategoryJson = """
            {
            "name": "nákup v Lidlu",
            "description": "popis",
            "amount": 100.0,
            "date": "2020-12-12T12:12",
            "category_name": "category",
            "category_color": "#ff0000",
            "type": "Outgoing"
            }
            """;

    private final String transactionNullCategoryJson = """
            {
            "name": "nákup v Lidlu",
            "description": "popis",
            "amount": 100.0,
            "date": "2020-12-12T12:12",
            "category_name": "",
            "category_color": "",
            "type": "Outgoing"
            }
            """ ;

    private final String transactionRedCategoryCsv =
            "nákup v Lidlu,popis,100.0,2020-12-12T12:12,category,#ff0000,Outgoing\n";

    private final String transactionNullCategoryCsv =
            "nákup v Lidlu,popis,100.0,2020-12-12T12:12,null,null,Outgoing\n";


    public Category getRedCategory() {
        return category;
    }

    public Transaction getTransactionRedCategory() {
        return transactionRedCategory;
    }

    public Transaction getTransactionNullCategory() {
        return transactionNullCategory;
    }

    public String getTransactionRedCategoryJson() {
        return "[" + transactionRedCategoryJson + "]" ;
    }

    public String getTransactionNullCategoryJson() {
        return "[" + transactionNullCategoryJson + "]";
    }

    public String multiTransactionsJson(){
        return "[" + transactionRedCategoryJson + "\n,\n" + transactionNullCategoryJson + "]";
    }


    public String getTransactionRedCategoryCsv() {
        return transactionRedCategoryCsv;
    }

    public String getTransactionNullCategoryCsv() {
        return transactionNullCategoryCsv;
    }

    public String multiTransactionCsv(){
        return transactionRedCategoryCsv + transactionNullCategoryCsv;
    }
}
