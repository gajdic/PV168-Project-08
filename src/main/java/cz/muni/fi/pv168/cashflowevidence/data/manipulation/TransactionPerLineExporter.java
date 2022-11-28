package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.adapter.TransactionSerializer;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class TransactionPerLineExporter implements TransactionExporter{

    private final TransactionSerializer serializer;

    public TransactionPerLineExporter(TransactionSerializer serializer){
        this.serializer = serializer;
    }

    @Override
    public void exportTransactions(Collection<Transaction> transactions, String filePath) {
        Logger.info("Exporting transactions to {}", filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            for (Transaction transaction: transactions) {
                writer.write(serializer.serialize(transaction));
                writer.newLine();
            }

            Logger.info("Exported {} records", transactions.size());
        }
        catch (IOException e) {
            Logger.error("Error while exporting transactions");
            throw new RuntimeException();
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.csv;
    }
}
