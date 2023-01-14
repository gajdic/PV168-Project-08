package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.FormatMapping;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DaoExporter implements Exporter {

    private final Repository<Category> categoryRepository;
    private final Repository<Transaction> transactionRepository;
    private final FormatMapping<BatchExporter> exporters;
    private JTable transactionTable;

    public DaoExporter(Repository<Category> categoryRepository, Repository<Transaction> transactionRepository, Collection<BatchExporter> exporters) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.exporters = new FormatMapping<>(exporters);
        this.transactionTable = null;
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        List<Transaction> filteredTransactions = new ArrayList<>(Collections.emptyList());
        TransactionTableModel model = (TransactionTableModel) transactionTable.getModel();
        for (int i = 0; i < transactionTable.getRowCount(); i++) {
            filteredTransactions.add(model.getEntity(transactionTable.convertRowIndexToModel(i)));
        }

        var batch = new Batch(categoryRepository.findAll(), filteredTransactions);

        try {
            exporter.exportBatch(batch, filePath);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    null, "Something went wrong while exporting your transactions.");
            throw new BatchOperationException("Export failed", ex);
        }
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var exporter = exporters.findByExtension(extension);
        if (exporter == null) {
            JOptionPane.showMessageDialog(
                    null, "Something went wrong while exporting your transactions.");
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return exporter;
    }

    public void setTransactionTable(JTable table) {
        transactionTable = table;
    }
}
