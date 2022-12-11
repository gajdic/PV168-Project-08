package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchExporter;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class ExportAction extends AbstractAction {
    private final JTable table;
    private final BatchExporter exporter;

    public ExportAction(JTable table, BatchExporter exporter) {
        super("Export " + exporter.getFormat().getName(), UIManager.getIcon("FileChooser.fileIcon"));
        this.table = Objects.requireNonNull(table);
        this.exporter = Objects.requireNonNull(exporter);
        putValue(SHORT_DESCRIPTION, "Exports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter(exporter.getFormat().getName(),
                exporter.getFormat().getName()));
        fileChooser.setSelectedFile(new File("export." + exporter.getFormat().getName().toLowerCase()));

        int dialogResult = fileChooser.showSaveDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File exportFile = fileChooser.getSelectedFile();
            var transactionTableModel = (TransactionTableModel) table.getModel();
            var categories = transactionTableModel.getEntities().stream().map(Transaction::getCategory).toList();
            var batch = new Batch(categories, transactionTableModel.getEntities());

            exporter.exportBatch(batch, exportFile.getAbsolutePath());
        }
    }

    @Override public String toString() {
        return this.exporter.getFormat().getName();
    }
}