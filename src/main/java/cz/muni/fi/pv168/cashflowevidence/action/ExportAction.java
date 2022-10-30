package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionExporter;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class ExportAction extends AbstractAction {
    private final JTable table;
    private final TransactionExporter exporter;

    public ExportAction(JTable table, TransactionExporter exporter) {
        super("Export", UIManager.getIcon("FileChooser.fileIcon"));
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

        int dialogResult = fileChooser.showSaveDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File exportFile = fileChooser.getSelectedFile();
            var transactionTableModel = (TransactionTableModel) table.getModel();
            //TODO: switch between which export option e.g json or csv
            exporter.exportJson(transactionTableModel.getEntities(), exportFile.getAbsolutePath());
        }
    }
}